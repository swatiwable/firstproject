package com.bnpp.reportsfreemaker;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.bnpp.runner.JunitRunner;

/**
 * Entry class for the report generator
 */
public class ReportBuilder {
	/**
	 * Generates the html report for the passed cucumber output file
	 *
	 * @param args This method requires the cucumber report file path as the first
	 *             argument
	 * @throws Exception
	 */
	public static void build(String[] args) throws Exception {
		if (args != null && args.length > 0) {
			String path = args[0];
			String reportSuffix = "";
			if (args.length > 1) {
				reportSuffix = args[1];
			}

			System.out.println("Argument received: [" + path + "and" + reportSuffix + "]");
			new ReportGenerator().generateReport(path, reportSuffix);

		} else {
			throw new IllegalArgumentException("File path not passed");
		}
	}

	public static void generateReport() throws Exception {

		TimeUnit.SECONDS.sleep(5);

		Date currentDate = new Date();
		JunitRunner.folderNameReport = "BNP_" + currentDate.toString().replace(":", "_").replace(" ", "_");

		ReportGenerator rg = new ReportGenerator();

		rg.generateReport(JunitRunner.PATH_TO_CUCUMBER_REPORT, JunitRunner.folderNameReport);

	}
}
