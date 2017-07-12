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

// In this lesson, we drop the pyramid and add texture to the cube.  This lesson not only
// uses external files (the texture image), it uses an external library to load the texture,
// namely the Slick 2D Game Library.  OpenGL provides very little in the way of texture
// management, and the library that the original tutorial uses is ancient and not part of
// LWJGL regardless.  Slick gives us what we need here.


public class Lesson06 {
    private String windowTitle = "NeHe Lesson 6: Texture Mapping";
    private boolean quitRequested = false;

    private int windowWidth  = 800;
    private int windowHeight = 600;

    // As the previous lesson mentioned, multiple rotations in a row are "stacked" the same
    // way multiple translations affect each other.  Applying these rotations sequentially
    // will produce a chaotic "tumbling" effect.
    private float xrot = 0.0f;
    private float xdelta = 0.3f;

    private float yrot = 0.0f;
    private float ydelta = 0.2f;

    private float zrot = 0.0f;
    private float zdelta = 0.4f;

    // The resources needed are in the files/ directory right beneath this one, but we load
    // them with Slick's ResourceLoader which will find the files rooted on the classpath.
    // The upshot of this is that it Just Works, whether in files or one or several jars.
    private String texturePath = "tutorials/nehe/files/NeHe.png";
    private Texture cubeTexture;

    public static void main(String[] args) throws Exception {
        Lesson06 app = new Lesson06();
        app.run();
    }


    private void initGL() throws IOException {
        // The rest is the same as before
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        DisplayMode dm = Display.getDisplayMode();
        int w = dm.getWidth();
        int h = dm.getHeight();
        GLU.gluPerspective(45.0f, (float) w / (float) h, 0.1f, 100.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // In order to attach texture images to the faces of polygons, we need to tell
        // OpenGL to enable the ability to do so.  And yes, there are 1-dimensional and
        // even 3-dimensional textures (3D textures are part of OpenGL 1.2 and above)
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        // Here is where Slick does the work in loading the image.  The first parameter
        // is the image format to use (Slick supports PNG, JPG, GIF, and TGA).  The second
        // parameter is an InputStream containing the image data, and the third says the image
        // should be flipped vertically.  The flipping is needed because OpenGL expects image
        // data from the bottom up, due to the way texture coordinates work, so unless you
        // store your textures upside down, you'll need to flip them.
        cubeTexture = TextureLoader.getTexture(
                "PNG", ResourceLoader.getResourceAsStream(texturePath), true
        );
        // FYI, it's usually a good idea to do the glEnable(GL_TEXTURE_2D) call before loading or
        // otherwise manipulating textures.  ATI cards in particular (at least in 2011) are known
        // for having troubles with some texture operations if things aren't done in that order.
    }

    private void renderScene() {
        xrot += xdelta;
        xrot %= 360;
        yrot += ydelta;
        yrot %= 360;
        zrot += zdelta;
        zrot %= 360;

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();

        GL11.glTranslatef(0.0f, 0.0f, -5.0f);
        GL11.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(zrot, 0.0f, 0.0f, 1.0f);

        // glBindTexture is how we pick which texture to use.  It takes the texture type,
        // which will almost always be GL_TEXTURE_2D, and the texture ID, which is an opaque
        // integer ID that our texture loader helpfully assigned for us.
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, cubeTexture.getTextureID());
        GL11.glBegin(GL11.GL_QUADS);
        {
            // You'll notice a new command in here, glTexCoord.  This command is always
            // seen paired with glVertex commands, and it says to associate the vertex
            // with a corresponding point on the primitive vertex.  Unlike any 2D image
            // blitting routings you may be used to, every texture in OpenGL has dimensions
            // of 1.0 x 1.0, with (0,0) at the lower left and (1.0,1.0) at the upper right.
            // By binding each of a quad's corners to the corners of the texture, the texture
            // will be stretched to fit the actual shape of the quad.  By giving different
            // texture coordinates that map small quads and triangles to small pieces of a
            // larger texture, you can create amazingly detailed objects this way.  We start
            // out less ambitiously and just map a square texture to six square cube faces.

            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(1.0f, -1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(-1.0f, 1.0f, 1.0f);

            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(1.0f, 1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(1.0f, -1.0f, -1.0f);

            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(1.0f, 1.0f, -1.0f);

            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(1.0f, -1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(-1.0f, -1.0f, 1.0f);

            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(1.0f, 1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(1.0f, -1.0f, 1.0f);

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
