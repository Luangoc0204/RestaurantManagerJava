package com.example.qldb.repository;


import com.example.qldb.entity.BanAn;
import com.example.qldb.entity.DatBan;
import com.example.qldb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DatBanRepository extends JpaRepository <DatBan, Integer> {
    @Query("select u from DatBan u where u.user = :user order by u.ngayDatBan desc")
    List<DatBan> findAllByUserOrOrderByNgayDatBan(@Param("user") User user);

    @Query("select u from DatBan u where u.isActive = true and u.banAn.idBanAn = :idBanAn and u.ngayDatBan = :date")
    List<DatBan> findAllByBanAn_IdBanAnAndNgayDatBan(@Param("idBanAn") Integer idBanAn,@Param("date") Date date);

    List<DatBan> findAllByNgayDatBan(Date date);

}
