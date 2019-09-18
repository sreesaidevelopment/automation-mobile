package kz.olx;

import static java.time.Duration.ofSeconds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.touch.offset.PointOption;
import net.bytebuddy.asm.Advice.Local;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;
import utils.DeviceHelper;


public class preConditions
{
	public static AppiumDriverLocalService service;
	private AppiumServiceBuilder builder;
	private DesiredCapabilities cap;
	public AndroidDriver<MobileElement> driver;
	
	public void userLoginFromWelcomeScreen(String Mobile, String Password){
		driver.findElement(By.id("kz.slando:id/loginWithEmail")).click();
		driver.findElement(By.xpath("//*[@resource-id='kz.slando:id/edtEmail']//*[@resource-id='kz.slando:id/value']")).sendKeys(Mobile);
		driver.findElement(By.xpath("//*[@resource-id='kz.slando:id/edtPassword']//*[@resource-id='kz.slando:id/value']")).sendKeys(Password);
		if (driver.findElement(By.id("kz.slando:id/btnLogInNew")).isDisplayed()) {
			driver.findElement(By.id("kz.slando:id/btnLogInNew")).click();
		}
		//menu button in home screen
		MobileElement menuButton = (MobileElement) driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc='Открыть меню']"));
		waitTillTheElementIsVisible(menuButton);
	}
	
	public void userLoginFromHomeScreen(String Mobile, String Password) {
		MobileElement menuButton = (MobileElement) driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc='Открыть меню']"));
		waitTillTheElementIsVisibleAndClickable(menuButton);
		menuButton.click();
		driver.findElement(By.id("kz.slando:id/log_in")).click();
		waitTillTheElementIsVisibleAndClickable((MobileElement)driver.findElement(By.id("kz.slando:id/btnLoginWithPassword")));
		driver.findElement(By.id("kz.slando:id/btnLoginWithPassword")).click();
		waitTillTheElementIsVisible((MobileElement)driver.findElement(By.xpath("//*[@resource-id='kz.slando:id/edtEmail']//*[@resource-id='kz.slando:id/value']")));
		driver.findElement(By.xpath("//*[@resource-id='kz.slando:id/edtEmail']//*[@resource-id='kz.slando:id/value']")).sendKeys(Mobile);
		driver.findElement(By.xpath("//*[@resource-id='kz.slando:id/edtPassword']//*[@resource-id='kz.slando:id/value']")).sendKeys(Password);
		driver.findElement(By.id("kz.slando:id/btnLogInNew")).click();
		waitTillTheElementIsVisible(driver.findElement(By.xpath("//*[@text='Подать объявление']")));
		tapOnPoint(600, 200);
	}
	
	public void openPostAdForm() {
    	//Menu button in home screen
		MobileElement menuButton = (MobileElement) driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc='Открыть меню']"));
		waitTillTheElementIsVisibleAndClickable(menuButton);
		menuButton.click();
		//Create Ad link from menu options
		driver.findElement(By.xpath("//*[@text='Подать объявление']")).click();
    }
	
	public void verifyThatRecoveryPopupAppearing() throws InterruptedException {
		Thread.sleep(3000);
		List<MobileElement> textData = driver.findElements(By.xpath("//android.widget.TextView"));
		for (int i = 0; i < textData.size(); i++) {
			if (textData.get(i).getText().equalsIgnoreCase("Назад к странице добавления")) {
				driver.findElement(By.xpath("//*[@text='ОТМЕНИТЬ']")).click();
				break;
			}
		}
	}
	
	public void attachImagesFromGallery(String noOfPohotos) {
    	int imagesCount = Integer.parseInt(noOfPohotos);
    	//camera button in create ad form
    	driver.findElement(By.id("kz.slando:id/photos_add_box")).click();
    	//List of gallery images
    	List<MobileElement> galleryImages = driver.findElements(By.id("kz.slando:id/img"));
    	for (int i = 0; i < imagesCount; i++) {
    		galleryImages.get(i).click();
		}
    	driver.findElement(By.id("kz.slando:id/action_done")).click();
    	waitTillTheElementIsVisible((MobileElement)driver.findElement(By.xpath("(//*[@resource-id='kz.slando:id/image'])[1]")));
    }
    
	public void setCategory(String Category, String Price, String Title) {
    	switch (Category) {
		case "cat1":
			driver.findElement(By.xpath("//*[@text='Рубрика']")).click();
			driver.findElement(By.xpath("//*[@text='Электроника']")).click();
			waitTillTheElementIsVisible(driver.findElement(By.xpath("//*[@text='Телефоны и аксессуары']")));
			driver.findElement(By.xpath("//*[@text='Телефоны и аксессуары']")).click();
			waitTillTheElementIsVisible(driver.findElement(By.xpath("//*[@text='Мобильные телефоны / смартфоны']")));
			driver.findElement(By.xpath("//*[@text='Мобильные телефоны / смартфоны']")).click();
			//Private or business
			driver.findElement(By.xpath("//*[@text='Частные или бизнес']")).click();
			driver.findElement(By.xpath("//*[@text='Частные']")).click();
			//Phone Brand
			driver.findElement(By.xpath("//*[@text='Марка телефона']")).click();
			selectPooneBranch(Title);
			swipeUp();
			//Operating System
			driver.findElement(By.xpath("//*[@text='Операционная система']")).click();
			selectOperatingSystem();
			//Screen size
			driver.findElement(By.xpath("//*[@text='Диагональ экрана']")).click();
			selectSize();
			//Entering price
			driver.findElement(By.xpath("//*[@text='Цена (тг.)']")).click();
			driver.findElement(By.id("kz.slando:id/value")).sendKeys(Price);
			driver.findElement(By.xpath("//*[@text='ГОТОВО']")).click();
			swipeUp();
			//Condition
			driver.findElement(By.xpath("//*[@text='Состояние']")).click();
			List<MobileElement> conditionOptions1 = driver.findElements(By.id("android:id/text1"));
			conditionOptions1.get(0).click();
			break;
			
		case "cat2":
			driver.findElement(By.xpath("//*[@text='Рубрика']")).click();
			waitTillTheElementIsVisible((MobileElement)driver.findElement(By.xpath("//*[@text='Электроника']")));
			driver.findElement(By.xpath("//*[@text='Электроника']")).click();
			waitTillTheElementIsVisible((MobileElement)driver.findElement(By.xpath("//*[@text='Компьютеры и комплектующие']")));
			driver.findElement(By.xpath("//*[@text='Компьютеры и комплектующие']")).click();
			waitTillTheElementIsVisible((MobileElement)driver.findElement(By.xpath("//*[@text='Комплектующие и аксессуары']")));
			driver.findElement(By.xpath("//*[@text='Комплектующие и аксессуары']")).click();
			waitTillTheElementIsVisible((MobileElement)driver.findElement(By.xpath("//*[@text='Частные или бизнес']")));
			//Filling options fields
			driver.findElement(By.xpath("//*[@text='Частные или бизнес']")).click();
			driver.findElement(By.xpath("//*[@text='Частные']")).click();
			driver.findElement(By.xpath("//*[@text='Цена (тг.)']")).click();
			driver.findElement(By.id("kz.slando:id/value")).sendKeys(Price);
			driver.findElement(By.xpath("//*[@text='ГОТОВО']")).click();
			driver.findElement(By.xpath("//*[@text='Типы комплектующих']")).click();
			List<MobileElement> options = driver.findElements(By.id("android:id/text1"));
			options.get(1).click();
			swipeUp();
			driver.findElement(By.xpath("//*[@text='Состояние']")).click();
			List<MobileElement> conditionOptions2 = driver.findElements(By.id("android:id/text1"));
			conditionOptions2.get(0).click();
			break;
		
		case "cat3":
			driver.findElement(By.xpath("//*[@text='Рубрика']")).click();
			waitTillTheElementIsVisible((MobileElement)driver.findElement(By.xpath("//*[@text='Электроника']")));
			driver.findElement(By.xpath("//*[@text='Электроника']")).click();
			waitTillTheElementIsVisible(driver.findElement(By.xpath("//*[@text='Фото / видео']")));
			driver.findElement(By.xpath("//*[@text='Фото / видео']")).click();
			waitTillTheElementIsVisible(driver.findElement(By.xpath("//*[@text='Цифровые фотоаппараты']")));
			driver.findElement(By.xpath("//*[@text='Цифровые фотоаппараты']")).click();
			driver.findElement(By.xpath("//*[@text='Частные или бизнес']")).click();
			driver.findElement(By.xpath("//*[@text='Частные']")).click();
			driver.findElement(By.xpath("//*[@text='Цена (тг.)']")).click();
			driver.findElement(By.id("kz.slando:id/value")).sendKeys(Price);
			driver.findElement(By.xpath("//*[@text='ГОТОВО']")).click();
			//Brand of Camera
			driver.findElement(By.xpath("//*[@text='Марка фотоаппарата']")).click();
			List<MobileElement> cameraBrand = driver.findElements(By.id("android:id/text1"));
			cameraBrand.get(generateRandomIntIntRange(0, cameraBrand.size()-1)).click();
			//Condition
			driver.findElement(By.xpath("//*[@text='Состояние']")).click();
			List<MobileElement> conditionOptions3 = driver.findElements(By.id("android:id/text1"));
			conditionOptions3.get(0).click();
			swipeUp();
			break;
		
		case "cat4":
			driver.findElement(By.xpath("//*[@text='Рубрика']")).click();
			waitTillTheElementIsVisible((MobileElement)driver.findElement(By.xpath("//*[@text='Электроника']")));
			driver.findElement(By.xpath("//*[@text='Электроника']")).click();
			waitTillTheElementIsVisible(driver.findElement(By.xpath("//*[@text='Игры и игровые приставки']")));
			driver.findElement(By.xpath("//*[@text='Игры и игровые приставки']")).click();
			waitTillTheElementIsVisible(driver.findElement(By.xpath("//*[@text='Приставки']")));
			driver.findElement(By.xpath("//*[@text='Приставки']")).click();
			//private or business
			driver.findElement(By.xpath("//*[@text='Частные или бизнес']")).click();
			driver.findElement(By.xpath("//*[@text='Частные']")).click();
			//entering price
			driver.findElement(By.xpath("//*[@text='Цена (тг.)']")).click();
			driver.findElement(By.id("kz.slando:id/value")).sendKeys(Price);
			driver.findElement(By.xpath("//*[@text='ГОТОВО']")).click();
			//condition
			driver.findElement(By.xpath("//*[@text='Состояние']")).click();
			List<MobileElement> conditionOptions4 = driver.findElements(By.id("android:id/text1"));
			conditionOptions4.get(0).click();
			//Types of Console
			driver.findElement(By.xpath("//*[@text='Тип приставки']")).click();
			List<MobileElement> typeOfConsole = driver.findElements(By.id("android:id/text1"));
			typeOfConsole.get(generateRandomIntIntRange(0, typeOfConsole.size()-1)).click();
			swipeUp();
			break;
		
		case "cat5":
			driver.findElement(By.xpath("//*[@text='Рубрика']")).click();
			waitTillTheElementIsVisible((MobileElement)driver.findElement(By.xpath("//*[@text='Электроника']")));
			driver.findElement(By.xpath("//*[@text='Электроника']")).click();
			waitTillTheElementIsVisible(driver.findElement(By.xpath("//*[@text='Планшеты / эл. книги и аксессуары']")));
			driver.findElement(By.xpath("//*[@text='Планшеты / эл. книги и аксессуары']")).click();
			waitTillTheElementIsVisible(driver.findElement(By.xpath("//*[@text='Планшетные компьютеры']")));
			driver.findElement(By.xpath("//*[@text='Планшетные компьютеры']")).click();
			//Private or business
			driver.findElement(By.xpath("//*[@text='Частные или бизнес']")).click();
			driver.findElement(By.xpath("//*[@text='Частные']")).click();
			//Tablet brand
			driver.findElement(By.xpath("//*[@text='Марка планшета']")).click();
			List<MobileElement> tabletBrand = driver.findElements(By.id("android:id/text1"));
			tabletBrand.get(generateRandomIntIntRange(0, tabletBrand.size()-1)).click();
			//Entering Price
			driver.findElement(By.xpath("//*[@text='Цена (тг.)']")).click();
			driver.findElement(By.id("kz.slando:id/value")).sendKeys(Price);
			driver.findElement(By.xpath("//*[@text='ГОТОВО']")).click();
			//Condition
			driver.findElement(By.xpath("//*[@text='Состояние']")).click();
			List<MobileElement> conditionOptions5 = driver.findElements(By.id("android:id/text1"));
			conditionOptions5.get(0).click();
			swipeUp();
			break;
			
		case "cat6":
			driver.findElement(By.xpath("//*[@text='Рубрика']")).click();
			waitTillTheElementIsVisible((MobileElement)driver.findElement(By.xpath("//*[@text='Электроника']")));
			driver.findElement(By.xpath("//*[@text='Электроника']")).click();
			waitTillTheElementIsVisible(driver.findElement(By.xpath("//*[@text='Ноутбуки и аксессуары']")));
			driver.findElement(By.xpath("//*[@text='Ноутбуки и аксессуары']")).click();
			waitTillTheElementIsVisible(driver.findElement(By.xpath("//*[@text='Ноутбуки']")));
			driver.findElement(By.xpath("//*[@text='Ноутбуки']")).click();
			//Private or business
			driver.findElement(By.xpath("//*[@text='Частные или бизнес']")).click();
			driver.findElement(By.xpath("//*[@text='Частные']")).click();
			//Entering Price
			driver.findElement(By.xpath("//*[@text='Цена (тг.)']")).click();
			driver.findElement(By.id("kz.slando:id/value")).sendKeys(Price);
			driver.findElement(By.xpath("//*[@text='ГОТОВО']")).click();
			//Laptop Brand
			driver.findElement(By.xpath("//*[@text='Марка ноутбука']")).click();
			List<MobileElement> laptopBrand = driver.findElements(By.id("android:id/text1"));
			laptopBrand.get(generateRandomIntIntRange(0, laptopBrand.size()-1)).click();
			//Screen Diagonal
			driver.findElement(By.xpath("//*[@text='Диагональ экрана']")).click();
			List<MobileElement> screenSize = driver.findElements(By.id("android:id/text1"));
			screenSize.get(generateRandomIntIntRange(0, screenSize.size()-1)).click();
			//Condition
			driver.findElement(By.xpath("//*[@text='Состояние']")).click();
			List<MobileElement> conditionOptions6 = driver.findElements(By.id("android:id/text1"));
			conditionOptions6.get(0).click();
			swipeUp();
			break;
			
		default:
			break;
		}
    }
    
	public void setLocation(String Location) throws InterruptedException {
		Thread.sleep(3000);
    	driver.findElement(By.xpath("//*[@text='Местоположение']")).click();
    	waitTillTheElementIsVisible((MobileElement)driver.findElement(By.id("kz.slando:id/search_src_text")));
    	driver.findElement(By.id("kz.slando:id/search_src_text")).sendKeys(Location);
    	Thread.sleep(3000);
    	tapOnPoint(330, 330);
    	waitTillTheElementIsVisible(driver.findElement(By.id("kz.slando:id/accept")));
    	if (driver.findElement(By.id("kz.slando:id/accept")).isEnabled()) {
    		driver.findElement(By.id("kz.slando:id/accept")).click();
		}else {
			Assert.assertEquals(driver.findElement(By.id("kz.slando:id/accept")).isEnabled(), true);
		}
    	
    }
    
	public void fillAdForm(String Title, String Description, String ContactPerson, String Email, String Category, String Price, String Location) throws InterruptedException {
    	driver.findElement(By.xpath("//*[@text='Заголовок']")).sendKeys(Title);
    	setCategory(Category, Price, Title);
    	driver.findElement(By.xpath("//*[@text='Описание']")).sendKeys(Description);
    	setLocation(Location);
    	swipeUp();
    	if (verifyContactFieldIsAppearing()) {
    		driver.findElement(By.xpath("//*[@text='Контактное лицо']")).sendKeys(ContactPerson);
		}
    	driver.findElement(By.xpath("//*[@text='E-mail']")).sendKeys(Email);
    }
	
	public boolean verifyContactFieldIsAppearing() {
		List<MobileElement> textFields = driver.findElements(By.id("kz.slando:id/value"));
		boolean flag = false;
		for (int i = 0; i < textFields.size(); i++) {
			if (textFields.get(i).getText().equalsIgnoreCase("Контактное лицо")) {
				flag = true;
			}
		}
		return flag;
	}
	
	public String getUserMobileNumber() {
		List<MobileElement> textFields = driver.findElements(By.id("kz.slando:id/value"));
		String mobileNumber = textFields.get(textFields.size()-1).getText();
		return mobileNumber;
	}
    
	public void submitForm() throws InterruptedException {
		swipeUp();
    	driver.findElement(By.id("kz.slando:id/postAdBtn")).click();
    	Thread.sleep(8000);
    	waitTillTheElementIsVisible(driver.findElement(By.xpath("//*[@text='Реклама']")));
    	waitTillTheElementIsVisible(driver.findElement(By.xpath("//*[@text='Не рекламировать']")));
		driver.findElement(By.xpath("//*[@text='Не рекламировать']")).click();
		waitTillTheElementIsVisible(driver.findElement(By.xpath("//*[@text='Подать объявление']")));
		backToHomeScreen();
    }
    
	public void logout() {
    	MobileElement menuButton = (MobileElement) driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc='Открыть меню']"));
		waitTillTheElementIsVisibleAndClickable(menuButton);
		menuButton.click();
		driver.findElement(By.xpath("//android.widget.TextView[@text='Выйти']")).click();
    }
  
  @BeforeTest
  public void launchApp() throws MalformedURLException {      
	  DesiredCapabilities capabilities = new DesiredCapabilities();
	  capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
	  capabilities.setCapability("deviceName", "Redmi 4");
	  capabilities.setCapability("platformVersion", "7.1.2");
	  capabilities.setCapability("platformName", "Android");
	  capabilities.setCapability("automationName", "UiAutomator2");
	  capabilities.setCapability("udid", "8da7f05c7d44");
	  capabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
	  capabilities.setCapability("appPackage", "kz.slando");
	  capabilities.setCapability("appActivity", "pl.tablica2.app.startup.activity.StartupActivity");
	  
	  driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
	  driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
  }

  public void backToHomeScreen(){
	try {
		driver.findElement(By.xpath("//*[@content-desc='Navigate up']")).click();
	} catch (Exception e) {
		backToHomeScreen();
	}
  }
  
  public void openHomeScreen() {
	  MobileElement title = driver.findElement(By.xpath("//*[@resource-id='kz.slando:id/action_bar']/android.widget.TextView"));
	  if (title.getText().equalsIgnoreCase("Местоположение") || (title.getText().equalsIgnoreCase("Выбор изображения"))) {
		  backToHomeScreen();
		  backToHomeScreen();
	  }else if(title.getText().equalsIgnoreCase("Подать объявление")) {
		  backToHomeScreen();
	  }
  }
  
  public void selectPooneBranch(String Title) {
	  List<MobileElement> mobileBrands = driver.findElements(By.id("android:id/text1"));
	  mobileBrands.get(generateRandomIntIntRange(0, mobileBrands.size()-1)).click(); 
  }
  
  public void selectOperatingSystem() {
	  List<MobileElement> os = driver.findElements(By.id("android:id/text1"));
	  os.get(generateRandomIntIntRange(0, os.size()-1)).click(); 
  }
  
  public void selectSize() {
	  List<MobileElement> screenSizes = driver.findElements(By.id("android:id/text1"));
	  screenSizes.get(generateRandomIntIntRange(0, screenSizes.size()-1)).click();
  }
  
  @AfterTest
  public void closeApp() {
	  driver.quit();
  }
  
  public static int generateRandomIntIntRange(int min, int max) {
	    Random r = new Random();
	    return r.nextInt((max - min) + 1) + min;
  }
  
  public void waitTillTheElementIsVisibleAndClickable(MobileElement element) {

      WebDriverWait wait = new WebDriverWait(driver, 10);
      wait.until(ExpectedConditions.visibilityOf(element));

      wait = new WebDriverWait(driver, 5);
      wait.until(ExpectedConditions.elementToBeClickable(element));
  }
  
  public void tapOnPoint(int x1, int y1) {
      new TouchAction((AndroidDriver<MobileElement>) driver).tap(PointOption.point(x1, y1)).perform();
  }

  public void waitTillTheElementIsVisible(MobileElement element) {
      WebDriverWait wait = new WebDriverWait(driver, 20);
      wait.until(ExpectedConditions.visibilityOf(element));
  }
  
  public void swipeUp() {
      Dimension size = driver.manage().window().getSize();
      int starty = (int) (size.height * 0.8);
      int endy = (int) (size.height * 0.2);
      int startx = (int) (size.width / 2.2);
      try {
          new TouchAction((PerformsTouchActions) driver).press(point(startx, starty)).waitAction(waitOptions(ofSeconds(3)))
                  .moveTo(point(startx, endy)).release().perform();
          //reportLogging("Swipe up");
      } catch (Exception e) {
    	  swipeUp();
      }
  }
  
  public boolean verifyWelcomeScreenIsDisplaying() throws InterruptedException {
	  Thread.sleep(3000);
	  String loginText = "Войти с номером телефона или email-адресом";
	  boolean flag = false;
	  List<MobileElement> welcomeScreenText = driver.findElements(By.xpath("//android.widget.TextView"));
	  for (int i = 0; i < welcomeScreenText.size(); i++) {
		if (loginText.equalsIgnoreCase(welcomeScreenText.get(i).getText())) {
			flag = true;
			break;
		}
	  }
	  return flag;
  }
}
