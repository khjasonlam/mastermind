package mastermind;

import static mastermind.GameConstants.COLS;

import java.util.HashSet;
import java.util.Set;

/**
 * Holds the secret code (4 colours) and precomputed duplicate information
 * used for evaluation in hard/extreme modes.
 */
public final class SecretCode {

    private final String[] colours;
    private final int serverDuplicate;
    private final String[] duplicatedColour;

    public SecretCode(String[] colours) {
        if (colours == null || colours.length != COLS) {
            throw new IllegalArgumentException("colours must be non-null and length " + COLS);
        }
        this.colours = colours.clone();
        this.duplicatedColour = new String[2];
        this.serverDuplicate = countAndCollectDuplicates(this.colours, this.duplicatedColour);
    }

    /** Returns a copy of the secret colours (length COLS). */
    public String[] getColours() {
        return colours.clone();
    }

    public int getServerDuplicate() {
        return serverDuplicate;
    }

    /** Returns a copy of the array of up to 2 duplicated colour names. */
    public String[] getDuplicatedColour() {
        return duplicatedColour.clone();
    }

    /**
     * Returns duplicate count: for each pair (i,j) with i>j and same colour, add 1.
     * Fills duplicatedColour[0..1] with up to 2 distinct colours that appear more than once.
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
        if (arr.length > 0) duplicatedColour[0] = arr[0];
        if (arr.length > 1) duplicatedColour[1] = arr[1];
        return count;
    }
}
