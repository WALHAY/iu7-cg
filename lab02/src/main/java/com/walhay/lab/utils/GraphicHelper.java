package com.walhay.lab.utils;

import com.walhay.lab.primitives.Point;
import java.awt.Polygon;
import java.util.List;

public class GraphicHelper {
	public static Polygon createPolygon(List<Point> points) {
		Polygon polygon = new Polygon();
		for (Point p : points) polygon.addPoint((int)p.x(), (int)p.y());
		return polygon;
	}
}
