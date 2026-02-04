package mastermind;

/**
 * Result of comparing a guess against the secret code (black = correct
 * position, white = wrong position).
 */
public final class PegFeedback {
    private final int black;
    private final int white;

    public PegFeedback(int black, int white) {
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
