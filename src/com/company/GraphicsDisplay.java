package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;


public class GraphicsDisplay extends JPanel implements MouseMotionListener, MouseListener
{

    private  Double [][] GraphicsData = null;
    private  Double [][] GraphicsDataold;
    private LinkedList<Double[][]> DataSave = new LinkedList<>();
    private Double XMin = 0.0;
    private Double XMax= 0.0;
    private Double YMax= 0.0;
    private Double YMin= 0.0;
    private Double scale = 0.0;
    private  Boolean showAxis = false;
    private Boolean showMarkers = false;
    private  Boolean showSort = false;
    private  Boolean showNet = false;
    private Font axisFont;
    private BasicStroke axis; // линии осей
    private BasicStroke LineGraph; // линии графика
    private BasicStroke MarkersLine; // линии для прорисовки маркеров
    private BasicStroke NetLine;
    private int mouseX = 0; private int mouseY = 0;
    private  int pmouseX = 0; private int pmouseY = 0;
    private String msg = " ";
    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
    private  Rectangle mouseRectangle = new Rectangle(0,0,0,0);
    private int mouseButton;
   public GraphicsDisplay ()
   {
       setBackground(Color.WHITE);
        LineGraph = new BasicStroke( 5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                4f, new float[] {16, 4,4,4,4,4,8,4,8}, 0.0f );
        axis = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                2f, null , 0.0f);
       MarkersLine = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
               1f, null , 0.0f);
       axisFont = new Font( Font.MONOSPACED, Font.ITALIC, 25);
       NetLine = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
               1f, new float[] {2,2} , 0.0f);









       addMouseMotionListener(this);
       addMouseListener(this);
       formatter.setMinimumFractionDigits(5);
       formatter.setGroupingUsed(false);

       DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
       dottedDouble.setDecimalSeparator('.');
       formatter.setDecimalFormatSymbols(dottedDouble);
   }

    public void ShowGraphicsData (Double[][] graphicsData) {
        GraphicsData = graphicsData;
        DataSave.add(graphicsData);
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
    public void setShowSort(boolean showSort) {
        this.showSort = showSort;
        repaint();
    }
    public void setShowNet(boolean showNet) {
        this.showNet = showNet;
        repaint();
    }
    public void returnFile ()
    {
        GraphicsData = DataSave.getFirst();

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
       // System.out.println(getSize());

            Graphics2D canvas = (Graphics2D) g;
            Stroke oldStroke = canvas.getStroke();
            Color oldColor = canvas.getColor();
            Paint oldPaint = canvas.getPaint();
            Font oldFont = canvas.getFont();
            GraphicsPaint(canvas);
            axisPaint(canvas);
            PaintMarkers(canvas);
            SortValues(canvas);
            SetNetLine(canvas);

        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
        canvas.setColor(Color.RED);
        if (mouseY > 11 && mouseX < getWidth() - 30)
        canvas.drawString(msg, mouseX + 5, mouseY);
        else if (mouseY <= 11 && mouseX <= getWidth() - 50)
        canvas.drawString(msg, mouseX+ 10, mouseY+20);
        else if(mouseY >= 11 && mouseX >= getWidth() - 50)
            canvas.drawString(msg, mouseX- msg.length()*6, mouseY);
        else if (mouseY <= 11 && mouseX >= getWidth() - 50)
            canvas.drawString(msg, mouseX- msg.length()*6, mouseY+7);
        canvas.setColor(Color.DARK_GRAY);
        canvas.setStroke(LineGraph);

        canvas.draw(mouseRectangle);

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
    protected Point2D.Double xyToPoint_obr (Double x, Double y)
    {
        Double deltaX = x/scale - XMin;
        Double deltaY = YMax - y/scale;
        return new Point2D.Double(deltaX, deltaY);


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
       Double icrement = 0.1* (YMax - YMin)/GraphicsData.length ;
      GeneralPath graphics = new GeneralPath();
      Point2D.Double lines = xyToPoint(0.0, YMin);
      graphics.moveTo(lines.getX(), lines.getY());
      lines = xyToPoint(0.0, YMax);
      graphics.lineTo(lines.getX(), lines.getY());
       graphics.closePath();
      lines = xyToPoint(XMin, 0.0);
       graphics.moveTo(lines.getX(), lines.getY());
       lines = xyToPoint(XMax, 0.0);
       graphics.lineTo(lines.getX(), lines.getY());

       canvas.draw(graphics);
       GeneralPath arrow = new GeneralPath();
       Point2D.Double lineEnd = xyToPoint(XMax - 3* icrement, -2.0* icrement);
       arrow.moveTo(lineEnd.getX(), lineEnd.getY());
       lineEnd = xyToPoint((XMax) ,  0.0);
       arrow.lineTo(lineEnd.getX(), lineEnd.getY());
       lineEnd = xyToPoint((XMax) - 3 * icrement, 2.0 * icrement);
       arrow.lineTo(lineEnd.getX(), lineEnd.getY());
       lineEnd = xyToPoint(XMax - 3* icrement, 3.0* icrement) ;

       canvas.drawString("x", (float) lineEnd.getX() - 25, (float) lineEnd.getY());
       canvas.draw(arrow);
     canvas.fill (arrow);

       GeneralPath arrowY = new GeneralPath();
       Point2D.Double lineEndY = xyToPoint( -2.0* icrement, YMax - 3* icrement);
       arrowY.moveTo(lineEndY.getX(), lineEndY.getY());
       lineEndY = xyToPoint( 0.0, YMax - 1* icrement);
       arrowY.lineTo(lineEndY.getX(), lineEndY.getY());
       lineEndY = xyToPoint(  2.0* icrement, (YMax) - 3* icrement );

       arrowY.lineTo(lineEndY.getX(), lineEndY.getY());
       lineEndY = xyToPoint (-10.0* icrement , YMax-8.0* icrement);


       canvas.draw(arrowY);

       canvas.drawString("y", (float) lineEndY.getX() +10 , (float) (lineEndY.getY()) +10) ;
       canvas.fill (arrowY);

   }

   }
    protected void PaintMarkers (Graphics2D canvas)
    {
        if (showMarkers) {
            canvas.setColor(Color.RED);
            canvas.setStroke(MarkersLine);
            canvas.setPaint(Color.RED);
            for (Double[] point : GraphicsData) {


                Point2D.Double center = xyToPoint(point[0], point[1]);
                GeneralPath marker = new GeneralPath();
                marker.moveTo(center.getX() - 5, center.getY() - 5);
                marker.lineTo(center.getX() + 5, center.getY() + 5);
                marker.moveTo(center.getX() - 5, center.getY() + 5);
                marker.lineTo(center.getX() + 5, center.getY() - 5);
                marker.moveTo(center.getX(), center.getY() + 5);
                marker.lineTo(center.getX(), center.getY() - 5);
                marker.moveTo(center.getX() + 5, center.getY());
                marker.lineTo(center.getX() - 5, center.getY());


                canvas.draw(marker); // Начертить контур маркера
                canvas.fill(marker); // Залить внутреннюю область маркера
            }
        }


    }

    protected void SortValues (Graphics2D canvans)
    {
        if (showSort) {
            for (Double[] point : GraphicsData) {
                Double pred = 10.0;
                Boolean condition = true;
                Double savePoint = point[1];
                while ((Math.abs(savePoint)) >= 1) {


                    if ((Math.abs(savePoint) % 10) > pred) {
                        condition = false;
                        break;
                    }
                    pred = Math.abs(savePoint) % 10;
                    savePoint /= 10;
                }

                if (condition) {
                    canvans.setColor(Color.YELLOW);
                    canvans.setStroke(MarkersLine);
                    canvans.setPaint(Color.YELLOW);

                    Point2D.Double center = xyToPoint(point[0], point[1]);
                    GeneralPath marker = new GeneralPath();
                    marker.moveTo(center.getX() - 5, center.getY() - 5);
                    marker.lineTo(center.getX() + 5, center.getY() + 5);
                    marker.moveTo(center.getX() - 5, center.getY() + 5);
                    marker.lineTo(center.getX() + 5, center.getY() - 5);
                    marker.moveTo(center.getX(), center.getY() + 5);
                    marker.lineTo(center.getX(), center.getY() - 5);
                    marker.moveTo(center.getX() + 5, center.getY());
                    marker.lineTo(center.getX() - 5, center.getY());


                    canvans.draw(marker); // Начертить контур маркера


                }

            }
        }
    }

    protected void SetNetLine (Graphics2D canvas)
    {
        if (showNet) {
            canvas.setColor(Color.BLACK);
            canvas.setStroke(NetLine);
            GeneralPath lines = new GeneralPath();
            for (Double i = YMin; i <= YMax; i += (YMax - YMin) / 10) {
                Point2D.Double coord = xyToPoint(XMax, i);
                lines.moveTo(coord.getX(), coord.getY());
                coord = xyToPoint(XMin, i);
                lines.lineTo(coord.getX(), coord.getY());
            }
            for (Double i = XMin; i <= XMax; i += (XMax - XMin) / 10) {
                Point2D.Double coord = xyToPoint(i, YMax);
                lines.moveTo(coord.getX(), coord.getY());
                coord = xyToPoint(i, YMin);
                lines.lineTo(coord.getX(), coord.getY());
            }
            canvas.draw(lines);
        }
    }



    @Override
    public void mouseMoved(MouseEvent e) {

        if (GraphicsData != null) {
            Boolean schet = false;
            for (int i = 0; i < GraphicsData.length; i++) {
                Point2D.Double point = xyToPoint(GraphicsData[i][0], GraphicsData[i][1]);
                if (Math.abs(e.getX() - (int)point.getX()) < 12 && Math.abs (e.getY() - (int)point.getY()) < 12) {
                    String formattedDoubleX = formatter.format(GraphicsData[i][0]);
                    String formattedDoubleY = formatter.format(GraphicsData[i][1]);
                    msg = "X: " + formattedDoubleX + "  " + "Y: " + formattedDoubleY;
                    mouseX = e.getX();
                    mouseY = e.getY();
                    schet = true;
                    repaint();


                }
                if (!schet) {
                    msg = " ";
                    repaint();
                }



            }

        }
   }
    @Override
    public void mouseDragged(MouseEvent e) {
       int heigthRect = 0; int widthRect = 0; int x = 0; int y = 0;
        if (e.getX()- pmouseX < 0) {widthRect = Math.abs (e.getX() - pmouseX);  x = e.getX(); }
        if (e.getX()- pmouseX > 0) {widthRect = Math.abs (e.getX() - pmouseX);  x = pmouseX; }
        if (e.getY()- pmouseY < 0) {heigthRect = Math.abs (e.getY() - pmouseY);  y = e.getY(); }
        if (e.getY()- pmouseY > 0) {heigthRect = Math.abs (e.getY() - pmouseY);  y = pmouseY; }

   mouseRectangle.setLocation(x, y);
   mouseRectangle.setSize(widthRect, heigthRect);

  // mouseRectangle.setBounds(pmouseX,pmouseY,mouseX - pmouseX,mouseY - pmouseY);
        System.out.println(mouseRectangle.getSize());
    repaint();

    }
    @Override
    public void mouseClicked(MouseEvent e) {
   mouseButton = e.getButton();
   if (mouseButton == 3 && DataSave.size ()!= 1)
   {
       System.out.println(DataSave.size());
       DataSave.removeLast ();
       System.out.println(DataSave.size());
       GraphicsData = DataSave.getLast();


   }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
      mouseButton = e.getButton();
      if (mouseButton == 1) {
          pmouseX = e.getX();
          pmouseY = e.getY();
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
       mouseButton = e.getButton();
       if (mouseButton == 1) {
           Double[][] a = new Double[GraphicsData.length][];
           int schet = 0;
           Point2D.Double bord1 = xyToPoint_obr(0.0, mouseRectangle.getY() +
                   mouseRectangle.getHeight());
           Point2D.Double bord2 = xyToPoint_obr(0.0, mouseRectangle.getY() + 0.0);
           for (int j = 0; j < GraphicsData.length; j++) {
               a[j] = new Double[2];
               Point2D.Double datas = xyToPoint(GraphicsData[j][0], GraphicsData[j][1]);
               if (datas.getX() > mouseRectangle.getX() && mouseRectangle.getX() +
                       mouseRectangle.getWidth() > datas.getX() &&
                       datas.getY() > mouseRectangle.getY() && mouseRectangle.getY() +
                       mouseRectangle.getHeight() > datas.getY()) {

                   a[j][0] = GraphicsData[j][0];
                   a[j][1] = GraphicsData[j][1];
                   schet++;
                   //System.out.println(a[j][0] + " " + a[j][1]);

               } else if (datas.getX() > mouseRectangle.getX() && mouseRectangle.getX() +
                       mouseRectangle.getWidth() > datas.getX()
                       && datas.getY() < mouseRectangle.getY()) {
                   a[j][0] = GraphicsData[j][0];
                   a[j][1] = bord2.getY();
                   // System.out.println(a[j][0] + " " + a[j][1]);
                   schet++;
               } else if (datas.getX() > mouseRectangle.getX() && mouseRectangle.getX() +
                       mouseRectangle.getWidth() > datas.getX()
                       && mouseRectangle.getY() +
                       mouseRectangle.getHeight() < datas.getY()) {

                   a[j][0] = GraphicsData[j][0];
                   a[j][1] = bord1.getY();
                   // System.out.println(a[j][0] + " " + a[j][1]);
                   schet++;

               }

           }
           Double[][] NewData = new Double[schet][];
           schet = 0;
           for (int i = 0; i < a.length; i++) {

               if (a[i][0] != null) {
                   NewData[schet] = new Double[2];
                   NewData[schet][0] = a[i][0];
                   NewData[schet][1] = a[i][1];
                   schet++;
               }

           }

           mouseRectangle.setLocation(0, 0);
           mouseRectangle.setSize(0, 0);
           if (NewData != null) {

               GraphicsData = NewData;
               DataSave.addLast(NewData);
               System.out.println("k");
           }
           repaint();
       }
    }
    public void mouseEntered (MouseEvent e)
    {



    }
    public  void mouseExited (MouseEvent e)
    {


    }
}
