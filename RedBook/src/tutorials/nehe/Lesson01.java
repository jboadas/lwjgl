package tutorials.nehe;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * The first NeHe lesson is just about getting a display and opening a blank window.  Unlike the
 * tutorial code, I don't set up a bunch of fancy options that don't get used.  Another difference
 * is that these lessons always start windowed. Use Alt-Enter (or Cmd-Enter) to toggle fullscreen
 * mode.
 * <p/>
 * While I try to keep the code free of serious WTF moments, keep in mind that these lessons are
 * about being brief, not about good Java style. Real-world applications should use accessors,
 * abstract things into more classes, use javadoc, decouple input handling from actions, and so on.
 */

public class Lesson01 {
    private String windowTitle = "NeHe Lesson 1: Creating an OpenGL Window";
    private int windowWidth  = 800;
    private int windowHeight = 600;

    private boolean quitRequested = false;

    public static void main(String[] args) throws Exception {
        Lesson01 app = new Lesson01();
        app.run();
    }

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
        // At the time this tutorial was written, LWJGL did not support a resizable window.
        // When it does, you should use this line instead of the two above:
        // float aspect = (float) Display.getWidth() / (float) Display.getHeight();

        // This tells OpenGL to take the red pill (sorry, that's the last Matrix joke I'll make).
        // It actually tells the following commands to work on the "projection matrix", which is
        // the internal data structure that defines how to turn the 3d world into the flat glowing
        // bits on your screen.
        GL11.glMatrixMode(GL11.GL_PROJECTION);

        // gluPerspective() sets up the projection matrix using a function from the utility library
        // called GLU.  The built-in OpenGL command for creating perspective projections is called
        // glFrustum, but it's a lot harder to use than gluPerspective.
        // We don't draw anything in this tutorial, so this projection isn't actually doing anything
        // useful.  We'll find out more about projections in later tutorials.
        GLU.gluPerspective(45.0f, aspect, 0.1f, 100.0f);

        // Now take the blue pill -- aargh!  This says to apply future transforms to the
        // modelview matrix, which affects how objects are positioned in the world.  We're
        // not actually putting any object in the world, but we're choosing to treat the
        // modelview matrix as "default" so we don't have to explicitly switch to it every
        // time we hit our render loop.
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        // This sets the background color to black, or more precisely it's the color everything
        // resets to when you call glClear on the color buffer.  There's a lot of other buffers,
        // but we don't need to worry about them yet.
        // The color is made up of four parts: red, blue, green, and alpha, which have values of
        // floating point numbers that range from 0.0 to 1.0 (think of it as 1.0 = 100%).
        // Anything above 1.0 is "clamped" to 1.0.
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    /** Reads queued keyboard events and takes appropriate action on them. */
    private void handleInput() throws LWJGLException {
        // This is one of the first things a "real application" should abstract by better decoupling
        // command handling from keyboard events, but these lessons are about graphics, not UI.
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

    /** Render the current frame, which will be displayed when Display.update() is called. */
    private void renderScene() {
        // This is where we draw the scene every frame, though it doesn't actually become visible
        // until Display.update() is called.  This is where your code needs to be the tightest.
        // In this tutorial all we do is clear the screen.  We don't really need a render loop
        // doing this at 60fps, but future lessons are going to do a lot more than this.

        // glClear can control a lot of buffers, but this call is clearing the color buffer,
        // which basically means "clear the screen".
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    /** Perform final actions to release resources. */
    private void cleanup() {
        // You want to call Display.destroy for environments where closing your display
        // doesn't necessarily exit the app.  It's not needed here, but it doesn't hurt.
        Display.destroy();
    }
}