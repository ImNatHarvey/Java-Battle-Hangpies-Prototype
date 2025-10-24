package gui;

import models.Marketplace;
import models.Pet;
import models.Player;
import utils.FileHandler;
import utils.GameConstants;
import utils.ImageUtils;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MarketplaceScreen extends Frame {
	private Player player;
	private Marketplace marketplace;
	private List<Pet> availablePets;

	private Panel petsPanel;
	private Label messageLabel;
	private Label coinsLabel;

	public MarketplaceScreen(Player player) {
		this.player = player;
		this.marketplace = new Marketplace();
		this.availablePets = marketplace.getAvailablePets();

		setupUI();
	}

	private void setupUI() {
		setTitle("Marketplace - Hangman Battle Game");
		setSize(800, 600);
		setResizable(false);
		setLayout(new BorderLayout());

		// Load background
		setBackground(GameConstants.BACKGROUND_COLOR);
		Image backgroundImage = ImageUtils.loadImage(GameConstants.UI_DIR + "market_bg.png", 800, 600);

		Panel mainPanel = new Panel() {
			@Override
			public void paint(Graphics g) {
				if (backgroundImage != null) {
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				} else {
					super.paint(g);
				}
			}
		};
		mainPanel.setLayout(new BorderLayout());

		setLocationRelativeTo(null);

		// Top panel
		Panel topPanel = new Panel(new BorderLayout());
		topPanel.setBackground(new Color(255, 255, 255, 200));

		Label titleLabel = new Label("Marketplace", Label.CENTER);
		titleLabel.setFont(GameConstants.TITLE_FONT);
		titleLabel.setForeground(GameConstants.PRIMARY_COLOR);

		coinsLabel = new Label("Your Coins: " + player.getCoins(), Label.CENTER);
		coinsLabel.setFont(GameConstants.HEADER_FONT);

		topPanel.add(titleLabel, BorderLayout.CENTER);
		topPanel.add(coinsLabel, BorderLayout.EAST);

		mainPanel.add(topPanel, BorderLayout.NORTH);

		// Message label
		messageLabel = new Label("", Label.CENTER);
		messageLabel.setFont(GameConstants.NORMAL_FONT);
		messageLabel.setForeground(GameConstants.ACCENT_COLOR);
		mainPanel.add(messageLabel, BorderLayout.SOUTH);

		// Pets panel
		petsPanel = new Panel();
		petsPanel.setLayout(new GridLayout(0, 2, 20, 20));
		petsPanel.setBackground(new Color(255, 255, 255, 180));

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.add(petsPanel);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		add(mainPanel);

		// Load pets
		loadPets();

		// Control panel
		Panel controlPanel = new Panel();
		Button backButton = new Button("Back to Main Menu");
		backButton.addActionListener(e -> returnToMenu());
		backButton.setBackground(GameConstants.ACCENT_COLOR);
		backButton.setForeground(Color.WHITE);
		controlPanel.add(backButton);

		mainPanel.add(controlPanel, BorderLayout.SOUTH);

		// Window listener
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				returnToMenu();
			}
		});

		setVisible(true);
	}

	private void loadPets() {
		petsPanel.removeAll();

		// Load player's owned pets
		List<Integer> ownedPetIds = FileHandler.loadPlayerPets(player.getUsername());

		for (int i = 0; i < availablePets.size(); i++) {
			Pet pet = availablePets.get(i);
			Panel petPanel = createPetPanel(pet, i, ownedPetIds.contains(i));
			petsPanel.add(petPanel);
		}

		petsPanel.revalidate();
		petsPanel.repaint();
	}

	private Panel createPetPanel(Pet pet, int petId, boolean isOwned) {
		Panel petPanel = new Panel();
		petPanel.setLayout(new BorderLayout());
		petPanel.setBackground(Color.WHITE);
		petPanel.setPreferredSize(new Dimension(350, 350)); // Increased height

		// Pet image - larger and centered
		Image petImage = ImageUtils.loadImage(ImageUtils.getPetImagePath(petId), 150, 150); // Larger image
		Canvas imageCanvas = new Canvas() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(petImage, (getWidth() - 150) / 2, 10, this);
			}
		};
		imageCanvas.setPreferredSize(new Dimension(350, 160)); // Larger canvas
		imageCanvas.setBackground(Color.LIGHT_GRAY);

		// Pet info - moved to bottom, more compact
		Panel infoPanel = new Panel(new GridLayout(3, 1, 5, 5));
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setPreferredSize(new Dimension(350, 90));

		Label nameLabel = new Label(pet.getName(), Label.CENTER);
		nameLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Larger font

		Label statsLabel = new Label("ATK: +" + pet.getAttack() + "  DEF: +" + pet.getDefense(), Label.CENTER);
		statsLabel.setFont(GameConstants.NORMAL_FONT);

		Label priceLabel = new Label("Price: " + pet.getPrice() + " coins", Label.CENTER);
		priceLabel.setFont(GameConstants.NORMAL_FONT);
		priceLabel.setForeground(GameConstants.WARNING_COLOR);

		infoPanel.add(nameLabel);
		infoPanel.add(statsLabel);
		infoPanel.add(priceLabel);

		// Action buttons panel
		Panel buttonPanel = new Panel(new FlowLayout());
		buttonPanel.setBackground(Color.WHITE);

		Button actionButton;

		if (isOwned) {
			if (player.getEquippedPetId() == petId) {
				actionButton = new Button("Equipped");
				actionButton.setBackground(Color.GREEN);
				actionButton.setEnabled(false);
			} else {
				actionButton = new Button("Equip");
				actionButton.setBackground(GameConstants.SUCCESS_COLOR);
				actionButton.addActionListener(e -> equipPet(petId));
			}

			// Add sell button for owned pets
			Button sellButton = new Button("Sell");
			sellButton.setBackground(GameConstants.ACCENT_COLOR);
			sellButton.setForeground(Color.WHITE);
			sellButton.addActionListener(e -> sellPet(petId, pet.getPrice()));
			buttonPanel.add(sellButton);

		} else {
			actionButton = new Button("Buy");
			actionButton.setBackground(GameConstants.PRIMARY_COLOR);
			actionButton.addActionListener(e -> buyPet(petId));

			if (player.getCoins() < pet.getPrice()) {
				actionButton.setEnabled(false);
				actionButton.setBackground(Color.GRAY);
			}
		}

		actionButton.setForeground(Color.WHITE);
		buttonPanel.add(actionButton);

		// Combine info and buttons
		Panel bottomPanel = new Panel(new BorderLayout());
		bottomPanel.setBackground(Color.WHITE);
		bottomPanel.add(infoPanel, BorderLayout.CENTER);
		bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

		petPanel.add(imageCanvas, BorderLayout.CENTER);
		petPanel.add(bottomPanel, BorderLayout.SOUTH);

		return petPanel;
	}

	private void buyPet(int petId) {
		Pet pet = availablePets.get(petId);

		if (marketplace.buyPet(player, petId)) {
			showMessage("Successfully purchased " + pet.getName() + "!");
			coinsLabel.setText("Your Coins: " + player.getCoins());
			loadPets(); // Refresh the display
		} else {
			showMessage("Failed to purchase " + pet.getName() + ". Not enough coins or already owned.");
		}
	}

	private void sellPet(int petId, int price) {
		// Check if trying to sell equipped pet
		if (player.getEquippedPetId() == petId) {
			showMessage("Cannot sell equipped pet! Unequip it first.");
			return;
		}

		// Confirm sale
		Dialog confirmDialog = new Dialog(this, "Confirm Sale", true);
		confirmDialog.setSize(300, 150);
		confirmDialog.setLayout(new BorderLayout());
		confirmDialog.setLocationRelativeTo(this);

		Pet pet = availablePets.get(petId);
		int sellPrice = (int) (price * 0.7); // 70% of original price

		Label messageLabel = new Label("Sell " + pet.getName() + " for " + sellPrice + " coins?", Label.CENTER);
		messageLabel.setFont(GameConstants.NORMAL_FONT);

		Panel buttonPanel = new Panel(new FlowLayout());
		Button yesButton = new Button("Yes, Sell");
		yesButton.setBackground(GameConstants.ACCENT_COLOR);
		yesButton.setForeground(Color.WHITE);
		yesButton.addActionListener(e -> {
			// Remove pet from player's collection
			player.getOwnedPets().remove(Integer.valueOf(petId));
			player.addCoins(sellPrice);

			// Update player data
			FileHandler.updatePlayer(player);

			// Remove from player pets file
			updatePlayerPetsFile(player.getUsername(), petId);

			showMessage("Sold " + pet.getName() + " for " + sellPrice + " coins!");
			coinsLabel.setText("Your Coins: " + player.getCoins());
			loadPets(); // Refresh display
			confirmDialog.dispose();
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

	private void updatePlayerPetsFile(String username, int petId) {
		// Read all player-pet relationships
		java.util.List<String> lines = new java.util.ArrayList<>();
		try (java.io.BufferedReader reader = new java.io.BufferedReader(
				new java.io.FileReader(GameConstants.PLAYER_PETS_FILE))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 2 && parts[0].equals(username) && Integer.parseInt(parts[1]) == petId) {
					continue; // Skip the line for the sold pet
				}
				lines.add(line);
			}
		} catch (java.io.IOException e) {
			System.err.println("Error reading player pets file: " + e.getMessage());
		}

		// Write back without the sold pet
		try (java.io.BufferedWriter writer = new java.io.BufferedWriter(
				new java.io.FileWriter(GameConstants.PLAYER_PETS_FILE))) {
			for (String line : lines) {
				writer.write(line);
				writer.newLine();
			}
		} catch (java.io.IOException e) {
			System.err.println("Error updating player pets file: " + e.getMessage());
		}
	}

	private void equipPet(int petId) {
		player.setEquippedPetId(petId);
		FileHandler.updatePlayer(player);
		showMessage("Equipped " + availablePets.get(petId).getName() + "!");
		loadPets(); // Refresh to update equip status
	}

	private void showMessage(String message) {
		messageLabel.setText(message);
	}

	private void returnToMenu() {
		FileHandler.updatePlayer(player);
		new MainMenu(player);
		dispose();
	}
}