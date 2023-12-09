package com.starryNougat.PokemonGo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class EventBonusJoinPK {

    @Column(name="PAY_TO_PLAY")
    private char payToPlay;

    @Column(name="EVENT_SEQ")
    private int eventSeq;

    @Column(name="BONUS_SEQ")
    private int bonusSeq;

    public EventBonusJoinPK() {
    }

    public EventBonusJoinPK(char payToPlay, int eventSeq, int bonusSeq) {
        this.payToPlay = payToPlay;
        this.eventSeq = eventSeq;
        this.bonusSeq = bonusSeq;
    }

    @Override
    public String toString() {
        return "EventBonusJoinPK{" +
                "payToPlay=" + payToPlay +
                ", eventSeq=" + eventSeq +
                ", bonusSeq=" + bonusSeq +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventBonusJoinPK that)) return false;
        return payToPlay == that.payToPlay && eventSeq == that.eventSeq && bonusSeq == that.bonusSeq;
    }

    @Override
    public int hashCode() {
        return Objects.hash(payToPlay, eventSeq, bonusSeq);
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
}
