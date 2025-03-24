/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui.custom;

/**
 *
 * @author Janidu
 */
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

public class RoundedCornerPanel extends JPanel {
    private int cornerRadius;

    public RoundedCornerPanel(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        setOpaque(false);  // Make the panel transparent
//        setBorder((Border) new CompoundBorder(new RoundBorder(new Color(91,0,0), cornerRadius), BorderFactory.createEmptyBorder(5, 5, 5, 5)));  // Set the border color and thickness
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();

        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);

        g2d.setColor(getBackground());
        g2d.fill(roundedRectangle);

        g2d.setColor(getForeground());
        g2d.draw(roundedRectangle);

        g2d.dispose();
    }

    private static class RoundBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        public RoundBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(color);
            g2d.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = radius;
            return insets;
        }
    }

    private static class CompoundBorder implements Border {
        private final Border[] borders;

        public CompoundBorder(Border... borders) {
            this.borders = borders;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            for (Border border : borders) {
                border.paintBorder(c, g, x, y, width, height);
            }
        }

        @Override
        public Insets getBorderInsets(Component c) {
            Insets result = new Insets(0, 0, 0, 0);
            for (Border border : borders) {
                Insets insets = border.getBorderInsets(c);
                result.top = Math.max(result.top, insets.top);
                result.left = Math.max(result.left, insets.left);
                result.bottom = Math.max(result.bottom, insets.bottom);
                result.right = Math.max(result.right, insets.right);
            }
            return result;
        }

        @Override
        public boolean isBorderOpaque() {
            for (Border border : borders) {
                if (border.isBorderOpaque()) {
                    return true;
                }
            }
            return false;
        }
    }
}
