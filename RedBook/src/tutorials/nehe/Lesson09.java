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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * We finally ditch the cube in the ninth lesson.
 * <p/>
 * This lesson follows the original NeHe code fairly closely, odd quirks and all.  I do take one
 * liberty, however, and draw the stars at 75% alpha instead of 100%, which makes the twinkling
 * effect slightly more pronounced.
 */

public class Lesson09 {
    private String windowTitle = "NeHe Lesson 9: Moving Bitmaps In 3D Space";
    private int windowWidth = 800;
    private int windowHeight = 600;
    private boolean quitRequested = false;

    float zoom = -15.0f;
    float tilt = 90.0f;
    float spin;

    private String texturePath = "tutorials/nehe/files/Star.png";
    private Texture starTexture;

    private boolean twinkle = false;
    public static final int NUM_STARS = 50;

    private List<Star> stars = new ArrayList<Star>();

    class Star {
        float red;
        float blue;
        float green;
        float distance;
        float angle;

        private Random randGen = new Random();

        public void setColor(float red, float green, float blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        /** Construct a randomly-colored star with the given angle and distance */
        public Star(float angle, float distance) {
            this.angle = angle;
            this.distance = distance;
            setColor(randGen.nextFloat(), randGen.nextFloat(), randGen.nextFloat());
        }
    }

    public static void main(String[] args) throws Exception {
        Lesson09 app = new Lesson09();
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

        initStars();
        starTexture = loadPNG(texturePath);

    }

    /** creates an evenly-distributed line of stars at angle 0 */
    private void initStars() {
        stars.clear();
        int i = 0;
        while (++i <= NUM_STARS) {
            stars.add(new Star(0, (float) i * 5 / NUM_STARS));
        }
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
            tilt -= 0.5f;

        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) 
            tilt += 0.5f;

        if (Keyboard.isKeyDown(Keyboard.KEY_PRIOR))
            zoom += 0.2f;

        if (Keyboard.isKeyDown(Keyboard.KEY_NEXT))
            zoom -= 0.2f; 


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

                    case Keyboard.KEY_T:
                        twinkle = !twinkle;
                        break;
                }
            }
        }
    }

    private void spinStars() {
        for (int i = 0; i < stars.size(); i++) {
            Star star = stars.get(i);
            star.angle += (float) i / NUM_STARS;
            star.distance -= 0.01f;

            if (star.distance < 0.0f) {
                stars.set(i, new Star(star.angle, 5.0f + star.distance));
            }

            spin += 0.01f;
        }
    }

    private void renderScene() {
        spinStars();

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();

        starTexture.bind();
        for (int i = 0; i < stars.size(); i++) {
            Star star = stars.get(i);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0f, 0.0f, zoom);
            GL11.glRotatef(tilt, 1.0f, 0.0f, 0.0f);

            GL11.glRotatef(star.angle, 0.0f, 1.0f, 0.0f);
            GL11.glTranslatef(star.distance, 0.0f, 0.0f);

            GL11.glRotatef(-star.angle, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(-tilt, 1.0f, 0.0f, 0.0f);

            if (twinkle) {
                // This odd code here is so the twinkle background is a different color than the
                // current one, so it "borrows" the color from another star, which will stick
                // until that star gets recycled and recolored, at which point it will take on
                // that new color.  It's clever, though probably not worth the bother...
                Star s2 = stars.get((NUM_STARS - i) - 1);
                GL11.glColor4f(s2.red, s2.green, s2.blue, 0.75f);

                GL11.glBegin(GL11.GL_QUADS);
                {
                    GL11.glTexCoord2f(0.0f, 0.0f);
                    GL11.glVertex3f(-1.0f, -1.0f, 0.0f);
                    GL11.glTexCoord2f(1.0f, 0.0f);
                    GL11.glVertex3f(1.0f, -1.0f, 0.0f);
                    GL11.glTexCoord2f(1.0f, 1.0f);
                    GL11.glVertex3f(1.0f, 1.0f, 0.0f);
                    GL11.glTexCoord2f(0.0f, 1.0f);
                    GL11.glVertex3f(-1.0f, 1.0f, 0.0f);
                }
                GL11.glEnd();
            }

            // Notice that by rotating here, the twinkle color is drawn unrotated.
            // This is deliberately done for effect.
            GL11.glRotatef(spin, 0.0f, 0.0f, 1.0f); // Rotate The Star On The Z Axis

            GL11.glColor4f(star.red, star.green, star.blue, 0.75f);
            GL11.glBegin(GL11.GL_QUADS);
            {
                GL11.glTexCoord2f(0.0f, 0.0f);
                GL11.glVertex3f(-1.0f, -1.0f, 0.0f);
                GL11.glTexCoord2f(1.0f, 0.0f);
                GL11.glVertex3f(1.0f, -1.0f, 0.0f);
                GL11.glTexCoord2f(1.0f, 1.0f);
                GL11.glVertex3f(1.0f, 1.0f, 0.0f);
                GL11.glTexCoord2f(0.0f, 1.0f);
                GL11.glVertex3f(-1.0f, 1.0f, 0.0f);
            }
            GL11.glEnd(); // Done Drawing The Textured Quad

        }
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

    /** Perform final actions to release resources. */
    private void cleanup() {
        Display.destroy();
    }


}
