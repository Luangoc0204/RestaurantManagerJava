package com.example.qldb.entity;



import org.springframework.data.relational.core.sql.In;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Entity
@Table(name = "banan")
public class BanAn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idBanAn", nullable = false)
    private Integer idBanAn;

    @Column(name = "loaiBanAn")
    private int loaiBanAn;

    @Column(name = "gioBD")
    private String gioBD;

    @Column(name = "gioKT")
    private String gioKT;

    @Column(name = "soLuongBanAn")
    private int soLuongBanAn;

    @Column(name = "isActive")
    private boolean isActive;

    public BanAn() {
    }

    public BanAn(Integer idBanAn, int loaiBanAn, String gioBD, String gioKT, int soLuongBanAn, boolean isActive) {
        this.idBanAn = idBanAn;
        this.loaiBanAn = loaiBanAn;
        this.gioBD = gioBD;
        this.gioKT = gioKT;
        this.soLuongBanAn = soLuongBanAn;
        this.isActive = isActive;
    }
    //hàm so sánh thời gian
    public static int compareTime(String gio1, String gio2){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime time1 = LocalTime.parse(gio1, dateFormat);
        LocalTime time2 = LocalTime.parse(gio2, dateFormat);
        return time1.compareTo(time2);
        /*return >0 nếu lớn hơn
          return <0 nếu nhỏ hơn
          return =0 nếu bằng nhau*/
    }
    public static List<BanAn> sortByLoaiBanAn(List<BanAn> banAnList){
        List<Integer> loaiBanAn_cotrungnhau = new ArrayList<>();
        for (int i=0; i< banAnList.size(); i++){
            loaiBanAn_cotrungnhau.add(banAnList.get(i).loaiBanAn);
        }
        //loại bỏ các phần tử trùng nhau của loại bàn ăn
        Set<Integer> set = new LinkedHashSet<Integer>(loaiBanAn_cotrungnhau);
        List<Integer> loaiBanAn = new ArrayList<>(set);
        //
        int x[] = new int[loaiBanAn.size()];
        for (int i=0; i< loaiBanAn.size(); i++){
            x[i] = loaiBanAn.get(i);
        }
        Arrays.sort(x);
        List<BanAn> dsBanAn = new ArrayList<>();
        for (int i=0; i< x.length; i++){
            for (int j=0; j< banAnList.size(); j++){
                if (x[i] == banAnList.get(j).loaiBanAn){
                    dsBanAn.add(banAnList.get(j));
                }
            }
        }
        return dsBanAn;
    }
    public static List<BanAn> mergeBanAn(List<BanAn> list1, List<BanAn> list2){
        for (int i=0; i<list2.size(); i++){
            list1.add(list2.get(i));
        }
        return list1;
    }
    public static List<BanAn> sortListBanAn(List<BanAn> banAnList, List<String> gioLamViec){
        System.out.println("Giờ làm việc:" + gioLamViec.toString());
        //lấy bàn ăn theo giờ bắt đầu sáng
        List<BanAn> dsBanAn1 = new ArrayList<>();
        for (int i=0; i<banAnList.size(); i++){
            if (compareTime(banAnList.get(i).getGioBD(), gioLamViec.get(0))>=0 && compareTime(banAnList.get(i).getGioBD(), gioLamViec.get(1)) <0 ){
                dsBanAn1.add(banAnList.get(i));
            }
        }
        dsBanAn1 = BanAn.sortByLoaiBanAn(dsBanAn1);
        System.out.println("DS bàn ăn sáng: " + dsBanAn1.toString());
        //lấy bàn ăn theo giờ bắt đầu tối
        List<BanAn> dsBanAn2 = new ArrayList<>();
        for (int i=0; i<banAnList.size(); i++){
            if (compareTime(banAnList.get(i).getGioBD(), gioLamViec.get(2))>=0 && compareTime(banAnList.get(i).getGioBD(), gioLamViec.get(3)) <0){
                dsBanAn2.add(banAnList.get(i));
            }
        }
        dsBanAn2 = BanAn.sortByLoaiBanAn(dsBanAn2);
        System.out.println("DS bàn ăn tối: " + dsBanAn2.toString());
        //gộp 2 ds bàn ăn
        List<BanAn> dsBanAn = BanAn.mergeBanAn(dsBanAn1, dsBanAn2);
        return dsBanAn;
    }
    public Integer getIdBanAn() {
        return idBanAn;
    }

    public void setIdBanAn(Integer idBanAn) {
        this.idBanAn = idBanAn;
    }

    public int getLoaiBanAn() {
        return loaiBanAn;
    }

    public void setLoaiBanAn(int loaiBanAn) {
        this.loaiBanAn = loaiBanAn;
    }

    public String getGioBD() {
        return gioBD;
    }

    public void setGioBD(String gioBD) {
        this.gioBD = gioBD;
    }

    public String getGioKT() {
        return gioKT;
    }

    public void setGioKT(String gioKT) {
        this.gioKT = gioKT;
    }

    public int getSoLuongBanAn() {
        return soLuongBanAn;
    }

    public void setSoLuongBanAn(int soLuongBanAn) {
        this.soLuongBanAn = soLuongBanAn;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isGiongNhau(BanAn banAn) {
        if (this.loaiBanAn == banAn.getLoaiBanAn() && this.soLuongBanAn == banAn.soLuongBanAn && this.gioBD.equals(banAn.getGioBD())  && this.gioKT.equals(banAn.getGioKT())){
            return true;
        } else {
            return false;
        }

    }

    @Override
    public String toString() {
        return "BanAn{" +
                "idBanAn=" + idBanAn +
                ", loaiBanAn=" + loaiBanAn +
                ", gioBD='" + gioBD + '\'' +
                ", gioKT='" + gioKT + '\'' +
                ", soLuongBanAn=" + soLuongBanAn +
                ", isActive=" + isActive  +
                '}' + "\n";
    }
}
