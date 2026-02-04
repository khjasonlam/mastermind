package mastermind;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Frame extends JFrame {

    public Frame(String title, int width, int height) {
        setTitle(title);
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
