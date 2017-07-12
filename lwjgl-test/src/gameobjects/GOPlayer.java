package gameobjects;

import game.GameObjects;
import game.Physics;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GOPlayer extends GameObjects {
	
	public static final int SIZEX = 16;
	public static final int SIZEY = SIZEX * 7;
	public static final float SPEED = 4f;
	private GOBall ball;
	private float currentSpeed = 0;
	
	public GOPlayer(float x, float y, GOBall ball){
		
		this.x = x;
		this.y = y;
		this.sx = SIZEX;
		this.sy = SIZEY;
		this.ball = ball;
	}

	public void render() {
		GL11.glColor3f(0f, 0.50f, 1f);
		super.render();
	};
	
	@Override
	public void update() {
		
		if( Physics.checkCollisions(this, ball)){
			ball.reverseX(currentSpeed);
		}
		if(currentSpeed != 0){
			y += currentSpeed;
			currentSpeed = 0;
		}
		if(y < GOWall.STD_SIZE){
			y = GOWall.STD_SIZE;
		}
		if(y > (Display.getHeight() - GOWall.STD_SIZE - SIZEY)){
			y = (Display.getHeight() - GOWall.STD_SIZE - SIZEY);
		}
		
	}
	
	public void move(float mag){
		
		currentSpeed = SPEED * mag;
		
	}

	@Override
	public void resize(int w, int h) {
		// TODO Auto-generated method stub
		
	}

}
