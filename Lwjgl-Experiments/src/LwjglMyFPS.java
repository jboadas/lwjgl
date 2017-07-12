import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;

import Camera3D.Camera;
import static org.lwjgl.opengl.GL11.*;

public class LwjglMyFPS {
	
	public Camera camera;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int FRAMERATE = 60;
	public static boolean running = false;
	public static int fps = 0;
	public static long lastTime;

	public LwjglMyFPS(){
		
		initDisplay();
		initOpenGL();
		gameLoop();
		cleanUp();
	}
	
	public void initDisplay(){
		
		try {
			DisplayMode dm = getDisplayMode(WIDTH, HEIGHT);
			if(dm == null){
				dm = new DisplayMode(WIDTH, HEIGHT);
			}
			Display.setDisplayMode(dm);
			Display.setVSyncEnabled(true);
			Display.create();
			Display.setFullscreen(false);
			Keyboard.create();
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public DisplayMode getDisplayMode(int width, int height){
		DisplayMode myDisplayMode = null; 
		DisplayMode[] modes;
		try {
			modes = Display.getAvailableDisplayModes();
			for (int i=0;i< modes.length;i++) {
				if((modes[i].getWidth() == width) &&
					(modes[i].getHeight() == height) &&
					(modes[i].isFullscreenCapable()) &&
					(modes[i].getFrequency() == FRAMERATE)){
						myDisplayMode = modes[i];
						break;
				}
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		return myDisplayMode;
	}
	
	public void initOpenGL(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glMatrixMode(GL_MODELVIEW);
		glClearColor(0f, 0f, 0f, 0.1f);
		glShadeModel(GL_SMOOTH);
		glCullFace (GL_BACK);
		glEnable (GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
		glEnable(GL_COLOR_MATERIAL);
	}
	
	public void gameLoop(){
		
		camera = new Camera(10, (float)Display.getWidth()/(float)Display.getHeight(),0.1f,1000);
		camera.moveZ(-15.0f);
		running =  true;
		lastTime = getTime();
		while(running && !Display.isCloseRequested()){
			getInput();
			render();
		}
	}
	
	public long getTime(){
		//Timer resolution gets the # of tics in a milisecond
		//get time get current tics 
		//so multiply by 1000 to get seconds (1000 miliseconds on a second)
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
    public void updateFPS() {
        if (getTime() - lastTime > 1000) {
            Display.setTitle("FPS: " + fps);
            fps = 0;
            lastTime += 1000;
        }
        fps++;
    }	
	public void getInput(){
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			running = false;
		}
		int dWheel = Mouse.getDWheel();
		if(dWheel!=0){
			camera.moveZ(0.005f * dWheel);
		}
		
		
		boolean meb = Mouse.isButtonDown(0);
		if(meb){
			int dx = Mouse.getDX();
			int dy = Mouse.getDY();
			
			if(dx !=0 || dy !=0){
				camera.moveXY(dx*0.02f, dy*0.02f);
			}
		}
	}
	
	public void render(){
		glClear(GL_COLOR_BUFFER_BIT);
		Display.update();
		Display.sync(FRAMERATE);
		updateFPS();
	}
	
	public void cleanUp(){
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
	
	public static void main(String[] args) {
		
		new LwjglMyFPS();

	}

}
