package elcon.games.splitman.gui;

import elcon.games.splitman.entities.EntityPlayer;
import elcon.games.splitman.tiles.Tile;
import elcon.games.splitman.world.World;

public class GuiGame extends GuiScreen {

	public World world;

	@Override
	public void start() {
		world = new World();
		for(int i = 0; i < world.sizeX; i++) {
			for(int j = 0; j < world.sizeY; j++) {
				if(i == 0 || i == world.sizeX - 1 || j == 0 || j == world.sizeY - 1) {
					world.setTile(i, j, Tile.test);
					world.setTileMetadata(i, j, world.random.nextInt(2));
				}
			}
		}
		world.addPlayer(new EntityPlayer(world, 64, 64, 0));
	}

	@Override
	public void update(int tick) {
		if(world != null) {
			world.update(tick);
		}
	}

	@Override
	public void render() {
		if(world != null) {
			world.render();
		}
	}

	@Override
	public void stop() {
		world = null;
	}
}
