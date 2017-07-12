import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

// Pagina 23

public class Boot {
	
	public Boot(){
		
		initialize();
		initializeOpenGL();
		gameLoop();
		cleanUp();
		
	}
	
	public static void initialize(){
		
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setVSyncEnabled(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void initializeOpenGL(){
		//Left, right, bottom, top, znear, zfar
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		//glOrtho(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0);
		glOrtho(0, 800, 640, 0, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glColor3f(1, 1, 1);
		glShadeModel(GL_FLAT);
	}
	
	public static void gameLoop(){
		
		while(! Display.isCloseRequested()){
			example11();
			Display.update();
			Display.sync(60);
		}
	}
	
	public static void example11(){
		glClear(GL_COLOR_BUFFER_BIT);
		glColor3f(1.0f, 1.0f, 1.0f);
		glBegin(GL_POLYGON);
		{
			glVertex2d(10, 10);
			glVertex2d(100, 10);
			glVertex2d(100, 100);
			glVertex2d(10, 100);
		}
		glEnd();
		glBegin(GL_LINES);
		{
			glVertex2f(100f,100f);
			glVertex2f(200f,200f);
			glVertex2f(200f,200f);
			glVertex2f(300f,200f);
			glVertex2f(300f,200f);
			glVertex2f(400f,100f);
		}
		glEnd();
//		glBegin(GL_LINES);
//		{
//		}
//		glEnd();
		glFlush(); //mmmmm
	}
	
	public static void cleanUp(){
		Display.destroy();
	}

	public static void main(String[] args) {

		new Boot();
		
	}

}
