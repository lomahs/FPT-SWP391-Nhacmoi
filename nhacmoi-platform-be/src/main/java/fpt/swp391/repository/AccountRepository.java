package fpt.swp391.repository;

import fpt.swp391.model.Account;
import fpt.swp391.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    @Query(value = "select acc from Account as acc where acc.account_name = :accountName")
    Optional<Account> findByAccountName(String accountName);

    @Query("SELECT u.account FROM User as u where u.user_email= :email")
    Account getAccountByEmail(String email);

}