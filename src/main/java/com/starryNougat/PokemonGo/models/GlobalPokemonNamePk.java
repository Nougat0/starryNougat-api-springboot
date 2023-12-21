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

    public GlobalPokemonNamePk() {
    }

    public GlobalPokemonNamePk(int languageSeq, int pmPokedexNum) {
        this.languageSeq = languageSeq;
        this.pmPokedexNum = pmPokedexNum;
    }

    @Override
    public String toString() {
        return "GlobalPokemonNamePk{" +
                "languageSeq=" + languageSeq +
                ", pmPokedexNum=" + pmPokedexNum +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GlobalPokemonNamePk that)) return false;
        return getLanguageSeq() == that.getLanguageSeq() && getPmPokedexNum() == that.getPmPokedexNum();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLanguageSeq(), getPmPokedexNum());
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
}
