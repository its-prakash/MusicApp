package com.musicBackend.song;





import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Artist is required")
    private String artist;

    @NotBlank(message = "Album is required")
    private String album;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotBlank(message = "Audio URL is required")
    private String audioUrl;

    @NotBlank(message = "Cover image URL is required")
    private String coverImageUrl;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be greater than 0")
    private Integer duration;
}