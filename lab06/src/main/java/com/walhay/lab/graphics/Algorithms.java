package com.walhay.lab.graphics;

import com.walhay.lab.interfaces.IView;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.util.Pair;

public final class Algorithms {
	public static Timeline timeline = new Timeline();

	private Algorithms() {}

	public static long floodFill(BufferedImage image, Point start, Color borderColor, Color fillColor) {
		long startTime = System.nanoTime();
		int width = image.getWidth();
		int height = image.getHeight();

		int borderRGB = borderColor.getRGB();
		int fillRGB = fillColor.getRGB();

		Deque<Point> stack = new ArrayDeque<>();
		stack.push(start);

		while (!stack.isEmpty()) {
			Point point = stack.pop();
			int x = point.x;
			int y = point.y;

			image.setRGB(x, y, fillRGB);

			int tempX = x;

			x++;
			while (x < width && image.getRGB(x, y) != borderRGB && image.getRGB(x, y) != fillRGB) {
				image.setRGB(x, y, fillRGB);
				x++;
			}
			int rightX = x - 1;

			x = tempX;

			x--;
			while (x >= 0 && image.getRGB(x, y) != borderRGB && image.getRGB(x, y) != fillRGB) {
				image.setRGB(x, y, fillRGB);
				x--;
			}
			int leftX = x + 1;

			x = leftX;
			y = y + 1;
			if (y < height) {
				while (x <= rightX) {
					boolean flag = false;
					while (x <= rightX && image.getRGB(x, y) != borderRGB && image.getRGB(x, y) != fillRGB) {
						if (!flag) {
							flag = true;
						}
						x++;
					}

					if (flag) {
						if (x == rightX && image.getRGB(x, y) != borderRGB && image.getRGB(x, y) != fillRGB) {
							stack.push(new Point(x, y));
						} else {
							stack.push(new Point(x - 1, y));
						}
						flag = false;
					}

					int enterX = x;
					while (x <= rightX && (image.getRGB(x, y) == borderRGB || image.getRGB(x, y) == fillRGB)) {
						x++;
					}

					if (x == enterX) {
						x++;
					}
				}
			}

			x = leftX;
			y = y - 2;
			if (y >= 0) {
				while (x <= rightX) {
					boolean flag = false;
					while (x <= rightX && image.getRGB(x, y) != borderRGB && image.getRGB(x, y) != fillRGB) {
						if (!flag) {
							flag = true;
						}
						x++;
					}

					if (flag) {
						if (x == rightX && image.getRGB(x, y) != borderRGB && image.getRGB(x, y) != fillRGB) {
							stack.push(new Point(x, y));
						} else {
							stack.push(new Point(x - 1, y));
						}
						flag = false;
					}

					int xEnter = x;
					while (x <= rightX && (image.getRGB(x, y) == borderRGB || image.getRGB(x, y) == fillRGB)) {
						x++;
					}

					if (x == xEnter) {
						x++;
					}
				}
			}
		}
		long endTime = System.nanoTime();
		return endTime - startTime;
	}

	public static long floodFill(
			BufferedImage image, Point start, Color borderColor, Color fillColor, int delay, IView view) {
		timeline.stop();
		timeline.getKeyFrames().clear();

		long startTime = System.nanoTime();

		List<Pair<Pair<Integer, Integer>, Integer>> fillBorders = new LinkedList<>();

		int width = image.getWidth();
		int height = image.getHeight();

		int borderRGB = borderColor.getRGB();
		int fillRGB = fillColor.getRGB();

		Deque<Point> stack = new ArrayDeque<>();
		stack.push(start);

		var g = image.getGraphics();
		g.setColor(fillColor);
		while (!stack.isEmpty()) {
			Point point = stack.pop();
			int x = point.x;
			int y = point.y;

			image.setRGB(x, y, fillRGB);

			int tempX = x;

			x++;
			while (x < width && image.getRGB(x, y) != borderRGB && image.getRGB(x, y) != fillRGB) {
				image.setRGB(x, y, fillRGB);
				x++;
			}
			int rightX = x - 1;

			x = tempX;

			x--;
			while (x >= 0 && image.getRGB(x, y) != borderRGB && image.getRGB(x, y) != fillRGB) {
				image.setRGB(x, y, fillRGB);
				x--;
			}
			int leftX = x + 1;

			fillBorders.add(new Pair<>(new Pair<>(leftX, rightX), y));

			x = leftX;
			y = y + 1;
			if (y < height) {
				while (x <= rightX) {
					boolean flag = false;
					while (x <= rightX && image.getRGB(x, y) != borderRGB && image.getRGB(x, y) != fillRGB) {
						if (!flag) {
							flag = true;
						}
						x++;
					}

					if (flag) {
						if (x == rightX && image.getRGB(x, y) != borderRGB && image.getRGB(x, y) != fillRGB) {
							stack.push(new Point(x, y));
						} else {
							stack.push(new Point(x - 1, y));
						}
						flag = false;
					}

					int enterX = x;
					while (x <= rightX && (image.getRGB(x, y) == borderRGB || image.getRGB(x, y) == fillRGB)) {
						x++;
					}

					if (x == enterX) {
						x++;
					}
				}
			}

			x = leftX;
			y = y - 2;
			if (y >= 0) {
				while (x <= rightX) {
					boolean flag = false;
					while (x <= rightX && image.getRGB(x, y) != borderRGB && image.getRGB(x, y) != fillRGB) {
						if (!flag) {
							flag = true;
						}
						x++;
					}

					if (flag) {
						if (x == rightX && image.getRGB(x, y) != borderRGB && image.getRGB(x, y) != fillRGB) {
							stack.push(new Point(x, y));
						} else {
							stack.push(new Point(x - 1, y));
						}
						flag = false;
					}

					int xEnter = x;
					while (x <= rightX && (image.getRGB(x, y) == borderRGB || image.getRGB(x, y) == fillRGB)) {
						x++;
					}

					if (x == xEnter) {
						x++;
					}
				}
			}
		}
		long endTime = System.nanoTime();

		int counter = 0;
		for (var p : fillBorders) {
			KeyFrame frame = new KeyFrame(Duration.millis(counter++ * delay), e -> {
				g.drawLine(p.getKey().getKey(), p.getValue(), p.getKey().getValue(), p.getValue());
				view.updateView();
			});
			timeline.getKeyFrames().add(frame);
		}

		g.setColor(Color.WHITE);
		for (var p : fillBorders) {
			g.drawLine(p.getKey().getKey(), p.getValue(), p.getKey().getValue(), p.getValue());
		}

		g.setColor(fillColor);

		timeline.play();

		return endTime - startTime;
	}
}
