package com.walhay.lab.graphics;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.walhay.lab.utils.DrawData;
import com.walhay.lab.utils.EllipseType;

import javafx.util.Pair;

public class Algorithms {
	private static int points = 100;

	public static void setPointsCount(int count)
	{
		points = count;
	}

	public static DrawData ellipseParam(BufferedImage image, Color color, Point center, Pair<Integer, Integer> radiuses) {
		long start = System.nanoTime();
		int ra = radiuses.getKey();
		int rb = radiuses.getValue();

		int x0 = center.x;
		int y0 = center.y;

		int rgbInt = color.getRGB();
		for (int i = 0; i < points; ++i) {
			double phi = 2 * Math.PI * i / points;

			int x = (int)(Math.cos(phi) * ra);
			int y = (int)(Math.sin(phi) * rb);

			image.setRGB(x0 + x, y0 + y, rgbInt);
		}
		long end = System.nanoTime();
		return new DrawData(EllipseType.PARAM, end - start);
	}

	public static DrawData ellipseGeneral(BufferedImage image, Color color, Point center, Pair<Integer, Integer> radiuses) {
		long start = System.nanoTime();
		int x1 = center.x - radiuses.getKey();
		int x2 = center.x + radiuses.getKey();

		int y1 = center.y - radiuses.getValue();
		int y2 = center.y + radiuses.getValue();

		int x0 = center.x;
		int y0 = center.y;

		double aSq = Math.pow(radiuses.getKey(), 2);
		double bSq = Math.pow(radiuses.getValue(), 2);

		int rgbInt = color.getRGB();
		for (int x = x1; x <= x2; ++x) {
			int y = (int)Math.sqrt((1 - Math.pow(x - x0, 2) / aSq) * bSq);

			image.setRGB(x, y0 + y, rgbInt);
			image.setRGB(x, y0 - y, rgbInt);
		}

		for (int y = y1; y <= y2; ++y) {
			int x = (int)Math.sqrt((1 - Math.pow(y - y0, 2) / bSq) * aSq);

			image.setRGB(x0 + x, y, rgbInt);
			image.setRGB(x0 - x, y, rgbInt);
		}
		long end = System.nanoTime();
		return new DrawData(EllipseType.GENERAL, end - start);
	}
}
