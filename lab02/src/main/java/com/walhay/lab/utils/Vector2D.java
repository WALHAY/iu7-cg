package com.walhay.lab.utils;

import com.walhay.lab.interfaces.*;

public class Vector2D implements IAffineTransform {
	private double x;
	private double y;

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	@Override
	public void rotate(double angle) {
		double rad = angle * Math.PI / 180.0f;
		double tempX = x * Math.cos(rad) - y * Math.sin(rad);
		y = x * Math.sin(rad) + y * Math.cos(rad);
		x = tempX;
	}

	@Override
	public void scale(double scale) {
		this.x *= scale;
		this.y *= scale;
	}

	@Override
	public void transpose(Vector2D move) {
		this.x += move.x;
		this.y += move.y;
	}
}
