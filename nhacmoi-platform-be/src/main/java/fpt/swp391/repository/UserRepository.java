package fpt.swp391.repository;

import fpt.swp391.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "select u from User as u where u.account.account_name = :accountName")
    User getUserByAccountName(String accountName);
}