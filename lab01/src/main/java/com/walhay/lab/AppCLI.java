package com.walhay.lab;

import com.walhay.lab.gui.DrawPlot;
import com.walhay.lab.utils.Geometry;
import com.walhay.lab.utils.Geometry.Triangle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

public class AppCLI {
	public static final int GENERATED_WIDTH = 1920;
	public static final int GENERATED_HEIGHT = 1080;

	public static final int ERROR_POINTS = 1;
	public static final int ERROR_TRIANGLES = 2;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Geometry geometry = new Geometry();

		System.out.println("Enter point count(>=6): ");
		int points = scanner.nextInt();

		if (points < 6) {
			System.out.println("Required at least 6 points");
			scanner.close();
			System.exit(ERROR_POINTS);
		}

		while (points-- > 0) {
			System.out.println("Enter point x and y: ");

			double x = scanner.nextDouble();
			double y = scanner.nextDouble();

			geometry.addPoint(x, y);
		}

		scanner.close();
		if (geometry.getPoints().size() < 6) {
			System.out.println("Required at least 6 unique points");
			System.exit(ERROR_POINTS);
		}

		Set<Geometry.Triangle> triangles = geometry.getAllTriangles();
		List<Geometry.Triangle> result = geometry.findTriangles(triangles);

		System.out.println("Result: ");
		if (result == null || result.isEmpty()) {
			System.out.println("No matching triangles were found");
			System.exit(ERROR_TRIANGLES);
		} else
			for (Triangle triangle : result) System.out.println(triangle);

		DrawPlot plot = new DrawPlot(GENERATED_WIDTH, GENERATED_HEIGHT);
		plot.setPoints(geometry.getPoints());
		plot.setTriangleSelection(result);
		try {
			File path = new File("generated.bmp");
			ImageIO.write(redrawImage(plot.getImage()), "bmp", path);
			System.out.println(
				String.format("Kartinochka generated uspeshno. Smotrite v %s.", path.getAbsoluteFile().toString()));
		} catch (Exception e) {
			System.out.println("Oshibka pri zapisi kartinochki");
		}
	}

	public static BufferedImage redrawImage(BufferedImage image) {
		if (image == null)
			return null;

		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics graphics = newImage.getGraphics();
		graphics.drawImage(image, 0, 0, Color.WHITE, null);
		return newImage;
	}
}
