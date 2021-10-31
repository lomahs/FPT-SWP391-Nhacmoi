package fpt.swp391.repository;

import fpt.swp391.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    @Query(value = "select acc from Account as acc where acc.account_name = :accountName")
    Optional<Account> findByAccountName(String accountName);
}