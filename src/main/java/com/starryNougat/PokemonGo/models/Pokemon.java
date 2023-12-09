package com.starryNougat.PokemonGo.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="pokemon")
@IdClass(PokemonPk.class)
public class Pokemon {
    @Id
    @Column(name="PM_POKEDEX_NUM")
    private int pmPokedexNum;

    @Id
    @Column(name="REGION_SEQ")
    private int regionSeq;

    @Id
    @Column(name="PM_FORM")
    private String pmForm;

    @Column(name="PM_URL")
    private String pmUrl;

    @Column(name="PM_RELEASE_DT")
    private Date pmReleaseDt;

    @Column(name="SHINY_RELEASE_DT")
    private Date shinyReleaseDt;

    @Column(name="SHADOW_RELEASE_DT")
    private Date shadowReleaseDt;

    @Column(name="PM_TYPE1")
    private String pmType1;

    @Column(name="PM_TYPE2")
    private String pmType2;

    public Pokemon() {
    }

    public Pokemon(int pmPokedexNum, int regionSeq, String pmForm, String pmUrl, String pmType1, String pmType2) {
        this.pmPokedexNum = pmPokedexNum;
        this.regionSeq = regionSeq;
        this.pmForm = pmForm;
        this.pmUrl = pmUrl;
        this.pmType1 = pmType1;
        this.pmType2 = pmType2;
    }

    public Pokemon(int pmPokedexNum, int regionSeq, String pmForm, String pmUrl, Date pmReleaseDt, Date shinyReleaseDt, Date shadowReleaseDt, String pmType1, String pmType2) {
        this.pmPokedexNum = pmPokedexNum;
        this.regionSeq = regionSeq;
        this.pmForm = pmForm;
        this.pmUrl = pmUrl;
        this.pmReleaseDt = pmReleaseDt;
        this.shinyReleaseDt = shinyReleaseDt;
        this.shadowReleaseDt = shadowReleaseDt;
        this.pmType1 = pmType1;
        this.pmType2 = pmType2;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "pmPokedexNum=" + pmPokedexNum +
                ", regionSeq=" + regionSeq +
                ", pmForm='" + pmForm + '\'' +
                ", pmUrl='" + pmUrl + '\'' +
                ", pmReleaseDt=" + pmReleaseDt +
                ", shinyReleaseDt=" + shinyReleaseDt +
                ", shadowReleaseDt=" + shadowReleaseDt +
                ", pmType1='" + pmType1 + '\'' +
                ", pmType2='" + pmType2 + '\'' +
                '}';
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

    public String getPmUrl() {
        return pmUrl;
    }

    public void setPmUrl(String pmUrl) {
        this.pmUrl = pmUrl;
    }

    public Date getPmReleaseDt() {
        return pmReleaseDt;
    }

    public void setPmReleaseDt(Date pmReleaseDt) {
        this.pmReleaseDt = pmReleaseDt;
    }

    public Date getShinyReleaseDt() {
        return shinyReleaseDt;
    }

    public void setShinyReleaseDt(Date shinyReleaseDt) {
        this.shinyReleaseDt = shinyReleaseDt;
    }

    public Date getShadowReleaseDt() {
        return shadowReleaseDt;
    }

    public void setShadowReleaseDt(Date shadowReleaseDt) {
        this.shadowReleaseDt = shadowReleaseDt;
    }

    public String getPmType1() {
        return pmType1;
    }

    public void setPmType1(String pmType1) {
        this.pmType1 = pmType1;
    }

    public String getPmType2() {
        return pmType2;
    }

    public void setPmType2(String pmType2) {
        this.pmType2 = pmType2;
    }
}
