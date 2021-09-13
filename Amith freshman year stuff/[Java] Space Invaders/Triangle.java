import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Triangle 
{
   int[] x = new int[3];
   int[] y = new int[3];
   int midX, midY;
   int dx;
   int dy;
   Color color;
   int hight;
   int width;
   int direction; //for the different animatios, the direction is used to know which way to go
   
   int leftX, rightX, topY, bottomY;
   
   public Triangle(Color c, int h, int w, int xStart, int yStart)
   {
      color = c;
      hight = h;
      width = w;
      
      dy = 0;
      dx = 0;
      
      direction = 1; 
      
      midX = xStart;
      midY = yStart - hight/2; 
      
      x[0] = xStart - (width/2);
      y[0] = yStart;
      
      x[1] = xStart;
      y[1] = yStart - hight;
      
      x[2] = xStart + (width/2);
      y[2] = yStart;
   }
   
   public void setCircleBoundaries(int lX, int rX, int tY, int bY)
   {
      leftX = lX;
      rightX = rX;
      topY = tY;
      bottomY = bY;
   }
   
   public void circle(int speed)
   {
      if(direction == 0)
      {
         if(midX >= rightX - (width/2))
         {
            direction = 1;
         }
         else
         {
            dx = speed;
            dy = speed;
         }
      }
      if(direction == 1)
      {
         if(midY >= bottomY - (hight/2))
         {
            direction = 2;
         }
         else
         {
            dx = -speed;
            dy = speed;
         }
      }
      if(direction == 2)
      {
         if(midX <= leftX + (width/2) - 1)
         {
            direction = 3;
         }
         else
         {
            dx = -speed;
            dy = -speed;
         }
      }
      if(direction == 3)
      {
         if(midY <= topY + (hight/2))
         {
            direction = 0;
         }
         else
         {
            dx = speed;
            dy = -speed;
         }
      }   
   }
   
   
   
   public void setDx(int newDx)
   {
      dx += newDx;
   }
   
   public void setDy(int newDy)
   {
      dy += newDy;
   }
   
   public void stopMoving()
   {
      dy = dx = 0; 
   }
   
   public void restrictMovement()
   {
      if(dx > 0 && x[2] >= 400)
      {
         dx = 0;
      }
      if(dx < 0 && x[0] <= 0)
      {
         dx = 0;
      }
      if(dy > 0 && y[0] >= 600)
      {
         dy = 0;
      }
      if(dy < 0 && y[1] <= 0)
      {
         dy = 0;
      }
   }
   
   public int[][] getLocation()
   {
      return new int[][]{x, y};
   }
   
   public boolean hit(int rX, int rY)
   {  
      if((rY < y[0] && rY + 8 > y[1]) && (rX + 4 > x[0] && rX < x[2]))
      {
         return true;
      }
      else
      {
         return false;
      }
      /*
      double s1 = (y[0] - y[1]) / (x[0] - x[1]);
      double s2 = (y[1] - y[2]) / (x[1] - x[2]);
      double b1 = y[1] - (s1*x[1]);
      double b2 = y[1] - (s2*x[1]); // the equation is y = mx + b; s1&2 = m and b1&2 = b
      for(int x = rX; x > rX + 4;  x++)
      {
         for(int y = rY; y > rY + 8; y++)
         {  
            if(y <= s1*x + b1 && y <= s2*x + b2 )//&& y <= this.y[0])
            {
               System.out.println("TRIGGERD");
               return true;
            }
         }
      }
      return false;*/
   }
   
   public void draw(Graphics g)
   {
      for(int i = 0; i < 3; i++)
      {
         x[i] += dx;
         y[i] += dy;
      }
      midX += dx;
      midY += dy;
      g.setColor(color);
      g.fillPolygon(x, y, 3);
      
   }
   
   public void blink(Graphics g)
   {
      for(int i = 0; i < 3; i++)
      {
         x[i] += dx;
         y[i] += dy;
      }
      midX += dx;
      midY += dy;
      g.setColor(color);
      g.drawPolygon(x, y, 3);
      
   }
}