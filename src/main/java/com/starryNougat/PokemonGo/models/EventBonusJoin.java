package com.starryNougat.PokemonGo.models;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name="event_bonus_join")
@IdClass(EventBonusJoinPK.class)
public class EventBonusJoin {
    @Id
    @Column(name="PAY_TO_PLAY")
    private char payToPlay;
    @Id
    @Column(name="EVENT_SEQ")
    private int eventSeq;
    @Id
    @Column(name="BONUS_SEQ")
    private int bonusSeq;

    @Column(name="BONUS_NUM")
    private String bonusNum;

    @Column(name="BONUS_UNIT")
    private String bonusUnit;

    public EventBonusJoin() {
    }

    public EventBonusJoin(char payToPlay, int eventSeq, int bonusSeq, String bonusNum, String bonusUnit) {
        this.payToPlay = payToPlay;
        this.eventSeq = eventSeq;
        this.bonusSeq = bonusSeq;
        this.bonusNum = bonusNum;
        this.bonusUnit = bonusUnit;
    }

    @Override
    public String toString() {
        return "EventBonusJoin{" +
                "payToPlay=" + payToPlay +
                ", eventSeq=" + eventSeq +
                ", bonusSeq=" + bonusSeq +
                ", bonusNum='" + bonusNum + '\'' +
                ", bonusUnit='" + bonusUnit + '\'' +
                '}';
    }

    public char getPayToPlay() {
        return payToPlay;
    }

    public void setPayToPlay(char payToPlay) {
        this.payToPlay = payToPlay;
    }

    public int getEventSeq() {
        return eventSeq;
    }

    public void setEventSeq(int eventSeq) {
        this.eventSeq = eventSeq;
    }

    public int getBonusSeq() {
        return bonusSeq;
    }

    public void setBonusSeq(int bonusSeq) {
        this.bonusSeq = bonusSeq;
    }

    public String getBonusNum() {
        return bonusNum;
    }

    public void setBonusNum(String bonusNum) {
        this.bonusNum = bonusNum;
    }

    public String getBonusUnit() {
        return bonusUnit;
    }

    public void setBonusUnit(String bonusUnit) {
        this.bonusUnit = bonusUnit;
    }
}
