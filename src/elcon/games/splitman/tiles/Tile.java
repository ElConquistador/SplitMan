package elcon.games.splitman.tiles;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import elcon.games.splitman.Resources;
import elcon.games.splitman.entities.Entity;
import elcon.games.splitman.util.BoundingBox;
import elcon.games.splitman.util.Util;
import elcon.games.splitman.world.World;

public class Tile {

	public static final int SIZE = 32;

	public static Tile[] tiles = new Tile[256];

	public static Tile air = new Tile(0, "air").setVisible(false).setSolid(false);
	public static Tile color = new TileColor(1, "color");
	public static Tile splitter = new TileSplitter(2, "splitter");
	public static Tile colorPlayer = new TileColorPlayer(3, "colorPlayer");

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

	public void addBoundingBoxesToList(World world, int x, int y, List<BoundingBox> list) {
		if(solid) {
			list.add(boundingBox.add(x * SIZE, y * SIZE));
		}
	}
	
	public void onCollide(World world, int x, int y, Entity entity) {
		
	}
	
	public int getTextureIndex(World world, int x, int y) {
		return 0;
	}
	
	public int getTextureCoordX(World world, int x, int y) {
		return getTextureIndex(world, x, y) % 8;
	}
	
	public int getTextureCoordY(World world, int x, int y) {
		return getTextureIndex(world, x, y) / 8;
	}
	
	public int getColor(World world, int x, int y) {
		return 0xFFFFFF;
	}

	public void render(World world, int x, int y, int offsetX, int offsetY) {
		if(isVisible(world, x, y)) {
			ArrayList<BoundingBox> list = new ArrayList<BoundingBox>();
			addBoundingBoxesToList(world, x, y, list);
			for(BoundingBox box : list) {
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				Resources.tiles.bind();
				Util.color(getColor(world, x, y));
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2d(getTextureCoordX(world, x, y) * (Resources.tiles.getWidth() / 8), getTextureCoordY(world, x, y) * (Resources.tiles.getHeight() / 8));
				GL11.glVertex2d(-offsetX + box.minX, -offsetY + box.minY);
				GL11.glTexCoord2d((getTextureCoordX(world, x, y) + 1) * (Resources.tiles.getWidth() / 8), getTextureCoordY(world, x, y) * (Resources.tiles.getHeight() / 8));
				GL11.glVertex2d(-offsetX + box.maxX, -offsetY + box.minY);
				GL11.glTexCoord2d((getTextureCoordX(world, x, y) + 1) * (Resources.tiles.getWidth() / 8), (getTextureCoordY(world, x, y) + 1) * (Resources.tiles.getHeight() / 8));
				GL11.glVertex2d(-offsetX + box.maxX, -offsetY + box.maxY);
				GL11.glTexCoord2d(getTextureCoordX(world, x, y) * (Resources.tiles.getWidth() / 8), (getTextureCoordY(world, x, y) + 1) * (Resources.tiles.getHeight() / 8));
				GL11.glVertex2d(-offsetX + box.minX, -offsetY + box.maxY);
				GL11.glEnd();
				GL11.glDisable(GL11.GL_TEXTURE_2D);
			}
		}
	}
}
