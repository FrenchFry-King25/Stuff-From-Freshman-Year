import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class TrianglePanel extends JPanel
{
   public static final int FRAME_width = 400;
   public static final int FRAME_hight = 600;
   private static final Color BACKGROUND = new Color(204, 204, 204);
   private BufferedImage myImage;
   private Graphics myBuffer;
   
   final int[][] triangles = new int[][]{{18, 18, 1, 5, 30, 85, 160}, 
                                         {34, 34, 2, 4, 200, 100, 200}, 
                                         {26, 26, 3, 3, 160, 120, 35}}; //triangles[blue, magenta, gold][hight, width, speed, rocket_speed, r, g, b] (the rgb values are sent to the GUI panel to be in the JTextFields)
   Color[] colors = new Color[]{new Color(30, 85, 160), 
                                new Color(200, 100, 200), 
                                new Color(160, 120, 35)};
   
   Triangle[] players = new Triangle[3];
   Triangle player;
   
   Rocket[] rocks = new Rocket[25];
   
   int hitCounter, rocketsfired, hitAnimationCounter, cooldown = 0; 
   
   EnemyRow[] rows;
   Rocket[] enemyRockets = new Rocket[25];
      
   Timer t = new Timer(5, new AnimationListener());
   int time;
   
   boolean up, down, left, right = false;
   
   int choice;
   int dificulty; //1 = easy; 2 = medium; 3 = hard
   boolean playScreen = false;
   boolean scoreScreen = false;
   boolean startScreen = true;
   
   public TrianglePanel()
   {
      myImage =  new BufferedImage(FRAME_width, FRAME_hight, BufferedImage.TYPE_INT_RGB);
      myBuffer = myImage.getGraphics();
      myBuffer.setColor(BACKGROUND);
      myBuffer.fillRect(0,0,FRAME_width,FRAME_hight);
      
      players[0] = new Triangle(colors[0], triangles[0][0], triangles[0][1], 100, 250); // speed 1, rocket speed 5
      players[1] = new Triangle(colors[1], triangles[1][0], triangles[1][1], 200, 250); // speed 2, rocket speed 4
      players[2] = new Triangle(colors[2], triangles[2][0], triangles[2][1], 300, 250); // speed 3, rocket speed 3
      players[0].setCircleBoundaries(50, 150, 150, 250);
      players[1].setCircleBoundaries(150, 250, 150, 250);
      players[2].setCircleBoundaries(250, 350, 150, 250);
      
      choice = 1;
      dificulty = 2;
      
      addKeyListener(new Key());
      setFocusable(true);
      
      t.start();
   }
   
   public void paintComponent(Graphics g)
   {
      g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
   }
   
   
   public void reset(Color c, int h, int w)
   {
      player.stopMoving();
      player = new Triangle(c, h, w, 200, 500);
      up = down = left = right = false;
      player.stopMoving();
      myBuffer.setColor(BACKGROUND);
      myBuffer.fillRect(0,0,FRAME_width,FRAME_hight);
      player.draw(myBuffer);
      rows = new EnemyRow[4];
      for(int i = 0; i < 4; i++)
      {
         rows[i] = new EnemyRow((65*i) + 30, i%2);
      }
      for(int i = 0; i  < 25; i++)
      {
         rocks[i] = null;
         enemyRockets[i] = null;
      } 
      time = 0;
      playScreen = true;
      scoreScreen = false;
      hitCounter = 0;
      rocketsfired = 0;
      hitAnimationCounter = 0;
      repaint();
   }
   
   public void playGame()
   {
      playScreen = true; 
      startScreen = false;
      time = 0;
      
      player = new Triangle(colors[choice], triangles[choice][0], triangles[choice][1], 200, 500);
      player.draw(myBuffer);
      
      rows = new EnemyRow[4];
      for(int i = 0; i < 4; i++)
      {
         rows[i] = new EnemyRow((65*i) + 30, i%2);
      }
   }
   
   public void info()
   {
      myBuffer.setColor(new Color(0, 13, 0));
      myBuffer.drawLine(150, 150, 150, 400);
      myBuffer.drawLine(250, 150, 250, 400);
      myBuffer.setFont(new Font("Monospaced", Font.BOLD, 20));
      myBuffer.drawString("SELECT SHIP AND DIFFICULTY", 43, 75);
      myBuffer.setFont(new Font("Serif", Font.BOLD, 14));
      
      myBuffer.drawString("Player Speed: 1", 40, 300);
      myBuffer.drawString("Rocket Speed: 5", 40, 330);
      
      myBuffer.drawString("Player Speed: 2", 151, 300);
      myBuffer.drawString("Rocket Speed: 4", 151, 330);
      
      myBuffer.drawString("Player Speed: 3", 260, 300);
      myBuffer.drawString("Rocket Speed: 3", 260, 330);
      
      myBuffer.drawRect(100, 400, 200, 150);
      
      myBuffer.setColor(Color.GREEN);
      myBuffer.fillRect(101, 351 + (50*(4-dificulty)), 198, 48);
      myBuffer.setColor(new Color(0, 13, 0));
      myBuffer.setFont(new Font("Arial", Font.BOLD, 25));
      myBuffer.drawString("HARD" , 170, 430);
      myBuffer.drawString("MEDIUM", 155, 480);
      myBuffer.drawString("EASY", 170, 530);
      
      //arrows
      int[] y = new int[]{350, 360, 370};
      if(choice < 2)//right arrow
      {
         int[] x = new int[]{110 + (100*choice), 125 + (100*choice), 110 + (100*choice)};
         myBuffer.fillPolygon(x, y, 3);
      }
      if(choice > 0)//left arrow
      {
         int[] x = new int[]{90 + (100*choice), 75 + (100*choice), 90 + (100*choice)};
         myBuffer.fillPolygon(x, y, 3);
      }
      myBuffer.fillRect(90 + (100*choice), 358, 20, 4);
      int[] x = new int[]{42, 50, 58};
      if(dificulty < 3) //up arrow
      {
         int[] y2 = new int[]{465, 450, 465};
         myBuffer.fillPolygon(x, y2, 3);
      }
      if(dificulty > 1) //down arrow
      {
         int[] y2 = new int[]{485, 500, 485};
         myBuffer.fillPolygon(x, y2, 3);
      }
      myBuffer.fillRect(48, 465, 4, 20);      
   }
   
   //these are for the GUI panel to acsess the RGB and hight and with for the JTextFields
   public int getR()
   {
      return triangles[choice][4];
   }
   public int getG()
   {
      return triangles[choice][5];
   }
   public int getB()
   {
      return triangles[choice][6];
   }
   public int getH()
   {
      return triangles[choice][0];
   }
   public int getW()
   {
      return triangles[choice][1];
   }
   
   public void drawTime()
   {
      myBuffer.setColor(new Color(0, 13, 0));
      myBuffer.setFont(new Font("Monospaced", Font.BOLD, 20));
      myBuffer.drawString("TIME:" + time/100, 10, 20);
      
      int score = (dificulty*1500) - (time/100*10) - ((rocketsfired - 28) *15) - (hitCounter*((4 - dificulty)*100));
      myBuffer.setColor(new Color(0, 13, 0));
      myBuffer.drawString("SCORE: " + score, 250, 20);  
   }
   
   public void shootEnemyRocket()
   {
      double rand = Math.random()*100;
      int nextRow = 3;
      double dificultyChange = (3-dificulty)*.45;
      if(rand > 99+dificultyChange)
      {
         for(int i = 3; i >= 0; i--)
         {
            if(rows[i].done())
            {
               nextRow--;
            }
            else
            {
               i = -1;
            }
         }
         if(nextRow >= 0)
         {
            int[] xy = rows[nextRow].getRandomXY(); 
          
            for(int i = 0; i < 25; i++)
            {
               if(enemyRockets[i] == null)
               {
                  enemyRockets[i] = new Rocket(xy[0], xy[1], 1);
                  break;
               }
            } 
         }
      }
   }
   
   private class AnimationListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e) 
      {
         myBuffer.setColor(BACKGROUND);
         myBuffer.fillRect(0,0,FRAME_width,FRAME_hight);
           
         if(playScreen)
         {
            //making sure player isn't off the screen
            //player.restrictMovement(); nvm failed attempt
            
            //timer and checking when the game is done to set it to score screen
            int c = 0;
            for(int i = 0; i < 4; i ++)
            {
               if(rows[i].done())
               {
                  c++;
               }
            }
            if(c != 4)//moving the triangle & drawing time/score
            {
               time++;
               drawTime();
               if(hitAnimationCounter %4 == 0)
               {
                  player.draw(myBuffer);
               }
            }
            else
            {
               playScreen = false;
               scoreScreen = true;
               for(int i = 0; i  < 25; i++)
               {
                  rocks[i] = null;
                  enemyRockets[i] = null;
               } 
            }
            //rocket
            for(int i = 0; i < 25; i ++)
            {   
               
               if(rocks[i] != null)
               { 
                  int[] xy = rocks[i].getXY();
                        
                  if(rows[0].hit(xy[0], xy[1]) || rows[1].hit(xy[0], xy[1]) || rows[2].hit(xy[0], xy[1]) || rows[3].hit(xy[0], xy[1]))
                  {
                     rocks[i] = null;
                  }
                  else if(rocks[i].ifOut())
                  {
                     rocks[i] = null;
                  }
                  else
                  {
                     rocks[i].moveUp(triangles[choice][3]);
                     rocks[i].draw(myBuffer);
                  }
               }
            } 
               
            
            for(int i = 0; i < 4; i ++)//drawing enemies
            {
               rows[i].draw(myBuffer);
            }
            shootEnemyRocket();
            for(int i = 0; i < 25; i++)//enemy rockets
            {
               if(enemyRockets[i] != null)
               {
                  int[] xy = enemyRockets[i].getXY();
                  if(player.hit(xy[0], xy[1]))
                  {
                     //System.out.println("OUT");
                     hitCounter++;
                     hitAnimationCounter += 32;
                     enemyRockets[i] = null;
                  }
                  else if(enemyRockets[i].ifOut())
                  {
                     enemyRockets[i] = null;
                  }
                  else
                  {
                     enemyRockets[i].moveDown();
                     enemyRockets[i].draw(myBuffer);
                  }
               }  
            }
            if(hitAnimationCounter != 0)
            {
               hitAnimationCounter--;
            }
         }
         if(scoreScreen)
         {
            int score = (dificulty*1500) - (time/100*10) - ((rocketsfired - 28) *15) - (hitCounter*((4 - dificulty)*100));
            myBuffer.setColor(new Color(0, 13, 0));
            myBuffer.setFont(new Font("Monospaced", Font.BOLD, 25));
            myBuffer.drawString("SCORE: " + score, 100, 300);
            myBuffer.setFont(new Font("Monospaced", Font.BOLD, 20));
            String d = "Medium";
            if(dificulty == 1) d = "Easy";
            if(dificulty == 2) d = "Medium";
            if(dificulty == 3) d = "Hard";
            myBuffer.drawString("Difficulty: " + d, 85, 350);
            myBuffer.drawString("Total time: " + time/100 + " seconds", 60, 375);
            myBuffer.drawString("Hit " + hitCounter + " times", 125, 400);
            myBuffer.drawString(rocketsfired + " lasers fired", 95, 425);            
         }
         if(startScreen)
         {
            myBuffer.setColor(Color.GREEN);
            myBuffer.fillRect(((choice+1)*100)- 50, 150, 100, 100);
         
            for(int i = 0; i < 3; i++)
            {
               players[i].circle(triangles[i][2]);
               players[i].draw(myBuffer);
            }
            info();
         }
         repaint();
      }
   }
   
   private class Key extends KeyAdapter
   {
      public void keyPressed(KeyEvent e)
      {
         if(playScreen)
         {
            if(e.getKeyCode() == KeyEvent.VK_UP && !up)
            {
               player.setDy(-triangles[choice][2]);
               up = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_DOWN && !down)
            {
               player.setDy(triangles[choice][2]);
               down = true;
            }            
            if(e.getKeyCode() == KeyEvent.VK_RIGHT && !right)
            {
               player.setDx(triangles[choice][2]);
               right = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_LEFT && !left)
            {
               player.setDx(-triangles[choice][2]);
               left = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_SPACE)
            {
               for(int i = 0; i  < 25; i++)
               {
                  if(rocks[i] == null)
                  {
                     int[][] location = player.getLocation();
                     rocks[i] = new Rocket(location[0][1], location[1][1], triangles[choice][3]);
                     //System.out.println("yupp");
                     break;
                  }
               }  
               rocketsfired++;
            }
         }
         else
         {
            if(e.getKeyCode() == KeyEvent.VK_RIGHT && choice < 2)
            {
               choice++;
            }
            if(e.getKeyCode() == KeyEvent.VK_LEFT && choice > 0)
            {
               choice--;
            }
            if(e.getKeyCode() == KeyEvent.VK_UP && dificulty < 3)
            {
               dificulty++;
            }
            if(e.getKeyCode() == KeyEvent.VK_DOWN && dificulty > 1)
            {
               dificulty--;
            }
         }
      }
      
      public void keyReleased(KeyEvent e)
      {
         if(playScreen)
         {
            if(e.getKeyCode() == KeyEvent.VK_UP)
            {
               player.setDy(triangles[choice][2]);
               up = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_DOWN)
            {
               player.setDy(-triangles[choice][2]);
               down = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            {
               player.setDx(-triangles[choice][2]);
               right = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_LEFT)
            {
               player.setDx(triangles[choice][2]);
               left = false;
            }
         }
      }
   }
}