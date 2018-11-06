//package com.bunreth.football;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//
//import lombok.Data;
//
//@Data
//@Entity
//class League {
//	
//	private @Id @GeneratedValue Long id;
//	private String name;
//	private String url;
//	private DraftStatus draftStatus;
//	private int numOfTeams;
//	private List<Team> teams;
//	
//	public League(String name, String url, DraftStatus draftStatus, int numOfTeams) {
//		this.name = name;
//		this.url = url;
//		this.draftStatus = draftStatus;
//		this.numOfTeams = numOfTeams;
//		this.teams = new ArrayList<>();
//	}
//
//	public League() {
//	}
//	
//	
//}