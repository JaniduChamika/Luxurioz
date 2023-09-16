/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 *
 * @author Janidu
 */
public class CustomeMouseAdapter extends MouseAdapter {

    JButton button;
    Color newColor;
    Color oldColor;
    boolean isFont;

    public CustomeMouseAdapter(JButton button) {
        this.button = button;
        this.newColor = new Color(120, 0, 0);
        this.oldColor = new Color(102, 0, 0);
        this.isFont = false;
    }

    public CustomeMouseAdapter(JButton button, Color newColor, Color oldColor) {
        this.button = button;
        this.newColor = newColor;
        this.oldColor = oldColor;
        this.isFont = false;

    }

    public CustomeMouseAdapter(JButton button, Color newColor, Color oldColor, boolean isfont) {
        this.button = button;
        this.newColor = newColor;
        this.oldColor = oldColor;
        this.isFont = isfont;

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Change the background color when the mouse enters
        if (isFont) {
            this.button.setForeground(newColor);
        } else {
            this.button.setBackground(newColor);

        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Restore the original background color when the mouse exits
        if (isFont) {
            this.button.setForeground(oldColor);
        } else {
            this.button.setBackground(oldColor);

        }
    }
}
