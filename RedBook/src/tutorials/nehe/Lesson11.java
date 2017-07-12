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

import static java.lang.Math.sin;

/**
 * Bosco's flag waving tutorial.  There's not a lot in terms of new concepts here, but it does
 * demonstrate using texture coordinates over a smaller piece of a texture, and a very simple mesh
 * that simulates a fabric.  This is a pretty expensive way to do it, but it is an interesting
 * effect regardless, and it's also a great candidate for switching to ever-faster technologies like
 * display lists and VBOs.
 * <p/>
 * I did alter the code slightly, allowing for the mesh parameters to be customized, but it doesn't
 * quite work yet.  Part of the reason is that the algorithm doesn't recompute the heights of each
 * part of the mesh when re-rendering, but uses the faster expedient of computing the heights once
 * then simply moving the heights over by one every time through the loop.  This saves significant
 * CPU time, but it imposes many more constraints than recalculating the sine wave every iteration.
 * I've left any further customization as an unsolved exercise for the reader to finish.
 * <p/>
 * Another small piece I changed is to generally slow down the rotation speed since it's way too
 * fast too fast on modern CPUs (the original code doesn't appear to be framerate-limited).
 */

public class Lesson11 {
    private String windowTitle = "NeHe Lesson 11: Flag Effect (Waving Texture)";
    private int windowWidth = 800;
    private int windowHeight = 600;
    private boolean quitRequested = false;

    // Changing these won't work quite right yet

    // The flag is this many ** 2 squares.  Note that it has to divide evenly into 360 or you
    // will get "kinks" in the flag due to the way the algorithm works.  Changing this doesn't
    // really work yet.
    public final static int FLAG_RESOLUTION = 45;

    // The same algorithm means this also has to divide 360 evenly.  This also isn't really
    // changeable yet.
    public final static float FLAG_DENSITY = 5.0f;

    float points[][][] = new float[FLAG_RESOLUTION][FLAG_RESOLUTION][3];
    int frameCount = 0;
    int waveEveryNFrames = 2;   // corresponds to "wiggle_count" in tutorial

    float xrot;
    float yrot;
    float zrot;

    private String texturePath = "tutorials/nehe/files/Flag.png";
    private Texture flagTexture;

    public static void main(String[] args) throws Exception {
        Lesson11 app = new Lesson11();
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
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_FILL); // Back Face Is Solid
        GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE); // Front Face Is Made Of Lines
        createFlag();
        flagTexture = loadPNG(texturePath);
    }

    private void renderScene() {
        // I've broken more of the renderer out into separate methods now, but it should still be
        // reasonably easy to follow along.
        xrot += 0.15f;
        yrot += 0.1f;
        zrot += 0.2f;

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();

        GL11.glTranslatef(0.0f, 0.0f, -12.0f);
        GL11.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(zrot, 0.0f, 0.0f, 1.0f);

        if (++frameCount % waveEveryNFrames == 0)
            waveFlag();
        drawFlag();

    }

    private void createFlag() {
        float offsetX = (float) -FLAG_RESOLUTION / 10;
        float offsetY = (float) -FLAG_RESOLUTION / 10;
        for (int x = 0; x < FLAG_RESOLUTION; x++) {
            // Height is calculated as a simple sine wave.  This needs some work in order to
            // work with arbitrary flag resolutions and densities.
            float height = (float) sin(Math.toRadians(x / FLAG_DENSITY * 40.0f));
            for (int y = 0; y < FLAG_RESOLUTION; y++) {
                points[x][y][0] = (x / FLAG_DENSITY) + offsetX;
                points[x][y][1] = (y / FLAG_DENSITY) + offsetY;
                points[x][y][2] = height;

            }
        }
    }

    private void waveFlag() {
        // The algorithm here shifts each height value over by a place, wrapping around the edge.
        // This means when our flag was created, the sine wave has to have gone an exact multiple
        // of 360 degrees so as to seamlessly wrap.  If it doesn't, you get a sharp "kink" that
        // will travel through the flag, ruining the effect.  The alternative, and a very acceptable
        // one on modern CPUs, is to simply recreate the flag every iteration.  I've kept the
        // original code here though for fidelity's sake.
        int upper = FLAG_RESOLUTION - 1;
        for (int y = 0; y < FLAG_RESOLUTION; y++) {
            float height = points[0][y][2];
            for (int x = 0; x < upper; x++) {
                points[x][y][2] = points[x + 1][y][2];
            }
            points[upper][y][2] = height;
        }
    }

    private void drawFlag() {
        flagTexture.bind();
        float bound = (float) FLAG_RESOLUTION - 1;
        GL11.glBegin(GL11.GL_QUADS);
        for (int x = 0; x < bound; x++) {
            for (int y = 0; y < bound; y++) {
                // The texture coordinate equivalent to (x,y) is (s,t).
                float s1 = (float) (x) / bound;
                float t1 = (float) (y) / bound;
                float s2 = (float) (x + 1) / bound;
                float t2 = (float) (y + 1) / bound;

                GL11.glTexCoord2f(s1, t1);
                GL11.glVertex3f(points[x][y][0], points[x][y][1], points[x][y][2]);

                GL11.glTexCoord2f(s1, t2);
                GL11.glVertex3f(points[x][y + 1][0], points[x][y + 1][1], points[x][y + 1][2]);

                GL11.glTexCoord2f(s2, t2);
                GL11.glVertex3f(points[x + 1][y + 1][0], points[x + 1][y + 1][1], points[x + 1][y + 1][2]);

                GL11.glTexCoord2f(s2, t1);
                GL11.glVertex3f(points[x + 1][y][0], points[x + 1][y][1], points[x + 1][y][2]);
            }
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
