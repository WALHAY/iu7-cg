package com.walhay.lab.graphics;

import com.walhay.lab.utils.ColorUtils;
import com.walhay.lab.utils.LineData;
import com.walhay.lab.utils.LineType;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Point;

public class Algorithms {
	private final static double EPS = 1e-8;

	public static int lineSteps(Point first, Point second) {
		return Math.min(Math.abs(second.x - first.x), Math.abs(second.y - first.y));
	}

	public static LineData lineLib(BufferedImage image, Color color, Point first, Point second) {
		Graphics graphics = image.getGraphics();
		graphics.setColor(color);
		long start = System.nanoTime();
		graphics.drawLine(first.x, first.y, second.x, second.y);
		long end = System.nanoTime();

		return new LineData(LineType.LIB, end - start, second.x - first.x, lineSteps(first, second));
	}

	public static LineData lineDDA(BufferedImage image, Color color, Point first, Point second) {
		long start = System.nanoTime();
		int dx = Math.abs(second.x - first.x);
		int dy = Math.abs(second.y - first.y);

		int length = Math.max(dx, dy);

		double s1 = (double)(second.x - first.x) / length;
		double s2 = (double)(second.y - first.y) / length;

		double x = first.x;
		double y = first.y;

		for (int i = 0; i <= length; ++i) {
			image.setRGB((int)Math.round(x), (int)Math.round(y), color.getRGB());
			x += s1;
			y += s2;
		}
		long end = System.nanoTime();
		return new LineData(LineType.DDA, end - start, length, lineSteps(first, second));
	}

	public static LineData lineBresenhamInt(BufferedImage image, Color color, Point first, Point second) {
		long start = System.nanoTime();
		int x = first.x;
		int y = first.y;

		int dx = second.x - first.x;
		int dy = second.y - first.y;

		int s1 = (int)Math.signum(dx);
		int s2 = (int)Math.signum(dy);

		dx = Math.abs(dx);
		dy = Math.abs(dy);

		boolean swapped = false;
		if (dy > dx) {
			int temp = dx;
			dx = dy;
			dy = temp;
			swapped = true;
		}

		int e = 2 * dy - dx;

		for (int i = 0; i <= dx; ++i) {
			image.setRGB(x, y, color.getRGB());

			while (e > 0) {
				if (swapped)
					x += s1;
				else
					y += s2;

				e -= 2 * dx;
			}

			if (swapped)
				y += s2;
			else
				x += s1;
			e += 2 * dy;
		}
		long end = System.nanoTime();
		return new LineData(LineType.BRS_INT, end - start, (int)Math.abs(dx), lineSteps(first, second));
	}

	public static LineData lineBresenhamReal(BufferedImage image, Color color, Point first, Point second) {
		long start = System.nanoTime();
		int x = first.x;
		int y = first.y;

		int dx = second.x - first.x;
		int dy = second.y - first.y;

		int s1 = (int)Math.signum(dx);
		int s2 = (int)Math.signum(dy);

		dx = Math.abs(dx);
		dy = Math.abs(dy);

		boolean swapped = false;
		if (dy > dx) {
			int temp = dx;
			dx = dy;
			dy = temp;
			swapped = true;
		}

		final double m = (double)dy / dx;
		double e = m - 0.5;

		for (int i = 0; i <= dx; ++i) {
			image.setRGB(x, y, color.getRGB());

			while (e > EPS) {
				if (swapped)
					x += s1;
				else
					y += s2;

				e--;
			}

			if (swapped)
				y += s2;
			else
				x += s1;
			e += m;
		}
		long end = System.nanoTime();
		return new LineData(LineType.BRS_F, end - start, (int)Math.abs(dx), lineSteps(first, second));
	}

	public static LineData lineBresenhamSteps(BufferedImage image, Color color, Point first, Point second) {
		long start = System.nanoTime();
		int x = first.x;
		int y = first.y;

		int dx = second.x - first.x;
		int dy = second.y - first.y;

		int s1 = (int)Math.signum(dx);
		int s2 = (int)Math.signum(dy);

		dx = Math.abs(dx);
		dy = Math.abs(dy);

		boolean swapped = false;
		if (dy > dx) {
			int temp = dx;
			dx = dy;
			dy = temp;
			swapped = true;
		}

		final double m = (double)dy / dx;
		double w = 1 - m;

		double e = 0.5;

		for (int i = 0; i <= dx; ++i) {
			image.setRGB(x, y, ColorUtils.colorWithIntensity(color, e).getRGB());
			if(e < w)
			{
				if(swapped)
					y += s2;
				else
					x += s1;

				e += m;
			} else {
				x += s1;
				y += s2;
				e -= w;
			}
		}
		long end = System.nanoTime();
		return new LineData(LineType.BRS_AA, end - start, (int)Math.abs(dx), lineSteps(first, second));
	}

	public static LineData lineWu(BufferedImage image, Color color, Point first, Point second) {
		long start = System.nanoTime();
		int dx = second.x - first.x;
		int dy = second.y - first.y;

		int s1 = (int)Math.signum(dx);
		int s2 = (int)Math.signum(dy);

		dx = Math.abs(dx);
		dy = Math.abs(dy);

		boolean swapped = false;
		if (dy > dx) {
			int temp = dx;
			dx = dy;
			dy = temp;
			swapped = true;
		}

		double m = dx > 0 ? (double)dy / dx : 1;

		m *= swapped ? s1 : s2;

		double x = first.x;
		double y = first.y;

		for (int i = 0; i <= dx; ++i) {
			double intensity = swapped ? x - (int)x : y - (int)y;

			image.setRGB((int)x, (int)y, ColorUtils.colorWithIntensity(color, 1 - intensity).getRGB());

			if(swapped)
				image.setRGB((int)x + 1, (int)y, ColorUtils.colorWithIntensity(color, intensity).getRGB());
			else
				image.setRGB((int)x, (int)y + 1, ColorUtils.colorWithIntensity(color, intensity).getRGB());

			if (swapped) {
				y += s2;
				x += m;
			} else {
				x += s1;
				y += m;
			}
		}

		long end = System.nanoTime();
		return new LineData(LineType.WU, end - start, (dx + 1) * 2, lineSteps(first, second));
	}
}
