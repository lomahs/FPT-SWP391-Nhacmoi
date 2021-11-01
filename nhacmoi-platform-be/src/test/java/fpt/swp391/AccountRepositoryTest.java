package fpt.swp391;

import fpt.swp391.model.Account;
import fpt.swp391.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindUserByEmail() {
        String accountName = "loi";

        Account user = repo.findByAccountName(accountName).orElse(null);

        assertThat(user).isNotNull();
    }
}