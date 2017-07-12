package tutorials.nehe;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.SGISGenerateMipmap;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * The eighth lesson introduces blending, as applied through the use of alpha and depth.  After the
 * whirlwind that was the last lesson, this one is refreshingly incremental.  In this lesson, we're
 * still on the rotating cube (you'll get a break from it soon, I promise), and we keep all the
 * texture filters and lights, but now we're able to control how transparent the texture is.
 */

public class Lesson08 {
    private String windowTitle = "NeHe Lesson 8: Blending";
    private int windowWidth  = 800;
    private int windowHeight = 600;
    private boolean quitRequested = false;

    private boolean lightingEnabled = true;
    private boolean blendingEnabled = false;

    private float xrot = 0.0f;
    private float xdelta = 0.0f;
    private float yrot = 0.0f;
    private float ydelta = 0.0f;
    private float zoom = 0.0f;

    private float[] lightAmbient  = {0.5f, 0.5f, 0.5f, 1.0f};
    private float[] lightDiffuse  = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] lightPosition = {0.0f, 0.0f, 2.0f, 1.0f};

    private int selectedTexture = 0;
    private String texturePath = "tutorials/nehe/files/Glass.png";
    private List<Texture> cubeTextures = new ArrayList<Texture>();

    public static void main(String[] args) throws Exception {
        Lesson08 app = new Lesson08();
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
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);

        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);

        FloatBuffer temp = BufferUtils.createFloatBuffer(4);
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, (FloatBuffer) temp.put(lightAmbient).rewind());
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, (FloatBuffer) temp.put(lightDiffuse).rewind());
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, (FloatBuffer) temp.put(lightPosition).rewind());
        GL11.glEnable(GL11.GL_LIGHT1);

        if (lightingEnabled)
            GL11.glEnable(GL11.GL_LIGHTING);
        else
            GL11.glDisable(GL11.GL_LIGHTING);

        // When lighting is disabled, textures take on color and alpha from the current color.
        // Here, we're asking for all colors of the textureto be drawn at their full intensity, but
        // with 50% alpha, which combined with the glBlendFunc below, will result in translucence.
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);

        // Next we have to tell OpenGL how to blend transparent surfaces.  Blending occurs whenever
        // two polygons are drawn on top of each other.  In order to blend, the color values of
        // both surfaces need to be recomputed to compute a final color, and glBlendFunc chooses
        // from the various functions that make this happen.  Some combinations make you draw
        // polygons in a specific order, some cause interesting special effects.  The default
        // blend function is glBlendFunc(GL_ONE, GL_ZERO) which basically means don't blend at all.
        // A full explanation of blending is outside the scope of this lesson, but definitely play
        // with the values to see what different results you get.
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

        // Notice how GL_BLEND and GL_DEPTH are mutually exclusive: if you think about it, what
        // GL_DEPTH_TEST does is to prevent drawing of one polygon that's completely behind another,
        // but blending means we want to see through for things behind.  What usually happens is
        // that blending will "win", and everything will render properly, but leaving depth testing
        // enabled at best means doing unnecessary work, and at worst means missing geometry.
        if (blendingEnabled) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        } else {
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
        loadTextures();
    }

    /** Reads queued keyboard events and takes appropriate action on them. */
    private void handleInput() throws LWJGLException {
        if (Display.isCloseRequested()) {
            // The display window is being closed
            quitRequested = true;
            return;
        }
        // Level-triggered (held) events
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
            xdelta -= 0.01;
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
            xdelta += 0.01;
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
            ydelta -= 0.01;
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
            ydelta += 0.01;
        if (Keyboard.isKeyDown(Keyboard.KEY_PRIOR)) // pgup
            zoom += 0.01;
        if (Keyboard.isKeyDown(Keyboard.KEY_NEXT)) // pgdown
            zoom -= 0.01;


        // Edge-triggered (press) events
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

                    case Keyboard.KEY_L:
                        lightingEnabled = !lightingEnabled;
                        if (lightingEnabled)
                            GL11.glEnable(GL11.GL_LIGHTING);
                        else
                            GL11.glDisable(GL11.GL_LIGHTING);
                        break;

                    case Keyboard.KEY_B :
                        blendingEnabled = !blendingEnabled;
                        if (blendingEnabled) {
                            GL11.glEnable(GL11.GL_BLEND);
                            GL11.glDisable(GL11.GL_DEPTH_TEST);
                        } else {
                            GL11.glDisable(GL11.GL_BLEND);
                            GL11.glEnable(GL11.GL_DEPTH_TEST);
                        }

                    case Keyboard.KEY_F:
                        selectedTexture = (selectedTexture + 1) % cubeTextures.size();
                }
            }
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

    private void loadTextures() throws IOException {
        // Still a monument to copy and paste since it makes it easier to comment blocks out
        Texture tex;

        // default options, still not enabling it by default
        // tex = loadPNG(texturePath);
        // tex.bind();
        // cubeTextures.add(tex);

        // nearest-neighbor
        tex = loadPNG(texturePath);
        tex.bind();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        cubeTextures.add(tex);

        // linear
        tex = loadPNG(texturePath);
        tex.bind();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        cubeTextures.add(tex);

        // mipmap linear ("trilinear")
        tex = loadPNG(texturePath);
        tex.bind();
        // GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, SGISGenerateMipmap.GL_GENERATE_MIPMAP_SGIS, GL11.GL_TRUE);
        GL11.glHint(SGISGenerateMipmap.GL_GENERATE_MIPMAP_HINT_SGIS, GL11.GL_NICEST);

        cubeTextures.add(tex);

        // anisotropic
        tex = loadPNG(texturePath);
        tex.bind();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, SGISGenerateMipmap.GL_GENERATE_MIPMAP_SGIS, GL11.GL_TRUE);
        GL11.glHint(SGISGenerateMipmap.GL_GENERATE_MIPMAP_HINT_SGIS, GL11.GL_NICEST);
        float max = GL11.glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, max);
        cubeTextures.add(tex);

    }

    private Texture loadPNG(String path) throws IOException {
        return TextureLoader.getTexture(
                "PNG", ResourceLoader.getResourceAsStream(path), true
        );
    }

    private void renderScene() {
        xrot = (xrot + xdelta) % 360;
        yrot = (yrot + ydelta) % 360;

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();

        GL11.glTranslatef(0.0f, 0.0f, -5.0f + zoom);
        GL11.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(yrot, 0.0f, 1.0f, 0.0f);


        cubeTextures.get(selectedTexture).bind();
        GL11.glBegin(GL11.GL_QUADS);
        {
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

            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(1.0f, 1.0f, -1.0f);

            GL11.glNormal3f(0.0f, -1.0f, 0.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(1.0f, -1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(-1.0f, -1.0f, 1.0f);

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
        }
        GL11.glEnd();
    }

    /** Perform final actions to release resources. */
    private void cleanup() {
        Display.destroy();
    }


}
