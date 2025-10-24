package utils;

import models.Player;
import models.Pet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    
    public static void initializeDataFiles() {
        try {
            // Create data directory if it doesn't exist
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            // Create images directory structure
            File imagesDir = new File("images");
            if (!imagesDir.exists()) {
                imagesDir.mkdirs();
                new File("images/pets").mkdirs();
                new File("images/enemies").mkdirs();
                new File("images/ui").mkdirs();
            }
            
            // Initialize players file
            File playersFile = new File(GameConstants.PLAYERS_FILE);
            if (!playersFile.exists()) {
                playersFile.createNewFile();
            }
            
            // Initialize player pets file
            File playerPetsFile = new File(GameConstants.PLAYER_PETS_FILE);
            if (!playerPetsFile.exists()) {
                playerPetsFile.createNewFile();
            }
            
            // Initialize marketplace file with default pets
            File marketplaceFile = new File(GameConstants.MARKETPLACE_FILE);
            if (!marketplaceFile.exists()) {
                initializeMarketplace();
            }
            
        } catch (IOException e) {
            System.err.println("Error initializing data files: " + e.getMessage());
        }
    }
    
    private static void initializeMarketplace() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GameConstants.MARKETPLACE_FILE))) {
            for (int i = 0; i < GameConstants.PET_NAMES.length; i++) {
                writer.write(GameConstants.PET_NAMES[i] + "," + 
                           GameConstants.PET_ATTACKS[i] + "," + 
                           GameConstants.PET_DEFENSES[i] + "," + 
                           GameConstants.PET_PRICES[i]);
                writer.newLine();
            }
        }
    }
    
    // Player data methods
    public static void savePlayer(Player player) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GameConstants.PLAYERS_FILE, true))) {
            writer.write(player.getUsername() + "," + 
                        player.getPassword() + "," + 
                        player.getLevel() + "," + 
                        player.getCoins() + "," + 
                        player.getEquippedPetId());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving player: " + e.getMessage());
        }
    }
    
    public static Player loadPlayer(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(GameConstants.PLAYERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(username) && parts[1].equals(password)) {
                    return new Player(
                        parts[0], 
                        parts[1], 
                        Integer.parseInt(parts[2]), 
                        Integer.parseInt(parts[3]), 
                        Integer.parseInt(parts[4])
                    );
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading player: " + e.getMessage());
        }
        return null;
    }
    
    public static boolean playerExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(GameConstants.PLAYERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking player existence: " + e.getMessage());
        }
        return false;
    }
    
    public static void updatePlayer(Player player) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(GameConstants.PLAYERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(player.getUsername())) {
                    line = player.getUsername() + "," + 
                          player.getPassword() + "," + 
                          player.getLevel() + "," + 
                          player.getCoins() + "," + 
                          player.getEquippedPetId();
                }
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading players for update: " + e.getMessage());
            return;
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GameConstants.PLAYERS_FILE))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating player: " + e.getMessage());
        }
    }
    
    // Pet data methods
    public static void savePlayerPet(String username, int petId) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GameConstants.PLAYER_PETS_FILE, true))) {
            writer.write(username + "," + petId);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving player pet: " + e.getMessage());
        }
    }
    
    public static List<Integer> loadPlayerPets(String username) {
        List<Integer> petIds = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(GameConstants.PLAYER_PETS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username)) {
                    petIds.add(Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading player pets: " + e.getMessage());
        }
        return petIds;
    }
    
    public static List<Pet> loadMarketplacePets() {
        List<Pet> pets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(GameConstants.MARKETPLACE_FILE))) {
            String line;
            int id = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    pets.add(new Pet(
                        id,
                        parts[0],
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])
                    ));
                    id++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading marketplace pets: " + e.getMessage());
        }
        return pets;
    }
}