package utils;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class GameConstants {
	// Colors
	public static final Color PRIMARY_COLOR = new Color(41, 128, 185);
	public static final Color SECONDARY_COLOR = new Color(52, 152, 219);
	public static final Color ACCENT_COLOR = new Color(231, 76, 60);
	public static final Color SUCCESS_COLOR = new Color(46, 204, 113);
	public static final Color WARNING_COLOR = new Color(241, 196, 15);
	public static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
	public static final Color TEXT_COLOR = new Color(44, 62, 80);

	// Fonts
	public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
	public static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 18);
	public static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);
	public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

	// Game Settings
	public static final int BASE_HEARTS = 3;
	public static final int STARTING_COINS = 50;
	public static final int BASE_DAMAGE = 1;

	// Level Settings
	public static final int TOTAL_LEVELS = 20;
	public static final int[] BOSS_LEVELS = { 5, 10, 15, 20 };

	// Word Categories with Clues
	public static final Map<String, String> WORD_CLUES = new HashMap<>();

	static {
		// Easy words with clues
		WORD_CLUES.put("JAVA", "A programming language");
		WORD_CLUES.put("CODE", "What programmers write");
		WORD_CLUES.put("GAME", "Fun activity with rules");
		WORD_CLUES.put("PLAY", "Opposite of work");
		WORD_CLUES.put("WORD", "Collection of letters");
		WORD_CLUES.put("LOOP", "Repeating code structure");
		WORD_CLUES.put("DATA", "Information in computing");
		WORD_CLUES.put("FILE", "Computer document");
		WORD_CLUES.put("TEXT", "Written words");
		WORD_CLUES.put("USER", "Person using a computer");

		// Medium words with clues
		WORD_CLUES.put("PROGRAM", "Set of instructions for computer");
		WORD_CLUES.put("OBJECT", "Instance of a class in OOP");
		WORD_CLUES.put("METHOD", "Function in a class");
		WORD_CLUES.put("CLASS", "Blueprint for objects");
		WORD_CLUES.put("STRING", "Sequence of characters");
		WORD_CLUES.put("NUMBER", "Mathematical value");
		WORD_CLUES.put("SYSTEM", "Organized set of components");
		WORD_CLUES.put("BATTLE", "Fight between opponents");
		WORD_CLUES.put("MARKET", "Place for buying and selling");
		WORD_CLUES.put("PLAYER", "Person playing a game");

		// Hard words with clues
		WORD_CLUES.put("VARIABLE", "Container for storing data");
		WORD_CLUES.put("FUNCTION", "Block of reusable code");
		WORD_CLUES.put("INTERFACE", "Contract for classes");
		WORD_CLUES.put("ABSTRACT", "Conceptual, not concrete");
		WORD_CLUES.put("POLYMORPHISM", "Many forms in OOP");
		WORD_CLUES.put("ENCAPSULATION", "Bundling data with methods");
		WORD_CLUES.put("INHERITANCE", "Acquiring properties from parent");
		WORD_CLUES.put("IMPLEMENTATION", "Actual code for functionality");
		WORD_CLUES.put("CONSTRUCTOR", "Special method for object creation");
		WORD_CLUES.put("ALGORITHM", "Step-by-step problem solving procedure");
	}

	public static final List<String> EASY_WORDS = Arrays.asList("JAVA", "CODE", "GAME", "PLAY", "WORD", "LOOP", "DATA",
			"FILE", "TEXT", "USER");

	public static final List<String> MEDIUM_WORDS = Arrays.asList("PROGRAM", "OBJECT", "METHOD", "CLASS", "STRING",
			"NUMBER", "SYSTEM", "BATTLE", "MARKET", "PLAYER");

	public static final List<String> HARD_WORDS = Arrays.asList("VARIABLE", "FUNCTION", "INTERFACE", "ABSTRACT",
			"POLYMORPHISM", "ENCAPSULATION", "INHERITANCE", "IMPLEMENTATION", "CONSTRUCTOR", "ALGORITHM");

	// Pet Definitions
	public static final String[] PET_NAMES = { "Fire Dragon", "Ice Wolf", "Thunder Bird", "Earth Golem",
			"Water Serpent", "Wind Fox", "Dark Panther", "Light Unicorn", "Metal Knight", "Nature Treant" };

	public static final int[] PET_ATTACKS = { 2, 1, 3, 0, 1, 2, 3, 1, 0, 2 };
	public static final int[] PET_DEFENSES = { 1, 2, 0, 3, 1, 2, 1, 3, 4, 2 };
	public static final int[] PET_PRICES = { 50, 45, 60, 40, 30, 70, 80, 75, 65, 55 };

	// File Paths
	public static final String PLAYERS_FILE = "data/players.txt";
	public static final String PLAYER_PETS_FILE = "data/player_pets.txt";
	public static final String MARKETPLACE_FILE = "data/marketplace.txt";

	// Image Paths
	public static final String IMAGES_DIR = "images/";
	public static final String PETS_DIR = IMAGES_DIR + "pets/";
	public static final String ENEMIES_DIR = IMAGES_DIR + "enemies/";
	public static final String UI_DIR = IMAGES_DIR + "ui/";

	public static String getClueForWord(String word) {
		return WORD_CLUES.getOrDefault(word, "Think about programming concepts!");
	}
}