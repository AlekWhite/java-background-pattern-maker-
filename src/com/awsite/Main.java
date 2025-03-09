package com.awsite;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        int w = 900;
        int h = 900;
        int s = 2;

        double r = ((double)w*h)/200;
        System.out.println(r);

        Page p = new Page(w, h, s, (int)r);

        // ui setup
        JFrame frame = new JFrame();
        frame.setSize(w*s, h*s);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setTitle("PATGEN");
        frame.getContentPane().setLayout(new BorderLayout());

        // put image on the ui
        frame.getContentPane().add(p);
        frame.setVisible(true);



    }
}
