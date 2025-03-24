/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;
import java.awt.Graphics;

/**
 *
 * @author Janidu
 */
public class RoundedButton extends JButton {

    private int cornerRadius = 10; // Adjust this value to change the corner radius

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false); // Make the button transparent
        setFocusPainted(false); // Remove focus indication
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Create a rounded rectangle shape for the button
        RoundRectangle2D.Float roundRect = new RoundRectangle2D.Float(
                0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius
        );

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground()); // Set the background color
        g2.fill(roundRect);
        g2.setColor(getForeground()); // Set the text color
        g2.drawString(getText(), 10, 20); // Adjust text position
        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Custom border drawing to maintain rounded appearance
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground()); // Set the border color
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        g2.dispose();
    }

    // Optional: Set the preferred size of the button
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 40); // Adjust the size as needed
    }
}
