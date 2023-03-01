package com.example.qldb.entity;


import java.util.ArrayList;
import java.util.List;

public class ListChonMon {

    private List<ChonMon> chonMonList;

    public ListChonMon() {
        chonMonList = new ArrayList<>();
    }

    public ListChonMon(List<ChonMon> chonMonList) {
        this.chonMonList = chonMonList;
    }


    public List<ChonMon> getChonMonList() {
        return chonMonList;
    }

    public void setChonMonList(List<ChonMon> chonMonList) {
        this.chonMonList = chonMonList;
    }

    @Override
    public String toString() {
        return "ListChonMon{" +
                "chonMonList=" + chonMonList +
                '}';
    }
}
