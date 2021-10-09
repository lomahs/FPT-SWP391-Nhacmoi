package fpt.swp391.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @TableGenerator(
            name = "clazz_gen",
            table = "id_gen",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val",
            allocationSize = 2
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "clazz_gen")
    private int user_id;

    @Column(columnDefinition = "nvarchar(30)")
    private String user_name;

    @Column(columnDefinition = "varchar(50)")
    private String user_email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    private char sex;

    private LocalDate birthday;

    @OneToMany(mappedBy = "user_added", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Song> listSong;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Playlist> listPlaylist;
}