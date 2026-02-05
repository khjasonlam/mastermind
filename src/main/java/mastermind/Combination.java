package mastermind;

import static mastermind.GameConstants.COLOURS_6;
import static mastermind.GameConstants.COLOURS_8;
import static mastermind.GameConstants.COLS;
import static mastermind.GameConstants.MODE_EASY;
import static mastermind.GameConstants.MODE_EXTREME;
import static mastermind.GameConstants.MODE_HARD;
import static mastermind.GameConstants.MODE_TWO_PLAYERS;

import java.util.Random;

/**
 * Builds the secret code for a game (either random or from player choice) and
 * starts the main game.
 */
public final class Combination {

    private static final Random RAND = new Random();

    /**
     * @param frame         the main frame
     * @param mode          game mode constant
     * @param playerChoices for two-player mode: length-COLS array of chosen colours; for one-player: null
     */
    public Combination(Frame frame, int mode, String[] playerChoices) {
        String[] palette = (mode == MODE_EASY || mode == MODE_HARD) ? COLOURS_6 : COLOURS_8;
        boolean allowDuplicates = (mode == MODE_HARD || mode == MODE_EXTREME);

        String[] secret;
        if (mode == MODE_TWO_PLAYERS && playerChoices != null) {
            secret = playerChoices.clone();
        } else {
            secret = allowDuplicates
                    ? generateSecretWithDuplicates(palette)
                    : generateSecretNoDuplicates(palette);
        }

        SecretCode secretCode = new SecretCode(secret);
        new MainMasterMind(frame, mode, secretCode);
    }

    private static String[] generateSecretNoDuplicates(String[] palette) {
        String[] secret = new String[COLS];
        for (int i = 0; i < COLS; i++) {
            String colour;
            do {
                colour = palette[RAND.nextInt(palette.length)];
            } while (alreadyUsed(secret, i, colour));
            secret[i] = colour;
        }
        return secret;
    }

    private static boolean alreadyUsed(String[] secret, int length, String colour) {
        for (int j = 0; j < length; j++) {
            if (secret[j].equals(colour))
                return true;
        }
        return false;
    }

    private static String[] generateSecretWithDuplicates(String[] palette) {
        String[] secret = new String[COLS];
        for (int i = 0; i < COLS; i++) {
            secret[i] = palette[RAND.nextInt(palette.length)];
        }
        return secret;
    }

}
