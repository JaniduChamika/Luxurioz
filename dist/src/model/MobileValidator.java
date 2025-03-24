/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.regex.Pattern;


/**
 *
 * @author Janidu
 */
public class MobileValidator {

    public boolean mobileValidate(String newtext) {

        if (newtext.length() == 1) {
            if (!Pattern.compile("0").matcher(newtext).matches()) {
                return true;

            } else {
                return false;

            }

        } else if (newtext.length() == 2) {
            if (!Pattern.compile("07").matcher(newtext).matches()) {
                return true;

            } else {
                return false;

            }

        } else if (newtext.length() == 3) {
            if (!Pattern.compile("07[1,2,4,5,6,7,8]").matcher(newtext).matches()) {
                return true;

            } else {
                return false;

            }

        } else if (newtext.length() <= 10) {
            if (!Pattern.compile("07[1,2,4,5,6,7,8][0-9]+").matcher(newtext).matches()) {
                return true;

            } else {
                return false;

            }

        } else {
            return true;

        }
    }
}
