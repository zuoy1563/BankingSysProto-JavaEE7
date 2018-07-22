/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsp.jsfs;

/**
 *
 * @author zuoy1
 */
public class Validation {
    public static boolean isNumber(String str){
        boolean isNumber = true;
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                isNumber = false;
                break;
            }
        }
        return isNumber;
    }
    
    public static boolean isBlank(String str) {
        return str.trim().length() == 0;
    }
}
