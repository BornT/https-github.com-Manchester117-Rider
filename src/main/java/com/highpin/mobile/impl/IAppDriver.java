package com.highpin.mobile.impl;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

/**
 * Created by Administrator on 2016/3/19.
 */
public interface IAppDriver {
    void click(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet);
    void input(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet);
    void clear(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet);
    void longPress(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet);
    void pinchByElement(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet);
    void pinchByPosition(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet);
    void zoomByElement(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet);
    void zoomByPosition(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet);
    void swipeToLeft(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet);
    void swipeToRight(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet);
    void swipeToUp(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet);
    void swipeToDown(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet);
    void scrollTo(AppiumDriver<MobileElement> driver, String locatorType, String locatorValue, String dataSet);
}
