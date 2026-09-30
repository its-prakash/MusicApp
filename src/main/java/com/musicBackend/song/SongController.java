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

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Song> createSong(

            @RequestPart("title") String title,

            @RequestPart("artist") String artist,

            @RequestPart("album") String album,

            @RequestPart("genre") String genre,

            @RequestPart("duration") Integer duration,

            @RequestPart("audio") FilePart audio,

            @RequestPart("cover") FilePart cover
    ) {

        Song song = new Song();

        song.setTitle(title);
        song.setArtist(artist);
        song.setAlbum(album);
        song.setGenre(genre);
        song.setDuration(duration);

        return songService.createSong(song, audio, cover);
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