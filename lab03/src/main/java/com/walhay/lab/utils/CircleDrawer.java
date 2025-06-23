package com.walhay.lab.utils;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class CircleDrawer {
	public static void drawCircle(LineType type, BufferedImage image, Color color, int points) {
		int offsetX = (int)Math.floor(image.getWidth() / 2.0);
		int offsetY = (int)Math.floor(image.getHeight() / 2.0);

		int radius = Math.min(offsetX, offsetY) - 1;

		Point first = new Point(offsetX, offsetY);
		Point second = new Point();
		for (int i = 0; i < points; ++i) {
			double phi = 2 * Math.PI * i / points;

			int x = (int)(Math.cos(phi) * radius);
			int y = (int)(Math.sin(phi) * radius);

			second.setLocation(offsetX + x, offsetY + y);

			LineDrawer.executeDraw(type, image, color, first, second);
		}
	}
}
