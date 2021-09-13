import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Enemy
{
   int myX; 
   int myY; 
   final int width = 30;
   final int hight = 16;
   // for the direction, 0 = left, and 1 = right
   int direction;
   int leftStop;
   int rightStop;
   
   public Enemy(int startX, int startY, int setLS, int setRS, int setD)
   {
      myX = startX;
      myY = startY;
      direction = setD;
      leftStop = setLS;
      rightStop = setRS;
   }
   
   public int[] getXY()
   {
      return new int[]{myX+(width/2), myY + (hight/2)};
   }
   
   public void move()
   {
      if(direction == 0)
      {
         if(myX <=  leftStop)
         {
            direction  = 1;
         }
         else
         {
            myX -= 1;
         }
      }
      else if (direction == 1)
      {
         if(myX >=  rightStop - width)
         {
            direction  = 0;
         }
         else
         {
            myX += 1;
         }

      }
   }
   
   public boolean hit(int rX, int rY)
   {
      if((rY < myY + hight && rY > myY - 8) && (rX > myX - 4 && rX < myX + width))
      {
         return true;
      }
      else
      {
         return false;
      }
   }
   
   public void draw(Graphics panel, int stage)
   {
      int sideCut = stage*4;
      if(sideCut > 15) sideCut = 14;
      int topCut = stage*2;
      if(topCut > 7) topCut = 7;
      panel.setColor(new Color(145, 143, 110));
      panel.fillRect(myX + sideCut, myY + topCut, width - (sideCut*2), hight - (topCut*2));
   }
}