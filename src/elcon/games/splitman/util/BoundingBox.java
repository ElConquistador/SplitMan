package elcon.games.splitman.util;

public class BoundingBox {

	public double minX;
	public double maxX;
	public double minY;
	public double maxY;

	public BoundingBox(double minX, double maxX, double minY, double maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	public BoundingBox add(double x, double y) {
		return new BoundingBox(minX + x, maxX + x, minY + y, maxY + y);
	}

	public BoundingBox subtract(double x, double y) {
		return new BoundingBox(minX - x, maxX - x, minY - y, maxY - y);
	}

	public BoundingBox add(Vector vector) {
		return add(vector.getX(), vector.getY());
	}

	public BoundingBox subtract(Vector vector) {
		return subtract(vector.getX(), vector.getY());
	}

	public void addTo(double x, double y) {
		minX += x;
		maxX += x;
		minY += y;
		maxY += y;
	}

	public void subtractFrom(double x, double y) {
		minX -= x;
		maxX -= x;
		minY -= y;
		maxY -= y;
	}

	public void addTo(Vector vector) {
		addTo(vector.getX(), vector.getY());
	}

	public void subtractFrom(Vector vector) {
		subtractFrom(vector.getX(), vector.getY());
	}

	public boolean intersects(BoundingBox box) {
		return intersects(this, box);
	}

	public boolean containsPoint(double x, double y) {
		return containsPoint(this, x, y);
	}

	public static boolean intersects(BoundingBox box1, BoundingBox box2) {
		return MathHelper.rangeIntersects(box1.minX, box1.maxX, box2.minX, box2.maxX) && MathHelper.rangeIntersects(box1.minY, box1.maxY, box2.minY, box2.maxY);
	}

	public static boolean containsPoint(BoundingBox box, double x, double y) {
		return MathHelper.inRange(x, box.minX, box.maxX) && MathHelper.inRange(y, box.minY, box.maxY);
	}

	@Override
	public String toString() {
		return "BoundingBox[" + minX + ", " + minY + "; " + maxX + ", " + maxY + "]";
	}
}
