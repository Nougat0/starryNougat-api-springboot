package com.starryNougat.PokemonGo.dto;

public class PMTypes {
    private int pokedexNum;
    private String formName;
    private String type1;
    private String type2;

    public PMTypes() {
    }

    public PMTypes(int pokedexNum, String formName, String type1, String type2) {
        this.pokedexNum = pokedexNum;
        this.formName = formName;
        this.type1 = type1;
        this.type2 = type2;
    }

    @Override
    public String toString() {
        return "PMTypes{" +
                "pokedexNum=" + pokedexNum +
                ", formName='" + formName + '\'' +
                ", type1='" + type1 + '\'' +
                ", type2='" + type2 + '\'' +
                '}';
    }

    public int getPokedexNum() {
        return pokedexNum;
    }

    public void setPokedexNum(int pokedexNum) {
        this.pokedexNum = pokedexNum;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }
}
