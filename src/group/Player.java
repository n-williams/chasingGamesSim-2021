package group;

import drawing.Canvas;
import geometry.CartesianCoordinate;

//basic class of player, contains the basic methods including movement,
//collision, and drawing
public class Player {
	private Canvas myCanvas;

	protected CartesianCoordinate pos;
	private double angle;
	public double size;
	public boolean chasing;
	public boolean stuck;
	private boolean penDown;
	
	//gives current x position of the player
	public double getPositionX() {
		return pos.getX();
	}
	
	//gives current y position of the player
	public double getPositionY() {
		return pos.getY();
	}
	
	public Player(Canvas myCanvas) {
		this.myCanvas = myCanvas;
		this.pos = new CartesianCoordinate(0, 0);
		this.setAngle(0);
		this.chasing = false;
		this.stuck = false;
		this.penDown = false;
	}

	public void move(double i) {
		double x = pos.getX();
		double y = pos.getY();
		CartesianCoordinate oldPos = new CartesianCoordinate(pos.getX(), pos.getY());
		x += i * Math.cos(Math.toRadians(getAngle()));
		y += i * Math.sin(Math.toRadians(getAngle()));
		pos = new CartesianCoordinate(x, y);
		if (this.penDown == true) {
			myCanvas.drawLineBetweenPoints(oldPos, pos);
		}
		
	}

	public void turn(double theta) {
		setAngle(getAngle() + theta);
	}
	
	//method to detect collision to the edges of the screen, as well as the obstacle
	//the player has been programmed to 'bounce' off at the same angle it came in at
	public void edgeCollision(double maxX, double maxY) {
		if (pos.getX() - size <= 0) {
			this.setAngle(0);
		}
		if (pos.getX() + size >= maxX) {
			this.setAngle(180);
		}
		if (pos.getY() - size <= 0) {
			this.setAngle(90);
		}
		if (pos.getY() + size >= maxY) {
			this.setAngle(270);
		}
	}
	
	//draws the player
	public void draw() {
		this.turn(-90);
		this.move(size/2);
		this.turn(90);
		this.putPenDown();
		for (int i = 0; i <= 360; i++) {
			if(chasing == true) {
				this.turn(90);
				this.move(size);
				this.putPenUp();	
				this.move(-size);
				this.turn(-90);
				this.putPenDown();
			}
			this.move((size/2) * Math.toRadians(1));
			this.turn(1);
			//Utils.pause(1);
			
		}
		if(stuck == true) {
			this.turn(90);
			this.move(size);
			this.putPenUp();	
			this.move(-size/2);
			this.turn(-90);
			this.move(size/2);
			this.putPenDown();
			this.move(-size);
			this.putPenUp();
			this.move(size/2);
			this.turn(-90);
			this.move(size/2);
			this.turn(90);
			this.putPenDown();
		}
		this.putPenUp();
		this.turn(90);
		this.move(size/2);
		this.turn(-90);
		
		
	}
	
	
	public void putPenUp() {
		this.penDown = false;
	}

	public void putPenDown() {
		this.penDown = true;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

}
