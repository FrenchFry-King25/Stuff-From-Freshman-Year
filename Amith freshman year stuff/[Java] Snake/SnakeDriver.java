import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Random;
import javax.swing.border.LineBorder;

public class SnakeDriver
{
   public static void main(String[] args)
   { 
      JFrame frame = new JFrame("SNAKE");
      frame.setSize(550, 635);
      frame.setLocation(20, 20);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(new GUIPanel());
      frame.setVisible(true);
   }
}

class GUIPanel extends JPanel
{
   DrawingPanel drawPan = new DrawingPanel();
   JLabel title;
   JButton restart;
   boolean start = true;
  
   public GUIPanel()
   {
      setLayout(new BorderLayout());
      title = new JLabel("SNAKE");
      title.setFont(new Font("impact", Font.ITALIC, 25));
      title.setHorizontalAlignment(SwingConstants.CENTER);
      title.setForeground(new Color(0, 0, 0));
      title.setOpaque(true);
      title.setBackground(new Color(200, 200, 200));
      add(title, BorderLayout.NORTH);
    
      drawPan.requestFocusInWindow();
      add(drawPan);
      
      restart = new JButton("START");
      restart.addActionListener(new RestartListener());
      restart.setBackground(new Color(200, 200, 200));
      restart.setForeground(new Color(0, 0, 0));
      restart.setBorder(new LineBorder(new Color(0, 0, 0)));
      add(restart, BorderLayout.SOUTH); 
   }
  
   private class RestartListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if(start)
         {
            title.setForeground(new Color(255, 255, 255));
            title.setOpaque(true);
            title.setBackground(new Color(54, 54, 54));
            title.updateUI();
            restart.setText("RESET");
            restart.setBackground(new Color(54, 54, 54));
            restart.setForeground(new Color(255, 255, 255));  
            restart.setBorder(new LineBorder(new Color(255, 255, 255)));          
            restart.updateUI();
            drawPan.requestFocusInWindow();
            drawPan.playTime();
            start = false;
         }
         else
         {
            drawPan.newGame();
            drawPan.requestFocusInWindow();
         }
      } 
   }
}

class DrawingPanel extends JPanel
{
   Apple apple;
   Snake snake; 
   String direction;
   Timer timer = new Timer(5, new AnimationListener());  
   int time; 
   private BufferedImage myImage;
   private Graphics myBuffer;
   int previousDirection;
   String stage; //START, PLAY, and GAMEOVER are the stages
   boolean prePlay;
   int color, hat, fruit; //0 = blue; 1 = green; 2 = magenta; 3 = yellow; 0 = cowboy, 1 = top-hat, 2 = beanie, 3 = none; 0 = apple, 1 = orange, 2 = watermelon
   int score, toGrow, speed, cooldown, speedOffset, appleCounter; // btw, lower speed = faster
   boolean power, dud;
   Apple[] trippleApples = new Apple[3];
   boolean[] trippleApplesLives = new boolean[3];
  
   public DrawingPanel()
   {
      myImage =  new BufferedImage(550, 600, BufferedImage.TYPE_INT_RGB);
      myBuffer = myImage.getGraphics();
    
      timer.start();
      time = 0;
    
      stage = "START";
      prePlay = false;
      color = hat = fruit = 0; // default is a blue snake with cowboy hat and apples
    
      direction = "NONE";
      snake = new Snake(5, 5, "NORTH");
      previousDirection = 4;
      toGrow = 0; 
    
      randomApple();
      power = false;
      dud = false;
      speedOffset = 0;//chage this number to change the speed of the snake: smaller = faster, bigger = slower; don't go below -9
      speed = 15;
      cooldown = 0;
      appleCounter = 0;
    
      addKeyListener(new Key());
      setFocusable(true);
   }
  
   public void paintComponent(Graphics g)
   {
      g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
   }
  
   public void randomApple()
   {
      boolean appleNotFound = true;
      int appleX = 0;
      int appleY = 0;
      while(appleNotFound)
      {
         int[][] snakeBody = snake.getLocation();
         int snakeLength = snake.getLength();
         appleX = (int)(Math.random()*10);
         appleY = (int)(Math.random()*10);
         appleNotFound = false;
      
         for(int i = 0; i < snakeLength + 1; i++)
         {
            if(snakeBody[i][0] == appleX && snakeBody[i][1] == appleY)
            {
               appleNotFound = true; 
            }
         }
      }
      
      apple = new Apple(appleX, appleY);
      
      if(cooldown == 0 && (int)(Math.random()*10) > 7)
      {
         //System.out.println("power");
         power = true;
      }  
   }
   
