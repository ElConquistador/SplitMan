package elcon.games.splitman.settings;

import org.lwjgl.input.Keyboard;

public class KeyBinding {

	public String name;
	public int defaultKey;
	public int key;
	
	public KeyBinding(String name, int key) {
		this.name = name;
		this.defaultKey = key;
		this.key = key;
	}
	
	public boolean isPressed() {
		return Keyboard.isKeyDown(key);
	}
}
