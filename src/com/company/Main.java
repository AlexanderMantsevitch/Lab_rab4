package com.company;

import javax.swing.*;
import java.io.*;

public class Main {

        public static void main(String[] args) {
         newFrame a = new newFrame();
            {
                try {
                    DataOutputStream out = new DataOutputStream(new FileOutputStream("coordinates.bin"));
                    double x = -10.0;
                    double y =  0.2 * Math.pow(x, 2);
                    for (int i = 0; i <= 20; i++) {
                        out.writeDouble(x);

                        out.writeDouble(y);

                        x += 1;
                        y = 0.2 * Math.pow(x, 2);
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
