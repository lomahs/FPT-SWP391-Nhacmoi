package fpt.swp391.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fpt.swp391.model.Account;
import fpt.swp391.repository.AccountRepository;
import fpt.swp391.service.IAccountService;

@Service
public class AccountServiceImpl implements IAccountService{

  @Autowired
  private AccountRepository accountRepository;

  @Override
  public boolean saveAccount(Account account) {
    if (account == null) {
      return false;
    }
    accountRepository.save(account);
    return true;
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
  
}
