package fpt.swp391.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(generator = "id_gen")
    @GenericGenerator(name = "id_gen", parameters = @Parameter(name = "prefix", value = "U"), strategy = "fpt.swp391.utils.IdGenerator")
    private String user_id;

    @Column(columnDefinition = "nvarchar(30)")
    private String user_name;

    @Column(columnDefinition = "varchar(50)")
    private String user_email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_name")
    private Account account;

    private char sex;

    private LocalDate birthday;

    @OneToMany(mappedBy = "user_added", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Song> listSong;

    @OneToMany(mappedBy = "user_created_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Playlist> listPlaylist;

}