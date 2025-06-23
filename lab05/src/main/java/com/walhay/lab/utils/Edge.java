package com.walhay.lab.utils;

import java.awt.Point;

public record Edge(Point start, Point end) {

	public int getMinY() {
		return Math.min(start.y, end.y);
	}

	public int getMaxY() {
		return Math.max(start.y, end.y);
	}

	@Override
	public String toString() {
		return String.format("{%d, %d; %d %d}", start.x, start.y, end.x, end.y);
	}
}
