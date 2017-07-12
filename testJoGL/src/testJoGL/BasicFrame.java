package testJoGL;

import java.awt.BorderLayout;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

public class BasicFrame implements GLEventListener{ 
	
	public static float currentTime = 0;
	public static float lastTime = 0;
	public static float fps = 0;
	
	
	@Override public void display(GLAutoDrawable drawable) { 
		
		final GL2 gl = drawable.getGL().getGL2();
		
		//printExtensions(gl);

		gl.glBegin (GL2.GL_LINES);
		//static field 
		gl.glVertex3f(0.50f,-0.50f,0); 
		gl.glVertex3f(-0.50f,0.50f,0); 
		gl.glEnd();	
	}
	
	private void printExtensions(GL2 gl){
		
		System.out.println("OpenGL Version : " + gl.glGetString(GL.GL_VERSION));

		String extensions = gl.glGetString(GL.GL_EXTENSIONS); 
        String[] exts = extensions.split(" ");
        
        System.out.println("Extensions :");
        
        for (int i = 0; i < exts.length; i++) {
			System.out.println(exts[i]);
		}		
	}
	
	
	@Override public void dispose(GLAutoDrawable arg0) {
		//method body 
	}
	@Override public void init(GLAutoDrawable arg0) {
		// method body 
	} @Override public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// method body 
	} 
	
	public static void main(String[] args) { 
		//getting the capabilities object of GL2 profile 
		final GLProfile profile = GLProfile.get(GLProfile.GL2); 
		GLCapabilities capabilities = new GLCapabilities(profile);
		// The canvas 
		final GLCanvas glcanvas = new GLCanvas(capabilities); 
		BasicFrame b = new BasicFrame();
		glcanvas.addGLEventListener(b); 
		glcanvas.setSize(400, 400); 
		//creating frame 
		final JFrame frame = new JFrame (" Basic Frame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(glcanvas, BorderLayout.CENTER);
		//adding canvas to frame 
		//frame.getContentPane().add(glcanvas); 
		frame.setSize(frame.getContentPane().getPreferredSize()); 
		frame.setVisible(true); 
	} 
}