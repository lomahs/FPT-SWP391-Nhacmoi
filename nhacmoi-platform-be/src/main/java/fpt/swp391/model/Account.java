package fpt.swp391.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "account_role", joinColumns = @JoinColumn(name = "account_name"), inverseJoinColumns = @JoinColumn(name = "role_name"))
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "account")
    @JsonBackReference
    private User user;

    public List<GrantedAuthority> getAuthorities() {

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole_name()))
                .collect(Collectors.toList());
    }

    public Account(String account_name, String password) {
        this.account_name = account_name;
        this.password = password;
    }
}