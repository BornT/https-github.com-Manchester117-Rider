package com.highpin.mobile.driver;

import com.highpin.mobile.base.BaseDriverOperation;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Peng.Zhao on 2016/3/17.
 */
public class AndroidDriverOperation extends BaseDriverOperation {

    public static Logger logger = LogManager.getLogger(AndroidDriverOperation.class.getName());

    // 初始化AndroidDriver
    public static AppiumDriver initDriver(String appName, String platformName, String platformVersion, String deviceName) throws Exception {
        // 定义项目目录以及apk存放位置
        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "apps/highpin");
        File app = new File(appDir, appName);
        // 定义测试工具的连接属性
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());

        // 设置Appium可以输入中文(不依赖键盘)
        capabilities.setCapability(AndroidMobileCapabilityType.UNICODE_KEYBOARD, "True");
        capabilities.setCapability(AndroidMobileCapabilityType.RESET_KEYBOARD, "True");
        // 跳过检查和对应用进行debug签名的步骤
        capabilities.setCapability(AndroidMobileCapabilityType.NO_SIGN, "False");
        // 初始化Driver
        AppiumDriver<MobileElement> driver = null;
        try {
            driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return driver;
    }
}
