package models;

import java.util.ArrayList;
import java.util.List;

import utils.GameConstants;

public class Player {
	private String username;
	private String password;
	private int level;
	private int coins;
	private int equippedPetId;
	private List<Integer> ownedPets;

	public Player(String username, String password, int level, int coins, int equippedPetId) {
		this.username = username;
		this.password = password;
		this.level = level;
		this.coins = coins;
		this.equippedPetId = equippedPetId;
		this.ownedPets = new ArrayList<>();
	}

	public Player(String username, String password) {
		this(username, password, 1, GameConstants.STARTING_COINS, -1);
	}

	// Getters and Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public int getEquippedPetId() {
		return equippedPetId;
	}

	public void setEquippedPetId(int equippedPetId) {
		this.equippedPetId = equippedPetId;
	}

	public List<Integer> getOwnedPets() {
		return ownedPets;
	}

	public void setOwnedPets(List<Integer> ownedPets) {
		this.ownedPets = ownedPets;
	}

	public void addCoins(int amount) {
		this.coins += amount;
	}

	public boolean deductCoins(int amount) {
		if (this.coins >= amount) {
			this.coins -= amount;
			return true;
		}
		return false;
	}

	public void levelUp() {
		this.level++;
	}

	public void addPet(int petId) {
		if (!ownedPets.contains(petId)) {
			ownedPets.add(petId);
		}
	}

	public boolean ownsPet(int petId) {
		return ownedPets.contains(petId);
	}
}