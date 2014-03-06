package elcon.games.splitman.tiles;

import org.lwjgl.opengl.GL11;

import elcon.games.splitman.world.World;

public class Tile {

	public static final int SIZE = 32;

	public static Tile[] tiles = new Tile[256];

	public static Tile air = new Tile(0, "air").setVisible(false).setSolid(false);
	public static Tile test = new Tile(1, "test");

	public byte id;
	public String name;

	public boolean visible = true;
	public boolean solid = true;

	public Tile(int id, String name) {
		this.id = (byte) id;
		this.name = name;

		tiles[id] = this;
	}

	public Tile setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}

	public Tile setSolid(boolean solid) {
		this.solid = solid;
		return this;
	}

	public boolean canUpdate() {
		return false;
	}

	public void update(World world, int x, int y) {

	}

	public boolean isVisible(World world, int x, int y) {
		return visible;
	}

	public void render(World world, int x, int y, int offsetX, int offsetY) {
		if(isVisible(world, x, y)) {
			GL11.glColor4f(1.0F, world.getTileMetadata(x, y) == 1 ? 1.0F : 0.0F, 1.0F, 1.0F);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(-offsetX + x * SIZE, -offsetY + y * SIZE);
			GL11.glVertex2d(-offsetX + x * SIZE + SIZE, -offsetY + y * SIZE);
			GL11.glVertex2d(-offsetX + x * SIZE + SIZE, -offsetY + y * SIZE + SIZE);
			GL11.glVertex2d(-offsetX + x * SIZE, -offsetY + y * SIZE + SIZE);
			GL11.glEnd();
		}
	}
}
