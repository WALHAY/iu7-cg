package com.walhay.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.walhay.lab.utils.ColorUtils;
import java.awt.Color;
import java.io.File;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(PipelineTestResult.class)
public class ColorUtilsTest {

	@Test
	void testColorIntensity() {
		Color color = new Color(1, 2, 3);
		Color colorWithIntensity = ColorUtils.colorWithIntensity(color, 0.2);
		assertEquals(colorWithIntensity, new Color(1, 2, 3, 51));
	}

	@Test
	void testColorToFX() {
		Color color = new Color(32, 69, 228, 255);
		javafx.scene.paint.Color fxColor = ColorUtils.toFXColor(color);
		assertEquals(fxColor, javafx.scene.paint.Color.rgb(32, 69, 228, 1.0f));
	}

	@Test
	void testColorToAwt() {
		javafx.scene.paint.Color color = javafx.scene.paint.Color.rgb(128, 72, 54, 0.8f);
		Color awtColor = ColorUtils.toAwtColor(color);
		assertEquals(awtColor, new Color(128, 72, 54, 204));
	}

	@Test
	void testColorConversion() {
		Color color = new Color(23, 45, 67, 125);
		Color convColor = ColorUtils.toAwtColor(ColorUtils.toFXColor(color));
		assertEquals(color, convColor);
	}

	@AfterAll
	public static void generateReport() {
		PipelineTestResult.generateReport(new File("./latest_report.json"));
	}
}
