package com.walhay.lab.graphics;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public final class Algorithms {
	private static int[] window = new int[4];
	private static final double EPS = 1e-6;

	private Algorithms() {}

	public static void setCutter(Point first, Point second) {
		if (first == null || second == null) {
			return;
		}

		window[0] = Math.min(first.x, second.x);
		window[1] = Math.max(first.x, second.x);
		window[2] = Math.min(first.y, second.y);
		window[3] = Math.max(first.y, second.y);
	}

	public static List<Line2D> midpointClip(Line2D line) {
		if (line == null) {
			return Collections.emptyList();
		}

		Point2D first = line.getP1();
		Point2D second = line.getP2();

		List<Line2D> result = new ArrayList<>();
		midpointClip(first.getX(), first.getY(), second.getX(), second.getY(), result);
		return result;
	}

	private static void midpointClip(double x1, double y1, double x2, double y2, List<Line2D> result) {
		var status1 = pointStatus(x1, y1);
		var status2 = pointStatus(x2, y2);

		if (status1.isEmpty() && status2.isEmpty()) {
			result.add(new Line2D.Double(x1, y1, x2, y2));
			return;
		}

		// same side but not in bound
		status1.retainAll(status2);
		if (!status1.isEmpty()) {
			return;
		}

		if (Point2D.distanceSq(x1, y1, x2, y2) < EPS) {
			result.add(new Line2D.Double(x1, y1, x2, y2));
			return;
		}

		double midX = (x1 + x2) / 2;
		double midY = (y1 + y2) / 2;

		midpointClip(x1, y1, midX, midY, result);
		midpointClip(midX, midY, x2, y2, result);
	}

	private static Set<LineStatus> pointStatus(double x, double y) {
		Set<LineStatus> status = EnumSet.noneOf(LineStatus.class);

		if (y > window[3]) {
			status.add(LineStatus.ABOVE);
		}

		if (y < window[2]) {
			status.add(LineStatus.UNDER);
		}

		if (x < window[0]) {
			status.add(LineStatus.LEFT);
		}

		if (x > window[1]) {
			status.add(LineStatus.RIGHT);
		}

		return status;
	}
}
