package elcon.games.splitman.settings;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;

public class Settings {
	
	public static boolean debug = true;
	public static HashMap<String, Boolean> debugOptions = new HashMap<String, Boolean>();
	
	static {
		debugOptions.put("entityMovement", false);
		debugOptions.put("playerMovement", false);
		debugOptions.put("cameraMovement", false);
	}

	public static KeyBinding moveLeft = new KeyBinding("moveLeft", Keyboard.KEY_A);
	public static KeyBinding moveRight = new KeyBinding("moveRight", Keyboard.KEY_D);
	public static KeyBinding jump = new KeyBinding("jump", Keyboard.KEY_SPACE);
	
	public static boolean getDebugOption(String debugOption) {
		return debug && debugOptions.containsKey(debugOption) && debugOptions.get(debugOption);
	}
}
