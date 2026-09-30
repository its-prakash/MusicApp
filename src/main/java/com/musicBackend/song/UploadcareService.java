package com.musicBackend.song;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UploadcareService {

    private final WebClient.Builder webClientBuilder;

    @Value("${uploadcare.public-key}")
    private String publicKey;

    public Mono<String> uploadFile(FilePart file) {
        MediaType contentType = file.headers().getContentType() != null
                ? file.headers().getContentType()
                : MediaType.APPLICATION_OCTET_STREAM;

        return DataBufferUtils.join(file.content())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    System.out.println("File bytes collected: " + bytes.length + ", filename: " + file.filename());
                    return bytes;
                })
                .flatMap(bytes -> {
                    MultipartBodyBuilder builder = new MultipartBodyBuilder();
                    builder.part("file", new ByteArrayResource(bytes) {
                        @Override
                        public String getFilename() { return file.filename(); }
                    }).contentType(contentType);
                    builder.part("UPLOADCARE_PUB_KEY", publicKey);
                    builder.part("UPLOADCARE_STORE", "1");
                    System.out.println("Sending to Uploadcare with key: " + publicKey);
                    return webClientBuilder
                            .baseUrl("https://upload.uploadcare.com")
                            .build()
                            .post()
                            .uri("/base/")
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .body(BodyInserters.fromMultipartData(builder.build()))
                            .retrieve()
                            .onStatus(
                                    status -> status.is4xxClientError() || status.is5xxServerError(),
                                    response -> response.bodyToMono(String.class)
                                            .flatMap(body -> Mono.error(new RuntimeException("Uploadcare error: " + body)))
                            )
                            .bodyToMono(UploadcareResponse.class)
                            .map(response -> "https://ucarecdn.com/" + response.getFile() + "/");
                });
    }
}