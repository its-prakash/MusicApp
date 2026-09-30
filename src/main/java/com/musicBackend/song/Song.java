package com.musicBackend.song;





import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("songs")
public class Song {

    @Id
    private Long id;

    private String title;

    private String artist;

    private String album;

    private String genre;

    private String audioUrl;

    private String coverImageUrl;

    private Integer duration;
}