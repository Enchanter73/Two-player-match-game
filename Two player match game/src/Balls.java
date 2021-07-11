import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Balls {
	
	//FIELDS
	private double x;
	private double y;
	private int r;
		
	private double dx;
	private double dy;
	private double angle;
	private double rad;
	private int speed;
		
	//CONSTRUCTOR
	public Balls() {
		
		speed = 12;
		r = 10;
		
		x = GamePanel.HEIGHT / 2;
		y = 80;
		
		angle = Math.random() * 70 + 55;
		while(angle > 80 && angle < 100) {
			angle = Math.random() * 70 + 55;
		}		
		rad = Math.toRadians(angle);
		
		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;	
	}
	
	public double getx() {
		return x;
	}
	public double gety() {
		return y;
	}
	public int getr() {
		return r;
	}
	public int getSpeed() {
		return speed;
	}
	public double getDY() {
		return dy;
	}
	
	public void changeWay() {
		dy = -dy;
	}
	
	public void increaseSpeed() {
		if(dx < 0) dx += dx/10;
		if(dx > 0) dx += dx/10;
		if(dy < 0) dy += dy/10;
		if(dy > 0) dy += dy/10;
	}
	
	public boolean update() {
		
		x += dx;
		y += dy;
		
		if(x < r && dx < 0) dx = -dx;
		if(y < 0 && dy < 0) {
			GamePanel.player1.increaseScore();
			dy = -dy;
			return true;
		}
		if(x > GamePanel.WIDTH - r && dx > 0) dx = -dx;
		if(y > GamePanel.HEIGHT && dy > 0) {
			GamePanel.player2.increaseScore();
			dy = -dy;
			return true;
		}
		return false;
	}
	
	public void draw(Graphics2D g) {		
		g.setColor(Color.WHITE);
		g.fillOval((int)(x-r), (int)(y-r), 2*r, 2*r);
		
		g.setStroke(new BasicStroke(3));
		
		g.setColor(Color.WHITE.darker());
		g.drawOval((int)(x-r), (int)(y-r), 2*r, 2*r);
		
		g.setStroke(new BasicStroke(1));
	}
}
