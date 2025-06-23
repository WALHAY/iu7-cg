package com.walhay.lab.utils;

import com.walhay.lab.primitives.Point;

public class Basis {
	private Vector2D zero = new Vector2D(0, 0);
	private Vector2D x = new Vector2D(1, 0);
	private Vector2D y = new Vector2D(0, 1);

	public Basis() {
	}

	public double localX(Point point) {
		return zero.x() + point.roughX() * x.x() + point.roughY() * y.x();
	}

	public double localY(Point point) {
		return zero.y() + point.roughX() * x.y() + point.roughY() * y.y();
	}

	public void rotate(double angle) {
		x.rotate(angle);
		y.rotate(angle);
	}

	public void scale(double scale) {
		x.scale(scale);
		y.scale(scale);
	}

	public void transpose(Vector2D move) {
		zero.transpose(move);
	}
}
