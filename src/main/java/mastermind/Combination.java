package mastermind;

import static mastermind.GameConstants.COLOURS_6;
import static mastermind.GameConstants.COLOURS_8;
import static mastermind.GameConstants.COLS;
import static mastermind.GameConstants.MODE_EASY;
import static mastermind.GameConstants.MODE_EXTREME;
import static mastermind.GameConstants.MODE_HARD;
import static mastermind.GameConstants.MODE_TWO_PLAYERS;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Builds the secret code for a game (either random or from player choice) and
 * starts the main game.
 */
public final class Combination {

    private static final Random RAND = new Random();

    public Combination(Frame frame, int mode, String playerChoice1, String playerChoice2,
            String playerChoice3, String playerChoice4) {
        String[] palette = (mode == MODE_EASY || mode == MODE_HARD) ? COLOURS_6 : COLOURS_8;
        boolean allowDuplicates = (mode == MODE_HARD || mode == MODE_EXTREME);

        String[] secret;
        int serverDuplicate;
        String[] duplicatedColour = new String[2];

        if (mode == MODE_TWO_PLAYERS) {
            secret = new String[] { playerChoice1, playerChoice2, playerChoice3, playerChoice4 };
            serverDuplicate = countAndCollectDuplicates(secret, duplicatedColour);
        } else {
            secret = allowDuplicates
                    ? generateSecretWithDuplicates(palette)
                    : generateSecretNoDuplicates(palette);
            serverDuplicate = countAndCollectDuplicates(secret, duplicatedColour);
        }

        new MainMasterMind(frame, mode,
                secret[0], secret[1], secret[2], secret[3],
                serverDuplicate, duplicatedColour[0], duplicatedColour[1]);
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

    /**
     * Returns duplicate count as in original: for each pair (i,j) with i>j and same
     * colour, add 1.
     * Fills duplicatedColour[0..1] with up to 2 distinct colours that appear more
     * than once.
     */
    private static int countAndCollectDuplicates(String[] secret, String[] duplicatedColour) {
        int count = 0;
        Set<String> duplicateColours = new HashSet<>();
        for (int i = 1; i < COLS; i++) {
            for (int j = 0; j < i; j++) {
                if (secret[j].equals(secret[i])) {
                    count++;
                    duplicateColours.add(secret[i]);
                }
            }
        }
        String[] arr = duplicateColours.toArray(new String[0]);
        if (arr.length > 0)
            duplicatedColour[0] = arr[0];
        if (arr.length > 1)
            duplicatedColour[1] = arr[1];
        return count;
    }
}
