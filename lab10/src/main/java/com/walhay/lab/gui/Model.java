package com.walhay.lab.gui;

import com.walhay.lab.Constants;
import com.walhay.lab.graphics.Algorithms;
import com.walhay.lab.interfaces.IModel;
import com.walhay.lab.interfaces.IView;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import lombok.Getter;
import lombok.Setter;

public class Model implements IModel {
	@Setter
	private IView view;

	private double angleX = 15;
	private double angleY = 15;
	private double angleZ = 0;

	@Getter
	private final BufferedImage image =
			new BufferedImage(Constants.WIDTH, Constants.HEIGHT, BufferedImage.TYPE_INT_ARGB);

	private final Graphics2D graphics = (Graphics2D) image.getGraphics();

	private Algorithms function = null;

	public Model() {
		clearCanvas();
	}

	void updateView() {
		if (view == null) {
			return;
		}

		view.updateView();
	}

	public void setFunction(Algorithms algo) {
		this.function = algo;
		draw();
	}

	public void setScale(double scale) {
		function.setScale(scale);
		draw();
	}

	public void addAngle(double x, double y, double z) {
		angleX = (angleX + x) % 360;
		angleY = (angleY + y) % 360;
		angleZ = (angleZ + z) % 360;
		draw();
	}

	public void setX(double min, double max, double step) {
		function.setX(min, max, step);
		draw();
	}

	public void setZ(double min, double max, double step) {
		function.setZ(min, max, step);
		draw();
	}

	@Override
	public void draw() {
		clearCanvas();
		if (function == null) {
			return;
		}

		graphics.setColor(Color.BLACK);
		function.floatingHorizon(graphics, angleX, angleY, angleZ);
		updateView();
	}

	public void clearScreen() {
		clearCanvas();
		updateView();
	}

	private void clearCanvas() {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
		updateView();
	}
}
