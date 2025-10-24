package gui;

import models.Player;
import utils.FileHandler;
import utils.GameConstants;

import java.awt.*;
import java.awt.event.*;

public class SettingsScreen extends Frame {
	private Player player;

	public SettingsScreen(Player player) {
		this.player = player;
		setupUI();
	}

	private void setupUI() {
		setTitle("Settings - Hangman Battle Game");
		setSize(800, 600); // Same size as other panels
		setResizable(false);
		setLayout(new BorderLayout());
		setBackground(GameConstants.BACKGROUND_COLOR);

		setLocationRelativeTo(null);

		Panel mainPanel = new Panel(new BorderLayout());
		mainPanel.setBackground(GameConstants.BACKGROUND_COLOR);

		// Title
		Label titleLabel = new Label("Game Settings", Label.CENTER);
		titleLabel.setFont(GameConstants.TITLE_FONT);
		titleLabel.setForeground(GameConstants.PRIMARY_COLOR);
		mainPanel.add(titleLabel, BorderLayout.NORTH);

		// Content panel
		Panel contentPanel = new Panel(new GridBagLayout());
		contentPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 15, 15, 15);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Game Information
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		Label infoLabel = new Label("Game Information:", Label.LEFT);
		infoLabel.setFont(GameConstants.HEADER_FONT);
		contentPanel.add(infoLabel, gbc);

		gbc.gridy = 1;
		TextArea infoArea = new TextArea("Hangman Battle Game v1.0\n\n" + "Features:\n"
				+ "• 20 progressive levels with boss battles\n" + "• Collectible pet system with stat bonuses\n"
				+ "• Marketplace economy\n" + "• File-based save system\n\n" + "How to Play:\n"
				+ "• Equip a pet from Marketplace first\n" + "• Click letters to guess the word\n"
				+ "• Correct guesses damage enemies\n" + "• Wrong guesses cost hearts\n"
				+ "• Complete words for extra damage\n" + "• Defeat enemies to earn coins and level up", 12, 50,
				TextArea.SCROLLBARS_VERTICAL_ONLY);
		infoArea.setEditable(false);
		contentPanel.add(infoArea, gbc);

		// Player Stats
		gbc.gridy = 2;
		Label statsLabel = new Label("Your Stats:", Label.LEFT);
		statsLabel.setFont(GameConstants.HEADER_FONT);
		contentPanel.add(statsLabel, gbc);

		gbc.gridy = 3;
		gbc.gridwidth = 1;
		Panel statsPanel = new Panel(new GridLayout(2, 2, 10, 10));
		statsPanel.setBackground(Color.WHITE);

		addStat(statsPanel, "Username:", player.getUsername());
		addStat(statsPanel, "Level:", String.valueOf(player.getLevel()));
		addStat(statsPanel, "Coins:", String.valueOf(player.getCoins()));

		String petName = "None";
		if (player.getEquippedPetId() != -1) {
			petName = utils.FileHandler.loadMarketplacePets().get(player.getEquippedPetId()).getName();
		}
		addStat(statsPanel, "Equipped Pet:", petName);

		contentPanel.add(statsPanel, gbc);

		mainPanel.add(contentPanel, BorderLayout.CENTER);

		// Control buttons
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		Panel buttonPanel = new Panel(new FlowLayout());

		Button resetButton = new Button("Reset Game Data");
		resetButton.setBackground(GameConstants.WARNING_COLOR);
		resetButton.setForeground(Color.WHITE);
		resetButton.addActionListener(e -> showResetConfirmation());

		Button backButton = new Button("Back to Main Menu");
		backButton.setBackground(GameConstants.PRIMARY_COLOR);
		backButton.setForeground(Color.WHITE);
		backButton.addActionListener(e -> returnToMenu());

		buttonPanel.add(resetButton);
		buttonPanel.add(backButton);

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);

		// Window listener
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				returnToMenu();
			}
		});

		setVisible(true);
	}

	private void addStat(Panel panel, String label, String value) {
		Panel statPanel = new Panel(new BorderLayout());
		statPanel.setBackground(Color.WHITE);

		Label statLabel = new Label(label);
		statLabel.setFont(GameConstants.BUTTON_FONT);
		Label valueLabel = new Label(value);
		valueLabel.setFont(GameConstants.NORMAL_FONT);

		statPanel.add(statLabel, BorderLayout.WEST);
		statPanel.add(valueLabel, BorderLayout.CENTER);

		panel.add(statPanel);
	}

	private void showResetConfirmation() {
		Dialog confirmDialog = new Dialog(this, "Confirm Reset", true);
		confirmDialog.setSize(350, 150);
		confirmDialog.setLayout(new BorderLayout());
		confirmDialog.setLocationRelativeTo(this);

		Label messageLabel = new Label("This will delete ALL game data!\nAre you sure?", Label.CENTER);
		messageLabel.setFont(GameConstants.NORMAL_FONT);

		Panel buttonPanel = new Panel(new FlowLayout());
		Button yesButton = new Button("Yes, Reset Everything");
		yesButton.setBackground(GameConstants.ACCENT_COLOR);
		yesButton.setForeground(Color.WHITE);
		yesButton.addActionListener(e -> {
			resetGameData();
			confirmDialog.dispose();
			returnToMenu();
		});

		Button noButton = new Button("Cancel");
		noButton.setBackground(GameConstants.PRIMARY_COLOR);
		noButton.setForeground(Color.WHITE);
		noButton.addActionListener(e -> confirmDialog.dispose());

		buttonPanel.add(yesButton);
		buttonPanel.add(noButton);

		confirmDialog.add(messageLabel, BorderLayout.CENTER);
		confirmDialog.add(buttonPanel, BorderLayout.SOUTH);
		confirmDialog.setVisible(true);
	}

	private void resetGameData() {
		// Delete data files
		java.io.File playersFile = new java.io.File(GameConstants.PLAYERS_FILE);
		java.io.File playerPetsFile = new java.io.File(GameConstants.PLAYER_PETS_FILE);
		java.io.File marketplaceFile = new java.io.File(GameConstants.MARKETPLACE_FILE);

		playersFile.delete();
		playerPetsFile.delete();
		marketplaceFile.delete();

		// Reinitialize files
		FileHandler.initializeDataFiles();

		// Show message
		Dialog messageDialog = new Dialog(this, "Reset Complete", true);
		messageDialog.setSize(250, 100);
		messageDialog.setLayout(new BorderLayout());
		messageDialog.setLocationRelativeTo(this);

		Label messageLabel = new Label("Game data has been reset.", Label.CENTER);
		Button okButton = new Button("OK");
		okButton.addActionListener(e -> {
			messageDialog.dispose();
			System.exit(0); // Close game to force fresh start
		});

		messageDialog.add(messageLabel, BorderLayout.CENTER);
		messageDialog.add(okButton, BorderLayout.SOUTH);
		messageDialog.setVisible(true);
	}

	private void returnToMenu() {
		new MainMenu(player);
		dispose();
	}
}