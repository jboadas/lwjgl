package example;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
 
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
 
public class HelloLWJGL3 {
 
    // We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback   keyCallback;
 
    // The window handle
    private long window;
 
    // the variable
    private float sp = 0.0f;
    private boolean swapcolor = false;
    
    public void run() {
        System.out.println("Hello LWJGL3 " + Version.getVersion() + "!");
 
        try {
            init();
            loop();
 
            // Release window and window callbacks
            glfwDestroyWindow(window);
            keyCallback.release();
        } finally {
            // Terminate GLFW and release the GLFWErrorCallback
            glfwTerminate();
            errorCallback.release();
        }
    }
 
    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
 
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( glfwInit() != GLFW_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");
 
        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable
 
        int WIDTH = 300;
        int HEIGHT = 300;
 
        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "Hello LWJGL3", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");
 
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, GLFW_TRUE); // We will detect this in our rendering loop
            }
        });
 
        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
            window,
            (vidmode.width() - WIDTH) / 2,
            (vidmode.height() - HEIGHT) / 2
        );
 
        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
 
        // Make the window visible
        glfwShowWindow(window);
    }
 
    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    	System.out.println("----------------------------");
        System.out.println("OpenGL Version : " + glGetString(GL_VERSION));
        System.out.println("OpenGL Max Texture Size : " + glGetInteger(GL_MAX_TEXTURE_SIZE));
    	System.out.println("OpenGL Vendor : " + glGetString(GL_VENDOR));
    	System.out.println("OpenGL Renderer : " + glGetString(GL_RENDERER));
    	System.out.println("OpenGL Extensions supported by your card : ");
    	String extensions =  glGetString(GL_EXTENSIONS);
    	String[] extArr = extensions.split("\\ ");
    	for(int i=0; i<extArr.length; i++)
    	{
    		System.out.println(extArr[i]);
    	}
    	System.out.println("----------------------------");
 
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( glfwWindowShouldClose(window) == GLFW_FALSE ) {
            // Set the clear color
            if(!swapcolor)
            {
                glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
            }
            else
            {
                glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
            }

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            update();
            render();
            glfwSwapBuffers(window); // swap the color buffers
 
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }
    
    private void update()
    {
    	sp = sp+0.001f;
    	if(sp > 1.0f)
    	{
    		sp = 0.0f;
    		swapcolor = !swapcolor;
    	}
    }
    
    private void render()
    {
    	drawQuad();
    }
    
    private void drawQuad()
    {
		if(!swapcolor)
		{
	    	glColor3f(0.0f, 1.0f, 0.0f);
		}
		else
		{
	    	glColor3f(0.0f, 0.0f, 1.0f);
		}
		
		glBegin(GL_QUADS);
		{

			glVertex3f(-sp, -sp, 0.0f);
			glVertex3f(sp, -sp, 0.0f);
			glVertex3f(sp, sp, 0.0f);
			glVertex3f(-sp, sp, 0.0f);
		}
		glEnd();
    	
    }
 
    public static void main(String[] args) {
        new HelloLWJGL3().run();
    }
 
}