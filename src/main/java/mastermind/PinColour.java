package mastermind;

import static mastermind.GameConstants.getImageIcon;

import javax.swing.JLabel;

public class PinColour extends JLabel {

    PinColour(String colour) {
        setIcon(getImageIcon(colour));
    }
}
