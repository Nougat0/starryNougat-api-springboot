package com.starryNougat.PokemonGo.controller;

import com.starryNougat.PokemonGo.models.Event;
import com.starryNougat.PokemonGo.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    EventService service;

    @GetMapping("/eventList/{year}")
    public ResponseEntity<List<Event>> getEventListofYear(@PathVariable("year") int year) throws IOException{
        return new ResponseEntity<>(service.getEventListofYear(year), HttpStatus.OK);
    }

    @GetMapping("/eventNameList/{year}")
    public ResponseEntity<List<String>> getEventNamesofYear(@PathVariable("year") int year) throws IOException{
        return new ResponseEntity<>(service.getEventNames(year), HttpStatus.OK);
    }

}
