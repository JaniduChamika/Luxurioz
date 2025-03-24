/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;

/**
 *
 * @author Janidu
 */

public class LoadingIndicator {

    public static void setWaitCursor(Component component) {
        Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        setCursorForComponent(component, waitCursor);
    }
  public static void setHandCursor(Component component) {
        Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        setCursorForComponent(component, waitCursor);
    }
    public static void resetCursor(Component component) {
        Cursor defaultCursor = Cursor.getDefaultCursor();
        setCursorForComponent(component, defaultCursor);
    }

    public static void setCursorForComponent(Component component, Cursor cursor) {
        component.setCursor(cursor);
        if (component instanceof Container) {
            Component[] children = ((Container) component).getComponents();
            for (Component child : children) {
                setCursorForComponent(child, cursor);
            }
        }
    }
}
