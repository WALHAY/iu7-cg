package com.walhay.lab.gui;

import com.walhay.lab.utils.Geometry;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class DrawPlot {
	private double pointSize = 3;
	private Stroke lineStroke = new BasicStroke(3);

	private BufferedImage image;

	private Set<Geometry.Point> points = null;
	private List<Geometry.Point> selectedPoints = null;
	private List<Geometry.Triangle> selectedTriangles = null;

	private double xMin = 0;
	private double xMax = 0;
	private double yMin = 0;
	private double yMax = 0;

	private boolean pointFocus = false;

	private static final Color PAPAYA_WHIP = new Color(0xFFEFD5);

	public DrawPlot(int width, int height) {
		updateSize(width, height);
	}

	private void updateSize(int width, int height) {
		if (width <= 0 || height <= 0)
			return;
		this.pointSize = Math.max(3, Math.round(height / 200.0f));
		this.lineStroke = new BasicStroke((float)pointSize / 2.0f);

		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	public void setPointFocus(boolean focus) {
		this.pointFocus = focus;
		redraw();
	}

	public void setSize(int width, int height) {
		updateSize(width, height);
		redraw();
	}

	public void setSelection(List<Geometry.Point> pointList) {
		this.selectedPoints = pointList;
		redraw();
	}

	public void setTriangleSelection(List<Geometry.Triangle> triangles) {
		this.selectedTriangles = triangles;
		redraw();
	}

	public void clearSelection() {
		this.selectedPoints.clear();
		redraw();
	}

	public void setPoints(Set<Geometry.Point> points) {
		this.points = points;
		redraw();
	}

	private void updateMinMaxXY(Set<Geometry.Point> points) {
		var it = points.iterator();
		if (it.hasNext()) {
			Geometry.Point point = it.next();
			this.xMin = point.x();
			this.xMax = xMin;
			this.yMax = point.y();
			this.yMin = yMax;
		}

		while (it.hasNext()) {
			Geometry.Point point = it.next();

			xMin = Math.min(point.x(), xMin);
			yMin = Math.min(point.y(), yMin);

			xMax = Math.max(point.x(), xMax);
			yMax = Math.max(point.y(), yMax);
		}
	}

	private Geometry.Point transformToCanvasPos(Geometry.Point point, double height, double scalex, double scaley,
												double border) {
		double localX = border + (point.x() - xMin) * scalex;
		double localY = height - border - (point.y() - yMin) * scaley;
		return new Geometry.Point(localX, localY);
	}

	public void redraw() {
		if (!pointFocus && selectedTriangles != null && !selectedTriangles.isEmpty()) {
			Set<Geometry.Point> trianglePoints =
				selectedTriangles.stream().flatMap(t -> t.getPoints().stream()).collect(Collectors.toSet());

			updateMinMaxXY(trianglePoints);
		} else
			updateMinMaxXY(points);

		int width = image.getWidth();
		int height = image.getHeight();

		double border = 0.05 * Math.max(width, height);

		double scaleX = (width - 2 * border) / Math.max(2, (xMax - xMin));
		double scaleY = (height - 2 * border) / Math.max(2, (yMax - yMin));

		Graphics2D gc = (Graphics2D)image.getGraphics();
		gc.setColor(PAPAYA_WHIP);
		gc.fillRect(0, 0, (int)width, (int)height);

		gc.setColor(Color.BLACK);

		if (selectedTriangles != null && !selectedTriangles.isEmpty()) {
			for (Geometry.Triangle triangle : selectedTriangles) {
				int[] x = new int[3];
				int[] y = new int[3];

				for (int i = 0; i < 3; ++i) {
					Geometry.Point point =
						transformToCanvasPos(triangle.getPoints().get(i), height, scaleX, scaleY, border);

					x[i] = (int)(point.x() + pointSize / 2);
					y[i] = (int)(point.y() + pointSize / 2);
				}

				gc.setStroke(lineStroke);
				gc.setColor(Color.GREEN);
				gc.drawPolygon(x, y, 3);
			}
		}

		for (Geometry.Point point : points) {
			Geometry.Point local = transformToCanvasPos(point, height, scaleX, scaleY, border);
			if (selectedPoints != null && selectedPoints.contains(point))
				gc.setColor(Color.RED);
			else
				gc.setColor(Color.BLACK);

			gc.fillOval((int)local.x(), (int)local.y(), (int)pointSize, (int)pointSize);
			String strCoord = String.format(Locale.US, "(%.1f, %.1f)", point.x(), point.y());

			int strWidth = gc.getFontMetrics().stringWidth(strCoord);
			gc.drawString(strCoord, (int)(local.x() - strWidth / 2), (int)local.y() + 20);
		}
	}

	public BufferedImage getImage() {
		return image;
	}
}
