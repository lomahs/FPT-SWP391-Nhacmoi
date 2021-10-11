package fpt.swp391.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    @Id
    @GeneratedValue(generator = "id_gen")
    @GenericGenerator(name = "id_gen",
            parameters = @Parameter(name = "prefix", value = "ART"),
            strategy = "fpt.swp391.utils.IdGenerator")
    private String artist_id;

    @Column(unique = true, columnDefinition = "nvarchar(20)")
    private String artist_name;

    @ManyToMany(mappedBy = "artist")
    private List<Song> listSong;
}