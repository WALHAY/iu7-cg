package com.walhay.lab.utils;

import com.walhay.lab.Constants;
import com.walhay.lab.graphics.Algorithms;
import java.util.function.BiFunction;

public class Functions {
	public static Algorithms first = new Algorithms(
			Constants.WIDTH,
			Constants.HEIGHT,
			-Math.PI * 2,
			Math.PI * 2,
			0.1,
			-Math.PI * 2,
			Math.PI * 2,
			0.1,
			new BiFunction<Double, Double, Double>() {
				@Override
				public Double apply(Double x, Double z) {
					double a = Math.pow(x - Math.PI, 2) + Math.pow(z - Math.PI, 2);
					return 1 / 5 * Math.sin(x) * Math.cos(z) - 3 / 2 * Math.cos(7 * a / 4) * Math.exp(-a);
				}
			});

	public static Algorithms second = new Algorithms(
			Constants.WIDTH, Constants.HEIGHT, -3, 3, 0.1, -3, 3, 0.1, new BiFunction<Double, Double, Double>() {
				@Override
				public Double apply(Double x, Double z) {
					return 1 - Math.abs(x + z) - Math.abs(z - x);
				}
			});

	public static Algorithms third = new Algorithms(
			Constants.WIDTH, Constants.HEIGHT, -3, 3, 0.1, -3, 3, 0.1, new BiFunction<Double, Double, Double>() {
				@Override
				public Double apply(Double x, Double z) {
					return Math.exp(Math.sin(Math.pow(x, 2) + Math.pow(z, 2)));
				}
			});
}
