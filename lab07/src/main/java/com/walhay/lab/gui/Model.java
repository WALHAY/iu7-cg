package com.walhay.lab.gui;

import com.walhay.lab.graphics.Algorithms;
import com.walhay.lab.interfaces.IModel;
import com.walhay.lab.interfaces.IView;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
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

	private boolean placeFirst = true;

	private Point firstRect = null;
	private Point secondRect = null;

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

	public void setRect(Point start, Point end) {

		if (start == null || end == null) {
			return;
		}
		firstRect = start;
		secondRect = end;

		Algorithms.setCutter(start, end);

		draw();
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

	public void addRectPoint(Point point) {
		if (placeFirst) {
			firstRect = point;
		} else {
			secondRect = point;
		}
		placeFirst = !placeFirst;

		Algorithms.setCutter(firstRect, secondRect);

		draw();
		updateView();
	}

	private void drawRect() {
		if (firstRect == null || secondRect == null) {
			return;
		}

		Rectangle rect = new Rectangle(firstRect);
		rect.add(secondRect);

		graphics.setColor(rectColor);
		graphics.drawRect(rect.x, rect.y, rect.width, rect.height);
	}

	private void drawLine(Line2D line) {
		graphics.draw(line);
	}

	private void drawLines() {
		graphics.setColor(lineColor);
		for (Line2D line : lines) {
			drawLine(line);
		}
	}

	public void actionCut() {
		graphics.setColor(cutColor);
		for (Line2D line : lines) {
			var result = Algorithms.midpointClip(line);
			for (Line2D res : result) {
				drawLine(res);
			}
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
		firstRect = null;
		secondRect = null;
		clearCanvas();
		updateView();
	}

	private void clearCanvas() {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
		updateView();
	}
}
