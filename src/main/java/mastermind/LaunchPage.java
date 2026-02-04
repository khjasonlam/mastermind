package mastermind;

import static mastermind.GameConstants.MODE_EASY;
import static mastermind.GameConstants.MODE_EXTREME;
import static mastermind.GameConstants.MODE_HARD;
import static mastermind.GameConstants.MODE_NORMAL;

import mastermind.GameConstants;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class LaunchPage {

    private static final int PANEL_WIDTH = GameConstants.FRAME_WIDTH;
    private static final int PANEL_HEIGHT = GameConstants.FRAME_HEIGHT;

    private static final class ModeConfig {
        final String name;
        final int modeId;
        final String description;

        ModeConfig(String name, int modeId, String description) {
            this.name = name;
            this.modeId = modeId;
            this.description = description;
        }
    }

    private static final ModeConfig[] ONE_PLAYER_MODES = {
            new ModeConfig("EASY", MODE_EASY, "(6 colours without duplicate)"),
            new ModeConfig("NORMAL", MODE_NORMAL, "(8 colours without duplicate)"),
            new ModeConfig("HARD", MODE_HARD, "(6 colours include duplicate)"),
            new ModeConfig("EXTREME", MODE_EXTREME, "(8 colours include duplicate)")
    };

    private static final int[] MODE_Y = { 300, 350, 400, 450 };

    private final Frame frame;
    private final JLabel title = new Label("MASTERMIND", 30);
    private final JLabel onePlayer = new Label("one_player", "ONE PLAYER", 15);
    private final JLabel twoPlayers = new Label("two_players", "TWO PLAYERS", 15);
    private final JLabel instruction = new Label("instruction", "INSTRUCTION", 15);
    private final JLabel description = new Label(null, 15);
    private final JLabel back = new Label("back", "BACK", 15);

    private final JPanel mainPanel;
    private final JPanel modePanel;
    private final JPanel instructionPanel;

    LaunchPage(Frame frame) {
        this.frame = frame;
        this.mainPanel = createPanel();
        this.modePanel = createPanel();
        this.modePanel.setVisible(false);
        this.instructionPanel = createPanel();
        this.instructionPanel.setVisible(false);

        layoutMain();
        layoutModeSubpage();
        layoutInstructionSubpage();
        wireListeners();
        showMain();
        frame.add(mainPanel);
        frame.add(modePanel);
        frame.add(instructionPanel);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private static JPanel createPanel() {
        JPanel p = new JPanel();
        p.setBackground(Color.darkGray);
        p.setSize(PANEL_WIDTH, PANEL_HEIGHT);
        p.setLayout(null);
        return p;
    }

    private void layoutMain() {
        title.setBounds(0, 180, PANEL_WIDTH, 100);
        onePlayer.setBounds(0, 300, PANEL_WIDTH, 20);
        twoPlayers.setBounds(0, 390, PANEL_WIDTH, 20);
        instruction.setBounds(0, 480, PANEL_WIDTH, 20);
    }

    private void layoutModeSubpage() {
        modePanel.add(title);
        modePanel.add(description);
        modePanel.add(back);
        for (int i = 0; i < ONE_PLAYER_MODES.length; i++) {
            JLabel lbl = new Label(ONE_PLAYER_MODES[i].name, 15);
            lbl.setBounds(0, MODE_Y[i], PANEL_WIDTH, 20);
            modePanel.add(lbl);
            attachModeListener(lbl, ONE_PLAYER_MODES[i]);
        }
        description.setBounds(0, 500, PANEL_WIDTH, 30);
        back.setBounds(0, 560, PANEL_WIDTH, 20);
    }

    private void attachModeListener(JLabel modeLabel, ModeConfig config) {
        modeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                modeLabel.setForeground(Color.red);
                modeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                description.setText(config.description);
                description.setForeground(Color.magenta);
                description.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                modeLabel.setForeground(Color.white);
                modeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                description.setVisible(false);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                modePanel.setVisible(false);
                new Combination(frame, config.modeId, null, null, null, null);
            }
        });
    }

    private void layoutInstructionSubpage() {
        String[] lines = {
                "The code breaker may click any combination of coloured",
                "guess pegs on the first line of 4 peg holes.",
                "Computer will verify the combination by using",
                "the white pegs, which indicate correct colour but wrong position,",
                "or the black pegs, which indicate correct colour and position.",
                "If neither are true, no peg should be placed.",
                "Play continues until the code is discovered or",
                "there are no remaining guesses or time."
        };
        instructionPanel.add(title);
        for (int i = 0; i < lines.length; i++) {
            JLabel line = new Label(lines[i], 12);
            line.setBounds(0, 280 + i * 30, PANEL_WIDTH, 30);
            instructionPanel.add(line);
        }
        instructionPanel.add(back);
    }

    private void wireListeners() {
        onePlayer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mainPanel.setVisible(false);
                modePanel.add(title);
                modePanel.add(description);
                modePanel.add(back);
                modePanel.setVisible(true);
            }
        });

        twoPlayers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mainPanel.setVisible(false);
                new Choice(frame);
            }
        });

        instruction.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mainPanel.setVisible(false);
                instructionPanel.add(title);
                instructionPanel.add(back);
                instructionPanel.setVisible(true);
            }
        });

        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                modePanel.setVisible(false);
                instructionPanel.setVisible(false);
                mainPanel.add(title);
                mainPanel.add(onePlayer);
                mainPanel.add(twoPlayers);
                mainPanel.add(instruction);
                mainPanel.setVisible(true);
            }
        });
    }

    private void showMain() {
        mainPanel.add(title);
        mainPanel.add(onePlayer);
        mainPanel.add(twoPlayers);
        mainPanel.add(instruction);
        mainPanel.setVisible(true);
    }
}
