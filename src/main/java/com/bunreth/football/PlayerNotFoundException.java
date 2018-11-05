// Exceptin used to indicate when a player is not found

package com.bunreth.football;

class PlayerNotFoundException extends RuntimeException {

	PlayerNotFoundException(Long id) {
		super("Could not find player " + id);
	}
}