package elcon.games.splitman.entities;

import elcon.games.splitman.util.Vector;
import elcon.games.splitman.world.World;

public class Entity {

	public World world;
	
	public Vector position;
	public Vector velocity;
	public Vector gravity;
	
	public double mass = 1.0;
	public double speed = 1.0;
	
	public boolean isDead = false;
	
	public Entity(World world) {
		this.world = world;
	}
	
	public Entity(World world, double x, double y) {
		this(world);
		position = new Vector(x, y);
		velocity = new Vector(0, 0);
		gravity = new Vector(0, 0);
	}
	
	public Entity(World world, double x, double y, double speed, double direction) {
		this(world, x, y);
		velocity.setLength(speed);
		velocity.setAngle(direction);
	}
	
	public void update() {
		velocity.addTo(gravity);
		position.addTo(velocity);
	}
	
	public void render() {
		
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
