package com.walhay.lab.gui;

import com.walhay.lab.interfaces.IModel;
import com.walhay.lab.interfaces.IView;
import com.walhay.lab.utils.CircleDrawer;
import com.walhay.lab.utils.ColorUtils;
import com.walhay.lab.utils.LineData;
import com.walhay.lab.utils.LineDrawer;
import com.walhay.lab.utils.LineType;
import com.walhay.lab.widgets.AlgoEntry;
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
	private final List<LineData> drawData = new LinkedList<>();

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
		for (LineData data : drawData) {
			builder.append(String.format(Locale.US, "Type: %s\n", data.type().getName()))
				.append(String.format(Locale.US, "Time: %f ms\n", data.time() / 1e6))
				.append(String.format(Locale.US, "Pixels: %d\n", data.pixels()))
				.append(String.format(Locale.US, "Steps: %d\n\n", data.steps()));
		}

		return builder.toString();
	}

	public void clearCanvas(Color color) {
		Graphics g = image.getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		updateView();
	}

	public void drawLines(Iterator<AlgoEntry> it, Point start, Point end) {
		drawData.clear();
		while (it != null && it.hasNext()) {
			AlgoEntry entry = it.next();

			LineData data =
				LineDrawer.executeDraw(entry.getType(), image, entry.getColor(), start, end);
			drawData.add(data);
		}
		updateView();
	}

	public void drawCircle(LineType type, Color color, int points) {
		CircleDrawer.drawCircle(type, image, color, points);
		updateView();
	}
}
