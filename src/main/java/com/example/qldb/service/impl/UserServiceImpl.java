package com.example.qldb.service.impl;


import com.example.qldb.entity.User;
import com.example.qldb.repository.UserRepository;
import com.example.qldb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service //giúp Spring xác định lớp hiện tại là một Service và tạo một bean cho lớp đó
public class UserServiceImpl implements UserService {
    //inject(tiêm) UserResponsitory vào UserServiceImpl
    @Autowired private UserRepository userRepository;
    @Override
    public List<User> getAllUser() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public List<User> findUsersByHoTen(String hoTen) {
        return (List<User>) userRepository.findUsersByHoTen(hoTen);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User findUserBySdt(String sdt) {
        return userRepository.findUserBySdt(sdt);
    }


}
