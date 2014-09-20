package elcon.games.splitman.entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import elcon.games.splitman.settings.Settings;
import elcon.games.splitman.tiles.Tile;
import elcon.games.splitman.util.BoundingBox;
import elcon.games.splitman.world.World;

public class Entity {

	public World world;

	public double x;
	public double y;
	public double vx;
	public double vy;
	public double ax;
	public double ay;
	public double friction;
	public double gravity;

	public double speed = 1.0;

	public int sizeX = Tile.SIZE;
	public int sizeY = Tile.SIZE;

	public BoundingBox boundingBox;

	public boolean isDead = false;
	public boolean noClip = false;
	public boolean onGround = false;
	public boolean isFalling = false;
	public boolean collisionX = false;
	public boolean collisionY = false;

	public Entity(World world) {
		this.world = world;
	}

	public Entity(World world, double x, double y) {
		this(world);
		this.x = x;
		this.y = y;
		ax = 0;
		ay = 0;
		gravity = 4;
		boundingBox = new BoundingBox(x, x + sizeX, y, y + sizeY);
	}

	public Entity(World world, double x, double y, double speed, double direction) {
		this(world, x, y);
		setSpeed(speed);
		setDirection(direction);
	}

	public void update(int tick) {
		vx += ax;
		vy += ay;
		ax *= friction;
		ay *= friction;
		if(ax < 0.01) {
			ax = 0;
		}
		if(ay < 0.01) {
			ay = 0;
		}
		vy += gravity;
		isFalling = vy > 0;
		
		if(!noClip) {
			if(collisionX) {
				collisionX = false;
				collisionY = false;
				collideX();
				collideY();
			} else if(collisionY) {
				collisionX = false;
				collisionY = false;
				collideY();
				collideX();
			} else {
				collideY();
				collideX();
			}
		}	
		
		if(vy < 0) {
			onGround = false;
		}
		if(isFalling && vy == 0) {
			onGround = true;
		}
		x += vx;
		y += vy;
		boundingBox.minX = x;
		boundingBox.maxX = x + sizeX;
		boundingBox.minY = y;
		boundingBox.maxY = y + sizeY;
		
		if(x < 0) {
			x = 0;
			vx = 0;
			boundingBox.minX = x;
			boundingBox.maxX = x + sizeX;
		} else if(x > world.sizeX * Tile.SIZE - sizeX) {
			x = world.sizeX * Tile.SIZE - sizeX;
			vx = 0;
			boundingBox.minX = x;
			boundingBox.maxX = x + sizeX;
		}
		if(y < 0) {
			y = 0;
			vx = 0;
			boundingBox.minY = y;
			boundingBox.maxY = y + sizeY;
		} else if(y > world.sizeY * Tile.SIZE - sizeY) {
			y = world.sizeY * Tile.SIZE - sizeY;
			vx = 0;
			boundingBox.minY = y;
			boundingBox.maxY = y + sizeY;
		}
	}
	
	public void collideX() {
		BoundingBox newBoundingBox = boundingBox.add(vx, vy);
		if(vx != 0) {
			boolean done = false;
			int minBX = (int) ((x + vx) / Tile.SIZE);
			int maxBX = (int) ((x + vx + sizeX - 1) / Tile.SIZE);
			int minBY = (int) ((y + vy) / Tile.SIZE);
			int maxBY = (int) ((y + vy + sizeY - 1) / Tile.SIZE);
			double mx = vx;
			double oldMX = mx;
			if(minBX < 0 || maxBX >= world.sizeX) {
				mx = 0;
				done = true;
			}
			if(minBY < 0 || maxBY >= world.sizeY) {
				vy = 0;
				done = true;
			}
			if(!done) {
				List<BoundingBox> boxes = new ArrayList<BoundingBox>();
				for(int x = vx > 0 ? maxBX : minBX; vx > 0 ? x >= minBX : x <= maxBX; x += (vx > 0 ? -1 : 1)) {
					for(int y = vy > 0 ? maxBY : minBY; vy > 0 ? y >= minBY : y <= maxBY; y += (vy > 0 ? -1 : 1)) {
						boxes.clear();
						world.getTile(x, y).addBoundingBoxesToList(world, x, y, boxes);
						for(BoundingBox box : boxes) {
							if(box != null && box.intersects(newBoundingBox)) {
								oldMX = mx;
								mx = collideX(newBoundingBox, box, mx);
								if(mx != oldMX) {
									collisionX = true;
									onCollide(x, y);
									world.getTile(x, y).onCollide(world, x, y, this);
								}
							}
						}
					}
				}
				List<Entity> entities = world.getEntitiesInRange(getCenterX(), getCenterY(), Math.max(sizeX, sizeY));
				entities.remove(this);
				for(Entity entity : entities) {
					if(entity.boundingBox != null && entity.boundingBox.intersects(newBoundingBox)) {
						onCollide(entity);
						entity.onCollide(this);
						/*oldMX = mx;
						mx = collideX(newBoundingBox, entity.boundingBox, mx);
						if(mx != oldMX) {
							collisionX = true;
							onCollide(entity);
							entity.onCollide(this);
						}*/
					}
				}
			}
			vx = mx;
		}
	}
	