   public void randomTrippleApple()
   {	
      for(int x = 0; x < 3; x++)
      {
         boolean appleNotFound = true;
         int appleX = 0;
         int appleY = 0;
         while(appleNotFound)
         {
            int[][] snakeBody = snake.getLocation();
            int snakeLength = snake.getLength();
            appleX = (int)(Math.random()*10);
            appleY = (int)(Math.random()*10);
            appleNotFound = false;
         
            for(int i = 0; i < snakeLength + 1; i++)
            {
               if(snakeBody[i][0] == appleX && snakeBody[i][1] == appleY)
               {
                  appleNotFound = true; 
               }
            }
            if(x == 1 || x == 2)
            {
               if(trippleApples[0].getX() == appleX && trippleApples[0].getY() == appleY)
               {
                  appleNotFound = true; 
               }
            }
            if(x == 2)
            {
               if(trippleApples[1].getX() == appleX && trippleApples[1].getY() == appleY)
               {
                  appleNotFound = true;
               }
            }
           
         }
        
         trippleApples[x] = new Apple(appleX, appleY);
         trippleApplesLives[x] = true;
      }
   }
  
   public void newGame()
   {
      snake = new Snake(5,5,"NORTH");
      stage = "PLAY";
      prePlay = true;
      direction = "NONE";
      previousDirection = 4;
      score = toGrow = appleCounter = cooldown = 0;
      randomApple();
      power = dud = false;
      animate();
   }
   
   public void playTime()
   {
      stage = "PLAY";
      prePlay = true;
   }
  
   public void animate()
   {
      if(stage == "START")
      {
         start();
      }
      if(stage == "PLAY")
      {
         play();
      }
      if(stage == "GAMEOVER")
      {
         //myBuffer.setColor(new Color(54, 54, 54)); //background color
         //myBuffer.fillRect(0,0,550,600); 
         myBuffer.setFont(new Font("Monospaced", Font.BOLD, 70));
         myBuffer.setColor(Color.WHITE);
         myBuffer.drawString("G A M E", 100, 218); 
         myBuffer.drawString("O V E R", 147, 268); 
         //myBuffer.setColor(new Color(255, 255, 255));
         myBuffer.drawString("SCORE: " + score, 100, 422); 
      }
      repaint();
   }
   
