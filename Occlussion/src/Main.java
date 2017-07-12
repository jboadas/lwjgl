import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Quadric;
import org.lwjgl.util.glu.Sphere;

import static org.lwjgl.opengl.GL11.*;

public class Main {

	public static final int NLIGHTS = 7;
	//public static FloatBuffer white = BufferUtils.createFloatBuffer(4).put(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
	public static FloatBuffer white;
	//public static FloatBuffer black = BufferUtils.createFloatBuffer(4).put(new float[] { 0.0f, 0.0f, 0.0f, 0.0f });
	public static FloatBuffer black;
	//public static FloatBuffer darkLight = BufferUtils.createFloatBuffer(4).put(new float[] { -2.0f, -2.0f, -2.0f, 2.0f });
	public static FloatBuffer darkLight;
	public static Quadric q;
	public static int startTime;
	public static float time;
	public static Disk disco;
	public static Sphere esfera;
	
	public static void main(String[] args) {
		
		initDisplay();
		gameLoop();
		cleanUp();

	}
	
	public static void initDisplay(){
		
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
			setupBuffers();
			setup();

		} catch (LWJGLException e) {
			e.printStackTrace();

		}
		
	}
	
	public static void setupBuffers(){
		float[] matWhite={0.1f,0.1f,0.1f,1f};
		white = BufferUtils.createFloatBuffer(4);
		white.put(matWhite);
		white.flip();		

		float[] matBlack={0f,0f,0f,0f};
		black = BufferUtils.createFloatBuffer(4);
		black.put(matBlack);
		black.flip();		

		float[] matDarkLight={ -2.0f, -2.0f, -2.0f, 2.0f };
		darkLight = BufferUtils.createFloatBuffer(4);
		darkLight.put(matDarkLight);
		darkLight.flip();
		
		disco =  new Disk();
		esfera = new Sphere();
		
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 600, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public static void setup(){

		q = new Quadric();
		q.setNormals(GLU.GLU_SMOOTH);
		//glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
		glEnable(GL_COLOR_MATERIAL);
		glLightModel(GL_LIGHT_MODEL_AMBIENT, white);
		
		for (int i = 0; i<NLIGHTS+1; i++){
			glLight(GL_LIGHT0+i, GL_AMBIENT, black);
			glLight(GL_LIGHT0+i, GL_SPECULAR, black);
			glLight(GL_LIGHT0+i, GL_DIFFUSE, darkLight);
			glLightf(GL_LIGHT0+i, GL_CONSTANT_ATTENUATION, 1.0f);
			glLightf(GL_LIGHT0+i, GL_LINEAR_ATTENUATION, 0.0f);
			glLightf(GL_LIGHT0+i, GL_QUADRATIC_ATTENUATION, 2.0f);
			
		}
		
		for (int j = 0; j<NLIGHTS; j++){
			glEnable(GL_LIGHT0+j);
		}
		
		glLightf(GL_LIGHT7, GL_CONSTANT_ATTENUATION,  0.0f);
		glLightf(GL_LIGHT7, GL_LINEAR_ATTENUATION,  1.0f);
		glLightf(GL_LIGHT7, GL_QUADRATIC_ATTENUATION,  0.0f); 
		glEnable (GL_LIGHT7);
		glEnable(GL_LIGHTING);
		glEnable(GL_DEPTH_TEST);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(60.0f,1.0f,0.1f,100.0f);
		GLU.gluLookAt(0,0,-10.0f, 0,0,0, 0,1,0);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		//time =0;
		
	}


	public static void moveSpheres(int val){
		glRotatef(time*0.4f,1,1,1);
		glRotatef(val*time,val,5,3);
		glTranslatef((float)Math.sin(val+time*0.1f)*4.0f,0,0);
	}

	public static void moveGroundLight(int val){
	    glTranslatef(0,-6.0f,0);
		glScalef(1.0f,0.0f,1.0f);
		moveSpheres(val);
	}

	public static void drawGround(){
	   // draw GROUND plane
	   glLoadIdentity();
	   glTranslatef(0.0f,-5.0f,0.0f);
	   glRotatef (-90,1,0,0);
	   glColor4f (1.0f,1.0f,0.9f,1.0f);
	   // disable the ground light as it should not occlude itself
	   glDisable (GL_LIGHT7);
	   //gluDisk(q,0.0f,20.0f,60,60);
	   disco.draw(0.0f,20.0f,60,60);
	   glEnable (GL_LIGHT7);
	}

	public static void drawAOSpheres(){
	int i;
	   // set the lights in position
	   for (i=0; i<NLIGHTS; i++) {
		    glLoadIdentity();
			moveSpheres(i);
			// black is 0,0,0,1, which are the values we need for
			// a point light at the origin.
			glLight (GL_LIGHT0+i, GL_POSITION, black);
	   }

	   // draw the spheres being careful not to draw a sphere occluding itself
	   for (i=0;i<NLIGHTS; i++) {
	     
		 // simulate a plane by always placing a light under the sphere on the plane
	     glLoadIdentity();
		 moveGroundLight(i);
		 glLight (GL_LIGHT7, GL_POSITION, black);
	     // a sphere should not occlude itself so switch off its own light
		 glDisable (GL_LIGHT0+i);
		 //glColor4f (1.0f-(i/20.0f),0.7f+(i/20.0f),1.0f,1.0f);
		 glColor4f (1.0f,0.7f,1.0f,1.0f);
		 glLoadIdentity();
		 moveSpheres(i);
		 //gluSphere (q,1,30,30);
		 esfera.draw(1,30,30);
	     glEnable (GL_LIGHT0+i);
	   }
	}
	
	
	
	public static void gameLoop(){
		
		while(!Display.isCloseRequested()){
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			drawAOSpheres();
			drawGround();
			Display.update();
		}
		
	}
	
	public static void cleanUp(){
		
		Display.destroy();
		
	}

}
