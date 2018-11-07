package com.bunreth.football;

import java.util.List;

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
	
	public Player(String firstName, String lastName, Position eligiblePosition) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.eligiblePosition = eligiblePosition;
	}

	public Player() {
	}
	
	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}
	
	public void setFullName(String fullName) {
		String[] parts = fullName.split(" ");
		this.firstName = parts[0];
		this.lastName = parts[1];
	}
	
}