   public void start()
   {
      myBuffer.setColor(new Color(200, 200, 200)); //background color
      myBuffer.fillRect(0,0,550,600); 
      
      myBuffer.setColor(new Color(0, 0, 0));
      myBuffer.setFont(new Font("Monospaced", Font.BOLD, 20));
      myBuffer.drawString("Select Color:", 50, 35); 
      
      Color[] colors = new Color[]{Color.BLUE, Color.GREEN, new Color(200, 100, 200), Color.YELLOW};
      //snake options: blue, green, magenta, and yellow
         
      myBuffer.setColor(new Color(0, 0, 0));
      myBuffer.fillRect(75 + (color*100), 48, 100, 156);
      
      int[] y = new int[]{220, 230, 240};
      if(color < 3)//right arrow
      {
         int[] x = new int[]{140 + (100*color), 155 + (100*color), 140 + (100*color)};
         myBuffer.fillPolygon(x, y, 3);
      }
      if(color > 0)//left arrow
      {
         int[] x = new int[]{110 + (100*color), 95 + (100*color), 110 + (100*color)};
         myBuffer.fillPolygon(x, y, 3);
      }
      myBuffer.fillRect(110 + (100*color), 228, 30, 4);
      
      for(int i = 0; i < 4; i++)
      {
         myBuffer.setColor(colors[i]);
         myBuffer.fillRect(100 + (i*100), 50, 50, 50);
         myBuffer.fillRect(100 + (i*100), 102, 50, 50);
         myBuffer.fillRect(100 + (i*100), 154, 50, 50); 
      }
      
      //accesories: cowboy hat, tophat, beanie
      myBuffer.setFont(new Font("Monospaced", Font.BOLD, 20));//instructions
      myBuffer.setColor(new Color(0, 0, 0));
      myBuffer.drawString("Select Hat:", 75, 275); 
      
      myBuffer.setColor(new Color(0, 0, 0));
      myBuffer.drawRect(73, 285 + (hat*60), 54, 60);//selection square
      myBuffer.drawRect(74, 286 + (hat*60), 52, 58);
      
      myBuffer.setColor(new Color(160, 120, 60));//cowboy hat
      myBuffer.fillOval(90, 300, 12, 30);
      myBuffer.fillOval(98, 300, 12, 30);
      myBuffer.fillOval(77, 320, 46, 10);
      myBuffer.fillOval(77, 318, 10, 10);
      myBuffer.fillOval(113, 318, 10, 10);
      myBuffer.setColor(new Color(0, 0, 0));
      myBuffer.fillRect(92, 318, 18, 2);
      myBuffer.fillOval(82, 321, 36, 7);
      
      myBuffer.fillRect(90, 360, 20, 30);//top-hat
      myBuffer.fillOval(80, 380, 40, 10);
      
      myBuffer.setColor(new Color(50, 190, 255));// beanie
      myBuffer.fillOval(76, 420, 48, 30);
      myBuffer.fillRect(76, 435, 48, 15);
      myBuffer.setColor(new Color(255, 50, 50));
      for(int i = 0; i < 5; i++)
         myBuffer.fillRect(79 + (i*10), 435, 2, 15);      
      
      myBuffer.setFont(new Font("Monospaced", Font.BOLD, 20));//no hat
      myBuffer.setColor(new Color(0, 0, 0));
      myBuffer.drawString("No", 85, 485); 
      myBuffer.drawString("Hat", 80, 500);  
      
      int[] x = new int[]{142, 150, 158};
      if(hat > 0) //up arrow
      {
         int[] y2 = new int[]{305 + (hat*60), 290 + (hat*60), 305 + (hat*60)};
         myBuffer.fillPolygon(x, y2, 3);
      }
      if(hat < 3) //down arrow
      {
         int[] y2 = new int[]{325 + (hat*60), 340 + (hat*60), 325 + (hat*60)};
         myBuffer.fillPolygon(x, y2, 3);
      }
      myBuffer.fillRect(148, 305 + (hat*60), 4, 20);  
      
      //fruit: apple, orange, watermelon
      myBuffer.setFont(new Font("Monospaced", Font.BOLD, 20));//instructions
      myBuffer.setColor(new Color(0, 0, 0));
      myBuffer.drawString("Select Fruit:", 265, 275);
      
      myBuffer.setColor(Color.RED); //apple
      myBuffer.fillOval(3 + 275, 6 + 300, 44, 44);
      myBuffer.setColor(new Color(140, 96, 34));
      myBuffer.fillRect(23 + 275, 0 + 300, 4, 10);
      myBuffer.setColor(new Color(0,102,0));
      myBuffer.fillOval(26 + 275, 0 + 300, 12, 6);
      
      myBuffer.setColor(new Color(255,128,0));//orange
      myBuffer.fillOval(0 + 275,0 + 380,50,50);
      myBuffer.setColor(new Color(0,102,0));
      myBuffer.fillOval(20+ 275,5+ 380,10,10);
      myBuffer.setColor(new Color(255,153,51));
      myBuffer.fillRect(4+ 275,25+ 380,2,2);
      myBuffer.fillRect(10+ 275,25+ 380,2,2);
      myBuffer.fillRect(20+ 275,28+ 380,2,2); 
      myBuffer.fillRect(30+ 275,10+ 380,2,2);
      myBuffer.fillRect(40+ 275,46+ 380,2,2);
      myBuffer.fillRect(45+ 275,3+ 380,2,2);
      myBuffer.fillRect(26+ 275,25+ 380,2,2);
      
      int[] xWater = new int[]{5 + 275, 25 + 275, 45 + 275};//watermelon
      int[] yWater = new int[]{40 + 460, 0 + 460, 40 + 460};
      myBuffer.setColor(Color.RED);
      myBuffer.fillPolygon(xWater, yWater, 3);
      myBuffer.setColor(Color.GREEN);
      myBuffer.fillRect(5 + 275, 40 + 460, 40, 10);
      myBuffer.setColor(Color.BLACK);
      myBuffer.fillOval(20 + 275, 17 + 460, 5, 5);
      myBuffer.fillOval(30 + 275, 30 + 460, 5, 5);
      myBuffer.fillOval(15 + 275, 27 + 460, 5, 5);
      
      myBuffer.setColor(new Color(0, 0, 0));
      myBuffer.drawRect(273, 285 + (fruit*80), 54, 80);//selection square
      myBuffer.drawRect(274, 286 + (fruit*80), 52, 78);
      
      int[] x2 = new int[]{339, 351, 359};
      if(fruit < 2) //down arrow
      {
         int[] y3 = new int[]{335 + (fruit*80), 350 + (fruit*80), 335 + (fruit*80)};
         myBuffer.fillPolygon(x2, y3, 3);
         myBuffer.fillRect(349, 325 + (fruit*80), 4, 10);
         myBuffer.setFont(new Font("Monospaced", Font.BOLD, 15));
         myBuffer.drawString("SPACE", 329, 315 + (fruit*80));
      }
      else //up arrow
      {
         int[] y3 = new int[]{475, 460, 475};
         myBuffer.fillPolygon(x2, y3, 3);
         int[] y4 = new int[]{465, 450, 465};
         myBuffer.fillPolygon(x2, y4, 3);
         myBuffer.fillRect(349, 475, 4, 10);
         myBuffer.setFont(new Font("Monospaced", Font.BOLD, 15));
         myBuffer.drawString("SPACE", 330, 495);
      }
   }
  
