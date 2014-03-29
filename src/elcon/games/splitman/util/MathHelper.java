package elcon.games.splitman.util;

public class MathHelper {

	public static double normalize(double value, double min, double max) {
		return (value - min) / (max - min);
	}
	
	public static double lerp(double value, double min, double max) {
		return (max - min) * value + min;
	}
	
	public static double map(double value, double srcMin, double srcMax, double destMin, double destMax) {
		return lerp(normalize(value, srcMin, srcMax), destMin, destMax);
	}
	
	public static double clamp(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	public static boolean inRange(double value, double min, double max) {
		return value >= Math.min(min, max) && value <= Math.max(min, max);
	}

	public static boolean rangeIntersects(double min1, double max1, double min2, double max2) {
		return Math.max(min1, max1) >= Math.min(min2, max2) && Math.min(min1, max1) <= Math.max(min2, max2);
	}
	
	public static double randomRange(double min, double max) {
		return min + Math.random() * (max - min);
	}

	public static int randomInt(double min, double max) {
		return (int) Math.floor(min + Math.random() * (max - min + 1));
	}
}
