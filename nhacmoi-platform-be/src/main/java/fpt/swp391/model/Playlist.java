package fpt.swp391.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Playlist {

    @Id
    @TableGenerator(
            name = "clazz_gen",
            table = "id_gen",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val",
            allocationSize = 2
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "clazz_gen")
    private int playlist_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "nvarchar(50)")
    private String playlist_name;

    private long playlist_duration;

    private String playlist_image;

    @ManyToMany(mappedBy = "listPlaylists", fetch = FetchType.LAZY)
    private List<Song> listSongs;
}