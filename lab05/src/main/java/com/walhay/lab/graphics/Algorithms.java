package com.walhay.lab.graphics;

import com.walhay.lab.interfaces.IView;
import com.walhay.lab.utils.CustomPolygon;
import com.walhay.lab.utils.Edge;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public final class Algorithms {
	private static Timeline timeline = new Timeline();

	private Algorithms() {}

	public static long fillPolygon(Graphics graphics, CustomPolygon polygon, Color color) {
		long start = System.nanoTime();
		List<Edge> edges = polygon.getEdges();

		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		for (Edge edge : edges) {
			minY = Math.min(minY, edge.getMinY());
			maxY = Math.max(maxY, edge.getMaxY());
		}

		List<CustomPolygon> polygons = new LinkedList<>(polygon.getPolygons());
		polygons.add(polygon);
		for (int y = minY; y <= maxY; y++) {
			List<Integer> intersections = new ArrayList<>();

			for (CustomPolygon poly : polygons) {
				edges = poly.getEdges();
				int size = edges.size();
				for (int i = 0; i < size; ++i) {
					Edge edge = edges.get(i);

					if (edge == null) {
						continue;
					}

					int startY = edge.start().y;
					int endY = edge.end().y;
					int startX = edge.start().x;
					int endX = edge.end().x;

					if (y == endY) {
						Edge nextEdge = edges.get((i + 1) % size);

						boolean isLocalMinMax =
								(nextEdge.end().y > y && startY > y) || (nextEdge.end().y < y && startY < y);

						if (!isLocalMinMax) {
							intersections.add(endX);
						}
					} else if ((y > startY && y < endY) || (y > endY && y < startY)) {
						double x = startX + (double) (y - startY) * (endX - startX) / (endY - startY);

						intersections.add((int) Math.round(x));
					}
				}
			}

			intersections.sort(Integer::compare);

			boolean inside = false;
			graphics.setColor(color);
			for (int i = 0; i < intersections.size(); i++) {
				if (inside) {
					int startX = intersections.get(i - 1);
					int endX = intersections.get(i);
					graphics.drawLine(startX, y, endX, y);
				}
				inside = !inside;
			}
		}

		long end = System.nanoTime();
		return end - start;
	}

	public static long fillPolygonDelay(Graphics graphics, CustomPolygon polygon, Color color, int delay, IView view) {
		timeline.stop();
		timeline.getKeyFrames().clear();

		long start = System.nanoTime();
		List<Edge> edges = polygon.getEdges();

		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		for (Edge edge : edges) {
			minY = Math.min(minY, edge.getMinY());
			maxY = Math.max(maxY, edge.getMaxY());
		}

		List<CustomPolygon> polygons = new LinkedList<>(polygon.getPolygons());
		polygons.add(polygon);
		for (int y = minY; y <= maxY; y++) {
			List<Integer> intersections = new ArrayList<>();

			for (CustomPolygon poly : polygons) {
				edges = poly.getEdges();
				int size = edges.size();
				for (int i = 0; i < size; ++i) {
					Edge edge = edges.get(i);

					if (edge == null) {
						continue;
					}

					int startY = edge.start().y;
					int endY = edge.end().y;
					int startX = edge.start().x;
					int endX = edge.end().x;

					if (y == endY) {
						Edge nextEdge = edges.get((i + 1) % size);

						boolean isLocalMinMax =
								(nextEdge.end().y > y && startY > y) || (nextEdge.end().y < y && startY < y);

						if (!isLocalMinMax) {
							intersections.add(endX);
						}
					} else if ((y > startY && y < endY) || (y > endY && y < startY)) {
						double x = startX + (double) (y - startY) * (endX - startX) / (endY - startY);

						intersections.add((int) Math.round(x));
					}
				}
			}

			intersections.sort(Integer::compare);

			boolean inside = false;
			int currentY = y;
			graphics.setColor(color);
			for (int i = 0; i < intersections.size(); i++) {
				if (inside) {
					int startX = intersections.get(i - 1);
					int endX = intersections.get(i);
					KeyFrame frame = new KeyFrame(Duration.millis(y * delay), e -> {
						graphics.drawLine(startX, currentY, endX, currentY);
						view.updateView();
					});
					timeline.getKeyFrames().add(frame);
				}
				inside = !inside;
			}
		}
		long end = System.nanoTime();
		timeline.play();
		return end - start;
	}
}
