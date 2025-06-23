package com.walhay.lab.widgets;

import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ResizableCanvas extends Canvas {
	private final GraphicsContext gc = getGraphicsContext2D();
	private BufferedImage image = null;

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public void clean() {
		gc.setFill(Color.WHITE);
		gc.clearRect(0, 0, getWidth(), getHeight());
	}

	public void update() {
		clean();
		gc.drawImage(SwingFXUtils.toFXImage(image, null), 0, 0);
	}

	@Override
	public boolean isResizable() {
		return false;
	}
}
