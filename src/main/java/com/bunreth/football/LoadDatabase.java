package com.bunreth.football;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
class LoadDatabase {
	
	@Bean
	CommandLineRunner initDatabase(PlayerRepository playerRepository) {
		
		// TODO: 
		// Connect Yahoo Fantasy API and load roster data directly 
		// using the following URIs:
		// * https://fantasysports.yahooapis.com/fantasy/v2/team//roster
		// * https://fantasysports.yahooapis.com/fantasy/v2/team//roster;week=10
		
		return args -> {
			playerRepository.save(new Player("Patrick", "Mahomes", Position.QB));
			playerRepository.save(new Player("Ezekiel", "Elliot", Position.RB));
			
			playerRepository.findAll().forEach(player -> {
				log.info("Preloaded " + player.getFirstName() + " " 
									  + player.getLastName() + " - " 
									  + player.getEligiblePosition());
			});
		};
	}
}