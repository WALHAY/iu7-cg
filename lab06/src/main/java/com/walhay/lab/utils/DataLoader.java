package com.walhay.lab.utils;

import java.awt.Color;
import java.awt.Point;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public final class DataLoader {

	private DataLoader() {}

	private static JSONArray pointToJson(Point point) {
		return new JSONArray().put(point.x).put(point.y);
	}

	private static Point jsonToPoint(JSONArray json) {
		return new Point(json.getInt(0), json.getInt(1));
	}

	private static JSONArray polygonToJson(CustomPolygon polygon) {
		JSONArray data = new JSONArray();

		polygon.getVertices().stream().map(DataLoader::pointToJson).forEach(data::put);
		return data;
	}

	private static CustomPolygon jsonToPolygon(JSONArray json) {
		CustomPolygon polygon = new CustomPolygon();
		for (int i = 0; i < json.length(); ++i) {
			Point point = jsonToPoint(json.getJSONArray(i));
			polygon.getVertices().add(point);
		}
		return polygon;
	}

	public static void saveData(CustomPolygon polygon, Color[] colors, String filename) throws IOException {
		JSONObject serializedData = new JSONObject();

		serializedData.put("polygonColor", colors[0].getRGB());
		serializedData.put("holeColor", colors[1].getRGB());
		serializedData.put("fillColor", colors[2].getRGB());
		serializedData.put("polygon", polygonToJson(polygon));

		JSONArray holesJson = new JSONArray();
		polygon.getPolygons().stream().map(DataLoader::polygonToJson).forEach(holesJson::put);
		serializedData.put("holes", holesJson);

		try (PrintWriter writer = new PrintWriter(filename)) {
			writer.write(serializedData.toString());
		}
	}

	public static Pair<Color[], CustomPolygon> loadData(String filename) throws IOException {
		Pair<Color[], CustomPolygon> result = null;
		try (FileReader reader = new FileReader(filename)) {

			JSONTokener tokener = new JSONTokener(reader);
			JSONObject json = new JSONObject(tokener);

			Color fillColor = new Color(json.getInt("fillColor"));
			Color holeColor = new Color(json.getInt("holeColor"));
			Color polygonColor = new Color(json.getInt("polygonColor"));

			CustomPolygon polygon = jsonToPolygon(json.getJSONArray("polygon"));
			JSONArray array = json.getJSONArray("holes");
			for (int i = 0; i < array.length(); ++i) {
				CustomPolygon hole = jsonToPolygon(array.getJSONArray(i));

				polygon.getPolygons().add(hole);
			}

			result = new Pair<>(new Color[] {polygonColor, holeColor, fillColor}, polygon);
		}
		return result;
	}
}
