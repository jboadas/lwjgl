import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.util.glu.GLU.*;

public class Camera {
	
	private static final double PI = 3.1415926535897932384626433832795;
	private float x; 		//position x
	private float y; 		//position y
	private float z; 		//position z
	private float rx; 		//rotation x
	private float ry; 		//rotation y
	private float rz; 		//rotation z
	
	private float fov; 		//field of view
	private float aspect; 	//aspect ratio
	private float near; 	//near clipping rectangle
	private float far; 		//far clipping rectangle
	
	public Camera(float pfov, float paspect, float pnear, float pfar){
		
		this.x = 0f;
		this.y = 0f;
		this.z = 0f;
		this.rx = 0f;
		this.ry = 0f;
		this.rz = 0f;
		
		this.fov = pfov;
		this.aspect = paspect;
		this.near = pnear;
		this.far = pfar;
		initProjetion();
		
	}
	
	private void initProjetion(){
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		perspectiveGL(fov, aspect, near, far);
		//gluPerspective(fov, aspect, near, far); //para no usar glu
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);
		
		//glDepthFunc(GL_LESS);
		
	}

	private void perspectiveGL( double fovY, double aspect, double zNear, double zFar )
	{
	    //  Half of the size of the x and y clipping planes.
	    double fW, fH;
	    //Calculate the distance from 0 of the y clipping plane.
	    //Basically trig to calculate position of clipper at zNear.
	    // Note: tan( double ) uses radians but OpenGL works in degrees so we convert degrees to radians
	    // by dividing by 360 then multiplying by pi.
	    //Formula below corrected by Carsten Jurenz:
	    //fH = tan( (fovY / 2) / 180 * pi ) * zNear;
	    //Which can be reduced to:
	    fH = Math.tan( fovY / 360 * PI ) * zNear;
	    //Calculate the distance from 0 of the x clipping plane based on the aspect ratio.
	    fW = fH * aspect;
	    //Finally call glFrustum, this is all gluPerspective does anyway! This is why we calculate
	    //half the distance between the clipping planes - glFrustum takes an offset from zero for each
	    //clipping planes distance. (Saves 2 divides)
	    glFrustum( -fW, fW, -fH, fH, zNear, zFar );
	}
	
	public void useView(){
		
		glRotatef(rx, 1, 0, 0);
		glRotatef(ry, 0, 1, 0);
		glRotatef(rz, 0, 0, 1);
		glTranslatef(x, y, z);
		
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getRx() {
		return rx;
	}

	public void setRx(float rx) {
		this.rx = rx;
	}

	public float getRy() {
		return ry;
	}

	public void setRy(float ry) {
		this.ry = ry;
	}

	public float getRz() {
		return rz;
	}

	public void setRz(float rz) {
		this.rz = rz;
	}
	
	public void moveZ(float amnt){
		z += amnt;
	}
	
	public void moveXY(float dx, float dy){
		x += dx;
		y += dy;
	}

}
