package com.walhay.lab.graphics;

import java.awt.*;
import java.util.function.BiFunction;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import lombok.Getter;
import lombok.Setter;

public class Algorithms {
	private final int screenWidth;
	private final int screenHeight;

	@Getter
	private double xMin;

	@Getter
	private double xMax;

	@Getter
	private double xStep;

	@Getter
	private double zMin;

	@Getter
	private double zMax;

	@Getter
	private double zStep;

	@Setter
	@Getter
	private double scale = 1;

	private double[] upperHorizon;
	private double[] lowerHorizon;

	@Setter
	private BiFunction<Double, Double, Double> function = null;

	public Algorithms(
			int screenWidth,
			int screenHeight,
			double xMin,
			double xMax,
			double xStep,
			double zMin,
			double zMax,
			double zStep,
			BiFunction<Double, Double, Double> function) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		this.xMin = xMin;
		this.xMax = xMax;
		this.xStep = xStep;

		this.zMin = zMin;
		this.zMax = zMax;
		this.zStep = zStep;

		this.function = function;

		this.upperHorizon = new double[screenWidth];
		this.lowerHorizon = new double[screenWidth];
	}

	public void setX(double min, double max, double step) {
		this.xMin = min;
		this.xMax = max;
		this.xStep = step;
	}

	public void setZ(double min, double max, double step) {
		this.zMin = min;
		this.zMax = max;
		this.zStep = step;
	}

	private Point3D rotate(double x, double y, double z, double angleX, double angleY, double angleZ) {
		final Rotate rotatorX = new Rotate(angleX, 0, 0, 0, Rotate.X_AXIS);
		final Rotate rotatorY = new Rotate(angleY, 0, 0, 0, Rotate.Y_AXIS);
		final Rotate rotatorZ = new Rotate(angleZ, 0, 0, 0, Rotate.Z_AXIS);

		Point3D rotatedX = rotatorX.transform(x, y, z);
		Point3D rotatedY = rotatorY.transform(rotatedX);

		return rotatorZ.transform(rotatedY);
	}

	private Point3D scale(Point3D point, double scale) {
		return new Scale(scale, scale, scale).transform(point);
	}

	private int toScreenX(double x) {
		double normalized = (x - xMin) / (xMax - xMin);

		normalized = Math.max(0, Math.min(1, normalized));
		return (int) (normalized * (screenWidth - 1));
	}

	private int toScreenY(double y) {
		double normalized = 1 - (y + 1) / 2;

		normalized = Math.max(0, Math.min(1, normalized));
		return (int) (normalized * (screenHeight - 1));
	}

	private int visibility(int x, double y, double[] upper, double[] lower) {
		if (x < 0 || x >= screenWidth) {
			return 0;
		}

		if (y >= upper[x]) {
			return 1;
		}
		if (y <= lower[x]) {
			return -1;
		}
		return 0;
	}

	private void updateHorizon(int x1, double y1, int x2, double y2, double[] upper, double[] lower) {
		if (x1 == x2) {
			upper[x1] = Math.max(upper[x1], y2);
			lower[x1] = Math.min(lower[x1], y2);
			return;
		}

		double slope = (y2 - y1) / (x2 - x1);
		for (int x = x1; x <= x2; x++) {
			double y = slope * (x - x1) + y1;
			upper[x] = Math.max(upper[x], y);
			lower[x] = Math.min(lower[x], y);
		}
	}

	private Point2D findIntersection(int x1, double y1, int x2, double y2, double[] horizon) {
		if (x1 == x2) {
			return new Point2D(x2, horizon[x2]);
		}

		double slope = (y2 - y1) / (x2 - x1);
		double y = y1 + slope;
		int x = x1 + 1;
		int prevSign = (int) Math.signum(y1 + slope - horizon[x1 + 1]);
		int currSign = prevSign;

		while (currSign == prevSign && x < x2) {
			y += slope;
			x++;
			currSign = (int) Math.signum(y - horizon[x]);
		}

		if (Math.abs(y - slope - horizon[x - 1]) < Math.abs(y - horizon[x])) {
			y -= slope;
			x--;
		}

		return new Point2D(x, y);
	}

	private void drawLine(Graphics graphics, int x0, int y0, int x1, int y1) {
		graphics.drawLine(x0, y0, x1, y1);
	}

	public void floatingHorizon(Graphics graphics, double angleX, double angleY, double angleZ) {
		if (function == null) {
			return;
		}

		for (int i = 0; i < screenWidth; i++) {
			upperHorizon[i] = 0;
			lowerHorizon[i] = screenHeight;
		}

		int leftX = -1;
		int leftY = -1;
		int rightX = -1;
		int rightY = -1;

		for (double z = zMax; z >= zMin; z -= zStep) {
			double prevX = xMin;
			double prevY = function.apply(xMin, z);

			Point3D prevRotated = rotate(prevX, prevY, z, angleX, angleY, angleZ);
			prevRotated = scale(prevRotated, scale);

			int prevScreenX = toScreenX(prevRotated.getX());
			int prevScreenY = toScreenY(prevRotated.getY());

			if (leftX == -1) {
				leftX = prevScreenX;
				leftY = prevScreenY;
			} else {
				updateHorizon(leftX, leftY, prevScreenX, prevScreenY, upperHorizon, lowerHorizon);
				leftX = prevScreenX;
				leftY = prevScreenY;
			}

			int prevVisibility = visibility(prevScreenX, prevScreenY, upperHorizon, lowerHorizon);

			for (double x = xMin + xStep; x <= xMax; x += xStep) {
				double y = function.apply(x, z);

				Point3D currRotated = rotate(x, y, z, angleX, angleY, angleZ);
				currRotated = scale(currRotated, scale);

				int currScreenX = toScreenX(currRotated.getX());
				int currScreenY = toScreenY(currRotated.getY());

				if (currScreenX < 0 || currScreenX >= screenWidth) {
					continue;
				}

				int currVisibility = visibility(currScreenX, currScreenY, upperHorizon, lowerHorizon);

				if (currVisibility == prevVisibility) {
					if (currVisibility != 0) {
						drawLine(graphics, prevScreenX, prevScreenY, currScreenX, currScreenY);
						updateHorizon(prevScreenX, prevScreenY, currScreenX, currScreenY, upperHorizon, lowerHorizon);
					}
				} else {
					Point2D inter = null;

					if (currVisibility == 0) {
						if (prevVisibility == 1) {
							inter = findIntersection(prevScreenX, prevScreenY, currScreenX, currScreenY, upperHorizon);
						} else {
							inter = findIntersection(prevScreenX, prevScreenY, currScreenX, currScreenY, lowerHorizon);
						}
						drawLine(graphics, prevScreenX, prevScreenY, (int) inter.getX(), (int) inter.getY());
						updateHorizon(
								prevScreenX, prevScreenY, (int) inter.getX(), inter.getY(), upperHorizon, lowerHorizon);
					} else if (currVisibility == 1) {
						if (prevVisibility == 0) {
							inter = findIntersection(prevScreenX, prevScreenY, currScreenX, currScreenY, upperHorizon);
							drawLine(graphics, (int) inter.getX(), (int) inter.getY(), currScreenX, currScreenY);
							updateHorizon(
									(int) inter.getX(),
									inter.getY(),
									currScreenX,
									currScreenY,
									upperHorizon,
									lowerHorizon);
						} else {
							inter = findIntersection(prevScreenX, prevScreenY, currScreenX, currScreenY, lowerHorizon);
							drawLine(graphics, prevScreenX, prevScreenY, (int) inter.getX(), (int) inter.getY());
							updateHorizon(
									prevScreenX,
									prevScreenY,
									(int) inter.getX(),
									inter.getY(),
									upperHorizon,
									lowerHorizon);

							inter = findIntersection(prevScreenX, prevScreenY, currScreenX, currScreenY, upperHorizon);
							drawLine(graphics, (int) inter.getX(), (int) inter.getY(), currScreenX, currScreenY);
							updateHorizon(
									(int) inter.getX(),
									inter.getY(),
									currScreenX,
									currScreenY,
									upperHorizon,
									lowerHorizon);
						}
					} else {
						if (prevVisibility == 0) {
							inter = findIntersection(prevScreenX, prevScreenY, currScreenX, currScreenY, lowerHorizon);
							drawLine(graphics, (int) inter.getX(), (int) inter.getY(), currScreenX, currScreenY);
							updateHorizon(
									(int) inter.getX(),
									inter.getY(),
									currScreenX,
									currScreenY,
									upperHorizon,
									lowerHorizon);
						} else {
							inter = findIntersection(prevScreenX, prevScreenY, currScreenX, currScreenY, upperHorizon);
							drawLine(graphics, prevScreenX, prevScreenY, (int) inter.getX(), (int) inter.getY());
							updateHorizon(
									prevScreenX,
									prevScreenY,
									(int) inter.getX(),
									inter.getY(),
									upperHorizon,
									lowerHorizon);

							inter = findIntersection(prevScreenX, prevScreenY, currScreenX, currScreenY, lowerHorizon);
							drawLine(graphics, (int) inter.getX(), (int) inter.getY(), currScreenX, currScreenY);
							updateHorizon(
									(int) inter.getX(),
									inter.getY(),
									currScreenX,
									currScreenY,
									upperHorizon,
									lowerHorizon);
						}
					}
				}

				prevScreenX = currScreenX;
				prevScreenY = currScreenY;
				prevVisibility = currVisibility;
			}

			if (rightX == -1) {
				rightX = prevScreenX;
				rightY = prevScreenY;
			} else {
				updateHorizon(prevScreenX, prevScreenY, rightX, rightY, upperHorizon, lowerHorizon);
				rightX = prevScreenX;
				rightY = prevScreenY;
			}
		}
	}
}
