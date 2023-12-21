package com.starryNougat.PokemonGo.models;

import jakarta.persistence.*;

@Entity
@Table(name="global_pokemon_name")
@IdClass(GlobalPokemonNamePk.class)
public class GlobalPokemonName {
    @Id
    @Column(name="LANGUAGE_SEQ")
    private int languageSeq;
    @Id
    @Column(name="PM_POKEDEX_NUM")
    private int pmPokedexNum;
    @Column(name="PM_NM")
    private String pmNm;

    public GlobalPokemonName() {
    }

    public GlobalPokemonName(int languageSeq, int pmPokedexNum, String pmNm) {
        this.languageSeq = languageSeq;
        this.pmPokedexNum = pmPokedexNum;
        this.pmNm = pmNm;
    }

    @Override
    public String toString() {
        return "GlobalPokemonName{" +
                "languageSeq=" + languageSeq +
                ", pmPokedexNum=" + pmPokedexNum +
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

    public String getPmNm() {
        return pmNm;
    }

    public void setPmNm(String pmNm) {
        this.pmNm = pmNm;
    }
}
