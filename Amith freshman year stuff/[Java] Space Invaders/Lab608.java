import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Lab608
{
   public static void main(String[] args)
   { 
      JFrame frame = new JFrame("Space Invaders");
      frame.setSize(600, 680);
      frame.setLocation(20, 20);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(new GUIPanel());
      frame.setVisible(true);
   }
}