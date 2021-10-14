package fpt.swp391.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fpt.swp391.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

}
