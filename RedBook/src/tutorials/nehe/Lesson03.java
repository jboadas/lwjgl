package tutorials.nehe;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

// The third NeHe Lesson involves adding color to the two polygons we drew before.
// For additional flair, I demonstrate the two OpenGL shading modes in action.

// Oh and I spelled the lesson name to say "Color" and not "Colour", because OpenGL
// doesn't speak the Queen's English either.

public class Lesson03 {
    private String windowTitle = "NeHe Lesson 3: Adding Color";
    private int windowWidth  = 800;
    private int windowHeight = 600;
    private boolean quitRequested = false;

    public static void main(String[] args) throws Exception {
        Lesson03 app = new Lesson03();
        app.run();
    }

    private void renderScene() {
        // This code is the same as the previous lesson, except for the addition
        // of shading and color commands.
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glLoadIdentity();

        GL11.glTranslatef(-1.5f, 0.0f, -6.0f);

        // The glShadeModel command takes either GL_SMOOTH or GL_FLAT as its argument
        // With GL_SMOOTH enabled, when a primitive has different colors on its vertices,
        // OpenGL will interpolate the colors for a smooth shading effect.
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glColor3f(0.0f, 1.0f, 0.0f);       // green
        GL11.glVertex3f(0.0f, 1.0f, 0.0f);
        GL11.glColor3f(1.0f, 0.0f, 0.0f);       // red
        GL11.glVertex3f(-1.0f, -1.0f, 0.0f);
        GL11.glColor3f(0.0f, 0.0f, 1.0f);       // blue
        GL11.glVertex3f(1.0f, -1.0f, 0.0f);
        GL11.glEnd();

        GL11.glTranslatef(3.0f, 0.0f, 0.0f);

        // When GL_FLAT is enabled, the last color "wins", and the other colors are ignored
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor3f(1.0f, 1.0f, 0.0f);       // yellow
        GL11.glVertex3f(-1.0f, 1.0f, 0.0f);
        GL11.glColor3f(1.0f, 0.0f, 1.0f);       // magenta
        GL11.glVertex3f(1.0f, 1.0f, 0.0f);
        GL11.glColor3f(1.0f, 0.5f, 0.25f);       // orange
        GL11.glVertex3f(1.0f, -1.0f, 0.0f);
        GL11.glColor3f(0.65f, 0.3f, 0.6f);       // purple
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
