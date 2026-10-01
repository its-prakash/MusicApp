package com.musicBackend.song;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    @Override
    public Mono<Song> createSong(Song song) {
        return songRepository.save(song);
    }

    @Override
    public Mono<Song> findSongById(Long id) {
        return songRepository.findById(id);
    }

    @Override
    public Mono<Void> deleteSongById(Long id) {
        return songRepository.deleteById(id);
    }

    @Override
    public Flux<Song> findAllSong() {
        return songRepository.findAll();
    }



    @Override
    public Mono<Song> updateSong(Song song, Long id) {
        return songRepository.findById(id)
                .flatMap(exitingSong -> {
                    exitingSong.setTitle(song.getTitle());
                    exitingSong.setArtist(song.getArtist());
                    exitingSong.setAlbum(song.getAlbum());
                    exitingSong.setGenre(song.getGenre());
                    exitingSong.setAudioUrl(song.getAudioUrl());
                    exitingSong.setCoverImageUrl(song.getCoverImageUrl());
                    exitingSong.setDuration(song.getDuration());
                    return songRepository.save(exitingSong);


                });
    }
}
