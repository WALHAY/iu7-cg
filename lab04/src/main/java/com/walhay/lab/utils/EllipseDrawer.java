package com.walhay.lab.utils;

import com.walhay.lab.graphics.Algorithms;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javafx.util.Pair;

public class EllipseDrawer {
	public static long measureTimeCircle(EllipseType type, BufferedImage image, Color color, Point center,
										 Pair<Integer, Integer> radiuses, int count) {
		long time = 0;
		for (int i = 0; i < count; ++i)
			time += drawEllipse(type, image, color, center, radiuses).time();

		return time;
	}

	public static DrawData drawEllipse(EllipseType type, BufferedImage image, Color color, Point center,
								   Pair<Integer, Integer> radiuses) {
		switch (type) {
			case GENERAL:
				return Algorithms.ellipseGeneral(image, color, center, radiuses);
			case PARAM:
				return Algorithms.ellipseParam(image, color, center, radiuses);
			default:
				return null;
		}
	}
}
