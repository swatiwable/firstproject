package com.bnpp.reportsfreemaker;

import java.io.File;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import com.github.mkolisnyk.cucumber.reporting.CucumberResultsOverview;

/**
 * Overview report which will contain the following details
 * <ul>
 * <li>Two pie charts representing the overall status</li>
 * <li>Overall summart table</li>
 * <li>Features Status</li>
 * <li>Scenario Status</li>
 * </ul>
 */
public class OverviewReport implements Report {
	private final static Logger LOGGER = Logger.getLogger(OverviewReport.class);
	private final String targetDirectoryPath;
	private final String outputName;
	private final String templatePath;
	private final String sourceName;

	/**
	 * Constructs the Result overview report with specified parameters.
	 *
	 * @param targetDirectoryPath Target directory path where the report file will
	 *                            be generated. This field is mandatory
	 * @param outputName          File name of the report to be generated. This
	 *                            field is mandatory
	 * @param templatePath        custom template file to be used for report
	 *                            generation. This field is not mandatory
	 * @param sourceName          Input file that should be used to generate the
	 *                            report. This field is mandatory
	 */

	public OverviewReport(String targetDirectoryPath, String outputName, String templatePath, String sourceName) {

		Validate.notEmpty(targetDirectoryPath, "Target directory path should not be null. Please provide a valid path");
		Validate.notEmpty(outputName, "Output name of the report is empty. Please provide a valid name");
		Validate.notEmpty(sourceName, "Source name for the report is empty. Please provide a valid path");

		this.targetDirectoryPath = targetDirectoryPath;
		this.outputName = outputName;
		this.templatePath = templatePath;
		this.sourceName = sourceName;
	}

	@Override
	public String generateReport() throws Exception {
		LOGGER.info("Generating OverviewReport started" + toString());

		CucumberResultsOverview results = new CucumberResultsOverview();
		results.setOutputDirectory(this.targetDirectoryPath);
		results.setOutputName(this.outputName);
		results.setSourceFile(this.sourceName);

		if (this.templatePath != null) {
			results.setTemplatesLocation(this.templatePath);
		}

		results.execute();
		File outputHtmlFile = results.getOutputHtmlFile();
		LOGGER.info("Generating DetailedReport completed " + outputHtmlFile.getCanonicalPath());

		return outputHtmlFile.getPath();
	}

	@Override
	public String toString() {
		return "OverviewReport{" + "targetDirectoryPath='" + targetDirectoryPath + '\'' + ", outputName='" + outputName
				+ '\'' + ", templatePath='" + templatePath + '\'' + ", sourceName='" + sourceName + '\'' + '}';
	}
}
