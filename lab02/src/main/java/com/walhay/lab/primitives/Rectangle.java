package com.walhay.lab.primitives;

import static com.walhay.lab.utils.VectorHelper.addVector;
import static com.walhay.lab.utils.VectorHelper.removeVector;

import com.walhay.lab.interfaces.IDrawable;
import com.walhay.lab.utils.GraphicHelper;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.List;

public class Rectangle extends AbstractPrimitive {
	private Color color;
	private Color strokeColor;

	private Point pA;
	private Point pB;
	private Point pC;
	private Point pD;

	private Polygon polygon;

	/*  Form
	 * 	A - B
	 * 	|   |
	 * 	C - D
	 */
	public Rectangle(Point pA, Point pB, Point pC, Color color, Color strokeColor) {
		this.pA = pA;
		this.pB = pB;
		this.pC = pC;
		this.pD = addVector(pB, removeVector(pC, pA));

		this.color = color;
		this.strokeColor = strokeColor;

		recalculatePoints();
	}

	public Rectangle(Point pA, Point pB, Point pC, Color color) {
		this(pA, pB, pC, color, color);
	}

	@Override
	protected void recalculatePoints() {
		polygon = GraphicHelper.createPolygon(List.of(pA, pB, pD, pC));
	}

	@Override
	public void draw(Graphics graphics) {
		graphics.setColor(color);
		graphics.fillPolygon(polygon);
		if (strokeColor != color) {
			graphics.setColor(strokeColor);
			graphics.drawPolygon(polygon);
		}
	}

	@Override
	public List<IDrawable> getDrawables() {
		return List.of(pA, pB, pC, pD);
	}
}
