import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class LoadingPanel extends JPanel
{
   public static final int FRAME_width = 400;
   public static final int FRAME_hight = 600;
   private static final Color BACKGROUND = new Color(204, 204, 204);
   private BufferedImage myImage;
   private Graphics myBuffer;
   int selection;
   Timer t = new Timer(10, new AnimationListener());
   Triangle[] players = new Triangle[3];
   int[] speeds = new int[]{1, 2, 3};
   
   public LoadingPanel(Triangle blue, Triangle magenta, Triangle gold)
   {
      myImage =  new BufferedImage(FRAME_width, FRAME_hight, BufferedImage.TYPE_INT_RGB);
      myBuffer = myImage.getGraphics();
      myBuffer.setColor(BACKGROUND);
      myBuffer.fillRect(0,0,FRAME_width,FRAME_hight);
      
      players[0] = blue;
      players[1] = magenta;
      players[2] = gold; 
      
      players[0].setCircleBoundaries(50, 150, 150, 250);
      players[1].setCircleBoundaries(150, 250, 150, 250);
      players[2].setCircleBoundaries(250, 350, 150, 250);
      
      selection = 1;
      
      t.start();      
      
      addKeyListener(new Key());
      setFocusable(true);
   }
   
   public void paintComponent(Graphics g)
   {
      g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
   }
   
   private class AnimationListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e) 
      {
         myBuffer.setColor(BACKGROUND);
         myBuffer.fillRect(0,0,FRAME_width,FRAME_hight);
         
         myBuffer.setColor(Color.GREEN);
         myBuffer.fillRect(((selection+1)*100)- 50, 150, 100, 100);
         
         for(int i = 0; i < 3; i++)
         {
            players[i].circle(speeds[i]);
            players[i].draw(myBuffer);
         }
         repaint();
      }
   }
   
   public int getSelection()
   {
      return selection;
   }

   private class Key extends KeyAdapter
   {
      public void keyPressed(KeyEvent e)
      {
         if(e.getKeyCode() == KeyEvent.VK_RIGHT && selection < 2)
         {
            selection++;
         }
         if(e.getKeyCode() == KeyEvent.VK_LEFT && selection > 0)
         {
            selection--;
         }
      }
   }
}