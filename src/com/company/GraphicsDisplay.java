package com.company;

import javax.swing.*;
import java.awt.*;


public class GraphicsDisplay extends JPanel {

    private  Double [][] GraphicsData = null;
    private Double XMin;
    private Double XMax;
    private Double YMax;
    private Double YMin;
    private Double scale;
    private  Boolean showAxis;
    private Boolean showMarkers;
    private Font axisFont;
    private BasicStroke axis; // линии осей
    private BasicStroke LineGraph; // линии графика
    private BasicStroke MarkersLine; // линии для прорисовки маркеров

   public GraphicsDisplay ()
   {
       setBackground(Color.WHITE);
        LineGraph = new BasicStroke( 4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                4f, new float[] {4, 1,1,1,1,1,2,1,2}, 0.0f );
        axis = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                2f, null , 0.0f);
       MarkersLine = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
               2f, null , 0.0f);

   }





}
