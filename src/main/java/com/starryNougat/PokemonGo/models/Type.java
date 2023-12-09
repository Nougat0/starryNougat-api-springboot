package com.starryNougat.PokemonGo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name="event")
public class Type {
    @Id
    @Column(name="TYPE_CODE")
    private String typeCode;

    @Column(name="TYPE_NM")
    private String typeNm;


    public Type() {
    }

    public Type(String typeCode, String typeNm) {
        this.typeCode = typeCode;
        this.typeNm = typeNm;
    }

    @Override
    public String toString() {
        return "Type{" +
                "typeCode='" + typeCode + '\'' +
                ", typeNm='" + typeNm + '\'' +
                '}';
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeNm() {
        return typeNm;
    }

    public void setTypeNm(String typeNm) {
        this.typeNm = typeNm;
    }
}
