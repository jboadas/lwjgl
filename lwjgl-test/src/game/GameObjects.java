package game;

public abstract class GameObjects {
	
	protected float x;
	protected float y;
	protected float sx;
	protected float sy;
	
	abstract public void update();
	abstract public void resize(int w, int h);
	
	public void render(){
		
		Draw.rect(x, y, sx, sy);
		
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getSx() {
		return sx;
	}

	public float getSy() {
		return sy;
	}
	
	public float getCenterY(){
		return y + sy/2;
	}

}
