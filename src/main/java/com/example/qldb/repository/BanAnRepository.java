package com.example.qldb.repository;


import com.example.qldb.entity.BanAn;
import com.example.qldb.entity.DatBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.websocket.server.PathParam;
import java.util.Date;
import java.util.List;


@Repository
public interface BanAnRepository extends JpaRepository<BanAn, Integer> {
    @Query("select u from BanAn u where u.isActive = true")
    List<BanAn> findByActiveTrue();

    @Query("select u from BanAn u where u.isActive = true and u.gioBD = :gioBD and u.gioKT = :gioKT and u.loaiBanAn = :loaiBanAn")
    BanAn findByGioBDAndGioKTAndActiveIsTrueAndLoaiBanAn(@Param("gioBD") String gioBD, @Param("gioKT") String gioKT, @Param("loaiBanAn") Integer loaiBanAn);

    @Query("select distinct u.loaiBanAn from BanAn u where u.isActive = true")
    List<Integer> findLoaiBanAn();

}
