package fpt.swp391.service.impl;

import fpt.swp391.model.Account;
import fpt.swp391.repository.AccountRepository;
import fpt.swp391.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;

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
    public boolean checkLogin(Account account) {
        Optional<Account> accountOptional = accountRepository.findByAccountName(account.getAccount_name());

        return accountOptional.map(acc -> acc.getPassword().equals(account.getPassword()))
                .orElse(false);
    }
}