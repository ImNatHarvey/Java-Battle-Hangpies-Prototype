package models;

import utils.WordGenerator;
import utils.GameConstants;

import java.util.HashSet;
import java.util.Set;

public class Battle {
	private Player player;
	private Enemy enemy;
	private Pet equippedPet;
	private boolean[] guessedLetters;
	private Set<Character> incorrectGuesses;
	private int remainingHearts;
	private boolean battleWon;

	public Battle(Player player, Enemy enemy, Pet equippedPet) {
		this.player = player;
		this.enemy = enemy;
		this.equippedPet = equippedPet;
		this.guessedLetters = new boolean[enemy.getWord().length()];
		this.incorrectGuesses = new HashSet<>();

		// Calculate hearts including pet defense bonus
		this.remainingHearts = GameConstants.BASE_HEARTS + (equippedPet != null ? equippedPet.getDefense() : 0);

		this.battleWon = false;
	}

	public boolean makeGuess(char letter) {
		letter = Character.toUpperCase(letter);
		String word = enemy.getWord();
		boolean correctGuess = false;

		// Check if letter is in word
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == letter) {
				guessedLetters[i] = true;
				correctGuess = true;
			}
		}

		if (correctGuess) {
			// Calculate damage including pet attack bonus - APPLY DAMAGE EVERY CORRECT
			// GUESS
			int damage = GameConstants.BASE_DAMAGE + (equippedPet != null ? equippedPet.getAttack() : 0);
			enemy.takeDamage(damage);

			// Check if word is complete
			if (isWordComplete()) {
				// Enemy takes EXTRA damage when word is completed
				int extraDamage = 2;
				enemy.takeDamage(extraDamage);

				if (!enemy.isDefeated()) {
					// Generate new word for the same enemy but keep current health
					generateNewWord();
				}
			}

			// Check if enemy is defeated
			if (enemy.isDefeated()) {
				battleWon = true;
			}
		} else {
			incorrectGuesses.add(letter);
			remainingHearts--;

			// Check if player lost
			if (remainingHearts <= 0) {
				battleWon = false;
			}
		}

		return correctGuess;
	}

	private void generateNewWord() {
		// Save current enemy health and stats
		int currentHealth = enemy.getCurrentHealth();
		int maxHealth = enemy.getMaxHealth();
		int level = enemy.getLevel();
		boolean isBoss = enemy.isBoss();

		// Generate new enemy with same level and boss status
		Enemy newEnemy = new Enemy(level);

		// Create a custom enemy that preserves the battle state
		this.enemy = newEnemy;

		// Manually set the health to preserve it
		// We need to bypass the normal constructor which sets new health
		// Since Enemy class doesn't have setters, we'll use reflection or create a
		// custom solution
		// For now, we'll create a wrapper that preserves health
		preserveEnemyHealth(newEnemy, currentHealth, maxHealth);

		// Reset guessing state for new word
		this.guessedLetters = new boolean[enemy.getWord().length()];
		this.incorrectGuesses = new HashSet<>();
	}

	private void preserveEnemyHealth(Enemy newEnemy, int currentHealth, int maxHealth) {
		// This is a workaround since we can't modify the Enemy class directly
		// We'll create a new battle with the preserved health
		// The damage will still work correctly
		try {
			// Take enough damage to set the health to the preserved value
			int damageToTake = newEnemy.getMaxHealth() - currentHealth;
			if (damageToTake > 0) {
				newEnemy.takeDamage(damageToTake);
			}
		} catch (Exception e) {
			System.err.println("Error preserving enemy health: " + e.getMessage());
		}
	}

	public boolean isWordComplete() {
		for (boolean guessed : guessedLetters) {
			if (!guessed)
				return false;
		}
		return true;
	}

	public boolean isBattleOver() {
		return battleWon || remainingHearts <= 0 || enemy.isDefeated();
	}

	public boolean isWon() {
		return battleWon;
	}

	// Getters
	public Player getPlayer() {
		return player;
	}

	public Enemy getEnemy() {
		return enemy;
	}

	public boolean[] getGuessedLetters() {
		return guessedLetters;
	}

	public Set<Character> getIncorrectGuesses() {
		return incorrectGuesses;
	}

	public int getRemainingHearts() {
		return remainingHearts;
	}

	public int getMaxHearts() {
		return GameConstants.BASE_HEARTS + (equippedPet != null ? equippedPet.getDefense() : 0);
	}

	public String getDisplayWord() {
		return enemy.getDisplayWord(guessedLetters);
	}

	public int calculateReward() {
		if (battleWon) {
			return utils.WordGenerator.calculateReward(enemy.getLevel());
		}
		return 0;
	}
}