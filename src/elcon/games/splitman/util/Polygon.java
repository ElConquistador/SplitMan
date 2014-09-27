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
		this.min = new Vector();
		this.max = new Vector();
		if(this.vertices.size() > 0) {
			computeCenter();
			computeBounds();
			computeEdges();
		}
	}
	
	public Polygon(List<Vector> vericies) {
		this.vertices.addAll(vericies);
		this.center = new Vector();
		this.min = new Vector();
		this.max = new Vector();
		if(this.vertices.size() > 0) {
			computeCenter();
			computeBounds();
			computeEdges();
		}
	}
	
	public Polygon(BoundingBox boundingBox) {
		this(new Vector(boundingBox.minX, boundingBox.minY), new Vector(boundingBox.maxX, boundingBox.minY), new Vector(boundingBox.maxX, boundingBox.maxY), new Vector(boundingBox.minX, boundingBox.maxY));
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
	
	public boolean canMerge(Polygon polygon) {
		for(Edge edge : edges) {
			for(Edge otherEdge : polygon.edges) {
				if(edge.contains(otherEdge.start) || edge.contains(otherEdge.end)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void merge(Polygon polygon) {
		for(Edge edge : edges) {
			for(Edge otherEdge : polygon.edges) {
				if(edge.contains(otherEdge.start) && edge.contains(otherEdge.end)) {
					ArrayList<Vector> newVertices = new ArrayList<Vector>();
					for(Vector vertex : vertices) {
						if(vertex.equals(otherEdge.start) || vertex.equals(otherEdge.end)) {
							newVertices.add(vertex);
							int index = polygon.vertices.indexOf(vertex.equals(otherEdge.start) ? otherEdge.start : otherEdge.end);
							for(int i = 0; i < polygon.vertices.size(); i++) {
								index = (index + 1) % polygon.vertices.size();
								newVertices.add(polygon.vertices.get(index));
							}
						} else {
							newVertices.add(vertex);
						}
					}
					vertices = newVertices;
					computeCenter();
					computeBounds();
					computeEdges();
					return;
				} else if(edge.contains(otherEdge.start)) {
					//TODO
				} else if(edge.contains(otherEdge.end)) {
					//TODO
				}
			}
		}
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
	
	public boolean collides(Edge otherEdge) {
		for(Edge edge : edges) {
			if(edge.intersects(otherEdge)) {
				return true;
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
	
	public ArrayList<Vector> getRayCollisions(Edge otherEdge) {
		ArrayList<Vector> intersections = new ArrayList<Vector>();
		Vector intersection;
		for(Edge edge : edges) {
			intersection = edge.getIntersection(otherEdge, false);
			if(intersection != null) {
				intersections.add(intersection);
			}
		}
		return intersections.size() > 0 ? intersections : null;
	}
	
	public Polygon copy() {
		return new Polygon(vertices.toArray(new Vector[vertices.size()]));
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(Vector vertex : vertices) {
			sb.append(vertex);
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append("]");
		return sb.toString();
	}
}