   public void play()
   {
      if(cooldown == 0)
         speed = 15;
      if(time % (speed + speedOffset) == 0)
      {
         if(!prePlay)
         {
            if(toGrow > 0) 
            {
               snake.move(true);
               toGrow--; 
            }
            else
               snake.move(false);
            previousDirection = snake.getDirection();
         }
         if(snake.hitWall() || snake.hitItself())
         {
            stage = "GAMEOVER";
         }
         int[][] snakeBody = snake.getLocation();
         if(appleCounter == 0)
         {
            if(apple.eaten(snakeBody[0][0], snakeBody[0][1]))
            {	
               dud=false;
               int addGrowth = 0;
               if(power)
               {
                  int randPower = (int)(Math.random()*5);
                  if(randPower == 0)//super fruit
                  {
                     addGrowth = 4;
                     cooldown = 20;
                  //System.out.println("grown"); 
                  }
                  else if(randPower == 1)//slow snake
                  {
                     speed = 10;
                     cooldown = 50;
                  }
                  else if(randPower == 2)//fast snake
                  {
                     speed = 20;
                     cooldown = 25;
                  }
                  else if (randPower == 3)
                  {
                     dud = true;
                     cooldown = 20;
                  }
                  else if(randPower == 4)
                  {
                     randomTrippleApple();
                     appleCounter = 3;
                  }
               }
               else
               {
                  score++;
                  addGrowth = 2;
               }
               toGrow += addGrowth;
               power = false;
               randomApple();
            }  
         }
         else
         {
            for(int i = 0; i < 3; i++)
            {
               if(trippleApplesLives[i])
               {
                  if(trippleApples[i].eaten(snakeBody[0][0], snakeBody[0][1]))
                  {
                     trippleApplesLives[i] = false;
                     appleCounter--;
                     score++;
                  }
               }
            }
         }
         if(cooldown > 0)
            cooldown--;
         
         myBuffer.setColor(new Color(54, 54, 54)); //repainting the background
         myBuffer.fillRect(0,0,550,600); 
         
         myBuffer.setColor(new Color(255, 255, 255)); //drawing the gridlines 
         for(int i = 0; i < 11; i++)
         {
            myBuffer.drawLine(25 + (i*50), 25, 25 + (i*50), 525);
            myBuffer.drawLine(25, 25 + (i*50), 525, 25 + (i*50));
         }
         
         //drawing the snake
         int snakeLength = snake.getLength();
         for(int i = 0; i < snakeLength + 1; i++)
         {
            int x = 0;
            if(i < 35)
               x = i;
            else if(i < 70)
               x = 70 - i;
            else
               x = i - 70; 
            if(color == 0) myBuffer.setColor(new Color(0, 0, 255 - (x*4)));
            else if(color == 1) myBuffer.setColor(new Color(0, 255 - (x*4), 0));
            else if(color == 2) myBuffer.setColor(new Color(200 - (x*3), 100 - (x*2), 200 - (x*3)));
            else if(color == 3) myBuffer.setColor(new Color(255 - (x*4), 255 - (x * 4), 0));
            myBuffer.fillRect(25 + (snakeBody[i][0]*50), 25 + (snakeBody[i][1]*50), 50, 50);
         }
            
            //drawing the hat
         if(hat == 0)
         {
            myBuffer.setColor(new Color(160, 120, 60));
            myBuffer.fillOval(15 + 25 + (snakeBody[0][0]*50), 0 + 25 + (snakeBody[0][1]*50), 12, 30);//cowboy hat
            myBuffer.fillOval(23 + 25 + (snakeBody[0][0]*50), 0 + 25 + (snakeBody[0][1]*50), 12, 30);
            myBuffer.fillOval(2 + 25 + (snakeBody[0][0]*50), 20 + 25 + (snakeBody[0][1]*50), 46, 10);
            myBuffer.fillOval(2 + 25 + (snakeBody[0][0]*50), 18 + 25 + (snakeBody[0][1]*50), 10, 10);
            myBuffer.fillOval(38 + 25 + (snakeBody[0][0]*50), 18 + 25 + (snakeBody[0][1]*50), 10, 10);
            myBuffer.setColor(new Color(0, 0, 0));
            myBuffer.fillRect(17 + 25 + (snakeBody[0][0]*50), 18 + 25 + (snakeBody[0][1]*50), 18, 2);
            myBuffer.fillOval(7 + 25 + (snakeBody[0][0]*50), 21 + 25 + (snakeBody[0][1]*50), 36, 7);
         }
         else if(hat == 1)
         {
            myBuffer.setColor(new Color(0, 0, 0));
            myBuffer.fillRect(15 + 25 + (snakeBody[0][0]*50), 0 + 25 + (snakeBody[0][1]*50), 20, 30);//top-hat
            myBuffer.fillOval(5 + 25 + (snakeBody[0][0]*50), 20 + 25 + (snakeBody[0][1]*50), 40, 10);
         }
         else if(hat == 2)
         {
            myBuffer.setColor(new Color(50, 190, 255));// beanie
            myBuffer.fillOval(1 + 25 + (snakeBody[0][0]*50), 0 + 25 + (snakeBody[0][1]*50), 48, 30);
            myBuffer.fillRect(1 + 25 + (snakeBody[0][0]*50), 15 + 25 + (snakeBody[0][1]*50), 48, 15);
            myBuffer.setColor(new Color(255, 50, 50));
            for(int i = 0; i < 5; i++)
               myBuffer.fillRect(4 + (i*10) + 25 + (snakeBody[0][0]*50), 15 + 25 + (snakeBody[0][1]*50), 2, 15);
         }
            
            //drawing eyes            
         int xOff = 0;
         int yOff = 0;
         if(snakeBody[0][2] == 0) yOff -= 5;
         else if(snakeBody[0][2] == 1) xOff += 8;
         else if(snakeBody[0][2] == 2) yOff += 5;
         else if(snakeBody[0][2] == 3) xOff -= 8;
         int red = 0;
         if(score >= 25) red = 255;
         myBuffer.setColor(new Color(red, 0, 0));
         myBuffer.fillOval(10 + xOff + 25 + (snakeBody[0][0]*50), 35 + yOff + 25 + (snakeBody[0][1]*50), 10, 10);
         myBuffer.fillOval(30 + xOff + 25 + (snakeBody[0][0]*50), 35 + yOff + 25 + (snakeBody[0][1]*50), 10, 10);
         
         if(appleCounter == 0)
         {
            if(power)//draw mystery box
            {
               myBuffer.setColor(new Color(164, 73, 164));
               myBuffer.fillRect(1 + 25 +(apple.getX()*50), 1 + 25 +(apple.getY()*50), 48, 48); 
               myBuffer.setColor(new Color(195, 195, 195));           
               myBuffer.fillRect(3 + 25 +(apple.getX()*50), 3 + 25 +(apple.getY()*50), 44, 44);
               myBuffer.setColor(new Color(0, 0, 0));
               myBuffer.fillRect(12 + 25 +(apple.getX()*50), 7 + 25 +(apple.getY()*50), 3, 7);
               myBuffer.fillRect(12 + 25 +(apple.getX()*50), 7 + 25 +(apple.getY()*50), 22, 3);
               myBuffer.fillRect(31 + 25 +(apple.getX()*50), 7 + 25 +(apple.getY()*50), 3, 20);
               myBuffer.fillRect(22 + 25 +(apple.getX()*50), 27 + 25 +(apple.getY()*50), 12, 3);
               myBuffer.fillRect(22 + 25 +(apple.getX()*50), 27 + 25 +(apple.getY()*50), 3, 8);
               myBuffer.fillOval(20 + 25 +(apple.getX()*50), 39 + 25 +(apple.getY()*50), 7, 7);
            } 
            else if (fruit == 0) // draw apple
            {
               myBuffer.setColor(Color.RED); //apple
               myBuffer.fillOval(3 + 25+ (apple.getX()*50), 6 + 25 +(apple.getY()*50), 44, 44);
               myBuffer.setColor(new Color(140, 96, 34));
               myBuffer.fillRect(23 + 25+ (apple.getX()*50), 0 + 25 +(apple.getY()*50), 4, 10);
               myBuffer.setColor(new Color(0,102,0));
               myBuffer.fillOval(26 + 25+ (apple.getX()*50), 0 + 25 +(apple.getY()*50), 12, 6);
            }
            else if(fruit == 1) //draw orange
            {
               myBuffer.setColor(new Color(255,128,0));//orange
               myBuffer.fillOval(0 + 25+ (apple.getX()*50),25 +(apple.getY()*50),50,50);
               myBuffer.setColor(new Color(0,102,0));
               myBuffer.fillOval(20+ 25 +(apple.getX()*50),1+ 25 +(apple.getY()*50),10,10);
               myBuffer.setColor(new Color(255,153,51));
               myBuffer.fillRect(4 + 25 + (apple.getX()*50),25+ 25 +(apple.getY()*50),2,2);
               myBuffer.fillRect(10+ 25 + (apple.getX()*50),25+ 25 +(apple.getY()*50),2,2);
               myBuffer.fillRect(20+ 25 + (apple.getX()*50),28+ 25 +(apple.getY()*50),2,2); 
               myBuffer.fillRect(30+ 25 +(apple.getX()*50),10+ 25 +(apple.getY()*50),2,2);
               myBuffer.fillRect(40+ 25 +(apple.getX()*50),35+ 25 +(apple.getY()*50),2,2);
               myBuffer.fillRect(35+ 25 +(apple.getX()*50),40+ 25 +(apple.getY()*50),2,2);
               myBuffer.fillRect(26+ 25 +(apple.getX()*50),25+ 25 +(apple.getY()*50),2,2);
               myBuffer.fillRect(15+ 25 +(apple.getX()*50),5+ 25 +(apple.getY()*50),2,2);
               myBuffer.fillRect(35+ 25 +(apple.getX()*50),6+ 25 +(apple.getY()*50),2,2);
            
            }
            else if(fruit == 2)//draw watermelon
            {
               int[] xWater = new int[]{5 + 25 +(apple.getX()*50), 25 + 25 +(apple.getX()*50), 45 + 25 +(apple.getX()*50)};//watermelon
               int[] yWater = new int[]{40 + 25 +(apple.getY()*50), 0 + 25 +(apple.getY()*50), 40 + 25 +(apple.getY()*50)};
               myBuffer.setColor(Color.RED);
               myBuffer.fillPolygon(xWater, yWater, 3);
               myBuffer.setColor(Color.GREEN);
               myBuffer.fillRect(5 + 25 +(apple.getX()*50), 40 + 25 +(apple.getY()*50), 40, 10);
               myBuffer.setColor(Color.BLACK);
               myBuffer.fillOval(20 + 25 +(apple.getX()*50), 17 + 25 +(apple.getY()*50), 5, 5);
               myBuffer.fillOval(30 + 25 +(apple.getX()*50), 30 + 25 +(apple.getY()*50), 5, 5);
               myBuffer.fillOval(15 + 25 +(apple.getX()*50), 27 + 25 +(apple.getY()*50), 5, 5);
            }    
         }
         else
         {
            for(int i = 0; i < 3; i++)
            {
               if(trippleApplesLives[i])
               {
                  if (fruit == 0) // draw Apple
                  {
                     myBuffer.setColor(Color.RED); //trippleApples[i]
                     myBuffer.fillOval(3 + 25+ (trippleApples[i].getX()*50), 6 + 25 +(trippleApples[i].getY()*50), 44, 44);
                     myBuffer.setColor(new Color(140, 96, 34));
                     myBuffer.fillRect(23 + 25+ (trippleApples[i].getX()*50), 0 + 25 +(trippleApples[i].getY()*50), 4, 10);
                     myBuffer.setColor(new Color(0,102,0));
                     myBuffer.fillOval(26 + 25+ (trippleApples[i].getX()*50), 0 + 25 +(trippleApples[i].getY()*50), 12, 6);
                  }
                  else if(fruit == 1) //draw orange
                  {
                     myBuffer.setColor(new Color(255,128,0));//orange
                     myBuffer.fillOval(0 + 25+ (trippleApples[i].getX()*50),25 +(trippleApples[i].getY()*50),50,50);
                     myBuffer.setColor(new Color(0,102,0));
                     myBuffer.fillOval(20+ 25 +(trippleApples[i].getX()*50),1+ 25 +(trippleApples[i].getY()*50),10,10);
                     myBuffer.setColor(new Color(255,153,51));
                     myBuffer.fillRect(4 + 25 + (trippleApples[i].getX()*50),25+ 25 +(trippleApples[i].getY()*50),2,2);
                     myBuffer.fillRect(10+ 25 + (trippleApples[i].getX()*50),25+ 25 +(trippleApples[i].getY()*50),2,2);
                     myBuffer.fillRect(20+ 25 + (trippleApples[i].getX()*50),28+ 25 +(trippleApples[i].getY()*50),2,2); 
                     myBuffer.fillRect(30+ 25 +(trippleApples[i].getX()*50),10+ 25 +(trippleApples[i].getY()*50),2,2);
                     myBuffer.fillRect(40+ 25 +(trippleApples[i].getX()*50),35+ 25 +(trippleApples[i].getY()*50),2,2);
                     myBuffer.fillRect(35+ 25 +(trippleApples[i].getX()*50),40+ 25 +(trippleApples[i].getY()*50),2,2);
                     myBuffer.fillRect(26+ 25 +(trippleApples[i].getX()*50),25+ 25 +(trippleApples[i].getY()*50),2,2);
                     myBuffer.fillRect(15+ 25 +(trippleApples[i].getX()*50),5+ 25 +(trippleApples[i].getY()*50),2,2);
                     myBuffer.fillRect(35+ 25 +(trippleApples[i].getX()*50),6+ 25 +(trippleApples[i].getY()*50),2,2);
                  
                  }
                  else if(fruit == 2)//draw watermelon
                  {
                     int[] xWater = new int[]{5 + 25 +(trippleApples[i].getX()*50), 25 + 25 +(trippleApples[i].getX()*50), 45 + 25 +(trippleApples[i].getX()*50)};//watermelon
                     int[] yWater = new int[]{40 + 25 +(trippleApples[i].getY()*50), 0 + 25 +(trippleApples[i].getY()*50), 40 + 25 +(trippleApples[i].getY()*50)};
                     myBuffer.setColor(Color.RED);
                     myBuffer.fillPolygon(xWater, yWater, 3);
                     myBuffer.setColor(Color.GREEN);
                     myBuffer.fillRect(5 + 25 +(trippleApples[i].getX()*50), 40 + 25 +(trippleApples[i].getY()*50), 40, 10);
                     myBuffer.setColor(Color.BLACK);
                     myBuffer.fillOval(20 + 25 +(trippleApples[i].getX()*50), 17 + 25 +(trippleApples[i].getY()*50), 5, 5);
                     myBuffer.fillOval(30 + 25 +(trippleApples[i].getX()*50), 30 + 25 +(trippleApples[i].getY()*50), 5, 5);
                     myBuffer.fillOval(15 + 25 +(trippleApples[i].getX()*50), 27 + 25 +(trippleApples[i].getY()*50), 5, 5);
                  }
               }
            }
         }
            //score
         myBuffer.setFont(new Font("Monospaced", Font.BOLD, 25));
         myBuffer.setColor(new Color(255, 255, 255));
         myBuffer.drawString("SCORE: " + score, 130, 570);  
         
         //powerups
         if(speed == 10)
         {
            myBuffer.setColor(Color.GREEN);
            myBuffer.drawString("Fast Snake", 330, 570); 
         }
         else if (speed == 20)
         {
            myBuffer.setColor(Color.RED);
            myBuffer.drawString("Slow Snake", 330, 570); 
         }
         else if (dud == true)
         {
            myBuffer.setColor(Color.RED);
            myBuffer.drawString("Dud:(", 330, 570); 
         }
         else if (appleCounter>0)
         { 
            myBuffer.setColor(Color.GREEN);
            myBuffer.drawString("Triple Apples", 330,570);
         }
         else if(cooldown > 0)
         {
            myBuffer.setColor(Color.GREEN);
            myBuffer.drawString("Super Fruit", 330, 570); 
         }
      } 
   }
  
