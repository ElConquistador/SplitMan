package elcon.games.splitman.util;


public class Vector {

	private double x;
	private double y;

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getLength() {
		return Math.sqrt(x * x + y * y);
	}

	public void setLength(double length) {
		double angle = Math.toRadians(getAngle());
		x = Math.cos(angle) * length;
		y = Math.sin(angle) * length;
	}

	public double getAngle() {
		return Math.atan2(y, x);
	}

	public void setAngle(double angle) {
		angle = Math.toRadians(angle);
		double length = getLength();
		x = Math.cos(angle) * length;
		y = Math.sin(angle) * length;
	}

	public Vector add(Vector vector) {
		return new Vector(x + vector.getX(), y + vector.getY());
	}

	public Vector subtract(Vector vector) {
		return new Vector(x - vector.getX(), y - vector.getY());
	}

	public Vector multiply(double value) {
		return new Vector(x * value, y * value);
	}

	public Vector divide(double value) {
		return new Vector(x / value, y / value);
	}

	public void addTo(Vector vector) {
		x += vector.getX();
		y += vector.getY();
	}

	public void subtractFrom(Vector vector) {
		x -= vector.getX();
		y -= vector.getY();
	}

	public void multiplyBy(double value) {
		x *= value;
		y *= value;
	}

	public void divideBy(double value) {
		x /= value;
		y /= value;
	}

	@Override
	public String toString() {
		return "Vector[" + x + ", " + y + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Vector)) {
			return false;
		}
		Vector vector = (Vector) obj;
		return x == vector.getX() && y == vector.getY();
	}
}
