package com.starryNougat.PokemonGo.models;

import jakarta.persistence.*;

import java.util.Objects;

@Embeddable
public class EventPokemonJoinPK {

    @Column(name="EVENT_SEQ")
    private int eventSeq;

    @Column(name="WHERE")
    private int where;

    @Column(name="PM_POKEDEX_NUM")
    private int pmPokedexNum;


    public EventPokemonJoinPK() {
    }

    public EventPokemonJoinPK(int eventSeq, int where, int pmPokedexNum) {
        this.eventSeq = eventSeq;
        this.where = where;
        this.pmPokedexNum = pmPokedexNum;

    }

    @Override
    public String toString() {
        return "EventPokemonJoin{" +
                "eventSeq=" + eventSeq +
                ", where=" + where +
                ", pmPokedexNum=" + pmPokedexNum +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventPokemonJoinPK that)) return false;
        return getEventSeq() == that.getEventSeq() && getWhere() == that.getWhere() && getPmPokedexNum() == that.getPmPokedexNum();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEventSeq(), getWhere(), getPmPokedexNum());
    }

    public int getEventSeq() {
        return eventSeq;
    }

    public void setEventSeq(int eventSeq) {
        this.eventSeq = eventSeq;
    }

    public int getWhere() {
        return where;
    }

    public void setWhere(int where) {
        this.where = where;
    }

    public int getPmPokedexNum() {
        return pmPokedexNum;
    }

    public void setPmPokedexNum(int pmPokedexNum) {
        this.pmPokedexNum = pmPokedexNum;
    }

}
