package utils;

import io.appium.java_client.*;
import io.appium.java_client.android.AndroidDriver;

import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.Calendar;
import java.util.List;

import static io.appium.java_client.touch.LongPressOptions.longPressOptions;
import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofSeconds;


public class DeviceHelper {

    public AppiumDriver driver;
    static WebDriverWait wait = null;

    public DeviceHelper(AppiumDriver driver) {
        this.driver = driver;
    }

    //Method to wait for time
    public static void waitWebDriver(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println("Method: waitWebDriver :: exception =  " + e.getMessage());

        }
    }


    /**
     * Perform swipe action vertically from point X to point Y on any screen
     *
     * @author :
     * @since : 22-July-2019
     */
    public void swipeUp() {
        Dimension size = driver.manage().window().getSize();
        int starty = (int) (size.height * 0.8);
        int endy = (int) (size.height * 0.2);
        int startx = (int) (size.width / 2.2);
        try {
            System.out.println("Trying to swipe up from x:" + startx + " y:" + starty + ", to x:" + startx + " y:" + endy);
            new TouchAction((PerformsTouchActions) driver).press(point(startx, starty)).waitAction(waitOptions(ofSeconds(3)))
                    .moveTo(point(startx, endy)).release().perform();
            //reportLogging("Swipe up");
        } catch (Exception e) {
            System.out.println("Swipe did not complete succesfully.");
        }
    }

    public void waitTillTheElementIsVisibleAndClickable(MobileElement element) {

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(element));

        wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public boolean waitTillTheElementInVisible(MobileElement element) {

        WebDriverWait wait = new WebDriverWait(driver, 30);
        return wait.until(ExpectedConditions.invisibilityOf(element));

    }

    public void waitForPageToLoad(WebElement id) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.elementToBeClickable(id));
    }

    public void waitForElementState(WebElement id) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.stalenessOf(id));

        wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.elementToBeClickable(id));
    }

    public void waitForPageToLoad(List<WebElement> id) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfAllElements(id));
    }

    public void waitForElementToDisAppear(List<WebElement> id) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.invisibilityOfAllElements(id));
    }

    public void waitForElementToDisAppear(List<WebElement> id, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.invisibilityOfAllElements(id));
    }

    public MobileElement waitForElementToAppear(MobileElement id) {
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.visibilityOf(id));
        return id;
    }

    public WebElement waitForElementToAppear(WebElement id, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.visibilityOf(id));
        return id;
    }

    public WebElement waitForElement(WebElement arg) {
        waitForPageToLoad(arg);
        WebElement el = arg;
        return el;
    }

    public void WaitForFrameAndSwitchToIt(String id) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(id));
    }

    public void WaitForFrameAndSwitchToIt(int id) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(id));
    }

    public void ScrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void waitForElements(List<WebElement> arg) {
        waitForPageToLoad(arg);
    }

    public MobileElement waitForElementToAppearOnScreen(MobileElement arg) {
        waitForElementToAppear(arg);
        MobileElement el = arg;
        return el;
    }

    public void clickUntilElementExists(WebElement clickLocator, By by) {
        boolean elementOnScreen;
        int i = 0;
        do {
            if (i == 25) {
                break;
            }
            try {
                driver.findElement(by);
                break;
            } catch (NoSuchElementException e) {
                clickLocator.click();
                elementOnScreen = false;
                System.out.println(i);
            }
            i++;
        } while (!elementOnScreen);
    }

    public String getCurrentMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    /*
    public void logStepIntoExtentReport(String elementDescription, String action,
                                        String typeString) {
        ExtentTestManager.getTest().log(Status.INFO,
                elementDescription + "; " + withBoldHTML("Text") + ": " + typeString);
    }
    */

    public String withBoldHTML(String string) {
        if (!string.trim().isEmpty()) {
            return "<b>" + string + "</b>";
        } else {
            return "";
        }
    }

    /*
    public String getPageObjectElemetDescription(Object pageObject, String fieldName) {
        try {
            return this.getClass().getAnnotation(PageName.class).value() + "::" +
                    pageObject.getClass().getField(fieldName).getAnnotation(ElementDescription.class)
                            .value();
        } catch (NoSuchFieldException e) {

            e.printStackTrace();
        }
        return "";
    }
    */

    /**
     * This Function is to check the element is present or not
     * @author Ramesh
     * @param: Mobile Element
     */
    public boolean isElementPresent(MobileElement locator) {
        try {
            if (locator.isDisplayed())
                System.out.println("Element presend on screen ***********" + locator);
            return true;
        } catch (NoSuchElementException e) {
            System.out.println("Element not present on screen **************" + locator);
            return false;
        }
    }

    public String getCurrentMonth(int month) {
        int i = Calendar.getInstance().get(Calendar.MONTH);
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month + i];
    }

    public void refreshWebPage() {
        driver.navigate().refresh();
    }

    public void switchToNewWindow() {
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
    }

    /**
     * Swipe the screen on the basis of x,y Co-ordinates. Start from (x1,y1) to (x2, Y2)
     * <p>
     * param x1
     * param y1
     *
     * @param x2
     * @param y2
     * @author jasmeetsingh
     */
    public void swipe(int x1, int y1, int x2, int y2) {
        new TouchAction(((AppiumDriver<MobileElement>) driver)).press(PointOption.point(x1, y1))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))).moveTo(PointOption.point(x2, y2))
                .release().perform();
    }

    public String GetTextOfElement(By value) {

        WebElement element = driver.findElement(value);

        return element.getText();
    }

    /**
     * This Function will pause the execution for given secs.
     *
     * @param secs : No of seconds to be paused.
     * @author jasmeetsingh
     */
    public void waitInSec(int secs) {
        try {
            Thread.sleep(secs * 1000);
            
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * This function will search for the element till(Maximum)
     * value of 'explicit_timeout' in Config.
     *
     * @param locator : Pass the By Locator
     * @return : MobileElement
     * @author : Jasmeetsingh
     */
    public MobileElement getElement(By locator) {
        if (wait == null)
            wait = new WebDriverWait(((AppiumDriver<MobileElement>) driver), 50);
        return (MobileElement) wait
                .until(ExpectedConditions.visibilityOf(((AppiumDriver<MobileElement>) driver).findElement(locator)));
    }


    /**
     * @param locator : By Locator of the element
     * @return true if element is displayed. Otherwise false.
     * @author Jasmeetsingh
     */
    public boolean isDisplayed(MobileElement locator) {


        return locator.isDisplayed();
    }

    /**
     * This Function is to wait till element present
     * @author Ramesh
     * @param: Mobile Element
     */
    public void waitTillTheElementIsVisible(MobileElement element) {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * This Function is to enter text in text field using action class
     * @author Ramesh
     * @param: Mobile Element & String
     */
    public void writeInputActions(MobileElement element, String otp) {
        try {
            waitTillTheElementIsVisible(element);
            Actions a = new Actions(driver);
            a.sendKeys(otp).build().perform();
        } catch (Exception e) {
            System.out.println("Write Input Action failed");
        }
    }

    /**
     * This function will longPress on the MobileElement provided
     * @param: MobileElement & int
     * @author Deepak Pedagada
     */

    public void longPress(MobileElement element, int value) {
        try {

            new TouchAction(driver).longPress(longPressOptions().withElement(element(element)).withDuration(Duration.ofSeconds(value))).release().perform();

        } catch (Exception e) {
            System.out.println("LongPress failed");
            System.out.println(e.getMessage());
        }
    }

    /* Tap the screen on the basis of x1,y1 point.
     * @author Diljeet Singh Ranaut
     * @param x1
     * @param y1
     * @since 10 july
     */
    public void tapOnPoint(int x1, int y1) {
        new TouchAction((AppiumDriver<MobileElement>) driver).tap(PointOption.point(x1, y1)).perform();
    }

    /* Zoom camera.
     * @author Sreesai
     * @param windowElement
     *
     * @since 1 Aug
     */
    public void zoom(MobileElement windowElement){
        int leftX = windowElement.getLocation().getX();
        int rightX = windowElement.getSize().getWidth() + leftX;
        int midX = (leftX + rightX) / 2;

        int upperY = windowElement.getLocation().getY();
        int lowerY = windowElement.getSize().getHeight() + upperY;
        int midY = (upperY + lowerY) / 2;

        int aX = (int) (midX * 0.3);
        int aY = (int) (midY * 1.7);
        int bX = (int) (midX * 0.8);
        int bY = (int) (midY * 1.2);

        int cX = (int) (midX * 1.7);
        int cY = (int) (midY * 0.3);
        int dX = (int) (midX * 1.2);
        int dY = (int) (midY * 0.8);

        TouchAction action0 = new TouchAction(driver);
        TouchAction action1 = new TouchAction(driver);

        action0.press(PointOption.point(bX, bY)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))).moveTo(PointOption.point(aX, aY)).release();
        action1.press(PointOption.point(dX, dY)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))).moveTo(PointOption.point(cX, cY)).release();

        MultiTouchAction mAction = new MultiTouchAction(driver);

        mAction.add(action0).add(action1).perform();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * This Function is to Scroll to element
     * @author Ramesh
     * @param: Mobile Element & String
     */
    public void scrollToMobileElement(MobileElement element, String scrollcount) {
        try {
            waitInSec(3);
            int count = Integer.parseInt(scrollcount);
            for (int i = 0; i < count; i++) {
                if (isElementPresent(element)) {
                    isElementPresent(element);
                } else {
                    swipeUp();
                }

            }
        } catch (Exception e) {
            System.out.println("Scroll to mobile element failed");
        }
    }
    /**
     * This method will click on the Back button.
     *
     * @author Jasmeet
     * @since 15 July 2019
     */
    public void clickBackBtn() {
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
    }

    /**
     * This method will return the Width of the Phone Screen
     *
     * @author Jasmeet
     * @since 08 July 2019
     */
    public int getWidthOfScreen() {
        return driver.manage().window().getSize().width;
    }

    /**
     * This method will return the Height of the Phone Screen
     *
     * @author Jasmeet
     * @since 08 July 2019
     */
    public int getHeightOfScreen() {
        return driver.manage().window().getSize().height;
    }
    /**
     * This method
     * @author Ramesh
     * @since 23 July 2019
     */
    /*
    public void reportLogging(String info ) {
        ExtentTest loggerReport = ExtentTestFactory.getExtentTest();
        loggerReport.info(info);
        ContextManager.getSingleReport().addStep(info, null, null, ExecutionResult.Pass);
    }
    */
    /**
     * This Function is to swipe Down
     * @author Ramesh
     */
    public void swipeDown() {
        Dimension size = driver.manage().window().getSize();
        int starty = (int) (size.height * 0.2);
        int endy = (int) (size.height * 0.8);
        int startx = (int) (size.width / 2.2);
        try {
            System.out.println("Trying to swipe up from x:" + startx + " y:" + starty + ", to x:" + startx + " y:" + endy);
            new TouchAction((PerformsTouchActions) driver).press(point(startx, starty)).waitAction(waitOptions(ofSeconds(3)))
                    .moveTo(point(startx, endy)).release().perform();
        } catch (Exception e) {
            System.out.println("Swipe did not complete succesfully.");
        }
    }
    /**
     * This Function is to check element is not present and return true or false
     *
     * @author Ramesh
     * @param: Mobile Element
     */
    public boolean isElementNotPresent(MobileElement element) {
        if (isElementPresent(element) == false) {
            return true;
        } else {
            return false;

        }
    }
    /**
     * Asserts that a condition is true. If it isn't,
     * an AssertionError is thrown.
     *
     * @author Ramesh
     * @param: Mobile Element
     */
    public void isElementPresentAssertTrue(MobileElement element) {
        try {
            Assert.assertTrue(isElementPresent(element));
        } catch (Exception e) {
            System.out.println(element + " The Element not present Assert false failed");
        }
    }

    /**
     * Asserts that a condition is true. If it isn't,
     * an AssertionError is thrown.
     *
     * @author Ramesh
     * @param: Mobile Element
     */
    public void isElementNotPresentAssertTrue(MobileElement element) {
        try {
            Assert.assertTrue(isElementNotPresent(element));
        } catch (Exception e) {
            System.out.println(element + "Element present assert true failed");
        }
    }
    /**
     * This Function is to scroll to element text
     * @author Ramesh
     * @param: Element text
     */
    public WebElement scrollToAndroidElementByText(String text) {
        return driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector())" +
                ".scrollIntoView(new UiSelector().text(\"" + text + "\"));"));
    }

    /**
     * This Function is to hide keyboard
     * @author Ramesh
     */
    public void hideKeyBoard() {
        try {
            ((AppiumDriver<MobileElement>) driver).hideKeyboard();
        } catch (Exception e) {
            System.out.println("Hide keyboard failed");
        }
    }

    /**
     * This Function is to Scroll  bottom to top And click the element
     * @author Ramesh
     * @param: Mobile Element and count of scroll
     */
    public void scrollToMobileElementAndTapElement(MobileElement element, String scrollcount) {
        try {
            waitInSec(3);
            int count = Integer.parseInt(scrollcount);
            for (int i = 0; i < count; i++) {
                if (isElementPresent(element)) {
                    element.click();
                } else {
                    swipeUp();
                }

            }
        } catch (Exception e) {
            System.out.println("Scroll to mobile element & click element failed");
        }
    }
    /**
     * This Function is to Scroll to element from top to bottom
     * @author Ramesh
     * @param: Mobile Element & String
     */
    public void scrollToMobileElementTopToBottom(MobileElement element, String scrollcount) {
        try {
            waitInSec(3);
            int count = Integer.parseInt(scrollcount);
            for (int i = 0; i < count; i++) {
                if (isElementPresent(element)) {
                } else {
                    swipeDown();
                }
            }
        } catch (Exception e) {
            System.out.println("Scroll to mobile element failed");
        }

    }
    /**
     * This Function is to Scroll to element
     * @author Ramesh
     * @param: String
     */
    public String generateTextXpathAndReturnText(String value) {
        String text = ((AppiumDriver<MobileElement>) driver).findElement(By.xpath("//*[@text='" + value + "']")).getText();
        return text;
    }
    
    /**
     * This Function is to Tap android back button
     *
     * @author Ramesh
     */
    public void tapAndroidBackButton() {
        try {
            ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        } catch (Exception e) {
            System.out.println("Android Back click failed");
        }
    }
}