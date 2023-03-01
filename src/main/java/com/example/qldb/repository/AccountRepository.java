package com.example.qldb.repository;


import com.example.qldb.entity.Account;
import com.example.qldb.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account,Integer> {
    Account findAccountByUsername(String username);
    Account findAccountByUser(User user);

}
