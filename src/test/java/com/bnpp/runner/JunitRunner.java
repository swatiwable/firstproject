package com.bnpp.runner;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.bnpp.reportsfreemaker.ReportBuilder;
import com.bnpp.utilities.Configurations;
import com.bnpp.xray.Log;
import com.dab.config.PropertiesHandler;
import com.dab.xray.TestExecution;
import com.dab.xray.Xray;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)

@CucumberOptions(monochrome = true, features = "D:\\Genz\\rta\\rta\\src\\test\\java\\com\\bnpp\\features\\flipkart.feature\\", dryRun = false, glue = {
		"com/bnpp/steps/" }, plugin = { "json:target/cucumber.json" })
public class JunitRunner {

	public final static String PATH_TO_CUCUMBER_REPORT = "target/cucumber.json";
	public final static String PATH_REPORT_TEAMPLATE = "custom_templates/templates.json";
	public static String currentXrayIssueKey = "";
	public static boolean featureTestPassed = true;
	public static String ExecutionID = "";
	public static String testStart = "";
	public static String testPlanId = "";
	public static String folderNameReport = "";

	@BeforeClass
	public static void setupBeforeClass() {

		com.dab.config.PropertiesHandler.setConfigPath(Configurations.XrayConfigPath);
		setLogger();
		ExecutionID = TestExecution.getExecKey();

		testPlanId = com.dab.config.PropertiesHandler.getXrayTestPlanKey();

		TestExecution.addTestPlanToTestExecution(testPlanId, ExecutionID);

		ZonedDateTime startDateTime = ZonedDateTime.now();
		testStart = startDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		Log.info("Test Start Time: " + testStart);

	}

	@AfterClass
	public static void teardown() throws Exception {

		ReportBuilder.generateReport();

		// *** activating and deacivating in config.properties
		Xray.attachFileToJiraIssue(Configurations.reportPath + folderNameReport, ExecutionID);

	}

	public static void setLogger() {

		System.setProperty("log-directory", PropertiesHandler.getLogsFolder());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
		System.setProperty("currenttime", dateFormat.format(new Date()));
		Log.info("Log configuration done. Log Dir:" + PropertiesHandler.getLogsFolder());

	}
}
