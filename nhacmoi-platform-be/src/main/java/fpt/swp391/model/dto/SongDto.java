package fpt.swp391.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class SongDto {
    private String id;
    private String name;
    private Set<CategoryDto> categories;
    private String path;
    private UserDto adder;
    private Set<ArtistDto> artists;
    private String image;
    private int duration;
    private LocalDate dateAdded;
    private long stream_count;
}