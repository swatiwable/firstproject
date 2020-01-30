package com.bnpp.steps;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;

import com.bnpp.library.CommonActions;
import com.bnpp.runner.JunitRunner;
import com.bnpp.utilities.Configurations;
import com.bnpp.xray.Log;
import com.bnpp.xray.XrayHelper;
import com.dab.xray.XRAY_CONFIG;
import com.dab.xray.Xray;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class Flipkartloginstep 
{
CommonActions commonActions;
String testFinish = "";
String XrayIssueKey = "";
	
	
	public Flipkartloginstep(CommonActions con) {
		this.commonActions = con;}
	
	@Before
	public void before(Scenario s) throws Exception {

		if ((Configurations.RunOnBrowserStack).equals("Y")) {
			commonActions.initReports(s.getName() + "_" + System.getProperty("browser"));
		} else {
			commonActions.initReports(s.getName() + "_" + "chrome");
		}
		commonActions.setfeaturefilenameandsceanrio(s.getId(), s.getName());
		commonActions.setScenario(s);

		checkNewTest(s);
	}

	/**
	 * Description Closing the resources after execution of each scenario
	 * 
	 * @throws IOException
	 */
	@After
	public void after(Scenario s) {

		commonActions.quit();

		saveTestResultsToXray(s);

	}

	private void saveTestResultsToXray(Scenario s) {

		ZonedDateTime finishDateTime = ZonedDateTime.now();
		testFinish = finishDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		Log.info("Test Finish Time: " + testFinish);

		if (s.isFailed()) {
			Log.error("Test Failed!");
			JunitRunner.featureTestPassed = false;
			Xray.writeResultsForSingleTest(JunitRunner.ExecutionID, XrayIssueKey, XRAY_CONFIG.TEST_STATUS_FAIL,
					JunitRunner.testStart, testFinish);
		} else {
			if (JunitRunner.featureTestPassed == true) {
				Log.info("Test Passed!");
				Xray.writeResultsForSingleTest(JunitRunner.ExecutionID, XrayIssueKey, XRAY_CONFIG.TEST_STATUS_PASS,
						JunitRunner.testStart, testFinish);
			}
		}

	}

	private void checkNewTest(Scenario s) {
		XrayIssueKey = XrayHelper.getTestIdFromFileName(s.getId());

		if (!JunitRunner.currentXrayIssueKey.contains(XrayIssueKey)) {
			System.out.println("This is a new Feature!");
			JunitRunner.currentXrayIssueKey = XrayIssueKey;
			JunitRunner.featureTestPassed = true;
		}

	}

	// ********Common step definitions ************//
	/*
	 * private String getTestIdFromFileName(String path) { String result = ""; File
	 * f = new File(path); //System.out.println("File Name1: " +
	 * f.getName().toString().toUpperCase().replace("_", "-").trim()); result =
	 * f.getName().toString().toUpperCase().replace("_",
	 * "-").trim().split(".FEATURE")[0]; System.out.println("File Name: " + result);
	 * // result = f.getName().toString().toUpperCase().replace("_", "-").trim();
	 * return result; }
	 * 
	 */
	

	@Given("^user navigate to flipcart$")
	public void user_navigate_to_flipcart() 
	{
	    // Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		
		try {
			System.out.println("given1");
			commonActions.launchBrowser();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Given("login as {string} with {string}")
	public void login_as_with(String mobile_no, String password) throws InterruptedException, FileNotFoundException, IOException, ParseException 
	{		System.out.println("given2");
		//System.out.println(commonActions.getKeyFromJson(mobile_no));
		try {
			
			String str = commonActions.getKeyFromJson(mobile_no);
			System.out.println(str);
			String str1 = commonActions.getKeyFromJson(password);
			System.out.println(str1);
			commonActions.enterText(mobile_no, commonActions.getKeyFromJson(mobile_no));
			commonActions.enterText(password, commonActions.getKeyFromJson(password));
		} catch (IllegalArgumentException | IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	
	}

	@Given("click on button {string}")
	public void click_on_button(String login) {
	    // Write code here that turns the phrase above into concrete actions
	    try {
			commonActions.click(login);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Then("user navigate to home page")
	public void user_navigate_to_home_page() {
	    // Write code here that turns the phrase above into concrete actions
	   // throw new cucumber.api.PendingException();

}

	
	
	@Then("user enter {string} in {string}")
	public void user_enter_in(String phone, String search_field) throws InterruptedException 
	{
	    
		String str2;
		try {
			Thread.sleep(3000);
			str2 = commonActions.getKeyFromJson(phone);
			Thread.sleep(3000);
			System.out.println(str2);
			commonActions.enterText(search_field, str2);
		      
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	   
	

	@Then("user click on {string}")
	public void user_click_on(String search_button)
	{
	    try {
	    	Thread.sleep(3000);
			commonActions.click(search_button);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Then("user click on checkbox {string}")
	public void user_click_on_checkbox(String Assured_checkbox) 
	{
	    // Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
		try {
			
			Thread.sleep(5000);
			//commonActions.moveScrollDown();
			//commonActions.moveScrollUp();
			commonActions.mouseover(Assured_checkbox);
			//commonActions.waitForVisibilityofElement(Assured_checkbox);
		   //commonActions.click(Assured_checkbox);
			//commonActions.moveScrollDown();
			
			//Thread.sleep(5000);
			commonActions.moveScrollUp();
			commonActions.moveScrollUp();
			Thread.sleep(10000);
			

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Then("show mobiles of flipkart assure")
	public void show_mobiles_of_flipkart_assure() {
	    // Write code here that turns the phrase above into concrete actions
	    //throw new cucumber.api.PendingException();
	}

	@Then("click on {string}")
	
	
	public void click_on(String RAM_field) {

		try {
			Thread.sleep(3000);
			//commonActions.click(RAM_field);
			commonActions.mouseover(RAM_field);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Then("user selects {string} from {string}")
	public void user_selects_from(String min_value, String min_field)
	{
	  try {
		 // commonActions.sele
		commonActions.selectFromDropDownByValue(min_field, min_value);
		Thread.sleep(3000);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}
	
	
	
	
	
}
