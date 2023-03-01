package com.example.qldb.repository;

import com.example.qldb.entity.ChonMon;
import com.example.qldb.entity.ChonMonKey;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChonMonRepository extends CrudRepository<ChonMon, ChonMonKey> {
    List<ChonMon> findAllByDatBan_IdDatBan(Integer idDB);
    ChonMon findChonMonByDatBan_IdDatBanAndAndMonAn_IdMonAn(Integer idDB, Integer idMA);
}