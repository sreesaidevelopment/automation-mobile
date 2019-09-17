package kz.olx;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumDriver;

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
	public void startTest(String Mobile, String Password, String Category, String Location,
			String Description, String Price, String Title, String ContactPerson, String Email, 
			String noOfPhotos) throws InterruptedException, IOException {
		try {
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
		} catch (Exception e) {
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			//The below method will save the screen shot in d drive with name "screenshot.png"
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			File screenShotName = new File("Screenshot"+timeStamp+".png");
			FileUtils.copyFile(scrFile, screenShotName);
			
			String filePath = screenShotName.toString();
			String path = "<img src=\"file://" + filePath + "\" alt=\"\"/>";
			Reporter.log(e.toString());
			Reporter.log(path);
			Assert.fail("Please find the attachment for the issue.");
		}
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
		attachImagesFromGallery(noOfPohotos);
		fillAdForm(Title, Description, ContactPerson, Email, Category, Price, Location);
		loggedInUserMobile = getUserMobileNumber();
		submitForm();
	}
}
