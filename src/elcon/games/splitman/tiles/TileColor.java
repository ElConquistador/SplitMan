package elcon.games.splitman.tiles;

import elcon.games.splitman.Resources;
import elcon.games.splitman.world.World;

public class TileColor extends Tile {
	
	public TileColor(int id, String name) {
		super(id, name);
	}
	
	@Override
	public int getTextureIndex(World world, int x, int y) {
		return 0;
	}
	
	@Override
	public int getColor(World world, int x, int y) {
		return Resources.colors[world.getTileMetadata(x, y)];
	}
}
