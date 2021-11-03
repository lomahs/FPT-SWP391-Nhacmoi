package fpt.swp391.repository;

import fpt.swp391.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "select u from User as u where u.account.account_name = :accountName")
    User getUserByAccountName(String accountName);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "set a.enabled = true where a.user_email = ?1")
    int enabledAppUser(String email);
}