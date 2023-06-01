import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	
	//variables needed for game
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 100;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	int hScore = 0;
	Timer timer;
	Random random;
	
	//sets background and starts recording inputs
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	//starts the timer, starts running game, and spawns in first apple
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	//draws the apple, snake, and grid pattern
	public void draw(Graphics g) {
		if(running) {
			//grid
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
				
				}
			//apple
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			//snake
			for (int i = 0; i < bodyParts; i++){
				if( i == 0) {
					g.setColor(new Color(102, 51, 0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					
				}
				else if(i%2 == 0){
					g.setColor(new Color(23, 125, 3));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(153, 153, 0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 20));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}
	
	//randomizes the location of new apples
	public void newApple(){
		
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	
	//controls movement of snake
	public void move() {
		//moves body parts to match movement of head
		for(int i = bodyParts; i>0; i--){
			x[i] = x[i-1];
			y[i] = y[i-1];
			
		}
		//controls which direction head will go based on key presses
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		}
	}
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
			
		}
	}
	
	//checks for collisons that would cause game to end
	public void checkCollisions() {
		//checks for collision w/body
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				running = false;
			}
		}
		//checks for collisions w/walls
		if(x[0] < 0) {
			running = false;
		}
		if(x[0] > SCREEN_WIDTH) {
					running = false;
		}
		if(y[0] < 0) {
					running = false;
		}
		if(y[0] > SCREEN_HEIGHT) {
					running = false;
		}
		//stops timer if game isn't running
		if(!running) {
			
		}
	}
	public void gameOver(Graphics g) {
		//Game Over Text
		if (applesEaten >= hScore){
		    hScore = applesEaten;
		}
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 20));
		FontMetrics metric = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metric.stringWidth("Score: " + applesEaten))/2, SCREEN_HEIGHT/2 + 60);
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 20));
		FontMetrics metri = getFontMetrics(g.getFont());
		g.drawString("PRESS SPACE TO RESTART", (SCREEN_WIDTH - metri.stringWidth("PRESS SPACE TO RESTART"))/2, SCREEN_HEIGHT/2 + 180);
		g.drawString("HIGH SCORE: " + hScore, (SCREEN_WIDTH - metri.stringWidth("HIGH SCORE: " + hScore))/2, SCREEN_HEIGHT/2 + 120);
	}
	
	//runs actions declared in previous methods
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			checkCollisions();
			//checks if a apple spawned in the body of the snake and respawns it if it did
			for(int i = bodyParts;i>0;i--) {
				if((appleX == x[i])&& (appleY == y[i])) {
					newApple();
				}
			}
		}
		repaint();
	}

	//detects key inputs
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e){
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			case KeyEvent.VK_SPACE:
				if(!running) {
					x[0] = 0;
					y[0] = 0;
					for(int i = bodyParts; i>0; i--){
						x[i] = x[0]-i*UNIT_SIZE;
						y[i] = y[0];
						
					}
					applesEaten = 0;
					bodyParts = 6;
					direction = 'R';
					newApple();
					running = true;
					
					
					
				}
				
			}
		}
	}
}
