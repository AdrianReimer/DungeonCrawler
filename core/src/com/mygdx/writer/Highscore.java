package com.mygdx.writer;

/**
 * Highscore Class consisting of the Players name and the collected gold.
 * @author Adrian Reimer
 *
 */
public class Highscore implements Comparable<Highscore> {
	
	private String name; // Player name
	private int gold; // gold collected
	
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

	public void setName(String name) {
		this.name = name;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	@Override
	public int compareTo(Highscore highscore) {
		if(this.gold > highscore.gold) {
			return -1;
		}else if(this.gold == highscore.gold) {
			return 0;
		}
		return 1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || obj.getClass() != Highscore.class) {
			return false;
		}
		Highscore highscore = (Highscore) obj;	
		return (name.equals(highscore.name)) && (gold == highscore.getGold());
	}
	
	@Override
	public String toString() {
		return name + " - " + gold;
	}

}
