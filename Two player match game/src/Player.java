import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Player {

	//FIELDS
	private int x;
	private int y;
	private int width;
	private int height;
	
	private int dx;
	private int speed;
	
	private boolean left;
	private boolean right;
	
	private boolean createBall;	
	private int score;
	
	//CONSTRUCTOR
	public Player(int whichPlayer) {
		
		if(whichPlayer == 1) {
			x = GamePanel.WIDTH / 2 - 75;
			y = GamePanel.HEIGHT - 50;
		}
		else if(whichPlayer == 2) {
			x = GamePanel.WIDTH / 2 - 75;
			y = 20;
		}
		
		width = 150;
		height = 30;
			
		dx = 0;
		speed = 15;
		
		createBall = false;		
		score = 0;
	}
	
	//FUNCTIONS
	public void update() {
		
		if(left) {
			dx = -speed;
		}
		if(right) {
			dx = speed;
		}
		
		x += dx;
		
		if(x < 0) x = 0;
		if(x > GamePanel.WIDTH - width) x = GamePanel.WIDTH - width;
		
		dx = 0;	
		
		if(createBall && GamePanel.balls.size() < 1) {
			GamePanel.balls.add(new Balls());			
		}	
	}
	
	public int getx() {
		return x;
	}
	public int gety() {
		return y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getScore() {
		return score;
	}
	
	public void setLeft(boolean left) {
		this.left = left;
	}
	public void setRight(boolean right) {
		this.right = right;
	}
	public void setCreateBall(boolean createBall) {
		this.createBall = createBall;
	}
	
	public void increaseScore() {
		score++;
	}
	
	public void draw(Graphics2D g) {
				
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
		
		g.setStroke(new BasicStroke(3));
			
		g.setColor(Color.WHITE.darker());
			
		g.drawRect(x, y, width, height);
		g.setStroke(new BasicStroke(1));		
							
	}
}