package kz.olx;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import utils.DeviceHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.testng.annotations.Test;

public class runTest extends preConditions {

	public static String loggedInUserMobile = "";
	public static String loggedUserName = "";

	@Test(dataProvider = "getCsvData")
	public void creatingAd(String Mobile, String Password, String Category, String Location,
			String Description, String Price, String Title, String ContactPerson, String Email, 
			String noOfPhotos) throws InterruptedException, IOException {
		logger = extent.startTest("Adding olx ad", Mobile+","+Password+","+Category+","+Location+","+
			Description+","+Price+","+Title+","+ContactPerson+","+Email+","+noOfPhotos);
		if (verifyWelcomeScreenIsDisplaying()) {
			userLoginFromWelcomeScreen(Mobile, Password);
		}
		if (loggedInUserMobile.equalsIgnoreCase(Mobile)) {
			firstTimeLogin(noOfPhotos, Category, Location, Title, Description, Price, ContactPerson, Email);
		} else {
			logout();
			userLoginFromHomeScreen(Mobile, Password);
			firstTimeLogin(noOfPhotos, Category, Location, Title, Description, Price, ContactPerson, Email);
		}
	}
	
	@AfterMethod
	public void getResults(ITestResult result) throws Exception {
		if(result.getStatus() == ITestResult.FAILURE){
			 logger.log(LogStatus.FAIL, "Test Case Failed is "+result.getName());
			 logger.log(LogStatus.FAIL, "Test Case Failed is "+result.getThrowable());
			 //To capture screenshot path and store the path of the screenshot in the string "screenshotPath"
			                        //We do pass the path captured by this mehtod in to the extent reports using "logger.addScreenCapture" method. 
			                        String screenshotPath = getScreenshot(result.getName());
			 //To add it in the extent report 
			 logger.log(LogStatus.FAIL, logger.addScreenCapture(screenshotPath));
			 openHomeScreen();
		}else if(result.getStatus() == ITestResult.SKIP){
			 logger.log(LogStatus.SKIP, "Test Case Skipped is "+result.getName());
		}else if (result.getStatus() == ITestResult.SUCCESS) {
			logger.log(LogStatus.PASS, result.getName()+"is Test Case passed");
		}
			 // ending test
		//endTest(logger) : It ends the current test and prepares to create HTML report
		extent.endTest(logger);
	}
	
	@DataProvider
	public Object[][] getCsvData() throws IOException{
		String pathToCsv = System.getProperty("user.dir")+"/DataFile/Testdata.csv";
		String row;
		int noOfRows = 0;
		String data[];
		BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
		csvReader.readLine();
		while ((row = csvReader.readLine()) != null) {
			noOfRows++;
		}
		csvReader = new BufferedReader(new FileReader(pathToCsv));
		String firstLine = csvReader.readLine();
		int column = firstLine.split(";").length;
		Object[][] csvData = new Object[noOfRows][column];
		for (int i=0; (row = csvReader.readLine()) != null; i++) {
			data = row.split(";");
			for (int j = 0; j < 10; j++) {
				csvData[i][j] = data[j];
			}
		}
		loggedInUserMobile = (String) csvData[0][0];
		return csvData;
	}

	public void firstTimeLogin(String noOfPohotos, String Category, String Location, String Title, String Description,
			String Price, String ContactPerson, String Email) throws InterruptedException {
		openPostAdForm();
		verifyThatRecoveryPopupAppearing();
		attachImagesFromGallery(noOfPohotos);
		fillAdForm(Title, Description, ContactPerson, Email, Category, Price, Location);
		loggedInUserMobile = getUserMobileNumber();
		submitForm();
	}
}
