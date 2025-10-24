package utils;

import java.util.List;
import java.util.Random;

public class WordGenerator {
	private static final Random random = new Random();

	public static String generateWord(int level) {
		List<String> wordList;

		if (level <= 5) {
			wordList = GameConstants.EASY_WORDS;
		} else if (level <= 10) {
			wordList = GameConstants.MEDIUM_WORDS;
		} else {
			wordList = GameConstants.HARD_WORDS;
		}

		return wordList.get(random.nextInt(wordList.size()));
	}

	public static boolean isBossLevel(int level) {
		for (int bossLevel : GameConstants.BOSS_LEVELS) {
			if (level == bossLevel) {
				return true;
			}
		}
		return false;
	}

	public static int calculateReward(int level) {
		if (isBossLevel(level)) {
			return level * 15; // Extra reward for boss levels
		}
		return level * 8;
	}

	public static String getClueForWord(String word) {
		return GameConstants.getClueForWord(word);
	}
}