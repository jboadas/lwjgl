import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;


public class CubeSDL {

    /** frames per second */
    private static int fps;
    /** last fps time */
    private static long lastFPS;
    private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static boolean running = false;
	private static boolean should_rotate = true;
    //Very long (& in theory accurate!) version of Pi.
    //Hopefully an optimizing compiler will replace references to this with the value!
    private static final double PI = 3.1415926535897932384626433832795;
    /* Our angle of rotation. */
    private static float angle = 0.0f;
	
	public static void main(String[] args) {
		
		initDisplay();
		initOpenGL();
		gameLoop();
		cleanUp();

	}

	private static void initDisplay() {
		
		try {
			
			DisplayMode dm = getDisplayMode(WIDTH, HEIGHT);
			if(dm == null){
				dm = new DisplayMode(WIDTH, HEIGHT);
			}
			Display.setDisplayMode(dm);
			Display.setVSyncEnabled(true);
			Display.create();
			Display.setFullscreen(true);
			Keyboard.create();
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		
	}

	public static DisplayMode getDisplayMode(int width, int height){
		DisplayMode myDisplayMode = null; 
		DisplayMode[] modes;
		try {
			modes = Display.getAvailableDisplayModes();
			for (int i=0;i< modes.length;i++) {
				if((modes[i].getWidth() == width) &&
					(modes[i].getHeight() == height) &&
					(modes[i].isFullscreenCapable())) {
						myDisplayMode = modes[i];
						break;
				}
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		return myDisplayMode;
	}
	
	private static void cleanUp() {
		
		Display.destroy();
		
	}

	private static void gameLoop() {
		
		lastFPS = getTime(); // call before loop to initialise fps timer
		running =  true;
	    while(running && !Display.isCloseRequested()){
			getInput();
	    	render();
			Display.sync(60);
			Display.update();
		}
	    cleanUp();
		
	}
	
    public static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
     
    /**
     * Calculate the FPS and set it in the title bar
     */
    public static void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps);
            System.out.println("FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
    
    private static void getInput(){
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			running = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			should_rotate = !should_rotate; 
		}
	}
	
	
	private static void render()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );

	    /*
	     * EXERCISE:
	     * Replace this awful mess with vertex
	     * arrays and a call to glDrawElements.
	     *
	     * EXERCISE:
	     * After completing the above, change
	     * it to use compiled vertex arrays.
	     *
	     * EXERCISE:
	     * Verify my windings are correct here ;).
	     */
//	    float v0[] = { -1.0f, -1.0f,  1.0f };
//	    float v1[] = {  1.0f, -1.0f,  1.0f };
//	    float v2[] = {  1.0f,  1.0f,  1.0f };
//	    float v3[] = { -1.0f,  1.0f,  1.0f };
//	    float v4[] = { -1.0f, -1.0f, -1.0f };
//	    float v5[] = {  1.0f, -1.0f, -1.0f };
//	    float v6[] = {  1.0f,  1.0f, -1.0f };
//	    float v7[] = { -1.0f,  1.0f, -1.0f };
//	    int red[]    = { 255,   0,   0, 255 };
//	    int green[]  = {   0, 255,   0, 255 };
//	    int blue[]   = {   0,   0, 255, 255 };
//	    int white[]  = { 255, 255, 255, 255 };
//	    int yellow[] = {   0, 255, 255, 255 };
//	    int black[]  = {   0,   0,   0, 255 };
//	    int orange[] = { 255, 255,   0, 255 };
//	    int purple[] = { 255,   0, 255,   0 };

	    /* Clear the color and depth buffers. */
	    glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );

	    /* We don't want to modify the projection matrix. */
	    glMatrixMode( GL_MODELVIEW );
	    glLoadIdentity( );

	    /* Move down the z-axis. */
	    glTranslatef( 0.0f, 0.0f, -5.0f );

	    /* Rotate. */
	    glRotatef( angle, 0.0f, 1.0f, 0.0f );

	    if( should_rotate ) {

	        if( ++angle > 360.0f ) {
	            angle = 0.0f;
	        }

	    }

