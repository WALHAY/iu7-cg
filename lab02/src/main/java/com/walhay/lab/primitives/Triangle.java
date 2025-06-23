package com.walhay.lab.primitives;

import com.walhay.lab.interfaces.IDrawable;
import com.walhay.lab.utils.GraphicHelper;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.List;

public class Triangle extends AbstractPrimitive {
	private Point a;
	private Point b;
	private Point c;
	private Color color = Color.GREEN;

	public Triangle(Point a, Point b, Point c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	@Override
	public void draw(Graphics graphics) {
		Polygon polygon = GraphicHelper.createPolygon(getPoints());
		graphics.setColor(color);
		graphics.fillPolygon(polygon);
	}

	private List<Point> getPoints() {
		return List.of(a, b, c);
	}

	@Override
	public List<IDrawable> getDrawables() {
		return List.of(a, b, c);
	}

	@Override
	protected void recalculatePoints() {
	}
}
