package com.example.qldb.service;


import com.example.qldb.entity.Account;
import com.example.qldb.entity.User;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> getAllAccount();
    void saveAccount(Account account);
    void deleteAccount(Integer idAccount);
    Account findAccountByUsername(String username);
    Account findAccountByUser(User user);
    Optional<Account> findAccountById(Integer id);
}
