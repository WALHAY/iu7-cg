package com.walhay.lab.primitives;

import com.walhay.lab.interfaces.IDrawable;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BezierCurve extends AbstractPrimitive {
	public GeneralPath curve;

	public Color color;
	public Color strokeColor;

	public List<Point> points = new LinkedList<>();
	public List<BezierCurve> bezierCurves = new LinkedList<>();

	public BezierCurve(Point start, Color color) {
		this(start, color, color);
	}

	public BezierCurve(Point start, Color color, Color strokeColor) {
		this.curve = new GeneralPath();
		this.curve.moveTo(start.x(), start.y());

		this.color = color;
		this.strokeColor = strokeColor;

		this.points.add(start);
	}

	public Shape getShape() {
		return curve;
	}

	public void addCurve(BezierCurve bezier) {
		bezierCurves.add(bezier);
		curve.append(bezier.getShape(), false);
	}

	public void addPoint(Point point, Point control1, Point control2) {
		points.add(control1);
		points.add(control2);
		points.add(point);

		curve.curveTo(control1.x(), control1.y(), control2.x(), control2.y(), point.x(), point.y());
	}

	@Override
	protected void recalculatePoints() {
		curve.reset();

		Point first = points.get(0);
		curve.moveTo(first.x(), first.y());
		for (int i = 1; i < points.size() - 1; i += 3) {
			Point control1 = points.get(i);
			Point control2 = points.get(i + 1);
			Point point = points.get(i + 2);

			curve.curveTo(control1.x(), control1.y(), control2.x(), control2.y(), point.x(), point.y());
		}

		for (BezierCurve bezier : bezierCurves) {
			curve.append(bezier.getShape(), false);
		}
	}

	@Override
	public void draw(Graphics graphics) {
		graphics.setColor(color);
		Graphics2D g2d = (Graphics2D)graphics;
		g2d.fill(curve);
		g2d.setColor(strokeColor);
		g2d.draw(curve);
	}

	public BezierCurve copy() {
		BezierCurve curve = new BezierCurve(points.get(0), this.color);

		LinkedList<Point> newPoints = new LinkedList<>();
		points.stream().map(p -> new Point(p.x(), p.y())).forEach(newPoints::add);
		curve.points = newPoints;
		List<BezierCurve> newBeziers = new LinkedList<>();

		bezierCurves.stream().map(BezierCurve::copy).forEach(newBeziers::add);
		curve.bezierCurves = newBeziers;
		curve.color = color;
		curve.strokeColor = strokeColor;
		return curve;
	}

	@Override
	public List<IDrawable> getDrawables() {
		List<IDrawable> drawables = new ArrayList<>(points);
		drawables.addAll(bezierCurves);
		return drawables;
	}
}
