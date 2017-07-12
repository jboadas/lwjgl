package utilities;

import static org.lwjgl.opengl.GL11.GL_EXTENSIONS;
import static org.lwjgl.opengl.GL11.GL_MAX_TEXTURE_SIZE;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glGetString;


public class Logg {
	
	public static void info(String msg){
		System.out.println("INFO : " + msg);
	}
	
	public static void warning(String msg){
		System.out.println("Warning : " + msg);
	}
	
	public static void error(String msg){
		System.out.println("***** ERROR ***** : " + msg);
	}

	public static void capabilities(){
    	System.out.println("OpenGL Version : " + glGetString(GL_VERSION));
    	System.out.println("OpenGL Max Texture Size : " + glGetInteger(GL_MAX_TEXTURE_SIZE));
    	System.out.println("OpenGL Vendor : " + glGetString(GL_VENDOR));
    	System.out.println("OpenGL Renderer : " + glGetString(GL_RENDERER));
    	System.out.println("----------------------------");
        
        String extensions = glGetString(GL_EXTENSIONS);
        String[] extArr = extensions.split(" ");
        for (int i = 0; i < extArr.length; i++) {
            System.out.println(extArr[i]);
		}
    }
	
	
}
