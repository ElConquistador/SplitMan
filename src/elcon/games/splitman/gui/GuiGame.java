package elcon.games.splitman.gui;

import org.lwjgl.opengl.GL11;

public class GuiGame extends GuiScreen {

	@Override
	public void start() {
		
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	public void render() {
		GL11.glColor4f(1.0F, 0.0F, 1.0F, 1.0F);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(width / 2 - 100, height / 2 - 100);
			GL11.glVertex2i(width / 2 + 100, height / 2 - 100);
			GL11.glVertex2i(width / 2 + 100, height / 2 + 100);
			GL11.glVertex2i(width / 2 - 100, height / 2 + 100);
		GL11.glEnd();
	}
	
	@Override
	public void stop() {
		
	}
}
