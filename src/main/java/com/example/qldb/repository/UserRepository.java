package com.example.qldb.repository;


import com.example.qldb.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findUsersByHoTen(String hoTen);
    User findUserBySdt(String sdt);
}
