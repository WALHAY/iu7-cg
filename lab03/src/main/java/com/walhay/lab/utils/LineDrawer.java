package com.walhay.lab.utils;

import com.walhay.lab.graphics.Algorithms;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class LineDrawer {
	public static long measureTime(LineType type, BufferedImage image, Color color, Point first, Point second,
								   int count) {
		long time = 0;
		for (int i = 0; i < count; ++i) {
			long start = System.nanoTime();
			LineData data = executeDraw(type, image, color, first, second);
			long end = System.nanoTime();
			time += type.equals(LineType.LIB) ? data.time() : (end - start) ;
		}
		return time / count;
	}

	public static LineData executeDraw(LineType type, BufferedImage image, Color color, Point first, Point second) {
		switch (type) {
			case LIB:
				return Algorithms.lineLib(image, color, first, second);
			case DDA:
				return Algorithms.lineDDA(image, color, first, second);
			case BRS_AA:
				return Algorithms.lineBresenhamSteps(image, color, first, second);
			case BRS_F:
				return Algorithms.lineBresenhamReal(image, color, first, second);
			case BRS_INT:
				return Algorithms.lineBresenhamInt(image, color, first, second);
			case WU:
				return Algorithms.lineWu(image, color, first, second);
			default:
				return null;
		}
	}
}
