package group;

import java.util.ArrayList;
import java.util.List;

import drawing.Canvas;
import geometry.LineSegment;
import tools.Utils;

//flock class to add flocking functionality
public class Group {
	//creating an array list to contain each player in the flock 
	public List<DynamicPlayer> players = new ArrayList<DynamicPlayer>();
	public static double impactParameter = 1.5;
	public int chasingCount;
	public int runningCount;
	public int stuckCount;

	//add player to the list with position x,y
	public void addRunner(Canvas canvas, double size, double xPos, double yPos) {
		players.add(new DynamicPlayer(canvas, size, xPos, yPos, (Math.random() * 360), (int)(Math.random() * 20), false));
	}
	
	public void addChaser(Canvas canvas, double size, double xPos, double yPos) {
		players.add(new DynamicPlayer(canvas, size, xPos, yPos, (Math.random() * 360), (int)(Math.random() * 20), true));
	}
	
	public void clearPlayers() {
		players.clear();
	}
	
	public double getDeltaTime() {
		return DynamicPlayer.deltaTime;
	}
	
	public double getSPEED() {
		return DynamicPlayer.SPEED;
	}
	
	public double getImpactParameter() {
		return impactParameter;
	}
	
	public void playerCollidedSIM(DynamicPlayer player1) {
		for (DynamicPlayer player2 : players) {
			if (player1 == player2) continue;
			LineSegment dist = new LineSegment(player1.pos, player2.pos);
			if (dist.length() <= player1.size * impactParameter) {
				if(player1.chasing == true || player2.chasing == true) {
					if(player1.chasing == true && player2.chasing == true) continue;
					else {
						if(player1.chasing == true) {
							player2.stuck = true;
						}
						if(player2.chasing == true) {
							player1.stuck = true;
						}
					}
				}
				if(player1.stuck == true || player2.stuck == true) {
					if(player1.stuck == true && player2.chasing == false) {
						player1.stuck = false;
					}
					if(player1.chasing == false && player2.stuck == true) {
						player2.stuck = false;
					}
				}
				
				double player1Ang = player1.getAngle();
				double player2Ang = player2.getAngle();
				player1.setAngle(player2Ang);
				player2.setAngle(player1Ang);
			}
		}
	}
	
	public void playerCollidedCT(DynamicPlayer player1) {
		for (DynamicPlayer player2 : players) {
			if (player1 == player2) continue;
			LineSegment dist = new LineSegment(player1.pos, player2.pos);
			
			if (dist.length() <= (player1.size * impactParameter)) {
				if(player1.chasing == true || player2.chasing == true) {
					if(player1.chasing == true && player2.chasing == true) {
						continue;
					}
					else {
						player1.chasing = true;
						player2.chasing = true;
					}
					
				}
				
				double player1Ang = player1.getAngle();
				double player2Ang = player2.getAngle();
				player1.setAngle(player2Ang);
				player2.setAngle(player1Ang);
			}
			
		}
	}
	
	//simulate method that is called in the game loop
	public void Simulate(Canvas canvas, int gamemode) {
		switch(gamemode) {
		case 1:
			chasingCount = 0;
			runningCount = 0;
			
			canvas.clear();
			for (DynamicPlayer player : players) {
				if(player.chasing == true) {
					chasingCount++;
				}
				else {
					runningCount++;
				}
				playerCollidedCT(player);
				player.edgeCollision(canvas.getWidth(), canvas.getHeight());
				player.update();
				player.draw();
			}
			break;
		
		case 2:
			chasingCount = 0;
			runningCount = 0;
			stuckCount = 0;
			
			canvas.clear();
			for (DynamicPlayer player : players) {
				if(player.chasing == true) {
					chasingCount++;
				}
				else if(player.stuck == true){
					stuckCount++;
				}
				else {
					runningCount++;
				}
				playerCollidedSIM(player);
				player.edgeCollision(canvas.getWidth(), canvas.getHeight());
				player.update();
				player.draw();
			}
			break;
			
		default:
			System.out.println("ERROR");
			System.exit(0);	
		}
		
		Utils.pause(20);
	}
}
