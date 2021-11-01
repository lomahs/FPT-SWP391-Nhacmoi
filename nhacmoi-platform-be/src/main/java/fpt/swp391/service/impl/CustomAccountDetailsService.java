package fpt.swp391.service.impl;

import fpt.swp391.model.Account;
import fpt.swp391.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomAccountDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountService;

    @Override
    public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException {
        Account account = accountService.findByAccountName(accountName).orElse(null);
        if (account == null) {
            throw new UsernameNotFoundException("Account not found");
        }
        return new CustomAccountDetails(account);
    }
}