package tutorials.nehe;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

// Things finally start moving in the fourth Nehe lessson -- literally.
// We already learned about translation in the last lesson, which tells
// OpenGL to shift all geometry commands along an axis.  Now we'll take a
// look at glTranslate, which rotates geometry commands around an axis.
// Instead of just applying a fixed rotation, we'll update the rotation
// amount every frame, yielding our first animation.

// By playing with the code, you can see how rotations and translations are
// able to affect each other.  Stacking multiple rotations on top of each other
// cumulatively can result in interesting and unpredictable movements.  Right now
// we just use glLoadIdentity to reset things to a starting point, but later you'll
// learn about more complex ways to manage and manipulate the modelview matrix.

public class Lesson04 {
    private String windowTitle = "NeHe Lesson 4: Rotation";
    private int windowWidth  = 800;
    private int windowHeight = 600;
    private boolean quitRequested = false;

    private float triangleRotation = 0.0f;
    private float quadRotation = 0.0f;

    public static void main(String[] args) throws Exception {
        Lesson04 app = new Lesson04();
        app.run();
    }

    private void renderScene() {
        // Again we only incrementally change the code from the previous example, adding
        // a couple of rotations to the mix.  We update the rotation parameters inside this
        // renderScene method for simplicity, but in real apps you might want to break out
        // program state and logic in into a separate method and keep it out of the renderer.

        triangleRotation += 1.0f;
        triangleRotation %= 360;

        quadRotation += 0.5f;
        quadRotation %= 360;

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glLoadIdentity();

        GL11.glTranslatef(-1.5f, 0.0f, -6.0f);

        // Here's where we do the rotation.  glRotate takes an angle (in degrees!) to rotate,
        // and three more parameters that make up an axis to rotate around.  This axis is defined
        // by drawing a line from the origin (0,0,0) to the x,y,z given by the next three
        // parameters, and rotating further geometry commands around that axis.  The rotation
        // follows the "right hand rule", meaning it rotates counterclockwise as you look down
        // the axis toward the origin.

        // The rotation axis is affected by previous translations, which means a rotation after
        // a translation means "translate, then rotate" -- in other words, it does what you expect.
        // If you reverse and do rotation then translation, it will rotate then translate in the
        // newly rotated direction, causing objects to "orbit" around a point.  And if you do two
        // rotations in a row, the first rotation will affect the second one, and so on.s

        GL11.glRotatef(triangleRotation, 0.0f, 1.0f, 0.0f);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glColor3f(0.0f, 1.0f, 0.0f);       // green
        GL11.glVertex3f(0.0f, 1.0f, 0.0f);
        GL11.glColor3f(1.0f, 0.0f, 0.0f);       // red
        GL11.glVertex3f(-1.0f, -1.0f, 0.0f);
        GL11.glColor3f(0.0f, 0.0f, 1.0f);       // blue
        GL11.glVertex3f(1.0f, -1.0f, 0.0f);
        GL11.glEnd();

        // Previously, we just translated relative to the first translation, but now we've
        // rotated, so if we translate again, it will be in the rotated direction and we'll
        // get that "orbiting" effect, which isn't what we want.  So we need to reset and
        // start over from the untranslated, unrotated origin.
        GL11.glLoadIdentity();
        GL11.glTranslatef(1.5f, 0.0f, -6.0f);
        GL11.glRotatef(quadRotation, 1.0f, 0.0f, 0.0f);

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
