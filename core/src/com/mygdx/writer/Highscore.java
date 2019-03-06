package com.mygdx.writer;

public class Highscore {
	
	private String name;
	private int gold;
	
	public Highscore (String name, int gold) {
		this.name = name;
		this.gold = gold;
	}

	public String getName() {
		return name;
	}

	public int getGold() {
		return gold;
	}

}
