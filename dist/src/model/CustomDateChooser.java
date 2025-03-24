/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Janidu
 */
public class CustomDateChooser {

    public static void customizeDateChooser(JDateChooser dateChooser) {

        JTextFieldDateEditor dateEditor = (JTextFieldDateEditor) dateChooser.getDateEditor();
        dateEditor.setBorder(null);
        dateEditor.setEditable(false);
        dateEditor.setBackground(Color.WHITE);
        Dimension monthChooserSize = dateChooser.getJCalendar().getMonthChooser().getPreferredSize();
        monthChooserSize.height = 40; // Set your desired height
        dateChooser.getJCalendar().getMonthChooser().setPreferredSize(monthChooserSize);

        Dimension yearChooserSize = dateChooser.getJCalendar().getYearChooser().getPreferredSize();
        yearChooserSize.height = 40; // Set your desired height
        dateChooser.getJCalendar().getYearChooser().setPreferredSize(yearChooserSize);
        try {
            Field[] fields = JDateChooser.class.getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == JButton.class) {
                    field.setAccessible(true);
                    JButton calButton = (JButton) field.get(dateChooser);
                    calButton.setPreferredSize(new Dimension(40, 40)); // Set your desired width and height

                    ImageIcon customIcon = new ImageIcon("src/icon/icons8-date-32.png");
                    calButton.setIcon(customIcon);
                    break;  // Assuming there's only one JButton field, adjust if needed
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static void customizeOnlyCalander(JDateChooser dateChooser) {

        Dimension monthChooserSize = dateChooser.getJCalendar().getMonthChooser().getPreferredSize();
        monthChooserSize.height = 40; // Set your desired height
        dateChooser.getJCalendar().getMonthChooser().setPreferredSize(monthChooserSize);

        Dimension yearChooserSize = dateChooser.getJCalendar().getYearChooser().getPreferredSize();
        yearChooserSize.height = 40; // Set your desired height
        dateChooser.getJCalendar().getYearChooser().setPreferredSize(yearChooserSize);

        JTextFieldDateEditor dateEditor = (JTextFieldDateEditor) dateChooser.getDateEditor();

//        dateEditor.setEditable(false);
       
    }
}
