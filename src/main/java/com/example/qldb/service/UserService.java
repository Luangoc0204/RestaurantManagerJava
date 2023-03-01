package com.example.qldb.service;


import com.example.qldb.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> getAllUser();

    List<User> findUsersByHoTen(String hoTen);

    void saveUser(User user);

    void deleteUser(Integer id);

    Optional<User> findUserById(Integer id);
    User findUserBySdt(String sdt);
}
