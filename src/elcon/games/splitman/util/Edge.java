package elcon.games.splitman.util;

public class Edge {

	public Vector start;
	public Vector end;
	
	public Edge(Vector start, Vector end) {
		this.start = start;
		this.end = end;
	}
	
	public double getSlope() {
		double dx = end.x - start.x;
		double dy = end.y - start.y;
		if(dx != 0) {
			return dy / dx;
		}
		return Double.POSITIVE_INFINITY;
	}
	
	public boolean contains(Vector vector) {
		double dx = end.x - start.x;
		double dy = end.y - start.y;
		if(dx == 0) {
			return vector.x == start.x && vector.y >= start.y && vector.y < end.y;
		} else if(dy == 0) {
			return vector.y == start.y && vector.x >= start.x && vector.x < end.x;
		} else {
			double a = dy / dx;
			double b = start.y - a * start.x;
			return vector.y - (a * vector.y + b) < 0.001;
		}
	}
	
	public boolean intersects(Edge other) {
		return intersects(other, false);
	}
	
	public boolean intersects(Edge other, boolean ray) {
		return getIntersection(other, ray) != null;
	}
	
	public Vector getIntersection(Edge other, boolean ray) {
		double dx1 = end.x - start.x;
		double dy1 = end.y - start.y;
		double dx2 = start.x - other.start.x;
		double dy2 = start.y - other.start.y;
		double dx3 = other.end.x - other.start.x;
		double dy3 = other.end.y - other.start.y;
		if(dy1 / dx1 != dy3 / dx3) {
			double d = dx1 * dy3 - dy1 * dx3;
			if(d != 0) {
				double r = (dy2 * dx3 - dx2 * dy3) / d; 
				double s = (dy2 * dx1 - dx2 * dy1) / d; 
				if(r >= 0 && (ray || r <= 1)) {
					if(s >= 0 && s <= 1) {
						return new Vector(start.x + r * dy1, start.y + r * dy1);
					}
				}
			}
		}
		return null;
	}
}
