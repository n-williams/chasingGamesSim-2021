package group;

import drawing.Canvas;

//more advanced class inherited from the player class, includes methods
//to get and set angle and speed, as well as updating each player after 
//a variable changes
public class DynamicPlayer extends Player {
	public static double SPEED = 50;
	public static double deltaTime = 0.2;
	public static double MAX_FOV = 80;
	private int counter;
	private double turnAng;
	
	//moves the player into a starting position other than 0,0
	public DynamicPlayer(Canvas canvas, double size, double xPosition, double yPosition, double initialAngle, int initialCounter, boolean tagState) {
		super(canvas);
		this.size = size;
		move(xPosition);
		turn(90);
		move(yPosition);
		turn(-90);
		turn(initialAngle);
		draw();
		this.chasing = tagState;
	}
	
	//update method to change angle of movement of the player and
	//amount of steps it should take given the refresh rate
	public void update() {
		
		
		if (counter == 0) {
			counter = (int)(Math.random() * 20);
			//angular velocity is limited to a set FOV in front of the player,
			//in both positive and negative directions
			turnAng = (-(MAX_FOV/2) + (Math.random()*MAX_FOV));
		}
		else {
			if(this.stuck == false) {
				//allows for a gradual turn rather than snapping in altering 
				//directions
				double theta = turnAng * deltaTime;
				turn(theta);
				
				double dist = SPEED * deltaTime;
				move(dist);
				counter--;
			}	
		}
	}
}
