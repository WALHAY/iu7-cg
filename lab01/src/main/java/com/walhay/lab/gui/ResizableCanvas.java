package com.walhay.lab.gui;

import com.walhay.lab.utils.Geometry;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

public class ResizableCanvas extends Canvas {
	private final DrawPlot drawPlot;

	public ResizableCanvas() {
		this.drawPlot = new DrawPlot(640, 480);
		widthProperty().addListener(e -> resize());
		heightProperty().addListener(e -> resize());
	}

	public void setPointFocus(boolean focus) {
		drawPlot.setPointFocus(focus);
		redraw();
	}

	public void forceUpdate() {
		drawPlot.redraw();
		redraw();
	}

	private void resize() {
		drawPlot.setSize((int)getWidth(), (int)getHeight());
		redraw();
	}

	public void setSelection(List<Geometry.Point> pointList) {
		drawPlot.setSelection(pointList);
		redraw();
	}

	public void setTriangleSelection(List<Geometry.Triangle> triangles) {
		drawPlot.setTriangleSelection(triangles);
		redraw();
	}

	public void clearSelection() {
		drawPlot.clearSelection();
		redraw();
	}

	public void setPoints(Set<Geometry.Point> points) {
		drawPlot.setPoints(points);
		redraw();
	}

	private WritableImage getWritableImage() {
		BufferedImage image = drawPlot.getImage();
		int width = image.getWidth();
		int height = image.getHeight();

		int[] pixels = new int[width * height];

		image.getRGB(0, 0, width, height, pixels, 0, width);

		WritableImage writableImage = new WritableImage(width, height);
		writableImage.getPixelWriter().setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0,
												 width);

		return writableImage;
	}

	public void redraw() {
		GraphicsContext gc = getGraphicsContext2D();
		gc.drawImage(getWritableImage(), 0, 0);
	}

	@Override
	public boolean isResizable() {
		return true;
	}
}
