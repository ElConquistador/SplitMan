package elcon.games.splitman.entities;

import org.lwjgl.opengl.GL11;

import elcon.games.splitman.settings.Settings;
import elcon.games.splitman.util.Util;
import elcon.games.splitman.world.World;

public class EntityPlayer extends Entity {

	public static final int[] COLORS = new int[]{0xFF0000, 0x0000FF, 0x00FF00, 0xFFFF00};
	
	public int color;
	
	public EntityPlayer(World world, double x, double y, int color) {
		super(world, x, y);
		this.color = color;
		
		speed = 4.0;
	}
	
	@Override
	public void update() {
		super.update();
		if(Settings.moveLeft.isPressed()) {
			velocity.setX(-speed);
		} else if(Settings.moveRight.isPressed()) {
			velocity.setX(speed);
		} else {
			velocity.setX(0);
		}
	}
	
	@Override
	public void render() {
		GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
		Util.color(COLORS[color]);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(getX(), getY());
		GL11.glVertex2d(getX() + 32, getY());
		GL11.glVertex2d(getX() + 32, getY() + 64);
		GL11.glVertex2d(getX(), getY() + 64);
		GL11.glEnd();
	}
}
