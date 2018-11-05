package com.bunreth.football;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PlayerController {
	
	private final PlayerRepository playerRepository;

	public PlayerController(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}
	
	// Aggregate root
	
	@GetMapping("/players")
	List<Player> all() {
		return playerRepository.findAll();
	}
	
	@PostMapping("/players")
	Player newPlayer(@RequestBody Player newPlayer) {
		return playerRepository.save(newPlayer);
	}
	
	// Single item
	
	@GetMapping("/players/{id}")
	Player one(@PathVariable Long id) {
		
		return playerRepository.findById(id)
				.orElseThrow(() -> new PlayerNotFoundException(id));
	}
	
	@PutMapping("/players/{id}")
	Player replacePlayer(@RequestBody Player newPlayer, @PathVariable Long id) {
		
		return playerRepository.findById(id)
				.map(player -> {
					player.setFirstName(newPlayer.getFirstName());
					player.setLastName(newPlayer.getLastName());
					return playerRepository.save(player);
				})
				.orElseGet(() -> {
					newPlayer.setId(id);
					return playerRepository.save(newPlayer);
				});
	}
	
	@DeleteMapping("/players/{id}")
	void deletePlayer(@PathVariable Long id) {
		playerRepository.deleteById(id);
	}
}