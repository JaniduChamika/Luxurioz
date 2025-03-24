package gui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class ProgressBarCustom extends JProgressBar {

    public Color getColorString() {
        return colorString;
    }

    public void setColorString(Color colorString) {
        this.colorString = colorString;
    }

    private Color colorString = new Color(200, 200, 200);

    public ProgressBarCustom() {
        setPreferredSize(new Dimension(100, 10));
        setBackground(new Color(255, 255, 255));
        setForeground(new Color(69, 124, 235));
        setUI(new CustomProgressBar());
    }
}

class CustomProgressBar extends BasicProgressBarUI {
//      @Override
//            protected void paintString(Graphics grphcs, int i, int i1, int i2, int i3, int i4, Insets insets) {
//                grphcs.setColor(new Color(69, 124, 235));
//                super.paintString(grphcs, i, i1, i2, i3, i4, insets);
//            }

    private final int cornerRadius=10;

//     CustomProgressBar(10) {
//        this.cornerRadius = cornerRadius;
//    }

    @Override
    protected void paintDeterminate(Graphics g, JComponent c) {
       super.paintDeterminate(g, c);

        JProgressBar progressBar = (JProgressBar) c;
        int barWidth = progressBar.getWidth();
        int barHeight = progressBar.getHeight();

        // Custom colors
        Color background = new Color(240, 240, 240); // Background color
        Color progressColor = new Color(0, 128, 255); // Progress color

        Graphics2D g2d = (Graphics2D) g.create();

        // Paint background
        g2d.setColor(background);
        g2d.fillRoundRect(0, 0, barWidth, barHeight, cornerRadius, cornerRadius);

        // Paint progress
        int width = (int) (barWidth * progressBar.getPercentComplete());
        g2d.setColor(progressColor);
        g2d.fillRoundRect(0, 0, width, barHeight, cornerRadius, cornerRadius);

        g2d.dispose();

    }
}
