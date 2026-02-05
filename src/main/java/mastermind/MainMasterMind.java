package mastermind;

import static mastermind.GameConstants.ALL_COLOURS;
import static mastermind.GameConstants.BOTTOM_PANEL_HEIGHT;
import static mastermind.GameConstants.BOTTOM_PANEL_Y;
import static mastermind.GameConstants.CENTRE_LEFT;
import static mastermind.GameConstants.CENTRE_PANEL_WIDTH;
import static mastermind.GameConstants.COLS;
import static mastermind.GameConstants.EMPTY;
import static mastermind.GameConstants.EMPTY_SMALL;
import static mastermind.GameConstants.FRAME_HEIGHT;
import static mastermind.GameConstants.FRAME_WIDTH;
import static mastermind.GameConstants.HINT_COL2;
import static mastermind.GameConstants.HINT_LEFT;
import static mastermind.GameConstants.MODE_DISPLAY_NAMES;
import static mastermind.GameConstants.MODE_EASY;
import static mastermind.GameConstants.MODE_HARD;
import static mastermind.GameConstants.PEG_SIZE;
import static mastermind.GameConstants.PEG_SMALL_SIZE;
import static mastermind.GameConstants.RESULT_LOSE;
import static mastermind.GameConstants.RESULT_TIME_UP;
import static mastermind.GameConstants.RESULT_WIN;
import static mastermind.GameConstants.ROWS;
import static mastermind.GameConstants.ROW_HEIGHT;
import static mastermind.GameConstants.SIDE_PANEL_WIDTH;
import static mastermind.GameConstants.TOTAL_PEGS;
import static mastermind.GameConstants.getImageIcon;
import static mastermind.MastermindEvaluator.evaluate;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MainMasterMind implements ActionListener {

    private static final int TIMER_MINUTES = 5;
    private static final int TIMER_SECONDS = 0;

    private final Frame frame;
    private final int mode;
    private final SecretCode secretCode;

    private JPanel centrePanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel bottomPanel;

    private List<JLabel> pegSlots;
    private List<JLabel> hints;
    private static final List<Integer> ROW_END_POSITIONS = buildRowEndPositions();
    private String[] currentGuess = new String[COLS];

    private Timer timer;
    private int seconds;
    private int minutes;
    private int currentSlotIndex;
    private int guessPosition;
    private int currentRow;
    private boolean timerStarted;

    private final JLabel[] colourLabels = new JLabel[ALL_COLOURS.length];
    private JLabel undoLabel;
    private JLabel exitLabel;
    private JLabel timerLabel = new Label(null, 20);
    private JLabel modeLabel = new Label(null, 15);

    public MainMasterMind(Frame frame, int mode, SecretCode secret) {
        this.frame = frame;
        this.mode = mode;
        this.secretCode = secret;

        initComponents();
        initParams();
    }

    private static int centreX(int index) {
        return CENTRE_LEFT + (index % COLS) * PEG_SIZE;
    }

    private static int centreY(int index) {
        return (ROWS - 1 - index / COLS) * ROW_HEIGHT;
    }

    private static int hintX(int index) {
        int c = index % COLS;
        return c % 2 == 0 ? HINT_LEFT : HINT_COL2;
    }

    private static int hintY(int index) {
        int row = index / COLS;
        int sub = (index % COLS) >= 2 ? 25 : 0;
        return (ROWS - 1 - row) * ROW_HEIGHT + sub;
    }

    private static List<Integer> buildRowEndPositions() {
        List<Integer> list = new ArrayList<>(ROWS);
        for (int r = 1; r <= ROWS; r++) {
            list.add(r * COLS - 1);
        }
        return Collections.unmodifiableList(list);
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
                    onColourSelected(colour);
                }
            });
        }

        undoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int rowStartSlot = currentRow * COLS;
                if (currentSlotIndex > rowStartSlot && guessPosition > 0) {
                    currentSlotIndex--;
                    guessPosition--;
                    pegSlots.get(currentSlotIndex).setIcon(getImageIcon(EMPTY));
                }
            }
        });

        exitLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (timer != null)
                    timer.stop();
                setPanelsVisible(false);
                new LaunchPage(frame);
            }
        });

        if (!timerStarted) {
            startTimer();
        }
    }

    private void startTimer() {
        seconds = TIMER_SECONDS;
        minutes = TIMER_MINUTES;
        timer = new Timer(1000, this);
        timer.start();
        timerStarted = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        if (seconds == 0) {
            minutes--;
            seconds = 59;
        } else {
            seconds--;
        }
        if (seconds == 0 && minutes == 0) {
            if (timer != null)
                timer.stop();
            timerLabel.setText("TIME'S UP");
            setPanelsVisible(false);
            showResult(RESULT_TIME_UP);
        }
    }

    private void setPanelsVisible(boolean visible) {
        bottomPanel.setVisible(visible);
        leftPanel.setVisible(visible);
        rightPanel.setVisible(visible);
        centrePanel.setVisible(visible);
    }

    private void onColourSelected(String colour) {
        if (currentSlotIndex < TOTAL_PEGS) {
            pegSlots.get(currentSlotIndex).setIcon(getImageIcon(colour));
            currentGuess[guessPosition] = colour;
            guessPosition++;
            currentSlotIndex++;
        }
        if (ROW_END_POSITIONS.contains(currentSlotIndex - 1)) {
            guessPosition = 0;
            verifyAndShowHints();
            currentRow++;
        }
    }

    private void verifyAndShowHints() {
        PegFeedback feedback = evaluate(secretCode, currentGuess);
        int numWhite = feedback.getWhite();
        int numBlack = feedback.getBlack();

        int hintStart = currentRow * COLS;
        for (int i = 0; i < numWhite; i++) {
            setHintIcon(hintStart + i, "White");
        }
        for (int j = numWhite; j < numBlack + numWhite; j++) {
            setHintIcon(hintStart + j, "Black");
        }

        if (numBlack == COLS) {
            stopTimerAndShowResult(RESULT_WIN);
        } else if (currentSlotIndex >= TOTAL_PEGS) {
            stopTimerAndShowResult(RESULT_LOSE);
        }
    }

    private void stopTimerAndShowResult(int resultStatus) {
        if (timer != null)
            timer.stop();
        setPanelsVisible(false);
        showResult(resultStatus);
    }

    private void showResult(int resultStatus) {
        new Result(frame, currentRow, secretCode.getColours(), resultStatus);
    }

    private void setHintIcon(int pos, String colour) {
        hints.get(pos).setIcon(getImageIcon(colour));
    }

    private void initComponents() {
        centrePanel = new JPanel();
        centrePanel.setBackground(Color.darkGray);
        centrePanel.setBounds(SIDE_PANEL_WIDTH, 0, CENTRE_PANEL_WIDTH, FRAME_HEIGHT);
        centrePanel.setLayout(null);

        leftPanel = new JPanel();
        leftPanel.setBackground(Color.darkGray);
        leftPanel.setBounds(0, 0, SIDE_PANEL_WIDTH, FRAME_HEIGHT);
        leftPanel.setLayout(null);

        rightPanel = new JPanel();
        rightPanel.setBackground(Color.darkGray);
        rightPanel.setBounds(SIDE_PANEL_WIDTH + CENTRE_PANEL_WIDTH, 0, SIDE_PANEL_WIDTH, FRAME_HEIGHT);
        rightPanel.setLayout(null);

        bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.darkGray);
        bottomPanel.setBounds(0, BOTTOM_PANEL_Y, FRAME_WIDTH, BOTTOM_PANEL_HEIGHT);
        bottomPanel.setLayout(null);

        pegSlots = new ArrayList<>(TOTAL_PEGS);
        for (int i = 0; i < TOTAL_PEGS; i++) {
            JLabel lbl = new PinColour(EMPTY);
            lbl.setBounds(centreX(i), centreY(i), PEG_SIZE, PEG_SIZE);
            centrePanel.add(lbl);
            pegSlots.add(lbl);
        }

        hints = new ArrayList<>(TOTAL_PEGS);
        for (int i = 0; i < TOTAL_PEGS; i++) {
            JLabel lbl = new PinColour(EMPTY_SMALL);
            lbl.setBounds(hintX(i), hintY(i), PEG_SMALL_SIZE, PEG_SMALL_SIZE);
            rightPanel.add(lbl);
            hints.add(lbl);
        }

        for (int i = 0; i < ALL_COLOURS.length; i++) {
            colourLabels[i] = new PinColour(ALL_COLOURS[i]);
        }

        if (mode == MODE_EASY || mode == MODE_HARD) {
            colourLabels[0].setBounds(5, 70, PEG_SIZE, PEG_SIZE);
            colourLabels[1].setVisible(false);
            colourLabels[2].setBounds(5, 150, PEG_SIZE, PEG_SIZE);
            colourLabels[3].setBounds(5, 230, PEG_SIZE, PEG_SIZE);
            colourLabels[4].setBounds(5, 310, PEG_SIZE, PEG_SIZE);
            colourLabels[5].setVisible(false);
            colourLabels[6].setBounds(5, 390, PEG_SIZE, PEG_SIZE);
            colourLabels[7].setBounds(5, 470, PEG_SIZE, PEG_SIZE);
        } else {
            colourLabels[1].setVisible(true);
            colourLabels[5].setVisible(true);
            colourLabels[0].setBounds(5, 20, PEG_SIZE, PEG_SIZE);
            colourLabels[1].setBounds(5, 90, PEG_SIZE, PEG_SIZE);
            colourLabels[2].setBounds(5, 160, PEG_SIZE, PEG_SIZE);
            colourLabels[3].setBounds(5, 230, PEG_SIZE, PEG_SIZE);
            colourLabels[4].setBounds(5, 300, PEG_SIZE, PEG_SIZE);
            colourLabels[5].setBounds(5, 370, PEG_SIZE, PEG_SIZE);
            colourLabels[6].setBounds(5, 440, PEG_SIZE, PEG_SIZE);
            colourLabels[7].setBounds(5, 510, PEG_SIZE, PEG_SIZE);
        }

        for (JLabel lbl : colourLabels) {
            leftPanel.add(lbl);
        }

        undoLabel = new Label("undo_label", "UNDO", 15);
        undoLabel.setBounds(9, 35, 70, 20);
        bottomPanel.add(undoLabel);

        exitLabel = new Label("exit_label", "EXIT", 15);
        exitLabel.setBounds(372, 35, 70, 20);
        bottomPanel.add(exitLabel);

        modeLabel.setText(mode >= 0 && mode < MODE_DISPLAY_NAMES.length ? MODE_DISPLAY_NAMES[mode] : "");
        modeLabel.setBounds(0, 35, FRAME_WIDTH, 20);
        bottomPanel.add(modeLabel);
        timerLabel.setBounds(0, 5, FRAME_WIDTH, 30);
        bottomPanel.add(timerLabel);

        frame.add(bottomPanel);
        frame.add(centrePanel);
        frame.add(leftPanel);
        frame.add(rightPanel);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
