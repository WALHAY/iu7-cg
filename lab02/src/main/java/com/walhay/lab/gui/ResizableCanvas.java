package com.walhay.lab.gui;

import com.walhay.lab.image.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

public class ResizableCanvas extends Canvas {
	private NewKirby kirby;

	public ResizableCanvas() {
		widthProperty().addListener(e -> update());
		heightProperty().addListener(e -> update());
	}

	public void setKirby(NewKirby kirby) {
		this.kirby = kirby;
		update();
	}

	public void update() {
		if (getWidth() <= 0 || getHeight() <= 0)
			return;

		GraphicsContext gc = getGraphicsContext2D();

		gc.drawImage(getWritableImage(), 0, 0);
	}

	private WritableImage getWritableImage() {
		int width = (int)getWidth();
		int height = (int)getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		var gc = (Graphics2D)image.getGraphics();
		gc.setColor(Color.WHITE);
		gc.fillRect(0, 0, width, height);

		gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		kirby.draw(gc);

		int[] pixels = new int[width * height];

		image.getRGB(0, 0, width, height, pixels, 0, width);

		WritableImage writableImage = new WritableImage(width, height);
		writableImage.getPixelWriter().setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0,
												 width);

		return writableImage;
	}
}
