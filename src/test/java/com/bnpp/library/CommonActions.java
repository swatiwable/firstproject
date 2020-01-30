package com.bnpp.library;

import java.io.File;
//import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.ClientProtocolException;
import org.assertj.core.api.SoftAssertions;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xml.sax.SAXException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
//import com.bnpp.mTANResources.MobileTan;
import com.bnpp.reports.ExtentManager;
import com.bnpp.utilities.Configurations;

import cucumber.api.Scenario;
import cucumber.api.java.Before;

public class CommonActions {
	WebDriver driver;
	Exception e;
	public ExtentReports report;
	public ExtentTest scenario;
	Properties properties;
	public FileInputStream fis;
	public static String featurename;
	public static String scenarioname;
	public SoftAssertions softAssertions;
	public String downloadDir;

	public Scenario sc;

	public CommonActions() {

		if (properties == null) {
			try {
				System.out.println("swa");
				properties = new Properties();
				fis = new FileInputStream(
						System.getProperty("user.dir") + "/src/test/resources/ObjectRepository/Object.properties");
				properties.load(fis);
				softAssertions = new SoftAssertions();
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail();
			}
		}

	}

	public WebDriver getDriver() {

		return driver;
	}

	public void screenCapture() {
		byte[] data = ((TakesScreenshot) (getDriver())).getScreenshotAs(OutputType.BYTES);
		String testName = sc.getName();
		sc.embed(data, "image/png");
		sc.write(testName);
	}

	public void setScenario(Scenario s) {
		sc = s;
	}

