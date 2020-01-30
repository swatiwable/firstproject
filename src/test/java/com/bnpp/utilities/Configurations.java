package com.bnpp.utilities;

public class Configurations {

	// DriverPaths and Run on browser parameter
	public static final String ExecutionEnvnmt = "flipkart"; // Valid values: intacc1, intacc2, load, gmail,flipkart
	public static final String BrowserName = "Chrome";

	public final static String chromeDriverPath = "./src/test/Resources/Drivers/chromedriver.exe";
	public final static String chromeDriverPath77 = "./src/test/Resources/Drivers/chromedriver77.exe";
	public final static String chromeDriverPath78 = "./src/test/Resources/Drivers/chromedriver78.exe";
	public final static String ieDriverPath = "./src/test/Resources/Drivers/IEDriverServer.exe";

	// Application URL
	public final static String AppurlLoad = "https://www.flipkart.com";
	public final static String AppurlEnv2 = "https://int-acc-ewev-2.consorsbank.de/home";
	public final static String AppurlEnv1 = "https://int-acc-ewev.consorsbank.de/home";
	public final static String Gmaildemo = "https://www.gmail.com";
	public final static String Flipkartdemo = "https://www.flipkart.com";
	// Test Data source path
	public final static String testDataResourcePath = "../src/test/java/com/bnpp/TestData/";

	// Browser Stack configuration
	public static final String RunOnBrowserStack = "N";
	public static final String USERNAME = "chetana19";

	public static final String AUTOMATE_KEY = "5tW8jrFVdPxbpgUSvssc";

	public static final String URL_BS = "https://" + USERNAME + ":" + AUTOMATE_KEY
			+ "@hub-cloud.browserstack.com/wd/hub";
	public static boolean cloud = false;

	// Output Reports path
	public static final String reportPath = "./Reports/";

	// download file path
	public static String downloadPath = System.getProperty("user.dir") + "\\Downloads\\";

	// Take screenshots on run parameter settings.
	public static final String takeScreenshots = "Y";

	public final static String XrayConfigPath = "./src/test/java/com/bnpp/xray/xray_config.properties";

}
