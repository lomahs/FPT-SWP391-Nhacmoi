package fpt.swp391.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    @Id
    @GeneratedValue(generator = "id_gen_artist")
    @GenericGenerator(name = "id_gen_artist", parameters = @Parameter(name = "prefix", value = "ART"), strategy = "fpt.swp391.utils.IdGenerator")
    private String artist_id;

    @Column(unique = true, columnDefinition = "nvarchar(20)")
    private String artist_name;

    private String image;

    @ManyToMany(mappedBy = "artist")
    @JsonBackReference
    private Set<Song> listSong = new HashSet<>();

}