	/**
	 * Description: Open the desired browser reading from properties file
	 * 
	 * @throws MalformedURLException
	 */
	public void launchBrowser() throws MalformedURLException {
		try {
			if ((Configurations.RunOnBrowserStack).equals("n")) {
				System.out.println("1");
				setUp();
			} else {
				if ((Configurations.BrowserName).equals("Chrome")) {
					System.out.println("2");

					System.setProperty("webdriver.chrome.driver", Configurations.chromeDriverPath78);
					System.out.println("3");

					driver = new ChromeDriver(loadChromeOptions());
					System.out.println("4");
					//logInfoStatus("Info | Browser : " + (Configurations.BrowserName));
					System.out.println("5");
				} else if ((Configurations.BrowserName).equals("IE")) {
					System.setProperty("webdriver.ie.driver", properties.getProperty("ieDriverPath"));
					driver = new InternetExplorerDriver();
				}
			}
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			if (Configurations.ExecutionEnvnmt.equalsIgnoreCase("intacc1")) {
				driver.get(Configurations.AppurlEnv1);
				logInfoStatus("Info | Environment Name: " + Configurations.AppurlEnv1);
			}

			if (Configurations.ExecutionEnvnmt.equalsIgnoreCase("intacc2")) {
				driver.get(Configurations.AppurlEnv2);
				logInfoStatus("Info | Environment Name: " + Configurations.AppurlEnv2);
			}
			if (Configurations.ExecutionEnvnmt.equalsIgnoreCase("load")) {
				driver.get(Configurations.AppurlLoad);
				logInfoStatus("Info | Environment Name: " + Configurations.AppurlLoad);
			}
			if (Configurations.ExecutionEnvnmt.equalsIgnoreCase("gmail")) {
				driver.get(Configurations.Gmaildemo);
				//logInfoStatus("Info | Environment Name: " + Configurations.Gmaildemo);
			}
			if (Configurations.ExecutionEnvnmt.equalsIgnoreCase("flipkart")) {
				driver.get(Configurations.Flipkartdemo);
				//logInfoStatus("Info | Environment Name: " + Configurations.Gmaildemo);
		} }catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Invalid browser name or configuration");
			logAssert_Fail("Launching browser failed");
		}
	}

	public ChromeOptions loadChromeOptions() {
		ChromeOptions ops = null;
		try {
			//System.out.println("11");
			System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "null");
			//System.out.println("12");
			System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
			//System.out.println("13");
			ops = new ChromeOptions();
			//System.out.println("14");
			ops.addArguments("--disable-notifications");
			ops.addArguments("disable-infobars");
			ops.addArguments("--start-maximized");
			ops.setExperimentalOption("useAutomationExtension", false);
			ops.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			//System.out.println("15");

			// Setting new download directory path
			Map<String, Object> prefs = new HashMap<String, Object>();
			// prefs.put("download.default_directory",
			// System.getProperty("user.dir") + "\\Downloads");
			prefs.put("plugins.plugins_disabled", new String[] { "Chrome PDF Viewer" });
			prefs.put("plugins.always_open_pdf_externally", true);
			prefs.put("profile.default_content_settings.popups", 0);

			// below condition is for creating download folder only in case of
			// scenario with PDF download feature

		/*	if (getScenarioName().equals("Ueberweisungslimit_MaxLimit_Error")
				
					|| getScenarioName().equals("Einzelkonto_DepotCFD_NeuesKonto")
					|| getScenarioName().equals("SparplanMinderjaehrigenkonto_2GV_Anlegen")
					|| getScenarioName().equals("SparplanGemeinschaftskonto_Anlegen")
					|| getScenarioName().equals("SparplanEinzelkonto_Anlegen")
					|| getScenarioName().equals("TagesgeldGemeinschaftskonto_Anlegen")
					|| getScenarioName().equals("TagesgeldMinderjaehrigenkonto1GV_Anlegen")
					|| getScenarioName().equals("TagesgeldMinderjaehrigenkonto2GV_Anlegen")
					|| getScenarioName().equals("DepotEinzelkonto_Anlegen")
					|| getScenarioName().equals("DepotGemeinschaftskonto_Anlegen")
					|| getScenarioName().equals("DepotMinderjaehrigenkonto_Anlegen")
					|| getScenarioName().equals("GVDepotBestehendesKind_Anlegen")
					|| getScenarioName().equals("GVTagesgeldBestehendesKind_Anlegen")
					|| getScenarioName().equals("GVDepotWeiteresKind_Anlegen")
					|| getScenarioName().equals("GVTagesgeldWeiteresKind_Anlegen")) {
				System.out.println("16");
				Date d = new Date();
				String folderName = d.toString().replace(":", "_");
				// folderName = d.toString().replace(" ", "_");
				new File(Configurations.downloadPath).mkdirs();
				downloadDir = Configurations.downloadPath + folderName;
				System.out.println(downloadDir);
				// directory of the report folder
				new File(downloadDir).mkdirs();
				prefs.put("download.default_directory", downloadDir);

			}
			ops.setExperimentalOption("prefs", prefs);*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return ops;
	}

	/**
	 * Description set text to the field
	 */
	public void setText(String objectKey, String datakey)
			throws IllegalArgumentException, InterruptedException, IOException, ParseException {
		try {
			getElement(objectKey).clear();
			getElement(objectKey).sendKeys(datakey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;

		}
	}

	/**
	 * @param objectKey
	 *            Description Common verify file is Present
	 * @throws InterruptedException
	 */
	public void VerifyifFilePresent() throws InterruptedException {
		try {
			Thread.sleep(20000);
			File files = new File(downloadDir);
			System.out.println("download path" + downloadDir);
			int Count = files.list().length;
			System.out.println("No. Of Files: " + Count);
			if (Count == 1) {
				// logPassStatus("PDF is downloaded successfully at
				// "+Configurations.downloadPath);
				logPassStatus("Pass | PDF is downloaded successfully at - " + downloadDir);

			} else {
				logFailStatus(scenarioname + " : "
						+ "Error | PDF download failed. Probably pdf not downloaded in desired folder");

			}
			downloadDir = "";
			// put download file path in reports
			// scenario.debug(Configurations.downloadPath);
		} catch (NullPointerException e) {
			logFailStatus(scenarioname + " : " + "Error | Probably pdf not downloaded in desired folder");
			System.out.println(scenarioname + " : " + "Error | Probably pdf not downloaded in desired folder");
			downloadDir = "";
			throw e;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}

	/**
	 * @param objectKey
	 * @param data
	 * @throws Exception
	 *             Description Common action select from combo box by value text
	 */
	public void selectFromDropDownByValue(String objectKey, String datakey) throws Exception {
		try {
			Select s = new Select(getElement(objectKey));
			String myData = getValueFromJson(datakey);
			if (datakey.equals("Account_Type")) {
				myData = getValueFromJson("UserID_Kontonummer");
			}
			try {
				s.selectByValue(myData);
			} catch (Exception e) {
				logAssert_Fail("Select by value failed " + objectKey);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}

	}

	/**
	 * Description Refresh the page
	 */
	public void navigateBack() {
		driver.navigate().back();
	}

	/**
	 * 
	 */

	public void waitForVisibilityofElement(String ObjectKey) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.visibilityOf(getElement(ObjectKey)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logAssert_Fail("Element not visible within given time limit: " + ObjectKey);
		}
	}

	public void waitForInvisibilityofElement(String ObjectKey) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.invisibilityOf(getElement(ObjectKey)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logAssert_Fail("Element still visible within given time limit: " + ObjectKey);
			throw e;
		}
	}

	/**
	 * @param objectKey
	 * @return WebElement Description: Central function to extract objects
	 */
	public WebElement getElement(String objectKey) throws IllegalArgumentException {
		WebElement e = null;
		WebDriverWait wait = new WebDriverWait(driver, 40);
		//e = driver.findElement(By.xpath(properties.getProperty(objectKey)));
		try {
			//System.out.println("swat");
			e = driver.findElement(By.xpath(properties.getProperty(objectKey)));// present
			 if (!objectKey.equals("Edit_Aktie") && (!scenarioname.equals("KaufOrder_Loeschen_Aktie"))
					&& (!scenarioname.equals("KaufOrder_Loeschen_Fond"))) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", e);
				Thread.sleep(1000);
			}
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
			System.out.println("\r\n" + "Locator key missing in object repository file: " + objectKey);
			logAssert_Fail("\r\n" + "Locator key missing in object repository file: " + objectKey);
		} catch (NoSuchElementException ex) {
			ex.printStackTrace();
			System.out.println("Element not present on the page: " + objectKey);
			logAssert_Fail("\r\n" + "Element not present on the page: " + objectKey);
		} catch (ElementNotInteractableException ex) {
			ex.printStackTrace();
			logAssert_Fail("\r\n" + "Element not visible on the page: " + objectKey);
		} catch (InvalidSelectorException ex) {
			ex.printStackTrace();
			logAssert_Fail("Invalid xpath selector");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Object identification failed: " + objectKey);
			logAssert_Fail("\r\n" + "Object identification failed: " + objectKey);
		}
		return e;
	}

	/**
	 * @param objectKey
	 * @return true if element is present false if not found. Description: Check if
	 *         element is present and used as a checkpoint. true - present
	 */
	public boolean isElementPresent(String objectKey) {
		List<WebElement> e = null;
		//System.out.println("sw");
		e = driver.findElements(By.xpath(properties.getProperty(objectKey)));
		if (e.size() == 0) {
			System.out.println("element not present: " + objectKey);
			return false;
		} else {
			System.out.println("Elements present:Count " + objectKey + ":" + e.size());
			return true;
		}
	}

	/**
	 * 
	 * @param objectKey
	 *            Description: Common action click
	 * @throws InterruptedException
	 */
	public void click(String objectKey) throws InterruptedException {
		try {
			Thread.sleep(2000);
			getElement(objectKey).click();
			logInfoStatus("Info | Clicked on : " + objectKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}

	/**
	 * @param objectKey
	 * @param strValue
	 *            Description: Common action type
	 * @throws IllegalArgumentException
	 * @throws InterruptedException
	 * @throws ParseException
	 * @throws IOException
	 */
	public void enterText(String objectKey, String textToEnter)
			throws IllegalArgumentException, InterruptedException, IOException, ParseException {
		try {
			getElement(objectKey).clear();
			getElement(objectKey).sendKeys(textToEnter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}

	}

	public void enterTextToLogin(String objectKey, String dataKey)
			throws IllegalArgumentException, InterruptedException, IOException, ParseException {
		try {
			getElement(objectKey).clear();
			getElement(objectKey).sendKeys(getKeyFromJson(dataKey));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}

	}

	/**
	 * 
	 * @param objectKey
	 * @param strValue
	 *            Description Type Tan no
	 * @throws InterruptedException
	 */
	public void enterTan(String objectKey, String strValue) throws InterruptedException {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(2000);
			getElement(objectKey).sendKeys(strValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}

	}

	/**
	 * @param objectKey
	 *            Description Common action clear
	 */
	public void clearfield(String objectKey) {
		try {
			getElement(objectKey).clear();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}

	/**
	 * @param objectKey
	 * @return string Description Common action gettext
	 */
	public String getText(String objectKey) {
		try {
			String str = "";
			return str = getElement(objectKey).getText();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}

	public String getAttribute(String objectKey, String attributeName) {
		try {
			String str = "";
			return str = getElement(objectKey).getAttribute(attributeName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}

	/**
	 * @param objectKey
	 * @param data
	 * @throws Exception
	 *             Description Common action select from combo box by visible text
	 */
	public void selectFromDropDown(String objectKey, String datakey) throws Exception {
		Select s = new Select(getElement(objectKey));
		String myData = getValueFromJson(datakey);
		try {
			s.selectByVisibleText(myData);
		} catch (Exception e) {
			logAssert_Fail("Select by visble text failed on: " + objectKey);
			throw e;
		}

	}

	public void selectAccountType(String dataKey, String locatorKey) throws Exception, IOException, Exception {
		Select s = new Select(getElement(locatorKey));
		String myData = getValueFromJson(dataKey);
		myData = myData + " " + "| " + getKeyFromJson("UserID_Kontonummer");
		// System.out.println(myData);
		try {
			s.selectByVisibleText(myData);
		} catch (Exception e) {
			logAssert_Fail("Select by visble text failed on: " + locatorKey);
			throw e;
		}

	}

	/**
	 * Description Press escape key
	 */
	public void pressTab() {
		try {
			Actions act = new Actions(driver);
			act.sendKeys(Keys.TAB).build().perform();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}

	/**
	 * Description Press enter key
	 */
	public void pressEnter() {
		try {
			Actions act = new Actions(driver);
			act.sendKeys(Keys.ENTER).build().perform();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}

	/**
	 * 
	 * @param objectKey
	 * @param data
	 * @throws Exception
	 *             Description Common action select from combo box by Index
	 */
	public void selectDropDownByIndex(String objectKey, String data) throws Exception {
		try {
			Select s = new Select(getElement(objectKey));
			if (data.equals("Intervall")) {
				Thread.sleep(3000);
				s.selectByIndex(2);
				System.out.println("dropdown condition success-MONATLICH selected");
			} else if (data.equals("Immer_am")) {
				System.out.println("dropdown condition success");
				Thread.sleep(3000);
				s.selectByIndex(9);
				System.out.println("dropdown condition success-10. des montas selected");
			} else {

				Thread.sleep(5000);
				s.selectByIndex(Integer.parseInt(data));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}

	}

	/**
	 * Description Common action move to a child window or new window
	 */

	public void movetoChildWindow() {
		Set<String> handles = driver.getWindowHandles();
		String handle = driver.getWindowHandle();
		for (String a : handles) {
			if (!a.equals(handle)) {
				driver.switchTo().window(a);
			}
		}
	}

	/**
	 * 
	 * @param objectKey
	 *            Description Common action mouse over the element
	 */
	public void mouseover(String objectKey) {
		try {
			Actions act = new Actions(driver);
			act.moveToElement(getElement(objectKey)).build().perform();
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
	}

	/**
	 * Description Common action to move scroll down
	 */

	public void moveScrollDown() {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,250)");

	}

	public void moveScrollUp() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,-500)");
	}

	/**
	 * Description Reporting function to message the information step
	 */
	public void logInfoStatus(String msg) {
		scenario.log(Status.INFO, msg);
	}

	/**
	 * 
	 * @param msg
	 *            Description Reporting function to pass the step
	 */
	public void logPassStatus(String msg) {
		scenario.log(Status.PASS, msg);

		softAssertions.assertThat(true);

		// assertEquals(true, true);
	}

	/**
	 * @param errMsg
	 *            Description Common function to fail the report and stop execution
	 */
	public void logAssert_Fail(String errMsg) {
		// fail in extent reports

		scenario.log(Status.FAIL, errMsg);
		if ((Configurations.takeScreenshots).equalsIgnoreCase("Y")) {
			takeSceenShot();
		}
		// take screenshot and put in repots
		// fail in cucumber as well
		Assert.fail();
		// try {
		// throw new NoSuchFieldException();
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		//
	}

	/**
	 * 
	 * @param msg
	 *            Description Reporting function to fail the step and continue
	 *            execution
	 */
	public void logFailStatus(String msg) {
		scenario.log(Status.FAIL, msg);
		softAssertions.assertThat(false);
		takeSceenShot();
		try {
			throw new NoSuchFieldError();
		} catch (Exception e) {

		}
	}

	/**
	 * Description Common function to take the screenshots of failure steps
	 */

	public void takeSceenShot1() {
		if ((Configurations.takeScreenshots).equals("Y")) {
			Date d = new Date();
			try {

				String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
				// take screenshot
				File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

				// get the dynamic folder name
				FileUtils.copyFile(srcFile, new File(ExtentManager.screenshotFolderPath + screenshotFile));
				String PathofScreenShot = System.getProperty("user.dir") + "/" + ExtentManager.screenshotFolderPath
						+ screenshotFile;
				// put screenshot file in reports
				scenario.info("Screenshot", MediaEntityBuilder.createScreenCaptureFromPath(PathofScreenShot).build());
			} catch (IOException e) {
				e.printStackTrace();
				Assert.fail();
			}
		}

	}

	// public void takeSceenShot_NewReport() {
	public void takeSceenShot() {
		if ((Configurations.takeScreenshots).equals("Y")) {
			Date d = new Date();
			try {
				/**
				 * Screen capture for new report
				 */
				// screenCapture();
				byte[] data = ((TakesScreenshot) (getDriver())).getScreenshotAs(OutputType.BYTES);
				String testName = sc.getName();
				sc.embed(data, "image/png");
				sc.write(testName);

				String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
				// take screenshot
				File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

				// get the dynamic folder name
				FileUtils.copyFile(srcFile, new File(ExtentManager.screenshotFolderPath + screenshotFile));
				String PathofScreenShot = System.getProperty("user.dir") + "\\" + ExtentManager.screenshotFolderPath
						+ screenshotFile;
				// put screenshot file in reports
				scenario.info("Screenshot", MediaEntityBuilder.createScreenCaptureFromPath(PathofScreenShot).build());
			} catch (IOException e) {
				e.printStackTrace();
				Assert.fail();
			}
		}

	}

	/**
	 * Description Common function for quitting the browser and reports.
	 */
	public void quit() {
		takeSceenShot();
		if (report != null)
			report.flush();
		if (driver != null)
			driver.quit();
		softAssertions.assertAll();
		if ((softAssertions.errorsCollected().size()) != 0)
			logAssert_Fail(scenarioname + " failed");
	}

	/**
	 * 
	 * @param scenarioName
	 *            Description Common function to initialize the reports
	 */
	public void initReports(String scenarioName) {
		try {
			report = ExtentManager.getInstance(Configurations.reportPath);
			scenario = report.createTest(scenarioName);
			scenario.log(Status.INFO, "Starting " + scenarioName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}

	public String getValueFromJson(String dataKeyInJson) throws FileNotFoundException, IOException, ParseException {
		System.out.println("dataKeyInJson >>>>" + dataKeyInJson);
		System.out.println("B");
		String datakey = null;
		try {
			datakey = getKeyFromJson(dataKeyInJson);
			datakey = checkGermanCharacters(datakey);

		} catch (FileNotFoundException e) {
			logAssert_Fail(featurename + " .json file not found");
		} catch (NullPointerException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			logAssert_Fail(dataKeyInJson + " : Please make sure ojectKey present in json file");
		}

		return datakey;
	}

	public String getKeyFromJson(String dataKey) throws FileNotFoundException, IOException, ParseException {

		try {
			String data = null;
			JSONParser parser = new JSONParser();
			if (Configurations.ExecutionEnvnmt.equalsIgnoreCase("intacc1")) {
				JSONObject getFeatureName = (JSONObject) parser
						.parse(new FileReader("./src/test/java/com/bnpp/testdata/intacc1/" + featurename + ".json"));
				JSONObject featureName = (JSONObject) getFeatureName.get(featurename);
				Map<String, String> getScenarioName = (Map<String, String>) featureName.get(scenarioname);
				Iterator it = getScenarioName.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					if (pair.getKey().toString().equals(dataKey)) {
						data = pair.getValue().toString();
						break;
					}
					// System.out.println(pair.getKey() + ":" +

				}

			} else if (Configurations.ExecutionEnvnmt.equalsIgnoreCase("intacc2")) {
				JSONObject getFeatureName = (JSONObject) parser
						.parse(new FileReader("./src/test/java/com/bnpp/testdata/intacc2/" + featurename + ".json"));
				JSONObject featureName = (JSONObject) getFeatureName.get(featurename);
				Map<String, String> getScenarioName = (Map<String, String>) featureName.get(scenarioname);
				Iterator it = getScenarioName.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					if (pair.getKey().toString().equals(dataKey)) {
						data = pair.getValue().toString();
						break;
					}
					// System.out.println(pair.getKey() + ":" +

				}
				// pair.getValue().toString());
			} else if (Configurations.ExecutionEnvnmt.equalsIgnoreCase("load")) {

				JSONObject getFeatureName = (JSONObject) parser
						.parse(new FileReader("./src/test/java/com/bnpp/testdata/load/" + featurename + ".json"));
				JSONObject featureName = (JSONObject) getFeatureName.get(featurename);
				Map<String, String> getScenarioName = (Map<String, String>) featureName.get(scenarioname);
				Iterator it = getScenarioName.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					if (pair.getKey().toString().equals(dataKey)) {
						data = pair.getValue().toString();
						break;
					}
					// System.out.println(pair.getKey() + ":" +
				}
			}
			
			else if (Configurations.ExecutionEnvnmt.equalsIgnoreCase("gmail")) {

				JSONObject getFeatureName = (JSONObject) parser
						.parse(new FileReader("./src/test/java/com/bnpp/testdata/gmail/" + featurename + ".json"));
				JSONObject featureName = (JSONObject) getFeatureName.get(featurename);
				Map<String, String> getScenarioName = (Map<String, String>) featureName.get(scenarioname);
				Iterator it = getScenarioName.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					if (pair.getKey().toString().equals(dataKey)) {
						data = pair.getValue().toString();
						break;
					}
					// System.out.println(pair.getKey() + ":" +
				}
			}
			else if (Configurations.ExecutionEnvnmt.equalsIgnoreCase("flipkart")) {
				System.out.println("a");

				JSONObject getFeatureName = (JSONObject) parser
						.parse(new FileReader("./src/test/java/com/bnpp/testdata/flipkart/" + featurename + ".json"));
				JSONObject featureName = (JSONObject) getFeatureName.get(featurename);
				System.out.println(featureName);
				Map<String, String> getScenarioName = (Map<String, String>) featureName.get(scenarioname);
				Iterator it = getScenarioName.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					if (pair.getKey().toString().equals(dataKey)) {
						data = pair.getValue().toString();
						break;
					}
					//System.out.println(pair.getKey() + ":" +
				}
				System.out.println("Data  "+ data);
			}
			return data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}

	}

	public String verifyErrorMessage(String messageKey) throws FileNotFoundException, IOException, ParseException {
		String data = null;
		try {
			JSONParser parser = new JSONParser();
			if (Configurations.ExecutionEnvnmt.equalsIgnoreCase("intacc1")) {

				JSONObject getFeatureName = (JSONObject) parser
						.parse(new FileReader("./src/test/java/com/bnpp/testdata/intacc1/" + featurename + ".json"));
				JSONObject featureName = (JSONObject) getFeatureName.get(featurename);
				JSONObject scenario = (JSONObject) featureName.get(scenarioname);
				Map<String, String> getmessagename = (Map<String, String>) scenario.get("ErrorMesssages");
				Iterator it = getmessagename.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					if (pair.getKey().toString().equals(messageKey)) {
						data = pair.getValue().toString();
						break;
					}
					// System.out.println(pair.getKey() + ":" +
					// pair.getValue().toString());
				}
			} else if (Configurations.ExecutionEnvnmt.equalsIgnoreCase("intacc2")) {

				JSONObject getFeatureName = (JSONObject) parser
						.parse(new FileReader("./src/test/java/com/bnpp/testdata/intacc2/" + featurename + ".json"));
				JSONObject featureName = (JSONObject) getFeatureName.get(featurename);
				JSONObject scenario = (JSONObject) featureName.get(scenarioname);
				Map<String, String> getmessagename = (Map<String, String>) scenario.get("ErrorMesssages");
				Iterator it = getmessagename.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					if (pair.getKey().toString().equals(messageKey)) {
						data = pair.getValue().toString();
						break;
					}
					// System.out.println(pair.getKey() + ":" +
					// pair.getValue().toString());
				}
			} else if (Configurations.ExecutionEnvnmt.equalsIgnoreCase("load")) {
				JSONObject getFeatureName = (JSONObject) parser
						.parse(new FileReader("./src/test/java/com/bnpp/testdata/load/" + featurename + ".json"));
				JSONObject featureName = (JSONObject) getFeatureName.get(featurename);
				JSONObject scenario = (JSONObject) featureName.get(scenarioname);
				Map<String, String> getmessagename = (Map<String, String>) scenario.get("ErrorMesssages");
				Iterator it = getmessagename.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					if (pair.getKey().toString().equals(messageKey)) {
						data = pair.getValue().toString();
						break;
					}
					// System.out.println(pair.getKey() + ":" +
					// pair.getValue().toString());
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logAssert_Fail("Json file not found");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logAssert_Fail("Unable to read message data from json");
		}
		return data;
	}

	public String checkGermanCharacters(String data) {
		try {
			if (data.equals("")) {
				// if (data.contains("ae"))
				// data = data.replace("ae", "ä");
				// if (data.contains("oe"))
				// data = data.replace("oe", "ö");
				// if (data.contains("ue"))
				// data = data.replace("ue", "ü");
				// if (data.contains("Ae"))
				// data = data.replace("Ae", "Ä");
				// if (data.contains("Oe"))
				// data = data.replace("Oe", "Ö");
				// if (data.contains("Ue"))
				// data = data.replace("Ue", "Ü");
			}
		} catch (Exception e) {

			throw e;

		}
		return data;

	}

	public void setfeaturefilenameandsceanrio(String id, String name) {
		featurename = id;
		String[] d = featurename.split("/features/");
		// System.out.println(d[0] + " " + d[1]);
		String[] d2 = d[1].split(".feature");
		// System.out.println(d2[0]);
		featurename = d2[0];
		scenarioname = name;
	}

	public String getScenarioName() {
		return scenarioname;
	}

	public String getFeatureName() {
		return featurename;
	}

	public void clearCheckBox(String objectKey) {
		WebElement e;

		try {
			e = driver.findElement(By.xpath(properties.getProperty(objectKey + "_checkbox")));// present
			if (e.isSelected()) {
				Thread.sleep(1000);
				driver.findElement(By.xpath(properties.getProperty(objectKey))).click();
			}
		} catch (Exception ex) {
			logAssert_Fail("Clear checkbox failed");
		}
	}

	public boolean compareTextWithJsonDataKeyValue(String ObjectKey, String jsonDataKey)
			throws FileNotFoundException, IOException, ParseException {
		if (getText(ObjectKey).equals(getValueFromJson(jsonDataKey)))
			return true;
		else
			return false;

	}

	public boolean compareAtrributeWithJson(String ObjectKey, String AttributeName, String jsonDataKey)
			throws FileNotFoundException, IOException, ParseException {
		if (getAttribute(ObjectKey, AttributeName).equals(getValueFromJson(jsonDataKey)))
			return true;
		else
			return false;
	}

	// Not in use currently for load environement 13.11.2019
	public void enterNewMobileTan(String tanKey, String token) throws InterruptedException, ClientProtocolException,
			IOException, ParserConfigurationException, SAXException, ParseException {
		Properties prop = new Properties();
		// FileInputStream fis = new
		// FileInputStream("C:\\workspace\\mobileTANTest\\src\\main\\java\\mTANResources\\data.properties");
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/test/java/com/bnpp/mTANResources/data.properties");
		prop.load(fis);
		// String customerId = prop.getProperty("userID");
		String customerId = getValueFromJson("UserID_Kontonummer");
		String customerPin = getValueFromJson("PIN_Password");
		String cafeUser = prop.getProperty("cafeUserID");
		String cafePin = prop.getProperty("cafePin");

		// Redirecting Mobile TAN
		//MobileTan mt = new MobileTan();
		//mt.mTanRedirection(customerId, customerPin, cafeUser, cafePin);

		// String MobileTAN_link_Login = "//a[@id='mobile-tan-request']";
		click("MobileTAN_link_Login");
		//String mTAN = mt.getMTan(customerId, customerPin, cafeUser, cafePin);
		// System.out.println("mTAN is -" + mTAN);

		Thread.sleep(3000);
		//enterTan(tanKey, mTAN);
		if (tanKey.equals("TAN_field_AngabenZurPerson")) {
			click("TAN_field_AngabenZurPerson_Button");
		}
		logInfoStatus("Info | Token used : " + token);

		// commonActions.enterTokenTan(TanKey, TANGenerator.requestTan());
	}

	/**
	 * Description Common function for checked or unchecked the radio button
	 * 
	 * @throws Exception
	 */
	public void clearRadioButton(String objectKey) throws Exception {
		WebElement e;

		try {
			e = driver.findElement(By.xpath(properties.getProperty(objectKey)));// present
			if (e.isSelected()) {
				Thread.sleep(1000);
				driver.findElement(By.xpath(properties.getProperty(objectKey))).click();
			}
		} catch (Exception ex) {
			throw ex;
		}
	}

	/*
	 * Description Count no of elements
	 */
	public int noofelement(String objectKey) {
		List<WebElement> e = driver.findElements(By.xpath(properties.getProperty(objectKey)));
		return e.size();

	}

	// Not in use currently for load environement 13.11.2019
	public void clickonMobiletanLinkandEnterTan(String mobiletanlink, String tanfield) throws ClientProtocolException,
			IOException, ParserConfigurationException, SAXException, InterruptedException, ParseException {
		Properties prop = new Properties();
		// FileInputStream fis = new
		// FileInputStream("C:\\workspace\\mobileTANTest\\src\\main\\java\\mTANResources\\data.properties");
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\test\\java\\com\\bnpp\\mTANResources\\data.properties");
		prop.load(fis);

		String customerId = getValueFromJson("UserID_Kontonummer");
		String customerPin = getValueFromJson("PIN_Password");
		String cafeUser = prop.getProperty("cafeUserID");
		String cafePin = prop.getProperty("cafePin");

		// Redirecting Mobile TAN
		//MobileTan mt = new MobileTan();
		//mt.mTanRedirection(customerId, customerPin, cafeUser, cafePin);

		// String MobileTAN_link_Login = "//a[@id='mobile-tan-request']";
		click(mobiletanlink);

		//String mTAN = mt.getMTan(customerId, customerPin, cafeUser, cafePin);
		//System.out.println("mTAN is -" + mTAN);
		//enterTan(tanfield, mTAN);
		pressTab();
		if (tanfield.equals("TAN_field_AngabenZurPerson")) {
			click("TAN_field_AngabenZurPerson_Button");
		}
		//logInfoStatus("Info | Token used : " + mTAN);
	}

	public void enterTexttoken(String tankey, String string) {
		// TODO Auto-generated method stub
		getElement(tankey).sendKeys(string);
	}

	public void enterLoadenvironmentTan(String tanField, String string) throws InterruptedException {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw e;
		}
		getElement(tanField).sendKeys(string);
	}

	public String enterFutureDateAddingDays(String noofDaysToAdd) throws java.text.ParseException, ParseException {
		String oldDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date()).toString();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(oldDate));
		// Number of Days to add
		c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(noofDaysToAdd));
		// Date after adding the days to the given date
		String newDate = sdf.format(c.getTime());
		// Displaying the new Date after addition of Days
		System.out.println(newDate);
		return newDate;
	}

	public void setUp() throws Exception {

		DesiredCapabilities capabilities = new DesiredCapabilities();

		System.getProperties().put("https.proxyHost", "proxyclient.corp.dir");
		System.getProperties().put("https.proxyPort", "8080");
		System.out.println(System.getProperty("browser"));
		if (System.getProperty("browser").equals("chrome")) {
			capabilities.setCapability("browser", System.getProperty("browser"));
			capabilities.setCapability("browser_version", "75.0");
			capabilities.setCapability("os", "Windows");
			capabilities.setCapability("os_version", "10");
		} else if (System.getProperty("browser").equals("firefox")) {
			capabilities.setCapability("browser", System.getProperty("browser"));
			capabilities.setCapability("browser_version", "69.0");
			capabilities.setCapability("os", "Windows");
			capabilities.setCapability("os_version", "10");
		} else if (System.getProperty("browser").equals("safari")) {
			capabilities.setCapability("browser", System.getProperty("browser"));
			capabilities.setCapability("browser_version", "7.1");
			capabilities.setCapability("os", "OS X");
			capabilities.setCapability("os_version", "Mavericks");
		} else if (System.getProperty("browser").equals("IE")) {
			capabilities.setCapability("browser", System.getProperty("browser"));
			capabilities.setCapability("browser_version", "11.0");
			capabilities.setCapability("os", "Windows");
			capabilities.setCapability("os_version", "10");
		}

		Thread.sleep(2000);
		String username = "chetana19";
		String accessKey = "5tW8jrFVdPxbpgUSvssc";

		// if(capabilities.getCapability("browserstack.local") != null &&
		// capabilities.getCapability("browserstack.local") == "true"){
		// l = new Local();
		// Map<String, String> options = new HashMap<String, String>();
		// options.put("key", accessKey);
		// l.start(options);
		// }
		capabilities.setCapability("browserstack.local", "true");
		capabilities.setCapability("name", getScenarioName());
		capabilities.setCapability("acceptSslCerts", "true");
		capabilities.setCapability("browserstack.debug", "true");

		driver = new RemoteWebDriver(new URL("https://" + username + ":" + accessKey + "@hub.browserstack.com/wd/hub"),
				capabilities);
	}

	public void pressEscape() {
		// TODO Auto-generated method stub
		try {
			Actions act = new Actions(driver);
			act.sendKeys(Keys.ESCAPE).build().perform();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}

	public void pressRightArrow() {
		// TODO Auto-generated method stub
		try {
			Actions act = new Actions(driver);
			act.sendKeys(Keys.ARROW_RIGHT).build().perform();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
}
