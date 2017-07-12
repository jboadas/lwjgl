package Camera3D;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;
//TODO Pag 195 Display Lists
import org.lwjgl.util.glu.Sphere;
import org.newdawn.slick.opengl.Texture;

public class mainCamera3D {
	
	public Camera camera;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int FRAMERATE = 60;
	public static final float PI = 3.1415926535898f;
	public static boolean running = false;
	public static int fps = 0;
	public static long lastTime;
	public static float rotAng = 0f;
	public static Sphere sun;
	public static Sphere planet;
	public static float year = 0.0f;
	public static float day = 0.0f;
	public static Texture base;

	public mainCamera3D(){
		
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
						//System.out.println("Frec : " + modes[i].getFrequency());
						break;
				}
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		return myDisplayMode;
	}
	
	public void initOpenGL(){
		glClearColor(0f, 0f, 0f, 0f);
		glClearDepth(1.0);
		glEnable(GL_BLEND);
		glShadeModel(GL_SMOOTH);
		glPolygonMode(GL_FRONT, GL_FILL); //para ver la orientacion de las normales
		glPolygonMode(GL_BACK, GL_LINE);
		glEnable(GL_NORMALIZE);
		glEnableClientState(GL_NORMAL_ARRAY);
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		base = Util.loadTexture("base");
		glEnable(GL_TEXTURE_2D);
	}
	
	public void gameLoop(){

		sun = new Sphere();
		sun.setDrawStyle(GLU.GLU_LINE);
		planet = new Sphere();
		planet.setDrawStyle(GLU.GLU_LINE);
		
		//camera = new Camera(70, (float)Display.getWidth()/(float)Display.getHeight(),0.3f,1000);
		camera = new Camera(60, (float)Display.getWidth()/(float)Display.getHeight(),1f,100);
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
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		camera.useView();
		drawCube();
		//solarSystem();
		Display.update();
		Display.sync(FRAMERATE);
		updateFPS();
	}
	
	private void drawCube(){
		
		glColor3f(1.0f, 0.5f, 0f);
		glTranslatef(0, 0, -10);
		//glRotatef(rotAng, 1, 1, 0);
		glLineWidth(1.0f);
		base.bind();
		glPushMatrix();
		{
			glBegin(GL_QUADS);
			{
				//back plane
				glColor3f(0.6f, 0.6f, 0.6f);
				glTexCoord2f(0, 1);	glVertex3f(-30f, -30f, -1f);
				glTexCoord2f(1, 1);	glVertex3f(30f, -30f, -1f);
				glTexCoord2f(1, 0); glVertex3f(30f, 30f, -1f);
				glTexCoord2f(0, 0); glVertex3f(-30f, 30f, -1f);
				
//				//back Face //no se ve
//				glVertex3f(-1, -1, -1);
//				glVertex3f(1, -1, -1);
//				glVertex3f(1, 1, -1);
//				glVertex3f(-1, 1, -1);
//				//bottom Face
//				glColor3f(0f, 1f, 0.5f);//verde
//				glVertex3f(1, -1, -1);
//				glVertex3f(1, -1, 1);
//				glVertex3f(-1, -1, 1);
//				glVertex3f(-1, -1, -1);
//				//left Face
//				glColor3f(0f, 0f, 1f); //azul
//				glVertex3f(-1, -1, -1);
//				glVertex3f(-1, -1, 1);
//				glVertex3f(-1, 1, 1);
//				glVertex3f(-1, 1, -1);
//				//top Face
//				glColor3f(1f, 0f, 0f);//roja
//				glVertex3f(-1, 1, -1);
//				glVertex3f(-1, 1, 1);
//				glVertex3f(1, 1, 1);
//				glVertex3f(1, 1, -1);
//				//front Face
//				glColor3f(0.1f, 1f, 1f);//azul claro
//				glVertex3f(-1, -1, 1);
//				glVertex3f(1, -1, 1);
//				glVertex3f(1, 1, 1);
//				glVertex3f(-1, 1, 1);
//				//right Face
//				glColor3f(1f, 1f, 0f);//amarilla
//				glVertex3f(1, -1, 1);
//				glVertex3f(1, -1, -1);
//				glVertex3f(1, 1, -1);
//				glVertex3f(1, 1, 1);
			}
			glEnd();
//			
//			//Draw Hexagon
//			int circle_points = 6;
//			glColor3f(0f, 0f, 0f);
//			glLineWidth(4.0f);
//			glBegin(GL_LINE_LOOP);
//			{
//				for (int i = 0; i < circle_points; i++) {
//					float angle = 2*PI*i/circle_points;
//					glVertex3f((float)Math.cos(angle), (float)Math.sin(angle),2f);
//				}
//			}
//			glEnd();
//			glBegin(GL_POLYGON);
//			{
//				for (int i = 0; i < circle_points; i++) {
//					float angle = 2*PI*i/circle_points;
//					glVertex3f((float)Math.cos(angle), (float)Math.sin(angle),3f);
//				}
//			}
//			glEnd();			
			
			drawElements02();
			
			//drawArrayElements();
		}
		glPopMatrix();
		rotAng += 0.9f;
	}
	
	public void solarSystem()
	{
		glColor3f (1.0f, 1.0f, 1.0f);
		glPushMatrix();
		{
			glRotatef (year, 0.0f, 0.0f, 1.0f);
			//glRotatef(90, 1.0f, 0f, 0f);
			sun.draw(0.2f, 20, 16);
			glTranslatef (0.5f, 0.0f, 0.0f);
			glRotatef (day, 0.0f, 0.0f, 1.0f);
			planet.draw(0.1f, 10, 8);			
		}
		glPopMatrix();
		year = (year + 0.5f) % 360;
		day = (day + 0.10f) % 360;		
	}
	
	public void drawElements02(){
		float[] geom = {
			-1.0f, -1.0f, -1.0f,
			1.0f, -1.0f, -1.0f,
			1.0f, 1.0f, -1.0f,
			-1.0f, 1.0f, -1.0f,

			-1.0f, -1.0f, 1.0f,
			1.0f, -1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			-1.0f, 1.0f, 1.0f,
		};
		
	    float colors[] = {
	    	1.0f, 0f, 0f,
	    	1.0f, 0f, 0f,
	    	1.0f, 0f, 0f,
	    	1.0f, 0f, 0f,

	    	1f, 0f, 1f,
	    	1f, 0f, 1f,
	    	1f, 0f, 1f,
	    	1f, 0f, 1f,
	    };

	    int[] index = {
				4, 5, 6, 7,
				1, 2, 6, 5,
				0, 1, 5, 4,
				0, 3, 2, 1,
				0, 4, 7, 3,
				2, 3, 7, 6
		};
		
		
	    FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colors.length);
	    colorBuffer.put(colors).flip();

		FloatBuffer s_coords = BufferUtils.createFloatBuffer(geom.length);
		IntBuffer s_indices = BufferUtils.createIntBuffer(index.length);
		s_coords.put(geom);
		s_coords.flip();
		s_indices.put(index);
		s_indices.flip();
		 
		//glLineWidth(4.0f);
		//glColor3f(0f, 0f, 1f);
	    glColorPointer(3, 0, colorBuffer);
		glVertexPointer(3, 0, s_coords);
		//glDrawElements(GL_TRIANGLES, s_indices);
		//glColor3f(0.2f, 0f, 1.0f);
		glDrawElements(GL_QUADS, s_indices);		
		
	}
	
	public void drawArrayElements(){

		glEnableClientState(GL_VERTEX_ARRAY);
	    glEnableClientState(GL_COLOR_ARRAY);

	    float vertices[] = {1.0f, 2.0f, 0.0f, 2.0f, 1.0f, 0f, 1.0f, 1.0f, 0f};
	    float colors[] = {1.0f, 0.5f, 0.8f, 0.3f, 0.5f, 0.8f, 0.3f, 0.5f, 0.8f};


	    FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
	    vertexBuffer.put(vertices).flip();
	    FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colors.length);
	    colorBuffer.put(colors).flip();

	    glVertexPointer(3, 0, vertexBuffer);
	    glColorPointer(3, 0, colorBuffer);

	        glBegin(GL_TRIANGLES);
	        {
		        glArrayElement(0);
		        glArrayElement(1);
		        glArrayElement(2);
				//glVertex3f(-1, -1, -1);
				//glVertex3f(1, -1, -1);
				//glVertex3f(1, 1, -1);
	        }
	        glEnd();
	}
	
	public void cleanUp(){
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
	
	public static void main(String[] args) {
		
		new mainCamera3D();

	}

}
