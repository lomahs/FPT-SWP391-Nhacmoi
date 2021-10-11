package fpt.swp391.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(generator = "id_gen")
    @GenericGenerator(name = "id_gen",
            parameters = @Parameter(name = "prefix", value = "U"),
            strategy = "fpt.swp391.utils.IdGenerator")
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

    @OneToMany(mappedBy = "user_added", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Song> listSong;

    @OneToMany(mappedBy = "user_created_id", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Playlist> listPlaylist;
}