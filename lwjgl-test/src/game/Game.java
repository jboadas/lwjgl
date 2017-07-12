package game;

import gameobjects.GOBall;
import gameobjects.GOEnemy;
import gameobjects.GOPlayer;
import gameobjects.GOWall;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class Game {

	private ArrayList<GameObjects> objects;
	private GOPlayer player;
	private GOEnemy enemy;
	private GOBall ball;
	
	private int playerScore;
	private int enemyScore;
	
	public Game(){
		
		playerScore = 0;
		enemyScore = 0;
		objects = new ArrayList<GameObjects>();
		ball = new GOBall((Display.getWidth()>>1) - (GOBall.SIZE>>1),(Display.getHeight()>>1) - (GOBall.SIZE>>1));
		objects.add(ball);
		
		player = new GOPlayer(10, (Display.getHeight()>>1) - (GOPlayer.SIZEY >> 1), ball);
		objects.add(player);
		
		enemy = new GOEnemy(Display.getWidth() - GOEnemy.SIZEX - 10, (Display.getHeight() >> 1) - (GOEnemy.SIZEY >> 1), ball);
		objects.add(enemy);
		
		GOWall wall_inf = new GOWall(0, 0, Display.getWidth(), GOWall.STD_SIZE, ball);
		objects.add(wall_inf);
		
		GOWall wall_sup = new GOWall(0, Display.getHeight() - GOWall.STD_SIZE, Display.getWidth(), GOWall.STD_SIZE, ball);
		objects.add(wall_sup);
	}
	
	public void getInput(){
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)){
			player.move(1);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			player.move(-1);
		}
	}
	
	public void resize(int w, int h){
		
		for(GameObjects go : objects){
			go.resize(w,h);
		}
		
	}
	
	public void update(){
		for(GameObjects go : objects ){
			go.update();
		}
		
		if(ball.getX() > Display.getWidth()){
			
			playerScore++;
			ball.resetPosition();
			
		}
		if(ball.getX() < 0){
			
			enemyScore++;
			ball.resetPosition();
			
		}
	}
	
	public void render(){
		for(GameObjects go : objects){
			go.render();
		}
		
		Display.setTitle("Player Score: " + playerScore + " Enemy Score : " + enemyScore);
	}
}
