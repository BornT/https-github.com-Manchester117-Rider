package com.highpin.mobile;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/3/17.
 */
public class AndroidHandler {

    public static Logger logger = LogManager.getLogger(AndroidHandler.class.getName());

    // 获取屏幕高度
    public static int getScreenHeight(AndroidDriver<AndroidElement> driver) {
        return driver.manage().window().getSize().height;
    }

    // 获取屏幕宽度
    public static int getScreenWidth(AndroidDriver<AndroidElement> driver) {
        return driver.manage().window().getSize().width;
    }

    // 初始化AndroidDriver
    public static void initAndroidDriver(AndroidDriver<AndroidElement> driver) {
        // 定义项目目录以及apk存放位置
        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "apps/highpin");
        File app = new File(appDir, "highpin_V100_91zhushou.apk");
        // 定义测试工具的连接属性
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, Platform.ANDROID);
        capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        capabilities.setCapability(AndroidMobileCapabilityType.VERSION, "4.4");
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.zhaopin.highpin");
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".page.misc.starter");
        // 设置Appium可以输入中文(不依赖键盘)
        capabilities.setCapability(AndroidMobileCapabilityType.UNICODE_KEYBOARD, "True");
        capabilities.setCapability(AndroidMobileCapabilityType.RESET_KEYBOARD, "True");

        try {
            driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void destroyAndroidDriver(AndroidDriver<AndroidElement> driver) {
        driver.quit();
    }

    // 获取屏幕元素
    public static AndroidElement getElement(AndroidDriver<AndroidElement> driver, String locatorType, String locatorValue) {
        String locatorTypeKey = locatorType.toUpperCase();
        AndroidElement element = null;
        switch (locatorTypeKey) {
            case "ID":
                element = driver.findElementById(locatorValue);
                break;
            case "XPATH":
                element = driver.findElementByXPath(locatorValue);
                break;
            case "NAME":
                element = driver.findElementByName(locatorValue);
                break;
            case "CSS":
                element = driver.findElementByCssSelector(locatorValue);
                break;
            case "CLASSNAME":
                element = driver.findElementByClassName(locatorValue);
                break;
            case "ACCESSIBILITYID":
                element = driver.findElementByAccessibilityId(locatorValue);
                break;
            case "LINKTEXT":
                element = driver.findElementByLinkText(locatorValue);
                break;
            case "PARTIALLINKTEXT":
                element = driver.findElementByPartialLinkText(locatorValue);
                break;
            case "TAGNAME":
                element = driver.findElementByTagName(locatorValue);
                break;
            default:
                // 临时日志输入
                logger.error("定位错误");
        }

        return element;
    }

    public static void click(AndroidDriver<AndroidElement> driver, String locatorType, String locatorValue, String dataSet) {
        AndroidElement element = getElement(driver, locatorType, locatorValue);
        // 需要增加点击前的准备语句
        element.click();
    }

    public static void input(AndroidDriver<AndroidElement> driver, String locatorType, String locatorValue, String dataSet) {
        AndroidElement element = getElement(driver, locatorType, locatorValue);
        // 需要增加输入前的准备语句
        element.sendKeys(dataSet);
    }

    public static void clear(AndroidDriver<AndroidElement> driver, String locatorType, String locatorValue, String dataSet) {
        AndroidElement element = getElement(driver, locatorType, locatorValue);
        // 需要增加清空前的准备语句
        element.clear();
    }

    public static void longPress(AndroidDriver<AndroidElement> driver, String locatorType, String locatorValue, String dataSet) {
        AndroidElement element = getElement(driver, locatorType, locatorValue);
        TouchAction action = new TouchAction(driver);
        action.longPress(element, Integer.parseInt(dataSet)).release().perform();
    }

    // 通过元素进行放大
    public static void pinchByElement(AndroidDriver<AndroidElement> driver, String locatorType, String locatorValue, String dataSet) {
        AndroidElement element = getElement(driver, locatorType, locatorValue);
        driver.pinch(element);
    }

    // 通过坐标进行放大
    public static void pinchByPosition(AndroidDriver<AndroidElement> driver, String locatorType, String locatorValue, String dataSet) {
        int x = getScreenHeight(driver);
        int y = getScreenWidth(driver);
        driver.pinch(x, y);
    }

    // 通过元素进行缩小
    public static void zoomByElement(AndroidDriver<AndroidElement> driver, String locatorType, String locatorValue, String dataSet) {
        AndroidElement element = getElement(driver, locatorType, locatorValue);
        driver.zoom(element);
    }

    // 通过坐标进行缩小
    public static void zoomByPosition(AndroidDriver<AndroidElement> driver, String locatorType, String locatorValue, String dataSet) {
        int x = getScreenHeight(driver);
        int y = getScreenWidth(driver);
        driver.zoom(x, y);
    }

    // 向左滑屏
    public static void swipeToLeft(AndroidDriver<AndroidElement> driver, String locatorType, String locatorValue, String dataSet) {
        int height = getScreenHeight(driver);
        int width = getScreenWidth(driver);
        driver.swipe(width * 4 / 5, height / 2, width / 20, height / 2, 500);
    }

    // 向右滑屏
    public static void swipeToRight(AndroidDriver<AndroidElement> driver, String locatorType, String locatorValue, String dataSet) {
        int height = getScreenHeight(driver);
        int width = getScreenWidth(driver);
        driver.swipe(width / 5, height / 2, width * 19 / 20, height / 2, 500);
    }

    // 向上滑动
    public static void swipeToUp(AndroidDriver<AndroidElement> driver, String locatorType, String locatorValue, String dataSet) {
        int height = getScreenHeight(driver);
        int width = getScreenWidth(driver);
        driver.swipe(width / 2, height * 4 / 5, width / 2, height / 20, 500);
    }

    // 向下滑动
    public static void swipeToDown(AndroidDriver<AndroidElement> driver, String locatorType, String locatorValue, String dataSet) {
        int height = getScreenHeight(driver);
        int width = getScreenWidth(driver);
        driver.swipe(width / 2, height / 5, width / 2, height * 19 / 20, 500);
    }

    // 滚动到特定元素
    public static void scrollTo(AndroidDriver<AndroidElement> driver, String locatorType, String locatorValue, String dataSet) {
        driver.scrollTo(dataSet);
    }
}
