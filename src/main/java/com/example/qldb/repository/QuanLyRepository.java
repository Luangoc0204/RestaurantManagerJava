package com.example.qldb.repository;


import com.example.qldb.entity.QuanLy;
import com.example.qldb.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuanLyRepository extends CrudRepository<QuanLy, Integer> {
    QuanLy findQuanLyByUser(User user);
}
