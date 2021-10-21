package fpt.swp391.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Playlist {

    @Id
    @GeneratedValue(generator = "id_gen")
    @GenericGenerator(name = "id_gen", parameters = @Parameter(name = "prefix", value = "PL"), strategy = "fpt.swp391.utils.IdGenerator")
    private String playlist_id;

    @ManyToOne
    @JoinColumn(name = "user_created_id")
    @JsonBackReference
    private User user_created_id;

    @Column(columnDefinition = "nvarchar(50)")
    private String playlist_name;

    private long playlist_duration;

    private String playlist_image;

    @ManyToMany(mappedBy = "listPlaylists", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Song> listSongs;
}