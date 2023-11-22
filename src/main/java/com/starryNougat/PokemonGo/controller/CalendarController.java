package com.starryNougat.PokemonGo.controller;

import com.starryNougat.PokemonGo.dto.PMReleaseDates;
import com.starryNougat.PokemonGo.models.GlobalPokemonName;
import com.starryNougat.PokemonGo.service.CalendarService;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    CalendarService service;

    @RequestMapping("/event")
    public String event (@RequestParam(name="yymm", required=false) String yymm) {
        if(yymm == null) {
            Calendar now = Calendar.getInstance();
            yymm = String.valueOf((now.get(Calendar.YEAR)))+String.valueOf((now.get(Calendar.MONTH) + 1));
        }

        return "하하하!"+yymm;
    }

    @GetMapping("/pokemonReleaseDates")
    public ResponseEntity<List<PMReleaseDates>> getDates() throws IOException {
        return new ResponseEntity<>(service.getDates(), HttpStatus.OK);
    }

    @GetMapping("/pokemonEnNames")
    public ResponseEntity<List<GlobalPokemonName>> getEnNames()  {
        return new ResponseEntity<>(service.getEnNames(), HttpStatus.OK);
    }

    @GetMapping("/pokemonPage/{pokemonName}")
    public ResponseEntity<List<PMReleaseDates>> getOnePokemon(@PathVariable("pokemonName") String pokemonName) throws IOException {
        return new ResponseEntity<>(service.getOnePokemon(pokemonName), HttpStatus.OK);
    }


}
