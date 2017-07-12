package tutorials.nehe;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

// The second NeHe lesson draws a couple of polygons on the screen, using primitive
// operations.

// Now that you can see something real getting drawn, you should tweak the parameters
// in this program: change the coordinates in glVertex and glTranslate for starters.

public class Lesson02 {
    private String windowTitle = "NeHe Lesson 2: Your First Polygons";
    private int windowWidth  = 800;
    private int windowHeight = 600;
    private boolean quitRequested = false;

    public static void main(String[] args) throws Exception {
        Lesson02 app = new Lesson02();
        app.run();
    }

    private void renderScene() {
        // Clear the screen to the defined glClearColor
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        // glLoadIdentity resets the currently selected matrix to the identity matrix, which
        // undoes any translations and rotations currently in effect.
        GL11.glLoadIdentity();

        // glTranslate is a command that changes the ModelView matrix to translate (i.e. move)
        // all further geometry commands (like glVertex3f) by the given number of units on the
        // x,y,z axes respectively.  This translation will "stick" until altered by another
        // translation, or until glLoadIdentity() is called.
        // This translation moves -1.5 units on the X axis, which given our current projection
        // and modelview matrices, means "left".  It's also moving -6 units along the Z axis,
        // which means "away" from us (deeper into the screen).  When we use a perspective
        // projection, things have to be translated to a depth between the near and far planes
        // in order to be visible (change the -6.0f to 0.0f and you'll see ... or won't!)
        GL11.glTranslatef(-1.5f, 0.0f, -6.0f);

        // glBegin and glEnd come in pairs, as you might expect.  They tell OpenGL how to interpret
        // the glVertex commands between them.  In this case, it will connect three vertices into
        // a triangle.  You can draw multiple triangles inside a single glBegin/End pair, just give
        // it three more vertices.  You can try drawing more triangles, but you might want to wait
        // for the next lesson which adds color to the polygons.
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex3f( 0.0f,  1.0f, 0.0f);
        GL11.glVertex3f(-1.0f, -1.0f, 0.0f);
        GL11.glVertex3f( 1.0f, -1.0f, 0.0f);
        GL11.glEnd();

        // Translations "stack" with each other, so moving 3 units positively along the X axis
        // means we're now 1.5 units to the right of the origin, and still 6 deep on the Z axis.
        GL11.glTranslatef(3.0f, 0.0f, 0.0f);

        // A quad is like triangles, except it turns sets of four vertices into a 4-sided polygon.
        // There are some restrictions: all the points for a quad have to lie on the same plane, and
        // your quads have to be convex (this goes for any polygon you draw in OpenGL).  The upshot
        // of triangles is they always lie on a plane and they're always convex, but quads still
        // have their uses in other places.
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3f(-1.0f,  1.0f, 0.0f);
        GL11.glVertex3f( 1.0f,  1.0f, 0.0f);
        GL11.glVertex3f( 1.0f, -1.0f, 0.0f);
        GL11.glVertex3f(-1.0f, -1.0f, 0.0f);
        GL11.glEnd();                        
    }

    // Everything below here is the same as it was in the previous lesson.

    /** Sets up OpenGL, runs the main loop of our app, and handles exiting */
    public void run() throws Exception {
        initialize();
        try {
            while (!quitRequested) {
                // This is the main loop of our application
                handleInput();      // Process input (e.g. keyboard, mouse, window events)
                renderScene();      // Render the frame to be drawn to the back buffer
                Display.update();   // Display the back buffer, then poll for input
                Display.sync(60);   // Sleep long enough for the app to run at 60FPS
            }
        } catch (Exception e) {
            Sys.alert(windowTitle, "An error occured -- now exiting.");
            e.printStackTrace();
            System.exit(0);
        } finally {
            cleanup();
        }
    }

    /** Sets up the window and sets up openGL options. */
    private void initialize() throws Exception {
        initDisplay();  // Get a display window
        initGL();       // Set options and initial projection
    }

    /** Creates a new window and sets options on it */
    private void initDisplay() throws LWJGLException {
        DisplayMode mode = new DisplayMode(windowWidth, windowHeight);
        Display.setDisplayMode(mode);
        Display.setTitle(windowTitle);
        Display.setVSyncEnabled(true);
        Display.create();
    }

    /** Sets up OpenGL options after a successful initDisplay() */
    private void initGL() {
        DisplayMode dm = Display.getDisplayMode();
        float aspect = (float) dm.getWidth() / (float) dm.getHeight();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GLU.gluPerspective(45.0f, aspect, 0.1f, 100.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }


    /** Reads queued keyboard events and takes appropriate action on them. */
    private void handleInput() throws LWJGLException {
        if (Display.isCloseRequested())

        {
            // The display window is being closed
            quitRequested = true;
            return;
        }

        while (Keyboard.next())

        {
            int key = Keyboard.getEventKey();
            boolean isDown = Keyboard.getEventKeyState();
            if (isDown) {
                switch (key) {
                    case Keyboard.KEY_ESCAPE:
                        quitRequested = true;
                        break;

                    case Keyboard.KEY_RETURN:
                        if (Keyboard.isKeyDown(Keyboard.KEY_LMENU))
                            Display.setFullscreen(!Display.isFullscreen());
                        break;
                }
            }
        }

    }

    /** Perform final actions to release resources. */
    private void cleanup() {
        Display.destroy();
    }

}
