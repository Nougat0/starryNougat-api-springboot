package com.starryNougat.PokemonGo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class PokemonPk {

    @Column(name="PM_POKEDEX_NUM")
    private int pmPokedexNum;

    @Column(name="REGION_SEQ")
    private int regionSeq;

    @Column(name="PM_FORM")
    private String pmForm;

    public PokemonPk() {
    }

    public PokemonPk(int pmPokedexNum, int regionSeq, String pmForm) {
        this.pmPokedexNum = pmPokedexNum;
        this.regionSeq = regionSeq;
        this.pmForm = pmForm;
    }

    @Override
    public String toString() {
        return "PokemonPk{" +
                "pmPokedexNum=" + pmPokedexNum +
                ", regionSeq=" + regionSeq +
                ", pmForm='" + pmForm + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PokemonPk pokemonPk)) return false;
        return pmPokedexNum == pokemonPk.pmPokedexNum && regionSeq == pokemonPk.regionSeq && Objects.equals(pmForm, pokemonPk.pmForm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pmPokedexNum, regionSeq, pmForm);
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
}
