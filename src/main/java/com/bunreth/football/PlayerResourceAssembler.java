package com.bunreth.football;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
class PlayerResourceAssembler implements ResourceAssembler<Player, Resource<Player>> {

	@Override
	public Resource<Player> toResource(Player player) {

		return new Resource<>(player,
			linkTo(methodOn(PlayerController.class).one(player.getId())).withSelfRel(),
			linkTo(methodOn(PlayerController.class).all()).withRel("players"));
	}
}