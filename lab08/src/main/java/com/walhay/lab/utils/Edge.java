package com.walhay.lab.utils;

import java.awt.Point;

public class Edge {
	public final Point start;
	public final Point end;
	public final Point normal;
	public final Point vector;

	public Edge(Point start, Point end) {
		this.start = start;
		this.end = end;

		int vecX = end.x - start.x;
		int vecY = end.y - start.y;

		this.vector = new Point(vecX, vecY);
		this.normal = new Point(-vecY, vecX);
	}

	@Override
	public String toString() {
		return String.format("{%d, %d; %d %d}", start.x, start.y, end.x, end.y);
	}
}
