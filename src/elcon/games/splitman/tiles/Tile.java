package elcon.games.splitman.tiles;

import org.lwjgl.opengl.GL11;

import elcon.games.splitman.util.BoundingBox;
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
	
	public BoundingBox boundingBox;

	public Tile(int id, String name) {
		this.id = (byte) id;
		this.name = name;
		
		setBoundingBox(new BoundingBox(0, SIZE, 0, SIZE));

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
	
	public Tile setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
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
	
	public BoundingBox getBoundingBox(World world, int x, int y) {
		return solid ? boundingBox.add(x * SIZE, y * SIZE) : null;
	}

	public void render(World world, int x, int y, int offsetX, int offsetY) {
		if(isVisible(world, x, y)) {
			BoundingBox boundingBox = getBoundingBox(world, x, y);
			GL11.glColor4f(1.0F, world.getTileMetadata(x, y) == 1 ? 1.0F : 0.0F, 1.0F, 1.0F);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(-offsetX + boundingBox.minX, -offsetY + boundingBox.minY);
			GL11.glVertex2d(-offsetX + boundingBox.maxX, -offsetY + boundingBox.minY);
			GL11.glVertex2d(-offsetX + boundingBox.maxX, -offsetY + boundingBox.maxY);
			GL11.glVertex2d(-offsetX + boundingBox.minX, -offsetY + boundingBox.maxY);
			GL11.glEnd();
		}
	}
}
