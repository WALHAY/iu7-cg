package com.walhay.lab.primitives;

import static com.walhay.lab.utils.VectorHelper.addVector;
import static com.walhay.lab.utils.VectorHelper.crossProductVectors;
import static com.walhay.lab.utils.VectorHelper.removeVector;

import com.walhay.lab.interfaces.IDrawable;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

public class Ellipse extends AbstractPrimitive {
	private int pointsCount = 1000;

	private Color color;
	private Color strokeColor;

	private Point pA;
	private Point pB;
	private Point pC;

	private int[] pointsX;
	private int[] pointsY;

	private static final int POINT_SCALE_FACTOR = 2;

	public Ellipse(Point center, int rA, int rB, Color color, Color strokeColor) {
		this(center, addVector(center, new Point(rA, 0)), addVector(center, new Point(0, rB)), color, strokeColor);
	}

	public Ellipse(Point pA, Point pB, Point pC, Color color) {
		this(pA, pB, pC, color, color);
	}

	public Ellipse(Point pA, Point pB, Point pC, Color fillColor, Color strokeColor) {
		this.pA = pA;
		this.pB = pB;
		this.pC = pC;

		this.color = fillColor;
		this.strokeColor = strokeColor;

		recalculatePoints();
	}

	private void recalculatePointsCount() {
		Point first = removeVector(pC, pA);
		Point second = removeVector(pB, pA);

		double area = Math.abs(crossProductVectors(first, second));
		pointsCount = (int)(Math.sqrt(area) * POINT_SCALE_FACTOR);
	}

	@Override
	protected void recalculatePoints() {
		recalculatePointsCount();

		pointsX = new int[pointsCount];
		pointsY = new int[pointsCount];

		for (int i = 0; i < pointsCount; ++i) {
			double temp = i * 2 * Math.PI / pointsCount;
			pointsX[i] =
				(int)Math.round(pA.x() + (pB.x() - pA.x()) * Math.cos(temp) + (pC.x() - pA.x()) * Math.sin(temp));
			pointsY[i] =
				(int)Math.round(pA.y() + (pB.y() - pA.y()) * Math.cos(temp) + (pC.y() - pA.y()) * Math.sin(temp));
		}
	}

	@Override
	public void draw(Graphics graphics) {
		Graphics2D g2d = (Graphics2D)graphics;
		graphics.setColor(color);
		g2d.fillPolygon(pointsX, pointsY, pointsCount);
		if (strokeColor != color) {
			g2d.setColor(strokeColor);
			g2d.drawPolygon(pointsX, pointsY, pointsCount);
		}
	}

	@Override
	public List<IDrawable> getDrawables() {
		return List.of(pA, pB, pC);
	}
}
