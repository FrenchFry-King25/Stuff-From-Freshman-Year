import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Rocket
{
   private int myX;
   private int myY;
   private final Color color = new Color(255, 0, 0);
   int speed;
      
   public Rocket(int x, int y, int s)
   {
      myX = x;
      myY = y;
      speed = s;
   }
   
   public void moveUp(int speed)
   {
      myY -= speed; 
   }
   
   public void moveDown()
   {
      myY += 3; 
   }
   
   public boolean ifOut()
   {
      if(myY <= 0 || myY >= 600)
      {
         return true;
      }
      else
      {
         return false;
      }
   }
   
   public int[] getXY()
   {
      return new int[]{myX, myY};
   }
      
   public void draw(Graphics myBuffer) 
   {
      myBuffer.setColor(color);
      myBuffer.fillRect(myX - 2, myY - 8, 4, 8);
   }
}