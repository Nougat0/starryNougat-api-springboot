package com.starryNougat.PokemonGo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="global_pokemon_name")
public class GlobalPokemonName {
    @Id
    @Column(name="LANGUAGE_SEQ")
    private int languageSeq;
    @Id
    @Column(name="PM_POKEDEX_NUM")
    private int pmPokedexNum;
    @Id
    @Column(name="REGION_SEQ")
    private int regionSeq;
    @Id
    @Column(name="PM_FORM")
    private String pmForm;

    @Column(name="PM_NM")
    private String pmNm;

    public GlobalPokemonName() {
    }

    public GlobalPokemonName(int languageSeq, int pmPokedexNum, int regionSeq, String pmForm, String pmNm) {
        this.languageSeq = languageSeq;
        this.pmPokedexNum = pmPokedexNum;
        this.regionSeq = regionSeq;
        this.pmForm = pmForm;
        this.pmNm = pmNm;
    }

    @Override
    public String toString() {
        return "GlobalPokemonName{" +
                "languageSeq=" + languageSeq +
                ", pmPokedexNum=" + pmPokedexNum +
                ", regionSeq=" + regionSeq +
                ", pmForm='" + pmForm + '\'' +
                ", pmNm='" + pmNm + '\'' +
                '}';
    }

    public int getLanguageSeq() {
        return languageSeq;
    }

    public void setLanguageSeq(int languageSeq) {
        this.languageSeq = languageSeq;
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

    public String getPmNm() {
        return pmNm;
    }

    public void setPmNm(String pmNm) {
        this.pmNm = pmNm;
    }
}
