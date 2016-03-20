package com.highpin.mobile.wrapper;

import com.highpin.mobile.param.ParameterObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/3/19.
 */
public class FunctionWapper {
    public static String configAndroidWrapper(ParameterObject po) {
        String time = new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date());
        String statements = "this.extent = new com.relevantcodes.extentreports.ExtentReports(\"reports/" + po.getSuiteName() + "_" + time + "/Automation Test Report-HighPin-MIK.html\", java.lang.Boolean.FALSE);" +
                            "this.extent.addSystemInfo(\"Appium Version\", \"1.4.16.1\");" +
                            "this.extent.addSystemInfo(\"Appium Java Client Version\", \"3.4\");" +
                            "this.extent.addSystemInfo(\"Selenium Server Version\", \"2.52.0\");" +
                            "this.extent.addSystemInfo(\"Environment\", \"QA\");" +
                            "this.test = this.extent.startTest(\"" + po.getClassName() + "\", \"HighPin Automation Test\");" +
                            "try {" +
                                // 调用真实操作方法
                                "this.capabilities = com.highpin.mobile.driver.AndroidDriverOperation.configAndroid();" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.PASS, \"" + po.getDescription() + " --->> " + po.getDataSet() + "\");" +
                            "} catch (java.lang.Exception e) {" +
                                "e.printStackTrace();" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.FAIL, \"" + po.getDescription() + " --->> " + po.getDataSet() + "\" + \":  \" + e.getMessage());" +
                            "}";
        return statements;

    }

    public static String initAndroidWrapper(ParameterObject po) {
        String time = new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date());
        String statements = "try {" +
                                // 调用真实操作方法
                                "this.driver = com.highpin.mobile.driver.AndroidDriverOperation.initAndroidDriver(this.capabilities);" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.PASS, \"" + po.getDescription() + " --->> " + po.getDataSet() + "\");" +
                            "} catch (java.lang.Exception e) {" +
                                "e.printStackTrace();" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.FAIL, \"" + po.getDescription() + " --->> " + po.getDataSet() + "\" + \":  \" + e.getMessage());" +
                            "} finally {" +
                                "if (\"Yes\".equalsIgnoreCase(\"" + po.getScreenCapture() + "\")) {" +
                                    "java.lang.String imgPath = com.highpin.tools.Utility.captureScreenShot(this.driver, \"" + po.getSuiteName() + "_" + time + "\", \"" + po.getClassName() + "_" + po.getDescription() + "\");" +
                                    "this.test.log(com.relevantcodes.extentreports.LogStatus.INFO, \"截图 -- " + po.getDescription() + ": \" + this.test.addScreenCapture(imgPath));" +
                                "}" +
                            "}";
        return statements;
    }

    public static String destroyAndroidWrapper(ParameterObject po) {
        String statements = "try {" +
                                "com.highpin.mobile.driver.AndroidDriverOperation.destroyAndroidDriver(this.driver);" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.PASS, \"" + po.getDescription() + "\");" +
                            "} catch (java.lang.Exception e) {" +
                                "e.printStackTrace();" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.FAIL, \"" + po.getDescription() + "\" + \":  \" + e.getMessage());" +
                            "} finally {" +
                                "this.extent.endTest(this.test);" +
                                "this.extent.flush();" +
                                "this.extent.close();" +
                            "}";
        return statements;
    }

    public static String operationWrapper(ParameterObject po) {
        String realFun = null;
        if (po.getActionType().equalsIgnoreCase("wait")) {
            realFun = "com.highpin.mobile.driver.AndroidDriverOperation.waitDriver(\"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("click")) {
            realFun = "com.highpin.mobile.driver.AndroidDriverOperation.click(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("input")) {
            realFun = "com.highpin.mobile.driver.AndroidDriverOperation.input(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("clear")) {
            realFun = "com.highpin.mobile.driver.AndroidDriverOperation.clear(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("swipeToLeft")) {
            realFun = "com.highpin.mobile.driver.AndroidDriverOperation.swipeToLeft(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("swipeToRight")) {
            realFun = "com.highpin.mobile.driver.AndroidDriverOperation.swipeToRight(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("swipeToUp")) {
            realFun = "com.highpin.mobile.driver.AndroidDriverOperation.swipeToUp(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("swipeToDown")) {
            realFun = "com.highpin.mobile.driver.AndroidDriverOperation.swipeToDown(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("longPress")) {
            realFun = "com.highpin.mobile.driver.AndroidDriverOperation.longPress(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("scrollTo")) {
            realFun = "com.highpin.mobile.driver.AndroidDriverOperation.scrollTo(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("pinchByElement")) {
            realFun = "com.highpin.mobile.driver.AndroidDriverOperation.pinchByElement(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("pinchByPosition")) {
            realFun = "com.highpin.mobile.driver.AndroidDriverOperation.pinchByPosition(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("zoomByElement")) {
            realFun = "com.highpin.mobile.driver.AndroidDriverOperation.zoomByElement(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("zoomByPosition")) {
            realFun = "com.highpin.mobile.driver.AndroidDriverOperation.zoomByPosition(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else {
            System.out.println("不正确的操作类型");
        }
        String time = new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date());
        String statements = "try {" +
                                realFun +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.PASS, \"" + po.getDescription() + " --->> " + po.getLocValue() + "\");" +
                            "} catch (java.lang.Exception e) {" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.FAIL, \"" + po.getDescription() + " --->> " + po.getLocValue() + "\" + \":  \" + e.getMessage());" +
                            "} finally {" +
                                "if (\"Yes\".equalsIgnoreCase(\"" + po.getScreenCapture() + "\")) {" +
                                    "java.lang.String imgPath = com.highpin.tools.Utility.captureScreenShot(this.driver, \"" + po.getSuiteName() + "_" + time + "\", \"" + po.getClassName() + "_" + po.getDescription() + "\");" +
                                    "this.test.log(com.relevantcodes.extentreports.LogStatus.INFO, \"截图 -- " + po.getDescription() + ": \" + this.test.addScreenCapture(imgPath));" +
                                "}" +
                            "}";
//        System.out.println(statements);
        return statements;
    }
}
