package com.company;

import javax.swing.*;
import java.io.*;

public class Main {

        public static void main(String[] args) {
         newFrame a = new newFrame();
            {
                try {
                    DataOutputStream out = new DataOutputStream(new FileOutputStream("coordinates.bin"));
                    //double x = -10;
                    double x = -5 * Math.PI;
                   // double y =  1 * Math.pow(x, 2)  ;
                    double y = 25 * Math.sin(x);
                    for (int i = 0; i <= 150; i++) {
                        out.writeDouble(x);

                        out.writeDouble(y);

                        x += 0.25;
                      //  x+= 1.0;
                     //   y = 1 * Math.pow(x, 2) ;
                        y = 25 * Math.sin(x);
                    }
                    out.close();
                } catch (FileNotFoundException e) {

                    System.out.println("aaaaaa, ne rabotaet");
                } catch (IOException e) {
                }
            }

            a.setVisible(true);
    a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

 }



}