	    /* Send our triangle data to the pipeline. */
	    glBegin( GL_TRIANGLES );
	    {
	    	glColor4ub((byte)255, (byte)0, (byte)0, (byte)255); //red
	        glVertex3f(-1.0f, -1.0f,  1.0f);
	        glColor4ub((byte)0, (byte)255, (byte)0, (byte)255); //green
	        glVertex3f(1.0f, -1.0f,  1.0f);
	        glColor4ub((byte)0, (byte)0, (byte)255, (byte)255); //blue
	        glVertex3f(1.0f,  1.0f,  1.0f);

	        glColor4ub((byte)255, (byte)0, (byte)0, (byte)255); //red
	        glVertex3f(-1.0f, -1.0f,  1.0f);
	        glColor4ub((byte)0, (byte)0, (byte)255, (byte)255); //green
	        glVertex3f(1.0f,  1.0f,  1.0f);
	        glColor4ub((byte)255, (byte)255, (byte)255, (byte)255);//white
	        glVertex3f(-1.0f,  1.0f,  1.0f);

	        glColor4ub((byte)0, (byte)255, (byte)0, (byte)255); //green
	        glVertex3f(1.0f, -1.0f,  1.0f);
	        glColor4ub((byte)0, (byte)0, (byte)0, (byte)255); //Black
	        glVertex3f(1.0f, -1.0f, -1.0f);
	        glColor4ub((byte)255,(byte)255,(byte)0,(byte)255);//orange
	        glVertex3f(1.0f,  1.0f, -1.0f);

	        glColor4ub((byte)0, (byte)255, (byte)0, (byte)255); //green
	        glVertex3f(1.0f, -1.0f,  1.0f);
	        glColor4ub((byte)255,(byte)255,(byte)0,(byte)255); //orange
	        glVertex3f(1.0f,  1.0f, -1.0f);
	        glColor4ub((byte)0, (byte)0, (byte)255, (byte)255); //blue
	        glVertex3f(1.0f,  1.0f,  1.0f);

	        glColor4ub((byte)0, (byte)0, (byte)0, (byte)255); //black
	        glVertex3f(1.0f, -1.0f, -1.0f);
	        glColor4ub((byte)0,(byte) 255,(byte) 255,(byte) 255); //yellow
	        glVertex3f(-1.0f, -1.0f, -1.0f);
	        glColor4ub((byte)255,(byte)0,(byte)255,(byte)0); //purple
	        glVertex3f(-1.0f,  1.0f, -1.0f);

	        glColor4ub((byte)0, (byte)0, (byte)0, (byte)255); //black
	        glVertex3f(1.0f, -1.0f, -1.0f);
	        glColor4ub((byte)255,(byte)0,(byte)255,(byte)0); //purple 
	        glVertex3f(-1.0f,  1.0f, -1.0f);
	        glColor4ub((byte)255,(byte)255,(byte)0,(byte)255); //orange
	        glVertex3f(1.0f,  1.0f, -1.0f);

	        glColor4ub((byte)0,(byte) 255,(byte) 255,(byte) 255); //yellow
	        glVertex3f(-1.0f, -1.0f, -1.0f);
	        glColor4ub((byte)255, (byte)0, (byte)0, (byte)255); //red
	        glVertex3f(-1.0f, -1.0f,  1.0f);
	        glColor4ub((byte)255, (byte)255, (byte)255, (byte)255); //white
	        glVertex3f(-1.0f,  1.0f,  1.0f);

	        glColor4ub((byte)0,(byte) 255,(byte) 255,(byte) 255); //yellow
	        glVertex3f(-1.0f, -1.0f, -1.0f);
	        glColor4ub((byte)255, (byte)255, (byte)255, (byte)255); //white
	        glVertex3f(-1.0f,  1.0f,  1.0f);
	        glColor4ub((byte)255,(byte)0,(byte)255,(byte)0);//purple
	        glVertex3f(-1.0f,  1.0f, -1.0f);

	        glColor4ub((byte)255, (byte)255, (byte)255, (byte)255); //white
	        glVertex3f(-1.0f,  1.0f,  1.0f);
	        glColor4ub((byte)0, (byte)0, (byte)255, (byte)255); //blue
	        glVertex3f(1.0f,  1.0f,  1.0f);
	        glColor4ub((byte)255,(byte)255,(byte)0,(byte)255); //orange
	        glVertex3f(1.0f,  1.0f, -1.0f);

	        glColor4ub((byte)255, (byte)255, (byte)255, (byte)255); //white
	        glVertex3f(-1.0f,  1.0f,  1.0f);
	        glColor4ub((byte)255,(byte)255,(byte)0,(byte)255); //orange
	        glVertex3f(1.0f,  1.0f, -1.0f);
	        glColor4ub((byte)255,(byte)0,(byte)255,(byte)0); //purple
	        glVertex3f(-1.0f,  1.0f, -1.0f);

	        glColor4ub((byte)0, (byte)255, (byte)0, (byte)255); //green
	        glVertex3f(1.0f, -1.0f,  1.0f);
	        glColor4ub((byte)255, (byte)0, (byte)0, (byte)255); //red
	        glVertex3f(-1.0f, -1.0f,  1.0f);
	        glColor4ub((byte)0,(byte) 255,(byte) 255,(byte) 255); //yellow
	        glVertex3f(-1.0f, -1.0f, -1.0f);

	        glColor4ub((byte)0, (byte)255, (byte)0, (byte)255); //green
	        glVertex3f(1.0f, -1.0f,  1.0f);
	        glColor4ub((byte)0,(byte) 255,(byte) 255,(byte) 255); //yellow
	        glVertex3f(-1.0f, -1.0f, -1.0f);
	        glColor4ub((byte)0, (byte)0, (byte)0, (byte)255); //black
	        glVertex3f(1.0f, -1.0f, -1.0f);
	    }
	    glEnd( );

