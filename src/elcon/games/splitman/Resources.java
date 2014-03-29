package elcon.games.splitman;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Resources {

	public static int[] colors = new int[]{0xFFFFFF, 0xFF0000, 0x00FF00, 0x0000FF, 0xFFFF00, 0xFF00FF, 0x00FFFF};
	
	public static Texture tiles;
	
	public static void load() {
		try {
			tiles = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("textures/tiles.png"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
