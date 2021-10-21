package fpt.swp391.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Song {

        @Id
        @GeneratedValue(generator = "id_gen")
        @GenericGenerator(name = "id_gen", parameters = @Parameter(name = "prefix", value = "SO"), strategy = "fpt.swp391.utils.IdGenerator")
        private String song_id;

        @Column(columnDefinition = "nvarchar(50)")
        private String song_name;

        @ManyToMany(mappedBy = "listSong", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private List<Category> categories;

        private String path;

        @ManyToOne
        @JoinColumn(name = "user_added_id")
        private User user_added;

        @ManyToMany
        @JoinTable(name = "song_artist", joinColumns = @JoinColumn(name = "song_id"), inverseJoinColumns = @JoinColumn(name = "artist_id"))
        private List<Artist> artist;

        private String song_image;

        private int song_duration;

        private LocalDate date_added;

        private long stream_count;

        @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JoinTable(name = "song_playlist", joinColumns = @JoinColumn(name = "song_id"), inverseJoinColumns = @JoinColumn(name = "playlist_id"))
        private List<Playlist> listPlaylists;

        public void updateCategory(List<Category> list) {
                categories = new ArrayList<>(list);
        }
}
