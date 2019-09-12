package kz.olx;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;


public class preConditions 
{
	public static AppiumDriverLocalService service;
	private AppiumServiceBuilder builder;
	private DesiredCapabilities cap;
	public String serverIp = "127.0.0.1";
	public int Port = 4723;
	private static AndroidDriver driver;
	
	
	public void startAppiumServer() {
		//Set Capabilities
		cap = new DesiredCapabilities();
		//Build the Appium service
		builder = new AppiumServiceBuilder();
		builder.withIPAddress(serverIp);
		builder.usingPort(Port);
		builder.withCapabilities(cap);
		builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
		builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");
		
		AppiumDriverLocalService service = AppiumDriverLocalService.buildDefaultService();
		service.start();
		System.out.println("Appium server started");
	}
	
	
	public void setupServer() {
		if(!checkIfServerIsRunnning(Port)) {
			startAppiumServer();
		} else {
			System.out.println("Appium Server already running on Port - " + Port);
		}
	}
	
	public void stopServer() {
		service.stop();
		System.out.println("Server stopped");
	}
	

	public boolean checkIfServerIsRunnning(int port) {
			
			boolean isServerRunning = false;
			ServerSocket serverSocket;
			try {
				serverSocket = new ServerSocket(port);
				serverSocket.close();
			} catch (IOException e) {
				//If control comes here, then it means that the port is in use
				isServerRunning = true;
			} finally {
				serverSocket = null;
			}
			return isServerRunning;
		}
	
  @Test(dataProvider="logindata")	
  public void startTest(String uname,String pword) throws MalformedURLException
  {
	  launchApp();
	  System.out.println("App Launched");
	  closeApp();
	  System.out.println(uname+" "+pword);
	  
  }	
	
  @DataProvider	
  public Object[][] logindata() throws IOException
  {
	  String xlfile,xlsheet;
	  xlfile = "/Users/mtpl/Automation/olx/DataFile/Testdata.xlsx";
	  xlsheet = "LoginData";
	  int rc,cc;
	  
	  XLUtils xl=new XLUtils();
	  rc=xl.getRowCount(xlfile,xlsheet);
	  cc=xl.getCellCount(xlfile, xlsheet, 1);
	  Object[][] data=new Object[rc][cc];
	  
	  for (int i = 1; i <= rc; i++) 
	  {
		  data[i-1][0]=xl.getCellData(xlfile, xlsheet, i, 0);
		  data[i-1][1]=xl.getCellData(xlfile, xlsheet, i, 1);
	  }
	  return data; 
	  
  }
  
  public void launchApp() throws MalformedURLException {
	  
	  DesiredCapabilities capabilities = new DesiredCapabilities();
	  capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
	  capabilities.setCapability("deviceName", "Redmi 4");
	  capabilities.setCapability("platformVersion", "7.1.2");
	  capabilities.setCapability("platformName", "Android");
	  capabilities.setCapability("automationName", "UiAutomator2");
	  capabilities.setCapability("udid", "8da7f05c7d44");
	  capabilities.setCapability("appPackage", "kz.slando");
	  capabilities.setCapability("appActivity", "pl.tablica2.app.startup.activity.StartupActivity");
	  
	  driver = new AndroidDriver<MobileElement>(new URL("http://"+serverIp+":"+Port+"/wd/hub"), capabilities);
	  driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
  }

  public void closeApp() {
	  driver.quit();
  }
	
}
