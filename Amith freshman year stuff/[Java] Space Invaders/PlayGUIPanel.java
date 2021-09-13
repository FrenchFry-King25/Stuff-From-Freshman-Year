import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class PlayGUIPanel extends JPanel
{
   TrianglePanel pic;
   final int[][] triangles = new int[][]{{18, 18, 1, 5}, 
                                         {34, 34, 2, 4}, 
                                         {26, 26, 3, 3}}; //setSpeed[blue, magenta, gold][hight, width, speed, rocket speed]
   Color[] colors = new Color[]{new Color(30, 85, 160), 
                                new Color(200, 100, 200), 
                                new Color(160, 120, 35)};
   Triangle blue = new Triangle(colors[0], triangles[0][0], triangles[0][1], 100, 250); // speed 1, rocket speed 5
   Triangle magenta = new Triangle(colors[1], triangles[1][0], triangles[1][1], 200, 250); // speed 2, rocket speed 4
   Triangle gold = new Triangle(colors[2], triangles[2][0], triangles[2][1], 300, 250); // speed 3, rocket speed 3
   
   public PlayGUIPanel(int choice)
   {
   //screen
      pic = new TrianglePanel(colors[choice], triangles[choice][0], 
                                              triangles[choice][1], 
                                              triangles[choice][2], 
                                              triangles[choice][3]);
      add(pic);
      pic.requestFocusInWindow();
      // reset button
      JButton button = new JButton("RESART GAME");
      button.addActionListener(new ResetListener());
      //button.setBackground(new Color(Integer.parseInt(r.getText()), Integer.parseInt(g.getText()), Integer.parseInt(b.getText())));
      add(button, BorderLayout.SOUTH);
      //title
      JLabel title = new JLabel("Space Invaders!");
      title.setFont(new Font("Serif", Font.BOLD, 20));
      title.setHorizontalAlignment(SwingConstants.CENTER);
      add(title, BorderLayout.NORTH);
   }
   
   private class ResetListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      { 
         pic.requestFocusInWindow();
      }
   }

}
