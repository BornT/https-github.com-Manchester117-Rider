package com.rider.check;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by Peng.Zhao on 2016/3/28.
 */
public class VerifyModule {
    public static Logger logger = LogManager.getLogger(VerifyModule.class.getName());

    /**
     * @Description: 将多个验证语句进行拼接
     * @param driver       --       AppiumDriver
     * @return verifyStatement  --  返回拼接的验证语句
     * @throws Exception 如果验证类型/验证路径不正确则抛出NotFoundLocatorException
     */
    public static boolean appendVerifyContentStatement(AppiumDriver<MobileElement> driver, String verifyType, String verifyTarget, String verifyValue) {
        WebDriverWait wait = new WebDriverWait(driver, 15L);
        MobileElement element = null;
        if (verifyType.equalsIgnoreCase("id")) {
            element = driver.findElementById(verifyTarget);
            wait.until(ExpectedConditions.visibilityOf(element));
        } else if (verifyType.equalsIgnoreCase("xpath")) {
            element = driver.findElementByXPath(verifyTarget);
            wait.until(ExpectedConditions.visibilityOf(element));
        } else {
            logger.error("不正确的验证类型");
        }
        String targetText = null;
        if (element != null) {
            targetText = element.getText();
        }
        return verifyValue.equals(targetText);
    }
}