	    /*
	     * EXERCISE:
	     * Draw text telling the user that 'Spc'
	     * pauses the rotation and 'Esc' quits.
	     * Do it using vetors and textured quads.
	     */

	    /*
	     * Swap the buffers. This this tells the driver to
	     * render the next frame from the contents of the
	     * back-buffer, and to set all rendering operations
	     * to occur on what was the front-buffer.
	     *
	     * Double buffering prevents nasty visual tearing
	     * from the application drawing on areas of the
	     * screen that are being updated at the same time.
	     */
	    //SDL_GL_SwapBuffers( );
	    updateFPS(); // update FPS Counter
	}

	private static void initOpenGL() {
		
		glClearColor(0f, 0f, 0f, 0f);
	    float ratio = (float) WIDTH / (float) HEIGHT;

	    /* Our shading model--Gouraud (smooth). */
	    glShadeModel( GL_SMOOTH );

	    /* Culling. */
	    glCullFace( GL_BACK );
	    glFrontFace( GL_CCW );
	    glEnable( GL_CULL_FACE );

	    /* Set the clear color. */
	    glClearColor( 0, 0, 0, 0 );

	    /* Setup our viewport. */
	    glViewport( 0, 0, WIDTH, HEIGHT );

	    /*
	     * Change to the projection matrix and set
	     * our viewing volume.
	     */
	    glMatrixMode( GL_PROJECTION );
	    glLoadIdentity( );
	    /*
	     * EXERCISE:
	     * Replace this with a call to glFrustum.
	     */
	    //gluPerspective( 60.0, ratio, 1.0, 1024.0 );
	    //perspectiveGL(60.0, ratio, 1.0, 1024.0);
	    perspectiveGL(60.0, ratio, 1.0, 1024.0);		
		
	}
	static void perspectiveGL( double fovY, double aspect, double zNear, double zFar )
	{
	    //  Half of the size of the x and y clipping planes.
	    double fW, fH;
	    //Calculate the distance from 0 of the y clipping plane.
	    //Basically trig to calculate position of clipper at zNear.
	    // Note: tan( double ) uses radians but OpenGL works in degrees so we convert degrees to radians
	    // by dividing by 360 then multiplying by pi.
	    //Formula below corrected by Carsten Jurenz:
	    //fH = tan( (fovY / 2) / 180 * pi ) * zNear;
	    //Which can be reduced to:
	    fH = Math.tan( fovY / 360 * PI ) * zNear;
	    //Calculate the distance from 0 of the x clipping plane based on the aspect ratio.
	    fW = fH * aspect;
	    //Finally call glFrustum, this is all gluPerspective does anyway! This is why we calculate
	    //half the distance between the clipping planes - glFrustum takes an offset from zero for each
	    //clipping planes distance. (Saves 2 divides)
	    glFrustum( -fW, fW, -fH, fH, zNear, zFar );
	}
}
