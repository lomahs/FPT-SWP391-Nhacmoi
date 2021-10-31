package fpt.swp391.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Song {

    @Id
    @GeneratedValue(generator = "id_gen_song")
    @GenericGenerator(name = "id_gen_song", parameters = @Parameter(name = "prefix", value = "SO"), strategy = "fpt.swp391.utils.IdGenerator")
    private String song_id;

    @Column(columnDefinition = "nvarchar(50)")
    @Size(max = 50, min = 1, message = "name must be at least 1 character, maximum 50 characters")
    @NotBlank(message = "name is only whitespace")
    private String song_name;

    @ManyToMany
    @JoinTable(name = "song_category", joinColumns = @JoinColumn(name = "song_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    // @JsonBackReference
    @NotNull(message = "categories is empty")
    private Set<Category> categories = new HashSet<>();

    private String path;

    @ManyToOne
    @JsonBackReference(value = "adder")
    @NotNull(message = "user is empty")
    private User adder;

    @ManyToMany
    @JoinTable(name = "song_artist", joinColumns = @JoinColumn(name = "song_id"), inverseJoinColumns = @JoinColumn(name = "artist_id"))
    @NotNull(message = "artist is empty")
    private Set<@Valid Artist> artist = new HashSet<>();

    private String song_image;

    private int song_duration;

    private LocalDate date_added = LocalDate.now();

    private long stream_count = 0;

    @ManyToMany(mappedBy = "listSongs")
    @JsonBackReference
    private Set<Playlist> listPlaylists = new HashSet<>();

    public void removeCategory(Category category) {
        categories.remove(category);
        category.getListSong().remove(this);
    }
}