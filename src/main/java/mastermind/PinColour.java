package mastermind;

import static mastermind.GameConstants.getImageIcon;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class PinColour extends JLabel {

    PinColour(String colour) {
        setIcon(getImageIcon(colour));
    }
}
