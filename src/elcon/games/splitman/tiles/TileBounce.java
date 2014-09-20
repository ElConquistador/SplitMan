package elcon.games.splitman.tiles;

import elcon.games.splitman.entities.Entity;
import elcon.games.splitman.world.World;

public class TileBounce extends Tile {

	public TileBounce(int id, String name) {
		super(id, name);
	}
	
	@Override
	public int getTextureIndex(World world, int x, int y) {
		return 0;
	}
	
	@Override
	public void onCollide(World world, int x, int y, Entity entity) {
		if(entity.y < y * Tile.SIZE && entity.onGround) {
			entity.ay = -48;
		}		
	}
	
	@Override
	public int getColor(World world, int x, int y) {
		return 0xFF00FF;
	}
}
