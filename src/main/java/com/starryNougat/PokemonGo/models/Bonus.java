package com.starryNougat.PokemonGo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name="bonus")
public class Bonus {
    @Id
    @Column(name="BONUS_SEQ")
    private int bonusSeq;

    @Column(name="WHAT")
    private String what;

    @Column(name="WHEN_OR_WHERE")
    private String whenOrWhere;

    public Bonus() {
    }

    public Bonus(int bonusSeq, String what, String whenOrWhere) {
        this.bonusSeq = bonusSeq;
        this.what = what;
        this.whenOrWhere = whenOrWhere;
    }

    @Override
    public String toString() {
        return "Bonus{" +
                "bonusSeq=" + bonusSeq +
                ", what='" + what + '\'' +
                ", whenOrWhere='" + whenOrWhere + '\'' +
                '}';
    }

    public int getBonusSeq() {
        return bonusSeq;
    }

    public void setBonusSeq(int bonusSeq) {
        this.bonusSeq = bonusSeq;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getWhenOrWhere() {
        return whenOrWhere;
    }

    public void setWhenOrWhere(String whenOrWhere) {
        this.whenOrWhere = whenOrWhere;
    }
}
