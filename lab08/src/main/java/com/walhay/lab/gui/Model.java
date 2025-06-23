package com.walhay.lab.gui;

import com.walhay.lab.graphics.Algorithms;
import com.walhay.lab.interfaces.IModel;
import com.walhay.lab.interfaces.IView;
import com.walhay.lab.utils.CustomPolygon;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class Model implements IModel {
	@Setter
	private IView view;

	private static final int WIDTH = 1080;
	private static final int HEIGHT = 720;

	@Setter
	private Color cutColor = Color.RED;

	@Setter
	private Color lineColor = Color.BLACK;

	@Setter
	private Color rectColor = Color.GREEN;

	private CustomPolygon cutter = null;

	private Point firstLine = null;
	private Point secondLine = null;
	private List<Line2D> lines = new LinkedList<>();

	@Getter
	private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

	private final Graphics2D graphics = (Graphics2D) image.getGraphics();

	public Model() {
		clearCanvas();
	}

	public void addLine(Point start, Point end) {
		if (start == null || end == null) {
			return;
		}

		Line2D newLine = new Line2D.Float(start, end);

		lines.add(newLine);
		graphics.setColor(lineColor);
		drawLine(newLine);
		updateView();
	}

	public void addLinePoint(Point point) {
		if (firstLine == null) {
			firstLine = point;
			return;
		}

		if (secondLine == null) {
			secondLine = point;
		}

		Line2D newLine = new Line2D.Float(firstLine, secondLine);

		firstLine = null;
		secondLine = null;

		lines.add(newLine);
		graphics.setColor(lineColor);
		drawLine(newLine);
		updateView();
	}

	public void addCutterPoint(Point point) {
		if (cutter == null) {
			cutter = new CustomPolygon();
		}

		cutter.getVertices().add(point);

		draw();
		updateView();
	}

	private void drawRect() {
		if (cutter == null) {
			return;
		}

		var polygon = cutter.toAwtPolygon();

		graphics.setColor(rectColor);
		graphics.drawPolygon(polygon);
	}

	private void drawLine(Line2D line) {
		if (line == null) {
			return;
		}
		graphics.draw(line);
	}

	private void drawLines() {
		graphics.setColor(lineColor);
		for (Line2D line : lines) {
			drawLine(line);
		}
	}

	public boolean checkCutterConvex() {
		return cutter != null && Algorithms.isConvex(cutter);
	}

	public void actionCut() {
		if (!checkCutterConvex()) {
			return;
		}

		graphics.setColor(cutColor);
		for (Line2D line : lines) {
			var result = Algorithms.clip(line, cutter);
			drawLine(result);
		}
		updateView();
	}

	void updateView() {
		if (view == null) {
			return;
		}

		view.updateView();
	}

	@Override
	public void draw() {
		clearCanvas();
		drawRect();
		drawLines();
		updateView();
	}

	public void clearScreen() {
		lines.clear();
		firstLine = null;
		secondLine = null;
		cutter = null;
		clearCanvas();
		updateView();
	}

	private void clearCanvas() {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
		updateView();
	}
}
