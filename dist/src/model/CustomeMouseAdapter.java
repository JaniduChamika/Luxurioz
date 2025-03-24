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
    boolean isActive;

    public CustomeMouseAdapter(JButton button) {
        this.button = button;
        this.newColor = new Color(120, 0, 0);
        this.oldColor = new Color(102, 0, 0);
        this.isFont = false;
        this.isActive = true;

    }

    public CustomeMouseAdapter(JButton button, Color newColor, Color oldColor) {
        this.button = button;
        this.newColor = newColor;
        this.oldColor = oldColor;
        this.isFont = false;
        this.isActive = true;
    }

    public CustomeMouseAdapter(JButton button, Color newColor, Color oldColor, boolean isfont) {
        this.button = button;
        this.newColor = newColor;
        this.oldColor = oldColor;
        this.isFont = isfont;
        this.isActive = true;
    }

    public void setState(boolean i) {
        this.isActive = i;
    }
    LoadingIndicator li = new LoadingIndicator();

    @Override
    public void mouseEntered(MouseEvent e) {
        // Change the background color when the mouse enters

        if (button.isEnabled()) {
            li.setHandCursor(button);

            if (isActive) {

                if (isFont) {
                    this.button.setForeground(newColor);
                } else {
                    this.button.setBackground(newColor);

                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Restore the original background color when the mouse exits
        li.resetCursor(button);
        if (isActive) {
            if (isFont) {
                this.button.setForeground(oldColor);
            } else {
                this.button.setBackground(oldColor);

            }
        }

    }
}
