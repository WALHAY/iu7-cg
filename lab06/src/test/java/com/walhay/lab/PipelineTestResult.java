package com.walhay.lab;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONObject;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class PipelineTestResult implements TestWatcher {
	private static final int COVERAGE = 5;
	private static int TEST_PASSED = 0;
	private static int TEST_FAILED = 0;

	@Override
	public void testSuccessful(ExtensionContext context) {
		TEST_PASSED++;
	}

	@Override
	public void testFailed(ExtensionContext context, Throwable cause) {
		TEST_FAILED++;
	}

	public static void generateReport(File file) {
		String timeStamp = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssXXX", Locale.US).format(new Date());
		JSONObject json = new JSONObject()
				.put("timestamp", timeStamp)
				.put("coverage", COVERAGE)
				.put("passed", TEST_PASSED)
				.put("failed", TEST_FAILED);

		try (PrintWriter writer = new PrintWriter(file)) {
			writer.write(json.toString(4));
		} catch (IOException e) {
			System.out.println("Unable to write report data!");
		}
	}
}
