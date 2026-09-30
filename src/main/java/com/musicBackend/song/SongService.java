package com.musicBackend.song;



import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface SongService {

    Mono<Song> createSong(Song song);
    Mono<Song> findSongById(Long id);
    Mono<Void> deleteSongById(Long id);
    Flux<Song> findAllSong();

//    Mono<Song> updateSong(Song song);

    Mono<Song> updateSong(Song song, Long id);
}
