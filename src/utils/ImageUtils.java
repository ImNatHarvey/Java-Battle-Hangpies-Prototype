package utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class ImageUtils {
	private static final Random random = new Random();

	public static Image loadImage(String path, int width, int height) {
		try {
			File file = new File(path);
			if (file.exists()) {
				Image image = ImageIO.read(file);
				return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			} else {
				// Generate placeholder if image doesn't exist
				return generatePlaceholder(width, height, getColorForPath(path));
			}
		} catch (IOException e) {
			System.err.println("Error loading image: " + path);
			return generatePlaceholder(width, height, Color.GRAY);
		}
	}

	public static Image generatePlaceholder(int width, int height, Color color) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();

		// Set background
		g2d.setColor(color);
		g2d.fillRect(0, 0, width, height);

		// Set border
		g2d.setColor(color.darker());
		g2d.setStroke(new BasicStroke(3));
		g2d.drawRect(0, 0, width - 1, height - 1);

		// Set text
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Arial", Font.BOLD, 12));
		String text = width + "x" + height;
		FontMetrics fm = g2d.getFontMetrics();
		int textWidth = fm.stringWidth(text);
		int textHeight = fm.getHeight();
		g2d.drawString(text, (width - textWidth) / 2, (height + textHeight) / 2 - 5);

		g2d.dispose();
		return image;
	}

	private static Color getColorForPath(String path) {
		if (path.contains("pets")) {
			return Color.ORANGE;
		} else if (path.contains("enemies")) {
			return Color.RED;
		} else if (path.contains("ui")) {
			return Color.BLUE;
		}
		return Color.GRAY;
	}

	public static String getPetImagePath(int petId) {
		String[] petImageNames = { "dragon.png", "wolf.png", "bird.png", "golem.png", "serpent.png", "fox.png",
				"panther.png", "unicorn.png", "knight.png", "treant.png" };

		if (petId >= 0 && petId < petImageNames.length) {
			return GameConstants.PETS_DIR + petImageNames[petId];
		}
		return GameConstants.PETS_DIR + "default.png";
	}

	public static String getEnemyImagePath(int level, boolean isBoss) {
		if (isBoss) {
			int bossIndex = (level / 5) - 1;
			String[] bossImages = { "boss1.png", "boss2.png", "boss3.png", "boss4.png" };
			return GameConstants.ENEMIES_DIR + bossImages[Math.min(bossIndex, bossImages.length - 1)];
		} else {
			// Randomize regular enemy images per battle
			String[] enemyImages = { "enemy1.png", "enemy2.png", "enemy3.png", "enemy4.png", "enemy5.png" };
			int randomIndex = random.nextInt(enemyImages.length);
			return GameConstants.ENEMIES_DIR + enemyImages[randomIndex];
		}
	}
}