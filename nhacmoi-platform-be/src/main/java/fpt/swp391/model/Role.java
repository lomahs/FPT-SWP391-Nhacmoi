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
    @Column(length = 20)
    private String role_name;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<Account> listAccount;
}