package elcon.games.splitman.gui;

import elcon.games.splitman.Resources;
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
					world.setTile(i, j, Tile.color);
				}
			}
		}
		world.setTileAndMetadata(14, 28, Tile.colorPlayer, 1);
		world.setTileAndMetadata(15, 28, Tile.colorPlayer, 1);
		world.setTileAndMetadata(16, 28, Tile.colorPlayer, 2);
		world.setTileAndMetadata(17, 28, Tile.colorPlayer, 2);
		world.setTileAndMetadata(19, 28, Tile.colorPlayer, 3);
		world.setTileAndMetadata(20, 28, Tile.colorPlayer, 3);
		world.setTileAndMetadata(18, 31, Tile.splitter, (1 + world.random.nextInt(Resources.colors.length - 1)) * 2);
		world.setTileAndMetadata(24, 31, Tile.bounce, 0);
		world.setTileAndMetadata(25, 31, Tile.bounce, 0);
		world.setTileAndMetadata(22, 26, Tile.color, 0);
		world.setTileAndMetadata(23, 26, Tile.color, 0);
		world.addPlayer(new EntityPlayer(world, 64, 64, 0).setControlled());
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
