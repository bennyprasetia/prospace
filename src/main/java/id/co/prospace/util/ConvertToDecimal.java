/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.prospace.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import static javax.swing.UIManager.put;

/**
 *
 * @author Benny Prasetia
 */
public class ConvertToDecimal extends ConvertRules {

    public float numberToDecimal(java.lang.String number) {

        float decimal = 0;
        float lastNumber = 0;
        char[] romanNumeral = number.toUpperCase().toCharArray();

        for (int x = romanNumeral.length - 1; x >= 0; x--) {
            Character convertToDecimal = romanNumeral[x];

            switch (convertToDecimal) {
                case 'I':
                    super.checkLiteralCountValidity(convertToDecimal);
                    decimal = processToDecimal(1, lastNumber, decimal);
                    lastNumber = 1;
                    break;

                case 'V':
                    super.checkLiteralCountValidity(convertToDecimal);
                    decimal = processToDecimal(5, lastNumber, decimal);
                    lastNumber = 5;
                    break;

                case 'X':
                    super.checkLiteralCountValidity(convertToDecimal);
                    decimal = processToDecimal(10, lastNumber, decimal);
                    lastNumber = 10;
                    break;

                case 'L':
                    super.checkLiteralCountValidity(convertToDecimal);
                    decimal = processToDecimal(50, lastNumber, decimal);
                    lastNumber = 50;
                    break;

                case 'C':
                    super.checkLiteralCountValidity(convertToDecimal);
                    decimal = processToDecimal(100, lastNumber, decimal);
                    lastNumber = 100;
                    break;

                case 'D':
                    super.checkLiteralCountValidity(convertToDecimal);
                    decimal = processToDecimal(500, lastNumber, decimal);
                    lastNumber = 500;
                    break;

                case 'M':
                    super.checkLiteralCountValidity(convertToDecimal);
                    decimal = processToDecimal(1000, lastNumber, decimal);
                    lastNumber = 1000;
                    break;
            }
        }
        super.resetLiteralsCounter();
        return decimal;
    }

    public float processToDecimal(float decimal, float lastNumber, float lastDecimal) {
        if (lastNumber > decimal) {
            return super.subtractionLogic(lastDecimal, decimal, lastNumber);
        } else {
            return lastDecimal + decimal;
        }
    }
}
