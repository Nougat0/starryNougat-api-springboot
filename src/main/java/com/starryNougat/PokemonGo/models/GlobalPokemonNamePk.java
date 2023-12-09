package com.starryNougat.PokemonGo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class GlobalPokemonNamePk {

    @Column(name="LANGUAGE_SEQ")
    private int languageSeq;

    @Column(name="PM_POKEDEX_NUM")
    private int pmPokedexNum;

    @Column(name="REGION_SEQ")
    private int regionSeq;

    @Column(name="PM_FORM")
    private String pmForm;

    public GlobalPokemonNamePk() {
    }

    public GlobalPokemonNamePk(int languageSeq, int pmPokedexNum, int regionSeq, String pmForm) {
        this.languageSeq = languageSeq;
        this.pmPokedexNum = pmPokedexNum;
        this.regionSeq = regionSeq;
        this.pmForm = pmForm;
    }

    @Override
    public String toString() {
        return "GlobalPokemonNamePk{" +
                "languageSeq=" + languageSeq +
                ", pmPokedexNum=" + pmPokedexNum +
                ", regionSeq=" + regionSeq +
                ", pmForm='" + pmForm + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GlobalPokemonNamePk that)) return false;
        return getLanguageSeq() == that.getLanguageSeq() && getPmPokedexNum() == that.getPmPokedexNum() && getRegionSeq() == that.getRegionSeq() && Objects.equals(getPmForm(), that.getPmForm());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLanguageSeq(), getPmPokedexNum(), getRegionSeq(), getPmForm());
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
}
