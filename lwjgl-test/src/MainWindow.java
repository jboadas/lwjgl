import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;
import game.Game;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import utilities.Logg;

public class MainWindow {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static boolean running = false;
    public static Game game;

    public static void main(String[] args){
    	initDisplay();
        initOpenGL();
        initGame();
        resize();
        //Logg.capabilities();
        gameLoop();
        dispose();
    }

    private static void initGame() {
		game = new Game();
	}

    public static void exit() {
        running = false;
    }


    private static void initDisplay(){
        try {
			Display.setTitle("Display example"); //title of our window
			Display.setResizable(true); //whether our window is resizable
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT)); //resolution of our display
			Display.setVSyncEnabled(true); //whether hardware VSync is enabled
			Display.setFullscreen(false); //whether fullscreen is enabled
			Display.create();
			Keyboard.create();
		} catch (LWJGLException e) {
			Logg.error("Fallo la inicilizacion del Display");
			e.printStackTrace();
		}
    }
    
    private static void initOpenGL() {
    	try {
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, Display.getWidth(), 0, Display.getHeight(), -1, 1);
			glMatrixMode(GL_MODELVIEW);
			glClearColor(0f, 0f, 0f, 1f);
			glDisable(GL_DEPTH_TEST);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		} catch (Exception e) {
			Logg.error("Fallo la inicilizacion de OpenGL");
			e.printStackTrace();
		}
    }

    private static void getInput(){
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			exit();
		}
    	game.getInput();
    }
    
    private static void update(){
    	game.update();
    }
    
    private static void render() {
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();
        game.render();
        Display.update();
        Display.sync(60);
    }

    private static void resize() {
        
    	int w = Display.getWidth();
        int h = Display.getHeight();
        
        game.resize(w,h);
    	glViewport(0, 0, w, h);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, w, 0, h, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity ();        
    }

    private static void dispose() {
        Display.destroy();
        Keyboard.destroy();
    }

    private static void gameLoop(){
        
    	running = true;
        while (running && !Display.isCloseRequested()) {
            if (Display.wasResized()){
                resize();
            }
            getInput();
            update();
            render();
        }
        
        dispose();
    }
}