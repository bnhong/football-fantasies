package com.bunreth.football;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
class Player {
	
	private @Id @GeneratedValue Long id;
	private String firstName;
	private String lastName;
	private Position eligiblePosition; 
	private GameStatus gameStatus;
	
	public Player(String firstName, String lastName, Position eligiblePosition) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.eligiblePosition = eligiblePosition;
	}

	public Player() {
	}
	
	
	
}