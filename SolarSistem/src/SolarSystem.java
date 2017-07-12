import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;

public class SolarSystem {
	
	public static final int FRAMERATE = 60;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static boolean running = false;
	public static int fps = 0;
	public static long lastTime;
	public static float year = 0.0f;
	public static float day = 0.0f;
	public static float[] mat_specular = {1.0f,1.0f,1.0f,1.0f};
	public static float[] light_position = { 1.0f, 1.0f, 1.0f, 0.0f };
	public static float[] light_diffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
	public static FloatBuffer matSpecular;
	public static FloatBuffer lightDiffuse;
	public static FloatBuffer lightPosition;
	public static Camera camera;
	public static Texture earth;
	public static Texture moon;
	
	public SolarSystem(){
		
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
		glClearColor(0f, 0.0f, 0.0f, 0.0f);
		glShadeModel(GL_SMOOTH);
		glCullFace (GL_BACK);
		glEnable (GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
		glEnable( GL_BLEND );
	    glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );
		glEnable(GL_COLOR_MATERIAL);
		matSpecular = BufferUtils.createFloatBuffer(4);
		matSpecular.put(mat_specular).flip();
		lightPosition = BufferUtils.createFloatBuffer(4);
		lightPosition.put(light_position).flip();
		lightDiffuse = BufferUtils.createFloatBuffer(4);
		lightDiffuse.put(light_diffuse).flip();
		glMaterialf(GL_FRONT, GL_SHININESS, 50.0f);
		glMaterial(GL_FRONT, GL_SPECULAR, matSpecular);
		glLight(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);
		glLight(GL_LIGHT0, GL_POSITION, lightPosition);
		//glEnable(GL_LIGHTING);
		//glEnable(GL_LIGHT0);
		glEnable(GL_TEXTURE_2D);
	}
	
	public void gameLoop(){

		earth = Util.loadTexture("base1");
		moon = Util.loadTexture("base2");
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
            Display.setTitle("Solar System FPS: " + fps);
            System.out.println("FPS : " + fps);
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
		
		//drawPlanets();
		drawPlanets2D();

		Display.update();
		//Display.sync(60);
		updateFPS();
	}
	
	public void drawPlanets2D(){
		for(int i=-24; i<25; i++){
			for(int j=-24; j<25; j++){
				drawPlanet2D((float)i, (float)j);
			}
		}
		year = (year + 0.5f) % 360;
		day = (day + 0.10f) % 360;				
	}
	
	
	
	public void drawPlanet2D(float x, float y){
		
		earth.bind();
		glPushMatrix();
		{
			glTranslatef(x, y, 0.0f);
			glRotatef(day, 0.0f, 0.0f, 1.0f);
			
			glColor3f(1.0f, 1.0f, 1.0f);
			
			float sp = 0.2f;
			glBegin(GL_QUADS);
			{
	
				//glColor3f(0.0f, 1.0f, 0.0f);
				glTexCoord2f(0, 1);	glVertex3f(-sp, -sp, 0.0f);
	
				//glColor3f(1.0f, 0.5f, 0.0f);
				glTexCoord2f(1, 1); glVertex3f(sp, -sp, 0.0f);
	
				//glColor3f(1.0f, 0.0f, 0.0f);
				glTexCoord2f(1, 0); glVertex3f(sp, sp, 0.0f);
	
				//glColor3f(0.0f, 0.0f, 1.0f);
				glTexCoord2f(0, 0); glVertex3f(-sp, sp, 0.0f);
			}
			glEnd();
			
		}
		glPopMatrix();
		
		moon.bind();
		glPushMatrix();
		{
			glTranslatef(x, y, 0.0f);
			glRotatef(year, 0.0f, 0.0f, 1.0f);
			float dm = 0.4f;
			glTranslatef(dm, dm, 0.0f);
			glRotatef(year, 0.0f, 0.0f, 1.0f);
			float sp1 = 0.1f;
			glBegin(GL_QUADS);
			{
				//glColor3f(0.0f, 1.0f, 0.0f);
				glTexCoord2f(0, 1);	glVertex3f(-sp1, -sp1, 0.0f);
	
				//glColor3f(1.0f, 0.5f, 0.0f);
				glTexCoord2f(1, 1); glVertex3f(sp1, -sp1, 0.0f);
	
				//glColor3f(1.0f, 0.0f, 0.0f);
				glTexCoord2f(1, 0); glVertex3f(sp1, sp1, 0.0f);
	
				//glColor3f(0.0f, 0.0f, 1.0f);
				glTexCoord2f(0, 0); glVertex3f(-sp1, sp1, 0.0f);
			}
			glEnd();
		}
		glPopMatrix();
	}
	
	public void drawPlanets(){
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				drawPlanet((float)i, (float)j);
			}
		}
		year = (year + 0.5f) % 360;
		day = (day + 0.10f) % 360;		
		
	}
	
	public void drawPlanet(float x, float y){
		Sphere sun;
		Sphere planet;
		sun = new Sphere();
		sun.setDrawStyle(GLU.GLU_FILL);
		planet = new Sphere();
		planet.setDrawStyle(GLU.GLU_FILL);

		glPushMatrix();
		{
			glTranslatef(x, y, 0);
			glRotatef (year, 0.0f, 0.0f, 1.0f);
			//glRotatef(90, 1.0f, 0f, 0f);
			sun.draw(0.2f, 20, 16);
			glTranslatef (0.5f, 0.0f, 0.0f);
			glRotatef (day, 0.0f, 0.0f, 1.0f);
			planet.draw(0.1f, 20, 8);			
		}
		glPopMatrix();
	}
	
	public void cleanUp(){
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
	
	public static void main(String[] args) {
		
		new SolarSystem();

	}

}
