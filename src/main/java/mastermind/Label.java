package mastermind;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Label extends JLabel {

    private static final Cursor HAND = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    private static final Cursor DEFAULT = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    Label(String name, String text, int fontsize) {
        setText(text);
        setFont(new Font("Rockwell", Font.BOLD, fontsize));
        setForeground(Color.white);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        if (name != null)
            setName(name);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(Color.red);
                setCursor(HAND);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(Color.white);
                setCursor(DEFAULT);
            }
        });
    }

    Label(String text, int fontsize) {
        setText(text);
        setFont(new Font("Rockwell", Font.BOLD, fontsize));
        setForeground(Color.white);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }
}
