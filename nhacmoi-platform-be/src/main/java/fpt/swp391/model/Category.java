package fpt.swp391.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(generator = "id_gen_cat")
    @GenericGenerator(name = "id_gen_cat", parameters = @Parameter(name = "prefix", value = "CAT"), strategy = "fpt.swp391.utils.IdGenerator")
    private String category_id;

    @Column(unique = true, columnDefinition = "nvarchar(20)")
    @Size(max = 20, min = 1, message = "name must be at least 1 character, maximum 20 characters")
    @NotBlank(message = "name is only whitespace")
    private String category_name;

    @ManyToMany(mappedBy = "categories")
    @JsonBackReference
    private Set<Song> listSong;

}