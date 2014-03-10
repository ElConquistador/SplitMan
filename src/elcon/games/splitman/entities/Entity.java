package elcon.games.splitman.entities;

import org.lwjgl.opengl.GL11;

import elcon.games.splitman.tiles.Tile;
import elcon.games.splitman.util.BoundingBox;
import elcon.games.splitman.util.Vector;
import elcon.games.splitman.world.World;

public class Entity {

	public World world;
	
	public Vector position;
	public Vector velocity;
	public Vector gravity;
	
	public double speed = 1.0;
	
	public int sizeX = Tile.SIZE;
	public int sizeY = Tile.SIZE;
	
	public BoundingBox boundingBox;
	
	public boolean isDead = false;
	public boolean onGround = false;
	
	public Entity(World world) {
		this.world = world;
	}
	
	public Entity(World world, double x, double y) {
		this(world);
		position = new Vector(x, y);
		velocity = new Vector(0, 0);
		gravity = new Vector(0, 4);
		boundingBox = new BoundingBox(x, x + sizeX, y, y + sizeY);
	}
	
	public Entity(World world, double x, double y, double speed, double direction) {
		this(world, x, y);
		velocity.setLength(speed);
		velocity.setAngle(direction);
	}
	
	public void update() {
		velocity.addTo(gravity);
		BoundingBox newBoundingBox = boundingBox.add(velocity);
		if(velocity.getX() != 0 || velocity.getY() != 0) {
			boolean done = false;
			//while(!done) {
				int minBX = (int) ((getX() + velocity.getX()) / Tile.SIZE);
				int maxBX = (int) ((getX() + velocity.getX() + sizeX - 1) / Tile.SIZE);
				int minBY = (int) ((getY() + velocity.getY()) / Tile.SIZE);
				int maxBY = (int) ((getY() + velocity.getY() + sizeY - 1) / Tile.SIZE);
				double mx = velocity.getX();
				double my = velocity.getY();
				if(minBX < 0 || maxBX >= world.sizeX) {
					mx = 0;
					done = true;
				}
				if(minBY < 0 || maxBY >= world.sizeY) {
					my = 0;
					done = true;
				}
				if(!done) {
					System.out.println(mx + " " + my);
					BoundingBox box;
					for(int x = velocity.getX() > 0 ? maxBX : minBX; velocity.getX() > 0 ? x >= minBX : x <= maxBX; x += (velocity.getX() > 0 ? -1 : 1)) {
						for(int y = velocity.getY() > 0 ? maxBY : minBY; velocity.getY() > 0 ? y >= minBY : y <= maxBY; y += (velocity.getY() > 0 ? -1 : 1)) {
							box = world.getTile(x, y).getBoundingBox(world, x, y);
							if(box != null && box.intersects(newBoundingBox)) {
								System.out.println("intersect at " + x + ", " + y);
								if(mx < 0) {
									
								} else if(mx > 0) {
									
								}
								if(my < 0) {
									
								} else if(my > 0) {
									
								}
							}
						}
					}
					done = true;
				}
				velocity.setX(mx);
				velocity.setY(my);
			//}
		}
		/*if(velocity.getX() < 0) {
			
		} else if(velocity.getX() > 0) {
			
		}
		if(velocity.getY() < 0) {
			int minBX = (int) (getX() / Tile.SIZE);
			int maxBX = (int) ((getX() + sizeX - 1) / Tile.SIZE);
			int blockY = (int) ((getY() + velocity.getY() - 1) / Tile.SIZE);
			if(blockY < 0) {
				velocity.setY(0);
			} else {
				int my = (int) velocity.getY();
				BoundingBox box;
				for(int i = minBX; i <= maxBX; i++) {
					box = world.getTile(i, blockY).getBoundingBox(world, i, blockY);
					if(box != null && box.intersects(newBoundingBox)) {
						my = (int) Math.max(my, my - (newBoundingBox.minY - box.maxY));
					}
				}
				velocity.setY(my);
			}
		} else if(velocity.getY() > 0) {
			int minBX = (int) (getX() / Tile.SIZE);
			int maxBX = (int) ((getX() + sizeX - 1) / Tile.SIZE);
			int blockY = (int) ((getY() + velocity.getY() - 1) / Tile.SIZE);
			if(blockY >= world.sizeY) {
				velocity.setY(0);
			} else {
				int my = (int) velocity.getY();
				BoundingBox box;
				for(int i = minBX; i <= maxBX; i++) {
					box = world.getTile(i, blockY).getBoundingBox(world, i, blockY);
					if(box != null && box.intersects(newBoundingBox)) {
						System.out.println("intersect " + my + " | " + newBoundingBox.maxY + " | " + box.minY);
						my = (int) Math.min(my, my - (newBoundingBox.maxY - box.minY));
						System.out.println("    new: " + my);
					}
				}
				velocity.setY(my);
			}
		}*/
		position.addTo(velocity);
		boundingBox.addTo(velocity);
		if(getX() < 0) {
			setX(0);
			velocity.setX(0);
			boundingBox.minX = getX();
			boundingBox.maxX = getX() + sizeX;
		} else if(getX() > world.sizeX * Tile.SIZE - sizeX) {
			setX(world.sizeX * Tile.SIZE - sizeX);
			velocity.setX(0);
			boundingBox.minX = getX();
			boundingBox.maxX = getX() + sizeX;
		}
		if(getY() < 0) {
			setY(0);
			velocity.setY(0);
			boundingBox.minY = getY();
			boundingBox.maxY = getY() + sizeY;
		} else if(getY() > world.sizeY * Tile.SIZE - sizeY) {
			setY(world.sizeY * Tile.SIZE - sizeY);
			velocity.setY(0);
			boundingBox.minY = getY();
			boundingBox.maxY = getY() + sizeY;
		}
	}
	
	public void render() {
		GL11.glColor4f(1.0F, 0.0F, 1.0F, 1.0F);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(-world.offsetX + getX(), -world.offsetY + getY());
		GL11.glVertex2d(-world.offsetX + getX() + sizeX, -world.offsetY + getY());
		GL11.glVertex2d(-world.offsetX + getX() + sizeX, -world.offsetY + getY() + sizeY);
		GL11.glVertex2d(-world.offsetX + getX(), -world.offsetY + getY() + sizeY);
		GL11.glEnd();
	}
	
	public void setDead() {
		isDead = true;
	}
	
	public double getX() {
		return position.getX();
	}
	
	public void setX(double x) {
		position.setX(x);
	}
	
	public double getY() {
		return position.getY();
	}
	
	public void setY(double y) {
		position.setY(y);
	}
}
