package tutorials.nehe;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

// In the last lesson, we got a taste of the third dimension by rotating some flat planar objects
// around.  Now we're finally going to start building real three dimensional objects and
// pop that triangle and square into a pyramid and cube and spin those around.

public class Lesson05 {
    private String windowTitle = "NeHe Lesson 5: 3D Shapes";
    private int windowWidth  = 800;
    private int windowHeight = 600;
    private boolean quitRequested = false;

    private float pyramidRotation = 0.0f;
    private float cubeRotation = 0.0f;

    public static void main(String[] args) throws Exception {
        Lesson05 app = new Lesson05();
        app.run();
    }


    private void initGL() {
        DisplayMode dm = Display.getDisplayMode();
        int w = dm.getWidth();
        int h = dm.getHeight();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(45.0f, (float) w / (float) h, 0.1f, 100.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Believe it or not, you need to explicitly tell OpenGL to support the idea
        // that objects in front will obscure objects in back.  This is because it needs
        // extra memory for every pixel to keep track of this depth information, so it's
        // stored separately from colors in a place called the "depth buffer".
        // So let's turn on support for using the depth buffer.
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        // If you don't use depth testing, then polygons will be drawn over each other in
        // the order you draw them in, without regard to whether they're behind another or
        // not.  Try commenting out the above glEnable line, then notice how the cube is drawn.

        // LWJGL creates windows that have a depth buffer by default, so there's nothing else
        // to do -- the command above just told it to actually use that buffer.

        // I'm not going to use the GL_FLAT trick I was using anymore, and for flat
        // shading, I'll just set the colors once per side.  For some primitives, you
        // do actually need to turn on flat shading to keep it from blending, but
        // we're not using anything that requires it in this lesson.
        GL11.glShadeModel(GL11.GL_SMOOTH);
    }

    private void renderScene() {
        // The code should look vaguely familiar, but we're drawing a lot more shapes now

        pyramidRotation += 1.0f;
        pyramidRotation %= 360;

        cubeRotation += 0.5f;
        cubeRotation %= 360;

        // Now that we're dealing with depth, we have to reset a little more than before,
        // namely the depth buffer that we told OpenGL to support above.
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glLoadIdentity();                          // Reset The Current Modelview Matrix

        GL11.glTranslatef(-1.5f, 0.0f, -6.0f);
        GL11.glRotatef(pyramidRotation, 0.0f, 1.0f, 0.0f);
        GL11.glBegin(GL11.GL_TRIANGLES);
        {
            // You might notice I used brackets to enclose these commands.
            // This doesn't change the effect of the code, but it does cause
            // my IDE to indent it, which makes the begin/end stand out better.
            GL11.glColor3f(1.0f, 0.0f, 0.0f);
            GL11.glVertex3f(0.0f, 1.0f, 0.0f);
            GL11.glColor3f(0.0f, 1.0f, 0.0f);
            GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
            GL11.glColor3f(0.0f, 0.0f, 1.0f);
            GL11.glVertex3f(1.0f, -1.0f, 1.0f);

            // Separating each face with a blank line also helps.
            GL11.glColor3f(1.0f, 0.0f, 0.0f);
            GL11.glVertex3f(0.0f, 1.0f, 0.0f);
            GL11.glColor3f(0.0f, 0.0f, 1.0f);
            GL11.glVertex3f(1.0f, -1.0f, 1.0f);
            GL11.glColor3f(0.0f, 1.0f, 0.0f);
            GL11.glVertex3f(1.0f, -1.0f, -1.0f);

            GL11.glColor3f(1.0f, 0.0f, 0.0f);
            GL11.glVertex3f(0.0f, 1.0f, 0.0f);
            GL11.glColor3f(0.0f, 1.0f, 0.0f);
            GL11.glVertex3f(1.0f, -1.0f, -1.0f);
            GL11.glColor3f(0.0f, 0.0f, 1.0f);
            GL11.glVertex3f(-1.0f, -1.0f, -1.0f);

            GL11.glColor3f(1.0f, 0.0f, 0.0f);
            GL11.glVertex3f(0.0f, 1.0f, 0.0f);
            GL11.glColor3f(0.0f, 0.0f, 1.0f);
            GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
            GL11.glColor3f(0.0f, 1.0f, 0.0f);
            GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
        }
        GL11.glEnd();

        GL11.glLoadIdentity();
        GL11.glTranslatef(1.5f, 0.0f, -7.0f);
        // Notice how we're rotating on an axis that's diagonal to all three.
        // This makes the cube rotation much more appealing.  To visualize how
        // the rotation works along an arbitrary axis, imagine you're skewering
        // the object with a spear along the axis, then turning it on that spear.
        GL11.glRotatef(cubeRotation, 1.0f, 1.0f, 1.0f);
        GL11.glColor3f(0.5f, 0.5f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glColor3f(0.0f, 1.0f, 0.0f);
            GL11.glVertex3f(1.0f, 1.0f, -1.0f);
            GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
            GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
            GL11.glVertex3f(1.0f, 1.0f, 1.0f);

            GL11.glColor3f(1.0f, 0.5f, 0.0f);
            GL11.glVertex3f(1.0f, -1.0f, 1.0f);
            GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
            GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
            GL11.glVertex3f(1.0f, -1.0f, -1.0f);

            GL11.glColor3f(1.0f, 0.0f, 0.0f);
            GL11.glVertex3f(1.0f, 1.0f, 1.0f);
            GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
            GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
            GL11.glVertex3f(1.0f, -1.0f, 1.0f);

            GL11.glColor3f(1.0f, 1.0f, 0.0f);
            GL11.glVertex3f(1.0f, -1.0f, -1.0f);
            GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
            GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
            GL11.glVertex3f(1.0f, 1.0f, -1.0f);

            GL11.glColor3f(0.0f, 0.0f, 1.0f);
            GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
            GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
            GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
            GL11.glVertex3f(-1.0f, -1.0f, 1.0f);

            GL11.glColor3f(1.0f, 0.0f, 1.0f);
            GL11.glVertex3f(1.0f, 1.0f, -1.0f);
            GL11.glVertex3f(1.0f, 1.0f, 1.0f);
            GL11.glVertex3f(1.0f, -1.0f, 1.0f);
            GL11.glVertex3f(1.0f, -1.0f, -1.0f);
        }
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
