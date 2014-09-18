package elcon.games.splitman.util;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

	public ArrayList<Vector> vertices = new ArrayList<Vector>();
	public ArrayList<Edge> edges = new ArrayList<Edge>();
	public Vector center;
	public Vector min;
	public Vector max;
	
	public Polygon(Vector... vertices) {
		for(int i = 0; i < vertices.length; i++) {
			this.vertices.add(vertices[i]);
		}
		this.center = new Vector();
		if(this.vertices.size() > 0) {
			computeCenter();
			computeBounds();
			computeEdges();
		}
	}
	
	public Polygon(List<Vector> vericies) {
		this.vertices.addAll(vericies);
		this.center = new Vector();
		if(this.vertices.size() > 0) {
			computeCenter();
			computeBounds();
			computeEdges();
		}
	}
	
	public void computeCenter() {
		center.set(0, 0);
		for(Vector vertex : vertices) {
			center.addTo(vertex);
		}
		center.divideBy(vertices.size());
	}
	
	public void computeBounds() {
		min.set(Double.MAX_VALUE, Double.MAX_VALUE);
		max.set(Double.MIN_VALUE, Double.MIN_VALUE);
		for(Vector vertex : vertices) {
			min.min(vertex);
			max.min(vertex);
		}
	}
	
	public void computeEdges() {
		edges.clear();
		for(int i = 0; i < vertices.size(); i++) {
			edges.add(new Edge(vertices.get(i), vertices.get((i + 1) % vertices.size())));
		}
	}
	
	public void translate(Vector vector) {
		center.addTo(vector);
		min.addTo(vector);
		max.addTo(vector);
		for(Vector vertex : vertices) {
			vertex.addTo(vector);
		}
	}
	
	public void rotate(double radians) {
		rotate(radians, center);
	}
	
	public void rotate(double radians, Vector pivot) {
		double s = Math.sin(radians);
		double c = Math.cos(radians);
		double dx;
		double dy;
		for(Vector vertex : vertices) {
			dx = vertex.x - pivot.x;
			dy = vertex.y - pivot.y;
			vertex.x = c * dx - s * dy + pivot.x;
			vertex.x = s * dx + c * dy + pivot.y;
		}
		computeBounds();
	}
	
	public boolean contains(Vector vector) {
		if(vector.x > max.x || vector.x < min.x || vector.y > max.y || vector.y < min.y) {
			return false;
		}
		Edge ray = new Edge(vector, new Vector(min.x - 1, min.y - 1));
		int intersections = 0;
		for(Edge edge : edges) {
			if(ray.intersects(edge, true)) {
				intersections++;
			}
		}
		return (intersections % 2) == 1;
	}
	
	public boolean collides(Polygon polygon) {
		if(polygon.min.greaterThan(max) || polygon.max.lessThan(min)) {
			return false;
		}
		for(Edge edge : edges) {
			for(Edge otherEdge : polygon.edges) {
				if(edge.intersects(otherEdge)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public ArrayList<Vector> getCollisions(Polygon polygon) {
		if(polygon.min.greaterThan(max) || polygon.max.lessThan(min)) {
			return null;
		}
		ArrayList<Vector> intersections = new ArrayList<Vector>();
		Vector intersection;
		for(Edge edge : edges) {
			for(Edge otherEdge : polygon.edges) {
				intersection = edge.getIntersection(otherEdge, false);
				if(intersection != null) {
					intersections.add(intersection);
				}
			}
		}
		return intersections.size() > 0 ? intersections : null;
	}
	
	public Polygon copy() {
		return new Polygon(vertices.toArray(new Vector[vertices.size()]));
	}
}
