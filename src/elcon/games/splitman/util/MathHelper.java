package elcon.games.splitman.util;

public class MathHelper {

	public double normalize(double value, double min, double max) {
		return (value - min) / (max - min);
	}
	
	public double lerp(double value, double min, double max) {
		return (max - min) * value + min;
	}
	
	public double map(double value, double srcMin, double srcMax, double destMin, double destMax) {
		return lerp(normalize(value, srcMin, srcMax), destMin, destMax);
	}
	
	public double clamp(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}
}
