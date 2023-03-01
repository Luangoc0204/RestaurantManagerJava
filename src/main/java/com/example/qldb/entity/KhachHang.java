package com.example.qldb.entity;

import javax.persistence.*;

@Entity
@Table(name = "khachhang")
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idKhachHang;

    @OneToOne
    @JoinColumn(name = "idUser")
    private User user;

    @Column(name = "soLanDB")
    private int soLanDB;

    @Column(name = "giamGia")
    private int giamGia;

    public KhachHang() {
    }

    public KhachHang(Integer idKhachHang, User user, int soLanDB, int giamGia) {
        this.idKhachHang = idKhachHang;
        this.user = user;
        this.soLanDB = soLanDB;
        this.giamGia = giamGia;
    }

    public void updateSoLanDB(int x){
        this.soLanDB = x;
    }
    public void updateGiamGia(int tien){
        if (tien>=10000000 && tien<20000000){
            this.giamGia = 5;
        } else if (tien >= 20000000){
            this.giamGia = 10;
        } else {
            giamGia = 0;
        }
    }
    public Integer getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(Integer idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getSoLanDB() {
        return soLanDB;
    }

    public void setSoLanDB(int soLanDB) {
        this.soLanDB = soLanDB;
    }

    public int getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(int giamGia) {
        this.giamGia = giamGia;
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "idKhachHang=" + idKhachHang +
                ", user=" + user +
                ", soLanDB=" + soLanDB +
                ", giamGia=" + giamGia +
                '}';
    }
}
