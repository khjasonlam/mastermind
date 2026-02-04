package mastermind;

import static mastermind.GameConstants.FRAME_HEIGHT;
import static mastermind.GameConstants.FRAME_WIDTH;

public class Main {

    public static void main(String[] args) {
        Frame frame = new Frame("MASTERMIND", FRAME_WIDTH, FRAME_HEIGHT);
        new LaunchPage(frame);
    }
}
