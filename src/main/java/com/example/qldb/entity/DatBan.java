package com.example.qldb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "datban")
public class DatBan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDatBan;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    @OneToOne
    @JoinColumn(name = "idBanAn")
    private BanAn banAn;

    @Column(name = "soLuong")
    private int soLuong;

    @Column(name = "ngayDatBan")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayDatBan;

    @Column(name = "gioDatBan")
    private String gioDatBan;

    @Column(name = "ghiChu")
    private String ghiChu;

    @Column(name = "isCheckin")
    private boolean isCheckin;

    @Column(name = "isActive")
    private boolean isActive;

    @OneToMany(mappedBy = "datBan")
    private List<ChonMon> listChonMon;


    public DatBan() {
    }

    public DatBan(Integer idDatBan, User user, BanAn banAn, int soLuong, Date ngayDatBan, String gioDatBan, String ghiChu, boolean isCheckin) {
        this.idDatBan = idDatBan;
        this.user = user;
        this.banAn = banAn;
        this.soLuong = soLuong;
        this.ngayDatBan = ngayDatBan;
        this.gioDatBan = gioDatBan;
        this.ghiChu = ghiChu;
        this.isCheckin = isCheckin;
    }

    public DatBan(Integer idDatBan, com.example.qldb.entity.User user, BanAn banAn, int soLuong, Date ngayDatBan, String gioDatBan, boolean isCheckin, List<ChonMon> listChonMon) {
        this.idDatBan = idDatBan;
        this.user = user;
        this.banAn = banAn;
        this.soLuong = soLuong;
        this.ngayDatBan = ngayDatBan;
        this.gioDatBan = gioDatBan;
        this.isCheckin = isCheckin;
        this.listChonMon = listChonMon;
    }

    public DatBan(Integer idDatBan, User user, BanAn banAn, int soLuong, Date ngayDatBan, String gioDatBan, String ghiChu, boolean isCheckin, boolean isActive, List<ChonMon> listChonMon) {
        this.idDatBan = idDatBan;
        this.user = user;
        this.banAn = banAn;
        this.soLuong = soLuong;
        this.ngayDatBan = ngayDatBan;
        this.gioDatBan = gioDatBan;
        this.ghiChu = ghiChu;
        this.isCheckin = isCheckin;
        this.isActive = isActive;
        this.listChonMon = listChonMon;
    }
    public static List<DatBan> sortListDatBanByGioDatBan(List<DatBan> datBanList){
        String listGioDatBan[] = new String[datBanList.size()];
        for (int i=0; i<datBanList.size(); i++){

            listGioDatBan[i] = datBanList.get(i).getGioDatBan();
        }
        String tg;
        for (int i=0; i< listGioDatBan.length - 1; i++){
            for (int j=i+1; j< listGioDatBan.length; j++){
                if (BanAn.compareTime(listGioDatBan[i], listGioDatBan[j])>0){
                    tg = listGioDatBan[i];
                    listGioDatBan[i] = listGioDatBan[j];
                    listGioDatBan[j] = tg;
                }
            }
        }
        List<DatBan> datBanList_save = new ArrayList<>();
        for (int i=0; i<datBanList.size(); i++){
            for (int j=0; j< listGioDatBan.length; j++){
                if (datBanList.get(i).getGioDatBan().equals(listGioDatBan[j])){
                    datBanList_save.add(datBanList.get(i));
                }
            }
        }
        System.out.println("List đặt bàn save: " + datBanList_save.toString());
        return datBanList_save;

    }

    public List<ChonMon> getListChonMon() {
        return listChonMon;
    }

    public void setListChonMon(List<ChonMon> listChonMon) {
        this.listChonMon = listChonMon;
    }

    public Integer getIdDatBan() {
        return idDatBan;
    }

    public void setIdDatBan(Integer idDatBan) {
        this.idDatBan = idDatBan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BanAn getBanAn() {
        return banAn;
    }

    public void setBanAn(BanAn banAn) {
        this.banAn = banAn;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public Date getNgayDatBan() {
        return ngayDatBan;
    }

    public void setNgayDatBan(Date ngayDatBan) {
        this.ngayDatBan = ngayDatBan;
    }

    public String getGioDatBan() {
        return gioDatBan;
    }

    public void setGioDatBan(String gioDatBan) {
        this.gioDatBan = gioDatBan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public boolean isCheckin() {
        return isCheckin;
    }

    public void setCheckin(boolean checkin) {
        isCheckin = checkin;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "DatBan{" +
                "idDatBan=" + idDatBan +
                ", User=" + user +
                ", banAn=" + banAn +
                ", soLuong=" + soLuong +
                ", ngayDatBan=" + ngayDatBan +
                ", gioDatBan='" + gioDatBan + '\'' +
                ", isCheckin=" + isCheckin +
                '}';
    }
}
