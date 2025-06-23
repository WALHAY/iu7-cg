package com.walhay.lab.gui;

import com.walhay.lab.graphics.Algorithms;
import com.walhay.lab.interfaces.IModel;
import com.walhay.lab.interfaces.IView;
import com.walhay.lab.utils.DrawData;
import com.walhay.lab.utils.EllipseDrawer;
import com.walhay.lab.utils.EllipseType;
import com.walhay.lab.utils.WatermelonDrawer;
import com.walhay.lab.utils.WatermelonType;
import com.walhay.lab.widgets.AlgoEntry;

import javafx.util.Pair;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class Model implements IModel {
	private IView view;

	private final int WIDTH = 1000;
	private final int HEIGHT = 1000;
	private final List<DrawData> drawData = new LinkedList<>();

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

	public Model() {
		clearCanvas(Color.WHITE);
	}

	@Override
	public void setView(IView view) {
		this.view = view;
	}

	private void updateView() {
		if (view == null)
			return;

		view.updateView();
	}

	public BufferedImage getImage() {
		return image;
	}

	public String getDrawReport() {
		StringBuilder builder = new StringBuilder();
		for (DrawData data : drawData) {
			builder.append(String.format(Locale.US, "Type: %s\n", data.type().toString()))
				.append(String.format(Locale.US, "Time: %f ms\n", data.time() / 1e6));
		}

		return builder.toString();
	}

	public void clearCanvas(Color color) {
		Graphics g = image.getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		updateView();
	}

	public Point getImageCenter() {
		return new Point(image.getWidth() / 2, image.getHeight() / 2);
	}

	public void drawEllipses(Iterator<AlgoEntry<EllipseType>> it, Pair<Integer, Integer> radiuses) {
		drawData.clear();
		while (it != null && it.hasNext()) {
			AlgoEntry<EllipseType> entry = it.next();

			if(entry.getType().equals(EllipseType.PARAM))
				Algorithms.setPointsCount(entry.getPoints());

			DrawData data = EllipseDrawer.drawEllipse(entry.getType(), image, entry.getColor(), getImageCenter(), radiuses);
			drawData.add(data);
		}
		updateView();
	}

	public void drawWatermelons(Iterator<AlgoEntry<EllipseType>> it, Pair<Integer, Integer> radiuses, WatermelonType type, int parts)
	{
		drawData.clear();
		while (it != null && it.hasNext()) {
			AlgoEntry<EllipseType> entry = it.next();

			if(entry.getType().equals(EllipseType.PARAM))
				Algorithms.setPointsCount(entry.getPoints());

			WatermelonDrawer.drawWatermelon(entry.getType(), image, entry.getColor(), getImageCenter(), radiuses, type, parts);
		}
		updateView();
	}
}
