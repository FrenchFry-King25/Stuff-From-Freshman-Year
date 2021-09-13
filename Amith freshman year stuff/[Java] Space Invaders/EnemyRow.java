import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class EnemyRow
{
   Enemy[] row = new Enemy[7];
   int[] alive = new int[7];//0 = alive; 1 to 4 = stages of death; 5 = dead
   int[] animationCounter = new int[7];
   int Y;
   int direction;
   
   public EnemyRow(int y, int d)
   {
      direction = d;
      for(int i = 0; i < 7; i++)
      {
         int offsetL = 2 + (40*i);
         int offsetR = 368 - (40*(6-i));
         if(direction == 1)
         {
            row[i] = new Enemy(offsetL, y, offsetL, offsetR, d);
         }
         else if(direction == 0)
         {
            row[i] = new Enemy(offsetR, y, offsetL, offsetR, d);
         }
         alive[i] =  animationCounter[i] = 0;
      }
   }
   
   public boolean hit(int Rx, int Ry)
   {
      boolean hit = false;
      for(int i = 0; i < 7; i++)
      {
         if(alive[i]  == 0 && row[i].hit(Rx, Ry))
         {
            animationCounter[i] = 16;  
            hit = true;
         }
      }
      return hit;
   }
   
   public boolean done()
   {
      for(int i = 0; i < 7; i++)
      {
         if(alive[i] == 0)
         {
            return false;  
         }
      }
      return true;
   }
   
   public int[] getRandomXY()
   {
      boolean keepgoing = true;
      int i = 0;
      while(keepgoing)
      {
         double p = Math.random() * 7;
         i = (int) p;
         if(alive[i] == 0)
         {
            keepgoing= false;
         }
      }
      return row[i].getXY();
   }  
   
   public void draw(Graphics panel)
   {
      for(int i = 0; i < 7; i++)
      {
         if(alive[i] == 0 && animationCounter[i] == 0)
         {
            row[i].move();
            row[i].draw(panel, alive[i]);
         }
         else if (alive[i] < 5)
         {
            if(animationCounter[i] % 4 == 0) alive[i] += 1;
            row[i].move();
            row[i].draw(panel, alive[i]);
            animationCounter[i]--;
         }
      }
   
   }
}