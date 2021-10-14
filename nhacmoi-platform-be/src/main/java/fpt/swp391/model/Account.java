package fpt.swp391.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

    @Id
    @Column(columnDefinition = "varchar(20)")
    private String account_name;

    @Column(columnDefinition = "varchar(30)")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_name")
    private Role role;

    @OneToOne(mappedBy = "account")
    @JsonIgnore
    private User user;
}