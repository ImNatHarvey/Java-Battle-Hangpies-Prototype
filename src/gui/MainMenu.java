package gui;

import models.Player;
import utils.GameConstants;
import utils.ImageUtils;

import java.awt.*;
import java.awt.event.*;

public class MainMenu extends Frame {
	private Player player;
	private Image backgroundImage;

	public MainMenu(Player player) {
		this.player = player;
		setupUI();
		setupEvents();
	}

	private void setupUI() {
		setTitle("Hangman Battle Game - Main Menu");
		setSize(800, 600);
		setResizable(false);
		setLayout(new BorderLayout());

		// Load background image
		backgroundImage = ImageUtils.loadImage(GameConstants.UI_DIR + "background.png", 800, 600);

		// Center the window
		setLocationRelativeTo(null);

		// Main panel with overlay
		Panel mainPanel = new Panel() {
			@Override
			public void paint(Graphics g) {
				if (backgroundImage != null) {
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				} else {
					g.setColor(GameConstants.BACKGROUND_COLOR);
					g.fillRect(0, 0, getWidth(), getHeight());
				}

				// Draw overlay
				g.setColor(new Color(0, 0, 0, 128));
				g.fillRect(0, 0, getWidth(), getHeight());

				super.paint(g);
			}
		};
		mainPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 15, 15, 15);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Game Title
		Label titleLabel = new Label("Hangman Battle Game", Label.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		mainPanel.add(titleLabel, gbc);

		// Welcome message
		Label welcomeLabel = new Label("Welcome, " + player.getUsername() + "!", Label.CENTER);
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
	
		gbc.gridy = 1;
		mainPanel.add(welcomeLabel, gbc);

		// Stats panel
		Panel statsPanel = createStatsPanel();
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		mainPanel.add(statsPanel, gbc);

		// Buttons
		gbc.gridwidth = 1;
		gbc.gridy = 3;
		gbc.gridx = 0;
		Button playButton = createMenuButton("Play Battle");
		playButton.addActionListener(e -> startBattle());
		mainPanel.add(playButton, gbc);

		gbc.gridx = 1;
		Button marketButton = createMenuButton("Marketplace");
		marketButton.addActionListener(e -> openMarketplace());
		mainPanel.add(marketButton, gbc);

		gbc.gridy = 4;
		gbc.gridx = 0;
		Button profileButton = createMenuButton("Profile");
		profileButton.addActionListener(e -> openProfile());
		mainPanel.add(profileButton, gbc);

		gbc.gridx = 1;
		Button settingsButton = createMenuButton("Settings");
		settingsButton.addActionListener(e -> openSettings());
		mainPanel.add(settingsButton, gbc);

		gbc.gridy = 5;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		Button exitButton = createMenuButton("Exit Game");
		exitButton.setBackground(GameConstants.ACCENT_COLOR);
		exitButton.addActionListener(e -> exitGame());
		mainPanel.add(exitButton, gbc);

		add(mainPanel, BorderLayout.CENTER);

		// Window listener
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				saveAndExit();
			}
		});

		setVisible(true);
	}

	private Panel createStatsPanel() {
		Panel statsPanel = new Panel();
		statsPanel.setLayout(new GridLayout(1, 3, 10, 10));
		statsPanel.setBackground(new Color(255, 255, 255, 180));

		// Level
		Panel levelPanel = new Panel();
		levelPanel.setLayout(new BorderLayout());
		Label levelLabel = new Label("Level", Label.CENTER);
		levelLabel.setFont(GameConstants.BUTTON_FONT);
		Label levelValue = new Label(String.valueOf(player.getLevel()), Label.CENTER);
		levelValue.setFont(GameConstants.HEADER_FONT);
		levelPanel.add(levelLabel, BorderLayout.NORTH);
		levelPanel.add(levelValue, BorderLayout.CENTER);

		// Coins
		Panel coinPanel = new Panel();
		coinPanel.setLayout(new BorderLayout());
		Label coinLabel = new Label("Coins", Label.CENTER);
		coinLabel.setFont(GameConstants.BUTTON_FONT);
		Label coinValue = new Label(String.valueOf(player.getCoins()), Label.CENTER);
		coinValue.setFont(GameConstants.HEADER_FONT);
		coinPanel.add(coinLabel, BorderLayout.NORTH);
		coinPanel.add(coinValue, BorderLayout.CENTER);

		// Equipped Pet
		Panel petPanel = new Panel();
		petPanel.setLayout(new BorderLayout());
		Label petLabel = new Label("Equipped Pet", Label.CENTER);
		petLabel.setFont(GameConstants.BUTTON_FONT);
		String petName = (player.getEquippedPetId() != -1)
				? utils.FileHandler.loadMarketplacePets().get(player.getEquippedPetId()).getName()
				: "None";
		Label petValue = new Label(petName, Label.CENTER);
		petValue.setFont(GameConstants.NORMAL_FONT);
		petPanel.add(petLabel, BorderLayout.NORTH);
		petPanel.add(petValue, BorderLayout.CENTER);

		statsPanel.add(levelPanel);
		statsPanel.add(coinPanel);
		statsPanel.add(petPanel);

		return statsPanel;
	}

	private Button createMenuButton(String text) {
		Button button = new Button(text);
		button.setFont(GameConstants.BUTTON_FONT);
		button.setBackground(GameConstants.PRIMARY_COLOR);
		button.setForeground(Color.WHITE);
		button.setPreferredSize(new Dimension(200, 50));
		return button;
	}

	private void setupEvents() {
		// Additional event setup if needed
	}

	private void startBattle() {
		if (player.getEquippedPetId() == -1) {
			// Show error message if no pet equipped
			Dialog errorDialog = new Dialog(this, "No Pet Equipped", true);
			errorDialog.setSize(500, 150);
			errorDialog.setLayout(new BorderLayout());
			errorDialog.setLocationRelativeTo(this);

			Label messageLabel = new Label("You need to equip a pet first! Visit the Marketplace.", Label.CENTER);
			messageLabel.setFont(GameConstants.NORMAL_FONT);

			Panel buttonPanel = new Panel(new FlowLayout());
			Button okButton = new Button("OK");
			okButton.addActionListener(e -> errorDialog.dispose());
			buttonPanel.add(okButton);

			Button marketButton = new Button("Go to Marketplace");
			marketButton.addActionListener(e -> {
				errorDialog.dispose();
				openMarketplace();
			});
			buttonPanel.add(marketButton);

			errorDialog.add(messageLabel, BorderLayout.CENTER);
			errorDialog.add(buttonPanel, BorderLayout.SOUTH);
			errorDialog.setVisible(true);
			return;
		}
		new BattleScreen(player);
		dispose();
	}

	private void openMarketplace() {
		new MarketplaceScreen(player);
		dispose();
	}

	private void openProfile() {
		new ProfileScreen(player);
		dispose();
	}

	private void openSettings() {
		new SettingsScreen(player);
		dispose();
	}

	private void exitGame() {
		saveAndExit();
	}

	private void saveAndExit() {
		utils.FileHandler.updatePlayer(player);
		System.exit(0);
	}
}