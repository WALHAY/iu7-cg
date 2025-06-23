package com.walhay.lab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class PipelineTestResult implements TestWatcher {
	public final static int COVERAGE = 1;
	public static int TEST_PASSED = 0;
	public static int TEST_FAILED = 0;

	@Override
	public void testSuccessful(ExtensionContext context) {
		TEST_PASSED++;
	}

	@Override
	public void testFailed(ExtensionContext context, Throwable cause) {
		TEST_FAILED++;
	}

	public static void generateReport(File file) {
		String timeStamp = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssXXX").format(new Date());
		JSONObject json = new JSONObject()
							  .put("timestamp", timeStamp)
							  .put("coverage", COVERAGE)
							  .put("passed", TEST_PASSED)
							  .put("failed", TEST_FAILED);

		try (PrintWriter writer = new PrintWriter(file)) {
			writer.write(json.toString(2));
			writer.append('\n');
		} catch (FileNotFoundException e) {
		}
	}
}
