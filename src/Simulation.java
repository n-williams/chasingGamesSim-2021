import javax.swing.*;

import drawing.Canvas;
import group.DynamicPlayer;
import group.Group;
import tools.Utils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Simulation {
	
	//objects
	private JFrame frame;
	private Canvas canvas;
	
	//instantiating
	Group group = new Group();
	
	private final static int WINDOW_SIZE = 1000;
	private final static int GAMEMODE = 1;
	private final static int NO_OF_RUNS = 1;
	private final static int RUNTIME = 1500;
	private final static double playerSize = (double)(10);
	
	private final int chaserCount = 5;
	private final int runnerCount = 15;

	private boolean continueRunning;
	private int runtimeCounter;
	private int runNumber;
	
	private double nuModel;
	
	private float[][] chaserArray = new float[RUNTIME][NO_OF_RUNS];
	private float[] meanC = new float[RUNTIME];
	private double[] standDevC = new double[RUNTIME];
	private double[] gradientC = new double[RUNTIME];
	private double[] nuC = new double[RUNTIME];
	
	private float[][] runnerArray = new float[RUNTIME][NO_OF_RUNS];
	private float[] meanR = new float[RUNTIME];
	private double[] standDevR = new double[RUNTIME];
	private double[] gradientR = new double[RUNTIME];
	private double[] nuR = new double[RUNTIME];
	
	private float[][] stuckArray = new float[RUNTIME][NO_OF_RUNS];
	private float[] meanS = new float[RUNTIME];
	private double[] standDevS = new double[RUNTIME];
	private double[] gradientS = new double[RUNTIME];
	
	
	@SuppressWarnings("unused")
	public Simulation() {
		runNumber = 0;
		
		setupGUI();
		
		for(int i = 0; i < NO_OF_RUNS; i++) {
			group.clearPlayers();
			for(int j = 0; j < chaserCount; j++) {
				group.addChaser(canvas, playerSize, playerSize + (Math.random() * (WINDOW_SIZE - (playerSize * 2))), playerSize + (Math.random() * (WINDOW_SIZE - (playerSize * 2))));
			}
			for(int j = 0; j < runnerCount; j++) {
				group.addRunner(canvas, playerSize, playerSize + (Math.random() * (WINDOW_SIZE - (playerSize * 2))), playerSize + (Math.random() * (WINDOW_SIZE - (playerSize * 2))));
			}
			gameLoop(runNumber);
			runNumber++;
			System.out.println(runNumber);
		}
		
		nuModel = (2 * (group.getImpactParameter() * playerSize) * group.getSPEED() * (chaserCount + runnerCount)) / (WINDOW_SIZE * WINDOW_SIZE);
		
		for(int i = 0; i < RUNTIME; i++) {
			double squareDiffC = 0;
			double squareDiffR = 0;
			double squareDiffS = 0;
			double varianceC = 0;
			double varianceR = 0;
			double varianceS = 0;
			float time = (float)(group.getDeltaTime() * i);
			
			for(int j = 0; j < NO_OF_RUNS; j++) {
				chaserArray[i][j] = chaserArray[i][j] / group.players.size();
				runnerArray[i][j] = runnerArray[i][j] / group.players.size();
				
				meanC[i] = meanC[i] + chaserArray[i][j];
				meanR[i] = meanR[i] + runnerArray[i][j];
				
				if(GAMEMODE == 2) {
					stuckArray[i][j] = stuckArray[i][j] / group.players.size();
					meanS[i] = meanS[i] + stuckArray[i][j];
				}
			}
			
			meanC[i] = meanC[i] / NO_OF_RUNS;
			meanR[i] = meanR[i] / NO_OF_RUNS;
			
			if(GAMEMODE == 2) {
				meanS[i] = meanS[i] / NO_OF_RUNS;
			}
			
			for(int j = 0; j < NO_OF_RUNS; j++) {
				squareDiffC = Math.pow((chaserArray[i][j] - meanC[i]), 2);
				squareDiffR = Math.pow((runnerArray[i][j] - meanR[i]), 2);
				varianceC = varianceC + squareDiffC;
				varianceR = varianceR + squareDiffR;
				
				if(GAMEMODE == 2) {
					squareDiffS = Math.pow((stuckArray[i][j] - meanS[i]), 2);
					varianceS = varianceS + squareDiffS;
				}
			}
			
			varianceC = varianceC / NO_OF_RUNS;
			varianceR = varianceR / NO_OF_RUNS;
			
			standDevC[i] = Math.sqrt(varianceC);
			standDevR[i] = Math.sqrt(varianceR);
			
			if(GAMEMODE == 2) {
				varianceS = varianceS / NO_OF_RUNS;
				standDevS[i] = Math.sqrt(varianceS);
			}
			
		}
		
		for(int i = 0; i < RUNTIME; i++) {
			float time = (float)(group.getDeltaTime() * i);
			
			if(i == 0 || i == (RUNTIME-1)) {
				gradientC[i] = 0;
				gradientR[i] = 0;
				if(GAMEMODE == 2) {
					gradientS[i] = 0;
				}
			}
			else {
				//LOOK HERE LATER, CANT CALCULATE i+1
				//gradientC[i] = (meanC[i+1] - meanC[i - 1]) / (2 * 0.2);
				gradientC[i] = (meanC[i + 1] - meanC[i - 1]) / (2 * 0.2);
				gradientR[i] = (meanR[i + 1] - meanR[i - 1]) / (2 * 0.2);
				if(GAMEMODE == 2) {
					gradientS[i] = (meanS[i + 1] - meanS[i - 1]) / (2 * 0.2);
				}
			}
			
			if(meanC[i] > 0.2 && meanC[i] < 0.8) {
				nuC[i] = gradientC[i] / (meanC[i] * (1 - meanC[i]));
				nuR[i] = gradientR[i] / (meanR[i] * (1 - meanR[i]));
				
				try {
					FileWriter myWriter = new FileWriter("C:\\Users\\noah\\Desktop\\University\\proj\\Simulation\\nu.txt", true);
					myWriter.write(time + "\t" + nuC[i] + "\t" + nuR[i] + "\t" + nuModel + "\t" + -nuModel + "\n");
					myWriter.close();
					System.out.println("Successfully wrote to the file.");
				} catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
			}
			
			
			try {
				FileWriter myWriter = new FileWriter("C:\\Users\\noah\\Desktop\\University\\proj\\Simulation\\chasers.txt", true);
				myWriter.write(time + "");
				for(int j = 0; j < NO_OF_RUNS; j++) {
					myWriter.write("\t" + chaserArray[i][j]);
				}
				myWriter.write("\n");
				myWriter.close();
				System.out.println("Successfully wrote to the file.");
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
			
			try {
				FileWriter myWriter = new FileWriter("C:\\Users\\noah\\Desktop\\University\\proj\\Simulation\\resultsC.txt", true);
				myWriter.write(time + "\t" + meanC[i] + "\t" + standDevC[i] + "\t" + gradientC[i] + "\n");
				myWriter.close();
				System.out.println("Successfully wrote to the file.");
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
			
			try {
				FileWriter myWriter = new FileWriter("C:\\Users\\noah\\Desktop\\University\\proj\\Simulation\\runners.txt", true);
				myWriter.write(time + "");
				for(int j = 0; j < NO_OF_RUNS; j++) {
					myWriter.write("\t" + runnerArray[i][j]);
				}
				myWriter.write("\n");
				myWriter.close();
				System.out.println("Successfully wrote to the file.");
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
			
			try {
				FileWriter myWriter = new FileWriter("C:\\Users\\noah\\Desktop\\University\\proj\\Simulation\\resultsR.txt", true);
				myWriter.write(time + "\t" + meanR[i] + "\t" + standDevR[i] + "\t" + gradientR[i] + "\n");
				myWriter.close();
				System.out.println("Successfully wrote to the file.");
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
			
			if(GAMEMODE == 2) {
				try {
					FileWriter myWriter = new FileWriter("C:\\Users\\noah\\Desktop\\University\\proj\\Simulation\\stuck.txt", true);
					myWriter.write(time + "");
					for(int j = 0; j < NO_OF_RUNS; j++) {
						myWriter.write("\t" + stuckArray[i][j]);
					}
					myWriter.write("\n");
					myWriter.close();
					System.out.println("Successfully wrote to the file.");
				} catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
				
				try {
					FileWriter myWriter = new FileWriter("C:\\Users\\noah\\Desktop\\University\\proj\\Simulation\\resultsS.txt", true);
					myWriter.write(time + "\t" + meanS[i] + "\t" + standDevS[i] + "\t" + gradientS[i] + "\n");
					myWriter.close();
					System.out.println("Successfully wrote to the file.");
				} catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
			}
		}
	}
	
	//method to set the GUI to my desired specifications
	private void setupGUI() {
		
		frame = new JFrame();
		frame.setTitle("Simulator");
		frame.setSize(WINDOW_SIZE + 16, WINDOW_SIZE + 39);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		
		canvas = new Canvas();
		
		frame.add(canvas);
	}
	
	//the main game loop, will loop until 'continueRunning' is False
	@SuppressWarnings("unused")
	private void gameLoop(int runNumber) {
		continueRunning = true;
		runtimeCounter = 0;
		
		while (continueRunning) {
			
			group.Simulate(canvas, GAMEMODE);
			chaserArray[runtimeCounter][runNumber] = group.chasingCount;
			runnerArray[runtimeCounter][runNumber] = group.runningCount;
			if(GAMEMODE == 2) {
				stuckArray[runtimeCounter][runNumber] = group.stuckCount;
			}
			runtimeCounter++;
			
			if(runtimeCounter >= RUNTIME) {
				continueRunning = false;
				Utils.pause(500);
			}
		}
	}
	
	//main function
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		System.out.println();
		File arrayC = new File("C:\\Users\\noah\\Desktop\\University\\proj\\Simulation\\chasers.txt");
		arrayC.delete();
		arrayC.createNewFile();
		
		File resultsC = new File("C:\\Users\\noah\\Desktop\\University\\proj\\Simulation\\resultsC.txt");
		resultsC.delete();
		resultsC.createNewFile();
		
		File arrayR = new File("C:\\Users\\noah\\Desktop\\University\\proj\\Simulation\\runners.txt");
		arrayR.delete();
		arrayR.createNewFile();
		
		File resultsR = new File("C:\\Users\\noah\\Desktop\\University\\proj\\Simulation\\resultsR.txt");
		resultsR.delete();
		resultsR.createNewFile();
		
		File nu = new File("C:\\Users\\noah\\Desktop\\University\\proj\\Simulation\\nu.txt");
		nu.delete();
		nu.createNewFile();
		
		if(GAMEMODE == 2) {
			File arrayS = new File("C:\\Users\\noah\\Desktop\\University\\proj\\Simulation\\stuck.txt");
			arrayS.delete();
			arrayS.createNewFile();
			
			File resultsS = new File("C:\\Users\\noah\\Desktop\\University\\proj\\Simulation\\resultsS.txt");
			resultsS.delete();
			resultsS.createNewFile();
		}
		
		new Simulation();
		
	}

}
