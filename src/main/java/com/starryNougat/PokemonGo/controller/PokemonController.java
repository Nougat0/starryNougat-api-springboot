package com.starryNougat.PokemonGo.controller;

import com.starryNougat.PokemonGo.models.GlobalPokemonName;
import com.starryNougat.PokemonGo.models.Pokemon;
import com.starryNougat.PokemonGo.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {

    @Autowired
    PokemonService service;

    @GetMapping("/pokemonReleaseDates")
    public ResponseEntity<List<Pokemon>> getDates() throws IOException {
        return new ResponseEntity<>(service.getDates(), HttpStatus.OK);
    }

    @GetMapping("/missingFrom/{siteName}")
    public ResponseEntity<List<Integer>> getMissingFrom(@PathVariable("siteName") String siteName) throws IOException  {
        return new ResponseEntity<>(service.getMissing(siteName), HttpStatus.OK);
    }

    @GetMapping("/pokemonEnNames")
    public ResponseEntity<List<GlobalPokemonName>> getEnNames()  {
        return new ResponseEntity<>(service.getEnNames(), HttpStatus.OK);
    }

    @GetMapping("/pokemonPage/{pokemonName}")
    public ResponseEntity<List<Pokemon>> getOnePokemon(@PathVariable("pokemonName") String pokemonName) throws IOException {
        return new ResponseEntity<>(service.getOnePokemon(pokemonName), HttpStatus.OK);
    }

    @GetMapping("/pokemon/{pokemonName}")
    public ResponseEntity<HashMap> getOtherForms(@PathVariable("pokemonName") String pokemonName) throws IOException {
        return new ResponseEntity<>(service.getOtherForms(pokemonName), HttpStatus.OK);
    }
}
