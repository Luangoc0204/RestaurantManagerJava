package com.example.qldb.service.impl;


import com.example.qldb.entity.Account;
import com.example.qldb.repository.AccountRepository;
import com.example.qldb.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountImpl implements UserDetailsService, AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(username);
        if (account == null){
            System.out.println("User not found!");
            throw new UsernameNotFoundException("User not found");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(account.getRole());
        grantedAuthorities.add(grantedAuthority);
        UserDetails userDetails = new User(account.getUsername(), account.getPassword(), grantedAuthorities);
        return userDetails;
    }

    @Override
    public List<Account> getAllAccount() {
        return (List<Account>) accountRepository.findAll();
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Integer idAccount) {
        accountRepository.deleteById(idAccount);
    }

    @Override
    public Account findAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    @Override
    public Account findAccountByUser(com.example.qldb.entity.User user) {
        return accountRepository.findAccountByUser(user);
    }

    @Override
    public Optional<Account> findAccountById(Integer id) {
        return accountRepository.findById(id);
    }
}
