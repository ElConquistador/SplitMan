package elcon.games.splitman.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import elcon.games.splitman.Resources;
import elcon.games.splitman.entities.EntityPlayer;
import elcon.games.splitman.tiles.Tile;
import elcon.games.splitman.util.Edge;
import elcon.games.splitman.util.Vector;
import elcon.games.splitman.world.World;

public class GuiGame extends GuiScreen {

	public World world;
	public Vector light;
	public Vector intersection;
	public Edge ray;
	public List<Vector> points;

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
		//TODO: change back to colorPlayer
		world.setTileAndMetadata(14, 28, Tile.color, 1);
		world.setTileAndMetadata(15, 28, Tile.color, 1);
		world.setTileAndMetadata(16, 28, Tile.color, 2);
		world.setTileAndMetadata(17, 28, Tile.color, 2);
		world.setTileAndMetadata(19, 28, Tile.color, 3);
		world.setTileAndMetadata(20, 28, Tile.color, 3);
		world.setTileAndMetadata(18, 31, Tile.splitter, (1 + world.random.nextInt(Resources.colors.length - 1)) * 2);
		world.setTileAndMetadata(24, 31, Tile.bounce, 0);
		world.setTileAndMetadata(25, 31, Tile.bounce, 0);
		world.setTileAndMetadata(22, 26, Tile.color, 0);
		world.setTileAndMetadata(23, 26, Tile.color, 0);
		world.addPlayer(new EntityPlayer(world, 64, 64, 0).setControlled());
		
		light = new Vector(10 * Tile.SIZE, 10 * Tile.SIZE);
		points = world.rayCast(light);
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
		/*ray = new Edge(new Vector(light.x, light.y), world.getRayEnd(light, new Vector(world.offsetX + Mouse.getX(), world.offsetY + height - Mouse.getY()), world.sizeX * Tile.SIZE, world.sizeY * Tile.SIZE));
		intersection = world.getRayIntersection(ray);
		if(intersection != null) {
			GL11.glPushMatrix();
			GL11.glBegin(GL11.GL_LINES);
			GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
			GL11.glVertex2d(-world.offsetX + light.x, -world.offsetY + light.y);
			GL11.glVertex2d(-world.offsetX + ray.end.x, -world.offsetY + ray.end.y);
			GL11.glEnd();
			
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
			double radius = 4.0;
			for(double angle = 0.0; angle < 360.0; angle += 4.0) {
				GL11.glVertex2d(-world.offsetX + intersection.x + Math.sin(angle) * radius, -world.offsetY + intersection.y + Math.cos(angle) * radius);
			}
			GL11.glEnd();
			GL11.glPopMatrix();
		}*/
		if(points != null) {
			/*GL11.glPushMatrix();
			GL11.glBegin(GL11.GL_POLYGON);
			GL11.glColor4f(1.0F, 0.0F, 1.0F, 1.0F);
			for(Vector point : points) {
				GL11.glVertex2d(-world.offsetX + point.x, -world.offsetY + point.y);
			}
			GL11.glEnd();
			GL11.glPopMatrix();*/
			
			for(Vector point : points) {
				GL11.glPushMatrix();
				GL11.glBegin(GL11.GL_LINES);
				GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
				GL11.glVertex2d(-world.offsetX + light.x, -world.offsetY + light.y);
				GL11.glVertex2d(-world.offsetX + point.x, -world.offsetY + point.y);
				GL11.glEnd();
				GL11.glPopMatrix();
			}
		}
	}

	@Override
	public void stop() {
		world = null;
	}
}
