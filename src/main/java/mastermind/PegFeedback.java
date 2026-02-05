package mastermind;

/**
 * Result of comparing a guess against the secret code (black = correct
 * position, white = wrong position).
 */
public final class PegFeedback {
    private final int black;
    private final int white;

    public PegFeedback(int black, int white) {
        if (black < 0 || white < 0) {
            throw new IllegalArgumentException("black and white must be non-negative");
        }
        this.black = black;
        this.white = white;
    }

    public int getBlack() {
        return black;
    }

    public int getWhite() {
        return white;
    }
}
