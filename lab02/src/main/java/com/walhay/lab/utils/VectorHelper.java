package com.walhay.lab.utils;

import com.walhay.lab.primitives.Point;

public class VectorHelper {
	public static Point addVector(Point a, Point b) {
		return new Point(a.x() + b.x(), a.y() + b.y());
	}

	public static Point removeVector(Point a, Point b) {
		return new Point(a.x() - b.x(), a.y() - b.y());
	}

	public static Point rotateVector(Point a, double angle) {
		double rad = angle * Math.PI / 180.0f;
		double temp_x = a.x() * Math.cos(rad) - a.y() * Math.sin(rad);
		a.setY(a.x() * Math.sin(rad) + a.y() * Math.cos(rad));
		a.setX(temp_x);

		return a;
	}

	public static Point scaleVector(Point a, double sx, double sy) {
		a.setX(a.x() * sx);
		a.setY(a.y() * sy);
		return a;
	}

	public static double crossProductVectors(Point a, Point b) {
		return a.x() * b.y() - b.x() * a.y();
	}

	public static double getVectorLength(Point a) {
		return Math.sqrt(Math.pow(a.x(), 2) + Math.pow(a.y(), 2));
	}
};
