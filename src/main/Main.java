package main;

import gui.LoginScreen;
import utils.FileHandler;

public class Main {
	public static void main(String[] args) {
		// Initialize data directories
		FileHandler.initializeDataFiles();

		// Start the game with login screen
		new LoginScreen();
	}
}