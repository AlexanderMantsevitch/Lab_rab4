package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class newFrame extends JFrame {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private JFileChooser FileChooser = null;


    public newFrame (){
    super ("График");

        Toolkit a = Toolkit.getDefaultToolkit();
        setLocation(a.getScreenSize().width - WIDTH, a.getScreenSize().height - HEIGHT);

        JMenuBar fileMenu = new JMenuBar();
        setJMenuBar(fileMenu);
        JMenu fileName = new JMenu("Файл");
        fileMenu.add(fileName);
        Action openFile = new AbstractAction("Открыть файл") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (FileChooser == null )
                {
                    FileChooser = new JFileChooser();
                    FileChooser.setCurrentDirectory(new File("."));

                }
                if (FileChooser.showOpenDialog(newFrame.this) ==
                        JFileChooser.APPROVE_OPTION) ;



            }
        };
        fileName.add (openFile);
    }


}
