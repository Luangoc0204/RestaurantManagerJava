package com.example.qldb.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class MonAn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMonAn;

    @Column(name = "loaiMonAn")
    private String loaiMonAn;

    @Column(name = "tenMonAn")
    private String tenMonAn;

    @Column(name = "giaTien")
    private int giaTien;

    @Column(name = "soLuongMonAn")
    private int soLuongMonAn;

    @Column(name = "img")
    private String img;

    @Column(name = "isActive")
    private boolean isActive;

    @OneToMany(mappedBy = "monAn")
    private List<ChonMon> listChonMon;
    public MonAn() {
    }

    public MonAn(Integer idMonAn, String loaiMonAn, String tenMonAn, int giaTien, int soLuongMonAn, String img, boolean isActive) {
        this.idMonAn = idMonAn;
        this.loaiMonAn = loaiMonAn;
        this.tenMonAn = tenMonAn;
        this.giaTien = giaTien;
        this.soLuongMonAn = soLuongMonAn;
        this.img = img;
        this.isActive = isActive;
    }

    public MonAn(Integer idMonAn, String loaiMonAn, String tenMonAn, int giaTien, int soLuongMonAn, String img, List<ChonMon> listChonMon) {
        this.idMonAn = idMonAn;
        this.loaiMonAn = loaiMonAn;
        this.tenMonAn = tenMonAn;
        this.giaTien = giaTien;
        this.soLuongMonAn = soLuongMonAn;
        this.img = img;
        this.listChonMon = listChonMon;
    }

    public List<ChonMon> getListChonMon() {
        return listChonMon;
    }

    public void setListChonMon(List<ChonMon> listChonMon) {
        this.listChonMon = listChonMon;
    }

    public Integer getIdMonAn() {
        return idMonAn;
    }

    public void setIdMonAn(Integer idMonAn) {
        this.idMonAn = idMonAn;
    }

    public String getLoaiMonAn() {
        return loaiMonAn;
    }

    public void setLoaiMonAn(String loaiMonAn) {
        this.loaiMonAn = loaiMonAn;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public int getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(int giaTien) {
        this.giaTien = giaTien;
    }

    public int getSoLuongMonAn() {
        return soLuongMonAn;
    }

    public void setSoLuongMonAn(int soLuongMonAn) {
        this.soLuongMonAn = soLuongMonAn;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "MonAn{" +
                "idMonAn=" + idMonAn +
                ", loaiMonAn='" + loaiMonAn + '\'' +
                ", tenMonAn='" + tenMonAn + '\'' +
                ", giaTien=" + giaTien +
                ", soLuongMonAn=" + soLuongMonAn +
                ", img='" + img + '\'' +
                '}' + "\n";
    }
}
