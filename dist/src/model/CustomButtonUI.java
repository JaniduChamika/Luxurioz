/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;

import javax.swing.plaf.basic.BasicButtonUI;


/**
 *
 * @author Janidu
 */
public class CustomButtonUI extends BasicButtonUI {

   private int iconTextGap;

    public CustomButtonUI(int iconTextGap) {
        this.iconTextGap = iconTextGap;
    }

    @Override
    protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
        ButtonModel model = b.getModel();
        FontMetrics fm = b.getFontMetrics(b.getFont());
        int mnemonicIndex = b.getDisplayedMnemonicIndex();

        if (model.isEnabled()) {
            g.setColor(b.getForeground());
        } else {
            g.setColor(new Color(204, 204, 204));
        }

        g.drawString(text, textRect.x + iconTextGap, textRect.y + fm.getAscent());
    }

    //<--------mouse click listeners--------->
    private boolean pressed = false;

    @Override
    protected void installListeners(AbstractButton b) {
        super.installListeners(b);
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
           
                pressed = true;
                b.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                b.repaint();
            }
        });
    }

    @Override
    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        if (pressed) {
            g.setColor(new Color(102, 0, 0)); // Change the background color during click-hold
            g.fillRect(0, 0, b.getWidth(), b.getHeight());
        }
    }
}