   private class AnimationListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e) 
      {
         animate();
         time++;
      }  
   }
   
   private class Key extends KeyAdapter
   {
      public void keyPressed(KeyEvent e)
      {
         if(stage == "START")
         {
            if(e.getKeyCode() == KeyEvent.VK_LEFT && color > 0){
               color--;
            }
            if(e.getKeyCode() == KeyEvent.VK_RIGHT && color < 3){
               color++; 
            }
            if(e.getKeyCode() == KeyEvent.VK_UP && hat > 0){
               hat--;
            }
            if(e.getKeyCode() == KeyEvent.VK_DOWN && hat < 3){
               hat++; 
            }
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
               if(fruit < 2) fruit++;
               else fruit = 0; 
            }
         }
         else
         {
            if(stage == "PLAY")
            {
               if(e.getKeyCode() == KeyEvent.VK_DOWN && direction != "VERTICAL" && previousDirection != 0){
                  prePlay = false;
                  direction = "VERTICAL";
                  snake.setDirection(2);
               }
               if(e.getKeyCode() == KeyEvent.VK_LEFT && direction != "HORIZONTAL" && previousDirection != 1){
                  prePlay = false;
                  direction = "HORIZONTAL";   
                  snake.setDirection(3);
               }
               if(e.getKeyCode() == KeyEvent.VK_RIGHT && direction != "HORIZONTAL" && previousDirection != 3){
                  prePlay = false;
                  direction = "HORIZONTAL";
                  snake.setDirection(1);
               }
               if(e.getKeyCode() == KeyEvent.VK_UP && direction != "VERTICAL" && previousDirection != 2){
                  prePlay = false;
                  direction = "VERTICAL";
                  snake.setDirection(0);
               }
            }
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
               newGame();
            }
            if(e.getKeyCode() == KeyEvent.VK_R){
               stage = "START";
            }
         }
         
      }
   }
}

