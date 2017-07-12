package gameobjects;

import org.lwjgl.opengl.GL11;

import game.GameObjects;

public class GOBall extends GameObjects {
	
	public static final float MAX_SPEEDX = 4f;
	public static final float MAX_SPEEDY = 8f;
	public static final float DAMPING = 0.5f;
	
	public static final int SIZE = 16;
	public float velX;
	public float velY;
	public float startX;
	public float startY;
	
	public GOBall(float x, float y) {
		this.x = x;
		this.y = y;
		this.sx = SIZE;
		this.sy = SIZE;
		velX = -MAX_SPEEDX;
		velY = 0;
		startX = x;
		startY = y;
		
	}
	
	public void render() {
		GL11.glColor3f(0f, 0.5f, 0f);
		super.render();
	};

	@Override
	public void update(){
		
		x += velX;
		y += velY;
		
	}
	
	public void reverseX(float speedY){
		velX *= -1;
		velY += speedY * DAMPING;
		
		if(velY > MAX_SPEEDY){
			velY = MAX_SPEEDY;
		}
		if(velX < - MAX_SPEEDY){
			velY = -MAX_SPEEDY;
		}
	}
	
	public void reverseY(){
		velY *= -1;
	}
	
	public void resetPosition(){
		
		this.x = startX;
		this.y = startY;
		this.velX *= -1;
		this.velY = 0;
		
	}

	@Override
	public void resize(int w, int h) {
		// TODO Auto-generated method stub
		
	}

}
