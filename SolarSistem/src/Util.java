

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Util {
	
	public static Texture loadTexture(String filename){
		
		
		try {
			return TextureLoader.getTexture("png", new FileInputStream(new File("res/"+filename+".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

}
