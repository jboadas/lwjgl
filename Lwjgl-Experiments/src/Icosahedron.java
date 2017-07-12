import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;

import Camera3D.Camera;
import static org.lwjgl.opengl.GL11.*;

public class Icosahedron {
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static boolean running = false;
	public static int fps = 0;
	public static long lastTime;
	public static Camera camera;

	public Icosahedron(){
		
		initDisplay();
		initOpenGL();
		gameLoop();
		cleanUp();
		
	}
	
	public void initDisplay(){
		
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
			Keyboard.create();
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void initOpenGL(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glMatrixMode(GL_MODELVIEW);
		glClearColor(0f, 0f, 0f, 1f);
		glShadeModel(GL_SMOOTH);
		glCullFace (GL_BACK);
		glEnable (GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
	}
	
	public void gameLoop(){
		
		camera = new Camera(10, (float)Display.getWidth()/(float)Display.getHeight(),0.1f,1000);
		camera.moveZ(-15.0f);
		running =  true;
		lastTime = getTime();
		while(running && !Display.isCloseRequested()){
			getInput();
			render();
			Display.update();
			Display.sync(60);
			updateFPS();
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
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		camera.useView();
		
		float X = .525731112119133606f; 
		float Z = .850650808352039932f;

		float[][] vdata = {    
		   {-X, 0.0f, Z}, {X, 0.0f, Z}, {-X, 0.0f, -Z}, {X, 0.0f, -Z},    
		   {0.0f, Z, X}, {0.0f, Z, -X}, {0.0f, -Z, X}, {0.0f, -Z, -X},    
		   {Z, X, 0.0f}, {-Z, X, 0.0f}, {Z, -X, 0.0f}, {-Z, -X, 0.0f} 
		};
		int[][] tindices = { 
		   {0,4,1}, {0,9,4}, {9,5,4}, {4,5,8}, {4,8,1},    
		   {8,10,1}, {8,3,10}, {5,3,8}, {5,2,3}, {2,7,3},    
		   {7,10,3}, {7,6,10}, {7,11,6}, {11,0,6}, {0,1,6}, 
		   {6,1,10}, {9,0,11}, {9,11,2}, {9,2,5}, {7,2,11} };
		int i;

		glColor3f(1.0f, 0.6f, 0.0f);
		glBegin(GL_TRIANGLES);    
		for (i = 0; i < 20; i++) {    
		   /* color information here */ 
		   glVertex3f(vdata[tindices[i][0]][0],vdata[tindices[i][0]][1],vdata[tindices[i][0]][2]); 
		   glVertex3f(vdata[tindices[i][1]][0],vdata[tindices[i][1]][1],vdata[tindices[i][1]][2]); 
		   glVertex3f(vdata[tindices[i][2]][0],vdata[tindices[i][2]][1],vdata[tindices[i][2]][2]); 
		}
		glEnd();
		glColor3f(0.0f, 0.0f, 0.0f);
		glBegin(GL_LINES);    
		for (i = 0; i < 20; i++) {    
		   /* color information here */ 
		   glVertex3f(vdata[tindices[i][0]][0],vdata[tindices[i][0]][1],vdata[tindices[i][0]][2]); 
		   glVertex3f(vdata[tindices[i][1]][0],vdata[tindices[i][1]][1],vdata[tindices[i][1]][2]); 
		   glVertex3f(vdata[tindices[i][2]][0],vdata[tindices[i][2]][1],vdata[tindices[i][2]][2]); 
		}
		glEnd();	
	}
	
	public void cleanUp(){
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
	
	public static void main(String[] args) {
		
		new Icosahedron();

	}

}
