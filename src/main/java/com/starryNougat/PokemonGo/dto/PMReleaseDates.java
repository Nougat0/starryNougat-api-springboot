package com.starryNougat.PokemonGo.dto;

public class PMReleaseDates {

    // 포켓몬 영어 이름 ~ 포켓몬
    private String pokemonName;

    private int pokedexNum;

    private String dt;

    private String shinyDt;

    private String shadowDt;

    private String type1;
    private String type2;


    public PMReleaseDates() {
    }

    public PMReleaseDates(String pokemonName, int pokedexNum, String dt, String shinyDt, String shadowDt) {
        this.pokemonName = pokemonName;
        this.pokedexNum = pokedexNum;
        this.dt = dt;
        this.shinyDt = shinyDt;
        this.shadowDt = shadowDt;
    }

    public PMReleaseDates(String pokemonName, String dt, String shinyDt, String shadowDt) {
        this.pokemonName = pokemonName;
        this.dt = dt;
        this.shinyDt = shinyDt;
        this.shadowDt = shadowDt;
    }

    public PMReleaseDates(String dt, String shinyDt, String shadowDt) {
        this.dt = dt;
        this.shinyDt = shinyDt;
        this.shadowDt = shadowDt;
    }

    public PMReleaseDates(String pokemonName, int pokedexNum) {
        this.pokemonName = pokemonName;
        this.pokedexNum = pokedexNum;
    }

    public PMReleaseDates(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public PMReleaseDates(String pokemonName, int pokedexNum, String dt, String shinyDt, String shadowDt, String type1, String type2) {
        this.pokemonName = pokemonName;
        this.pokedexNum = pokedexNum;
        this.dt = dt;
        this.shinyDt = shinyDt;
        this.shadowDt = shadowDt;
        this.type1 = type1;
        this.type2 = type2;
    }

    @Override
    public String toString() {
        return "PMReleaseDates{" +
                "pokemonName='" + pokemonName + '\'' +
                ", pokedexNum=" + pokedexNum +
                ", dt='" + dt + '\'' +
                ", shinyDt='" + shinyDt + '\'' +
                ", shadowDt='" + shadowDt + '\'' +
                ", type1='" + type1 + '\'' +
                ", type2='" + type2 + '\'' +
                '}';
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

    public String getPokemonName() {
        return pokemonName;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getShinyDt() {
        return shinyDt;
    }

    public void setShinyDt(String shinyDt) {
        this.shinyDt = shinyDt;
    }

    public String getShadowDt() {
        return shadowDt;
    }

    public void setShadowDt(String shadowDt) {
        this.shadowDt = shadowDt;
    }

    public int getPokedexNum() {
        return pokedexNum;
    }

    public void setPokedexNum(int pokedexNum) {
        this.pokedexNum = pokedexNum;
    }
}
