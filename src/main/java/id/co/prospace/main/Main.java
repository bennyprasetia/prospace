/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.prospace.main;

import id.co.prospace.util.Process;

/**
 *
 * @author Benny Prasetia
 */
public class Main {

    public static void main(String[] args) {

        String filePath = null;
        if (args.length != 0) {
            filePath = args[0];
        }
        try {
            Process.proccessInputData(filePath);
            Process.MapTokentoIntegerValue();
            Process.processOutputData();
        } catch (Exception e) {
            System.out.println("Oops !! File Not Found ");
        }
    }

}
