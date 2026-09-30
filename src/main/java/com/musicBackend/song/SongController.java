package com.musicBackend.song;



import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> uploadFile(@RequestPart("file") FilePart file) {
        return songService.uploadFile(file)
                .doOnSuccess(url -> System.out.println("Upload success: " + url))
                .doOnError(e -> System.out.println("Upload error: " + e.getMessage()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Song> createSong(@RequestBody Song song) {
        return songService.createSong(song);
    }

    @GetMapping
    public Flux<Song> getAllSongs() {
        return songService.findAllSong();
    }

    @GetMapping("/{id}")
    public Mono<Song> getSongById(@PathVariable Long id) {
        return songService.findSongById(id);
    }

    @PutMapping("/{id}")
    public Mono<Song> updateSong(
            @PathVariable Long id,
            @RequestBody Song song) {

        return songService.updateSong(song, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteSong(@PathVariable Long id) {
        return songService.deleteSongById(id);
    }
}