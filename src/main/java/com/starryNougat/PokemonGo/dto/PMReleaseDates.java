package com.starryNougat.PokemonGo.dto;

public class PMReleaseDates {

    private String pokemonName;

    private String dt;

    private String shinyDt;

    private String shadowDt;

    public PMReleaseDates() {
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

    @Override
    public String toString() {
        return "PMReleaseDates{" +
                "pokemonName='" + pokemonName + '\'' +
                ", dt='" + dt + '\'' +
                ", shinyDt='" + shinyDt + '\'' +
                ", shadowDt='" + shadowDt + '\'' +
                '}';
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
}
