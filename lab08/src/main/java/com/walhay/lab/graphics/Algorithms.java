package com.walhay.lab.graphics;

import com.walhay.lab.utils.CustomPolygon;
import com.walhay.lab.utils.Edge;
import java.awt.Point;
import java.awt.geom.Line2D;
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

	public static Line2D clip(Line2D line, CustomPolygon cutter) {
		if (cutter.getVertices().size() < 3) {
			return null;
		}

		Point d = new Point((int) (line.getX2() - line.getX1()), (int) (line.getY2() - line.getY1()));

		double tIn = 0.0;
		double tOut = 1.0;

		var edges = new LinkedList<>(cutter.getEdges());

		boolean cw = crossProduct(edges.get(0).vector, edges.get(1).vector) < 0;

		for (Edge edge : edges) {
			if (cw) {
				edge.normal.x *= -1;
				edge.normal.y *= -1;
			}

			Point w = new Point((int) (line.getX1() - edge.start.x), (int) (line.getY1() - edge.start.y));

			double dDotN = d.x * edge.normal.x + d.y * edge.normal.y;

			double wDotN = w.x * edge.normal.x + w.y * edge.normal.y;

			if (dDotN == 0) {
				if (wDotN < 0 ^ cw) {
					return null;
				}
			} else {
				double t = -wDotN / dDotN;
				if (dDotN > 0) {
					if (t > tIn) {
						tIn = t;
					}
				} else {
					if (t < tOut) {
						tOut = t;
					}
				}

				if (tIn > tOut) {
					return null;
				}
			}
		}

		if (tIn < tOut) {
			double x1 = line.getX1() + d.x * tIn;
			double y1 = line.getY1() + d.y * tIn;
			double x2 = line.getX1() + d.x * tOut;
			double y2 = line.getY1() + d.y * tOut;

			return new Line2D.Double(x1, y1, x2, y2);
		}

		return null;
	}
}
