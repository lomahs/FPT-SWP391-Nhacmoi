package fpt.swp391.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

    @Id
    @Column(columnDefinition = "varchar(20)")
    @Pattern(regexp = "^(?=.{6,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$", message = "username is 6-20 characters long, no _ or . at the beginning, no __ or _. or ._ or .. inside, no _ or . at the end")
    private String account_name;

    @Column(columnDefinition = "varchar(30)")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,30}$", message = "Minimum eight and maximum 30 characters, at least one uppercase letter, one lowercase letter, one number and one special character")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_name")
    private Role role;

    @OneToOne(mappedBy = "account")
    @JsonBackReference
    private User user;
}