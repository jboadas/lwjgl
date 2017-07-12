package gameobjects;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import game.GameObjects;
import game.Physics;

public class GOEnemy extends GameObjects {

	public static final int SIZEX = 16;
	public static final int SIZEY = SIZEX * 7;
	private static final float MAX_SPEEDY = 4f;
	private static final float DAMPING = 0.5f;
	private GOBall ball;
	
	public GOEnemy(float x, float y, GOBall ball) {
		
		this.x = x;
		this.y = y;
		this.sx = SIZEX;
		this.sy = SIZEY;
		this.ball = ball;

	}
	
	public void render() {
		GL11.glColor3f(0.5f, 0f, 0f);
		super.render();
	};

	@Override
	public void update() {
		
		float speed = (getCenterY() - ball.getCenterY())*DAMPING;
		
		if(Physics.checkCollisions(this, ball)){
			ball.reverseX(speed);
		}
		
		if(speed > MAX_SPEEDY){
			speed = MAX_SPEEDY;
		}
		if(speed < -MAX_SPEEDY){
			speed = - MAX_SPEEDY;
		}
		
		y -= speed;

		if(y < GOWall.STD_SIZE){
			y = GOWall.STD_SIZE;
		}
		if(y > (Display.getHeight() - GOWall.STD_SIZE - SIZEY)){
			y = (Display.getHeight() - GOWall.STD_SIZE - SIZEY);
		}
		
	}

	@Override
	public void resize(int w, int h) {
		
		this.x = w - SIZEX;
		
	}

}
