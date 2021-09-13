import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class GUIPanel extends JPanel
{
   JTextField r;
   JTextField g;
   JTextField b;
   JTextField h;
   JTextField w;
   JButton button;
   JPanel inputs;
   
   TrianglePanel pic;
   
   boolean playtime = false;
   
   public GUIPanel()
   {
      setLayout(new BorderLayout());
      
      pic = new TrianglePanel();
      add(pic);
      
      JLabel title = new JLabel("Space Invaders!");
      title.setFont(new Font("Impact", Font.ITALIC, 30));
      title.setHorizontalAlignment(SwingConstants.CENTER);
      add(title, BorderLayout.NORTH);
                  
      pic.requestFocusInWindow();
            
      // reset button
      button = new JButton("START GAME");
      button.addActionListener(new ResetListener());
      add(button, BorderLayout.SOUTH);
      pic.requestFocusInWindow();
      
      boolean banana = true;

   }
   
   public TrianglePanel getTrianglePanel()
   {
      return pic;
   }
   
   public void textfields(int R, int G, int B, int H, int W)
   {
      //input subpanel
      inputs = new JPanel();
      inputs.setLayout(new GridLayout(5, 2));
      
      JLabel redLabel = new JLabel("Red: ");
      redLabel.setHorizontalAlignment(SwingConstants.RIGHT);
      inputs.add(redLabel);
      
      r = new JTextField(Integer.toString(R), 4);
      r.setHorizontalAlignment(SwingConstants.CENTER);
      inputs.add(r);
      
      JLabel greenLabel = new JLabel("Green: ");
      greenLabel.setHorizontalAlignment(SwingConstants.RIGHT);
      inputs.add(greenLabel);
      
      g = new JTextField(Integer.toString(G), 4);
      g.setHorizontalAlignment(SwingConstants.CENTER);
      inputs.add(g);
      
      JLabel blueLabel = new JLabel("Blue: ");
      blueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
      inputs.add(blueLabel);
      
      b = new JTextField(Integer.toString(B), 4);
      b.setHorizontalAlignment(SwingConstants.CENTER);
      inputs.add(b);
      
      JLabel hightLabel = new JLabel("Hight: ");
      hightLabel.setHorizontalAlignment(SwingConstants.RIGHT);
      inputs.add(hightLabel);
      
      h = new JTextField(Integer.toString(H), 5);
      h.setHorizontalAlignment(SwingConstants.CENTER);
      inputs.add(h);
      
      JLabel WidthLabel = new JLabel("Width: ");
      WidthLabel.setHorizontalAlignment(SwingConstants.RIGHT);
      inputs.add(WidthLabel);
      
      w = new JTextField(Integer.toString(W), 5);
      w.setHorizontalAlignment(SwingConstants.CENTER);
      inputs.add(w);
      
      add(inputs, BorderLayout.WEST);
   }
   
   private class ResetListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if(playtime)
         {
            int rInt = Integer.parseInt(r.getText());
            int gInt = Integer.parseInt(g.getText());
            int bInt = Integer.parseInt(b.getText());
            Color c = new Color(rInt, gInt, bInt);
            
            int hInt = Integer.parseInt(h.getText());
            int wInt = Integer.parseInt(w.getText());
            
            button.setBackground(c);
            button.updateUI();
            
            pic.reset(c, hInt, wInt);
            pic.requestFocusInWindow();
            }
            else
            {
               pic.playGame();
               pic.requestFocusInWindow();
               playtime = true;
               textfields(pic.getR(), pic.getG(), pic.getB(), pic.getH(), pic.getW());
               button.setText("RESET GAME");
               button.setBackground(new Color(pic.getR(), pic.getG(), pic.getB()));
               button.updateUI();
            }
      }
   }
}