package com.walhay.lab.gui;

import com.walhay.lab.graphics.Algorithms;
import com.walhay.lab.interfaces.IModel;
import com.walhay.lab.interfaces.IView;
import com.walhay.lab.utils.CustomPolygon;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
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
	private Color figureColor = Color.BLACK;

	@Setter
	private Color cutterColor = Color.GREEN;

	private CustomPolygon cutter = null;

	private CustomPolygon figure = null;

	@Getter
	private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

	private final Graphics2D graphics = (Graphics2D) image.getGraphics();

	public Model() {
		clearCanvas();
	}

	public void addFigurePoint(Point point) {
		if (figure == null) {
			figure = new CustomPolygon();
		}

		figure.getVertices().add(point);

		draw();
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

	private void drawPolygon(CustomPolygon polygon, Color color) {
		if (polygon == null) {
			return;
		}

		var awtPolygon = polygon.toAwtPolygon();

		graphics.setColor(color);
		graphics.drawPolygon(awtPolygon);
	}

	public boolean checkCutterConvex() {
		return cutter != null && Algorithms.isConvex(cutter);
	}

	public void actionCut() {
		if (!checkCutterConvex()) {
			return;
		}

		graphics.setColor(cutColor);
		var result = Algorithms.clip(figure, cutter);
		if (result != null) {
			drawPolygon(result, cutColor);
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
		drawPolygon(cutter, cutterColor);
		drawPolygon(figure, figureColor);
		updateView();
	}

	public void clearScreen() {
		figure = null;
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
