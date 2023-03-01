package com.example.qldb.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "hoadon")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHoaDon;

    @OneToOne
    @JoinColumn( name = "idDatBan")
    private DatBan datBan;

    @Column(name = "ngayHoaDon")
    private Date ngayHoaDon;

    @Column(name = "gioHoaDon")
    private String gioHoaDon;

    @Column(name = "tongTien")
    private int tongTien;

    public HoaDon() {
    }

    public HoaDon(Integer idHoaDon, DatBan datBan, Date ngayHoaDon, String gioHoaDon, int tongTien) {
        this.idHoaDon = idHoaDon;
        this.datBan = datBan;
        this.ngayHoaDon = ngayHoaDon;
        this.gioHoaDon = gioHoaDon;
        this.tongTien = tongTien;
    }

    public Integer getIdHoaDon() {
        return idHoaDon;
    }

    public void setIdHoaDon(Integer idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public DatBan getDatBan() {
        return datBan;
    }

    public void setDatBan(DatBan datBan) {
        this.datBan = datBan;
    }

    public Date getNgayHoaDon() {
        return ngayHoaDon;
    }

    public void setNgayHoaDon(Date ngayHoaDon) {
        this.ngayHoaDon = ngayHoaDon;
    }

    public String getGioHoaDon() {
        return gioHoaDon;
    }

    public void setGioHoaDon(String gioHoaDon) {
        this.gioHoaDon = gioHoaDon;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "idHoaDon=" + idHoaDon +
                ", datBan=" + datBan +
                ", ngayHoaDon=" + ngayHoaDon +
                ", gioHoaDon='" + gioHoaDon + '\'' +
                ", tongTien=" + tongTien +
                '}';
    }
}
