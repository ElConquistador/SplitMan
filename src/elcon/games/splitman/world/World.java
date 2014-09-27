package elcon.games.splitman.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import elcon.games.splitman.SplitMan;
import elcon.games.splitman.entities.Entity;
import elcon.games.splitman.entities.EntityPlayer;
import elcon.games.splitman.settings.Settings;
import elcon.games.splitman.tiles.Tile;
import elcon.games.splitman.util.BoundingBox;
import elcon.games.splitman.util.Edge;
import elcon.games.splitman.util.Polygon;
import elcon.games.splitman.util.Vector;

public class World {

	public Random random = new Random();

	public int sizeX = 64;
	public int sizeY = 32;

	public int offsetX = 0;
	public int offsetY = 0;

	public byte[] tileIDs = new byte[sizeX * sizeY];
	public byte[] tileMetadata = new byte[sizeX * sizeY];
	public int tileUpdates = -1;

	public ArrayList<Polygon> tileBoxes = new ArrayList<Polygon>();
	
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Entity> newEntities = new ArrayList<Entity>();
	public ArrayList<Entity> unloadedEntities = new ArrayList<Entity>();

	public ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();

	public int currentPlayerIndex = 0;
	public int playerSwitchTime = 0;
	public int nextPlayerSwitchTime = 600 + random.nextInt(1200);
	public int playerNoMoveTime = 0;

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
		entities.addAll(newEntities);
		newEntities.clear();
		if(tick == 30) {
			entities.trimToSize();
		}
		for(Entity entity : entities) {
			if(entity != null) {
				if(entity.isDead) {
					removeEntity(entity);
				} else {
					entity.update(tick);
				}
			}
		}
		playerNoMoveTime++;
		playerSwitchTime++;
		if(playerNoMoveTime >= 300 || playerSwitchTime >= nextPlayerSwitchTime) {
			playerNoMoveTime = 0;
			playerSwitchTime = 0;
			nextPlayerSwitchTime = 600 + random.nextInt(1200);
			if(players.size() > 1) {
				players.get(currentPlayerIndex).setUncontrolled();
				int playerIndex = random.nextInt(players.size());
				if(playerIndex == currentPlayerIndex) {
					if(playerIndex == players.size() - 1) {
						playerIndex--;
					} else {
						playerIndex++;
					}
				}
				currentPlayerIndex = playerIndex;
				players.get(currentPlayerIndex).setControlled();
			} else {

			}
		}
		if(Settings.getDebugOption("cameraMovement")) {
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				setOffsetX(offsetX - 8);
			} else if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				setOffsetX(offsetX + 8);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				setOffsetY(offsetY - 8);
			} else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				setOffsetY(offsetY + 8);
			}
		} else {
			setOffsetX((int) players.get(currentPlayerIndex).x - SplitMan.WIDTH / 2);
			setOffsetY((int) players.get(currentPlayerIndex).y - SplitMan.HEIGHT / 2);
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
		if(offsetX < 0) {
			offsetX = 0;
		} else if(offsetX > sizeX * Tile.SIZE - SplitMan.WIDTH) {
			offsetX = sizeX * Tile.SIZE - SplitMan.WIDTH;
		}
		this.offsetX = offsetX;
	}

	public void setOffsetY(int offsetY) {
		if(offsetY < 0) {
			offsetY = 0;
		} else if(offsetY > sizeY * Tile.SIZE - SplitMan.HEIGHT) {
			offsetY = sizeY * Tile.SIZE - SplitMan.HEIGHT;
		}
		this.offsetY = offsetY;
	}

	public void setOffset(int offsetX, int offsetY) {
		setOffsetX(offsetX);
		setOffsetY(offsetY);
	}

	public boolean isTile(int x, int y) {
		return x >= 0 && x < sizeX && y >= 0 && y < sizeY;
	}

	public byte getTileID(int x, int y) {
		return tileIDs[x + y * sizeX];
	}

	public void setTileID(int x, int y, int tileID) {
		if(tileID == Tile.air.id && getTileID(x, y) != Tile.air.id && getTile(x, y).canUpdate()) {
			tileUpdates--;
		}
		tileIDs[x + y * sizeX] = (byte) (tileID & 255);
		if(getTileID(x, y) != Tile.air.id) {
			Tile tile = getTile(x, y);
			if(tile.canUpdate()) {
				tileUpdates++;
			}			
			ArrayList<BoundingBox> list = new ArrayList<BoundingBox>();
			tile.addBoundingBoxesToList(this, x, y, list);
			for(BoundingBox box : list) {
				tileBoxes.add(new Polygon(box));
			}
			if(tileBoxes.size() > 1) {
				ArrayList<Polygon> removed = new ArrayList<Polygon>();
				for(Polygon polygon : tileBoxes) {
					for(Polygon otherPolygon : tileBoxes) {
						if(polygon.canMerge(otherPolygon)) {
							polygon.merge(otherPolygon);
							removed.add(otherPolygon);
						}
					}
				}
				tileBoxes.removeAll(removed);
			}
		}
	}

	public Tile getTile(int x, int y) {
		return Tile.tiles[getTileID(x, y)];
	}

	public void setTile(int x, int y		/*if(points != null) {
			for(Vector point : points) {
			GL11.glPushMatrix();
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
			GL11.glVertex2d(light.x, light.y);
			GL11.glVertex2d(light.x + 1, light.y);
			GL11.glVertex2d(point.x + 1, point.y);
			GL11.glVertex2d(point.x, point.y);
			GL11.glEnd();
			GL11.glPopMatrix();
		}
	}*/, Tile tile) {
		setTileID(x, y, tile.id);
	}

	public byte getTileMetadata(int x, int y) {
		return tileMetadata[x + y * sizeX];
	}

	public void setTileMetadata(int x, int y, int tileMeta) {
		tileMetadata[x + y * sizeX] = (byte) (tileMeta & 255);
	}

	public void setTileAndMetadata(int x, int y, int tileID, int tileMeta) {
		setTileID(x, y, tileID);
		setTileMetadata(x, y, tileMeta);
	}

	public void setTileAndMetadata(int x, int y, Tile tile, int tileMeta) {
		setTile(x, y, tile);
		setTileMetadata(x, y, tileMeta);
	}

	public List<Entity> getEntitiesInRange(double x, double y, double radius) {
		ArrayList<Entity> list = new ArrayList<Entity>();
		for(Entity entity : entities) {
			if(entity != null && !entity.isDead && entity.getDistance(x, y) <= radius) {
				list.add(entity);
			}
		}
		return list;
	}

	public List<BoundingBox> getEntityBoundingBoxesInRange(double x, double y, double radius) {
		ArrayList<BoundingBox> list = new ArrayList<BoundingBox>();
		for(Entity entity : entities) {
			if(entity != null && !entity.isDead && entity.getDistance(x, y) <= radius) {
				list.add(entity.boundingBox);
			}
		}
		return list;
	}

	public void addEntity(Entity entity) {
		newEntities.add(entity);
	}

	public void removeEntity(Entity entity) {
		unloadedEntities.add(entity);
	}

	public EntityPlayer getPlayer() {
		return players.get(currentPlayerIndex);
	}

	public void addPlayer(EntityPlayer player) {
		addEntity(player);
		players.add(player);
	}

	public void removePlayer(EntityPlayer player) {
		removeEntity(player);
		players.remove(players);
	}

	public List<Vector> rayCast(Vector light) {
		ArrayList<Vector> list = new ArrayList<Vector>();
		for(int i = 0; i < sizeX; i++) {
			for(int j = 0; j < sizeY; j++) {
				if(getTile(i, j).visible) {
					list.add(new Vector(i * Tile.SIZE, j * Tile.SIZE));
					list.add(new Vector(i * Tile.SIZE + Tile.SIZE, j * Tile.SIZE));
					list.add(new Vector(i * Tile.SIZE + Tile.SIZE, j * Tile.SIZE + Tile.SIZE));
					list.add(new Vector(i * Tile.SIZE, j * Tile.SIZE + Tile.SIZE));
				}
			}
		}
		double width = sizeX * Tile.SIZE;
		double height = sizeY * Tile.SIZE;
		//Vector min = new Vector(-0.1, -0.1);
		//Vector max = new Vector(0.1, 0.1);
		Vector vector;
		Vector end = null;
		Edge ray;
		Vector intersection;
		ArrayList<Vector> points = new ArrayList<Vector>();
		for(Vector v : list) {
			//for(int i = 0; i < 2; i++) {
				//vector = v.add(i == 1 ? max : min);
				vector = v;
				end = getRayEnd(light, vector, width, height);
				ray = new Edge(light, end);
				intersection = getRayIntersection(ray);
				if(intersection != null) {
					points.add(intersection);
				} else {
					points.add(ray.end);
				}
			//}
		}
		list.clear();
		list.add(new Vector(0, 0));
		list.add(new Vector(width, 0));
		list.add(new Vector(width, height));
		list.add(new Vector(0, height));
		for(Vector v : list) {
			ray = new Edge(light, v);
			intersection = getRayIntersection(ray);
			if(intersection != null) {
				points.add(intersection);
			}
		}
		
		final Vector center = light;
		Collections.sort(points, new Comparator<Vector>() {
			
			@Override
			public int compare(Vector a, Vector b) {
				if(a.x - center.x >= 0 && b.x - center.x < 0) {
					return 1;
				}
				if(a.x - center.x < 0 && b.x - center.x >= 0) {
					return -1;
				}
				if(a.x - center.x == 0 && b.x - center.x == 0) {
					if(a.y - center.y >= 0 || b.y - center.y >= 0) {
						return 1;
					}
					return -1;
				}
				double det = (a.x - center.x) * (b.y - center.y) - (b.x - center.x) * (a.y - center.y);
				if(det < 0) {
					return 1;
				}
				if(det > 0) {
					return -1;
				}
				double d1 = (a.x - center.x) * (a.x - center.x) + (a.y - center.y) * (a.y - center.y);
				double d2 = (b.x - center.x) * (b.x - center.x) + (b.y - center.y) * (b.y - center.y);
				return (int) (d2 - d1);
			}
		});
		return points;
	}
	
	public Vector getRayEnd(Vector light, Vector vector, double width, double height) {
		Vector end = null;
		if(vector.x == light.x) {
			if(vector.y <= light.y) {
				end = new Vector(light.x, 0);
			} else {
				end = new Vector(light.x, height);
			}
		} else if(vector.y == light.y) {
			if(vector.x <= light.x) {
				end = new Vector(0, light.y);
			} else {
				end = new Vector(width, light.y);
			}
		} else {
			double slope = (vector.y - light.y) / (vector.x - light.x);
			double b = light.y - slope * light.x;
			Vector left = new Vector(0, b);
			Vector right = new Vector(width, slope * width + b);
			Vector top = new Vector(-b / slope, 0);
			Vector bottom = new Vector((height - b) / slope, height);

			if(vector.y <= light.y && vector.x >= light.x) {
				if(top.x >= 0 && top.x <= width) {
					end = top;
				} else {
					end = right;
				}
			} else if(vector.y <= light.y && vector.x <= light.x) {
				if(top.x >= 0 && top.x <= width) {
					end = top;
				} else {
					end = left;
				}
			} else if(vector.y >= light.y && vector.x >= light.x) {
				if(bottom.x >= 0 && bottom.x <= width) {
					end = bottom;
				} else {
					end = right;
				}
			} else if(vector.y >= light.y && vector.x <= light.x) {
				if(bottom.x >= 0 && bottom.x <= width) {
					end = bottom;
				} else {
					end = left;
				}
			}
		}
		return end;
	}

	public Vector getRayIntersection(Edge ray) {
		double closestDistance = Double.MAX_VALUE;
		Vector closest = null;
		double distance;
		List<Vector> intersections;
		Polygon polygon = new Polygon(new Vector(0, 0), new Vector(Tile.SIZE, 0), new Vector(Tile.SIZE, Tile.SIZE), new Vector(0, Tile.SIZE));
		for(int i = 0; i < sizeX; i++) {
			for(int j = 0; j < sizeY; j++) {
				if(getTile(i, j).visible) {
					polygon.translate(new Vector(i * Tile.SIZE, j * Tile.SIZE));
					intersections = polygon.getRayCollisions(ray);
					if(intersections != null) {
						for(Vector vector : intersections) {
							distance = ray.start.getDistance(vector);
							if(distance < closestDistance) {
								closestDistance = distance;
								closest = vector;
							}
						}
					}
					polygon.translate(new Vector(-i * Tile.SIZE, -j * Tile.SIZE));
				}
			}
		}
		return closest;
	}
}
