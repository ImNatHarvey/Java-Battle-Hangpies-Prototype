package gui;

import models.Player;
import utils.FileHandler;
import utils.GameConstants;
import utils.ImageUtils;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class LoginScreen extends Frame {
	private TextField usernameField;
	private TextField passwordField;
	private Button loginButton;
	private Button registerButton;
	private Label messageLabel;

	public LoginScreen() {
		setupUI();
		setupEvents();
	}

	private void setupUI() {
		setTitle("Hangman Battle Game - Login");
		setSize(400, 300);
		setResizable(false);
		setLayout(new GridBagLayout());
		setBackground(GameConstants.BACKGROUND_COLOR);

		// Center the window on screen
		setLocationRelativeTo(null);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Title
		Label titleLabel = new Label("Hangman Battle Game", Label.CENTER);
		titleLabel.setFont(GameConstants.TITLE_FONT);
		titleLabel.setForeground(GameConstants.PRIMARY_COLOR);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		add(titleLabel, gbc);

		// Username
		gbc.gridwidth = 1;
		gbc.gridy = 1;
		gbc.gridx = 0;
		add(new Label("Username:"), gbc);

		gbc.gridx = 1;
		usernameField = new TextField(20);
		add(usernameField, gbc);

		// Password
		gbc.gridy = 2;
		gbc.gridx = 0;
		add(new Label("Password:"), gbc);

		gbc.gridx = 1;
		passwordField = new TextField(20);
		passwordField.setEchoChar('*');
		add(passwordField, gbc);

		// Message Label
		gbc.gridy = 3;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		messageLabel = new Label("", Label.CENTER);
		messageLabel.setForeground(GameConstants.ACCENT_COLOR);
		add(messageLabel, gbc);

		// Buttons
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		loginButton = new Button("Login");
		loginButton.setFont(GameConstants.BUTTON_FONT);
		loginButton.setBackground(GameConstants.PRIMARY_COLOR);
		loginButton.setForeground(Color.WHITE);
		add(loginButton, gbc);

		gbc.gridx = 1;
		registerButton = new Button("Register");
		registerButton.setFont(GameConstants.BUTTON_FONT);
		registerButton.setBackground(GameConstants.SECONDARY_COLOR);
		registerButton.setForeground(Color.WHITE);
		add(registerButton, gbc);

		// Window listener for closing
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});

		setVisible(true);
	}

	private void setupEvents() {
		loginButton.addActionListener(e -> login());
		registerButton.addActionListener(e -> register());

		// Enter key support
		passwordField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login();
				}
			}
		});
	}

	private void login() {
		String username = usernameField.getText().trim();
		String password = passwordField.getText();

		if (username.isEmpty() || password.isEmpty()) {
			showMessage("Please enter both username and password");
			return;
		}

		Player player = FileHandler.loadPlayer(username, password);
		if (player != null) {
			// Load player's pets
			player.setOwnedPets(FileHandler.loadPlayerPets(username));
			showMessage("Login successful!");
			new MainMenu(player);
			dispose();
		} else {
			showMessage("Invalid username or password");
		}
	}

	private void register() {
		String username = usernameField.getText().trim();
		String password = passwordField.getText();

		if (username.isEmpty() || password.isEmpty()) {
			showMessage("Please enter both username and password");
			return;
		}

		if (username.length() < 3) {
			showMessage("Username must be at least 3 characters");
			return;
		}

		if (password.length() < 4) {
			showMessage("Password must be at least 4 characters");
			return;
		}

		if (FileHandler.playerExists(username)) {
			showMessage("Username already exists");
			return;
		}

		Player newPlayer = new Player(username, password);
		FileHandler.savePlayer(newPlayer);
		showMessage("Registration successful! Please login.");

		// Clear fields
		passwordField.setText("");
	}

	private void showMessage(String message) {
		messageLabel.setText(message);
	}
}