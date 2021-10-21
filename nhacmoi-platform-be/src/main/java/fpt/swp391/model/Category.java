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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(generator = "id_gen")
    @GenericGenerator(name = "id_gen", parameters = @Parameter(name = "prefix", value = "CAT"), strategy = "fpt.swp391.utils.IdGenerator")
    private String category_id;

    @Column(unique = true, columnDefinition = "nvarchar(20)")
    private String category_name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Song_Category", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "song_id"))
    @JsonBackReference
    private List<Song> listSong;

}