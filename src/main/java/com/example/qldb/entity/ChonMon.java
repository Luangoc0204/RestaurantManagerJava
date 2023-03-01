package com.example.qldb.entity;

import javax.persistence.*;

@Entity
public class ChonMon {
    @EmbeddedId
    ChonMonKey idChonMon;

    @ManyToOne
    @MapsId("idDatBan")
    @JoinColumn(name = "idDatBan")
    DatBan datBan;

    @ManyToOne
    @MapsId("idMonAn")
    @JoinColumn(name = "idMonAn")
    MonAn monAn;

    @Column(name = "soLuong")
    int soLuong;

    public ChonMon() {
    }

    public ChonMon(MonAn monAn) {
        this.monAn = monAn;
    }

    public ChonMon(ChonMonKey idChonMon, DatBan datBan, MonAn monAn, int soLuong) {
        this.idChonMon = idChonMon;
        this.datBan = datBan;
        this.monAn = monAn;
        this.soLuong = soLuong;
    }

    public ChonMonKey getIdChonMon() {
        return idChonMon;
    }

    public void setIdChonMon(ChonMonKey idChonMon) {
        this.idChonMon = idChonMon;
    }

    public DatBan getDatBan() {
        return datBan;
    }

    public void setDatBan(DatBan datBan) {
        this.datBan = datBan;
    }

    public MonAn getMonAn() {
        return monAn;
    }

    public void setMonAn(MonAn monAn) {
        this.monAn = monAn;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public String toString() {
        return "ChonMon{" +
                "idChonMon=" + idChonMon +
                ", datBan=" + datBan +
                ", monAn=" + monAn +
                ", soLuong=" + soLuong +
                '}' + "\n";
    }
}
