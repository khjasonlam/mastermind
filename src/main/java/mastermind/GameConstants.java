package mastermind;

import javax.swing.ImageIcon;

/**
 * Shared game constants for Mastermind.
 */
public final class GameConstants {

    /** Classpath path for images (use with getResource). */
    public static final String IMAGE_PATH = "/images/";

    /** Main window size. */
    public static final int FRAME_WIDTH = 450;
    public static final int FRAME_HEIGHT = 700;

    public static final int ROWS = 10;
    public static final int COLS = 4;
    public static final int TOTAL_PEGS = ROWS * COLS; // 40
    public static final int PEG_SIZE = 70;
    public static final int PEG_SMALL_SIZE = 50;
    public static final int ROW_HEIGHT = 60;
    public static final int CENTRE_LEFT = 10;
    public static final int HINT_LEFT = 5;
    public static final int HINT_COL2 = 30;

    /** Colours for 6-colour mode (easy/hard). */
    public static final String[] COLOURS_6 = {
            "Red", "Blue", "Orange", "Violet", "Green", "Yellow"
    };

    /** Colours for 8-colour mode (normal/extreme). */
    public static final String[] COLOURS_8 = {
            "Red", "Blue", "Orange", "Violet", "Green", "Yellow", "LightGreen", "Rose"
    };

    /** All pin colours used in the UI (for two-player and palette). */
    public static final String[] ALL_COLOURS = {
            "Red", "Rose", "Orange", "Yellow", "Green", "LightGreen", "Blue", "Violet"
    };

    public static final String EMPTY = "Empty";
    public static final String EMPTY_SMALL = "Empty_Small";

    /**
     * Loads an image from classpath (e.g. "Red", "Empty"). Returns null if not
     * found.
     */
    public static ImageIcon getImageIcon(String name) {
        if (name == null)
            return null;
        java.net.URL url = GameConstants.class.getResource(IMAGE_PATH + name + ".png");
        return url != null ? new ImageIcon(url) : null;
    }

    /** Game result status. */
    public static final int RESULT_WIN = 1;
    public static final int RESULT_LOSE = 2;
    public static final int RESULT_TIME_UP = 3;

    /** Game mode. */
    public static final int MODE_EASY = 1;
    public static final int MODE_NORMAL = 2;
    public static final int MODE_HARD = 3;
    public static final int MODE_EXTREME = 4;
    public static final int MODE_TWO_PLAYERS = 5;

    private GameConstants() {
    }
}
