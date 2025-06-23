package com.walhay.lab.utils;

import java.awt.Point;
import java.awt.Polygon;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;

@Getter
public final class CustomPolygon {
	private List<Point> vertices = new LinkedList<>();
	private Polygon polygon = null;

	private List<CustomPolygon> addedPolygons = new LinkedList<>();

	public List<Point> getVertices() {
		return this.vertices;
	}

	public Polygon toAwtPolygon() {
		if (polygon == null) {
			polygon = new Polygon();
		} else {
			polygon.reset();
		}

		for (final Point vertex : vertices) {
			polygon.addPoint(vertex.x, vertex.y);
		}

		return polygon;
	}

	public List<CustomPolygon> getPolygons() {
		return addedPolygons;
	}

	public List<Edge> getEdges() {
		List<Edge> result = new LinkedList<>();

		int size = vertices.size();
		for (int i = 0; i < size; ++i) {
			Point start = vertices.get(i);
			Point end = vertices.get((i + 1) % size);

			Edge edge = new Edge(start, end);

			result.add(edge);
		}

		return result;
	}
}
