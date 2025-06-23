package com.walhay.lab.utils;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javafx.util.Pair;

public class WatermelonDrawer {
	public static void drawWatermelon(EllipseType type, BufferedImage image, Color color, Point center, Pair<Integer, Integer> radiuses, WatermelonType watermelonType, int parts) {
		if(parts <= 0)
			return;

		int diffX = radiuses.getKey() / parts;
		int diffY = radiuses.getValue() / parts;

		for (int i = 1; i < parts; ++i)
		{
			Pair<Integer, Integer> newRad = null;
			if(watermelonType.equals(WatermelonType.VERTICAL))
 				newRad = new Pair<>(diffX * i, radiuses.getValue());
			else
 				newRad = new Pair<>(radiuses.getKey(), diffY * i);
			EllipseDrawer.drawEllipse(type, image, color, center, newRad);
		}
	}
}
