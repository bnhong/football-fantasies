package com.bunreth.football;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
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
	private final PlayerResourceAssembler playerAssembler;

	public PlayerController(PlayerRepository playerRepository,
							PlayerResourceAssembler playerAssembler) {
		this.playerRepository = playerRepository;
		this.playerAssembler = playerAssembler;
	}
	
	// Aggregate root
	
	@GetMapping("/players")
	Resources<Resource<Player>> all() {

		List<Resource<Player>> players = playerRepository.findAll().stream()
			.map(playerAssembler::toResource)
			.collect(Collectors.toList());

		return new Resources<>(players,
			linkTo(methodOn(PlayerController.class).all()).withSelfRel());
	}
	
	@PostMapping("/players")	
	ResponseEntity<?> newPlayer(@RequestBody Player newPlayer) throws URISyntaxException {

		Resource<Player> resource = playerAssembler.toResource(playerRepository.save(newPlayer));

		return ResponseEntity
			.created(new URI(resource.getId().expand().getHref()))
			.body(resource);
	}
	
	// Single item
	
	@GetMapping("/players/{id}")
	Resource<Player> one(@PathVariable Long id) {

		Player player = playerRepository.findById(id)
			.orElseThrow(() -> new PlayerNotFoundException(id));

		return playerAssembler.toResource(player);
	}
	
	@PutMapping("/players/{id}")
	ResponseEntity<?> replacePlayer(@RequestBody Player newPlayer, @PathVariable Long id) throws URISyntaxException {

		Player updatedPlayer = playerRepository.findById(id)
			.map(player -> {
				player.setFirstName(newPlayer.getFirstName());
				player.setLastName(newPlayer.getLastName());
				player.setEligiblePosition(newPlayer.getEligiblePosition());
				return playerRepository.save(player);
			})
			.orElseGet(() -> {
				newPlayer.setId(id);
				return playerRepository.save(newPlayer);
			});

		Resource<Player> resource = playerAssembler.toResource(updatedPlayer);

		return ResponseEntity
			.created(new URI(resource.getId().expand().getHref()))
			.body(resource);
	}
	
	@DeleteMapping("/players/{id}")
	ResponseEntity<?> deletePlayer(@PathVariable Long id) {

		playerRepository.deleteById(id);

		return ResponseEntity.noContent().build();
	}
}