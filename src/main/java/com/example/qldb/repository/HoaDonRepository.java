package com.example.qldb.repository;

import com.example.qldb.entity.HoaDon;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonRepository extends CrudRepository<HoaDon, Integer> {
    HoaDon findHoaDonByDatBan_IdDatBan(Integer idDB);
}
