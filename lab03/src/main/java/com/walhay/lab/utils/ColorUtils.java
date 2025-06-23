package com.walhay.lab.utils;

import javafx.scene.paint.Color;

public class ColorUtils {
	private static int normalizeColorInt(double value) {
		return Math.min(255,(int)(255 * value));
	}

	private static double normalizeColorDouble(int value)
	{
		return value / 255.0f;
	}

	public static java.awt.Color toAwtColor(Color color) {
		int r = normalizeColorInt(color.getRed());
		int g = normalizeColorInt(color.getGreen());
		int b = normalizeColorInt(color.getBlue());
		int alpha = normalizeColorInt(color.getOpacity());
		return new java.awt.Color(r, g, b, alpha);
	}

	public static Color toFXColor(java.awt.Color color) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		double alpha = normalizeColorDouble(color.getAlpha());
		return Color.rgb(r, g, b, Math.min(alpha, 1.0f));
	}

	public static java.awt.Color colorWithIntensity(java.awt.Color color, double intensity) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		return new java.awt.Color(r, g, b, normalizeColorInt(intensity));
	}
}