	public double collideX(BoundingBox box1, BoundingBox box2, double mx) {
		if(mx < 0) {
			mx += Math.max(-mx, Math.min(Tile.SIZE, box1.minX - box2.maxX));
		} else if(mx > 0) {
			mx -= Math.min(mx, Math.min(Tile.SIZE, box1.maxX - box2.minX));
		}
		return mx;
	}
	
	public void collideY() {
		BoundingBox newBoundingBox = boundingBox.add(vx, vy);
		if(vy != 0) {
			boolean done = false;
			int minBX = (int) ((x + vx) / Tile.SIZE);
			int maxBX = (int) ((x + vx + sizeX - 1) / Tile.SIZE);
			int minBY = (int) ((y + vy) / Tile.SIZE);
			int maxBY = (int) ((y + vy + sizeY - 1) / Tile.SIZE);
			double my = vy;
			double oldMY = my;
			if(minBX < 0 || maxBX >= world.sizeX) {
				vx = 0;
				done = true;
			}
			if(minBY < 0 || maxBY >= world.sizeY) {
				my = 0;
				done = true;
			}
			if(!done) {
				List<BoundingBox> boxes = new ArrayList<BoundingBox>();
				for(int x = vx > 0 ? maxBX : minBX; vx > 0 ? x >= minBX : x <= maxBX; x += (vx > 0 ? -1 : 1)) {
					for(int y = vy > 0 ? maxBY : minBY; vy > 0 ? y >= minBY : y <= maxBY; y += (vy > 0 ? -1 : 1)) {
						boxes.clear();
						world.getTile(x, y).addBoundingBoxesToList(world, x, y, boxes);
						for(BoundingBox box : boxes) {
							if(box != null && box.intersects(newBoundingBox)) {
								oldMY = my;
								my = collideY(newBoundingBox, box, my);
								if(my != oldMY) {
									collisionY = true;
									onCollide(x, y);
									world.getTile(x, y).onCollide(world, x, y, this);
								}
							}
						}
					}
				}
				List<Entity> entities = world.getEntitiesInRange(getCenterX(), getCenterY(), Math.max(sizeX, sizeY));
				entities.remove(this);
				for(Entity entity : entities) {
					if(entity.boundingBox != null && entity.boundingBox.intersects(newBoundingBox)) {
						onCollide(entity);
						entity.onCollide(this);
						/*oldMY = my;
						my = collideY(newBoundingBox, entity.boundingBox, my);
						if(my != oldMY) {
							collisionY = true;
							onCollide(entity);
							entity.onCollide(this);
						}*/
					}
				}
			}
			vy = my;
		}
	}
	
	public double collideY(BoundingBox box1, BoundingBox box2, double my) {
		if(my < 0) {
			my += Math.max(-my, Math.min(Tile.SIZE, box1.minY - box2.maxY));
		} else if(my > 0) {
			my -= Math.min(my, Math.min(Tile.SIZE, box1.maxY - box2.minY));
		}
		return my;
	}
	
	public void onCollide(Entity entity) {
		
	}
	
	public void onCollide(int x, int y) {
		
	}

	public void render() {
		GL11.glColor4f(1.0F, 0.0F, 1.0F, 1.0F);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(-world.offsetX + x, -world.offsetY + y);
		GL11.glVertex2d(-world.offsetX + x + sizeX, -world.offsetY + y);
		GL11.glVertex2d(-world.offsetX + x + sizeX, -world.offsetY + y + sizeY);
		GL11.glVertex2d(-world.offsetX + x, -world.offsetY + y + sizeY);
		GL11.glEnd();

		if(Settings.getDebugOption("entityMovement")) {
			GL11.glColor4f(0.75F, 0.0F, 0.75F, 1.0F);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(-world.offsetX + x + vx, -world.offsetY + y + vy);
			GL11.glVertex2d(-world.offsetX + x + vx + sizeX, -world.offsetY + y + vy);
			GL11.glVertex2d(-world.offsetX + x + vx + sizeX, -world.offsetY + y + vy + sizeY);
			GL11.glVertex2d(-world.offsetX + x + vx, -world.offsetY + y + vy + sizeY);
			GL11.glEnd();
		}
	}

	public void setDead() {
		isDead = true;
	}

	public void setSize(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		boundingBox = new BoundingBox(x, y, x + sizeX, y + sizeY);
	}

	public double getSpeed() {
		return Math.sqrt(vx * vx + vy * vy);
	}

	public void setSpeed(double speed) {
		double angle = Math.toRadians(getDirection());
		vx = Math.cos(angle) * speed;
		vy = Math.sin(angle) * speed;
	}

	public double getDirection() {
		return Math.atan2(vy, vx);
	}

	public void setDirection(double angle) {
		angle = Math.toRadians(angle);
		double length = getSpeed();
		vx = Math.cos(angle) * length;
		vy = Math.sin(angle) * length;
	}
	
	public double getCenterX() {
		return x + sizeX / 2;
	}
	
	public double getCenterY() {
		return y + sizeY / 2;
	}
	
	public double getDistance(double x, double y) {
		double deltaX = this.x - x;
		double deltaY = this.y - y;
		return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}
	
	public double getDistance(Entity entity) {
		return getDistance(entity.x + entity.sizeX / 2, entity.y + entity.sizeY / 2);
	}
}
