package mastermind;

import static mastermind.GameConstants.ALL_COLOURS;
import static mastermind.GameConstants.COLS;
import static mastermind.GameConstants.EMPTY;
import static mastermind.GameConstants.MODE_TWO_PLAYERS;
import static mastermind.GameConstants.PEG_SIZE;
import static mastermind.GameConstants.FRAME_HEIGHT;
import static mastermind.GameConstants.FRAME_WIDTH;
import static mastermind.GameConstants.getImageIcon;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Choice {

    private static final int PANEL_WIDTH = FRAME_WIDTH;
    private static final int PANEL_HEIGHT = FRAME_HEIGHT;

    private final Frame frame;
    private final List<JLabel> emptySlots = new ArrayList<>(COLS);
    private final String[] playerCombination = new String[COLS];
    private int position;

    private final JLabel instructionLabel = new Label("Choose 4 pins from the following colours", 15);
    private final JLabel choiceLabel = new Label("your pattern:", 15);
    private final JLabel submitLabel = new Label("submit_label", "SUBMIT", 15);
    private final JLabel undoLabel = new Label("undo_label", "UNDO", 15);
    private final JLabel backLabel = new Label("back_label", "BACK TO THE MENU", 15);
    private final JLabel errorLabel = new Label("CHOOSE 4 PINS BEFORE YOU SUBMIT!!", 15);

    private final JLabel[] colourLabels = new JLabel[ALL_COLOURS.length];
    private final JPanel panel = new JPanel();

    public Choice(Frame frame) {
        this.frame = frame;
        initComponents();
        initParams();
    }

    private void initParams() {
        for (int i = 0; i < ALL_COLOURS.length; i++) {
            final String colour = ALL_COLOURS[i];
            final JLabel colourLabel = colourLabels[i];
            colourLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    colourLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    colourLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    errorLabel.setVisible(false);
                    onColourSelected(colour);
                }
            });
        }

        undoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (position > 0) {
                    position--;
                    emptySlots.get(position).setIcon(getImageIcon(EMPTY));
                }
            }
        });

        submitLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (position == COLS) {
                    panel.setVisible(false);
                    new Combination(frame, MODE_TWO_PLAYERS,
                            playerCombination[0], playerCombination[1],
                            playerCombination[2], playerCombination[3]);
                } else {
                    errorLabel.setVisible(true);
                }
            }
        });

        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panel.setVisible(false);
                new LaunchPage(frame);
            }
        });
    }

    private void onColourSelected(String colour) {
        if (position < COLS) {
            emptySlots.get(position).setIcon(getImageIcon(colour));
            playerCombination[position] = colour;
            position++;
        }
    }

    private void initComponents() {
        panel.setBackground(Color.darkGray);
        panel.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        panel.setLayout(null);

        instructionLabel.setBounds(0, 120, PANEL_WIDTH, 30);
        choiceLabel.setBounds(0, 350, PANEL_WIDTH, 30);
        undoLabel.setBounds(95, 490, 125, 30);
        submitLabel.setBounds(225, 490, 125, 30);
        backLabel.setBounds(0, 560, PANEL_WIDTH, 30);

        errorLabel.setBounds(0, 453, PANEL_WIDTH, 30);
        errorLabel.setForeground(Color.red);
        errorLabel.setVisible(false);

        int[] colourX = { 80, 150, 220, 290 };
        int[] colourY = { 180, 250 };
        for (int i = 0; i < ALL_COLOURS.length; i++) {
            colourLabels[i] = new PinColour(ALL_COLOURS[i]);
            int row = i / 4;
            int col = i % 4;
            colourLabels[i].setBounds(colourX[col], colourY[row], PEG_SIZE, PEG_SIZE);
        }

        int slotY = 380;
        for (int i = 0; i < COLS; i++) {
            JLabel slot = new PinColour(EMPTY);
            slot.setBounds(80 + i * PEG_SIZE, slotY, PEG_SIZE, PEG_SIZE);
            emptySlots.add(slot);
        }

        panel.add(instructionLabel);
        panel.add(choiceLabel);
        panel.add(submitLabel);
        panel.add(undoLabel);
        panel.add(backLabel);
        panel.add(errorLabel);
        for (JLabel lbl : colourLabels) {
            panel.add(lbl);
        }
        for (JLabel slot : emptySlots) {
            panel.add(slot);
        }

        frame.add(panel);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
