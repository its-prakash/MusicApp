package com.musicBackend.song;



import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.codec.multipart.FilePart;

public interface SongService {

    Mono<Song> createSong(Song song);
    Mono<String> uploadFile(FilePart file);
    Mono<Song> findSongById(Long id);
    Mono<Void> deleteSongById(Long id);
    Flux<Song> findAllSong();
    Mono<Song> updateSong(Song song, Long id);
}
