package com.example.qldb.repository;

import com.example.qldb.entity.KhachHang;
import com.example.qldb.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface KhachHangRepository extends CrudRepository<KhachHang, Integer> {
    KhachHang findKhachHangByIdKhachHang(Integer idKhachHang);
    KhachHang findKhachHangByUser(User user);
}
