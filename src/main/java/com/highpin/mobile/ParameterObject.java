package com.highpin.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

/**
 * Created by Administrator on 2016/3/17.
 */
public class ParameterObject {
    private AndroidDriver<AndroidElement> driver = null;
    private String locatorType = null;
    private String locatorValue = null;

    public AndroidDriver<AndroidElement> getDriver() {
        return driver;
    }

    public void setDriver(AndroidDriver<AndroidElement> driver) {
        this.driver = driver;
    }

    public String getLocatorType() {
        return locatorType;
    }

    public void setLocatorType(String locatorType) {
        this.locatorType = locatorType;
    }

    public String getLocatorValue() {
        return locatorValue;
    }

    public void setLocatorValue(String locatorValue) {
        this.locatorValue = locatorValue;
    }
}
