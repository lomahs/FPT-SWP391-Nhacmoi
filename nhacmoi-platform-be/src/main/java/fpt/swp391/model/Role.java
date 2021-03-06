package fpt.swp391.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {

    @Id
    @Column(length = 20)
    private String role_name;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private Set<Account> listAccount = new HashSet<>();

    public Role(String role_name) {
        this.role_name = role_name;
    }
}