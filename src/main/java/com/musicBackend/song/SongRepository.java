package com.musicBackend.song;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SongRepository extends ReactiveCrudRepository<Song, Long> {
}

