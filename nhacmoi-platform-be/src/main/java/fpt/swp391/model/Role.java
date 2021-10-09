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
public class Role {

    @Id
    @TableGenerator(
            name = "clazz_gen",
            table = "id_gen",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val",
            allocationSize = 2
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "clazz_gen")
    private int role_id;

    @Column(length = 20)
    private String role_name;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<Account> listAccount;
}