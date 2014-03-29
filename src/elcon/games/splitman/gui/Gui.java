package elcon.games.splitman.gui;

import org.lwjgl.opengl.GL11;

public class Gui {

	public void renderLine(int x1, int y1, int x2, int y2) {
		GL11.glBegin(GL11.GL_LINE);
		GL11.glVertex2i(x1, y1);
		GL11.glVertex2i(x2, y2);
		GL11.glEnd();
	}

	public void renderRectangle(int x, int y, int width, int height) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2i(x, y);
		GL11.glVertex2i(x + width, y);
		GL11.glVertex2i(x + width, y + height);
		GL11.glVertex2i(x, y + height);
		GL11.glEnd();
	}

	public void renderTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex2i(x1, y1);
		GL11.glVertex2i(x2, y2);
		GL11.glVertex2i(x3, y3);
		GL11.glEnd();
	}
}
