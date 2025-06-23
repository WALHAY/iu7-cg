package com.walhay.lab.primitives;

import com.walhay.lab.interfaces.IDrawable;
import com.walhay.lab.utils.*;
import java.awt.Graphics;
import java.util.Objects;

public class Point implements IDrawable {
	private double x;
	private double y;

	private Basis basis = new Basis();

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	};

	public Point(Basis basis, double x, double y) {
		this.x = x;
		this.y = y;
	};

	public double roughX() {
		return x;
	}

	public double roughY() {
		return y;
	}

	// returns x in basis coordinates
	public double x() {
		return basis.localX(this);
	}

	// returns x in basis coordinates
	public double y() {
		return basis.localY(this);
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public void draw(Graphics graphics) {
		graphics.drawOval((int)x - 1, (int)y - 1, 2, 2);
	}

	public Point copy() {
		return new Point(basis, x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Point point) {
			return point.x == x && point.y == y;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public void changeBasis(Basis basis) {
		this.basis = basis;
	}
}
