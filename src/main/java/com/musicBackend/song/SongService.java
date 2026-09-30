package com.musicBackend.song;



import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface SongService {

    Mono<Song> createSong
            (
            Song song,
            FilePart audio,
            FilePart image

           );
    Mono<Song> findSongById(Long id);
    Mono<Void> deleteSongById(Long id);
    Flux<Song> findAllSong();

//    Mono<Song> updateSong(Song song);

    Mono<Song> updateSong(Song song, Long id);
}
