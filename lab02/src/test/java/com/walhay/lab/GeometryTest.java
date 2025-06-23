package com.walhay.lab;

import static com.walhay.lab.utils.VectorHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.walhay.lab.primitives.Point;
import java.io.File;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(PipelineTestResult.class)
public class GeometryTest {
	@Test
	void testScaleVector() {
		Point a = new Point(1, 1);
		Point b = new Point(10, 10);
		assertEquals(b, scaleVector(a, 10, 10));
	}

	@Test
	void testTransposeVector() {
		Point a = new Point(0, 0);
		Point b = new Point(5, 6);
		Point c = new Point(4, 3);

		assertEquals(b, addVector(a, b));

		assertEquals(c, removeVector(b, new Point(1, 3)));
	}

	@Test
	void testRotateVector() {
		Point a = new Point(10, 10);
		double angle = 90.0f;

		assertEquals(new Point(-10, 10), rotateVector(a, angle));
	}

	@Test
	void testCrossProduct() {
		Point first = new Point(4, 0);
		Point second = new Point(0, 3);

		assertEquals(12, crossProductVectors(first, second));
	}

	@Test
	void testGetVectorLength() {
		Point a = new Point(1, 2);
		Point b = new Point(4, 6);

		assertEquals(5.0f, getVectorLength(removeVector(b, a)));
	}

	@AfterAll
	public static void generateReport() {
		PipelineTestResult.generateReport(new File("./latest_report.json"));
	}
}
