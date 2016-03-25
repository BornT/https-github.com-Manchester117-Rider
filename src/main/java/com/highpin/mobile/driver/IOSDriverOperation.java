package com.highpin.mobile.driver;

import com.highpin.mobile.base.BaseDriverOperation;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Administrator on 2016/3/17.
 */
public class IOSDriverOperation extends BaseDriverOperation {
    public static Logger logger = LogManager.getLogger(IOSDriverOperation.class.getName());

    public static AppiumDriver initDriver() throws Exception {
        // 定义项目目录以及apk存放位置
        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "apps/highpin");
        File app = new File(appDir, "highpin_V100_91zhushou.apk");
        // 定义测试工具的连接属性
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "ios");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "8.1");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone");
        capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());

        // 初始化Driver
        AppiumDriver<MobileElement> driver = null;
        try {
            driver = new IOSDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return driver;
    }
}
