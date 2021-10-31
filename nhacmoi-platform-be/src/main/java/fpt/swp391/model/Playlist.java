package fpt.swp391.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Playlist {

    @Id
    @GeneratedValue(generator = "id_gen_playlist")
    @GenericGenerator(name = "id_gen_playlist", parameters = @Parameter(name = "prefix", value = "PL"), strategy = "fpt.swp391.utils.IdGenerator")
    private String playlist_id;

    @ManyToOne
    @NotNull(message = "user is empty")
    private User owner;

    @Column(columnDefinition = "nvarchar(50)")
    @Size(max = 50, min = 1, message = "name must be at least 1 character, maximum 50 characters")
    @NotBlank(message = "name is only whitespace")
    private String playlist_name;

    private long playlist_duration;

    private String playlist_image;

    @ManyToMany
    @JoinTable(name = "playlist_song", joinColumns = @JoinColumn(name = "playlist_id"), inverseJoinColumns = @JoinColumn(name = "song_id"))
    private Set<Song> listSongs = new HashSet<>();

    public void addSong(Song song) {
        listSongs.add(song);
        song.getListPlaylists().add(this);
    }

    public void removeSong(Song song) {
        listSongs.remove(song);
        song.getListPlaylists().remove(this);
    }

    public Playlist(String name, User owner) {
        playlist_name = name;
        this.owner = owner;
    }
}