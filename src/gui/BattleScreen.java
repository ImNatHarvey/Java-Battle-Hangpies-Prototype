package gui;

import models.Battle;
import models.Enemy;
import models.Pet;
import models.Player;
import utils.FileHandler;
import utils.GameConstants;
import utils.ImageUtils;
import utils.WordGenerator;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class BattleScreen extends Frame {
	private Player player;
	private Battle battle;
	private Pet equippedPet;
	private boolean battleInProgress;

	// UI Components
	private Label levelLabel;
	private Label wordLabel;
	private Label clueLabel;
	private Label heartsLabel;
	private Label enemyHealthLabel;
	private Label messageLabel;
	private Label coinsLabel; // Made this an instance variable
	private Panel lettersPanel;
	private Image enemyImage;
	private Image petImage;
	private Canvas enemyCanvas;
	private Canvas petCanvas;
	private Label enemyLabel; // Made this an instance variable

	public BattleScreen(Player player) {
		this.player = player;
		this.battleInProgress = true;

		// Load equipped pet
		if (player.getEquippedPetId() != -1) {
			List<Pet> pets = FileHandler.loadMarketplacePets();
			if (player.getEquippedPetId() < pets.size()) {
				equippedPet = pets.get(player.getEquippedPetId());
			}
		}

		// Initialize battle
		startNewBattle();

		setupUI();
		updateDisplay();
	}

	private void startNewBattle() {
		Enemy enemy = new Enemy(player.getLevel());
		this.battle = new Battle(player, enemy, equippedPet);
		this.battleInProgress = true;

		// Load new enemy image for this battle
		enemyImage = ImageUtils.loadImage(ImageUtils.getEnemyImagePath(player.getLevel(), battle.getEnemy().isBoss()),
				200, 200);
	}

	private void setupUI() {
		setTitle("Hangman Battle - Level " + player.getLevel());
		setSize(800, 600);
		setResizable(false);
		setLayout(new BorderLayout());
		setBackground(Color.BLACK);

		setLocationRelativeTo(null);

		// Top panel - Stats
		Panel topPanel = new Panel(new GridLayout(1, 4));
		topPanel.setBackground(Color.DARK_GRAY);
		topPanel.setForeground(Color.WHITE);

		levelLabel = new Label("Level: " + player.getLevel(), Label.CENTER);
		levelLabel.setFont(GameConstants.HEADER_FONT);
		levelLabel.setForeground(Color.WHITE);

		heartsLabel = new Label("Hearts: " + battle.getRemainingHearts() + "/" + battle.getMaxHearts(), Label.CENTER);
		heartsLabel.setFont(GameConstants.HEADER_FONT);
		heartsLabel.setForeground(Color.WHITE);

		enemyHealthLabel = new Label(
				"Enemy HP: " + battle.getEnemy().getCurrentHealth() + "/" + battle.getEnemy().getMaxHealth(),
				Label.CENTER);
		enemyHealthLabel.setFont(GameConstants.HEADER_FONT);
		enemyHealthLabel.setForeground(Color.WHITE);

		coinsLabel = new Label("Coins: " + player.getCoins(), Label.CENTER); // Now instance variable
		coinsLabel.setFont(GameConstants.HEADER_FONT);
		coinsLabel.setForeground(Color.WHITE);

		topPanel.add(levelLabel);
		topPanel.add(heartsLabel);
		topPanel.add(enemyHealthLabel);
		topPanel.add(coinsLabel);

		add(topPanel, BorderLayout.NORTH);

		// Center panel - Battle view
		Panel centerPanel = new Panel(new BorderLayout());
		centerPanel.setBackground(Color.BLACK);

		// Enemy and player area
		Panel battleArea = new Panel(new GridLayout(1, 2));
		battleArea.setBackground(Color.BLACK);

		// PLAYER SIDE (LEFT)
		Panel playerPanel = new Panel(new BorderLayout());
		playerPanel.setBackground(Color.DARK_GRAY);

		// Load pet image if equipped
		if (equippedPet != null) {
			petImage = ImageUtils.loadImage(ImageUtils.getPetImagePath(equippedPet.getId()), 200, 200);
		} else {
			petImage = ImageUtils.generatePlaceholder(200, 200, Color.LIGHT_GRAY);
		}

		petCanvas = new Canvas() {
			@Override
			public void paint(Graphics g) {
				if (petImage != null) {
					g.drawImage(petImage, (getWidth() - 200) / 2, 10, this);
				}
			}
		};
		petCanvas.setPreferredSize(new Dimension(300, 250));
		petCanvas.setBackground(Color.DARK_GRAY);

		String petName = equippedPet != null ? equippedPet.getName() : "No Pet Equipped";
		Label petLabel = new Label(petName, Label.CENTER);
		petLabel.setFont(GameConstants.HEADER_FONT);
		petLabel.setForeground(Color.WHITE);

		// Pet stats
		Panel petStatsPanel = new Panel(new GridLayout(2, 1));
		petStatsPanel.setBackground(Color.DARK_GRAY);

		Label petAttackLabel = new Label("ATK: +" + (equippedPet != null ? equippedPet.getAttack() : 0), Label.CENTER);
		petAttackLabel.setFont(GameConstants.NORMAL_FONT);
		petAttackLabel.setForeground(Color.WHITE);

		Label petDefenseLabel = new Label("DEF: +" + (equippedPet != null ? equippedPet.getDefense() : 0),
				Label.CENTER);
		petDefenseLabel.setFont(GameConstants.NORMAL_FONT);
		petDefenseLabel.setForeground(Color.WHITE);

		petStatsPanel.add(petAttackLabel);
		petStatsPanel.add(petDefenseLabel);

		playerPanel.add(petLabel, BorderLayout.NORTH);
		playerPanel.add(petCanvas, BorderLayout.CENTER);
		playerPanel.add(petStatsPanel, BorderLayout.SOUTH);

		// ENEMY SIDE (RIGHT)
		Panel enemyPanel = new Panel(new BorderLayout());
		enemyPanel.setBackground(Color.DARK_GRAY);

		// Enemy canvas - will be updated when enemy changes
		enemyCanvas = new Canvas() {
			@Override
			public void paint(Graphics g) {
				if (enemyImage != null) {
					g.drawImage(enemyImage, (getWidth() - 200) / 2, 10, this);
				}
			}
		};
		enemyCanvas.setPreferredSize(new Dimension(300, 250));
		enemyCanvas.setBackground(Color.DARK_GRAY);

		enemyLabel = new Label(battle.getEnemy().isBoss() ? "BOSS BATTLE!" : "Enemy", Label.CENTER); // Now instance
																										// variable
		enemyLabel.setFont(GameConstants.HEADER_FONT);
		enemyLabel.setForeground(Color.WHITE);

		enemyPanel.add(enemyLabel, BorderLayout.NORTH);
		enemyPanel.add(enemyCanvas, BorderLayout.CENTER);

		battleArea.add(playerPanel);
		battleArea.add(enemyPanel);

		centerPanel.add(battleArea, BorderLayout.CENTER);

		// Word display and clue panel
		Panel wordPanel = new Panel(new BorderLayout());
		wordPanel.setBackground(Color.DARK_GRAY);
		wordPanel.setPreferredSize(new Dimension(800, 80));

		wordLabel = new Label("", Label.CENTER);
		wordLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
		wordLabel.setForeground(Color.WHITE);
		wordPanel.add(wordLabel, BorderLayout.CENTER);

		// Clue label
		clueLabel = new Label("Clue: " + GameConstants.getClueForWord(battle.getEnemy().getWord()), Label.CENTER);
		clueLabel.setFont(new Font("Arial", Font.ITALIC, 16));
		clueLabel.setForeground(Color.YELLOW);
		wordPanel.add(clueLabel, BorderLayout.SOUTH);

		centerPanel.add(wordPanel, BorderLayout.SOUTH);

		add(centerPanel, BorderLayout.CENTER);

		// Bottom panel - Letters and message
		Panel bottomPanel = new Panel(new BorderLayout());
		bottomPanel.setBackground(Color.DARK_GRAY);

		// Message label
		messageLabel = new Label("Guess a letter to attack! Complete words to damage enemy.", Label.CENTER);
		messageLabel.setFont(GameConstants.HEADER_FONT);
		messageLabel.setForeground(Color.WHITE);
		bottomPanel.add(messageLabel, BorderLayout.NORTH);

		// Letters panel
		lettersPanel = new Panel(new GridLayout(4, 7, 5, 5));
		lettersPanel.setBackground(Color.DARK_GRAY);
		setupLetters();
		bottomPanel.add(lettersPanel, BorderLayout.CENTER);

		// Control buttons
		Panel controlPanel = new Panel(new FlowLayout());
		controlPanel.setBackground(Color.DARK_GRAY);
		Button menuButton = new Button("Main Menu");
		menuButton.setBackground(GameConstants.PRIMARY_COLOR);
		menuButton.setForeground(Color.WHITE);
		menuButton.addActionListener(e -> returnToMenu());
		controlPanel.add(menuButton);

		bottomPanel.add(controlPanel, BorderLayout.SOUTH);

		add(bottomPanel, BorderLayout.SOUTH);

		// Window listener
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				returnToMenu();
			}
		});

		setVisible(true);
	}

	private void setupLetters() {
		lettersPanel.removeAll();

		if (!battleInProgress) {
			// Show continue button when battle is over
			if (battle.isWon()) {
				Button nextButton = new Button("Next Enemy");
				nextButton.setBackground(GameConstants.SUCCESS_COLOR);
				nextButton.setForeground(Color.WHITE);
				nextButton.setFont(GameConstants.BUTTON_FONT);
				nextButton.addActionListener(e -> continueToNextBattle());
				lettersPanel.add(nextButton);
			} else {
				Button tryAgainButton = new Button("Try Again");
				tryAgainButton.setBackground(GameConstants.WARNING_COLOR);
				tryAgainButton.setForeground(Color.WHITE);
				tryAgainButton.setFont(GameConstants.BUTTON_FONT);
				tryAgainButton.addActionListener(e -> continueToNextBattle());
				lettersPanel.add(tryAgainButton);
			}

			Button menuButton = new Button("Main Menu");
			menuButton.setBackground(GameConstants.PRIMARY_COLOR);
			menuButton.setForeground(Color.WHITE);
			menuButton.setFont(GameConstants.BUTTON_FONT);
			menuButton.addActionListener(e -> returnToMenu());
			lettersPanel.add(menuButton);

			// Fill remaining space
			for (int i = 0; i < 24; i++) {
				Panel emptyPanel = new Panel();
				emptyPanel.setBackground(Color.DARK_GRAY);
				lettersPanel.add(emptyPanel);
			}
		} else {
			// Show letter buttons during active battle
			for (char c = 'A'; c <= 'Z'; c++) {
				Button letterButton = new Button(String.valueOf(c));
				letterButton.setFont(GameConstants.BUTTON_FONT);
				letterButton.addActionListener(new LetterButtonListener(c));

				// Disable if already guessed
				if (battle.getIncorrectGuesses().contains(c)) {
					letterButton.setEnabled(false);
					letterButton.setBackground(Color.RED);
					letterButton.setForeground(Color.WHITE);
				} else {
					boolean isGuessed = false;
					for (int i = 0; i < battle.getGuessedLetters().length; i++) {
						if (battle.getEnemy().getWord().charAt(i) == c && battle.getGuessedLetters()[i]) {
							isGuessed = true;
							break;
						}
					}
					if (isGuessed) {
						letterButton.setEnabled(false);
						letterButton.setBackground(Color.GREEN);
						letterButton.setForeground(Color.WHITE);
					} else {
						letterButton.setBackground(Color.WHITE);
						letterButton.setForeground(Color.BLACK);
					}
				}

				lettersPanel.add(letterButton);
			}

			// Fill remaining slots if any
			int remainingSlots = 28 - 26; // 4x7 grid has 28 slots, we used 26 for letters
			for (int i = 0; i < remainingSlots; i++) {
				Panel emptyPanel = new Panel();
				emptyPanel.setBackground(Color.DARK_GRAY);
				lettersPanel.add(emptyPanel);
			}
		}

		lettersPanel.revalidate();
		lettersPanel.repaint();
	}

	private void updateDisplay() {
		wordLabel.setText(battle.getDisplayWord());
		heartsLabel.setText("Hearts: " + battle.getRemainingHearts() + "/" + battle.getMaxHearts());
		enemyHealthLabel
				.setText("Enemy HP: " + battle.getEnemy().getCurrentHealth() + "/" + battle.getEnemy().getMaxHealth());

		// FIXED: Update coins display in real-time
		coinsLabel.setText("Coins: " + player.getCoins());

		// Update clue when new word is generated
		clueLabel.setText("Clue: " + GameConstants.getClueForWord(battle.getEnemy().getWord()));

		// Update level and coins in real-time
		levelLabel.setText("Level: " + player.getLevel());

		// FIXED: Update enemy label for boss battles
		if (battle.getEnemy().isBoss()) {
			enemyLabel.setText("BOSS BATTLE!");
			setTitle("Hangman Battle - Level " + player.getLevel() + " BOSS!");
		} else {
			enemyLabel.setText("Enemy");
			setTitle("Hangman Battle - Level " + player.getLevel());
		}

		if (battle.isBattleOver()) {
			endBattle();
		}

		setupLetters(); // Update letter buttons

		// Force repaint of enemy canvas to show updated image
		enemyCanvas.repaint();
	}

	private void endBattle() {
		battleInProgress = false;

		if (battle.isWon()) {
			int reward = battle.calculateReward();
			player.addCoins(reward);
			player.levelUp();
			FileHandler.updatePlayer(player); // Save immediately

			messageLabel.setText("Victory! You earned " + reward + " coins! Level up to " + player.getLevel() + "!");

			// FIXED: Update coins display immediately after victory
			coinsLabel.setText("Coins: " + player.getCoins());
		} else {
			// Player lost - decrease level by 1 (minimum level 1)
			if (player.getLevel() > 1) {
				player.setLevel(player.getLevel() - 1);
			}
			FileHandler.updatePlayer(player); // Save immediately

			messageLabel.setText("Defeat! Level decreased to " + player.getLevel() + ". Try again!");
		}

		setupLetters(); // Switch to control buttons
	}

	private void continueToNextBattle() {
		startNewBattle();
		battleInProgress = true;

		// Update the display to show new enemy image and name
		updateDisplay();

		messageLabel.setText("Guess a letter to attack! Complete words to damage enemy.");
	}

	private class LetterButtonListener implements ActionListener {
		private char letter;

		public LetterButtonListener(char letter) {
			this.letter = letter;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean correct = battle.makeGuess(letter);

			if (correct) {
				messageLabel
						.setText("Good guess! Enemy took damage. Enemy HP: " + battle.getEnemy().getCurrentHealth());

				// Check if word is complete
				if (battle.isWordComplete()) {
					messageLabel.setText("Word complete! Enemy took extra damage! New word generated. Enemy HP: "
							+ battle.getEnemy().getCurrentHealth());
				}
			} else {
				messageLabel.setText("Wrong guess! You lost a heart. Hearts: " + battle.getRemainingHearts());
			}

			updateDisplay();
		}
	}

	private void returnToMenu() {
		FileHandler.updatePlayer(player);
		new MainMenu(player);
		dispose();
	}
}