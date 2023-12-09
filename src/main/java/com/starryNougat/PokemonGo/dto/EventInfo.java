package com.starryNougat.PokemonGo.dto;

import com.starryNougat.PokemonGo.models.Event;
import com.starryNougat.PokemonGo.models.EventBonusJoin;
import com.starryNougat.PokemonGo.models.EventPokemonJoin;

import java.util.List;

public class EventInfo {

    private Event event;
    private List<EventPokemonJoin> pokemonList;
    private List<EventBonusJoin> bonusList;

    public EventInfo() {
    }

    public EventInfo(Event event, List<EventPokemonJoin> pokemonList, List<EventBonusJoin> bonusList) {
        this.event = event;
        this.pokemonList = pokemonList;
        this.bonusList = bonusList;
    }

    @Override
    public String toString() {
        return "EventInfo{" +
                "event=" + event +
                ", pokemonList=" + pokemonList +
                ", bonusList=" + bonusList +
                '}';
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<EventPokemonJoin> getPokemonList() {
        return pokemonList;
    }

    public void setPokemonList(List<EventPokemonJoin> pokemonList) {
        this.pokemonList = pokemonList;
    }

    public List<EventBonusJoin> getBonusList() {
        return bonusList;
    }

    public void setBonusList(List<EventBonusJoin> bonusList) {
        this.bonusList = bonusList;
    }
}
