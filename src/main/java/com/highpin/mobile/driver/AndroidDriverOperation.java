package com.highpin.mobile.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
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
public class AndroidDriverOperation {

    public static Logger logger = LogManager.getLogger(AndroidDriverOperation.class.getName());

    // 初始化AndroidDriver
    public static DesiredCapabilities configAndroid() throws Exception {
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
        return capabilities;
    }

    public static AndroidDriver initAndroidDriver(DesiredCapabilities capabilities) throws Exception {
        AndroidDriver driver = null;
        try {
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return driver;
    }

    public static void destroyAndroidDriver(AndroidDriver<AndroidElement> driver) throws Exception{
        driver.quit();
    }

    // 获取屏幕高度
    public static int getScreenHeight(AppiumDriver<MobileElement> driver) {
        return driver.manage().window().getSize().height;
    }

    // 获取屏幕宽度
    public static int getScreenWidth(AppiumDriver<MobileElement> driver) {
        return driver.manage().window().getSize().width;
    }

    // 获取屏幕元素
    public static MobileElement getElement(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue) throws Exception {
        String locatorTypeKey = locatorType.toUpperCase();
        MobileElement element = null;
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

    public static void click(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet) {
        MobileElement element = null;
        try {
            element = getElement(driver, locatorType, locatorValue);
            // 需要增加点击前的准备语句
            element.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void input(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet) {
        MobileElement element = null;
        try {
            element = getElement(driver, locatorType, locatorValue);
            // 需要增加输入前的准备语句
            element.sendKeys(dataSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clear(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet) {
        MobileElement element = null;
        try {
            element = getElement(driver, locatorType, locatorValue);
            // 需要增加清空前的准备语句
            element.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void longPress(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet) {
        MobileElement element = null;
        try {
            element = getElement(driver, locatorType, locatorValue);
            TouchAction action = new TouchAction(driver);
            action.longPress(element, Integer.parseInt(dataSet)).release().perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 通过元素进行放大
    public static void pinchByElement(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet) {
        MobileElement element = null;
        try {
            element = getElement(driver, locatorType, locatorValue);
            driver.pinch(element);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 通过坐标进行放大
    public static void pinchByPosition(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet) {
        try {
            int x = getScreenHeight(driver);
            int y = getScreenWidth(driver);
            driver.pinch(x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 通过元素进行缩小
    public static void zoomByElement(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet) {
        MobileElement element = null;
        try {
            element = getElement(driver, locatorType, locatorValue);
            driver.zoom(element);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 通过坐标进行缩小
    public static void zoomByPosition(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet) {
        try {
            int x = getScreenHeight(driver);
            int y = getScreenWidth(driver);
            driver.zoom(x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 向左滑屏
    public static void swipeToLeft(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet) {
        try {
            int height = getScreenHeight(driver);
            int width = getScreenWidth(driver);
            driver.swipe(width * 4 / 5, height / 2, width / 20, height / 2, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 向右滑屏
    public static void swipeToRight(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet) {
        try {
            int height = getScreenHeight(driver);
            int width = getScreenWidth(driver);
            driver.swipe(width / 5, height / 2, width * 19 / 20, height / 2, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 向上滑动
    public static void swipeToUp(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet) {
        try {
            int height = getScreenHeight(driver);
            int width = getScreenWidth(driver);
            driver.swipe(width / 2, height * 4 / 5, width / 2, height / 20, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 向下滑动
    public static void swipeToDown(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet) {
        try {
            int height = getScreenHeight(driver);
            int width = getScreenWidth(driver);
            driver.swipe(width / 2, height / 5, width / 2, height * 19 / 20, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 滚动到特定元素
    public static void scrollTo(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet) {
        try {
            driver.scrollTo(dataSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
