package models;

public class Pet {
	private int id;
	private String name;
	private int attack;
	private int defense;
	private int price;

	public Pet(int id, String name, int attack, int defense, int price) {
		this.id = id;
		this.name = name;
		this.attack = attack;
		this.defense = defense;
		this.price = price;
	}

	// Getters
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefense() {
		return defense;
	}

	public int getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return String.format("%s (ATK: +%d, DEF: +%d) - %d coins", name, attack, defense, price);
	}
}