package com.starryNougat.PokemonGo.models;

import jakarta.persistence.*;

@Entity
@Table(name="event_pokemon_join")
@IdClass(EventPokemonJoinPK.class)
public class EventPokemonJoin {
    @Id
    @Column(name="EVENT_SEQ")
    private int eventSeq;
    @Id
    @Column(name="WHERE")
    private int where;
    @Id
    @Column(name="PM_POKEDEX_NUM")
    private int pmPokedexNum;

    @Column(name="REGION_SEQ")
    private int regionSeq;

    @Column(name="PM_FORM")
    private String pmForm;
    @Column(name="SHINY_YN")
    private char shinyYn;

    @Column(name="PAY_TO_PLAY")
    private char payToPlay;

    public EventPokemonJoin() {
    }

    public EventPokemonJoin(int eventSeq, int where, int pmPokedexNum, int regionSeq, String pmForm, char shinyYn, char payToPlay) {
        this.eventSeq = eventSeq;
        this.where = where;
        this.pmPokedexNum = pmPokedexNum;
        this.regionSeq = regionSeq;
        this.pmForm = pmForm;
        this.shinyYn = shinyYn;
        this.payToPlay = payToPlay;
    }

    @Override
    public String toString() {
        return "EventPokemonJoin{" +
                "eventSeq=" + eventSeq +
                ", where=" + where +
                ", pmPokedexNum=" + pmPokedexNum +
                ", regionSeq=" + regionSeq +
                ", pmForm='" + pmForm + '\'' +
                ", shinyYn=" + shinyYn +
                ", payToPlay=" + payToPlay +
                '}';
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

    public int getRegionSeq() {
        return regionSeq;
    }

    public void setRegionSeq(int regionSeq) {
        this.regionSeq = regionSeq;
    }

    public String getPmForm() {
        return pmForm;
    }

    public void setPmForm(String pmForm) {
        this.pmForm = pmForm;
    }

    public char getShinyYn() {
        return shinyYn;
    }

    public void setShinyYn(char shinyYn) {
        this.shinyYn = shinyYn;
    }

    public char getPayToPlay() {
        return payToPlay;
    }

    public void setPayToPlay(char payToPlay) {
        this.payToPlay = payToPlay;
    }
}
