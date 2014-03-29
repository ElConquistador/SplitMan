package elcon.games.splitman.tiles;

import elcon.games.splitman.Resources;
import elcon.games.splitman.entities.Entity;
import elcon.games.splitman.entities.EntityPlayer;
import elcon.games.splitman.world.World;

public class TileSplitter extends Tile {

	public TileSplitter(int id, String name) {
		super(id, name);
	}
	
	@Override
	public void onCollide(World world, int x, int y, Entity entity) {
		int meta = world.getTileMetadata(x, y);
		if((meta & 1) == 0) {
			if(entity instanceof EntityPlayer) {
				((EntityPlayer) entity).setUncontrolled();
				EntityPlayer clone = new EntityPlayer(world, entity.x, entity.y, (meta - (meta & 1)) / 2).setControlled();
				world.addPlayer(clone);
				world.currentPlayerIndex = world.players.size() - 1;
				world.setTileMetadata(x, y, meta | 1);
			}
		}
	}
	
	@Override
	public int getColor(World world, int x, int y) {
		int meta = world.getTileMetadata(x, y);
		return Resources.colors[(meta - (meta & 1)) / 2];
	}
}
