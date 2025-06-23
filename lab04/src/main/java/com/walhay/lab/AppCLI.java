package com.walhay.lab;

import com.walhay.lab.graphics.Algorithms;
import com.walhay.lab.utils.EllipseDrawer;
import com.walhay.lab.utils.EllipseType;

import javafx.util.Pair;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class AppCLI {
	public static void main(String[] args) throws IOException {
		try (FileReader reader = new FileReader(args[0])) {
			JSONTokener tokener = new JSONTokener(reader);
			JSONObject json = new JSONObject(tokener);

			EllipseType type = EllipseType.valueOf(json.getString("algo"));

			int count = json.getInt("count");

			Color color = Color.BLACK;
			if (json.has("color"))
				color = parseColor(json.getJSONArray("color"));

			Point canvasSize = parseIntPair(json.getJSONArray("canvas"));
			Point center = parseIntPair(json.getJSONArray("center"));
			Point ab = parseIntPair(json.getJSONArray("ab"));

			if(json.has("points"))
				Algorithms.setPointsCount(json.getInt("points"));

			BufferedImage image = new BufferedImage(canvasSize.x, canvasSize.y, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, canvasSize.x, canvasSize.y);

			long nano = EllipseDrawer.measureTimeCircle(type, image, color, center, new Pair<>(ab.x, ab.y), count);

			double milliSecs = (double)nano / 1e6;

			JSONObject report = generateReport(milliSecs);
			System.out.println(report);

			if (json.has("outfile")) {
				String outFileStr = json.getString("outfile");
				if(outFileStr != null && !outFileStr.equals("none"))
					ImageIO.write(image, "png", new File(outFileStr));
			}
		} catch (Exception e) {
		}
	}

	private static Color parseColor(JSONArray array) {
		int r = array.getInt(0);
		int g = array.getInt(1);
		int b = array.getInt(2);
		return new Color(r, g, b);
	}

	private static Point parseIntPair(JSONArray array) {
		int x = array.getInt(0);
		int y = array.getInt(1);
		return new Point(x, y);
	}

	private static JSONObject generateReport(double time) {
		return new JSONObject().put("elTime", time);
	}
}
