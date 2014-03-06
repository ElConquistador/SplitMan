package elcon.games.splitman.util;

import java.io.File;

import org.lwjgl.opengl.GL11;

public class Util {

	public static String firstUpperCase(String s) {
		return Character.toString(s.charAt(0)).toUpperCase() + s.substring(1, s.length());
	}

	public static String getFileExtension(File file) {
		return getFileExtension(file.getName());
	}

	public static String getFileExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
		if(i > p) {
			return fileName.substring(i + 1);
		}
		return "";
	}
	
	public static void color(int color) {
		float r = ((color >> 16) & 0xFF) * 255.0F;
		float g = ((color >> 8) & 0xFF) * 255.0F;
		float b = (color & 0xFF) * 255.0F;
		GL11.glColor4f(r, g, b, 1.0F);
	}
}
