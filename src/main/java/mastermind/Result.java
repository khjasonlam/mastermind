package mastermind;

import static mastermind.GameConstants.COLS;
import static mastermind.GameConstants.FRAME_HEIGHT;
import static mastermind.GameConstants.FRAME_WIDTH;
import static mastermind.GameConstants.PEG_SIZE;
import static mastermind.GameConstants.RESULT_LOSE;
import static mastermind.GameConstants.RESULT_WIN;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Result {

    private static final int PANEL_WIDTH = FRAME_WIDTH;
    private static final int PANEL_HEIGHT = FRAME_HEIGHT;

    private final JLabel winStatus = new Label("CONGRATULATIONS!!", 30);
    private final JLabel loseStatus = new Label("GAME OVER!!", 30);
    private final JLabel menuLabel = new Label("menu_label", "BACK TO THE MENU", 15);
    private final JLabel description = new Label(null, 15);
    private final JLabel[] solutionColours = new JLabel[COLS];

    Result(Frame frame, int row, String[] solution, int status) {
        String[] comb = solution.clone();
        final int solutionY = 310;
        final int solutionStartX = 85;
        for (int i = 0; i < COLS; i++) {
            solutionColours[i] = new PinColour(comb[i]);
            solutionColours[i].setBounds(solutionStartX + i * PEG_SIZE, solutionY, PEG_SIZE, PEG_SIZE);
        }

        JPanel panel = new JPanel();
        panel.setBackground(Color.darkGray);
        panel.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        panel.setLayout(null);

        if (status == RESULT_WIN) {
            winStatus.setBounds(0, 210, PANEL_WIDTH, 50);
            loseStatus.setVisible(false);
            description.setText("You finished in row " + (row + 1));
            description.setBounds(0, 270, PANEL_WIDTH, 30);
        } else {
            loseStatus.setBounds(0, 210, PANEL_WIDTH, 50);
            winStatus.setVisible(false);
            description.setBounds(0, 270, PANEL_WIDTH, 30);
            description.setText(status == RESULT_LOSE ? "You could not find the pattern" : "Time's up!");
        }

        menuLabel.setBounds(0, 490, PANEL_WIDTH, 50);
        menuLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                panel.setVisible(false);
                new LaunchPage(frame);
            }
        });

        for (JLabel sc : solutionColours) {
            panel.add(sc);
        }
        panel.add(menuLabel);
        panel.add(winStatus);
        panel.add(loseStatus);
        panel.add(description);

        frame.add(panel);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
