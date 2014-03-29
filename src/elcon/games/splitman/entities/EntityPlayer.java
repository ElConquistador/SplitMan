package elcon.games.splitman.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import elcon.games.splitman.Resources;
import elcon.games.splitman.settings.Settings;
import elcon.games.splitman.util.Util;
import elcon.games.splitman.world.World;

public class EntityPlayer extends Entity {
	
	public int color;
	public boolean isControlled = false;
	
	public EntityPlayer(World world, double x, double y, int color) {
		super(world, x, y);
		this.color = color;
		
		speed = 8.0;
		setSize(32, 64);
	}
	
	public EntityPlayer setControlled() {
		isControlled = true;
		return this;
	}
	
	public EntityPlayer setUncontrolled() {
		isControlled = false;
		vx = 0;
		vy = 0;
		return this;
	}
	
	@Override
	public void update(int tick) {
		if(isControlled) {
			if(Settings.moveLeft.isPressed()) {
				vx = -speed;
				world.playerNoMoveTime = 0;
			} else if(Settings.moveRight.isPressed()) {
				vx = speed;
				world.playerNoMoveTime = 0;
			} else {
				vx = 0;
			}
			if(Settings.jump.isPressed() && onGround) {
				vy = -speed * 4;
				world.playerNoMoveTime = 0;
			}
			if(Settings.getDebugOption("playerMovement")) {
				if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
					vy = -speed;
					world.playerNoMoveTime = 0;
				} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
					vy = speed;
					world.playerNoMoveTime = 0;
				}
			}
		}
		super.update(tick);
	}
	
	@Override
	public void render() {
		Util.color(Resources.colors[color]);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(-world.offsetX + x, -world.offsetY + y);
		GL11.glVertex2d(-world.offsetX + x + sizeX, -world.offsetY + y);
		GL11.glVertex2d(-world.offsetX + x + sizeX, -world.offsetY + y + sizeY);
		GL11.glVertex2d(-world.offsetX + x, -world.offsetY + y + sizeY);
		GL11.glEnd();
		
		if(Settings.getDebugOption("entityMovement")) {
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
