package com.example.qldb.repository;


import com.example.qldb.entity.NhanVien;
import com.example.qldb.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhanVienRepository extends CrudRepository<NhanVien, Integer> {
    NhanVien findNhanVienByUser(User user);

}
