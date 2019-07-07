/*
 * File: Breakout.java

 * -------------------
 * Name:Egemen Guray
 * Section Leader: Sherman Leung
 * 
 * This file will eventually implement the game of Breakout.
 */
 
import acm.graphics.*;

import acm.program.*;
import acm.util.*;
 
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
 
public class Breakout extends GraphicsProgram {
 
/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;
 
/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;
 
/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;
 
/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;
 
/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;
 
/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;
 
/** Separation between bricks */
	private static final int BRICK_SEP = 4;
 
/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;
 
/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;
 
/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;
 
/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 30;
 
/** Number of turns */
	private static final int NTURNS = 3;
	
/**Necessary variables for ball`s horizontal and vertical velocity*/	
	private double vx, vy;
	
/**Random number generator for vx*/	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	

	
/** Animation delay or paust time between ball moves */	
	private static final int DELAY =10;
 


	public void run(){
		for(int i=0; i < NTURNS; i++){
			loadItems();
			playGame();
			if(brickNumber == 0){
				ball.setVisible(false);
				break;
			}
			if(brickNumber > 0){
				removeAll();
			}
		}
	}
	/*
	 * Loads the items in order to start the game
	 */
	
	private void loadItems(){
		loadBricks((getWidth()/2.0), BRICK_Y_OFFSET);
		loadPaddle();
		loadBall();
	}
	
	/*
	 * Generates the bricks
	 */
	private GRect brick;
	
	private void loadBricks(double x, double y){	
		for(int i = 0; i < NBRICK_ROWS; i++){
			for(int j = 0; j < NBRICKS_PER_ROW; j++){
				double	brx = (x -((BRICK_WIDTH*NBRICKS_PER_ROW)/2.0) - (((NBRICKS_PER_ROW-1)*BRICK_SEP)/2.0) + ((j*BRICK_SEP)+(j*BRICK_WIDTH)));
				double	bry = (y + ((i*BRICK_HEIGHT) + (i*BRICK_SEP)));
				brick = new GRect( brx , bry , BRICK_WIDTH , BRICK_HEIGHT );
				add (brick);
				brick.setFilled(true);				
				if((i == 0) || (i == 1)){
					brick.setColor(Color.RED);
				}
				if((i == 2) || (i == 3)){
					brick.setColor(Color.ORANGE);
				}
				if((i == 4) || (i == 5)){
					brick.setColor(Color.YELLOW);
				}
				if((i == 6) || (i == 7)){
					brick.setColor(Color.GREEN);
				}
				if((i == 8) || (i == 9)){
					brick.setColor(Color.CYAN);
				}
			}
		}
	}
	
	/*
	 * Generates the paddle
	 */
	
	
	private GRect paddle;
	private void loadPaddle(){
		double px = ((getWidth()/2.0) - (PADDLE_WIDTH/2.0)); 
		double py = (getHeight() - PADDLE_HEIGHT - PADDLE_Y_OFFSET);
		paddle = new GRect (px, py, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setColor(Color.BLACK);
		add (paddle);
		addMouseListeners();
	}
	
	/*
	 * Paddle follows the cursor
	 */
	
	public void mouseMoved(MouseEvent followcursor){
		if((followcursor.getX() > (PADDLE_WIDTH/2.0)) && (followcursor.getX()<(getWidth() - (PADDLE_WIDTH/2.0)))) {
			double npx =(followcursor.getX() - (PADDLE_WIDTH/2.0));
			double npy = (getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
			paddle.setLocation(npx,npy);
		}
		
	}
	
	/*
	 * Generates the ball
	 */
	private GOval ball;
	private void loadBall(){
		double blx = ((getWidth()/2.0) - BALL_RADIUS);
		double bly = ((getHeight()/2.0) - BALL_RADIUS);
		ball = new GOval(blx, bly, BALL_RADIUS, BALL_RADIUS);
		ball.setFilled(true);
		ball.setColor(Color.BLACK);
		add(ball);
	}
	/*
	 * Loads the necessary rules of the game and with mouse click game starts
	 * if ball goes to the bottom or all bricks disappear game ends 
	 */
	
	private void playGame(){
		waitForClick();
		randomVelocity();
		while(true){
			moveBall();
			physicsRules();
			if(ball.getY()>getHeight()){
				break;
			}
			if(brickNumber == 0){
				break;
			}
		}
	}
	/*
	 * Sets the velocity of ball
	 */
	
	
	private void randomVelocity(){
		vx = rgen.nextDouble(1.0, 3.0);
		if(rgen.nextBoolean(0.5)){
			vx = -vx; 
		}
		vy = (4.0);
	}
	
	/*
	 * Moves the ball
	 */
	
	private void moveBall(){
		ball.move(vx, vy);
		pause(DELAY);
	}
	
	/*
	 * Physics rules for the collisions
	 * and removes the brick then refreshes the number of bricks remaining
	 */
	private void physicsRules(){
		if((((ball.getX()-vx)<=0) && (vx<0)) || (((ball.getX()+vx)>=(getWidth()-(BALL_RADIUS))) && (vx>0))) {
			vx = -vx;
		}
		if((((ball.getY()-vy)<=0) && (vy<0))){
			vy = -vy;
		}
		GObject collider = getCollidingObject();
		if(collider == paddle){			
			if((ball.getY()>=(getHeight()-PADDLE_Y_OFFSET-PADDLE_HEIGHT-BALL_RADIUS*2.0)) && (ball.getY()<(getHeight()-PADDLE_Y_OFFSET-PADDLE_HEIGHT-BALL_RADIUS*2.0+0.1))) {
				vy = -vy;	
			}
		}else{
			if(collider != null){		
			remove(collider); 
			brickNumber--;
			vy = -vy;
		}
		}
	}
	/*
	 * Determines the number of bricks in the game 
	 */
	
	private int brickNumber = (NBRICKS_PER_ROW*NBRICK_ROWS);
	/*
	 *Checks for the any object near the ball 
	 */
	private GObject getCollidingObject(){		
		if((getElementAt(ball.getX(), ball.getY())) != null) {
	         return getElementAt(ball.getX(), ball.getY());
	      }else{
	    	  if(getElementAt((ball.getX() + BALL_RADIUS*2.0), (ball.getY())) != null ){
	    		  return getElementAt((ball.getX() + BALL_RADIUS*2.0), (ball.getY()));
	    	  }
	    	  if(getElementAt((ball.getX()), (ball.getY() + BALL_RADIUS*2.0)) != null ){
	    		  return getElementAt((ball.getX()), (ball.getY() + BALL_RADIUS*2.0));
	    	  }
	    	  if(getElementAt((ball.getX() + BALL_RADIUS*2.0), (ball.getY() + BALL_RADIUS*2.0)) != null ){
	    		  return getElementAt((ball.getX() + BALL_RADIUS*2.0), (ball.getY() + BALL_RADIUS*2.0));
	    	  }else{
	    		  return null;
	    	  }
	      }
	}
}	
	
	
	


