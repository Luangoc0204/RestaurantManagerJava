package com.example.qldb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.time.LocalTime;
import java.util.Date;


@Entity
@Table(name = "nhanvien")
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNhanVien;

    @OneToOne
    @JoinColumn(name = "idUser")
    private User user;

    @Column(name = "ngayBDLamViec")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayBDLamViec;

    @Column(name = "gioBDLamViec")
    private String gioBDLamViec;

    @Column(name = "gioKTLamViec")
    private String gioKTLamViec;

    public NhanVien() {
    }

    public NhanVien(Integer idNhanVien, User user, Date ngayBDLamViec, String gioBDLamViec, String gioKTLamViec) {
        this.idNhanVien = idNhanVien;
        this.user = user;
        this.ngayBDLamViec = ngayBDLamViec;
        this.gioBDLamViec = gioBDLamViec;
        this.gioKTLamViec = gioKTLamViec;
    }

    public Integer getIdNhanVien() {
        return idNhanVien;
    }

    public void setIdNhanVien(Integer idNhanVien) {
        this.idNhanVien = idNhanVien;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getNgayBDLamViec() {
        return ngayBDLamViec;
    }

    public void setNgayBDLamViec(Date ngayBDLamViec) {
        this.ngayBDLamViec = ngayBDLamViec;
    }

    public String getGioBDLamViec() {
        return gioBDLamViec;
    }

    public void setGioBDLamViec(String gioBDLamViec) {
        this.gioBDLamViec = gioBDLamViec;
    }

    public String getGioKTLamViec() {
        return gioKTLamViec;
    }

    public void setGioKTLamViec(String gioKTLamViec) {
        this.gioKTLamViec = gioKTLamViec;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "idNhanVien=" + idNhanVien +
                ", user=" + user +
                ", ngayBDLamViec=" + ngayBDLamViec +
                ", gioBDLamViec='" + gioBDLamViec + '\'' +
                ", gioKTLamViec='" + gioKTLamViec + '\'' +
                '}';
    }
}
