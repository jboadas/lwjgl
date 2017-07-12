package tutorials.nehe;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;

/** In this lesson, we learn about display lists. */

public class Lesson12 {
    private String windowTitle = "NeHe Lesson 12: Display Lists";
    private int windowWidth = 800;
    private int windowHeight = 600;
    private boolean quitRequested = false;

    // Display lists are stored with integer IDs, undifferentiated from any other int, so I use
    // a little something like Hungarian notation on these IDs.  Ideally, you'd keep these IDs
    // encapsulated in a class the way Slick does with Texture, but I leave that as an exercise.
    int dlBox;
    int dlTop;

    float xrot = 0f;
    float yrot = 45.0f;

    float boxcol[][] = {
            {1.0f, 0.0f, 0.0f}, // red
            {1.0f, 0.5f, 0.0f}, // orange
            {1.0f, 1.0f, 0.0f}, // yellow
            {0.0f, 1.0f, 0.0f}, // green
            {0.0f, 1.0f, 1.0f}  // blue
    };

    float topcol[][] = {
            {0.5f, 0.0f, 0.0f},     // dark red
            {0.5f, 0.25f, 0.0f},    // dark orange
            {0.5f, 0.5f, 0.0f},     // dark yelllow
            {0.0f, 0.5f, 0.0f},     // dark green
            {0.0f, 0.5f, 0.5f}      // dark blue
    };

    private String texturePath = "tutorials/nehe/files/Cube.png";
    private Texture cubeTexture;

    public static void main(String[] args) throws Exception {
        Lesson12 app = new Lesson12();
        app.run();
    }

    private void initGL() throws IOException {
        DisplayMode dm = Display.getDisplayMode();
        int w = dm.getWidth();
        int h = dm.getHeight();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(45.0f, (float) w / (float) h, 0.1f, 100.0f);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        cubeTexture = loadPNG(texturePath);
        buildLists();
    }

    private void renderScene() {

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer

        cubeTexture.bind();

        // This draws our pyramid of boxes.  I'm not going to bother explaining the placement and
        // rotations, since you can always tweak the parameters yourself to see what they do.
        for (int row = 1; row < 6; row++) {
            for (int x = 0; x < row; x++) {
                GL11.glLoadIdentity();
                GL11.glTranslatef(1.4f + ((float) (x) * 2.8f) - ((float) (row) * 1.4f),
                        ((6.0f - (float) (row)) * 2.4f) - 7.0f,
                        -20.0f);
                GL11.glRotatef((float) (45 - (2 * row)) + xrot, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(yrot, 0.0f, 1.0f, 0.0f);

                // This draws box bottoms and sides using a display list per cube.
                float[] color = boxcol[row - 1];
                GL11.glColor3f(color[0], color[1], color[2]);
                GL11.glCallList(dlBox);

                // This draws the the box tops with a display list per square.
                color = topcol[row - 1];
                GL11.glColor3f(color[0], color[1], color[2]);
                GL11.glCallList(dlTop);
            }
        }
    }

    void buildLists() {
        // glGenLists(n) allocates n display lists, and returns the integer ID of the first display
        // list allocated.  The remaining display lists are guaranteed to have IDs that increase
        // by 1 for each list.  We could have just generated these lists independently and not paid
        // any real price for it in this, but we're demonstrating how it works.
        dlBox = GL11.glGenLists(2);

        // glNewList takes a display list ID (also called a "name", even though it's just a number)
        // and a mode that can be GL_COMPILE or GL_COMPILE_AND_EXECUTE.  You can think of a display
        // list as recording a series of commands to be played back later -- or if you use the mode
        // GL_COMPILE_AND_EXECUTE, executed now and recorded for later.  There are a lot of commands
        // that can't be used in a display list, but basic coordinate commands like these are fine.
        GL11.glNewList(dlBox, GL11.GL_COMPILE);
        {
            // I use the indentation trick here for the display list, but I'm not going to clutter
            // things up with another level of indent for the glBegin/glEnd pair...
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glNormal3f(0.0f, -1.0f, 0.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(1.0f, -1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(-1.0f, -1.0f, 1.0f);

            GL11.glNormal3f(0.0f, 0.0f, 1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(1.0f, -1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(-1.0f, 1.0f, 1.0f);

            GL11.glNormal3f(0.0f, 0.0f, -1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(1.0f, 1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(1.0f, -1.0f, -1.0f);

            GL11.glNormal3f(1.0f, 0.0f, 0.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(1.0f, 1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(1.0f, -1.0f, 1.0f);

            GL11.glNormal3f(-1.0f, 0.0f, 0.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
            GL11.glEnd();
        }
        GL11.glEndList();

        // Display list for the top face of the cube.  It's going to be drawn in a different color,
        // so we need to separate it out.  You can use glColor in a display list, but we pick a
        // different color for every row, so we don't set it in either of these lists.
        dlTop = dlBox + 1;
        GL11.glNewList(dlTop, GL11.GL_COMPILE);
        {
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(1.0f, 1.0f, -1.0f);
            GL11.glEnd();
        }
        GL11.glEndList();
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


    private Texture loadPNG(String path) throws IOException {
        return TextureLoader.getTexture(
                "PNG", ResourceLoader.getResourceAsStream(path), true
        );
    }

    /** Reads queued keyboard events and takes appropriate action on them. */
    private void handleInput() throws LWJGLException {
        if (Display.isCloseRequested()) {
            // The display window is being closed
            quitRequested = true;
            return;
        }

        // Held events
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
            xrot -= 1;

        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
            xrot += 1;

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
            yrot -= 1;

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
            yrot += 1;

        // Press events
        while (Keyboard.next()) {
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
