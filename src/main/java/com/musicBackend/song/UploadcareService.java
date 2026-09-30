package com.musicBackend.song;



import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
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

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.asyncPart(
                "file",
                file.content(),
                DataBuffer.class
        ).filename(file.filename());

        builder.part("UPLOADCARE_PUB_KEY", publicKey);

        return webClientBuilder
                .baseUrl("https://upload.uploadcare.com")
                .build()
                .post()
                .uri("/base/")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(UploadcareResponse.class)
                .map(response ->
                        "https://ucarecdn.com/"
                                + response.getFile()
                                + "/"
                );
    }
}
