package elcon.games.splitman.world;

import java.util.ArrayList;
import java.util.Random;

import elcon.games.splitman.SplitMan;
import elcon.games.splitman.entities.Entity;
import elcon.games.splitman.entities.EntityPlayer;
import elcon.games.splitman.tiles.Tile;

public class World {
	
	public Random random = new Random();
	
	public int sizeX = 64;
	public int sizeY = 32;
	
	public int offsetX = 0;
	public int offsetY = 0;
	
	public byte[] tileIDs = new byte[sizeX * sizeY];
	public byte[] tileMetadata = new byte[sizeX * sizeY];
	public int tileUpdates = -1;
	
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Entity> unloadedEntities = new ArrayList<Entity>();
	
	public ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();
	
	public void update(int tick) {
		if(tileUpdates > 0) {
			for(int i = 0; i < sizeX; i++) {
				for(int j = 0; j < sizeY; j++) {
					if(getTile(i, j).canUpdate()) {
						getTile(i, j).update(this, i, j);
					}
				}
			}
		} else if(tileUpdates == -1) {
			tileUpdates = 0;
			for(int i = 0; i < sizeX; i++) {
				for(int j = 0; j < sizeY; j++) {
					if(getTile(i, j).canUpdate()) {
						tileUpdates++;
					}
				}
			}
		}
		entities.removeAll(unloadedEntities);
		unloadedEntities.clear();
		if(tick == 30) {
			entities.trimToSize();
		}
		for(Entity entity : entities) {
			if(entity != null) {
				if(entity.isDead) {
					removeEntity(entity);
				} else {
					entity.update();
				}
			}
		}
	}
	
	public void render() {
		int minX = (int) Math.floor(offsetX / Tile.SIZE);
		int maxX = (int) Math.ceil((offsetX + SplitMan.WIDTH) / Tile.SIZE);
		int minY = (int) Math.floor(offsetY / Tile.SIZE);
		int maxY = (int) Math.ceil((offsetY + SplitMan.HEIGHT) / Tile.SIZE);
		for(int i = 0; i < sizeX; i++) {
			for(int j = 0; j < sizeY; j++) {
				if(i >= minX && i <= maxX && j >= minY && j <= maxY) {
					getTile(i, j).render(this, i, j, offsetX, offsetY);
				}
			}
		}
		for(Entity entity : entities) {
			if(entity != null && !entity.isDead) {
				entity.render();
			}
		}
	}
	
	public void setOffsetX(int offsetX) {
		if(offsetX > sizeX * Tile.SIZE - SplitMan.WIDTH) {
			offsetX = sizeX * Tile.SIZE - SplitMan.WIDTH;
		}
		this.offsetX = offsetX;
	}

	public void setOffsetY(int offsetY) {
		if(offsetY > sizeY * Tile.SIZE - SplitMan.HEIGHT) {
			offsetY = sizeY * Tile.SIZE - SplitMan.HEIGHT;
		}
		this.offsetY = offsetY;
	}
	
	public void setOffset(int offsetX, int offsetY) {
		setOffsetX(offsetX);
		setOffsetY(offsetY);
	}
	
	public byte getTileID(int x, int y) {
		return tileIDs[x + y * sizeX];
	}
	
	public void setTileID(int x, int y, int tileID) {
		if(tileID == Tile.air.id && getTileID(x, y) != Tile.air.id && getTile(x, y).canUpdate()) {
			tileUpdates--;
		}
		tileIDs[x + y * sizeX] = (byte) (tileID & 255);
		if(getTileID(x, y) != Tile.air.id && getTile(x, y).canUpdate()) {
			tileUpdates++;
		}
		
	}
	
	public Tile getTile(int x, int y) {
		return Tile.tiles[getTileID(x, y)];
	}
	
	public void setTile(int x, int y, Tile tile) {
		setTileID(x, y, tile.id);
	}
	
	public byte getTileMetadata(int x, int y) {
		return tileMetadata[x + y * sizeX];
	}
	
	public void setTileMetadata(int x, int y, int tileMeta) {
		tileMetadata[x + y * sizeX] = (byte) (tileMeta & 255);
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public void removeEntity(Entity entity) {
		unloadedEntities.add(entity);
	}
	
	public void addPlayer(EntityPlayer player) {
		addEntity(player);
		players.add(player);
	}
	
	public void removePlayer(EntityPlayer player) {
		removeEntity(player);
		players.remove(players);
	}
}
