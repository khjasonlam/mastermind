package mastermind;

import static mastermind.GameConstants.COLS;

/**
 * Evaluates a guess against the secret code and returns black/white peg counts.
 * Handles duplicate colours in the secret for hard/extreme modes.
 */
public final class MastermindEvaluator {

    private MastermindEvaluator() {
    }

    /**
     * Computes feedback for a guess against the secret.
     *
     * @param secret           the secret code (length COLS)
     * @param guess            the current guess (length COLS)
     * @param serverDuplicate  number of duplicate colour groups in the secret (0,
     *                         1, 2, 3, or 6)
     * @param duplicatedColour up to 2 colour names that are duplicated in the
     *                         secret (for adjustment)
     */
    public static PegFeedback evaluate(String[] secret, String[] guess,
            int serverDuplicate, String[] duplicatedColour) {
        int numBlack = 0;
        int numWhite = 0;
        int playerDuplicate = 0;

        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (secret[i].equals(guess[j]) && (i == j)) {
                    numBlack++;
                    if (playerDuplicate > 0) {
                        numWhite -= playerDuplicate;
                    }
                    playerDuplicate = 0;
                    break;
                } else if (secret[i].equals(guess[j])) {
                    numWhite++;
                    playerDuplicate++;
                    if (j == 3) {
                        numWhite = numWhite - playerDuplicate + 1;
                        playerDuplicate = 0;
                    }
                } else if ((j == 3) && (playerDuplicate > 0)) {
                    numWhite = numWhite - playerDuplicate + 1;
                    playerDuplicate = 0;
                }
            }
        }

        int sameColour1 = 0;
        int sameColour2 = 0;
        if (serverDuplicate == 6) {
            numWhite = 0;
        }
        if (serverDuplicate == 3 && duplicatedColour != null && duplicatedColour[0] != null) {
            for (int i = 0; i < COLS; i++) {
                if (duplicatedColour[0].equals(guess[i])) {
                    sameColour1++;
                }
            }
            numWhite = numWhite - (serverDuplicate - sameColour1);
            if (sameColour1 == 4) {
                numWhite -= 1;
            } else if (sameColour1 == 0) {
                numWhite += 3;
            }
        }
        if (serverDuplicate == 2 && duplicatedColour != null && duplicatedColour[0] != null
                && duplicatedColour[1] != null) {
            for (int i = 0; i < COLS; i++) {
                if (duplicatedColour[0].equals(guess[i])) {
                    sameColour1++;
                } else if (duplicatedColour[1].equals(guess[i])) {
                    sameColour2++;
                }
            }
            if ((sameColour1 == 1) && (sameColour2 == 1)) {
                numWhite -= 2;
            } else if ((sameColour1 == 3 && sameColour2 == 1) || (sameColour1 == 1 && sameColour2 == 3)
                    || (sameColour1 == 2 && sameColour2 == 1) || (sameColour1 == 1 && sameColour2 == 2)
                    || (sameColour1 == 1 && sameColour2 == 0) || (sameColour1 == 0 && sameColour2 == 1)) {
                numWhite -= 1;
            }
        }
        if (serverDuplicate == 1 && duplicatedColour != null && duplicatedColour[0] != null) {
            for (int i = 0; i < COLS; i++) {
                if (duplicatedColour[0].equals(guess[i])) {
                    sameColour1++;
                }
            }
            if (sameColour1 == 1) {
                numWhite -= 1;
            }
        }

        return new PegFeedback(numBlack, numWhite);
    }
}
