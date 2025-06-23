package com.walhay.lab;

import static com.walhay.lab.utils.Geometry.Triangle.countArea;
import static com.walhay.lab.utils.Geometry.Triangle.countPerimeter;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.walhay.lab.utils.Geometry;
import com.walhay.lab.utils.Geometry.Point;
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(PipelineTestResult.class)
public class GeometryTest {
	@Test
	void testCountPointsDistance() {
		assertEquals(5, Geometry.getDistanceBetweenPoints(new Point(3, 0), new Point(0, 4)));
	}

	@Test
	void testCountTrianglePerimeter() {
		Point a = new Point(3, 0);
		Point b = new Point(0, 0);
		Point c = new Point(0, 4);

		assertEquals(12, countPerimeter(List.of(a, b, c)));
	}

	@Test
	void testCountTriangleArea() {
		Point a = new Point(3, 0);
		Point b = new Point(0, 0);
		Point c = new Point(0, 4);

		assertEquals(6, countArea(List.of(a, b, c)));
	}

	@AfterAll
	public static void generateReport() {
		PipelineTestResult.generateReport(new File("./latest_report.json"));
	}
}
