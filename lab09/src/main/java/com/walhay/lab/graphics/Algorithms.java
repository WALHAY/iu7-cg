package com.walhay.lab.graphics;

import com.walhay.lab.utils.CustomPolygon;
import com.walhay.lab.utils.Edge;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Algorithms {
	private Algorithms() {}

	private static int crossProduct(Point first, Point second) {
		return first.x * second.y - first.y * second.x;
	}

	public static boolean isConvex(CustomPolygon polygon) {
		if (polygon == null) {
			return false;
		}

		List<Edge> edges = polygon.getEdges();

		if (edges.size() < 3) {
			return false;
		}

		int previousSign = 0;
		for (int i = 0; i < edges.size(); ++i) {
			Edge first = edges.get(i);
			Edge second = edges.get((i + 1) % edges.size());

			double cross = crossProduct(first.vector, second.vector);

			int sign = (int) Math.signum(cross);

			if (sign == 0) {
				continue;
			}

			if (previousSign == 0) {
				previousSign = sign;
			} else if (sign != previousSign) {
				return false;
			}
		}

		return true;
	}

	public static CustomPolygon clip(CustomPolygon polygon, CustomPolygon clipper) {
		List<Edge> edges = clipper.getEdges();
		if (edges.size() < 3) {
			return null;
		}

		boolean cw = crossProduct(edges.get(0).vector, edges.get(1).vector) >= 0;

		List<Point> updatedVertices = new LinkedList<>(polygon.getVertices());
		for (Edge edge : edges) {
			List<Point> oldVertices = new ArrayList<>(updatedVertices);
			updatedVertices.clear();

			if (oldVertices.isEmpty()) {
				return null;
			}

			Point start = oldVertices.get(oldVertices.size() - 1);
			for (Point end : oldVertices) {
				if (isInside(edge, end, cw)) {
					if (!isInside(edge, start, cw)) {
						updatedVertices.add(computeIntersection(start, end, edge));
					}
					updatedVertices.add(end);
				} else if (isInside(edge, start, cw)) {
					updatedVertices.add(computeIntersection(start, end, edge));
				}
				start = end;
			}
		}

		return new CustomPolygon(updatedVertices);
	}

	private static boolean isInside(Edge edge, Point point, boolean cw) {
		int a = (point.x - edge.start.x) * edge.vector.y;
		int b = (point.y - edge.start.y) * edge.vector.x;

		int result = a - b;

		return result == 0 || (result > 0 ^ cw);
	}

	private static Point computeIntersection(Point start, Point end, Edge edge) {
		Point sideVector = new Point(end.x - start.x, end.y - start.y);
		Point edgeVector = new Point(edge.start.x - start.x, edge.start.y - start.y);

		double d = crossProduct(edge.vector, sideVector);

		double intersect = crossProduct(sideVector, edgeVector) / d;

		double x = edge.start.x + intersect * edge.vector.x;
		double y = edge.start.y + intersect * edge.vector.y;

		return new Point((int) x, (int) y);
	}
}
