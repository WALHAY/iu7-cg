package com.walhay.lab.gui;

import com.walhay.lab.graphics.Algorithms;
import com.walhay.lab.interfaces.IModel;
import com.walhay.lab.interfaces.IView;
import com.walhay.lab.utils.CustomPolygon;
import com.walhay.lab.utils.DataLoader;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

public class Model implements IModel {
	@Setter
	private IView view;

	private static final int WIDTH = 1080;
	private static final int HEIGHT = 720;

	@Getter
	private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

	private CustomPolygon polygon = new CustomPolygon();
	private CustomPolygon currentHole = null;

	private boolean holeMode = false;

	@Setter
	private Color fillColor = Color.BLACK;

	public Model() {
		clearCanvas();
	}

	@Override
	public void draw() {
		updateView();
	}

	public void addPoint(Point point) {
		if (holeMode && currentHole == null) {
			currentHole = new CustomPolygon();
		}

		(holeMode ? currentHole : polygon).getVertices().add(point);

		clearCanvas();

		drawBorder();
		updateView();
	}

	public void drawBorder() {
		if (polygon == null) {
			return;
		}

		Graphics graphics = image.getGraphics();
		graphics.setColor(Color.BLACK);
		for (CustomPolygon poly : polygon.getPolygons()) {
			graphics.drawPolygon(poly.toAwtPolygon());
		}

		graphics.drawPolygon(polygon.toAwtPolygon());

		if (currentHole != null) {
			graphics.drawPolygon(currentHole.toAwtPolygon());
		}
	}

	private void updateView() {
		if (view == null) {
			return;
		}

		view.updateView();
	}

	public void fillPolygon(boolean withDelay, int interval) {
		Graphics graphics = image.getGraphics();
		clearCanvas();
		long time = 0;
		if (withDelay) {
			time = Algorithms.fillPolygonDelay(graphics, polygon, fillColor, interval, view);
		} else {
			time = Algorithms.fillPolygon(graphics, polygon, fillColor);
			updateView();
		}
		view.addReport(time);
	}

	private void clearCanvas() {
		Graphics graphics = image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
		draw();
	}

	public void setHoleMode(boolean holeMode) {
		this.holeMode = holeMode;
		clearCanvas();
		drawBorder();
		updateView();
	}

	public void endHole() {
		if (!holeMode || currentHole == null) {
			return;
		}

		polygon.getPolygons().add(currentHole);
		clearCanvas();
		drawBorder();
		updateView();
		currentHole = null;
	}

	public void loadData(File file) throws IOException {
		if (file == null) {
			return;
		}

		clearCanvas();
		Pair<Color, CustomPolygon> data = DataLoader.loadData(file.getAbsolutePath());
		this.polygon = data.getValue();
		this.fillColor = data.getKey();
		view.updateColor(fillColor);
		drawBorder();
		updateView();
	}

	public void saveData(File file) throws IOException {
		if (file == null) {
			return;
		}

		DataLoader.saveData(polygon, fillColor, file.getAbsolutePath());
	}
}
