/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Desktop;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;


/**
 *
 * @author Janidu
 */
public class StaticComponent {

    public static Icon successIcon = new ImageIcon("src/icon/success.png");
    public static ImageIcon titlebarIcon = new ImageIcon("src/images/logo/fram-logo.png");

    public static String capitalizeFirstLetter(String CapitalWord) {
        if (CapitalWord == null || CapitalWord.isEmpty()) {
            return CapitalWord;
        }
        return Character.toUpperCase(CapitalWord.charAt(0)) + CapitalWord.substring(1).toLowerCase();

    }

    public static void openPDF(String filePath) {
        try {
            File file = new File(filePath);
            Desktop desktop = Desktop.getDesktop();

            if (file.exists() && desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(file);
            } else {
                System.out.println("Unable to open the file.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
}