class Snake
{
   private int length; //length starts at 0, so a snake 5 box long has a length of 4
   private int[][] cords = new int[100][3]; //cords[the boxes of the snake starting at 0][x, y, direction(0 = North, 1 = East, 2 = South, 3 = West)]
   
  // constructors
   public Snake(int startX, int startY, String setDirect)
   {
      cords[0][0] = startX;
      cords[0][1] = startY;
     
      if(setDirect == "NORTH") cords[0][2] = 0;
      else if(setDirect == "EAST") cords[0][2] = 1;
      else if(setDirect == "SOUTH") cords[0][2] = 2;
      else if(setDirect == "WEST") cords[0][2] = 3;
     
      length = 0; 
   }
  
   public boolean isClear(int x, int y)
   {
      if(x < 0 || x >= 10 || y < 0 || y >= 10)
      {
         return false; 
      }
      for(int i = 0; i < length; i++)
      {
         if(cords[i][0] == x && cords[i][1] == y)
         {
            return false;
         }
      }
      return true; 
   }
  
   public void setDirection(int setDirect)
   {
      cords[0][2] = setDirect;
   }
   
   public int getDirection()
   {
      return cords[0][2];
   }
  
   public void move(boolean add)
   {
      int storedDirect = cords[0][2];
      int[] addCord = new int[]{cords[length][0], cords[length][1], cords[length][2]};
      for(int i = 0; i < length + 1; i++)
      {
         if(cords[i][2] == 0)
         {
            cords[i][1]--;
         }
         else if(cords[i][2] == 1)
         {
            cords[i][0]++;
         }
         else if(cords[i][2] == 2)
         {
            cords[i][1]++;
         }
         else if(cords[i][2] == 3)
         {
            cords[i][0]--;
         }
       
         int placeHolderDirection = cords[i][2]; 
         cords[i][2] = storedDirect; 
         storedDirect = placeHolderDirection; 
      }
      
      if(add)
      {
         length++;
         for(int i = 0; i < 3; i++)
            cords[length][i] = addCord[i];
      }
      
   }
  
   public int[] getHead()
   {
      return new int[]{cords[0][0], cords[0][1]};
   }
  
   public int[][] getLocation()
   {
      return cords;
   }
   
   public int getLength()
   {
      return length;
   }
  
   public boolean hitWall()
   {
      if(cords[0][0] < 0 || cords[0][0] > 9 || cords[0][1] < 0 || cords[0][1] > 9)
      {
         return true;
      }
      return false; 
   }
  
   public boolean hitItself()
   {
      for(int i = 1; i < length + 1; i++)
      {
         if(cords[0][0] == cords[i][0] && cords[0][1] == cords[i][1])
         {
            return true; 
         }
      }
      return false;
   }
}

class Apple
{
   int x, y;
  
   public Apple(int X, int Y)
   {
      x = X;
      y = Y;
   }
  
   public boolean eaten(int snakeX, int snakeY)
   {
      if(snakeX == x && snakeY == y)
      {
         return true;
      }
      return false;
   }
  
   public int getX()
   {
      return x;
   }
  
   public int getY()
   {
      return y;
   } 	
}