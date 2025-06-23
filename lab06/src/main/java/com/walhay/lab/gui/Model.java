package com.walhay.lab.gui;

import com.walhay.lab.graphics.Algorithms;
import com.walhay.lab.interfaces.IModel;
import com.walhay.lab.interfaces.IView;
import com.walhay.lab.utils.CustomPolygon;
import com.walhay.lab.utils.DataLoader;
import com.walhay.lab.utils.UserMode;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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

	private List<Pair<Point, Integer>> circles = null;

	private UserMode userMode = UserMode.POLYGON;

	@Setter
	private Color fillColor = Color.BLACK;

	@Setter
	private Color polygonBorderColor = Color.BLACK;

	@Setter
	private Color holeBorderColor = Color.BLACK;

	private Point fillPos = null;

	public Model() {
		clearCanvas();
	}

	@Override
	public void draw() {
		updateView();
	}

	public void onClick(Point point) {
		switch (userMode) {
			case POLYGON:
			case HOLE:
				addPoint(point);
				break;
			case FILL:
				fillPos = new Point(point);
				break;
		}
	}

	public void floodFill(boolean delay, int interval) {
		if (fillPos == null) {
			return;
		}

		long time = 0;
		if (delay) {
			time = Algorithms.floodFill(image, fillPos, polygonBorderColor, fillColor, interval, view);
		} else {
			time = Algorithms.floodFill(image, fillPos, polygonBorderColor, fillColor);
		}
		view.addReport(time);
		updateView();
	}

	public void clearFill() {
		Algorithms.timeline.stop();
		clearCanvas();
		drawBorder();
		updateView();
	}

	public void addCircle() {
		Random random = new Random();
		int radius = random.nextInt(50, 300);

		int x = random.nextInt(WIDTH);
		int y = random.nextInt(HEIGHT);

		if (circles == null) {
			circles = new LinkedList<Pair<Point, Integer>>();
		}

		circles.add(new Pair<>(new Point(x, y), radius));

		clearCanvas();
		drawBorder();
		updateView();
	}

	private void addPoint(Point point) {
		boolean isHoleMode = userMode.equals(UserMode.HOLE);
		if (isHoleMode && currentHole == null) {
			currentHole = new CustomPolygon();
		}

		(isHoleMode ? currentHole : polygon).getVertices().add(point);

		clearCanvas();

		drawBorder();
		updateView();
	}

	public void drawBorder() {
		if (polygon == null) {
			return;
		}

		Graphics graphics = image.getGraphics();
		graphics.setColor(polygonBorderColor);
		graphics.drawPolygon(polygon.toAwtPolygon());

		graphics.setColor(holeBorderColor);
		for (CustomPolygon poly : polygon.getPolygons()) {
			graphics.drawPolygon(poly.toAwtPolygon());
		}

		if (currentHole != null) {
			graphics.drawPolygon(currentHole.toAwtPolygon());
		}

		if (circles == null) {
			return;
		}
		for (Pair<Point, Integer> circle : circles) {
			graphics.drawOval(circle.getKey().x, circle.getKey().y, circle.getValue(), circle.getValue());
		}
	}

	private void updateView() {
		if (view == null) {
			return;
		}

		view.updateView();
	}

	private void clearCanvas() {
		Graphics graphics = image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
		draw();
	}

	public void setUserMode(UserMode mode) {
		this.userMode = mode;
		clearCanvas();
		drawBorder();
		updateView();
	}

	public void endHole() {
		if (!userMode.equals(UserMode.HOLE) || currentHole == null) {
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
		Pair<Color[], CustomPolygon> data = DataLoader.loadData(file.getAbsolutePath());
		this.polygon = data.getValue();
		this.polygonBorderColor = data.getKey()[0];
		this.holeBorderColor = data.getKey()[1];
		this.fillColor = data.getKey()[2];
		view.updatePalette(data.getKey());
		drawBorder();
		updateView();
	}

	public void saveData(File file) throws IOException {
		if (file == null) {
			return;
		}

		DataLoader.saveData(
				polygon, new Color[] {polygonBorderColor, holeBorderColor, fillColor}, file.getAbsolutePath());
	}
}
