package elcon.games.splitman.tiles;

import java.util.List;

import elcon.games.splitman.Resources;
import elcon.games.splitman.util.BoundingBox;
import elcon.games.splitman.world.World;

public class TileColorPlayer extends Tile {
	
	public TileColorPlayer(int id, String name) {
		super(id, name);
	}
	
	public boolean isVisible(World world, int x, int y) {
		return world.getTileMetadata(x, y) == world.getPlayer().color;
	}

	@Override
	public void addBoundingBoxesToList(World world, int x, int y, List<BoundingBox> list) {
		if(isVisible(world, x, y)) {
			super.addBoundingBoxesToList(world, x, y, list);
		}
	}

	@Override
	public int getTextureIndex(World world, int x, int y) {
		return 0;
	}
	
	@Override
	public int getColor(World world, int x, int y) {
		return Resources.colors[world.getTileMetadata(x, y)];
	}
	
	@Override
	public void render(World world, int x, int y, int offsetX, int offsetY) {
		if(isVisible(world, x, y)) {
			super.render(world, x, y, offsetX, offsetY);
		}
	}
}
