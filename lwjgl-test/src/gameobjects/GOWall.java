package gameobjects;

import org.lwjgl.opengl.GL11;

import game.GameObjects;
import game.Physics;

public class GOWall extends GameObjects {

	public static final int STD_SIZE = 16;	
	private GOBall ball;
	
	public GOWall(float x, float y, float sx, float sy, GOBall ball){
		this.x = x;
		this.y = y;
		this.sx = sx;
		this.sy = sy;
		this.ball = ball;
	}

	public void render() {
		GL11.glColor3f(0.5f, 0.5f, 0.5f);
		super.render();
	};
	
	@Override
	public void update() {
		if(Physics.checkCollisions(this, ball)){
			ball.reverseY();
		}
	}

	@Override
	public void resize(int w, int h) {
		
		if(this.x == 0 && this.y == 0){
			this.sx = w;
		}else{
			this.y = h - STD_SIZE;
			this.sx = w;
		}
		
	}

}
