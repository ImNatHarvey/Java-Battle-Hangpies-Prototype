package gui;

import models.Pet;
import models.Player;
import utils.FileHandler;
import utils.GameConstants;
import utils.ImageUtils;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ProfileScreen extends Frame {
	private Player player;
	private List<Pet> ownedPets;

	public ProfileScreen(Player player) {
		this.player = player;

		// Load player's owned pets
		List<Integer> ownedPetIds = FileHandler.loadPlayerPets(player.getUsername());
		List<Pet> allPets = FileHandler.loadMarketplacePets();
		ownedPets = new java.util.ArrayList<>();
		for (int petId : ownedPetIds) {
			if (petId < allPets.size()) {
				ownedPets.add(allPets.get(petId));
			}
		}

		setupUI();
	}

	private void setupUI() {
		setTitle("Player Profile - Hangman Battle Game");
		setSize(800, 600);
		setResizable(false);
		setLayout(new BorderLayout());
		setBackground(GameConstants.BACKGROUND_COLOR);

		setLocationRelativeTo(null);

		// Main panel
		Panel mainPanel = new Panel(new BorderLayout());
		mainPanel.setBackground(GameConstants.BACKGROUND_COLOR);

		// Title
		Label titleLabel = new Label("Player Profile", Label.CENTER);
		titleLabel.setFont(GameConstants.TITLE_FONT);
		titleLabel.setForeground(GameConstants.PRIMARY_COLOR);
		mainPanel.add(titleLabel, BorderLayout.NORTH);

		// Profile info
		Panel infoPanel = createInfoPanel();
		mainPanel.add(infoPanel, BorderLayout.CENTER);

		// Owned pets
		Panel petsPanel = createPetsPanel();
		mainPanel.add(petsPanel, BorderLayout.SOUTH);

		// Control panel
		Panel controlPanel = new Panel();
		Button backButton = new Button("Back to Main Menu");
		backButton.addActionListener(e -> returnToMenu());
		backButton.setBackground(GameConstants.ACCENT_COLOR);
		backButton.setForeground(Color.WHITE);
		controlPanel.add(backButton);

		mainPanel.add(controlPanel, BorderLayout.SOUTH);

		add(mainPanel);

		// Window listener
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				returnToMenu();
			}
		});

		setVisible(true);
	}

	private Panel createInfoPanel() {
		Panel infoPanel = new Panel(new GridLayout(4, 2, 10, 10));
		infoPanel.setBackground(Color.WHITE);

		// Add padding using empty borders
		Panel paddedPanel = new Panel(new BorderLayout());
		paddedPanel.setBackground(Color.WHITE);
		paddedPanel.add(infoPanel, BorderLayout.CENTER);

		// Username
		addInfoField(infoPanel, "Username:", player.getUsername());

		// Level
		addInfoField(infoPanel, "Level:", String.valueOf(player.getLevel()));

		// Coins
		addInfoField(infoPanel, "Coins:", String.valueOf(player.getCoins()));

		// Equipped Pet
		String equippedPetName = "None";
		if (player.getEquippedPetId() != -1) {
			List<Pet> allPets = FileHandler.loadMarketplacePets();
			if (player.getEquippedPetId() < allPets.size()) {
				equippedPetName = allPets.get(player.getEquippedPetId()).getName();
			}
		}
		addInfoField(infoPanel, "Equipped Pet:", equippedPetName);

		return paddedPanel;
	}

	private void addInfoField(Panel panel, String label, String value) {
		Label infoLabel = new Label(label);
		infoLabel.setFont(GameConstants.BUTTON_FONT);
		panel.add(infoLabel);

		Label valueLabel = new Label(value);
		valueLabel.setFont(GameConstants.NORMAL_FONT);
		panel.add(valueLabel);
	}

	private Panel createPetsPanel() {
		Panel petsPanel = new Panel(new BorderLayout());
		petsPanel.setBackground(Color.WHITE);

		Label petsTitle = new Label("Owned Pets (" + ownedPets.size() + ")", Label.CENTER);
		petsTitle.setFont(GameConstants.HEADER_FONT);
		petsPanel.add(petsTitle, BorderLayout.NORTH);

		if (ownedPets.isEmpty()) {
			Label noPetsLabel = new Label("No pets owned. Visit the marketplace!", Label.CENTER);
			petsPanel.add(noPetsLabel, BorderLayout.CENTER);
		} else {
			Panel petsGrid = new Panel(new GridLayout(0, 3, 10, 10));
			petsGrid.setBackground(Color.WHITE);

			for (Pet pet : ownedPets) {
				Panel petCard = createPetCard(pet);
				petsGrid.add(petCard);
			}

			ScrollPane scrollPane = new ScrollPane();
			scrollPane.add(petsGrid);
			petsPanel.add(scrollPane, BorderLayout.CENTER);
		}

		return petsPanel;
	}

	private Panel createPetCard(Pet pet) {
		Panel petCard = new Panel();
		petCard.setLayout(new BorderLayout());
		petCard.setBackground(Color.LIGHT_GRAY);
		petCard.setPreferredSize(new Dimension(150, 180));

		// Pet image
		Image petImage = ImageUtils.loadImage(ImageUtils.getPetImagePath(pet.getId()), 80, 80);
		Canvas imageCanvas = new Canvas() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(petImage, (getWidth() - 80) / 2, 10, this);
			}
		};
		imageCanvas.setPreferredSize(new Dimension(150, 100));

		// Pet info
		Panel infoPanel = new Panel(new GridLayout(3, 1));
		infoPanel.setBackground(Color.WHITE);

		Label nameLabel = new Label(pet.getName(), Label.CENTER);
		nameLabel.setFont(new Font("Arial", Font.BOLD, 12));

		Label statsLabel = new Label("ATK: +" + pet.getAttack() + " DEF: +" + pet.getDefense(), Label.CENTER);
		statsLabel.setFont(new Font("Arial", Font.PLAIN, 10));

		// Equip status
		String status = (player.getEquippedPetId() == pet.getId()) ? "✓ Equipped" : "Not Equipped";
		Label statusLabel = new Label(status, Label.CENTER);
		statusLabel.setFont(new Font("Arial", Font.ITALIC, 10));
		statusLabel.setForeground((player.getEquippedPetId() == pet.getId()) ? Color.GREEN : Color.GRAY);

		infoPanel.add(nameLabel);
		infoPanel.add(statsLabel);
		infoPanel.add(statusLabel);

		petCard.add(imageCanvas, BorderLayout.CENTER);
		petCard.add(infoPanel, BorderLayout.SOUTH);

		return petCard;
	}

	private void returnToMenu() {
		new MainMenu(player);
		dispose();
	}
}