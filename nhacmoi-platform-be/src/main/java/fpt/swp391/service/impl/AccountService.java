package fpt.swp391.service.impl;

import fpt.swp391.model.Account;
import fpt.swp391.repository.AccountRepository;
import fpt.swp391.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    public Account saveAccount(Account account) {

        return accountRepository.save(account);
    }

    @Override
    public boolean deleteAccount(String id) {
        Account acc = accountRepository.findById(id).orElse(null);
        if (acc != null) {
            accountRepository.delete(acc);
            return true;
        }
        return false;
    }

    @Override
    public Account loadUserByAccountName(String accountName) {

        return accountRepository.findByAccountName(accountName).orElse(null);
    }

    @Override
    public boolean checkLogin(Account account) throws Exception {
        Optional<Account> accountOptional = accountRepository.findByAccountName(account.getAccount_name());
        if(!userService.getUserByAccountName(account.getAccount_name()).getEnabled()){
            throw new Exception();
        }
        return accountOptional.map(acc -> passwordEncoder.matches(account.getPassword(), acc.getPassword()))
                .orElse(false);
    }
}