package models;

import utils.WordGenerator;
import utils.GameConstants;

public class Enemy {
	private int level;
	private String word;
	private int maxHealth;
	private int currentHealth;
	private boolean isBoss;
	private String clue;

	public Enemy(int level) {
		this.level = level;
		this.word = WordGenerator.generateWord(level);
		this.isBoss = WordGenerator.isBossLevel(level);
		this.clue = GameConstants.getClueForWord(word);

		// Increased health based on level and word length
		if (isBoss) {
			this.maxHealth = (word.length() * 2) + 10; // Bosses have much more health
		} else {
			// Scale health with level - higher levels have more health
			this.maxHealth = (word.length() * 2) + (level * 2);
		}

		this.currentHealth = this.maxHealth;
	}

	// Getters
	public int getLevel() {
		return level;
	}

	public String getWord() {
		return word;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public boolean isBoss() {
		return isBoss;
	}

	public String getClue() {
		return clue;
	}

	public void takeDamage(int damage) {
		currentHealth = Math.max(0, currentHealth - damage);
	}

	public boolean isDefeated() {
		return currentHealth <= 0;
	}

	public String getDisplayWord(boolean[] guessedLetters) {
		StringBuilder display = new StringBuilder();
		for (int i = 0; i < word.length(); i++) {
			if (guessedLetters[i]) {
				display.append(word.charAt(i)).append(" ");
			} else {
				display.append("_ ");
			}
		}
		return display.toString().trim();
	}
}