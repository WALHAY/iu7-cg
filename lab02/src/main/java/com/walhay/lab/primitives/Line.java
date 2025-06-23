package com.walhay.lab.primitives;

import static com.walhay.lab.utils.VectorHelper.getVectorLength;
import static com.walhay.lab.utils.VectorHelper.removeVector;

import com.walhay.lab.interfaces.IDrawable;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Line extends AbstractPrimitive {
	private Color color;

	private Point pA;
	private Point pB;

	public Line(Point start, Point end) {
		this(start, end, Color.BLACK);
	}

	public Line(Point start, Point end, Color color) {
		this.color = color;

		this.pA = start;
		this.pB = end;
	}

	@Override
	public void draw(Graphics graphics) {
		graphics.setColor(color);
		graphics.drawLine((int)pA.x(), (int)pA.y(), (int)pB.x(), (int)pB.y());
	}

	@Override
	public List<IDrawable> getDrawables() {
		return List.of(pA, pB);
	}

	@Override
	protected void recalculatePoints() {
		// Not needed
	}

	public double getLineLength() {
		return getVectorLength(removeVector(pB, pA));
	}
}
