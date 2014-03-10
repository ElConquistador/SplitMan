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
		
		sizeX = 32;
		sizeY = 64;
		speed = 8.0;
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
		if(Settings.jump.isPressed()) { //&& onGround) {
			velocity.setY(-speed);
		}
	}
	
	@Override
	public void render() {
		Util.color(COLORS[color]);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(-world.offsetX + getX(), -world.offsetY + getY());
		GL11.glVertex2d(-world.offsetX + getX() + sizeX, -world.offsetY + getY());
		GL11.glVertex2d(-world.offsetX + getX() + sizeX, -world.offsetY + getY() + sizeY);
		GL11.glVertex2d(-world.offsetX + getX(), -world.offsetY + getY() + sizeY);
		GL11.glEnd();
	}
}
