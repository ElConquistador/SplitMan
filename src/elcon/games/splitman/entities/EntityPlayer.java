package elcon.games.splitman.entities;

import org.lwjgl.opengl.GL11;

import elcon.games.splitman.SplitMan;
import elcon.games.splitman.settings.Settings;
import elcon.games.splitman.util.Util;
import elcon.games.splitman.world.World;

public class EntityPlayer extends Entity {

	public static final int[] COLORS = new int[]{0xFF0000, 0x0000FF, 0x00FF00, 0xFFFF00};
	
	public int color;
	
	public EntityPlayer(World world, double x, double y, int color) {
		super(world, x, y);
		this.color = color;
		
		speed = 8.0;
		setSize(32, 64);
	}
	
	@Override
	public void update() {
		if(Settings.moveLeft.isPressed()) {
			vx = -speed;
		} else if(Settings.moveRight.isPressed()) {
			vx = speed;
		} else {
			vx = 0;
		}
		if(Settings.jump.isPressed() && onGround) {
			vy = -speed * 4;
		}
		/*if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			vy = -speed;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			vy = speed;
		} else {
			vy = 0;
		}*/
		super.update();
	}
	
	@Override
	public void render() {
		Util.color(COLORS[color]);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(-world.offsetX + x, -world.offsetY + y);
		GL11.glVertex2d(-world.offsetX + x + sizeX, -world.offsetY + y);
		GL11.glVertex2d(-world.offsetX + x + sizeX, -world.offsetY + y + sizeY);
		GL11.glVertex2d(-world.offsetX + x, -world.offsetY + y + sizeY);
		GL11.glEnd();
		
		if(SplitMan.DEBUG) {
			GL11.glColor4f(0.0F, 1.0F, 0.0F, 1.0F);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(-world.offsetX + x + vx, -world.offsetY + y + vy);
			GL11.glVertex2d(-world.offsetX + x + vx + sizeX, -world.offsetY + y + vy);
			GL11.glVertex2d(-world.offsetX + x + vx + sizeX, -world.offsetY + y + vy + sizeY);
			GL11.glVertex2d(-world.offsetX + x + vx, -world.offsetY + y + vy  + sizeY);
			GL11.glEnd();
		}
	}
}
