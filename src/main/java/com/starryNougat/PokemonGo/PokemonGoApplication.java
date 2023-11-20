package com.starryNougat.PokemonGo;

import com.starryNougat.PokemonGo.controller.CalendarController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@AutoConfigurationPackage
@SpringBootApplication
//@ComponentScan(basePackageClasses = {CalendarController.class})
@ComponentScan("com.starryNougat.PokemonGo")
public class PokemonGoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokemonGoApplication.class, args);
	}

}
