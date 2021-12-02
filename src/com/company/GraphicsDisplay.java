package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;


public class GraphicsDisplay extends JPanel {

    private  Double [][] GraphicsData = null;
    private Double XMin;
    private Double XMax;
    private Double YMax;
    private Double YMin;
    private Double scale;
    private  Boolean showAxis = false;
    private Boolean showMarkers = false;
    private Font axisFont;
    private BasicStroke axis; // линии осей
    private BasicStroke LineGraph; // линии графика
    private BasicStroke MarkersLine; // линии для прорисовки маркеров

   public GraphicsDisplay ()
   {
       setBackground(Color.WHITE);
        LineGraph = new BasicStroke( 5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                4f, new float[] {16, 4,4,4,4,4,8,4,8}, 0.0f );
        axis = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                2f, null , 0.0f);
       MarkersLine = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
               2f, null , 0.0f);
       axisFont = new Font( Font.MONOSPACED, Font.ITALIC, 25);
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

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

            YMin -= 3;
            YMax += 3.0;
            XMin -=3;
            XMax +=3;
            double sc1 = getSize().getWidth() / (XMax - XMin);
            double sc2 = getSize().getHeight() / (YMax - YMin);
            scale = Math.min(sc1, sc2);

            if (scale == sc2)
            {
                double xIncrement = (getSize().getWidth()/scale - (XMax - XMin))/2;
                XMax += xIncrement;
                XMin -= xIncrement;

            }
            if (scale == sc1)
            {
                double yIncrement = (getSize().getHeight()/scale - (YMax - YMin))/2;

                YMax+=yIncrement;
                YMin-=yIncrement;
            }
        System.out.println(getSize());

            Graphics2D canvas = (Graphics2D) g;
            Stroke oldStroke = canvas.getStroke();
            Color oldColor = canvas.getColor();
            Paint oldPaint = canvas.getPaint();
            Font oldFont = canvas.getFont();
            GraphicsPaint(canvas);
            axisPaint(canvas);


        canvas.setFont(oldFont);
            canvas.setPaint(oldPaint);
            canvas.setColor(oldColor);
            canvas.setStroke(oldStroke);




    }

    protected Point2D.Double xyToPoint (Double x, Double y)
   {
        Double deltaX = x - XMin;
        Double deltaY = YMax - y;
        return new Point2D.Double(deltaX*scale, deltaY*scale);


   }

   protected  Point2D.Double shiftPoint (Point2D.Double src, Double x, Double y)
   {
       Point2D.Double a = new Point2D.Double();
       a.setLocation(src.getX() + x, src.getY() + y);
       return a;

   }
   protected  void GraphicsPaint (Graphics2D canvas)
   {
       canvas.setStroke( LineGraph);
       canvas.setColor(Color.BLUE);
       GeneralPath graphics = new GeneralPath();
      // System.out.println(GraphicsData.length);
       for (int i = 0; i < GraphicsData.length; i++)
       {
           Point2D.Double point = xyToPoint(GraphicsData[i][0], GraphicsData[i][1] );
           if (i > 0) {graphics.lineTo(point.getX(), point.getY());}
           else {graphics.moveTo(point.getX(),point.getY());}



       }
   canvas.draw(graphics);

   }
   protected void axisPaint (Graphics2D canvas)
   {
       if (showAxis)
       {
       canvas.setStroke(axis);
       canvas.setColor(Color.BLACK);
       canvas.setFont(axisFont);
       canvas.setColor(Color.BLACK);
      GeneralPath graphics = new GeneralPath();
      Point2D.Double lines = xyToPoint(0.0, YMin-1);
      graphics.moveTo(lines.getX(), lines.getY());
      lines = xyToPoint(0.0, YMax-1);
      graphics.lineTo(lines.getX(), lines.getY());
       graphics.closePath();
      lines = xyToPoint(XMin-1, 0.0);
       graphics.moveTo(lines.getX(), lines.getY());
       lines = xyToPoint(XMax-1, 0.0);
       graphics.lineTo(lines.getX(), lines.getY());

       canvas.draw(graphics);
       GeneralPath arrow = new GeneralPath();
       Point2D.Double lineEnd = xyToPoint(XMax - 3, -2.0);
       arrow.moveTo(lineEnd.getX(), lineEnd.getY());
       lineEnd = xyToPoint((XMax-1) ,  0.0);
       arrow.lineTo(lineEnd.getX(), lineEnd.getY());
       lineEnd = xyToPoint((XMax) - 3 , 2.0 );
       arrow.lineTo(lineEnd.getX(), lineEnd.getY());
       lineEnd = xyToPoint(XMax - 2, 2.0) ;

       canvas.drawString("x", (float) lineEnd.getX() - 10, (float) lineEnd.getY());
       canvas.draw(arrow);
     canvas.fill (arrow);

       GeneralPath arrowY = new GeneralPath();
       Point2D.Double lineEndY = xyToPoint( -2.0, YMax - 3);
       arrowY.moveTo(lineEndY.getX(), lineEndY.getY());
       lineEndY = xyToPoint( 0.0, YMax - 1);
       arrowY.lineTo(lineEndY.getX(), lineEndY.getY());
       lineEndY = xyToPoint(  2.0, (YMax) - 3 );

       arrowY.lineTo(lineEndY.getX(), lineEndY.getY());
       lineEndY = xyToPoint (-7.0 , YMax-5.0);


       canvas.draw(arrowY);
       canvas.drawString("y", (float) lineEndY.getX()  , (float) (lineEndY.getY()) ) ;
       canvas.fill (arrowY);
   }
   }
}
