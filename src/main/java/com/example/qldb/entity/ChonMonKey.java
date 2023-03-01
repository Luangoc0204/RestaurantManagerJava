package com.example.qldb.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChonMonKey implements Serializable {
    @Column(name = "idDatBan")
    Integer idDatBan;

    @Column(name = "idMonAn")
    Integer idMonAn;

    public ChonMonKey() {
    }

    public ChonMonKey(Integer idDatBan, Integer idMonAn) {
        this.idDatBan = idDatBan;
        this.idMonAn = idMonAn;
    }

    public Integer getIdDatBan() {
        return idDatBan;
    }

    public void setIdDatBan(Integer idDatBan) {
        this.idDatBan = idDatBan;
    }

    public Integer getIdMonAn() {
        return idMonAn;
    }

    public void setIdMonAn(Integer idMonAn) {
        this.idMonAn = idMonAn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChonMonKey that = (ChonMonKey) o;
        return Objects.equals(idDatBan, that.idDatBan) && Objects.equals(idMonAn, that.idMonAn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDatBan, idMonAn);
    }
}
