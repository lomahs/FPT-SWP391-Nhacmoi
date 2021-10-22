package fpt.swp391.service;

import fpt.swp391.model.Account;

public interface IAccountService {
    boolean saveAccount(Account account);

    boolean deleteAccount(String id);
}
