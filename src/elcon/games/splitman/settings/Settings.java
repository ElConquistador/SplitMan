package elcon.games.splitman.settings;

import org.lwjgl.input.Keyboard;

public class Settings {

	public static KeyBinding moveLeft = new KeyBinding("moveLeft", Keyboard.KEY_A);
	public static KeyBinding moveRight = new KeyBinding("moveRight", Keyboard.KEY_D);
	public static KeyBinding jump = new KeyBinding("jump", Keyboard.KEY_SPACE);
}
