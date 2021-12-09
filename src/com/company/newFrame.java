package com.company;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.util.Scanner;

public class newFrame extends JFrame {

    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private JFileChooser FileChooser = null;
    private GraphicsDisplay display = new GraphicsDisplay();
    private JCheckBoxMenuItem AxisCheck;
    private JCheckBoxMenuItem MarkersCheck;
    private JCheckBoxMenuItem SortCheck;
    private JCheckBoxMenuItem NetCheck;
    public newFrame (){
    super ("График");
        setSize(WIDTH,HEIGHT);
        Toolkit a = Toolkit.getDefaultToolkit();
        setLocation((a.getScreenSize().width - WIDTH)/2, (a.getScreenSize().height - HEIGHT)/2);

        JMenuBar fileMenu = new JMenuBar();
        setJMenuBar(fileMenu);
        JMenu fileName = new JMenu("Файл");
        JMenu gr = new JMenu( "График");
        gr.setEnabled(false);
        fileMenu.add(fileName);
        fileMenu.add (gr);
        Action returnFile = new AbstractAction("Вернуть первоначальный вид") {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.returnFile();
            }
        };

        returnFile.setEnabled(false);
        Action openFile = new AbstractAction("Открыть файл") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (FileChooser == null )
                {
                    FileChooser = new JFileChooser();
                    FileChooser.setCurrentDirectory(new File("."));
                    returnFile.setEnabled(true);
                    gr.setEnabled(true);
                }
                if (FileChooser.showOpenDialog(newFrame.this) ==
                        JFileChooser.APPROVE_OPTION)  openGraphicsFile(FileChooser.getSelectedFile()); ;



            }
        };
        Action axis = new AbstractAction("Показать оси") {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setShowAxis(AxisCheck.isSelected());

            }
        };
        AxisCheck = new JCheckBoxMenuItem(axis);
        AxisCheck.setSelected(false);
        gr.add (AxisCheck);
        fileName.add (openFile);
        fileName.add (returnFile);
        GraphicsDisplay graph = new GraphicsDisplay();
        getContentPane().add(display, BorderLayout.CENTER);

       Action markers = new AbstractAction("Показать маркеры") {
           @Override
           public void actionPerformed(ActionEvent e) {
               display.setShowMarkers(MarkersCheck.isSelected());
           }

       };
        MarkersCheck = new JCheckBoxMenuItem(markers);
        MarkersCheck.setSelected(false);
        gr.add (MarkersCheck);

        Action sort = new AbstractAction("Включить сортировку") {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setShowSort(SortCheck.isSelected());
            }

        };
        SortCheck = new JCheckBoxMenuItem(sort);
        SortCheck.setSelected(false);
        gr.add (SortCheck);
        Action Net = new AbstractAction("Включить сетку") {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setShowNet(NetCheck.isSelected());
            }
        };
        NetCheck = new JCheckBoxMenuItem(Net);
        NetCheck.setSelected(false);
        gr.add (NetCheck);

    }
protected void openGraphicsFile (File sel_file)
{
    try{
    DataInputStream in = new DataInputStream(new FileInputStream(sel_file));

    Double[][] data = new  Double[in.available()/(Double.SIZE/8)/2][];

    int i = 0;
    while (in.available()>0) {

        Double x = in.readDouble();
        Double y = in.readDouble();

       data[i++] = new Double[] {x, y};
    }

    if (data!=null && data.length>0) {
      /*  for (int j = 0; j < data.length; j++){
        System.out.println(data[j][0]);
        System.out.println(data[j][1]);
         }

       */

        display.ShowGraphicsData(data);

    }

    in.close();
    }
    catch (IOException e)
    {

        JOptionPane.showMessageDialog(newFrame.this, "Ошибка чтения  " +
                        "координат точек из файла", "Ошибка загрузки данных",
                JOptionPane.WARNING_MESSAGE);
        return;

    }


}

}
