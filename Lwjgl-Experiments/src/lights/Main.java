package lights;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

public class Main {

	public final int width = 800;
	public final int height = 600;

	public ArrayList<Light> lights = new ArrayList<Light>();
	public ArrayList<Block> blocks = new ArrayList<Block>();


	private void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		for (Light light : lights) {
			glColorMask(false, false, false, false);
			glStencilFunc(GL_ALWAYS, 1, 1);
			glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

			for (Block block : blocks) {
				Vector2f[] vertices = block.getVertices();
				for (int i = 0; i < vertices.length; i++) {
					Vector2f currentVertex = vertices[i];
					Vector2f nextVertex = vertices[(i + 1) % vertices.length];
					Vector2f edge = Vector2f.sub(nextVertex, currentVertex, null);
					Vector2f normal = new Vector2f(edge.getY(), -edge.getX());
					Vector2f lightToCurrent = Vector2f.sub(currentVertex, light.location, null);
					if (Vector2f.dot(normal, lightToCurrent) > 0) {
						Vector2f point1 = Vector2f.add(currentVertex, (Vector2f) Vector2f.sub(currentVertex, light.location, null).scale(800), null);
						Vector2f point2 = Vector2f.add(nextVertex, (Vector2f) Vector2f.sub(nextVertex, light.location, null).scale(800), null);
						glBegin(GL_QUADS); {
							glVertex2f(currentVertex.getX(), currentVertex.getY());
							glVertex2f(point1.getX(), point1.getY());
							glVertex2f(point2.getX(), point2.getY());
							glVertex2f(nextVertex.getX(), nextVertex.getY());
						} glEnd();
					}
				}
			}

			glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
			glStencilFunc(GL_EQUAL, 0, 1);
			glColorMask(true, true, true, true);

			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE, GL_ONE);

			glBegin(GL_QUADS); {
				glVertex2f(0, 0);
				glVertex2f(0, height);
				glVertex2f(width, height);
				glVertex2f(width, 0);
			} glEnd();

			glDisable(GL_BLEND);
			glClear(GL_STENCIL_BUFFER_BIT);
		}
		glColor3f(0, 0, 0);
		for (Block block : blocks) {
			glBegin(GL_QUADS); 
			{
				for (Vector2f vertex : block.getVertices()) {
					glVertex2f(vertex.getX(), vertex.getY());
				}
			}
			glEnd();
		}
		Display.update();
		Display.sync(60);
	}

	private void setUpObjects() {
		int lightCount = 5 + (int) (Math.random() * 1);
		int blockCount = 5 + (int) (Math.random() * 1);

		for (int i = 1; i <= lightCount; i++) {
			Vector2f location = new Vector2f((float) Math.random() * width, (float) Math.random() * height);
			lights.add(new Light(location, (float) Math.random() * 10, (float) Math.random() * 10, (float) Math.random() * 10));
		}

		for (int i = 1; i <= blockCount; i++) {
			int width = 50;
			int height = 50;
			int x = (int) (Math.random() * (this.width - width));
			int y = (int) (Math.random() * (this.height - height));
			blocks.add(new Block(x, y, width, height));
		}
	}

	private void initialize() {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("2D Lighting");
			Display.create(new PixelFormat(0, 16, 1));
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		glEnable(GL_STENCIL_TEST);
		glClearColor(0, 0, 0, 0);
	}

	private void cleanup() {

		Display.destroy();
	}

	public static void main(String[] args) {
		Main main = new Main();

		main.setUpObjects();
		main.initialize();

		while (!Display.isCloseRequested()) {
			main.render();
		}

		main.cleanup();
	}
}
