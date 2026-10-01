package com.musicBackend.song;



import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Song> createSong(@Valid @RequestBody Song song) {
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
            @Valid @RequestBody Song song) {
        return songService.updateSong(song, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteSong(@PathVariable Long id) {
        return songService.deleteSongById(id);
    }
}