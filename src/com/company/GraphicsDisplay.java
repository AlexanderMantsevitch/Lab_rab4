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
       axisFont = new Font( Font.MONOSPACED, Font.ITALIC, 38);
   }

    public void ShowGraphicsData (Double[][] graphicsData) {
        GraphicsData = graphicsData;
        repaint();
    }
    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }
    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }
   public void paintComponent (Graphics graph) {
       super.paintComponent(graph);
       if (GraphicsData == null || GraphicsData.length == 0) return;
       XMin = GraphicsData[0][0];
       XMax = GraphicsData[GraphicsData.length - 1][0];
       YMax = GraphicsData[GraphicsData.length - 1][1];
       YMin = GraphicsData[0][1];
       for (int i = 0; i < GraphicsData.length; i++) {
           if (GraphicsData[i][0] < XMin) XMin = GraphicsData[i][0];
           if (GraphicsData[i][0] > XMax) XMax = GraphicsData[i][0];
           if (GraphicsData[i][1] < YMin) YMin = GraphicsData[i][1];
           if (GraphicsData[i][1] > YMax) YMax = GraphicsData[i][1];

       }

       double sc1 = getSize().getWidth() / (XMax - XMin);
       double sc2 = getSize().getHeight() / (YMax - YMin);
       scale = Math.min(sc1, sc2);

       if (scale == sc1)
       {
           double icrement = (getSize().getHeight() / scale - (XMax - XMin))/2;
        YMax+=icrement;
        YMin-=icrement;
       }
       if (scale == sc2)
       {
           double icrement = (getSize().getWidth() / scale - (YMax - YMin))/2;
           XMax+=icrement;
           XMin-=icrement;
       }





   }
}
