package com.example.qldb.entity;

import javax.persistence.*;

@Entity
@Table(name = "quanly")
public class QuanLy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idQuanLy;

    @OneToOne
    @JoinColumn(name = "idUser")
    private User user;

    @Column(name = "gioBDSang")
    private String gioBDSang;

    @Column(name = "gioKTSang")
    private String gioKTSang;

    @Column(name = "gioBDToi")
    private String gioBDToi;

    @Column(name = "gioKTToi")
    private String gioKTToi;

    @Column(name = "doanhthu")
    private Long doanhThu;

    @Column(name = "monAnDuocYeuThich")
    private String monAnDuocYeuThich;

    public QuanLy() {
    }

    public QuanLy(Integer idQuanLy, User user, Long doanhThu, String monAnDuocYeuThich) {
        this.idQuanLy = idQuanLy;
        this.user = user;
        this.doanhThu = doanhThu;
        this.monAnDuocYeuThich = monAnDuocYeuThich;
    }

    public QuanLy(Integer idQuanLy, User user, String gioBDSang, String gioKTSang, String gioBDToi, String gioKTToi, Long doanhThu, String monAnDuocYeuThich) {
        this.idQuanLy = idQuanLy;
        this.user = user;
        this.gioBDSang = gioBDSang;
        this.gioKTSang = gioKTSang;
        this.gioBDToi = gioBDToi;
        this.gioKTToi = gioKTToi;
        this.doanhThu = doanhThu;
        this.monAnDuocYeuThich = monAnDuocYeuThich;
    }

    public Integer getIdQuanLy() {
        return idQuanLy;
    }

    public void setIdQuanLy(Integer idQuanLy) {
        this.idQuanLy = idQuanLy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getDoanhThu() {
        return doanhThu;
    }

    public void setDoanhThu(Long doanhThu) {
        this.doanhThu = doanhThu;
    }

    public String getMonAnDuocYeuThich() {
        return monAnDuocYeuThich;
    }

    public void setMonAnDuocYeuThich(String monAnDuocYeuThich) {
        this.monAnDuocYeuThich = monAnDuocYeuThich;
    }

    public String getGioBDSang() {
        return gioBDSang;
    }

    public void setGioBDSang(String gioBDSang) {
        this.gioBDSang = gioBDSang;
    }

    public String getGioKTSang() {
        return gioKTSang;
    }

    public void setGioKTSang(String gioKTSang) {
        this.gioKTSang = gioKTSang;
    }

    public String getGioBDToi() {
        return gioBDToi;
    }

    public void setGioBDToi(String gioBDToi) {
        this.gioBDToi = gioBDToi;
    }

    public String getGioKTToi() {
        return gioKTToi;
    }

    public void setGioKTToi(String gioKTToi) {
        this.gioKTToi = gioKTToi;
    }
}
