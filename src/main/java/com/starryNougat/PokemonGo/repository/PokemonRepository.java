package com.starryNougat.PokemonGo.repository;

import com.starryNougat.PokemonGo.models.Pokemon;
import com.starryNougat.PokemonGo.models.PokemonPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonRepository extends JpaRepository<Pokemon, PokemonPk> {
}
