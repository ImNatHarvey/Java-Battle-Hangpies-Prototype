package models;

import utils.FileHandler;
import java.util.List;

public class Marketplace {
	private List<Pet> availablePets;

	public Marketplace() {
		this.availablePets = FileHandler.loadMarketplacePets();
	}

	public List<Pet> getAvailablePets() {
		return availablePets;
	}

	public boolean buyPet(Player player, int petId) {
		if (petId < 0 || petId >= availablePets.size()) {
			return false;
		}

		Pet pet = availablePets.get(petId);

		// Check if player already owns the pet
		if (player.ownsPet(petId)) {
			return false;
		}

		// Check if player has enough coins
		if (player.deductCoins(pet.getPrice())) {
			player.addPet(petId);
			FileHandler.savePlayerPet(player.getUsername(), petId);
			FileHandler.updatePlayer(player);
			return true;
		}

		return false;
	}

	public Pet getPetById(int petId) {
		if (petId >= 0 && petId < availablePets.size()) {
			return availablePets.get(petId);
		}
		return null;
	}
}