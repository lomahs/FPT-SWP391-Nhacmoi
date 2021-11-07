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
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(generator = "id_gen_user")
    @GenericGenerator(name = "id_gen_user", parameters = @Parameter(name = "prefix", value = "U"), strategy = "fpt.swp391.utils.IdGenerator")
    private String user_id;

    @Column(columnDefinition = "nvarchar(30)")
    @Size(max = 30, min = 1, message = "name must be at least 1 character, maximum 30 characters")
    @NotBlank(message = "name is only whitespace")
    private String user_name;

    @Column(columnDefinition = "varchar(50)")
    @Size(max = 50, message = "maximum 50 characters")
    @Email(message = "invalid email")
    private String user_email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_name")
    @NotNull(message = "account is empty")
    @Valid
    private Account account;

    private char sex;

    @Past(message = "date is in future")
    private LocalDate birthday;
    
    private String user_image;

    private Boolean enabled = false;

    @OneToMany(mappedBy = "adder")
    @JsonBackReference(value = "adder")
    private Set<Song> listSong;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @JsonBackReference(value = "owner")
    private Set<Playlist> listPlaylist;

}