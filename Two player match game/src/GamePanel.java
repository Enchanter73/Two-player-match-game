
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements KeyListener, Runnable{

	//FIELDS
	public static int WIDTH = 800;
	public static int HEIGHT = 800;
	
	private Thread thread;
	private boolean running;
	
	private BufferedImage image;
	private Graphics2D g;
	
	private int FPS = 30;
	private double averageFPS;
	
	public static Player player1;
	public static Player player2;
	public static ArrayList<Balls> balls;
	
	private long increaseStartTimer;
	private long increaseStartTimerDiff;
	private int increaseDelay = 2000;
	private int increasedSpeedCounter = 0;
	
	private boolean onTheSameLine1 = false;
	private boolean onTheSameLine2 = false;
				
	//CONSTRUCTOR
		public GamePanel() {		
			super();
			setPreferredSize(new Dimension(WIDTH, HEIGHT));
			setFocusable(true);
			requestFocus();		
		}
	
	//FUNCTIONS
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
		addKeyListener(this);
	}
	
	public void run() {
		
		running = true;
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		player1 = new Player(1);
		player2 = new Player(2);
		balls = new ArrayList<Balls>();
		
		long startTime;
		long URDTimeMillis;
		long waitTime;
		long totalTime = 0;
		long targetTime = 1000 / FPS;
		
		int frameCount = 0;
		int maxFrameCount = 30;
			
		//GAME LOOP
		while(running) {			
			startTime = System.nanoTime();
			
			gameUpdate();
			gameRender();
			gameDraw();
			
			URDTimeMillis = (System.nanoTime() - startTime) / 1000000;
			waitTime = targetTime - URDTimeMillis;
			
			try {
				Thread.sleep(waitTime);
			}
			catch(Exception e) {			
			}
			
			totalTime += System.nanoTime() - startTime;
			frameCount++;
			
			if(frameCount == maxFrameCount) {
				averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
				frameCount = 0;
				totalTime = 0;
			}		
		}
	}
	
	private void gameDraw() {
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}
	
	private void gameRender() {
		//draw background
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//draw players
		player1.draw(g);
		player2.draw(g);
		
		//draw Balls
		for(int i=0; i<balls.size(); i++) {
			balls.get(i).draw(g);
		}
		
		//draw Score
		g.setFont(new Font("Century Gothic", Font.PLAIN, 60));
		String p1 = "" + player1.getScore();
		String p2 = "" + player2.getScore();
		
		g.setColor(Color.WHITE);
		if(player2.getScore() < 10) g.drawString(p2, WIDTH - 50, HEIGHT / 2 - 50);
		else g.drawString(p2, WIDTH - 75, HEIGHT / 2 - 50);
		
		if(player1.getScore() < 10) g.drawString(p1, WIDTH - 50, HEIGHT / 2 + 100);
		else g.drawString(p1, WIDTH - 75, HEIGHT / 2 +100);
		
		g.fillRect(WIDTH - 80, HEIGHT / 2, 80, 1);
		
		//draw info
		g.setFont(new Font("Century Gothic", Font.PLAIN, 40));
		if(balls.size() == 0) {
			String s = "Press space to get a ball";
			int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.setColor(Color.RED.brighter());
			g.drawString(s, WIDTH / 2 - length / 2, HEIGHT / 2);
		}
		if(player1.getScore() == 0 && player2.getScore() == 0 && balls.size() == 0) {
			String s1 = "Use A - D to move";
			String s2 = "Use left - right arrow keys to move";
			int length1 = (int) g.getFontMetrics().getStringBounds(s1, g).getWidth();
			int length2 = (int) g.getFontMetrics().getStringBounds(s2, g).getWidth();
			g.setColor(Color.RED.brighter());
			g.drawString(s1, WIDTH / 2 - length1 / 2, 100);
			g.drawString(s2, WIDTH / 2 - length2 / 2, HEIGHT - 80);
		}
	}
		
	private void gameUpdate() {
		//player updates
		player1.update();
		player2.update();
		
		//ball updates
		for(int i=0; i<balls.size(); i++) {
			boolean remove = balls.get(i).update();
			if(remove) {
				balls.remove(i);
				i--;
			}
		}
		
		//player - ball collision
		for(int i=0; i<balls.size(); i++) {
			if(balls.get(i).getx() > player1.getx() && balls.get(i).getx() < player1.getx() + player1.getWidth()) {
				onTheSameLine1 = true;
			}
			if(balls.get(i).getx() > player2.getx() && balls.get(i).getx() < player2.getx() + player2.getWidth()) {
				onTheSameLine2 = true;
			}
			if(new Rectangle(player1.getx(), player1.gety(), player1.getWidth(), player1.getHeight()).intersects(new Rectangle((int)balls.get(i).getx() - balls.get(i).getr(), (int)balls.get(i).gety() - balls.get(i).getr(), balls.get(i).getr() * 2, balls.get(i).getr() * 2))) {
				if(onTheSameLine1) {
					balls.get(i).changeWay();
				}		
			}
			if(new Rectangle(player2.getx(), player2.gety(), player2.getWidth(), player2.getHeight()).intersects(new Rectangle((int)balls.get(i).getx() - balls.get(i).getr(), (int)balls.get(i).gety() - balls.get(i).getr(), balls.get(i).getr() * 2, balls.get(i).getr() * 2))) {
				if(onTheSameLine2) {
					balls.get(i).changeWay();
				}
			}				
		}
		
		//increase in ball speed
		if(increaseStartTimer == 0 && balls.size() > 0) {
			increaseStartTimer = System.nanoTime();
		}
		else {
			increaseStartTimerDiff = (System.nanoTime() - increaseStartTimer) / 1000000;
			if(increaseStartTimerDiff > increaseDelay && increasedSpeedCounter < 30) {
				for(int i=0; i<balls.size(); i++) {
					balls.get(i).increaseSpeed();
					increasedSpeedCounter++;
				}				
				increaseStartTimer = 0;
				increaseStartTimerDiff = 0;
			}
		}
		
	}
	
	public void keyPressed(KeyEvent key) {
			
		int keyCode = key.getKeyCode();
		if(keyCode == KeyEvent.VK_A) {
			player2.setLeft(true);
		}
		if(keyCode == KeyEvent.VK_D) {
			player2.setRight(true);
		}
		if(keyCode == KeyEvent.VK_LEFT) {
			player1.setLeft(true);
		}
		if(keyCode == KeyEvent.VK_RIGHT) {
			player1.setRight(true);
		}
		if(keyCode == KeyEvent.VK_SPACE) {
			player1.setCreateBall(true);
		}
	}
	
	public void keyReleased(KeyEvent key) {
		int keyCode = key.getKeyCode();
		if(keyCode == KeyEvent.VK_A) {
			player2.setLeft(false);
		}
		if(keyCode == KeyEvent.VK_D) {
			player2.setRight(false);
		}
		if(keyCode == KeyEvent.VK_LEFT) {
			player1.setLeft(false);
		}
		if(keyCode == KeyEvent.VK_RIGHT) {
			player1.setRight(false);
		}
		if(keyCode == KeyEvent.VK_SPACE) {
			player1.setCreateBall(false);
		}
	}
	public void keyTyped(KeyEvent arg0) {}
}
