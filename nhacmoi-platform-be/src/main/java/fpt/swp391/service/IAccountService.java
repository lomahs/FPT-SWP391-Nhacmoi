package fpt.swp391.service;

import fpt.swp391.model.Account;

public interface IAccountService {
    Account saveAccount(Account account);

    boolean deleteAccount(String id);
}