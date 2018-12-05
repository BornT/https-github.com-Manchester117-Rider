package com.rider.mobile.wrapper;

import com.rider.mobile.param.ParameterObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Peng.Zhao on 2016/3/19.
 */
public class FunctionWrapper {
    public static String initAndroidWrapper(ParameterObject po, String appName, String platformName, String platformVersion, String deviceName) {
        String time = new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date());
        String statements = "this.extent = new com.relevantcodes.extentreports.ExtentReports(\"reports/" + po.getSuiteName() + "_" + time + "/Automation Test Report-Rider-MIK.html\", java.lang.Boolean.FALSE);" +
                            "this.extent.addSystemInfo(\"Appium Version\", \"1.5.3\");" +
                            "this.extent.addSystemInfo(\"Appium Java Client Version\", \"4.1.2\");" +
                            "this.extent.addSystemInfo(\"Selenium Server Version\", \"2.53.1\");" +
                            "this.extent.addSystemInfo(\"Environment\", \"QA\");" +
                            "this.test = this.extent.startTest(\"" + po.getClassName() + "\", \"Rider Automation Test\");" +
                            "try {" +
                                // 调用真实操作方法
                                "this.driver = com.rider.mobile.driver.AndroidDriverOperation.initDriver(\"" + appName + "\", \"" + platformName + "\", \"" + platformVersion + "\", \"" + deviceName + "\");" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.PASS, \"" + po.getDescription() + "\");" +
                            "} catch (java.lang.Exception e) {" +
                                "e.printStackTrace();" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.FAIL, \"" + po.getDescription() + " --->> 出现异常\" + \": \" + e.getMessage());" +
                                "org.testng.Assert.fail(\"AndroidDriver初始化异常\");" +
                            "}";
        return statements;
    }

    public static String initIOSWrapper(ParameterObject po, String appName, String platformName, String platformVersion, String deviceName) {
        String time = new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date());
        String statements = "this.extent = new com.relevantcodes.extentreports.ExtentReports(\"reports/" + po.getSuiteName() + "_" + time + "/Automation Test Report-Rider-MIK.html\", java.lang.Boolean.FALSE);" +
                            "this.extent.addSystemInfo(\"Appium Version\", \"1.4.16.1\");" +
                            "this.extent.addSystemInfo(\"Appium Java Client Version\", \"3.4\");" +
                            "this.extent.addSystemInfo(\"Selenium Server Version\", \"2.52.0\");" +
                            "this.extent.addSystemInfo(\"Environment\", \"QA\");" +
                            "this.test = this.extent.startTest(\"" + po.getClassName() + "\", \"Rider Automation Test\");" +
                            "try {" +
                                // 调用真实操作方法
                                "this.driver = com.rider.mobile.driver.IOSDriverOperation.initDriver(\"" + appName + "\", \"" + platformName + "\", \"" + platformVersion + "\", \"" + deviceName + "\");" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.PASS, \"" + po.getDescription() + "\");" +
                            "} catch (java.lang.Exception e) {" +
                                "e.printStackTrace();" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.FAIL, \"" + po.getDescription() + " --->> 出现异常\" + \": \" + e.getMessage());" +
                                "org.testng.Assert.fail(\"IOSDriver初始化异常\");" +
                            "}";
        return statements;
    }

    public static String destroyAndroidWrapper(ParameterObject po) {
        String statements = "try {" +
                                // 调用真实操作方法
                                "com.rider.mobile.driver.AndroidDriverOperation.destroyDriver(this.driver);" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.PASS, \"" + po.getDescription() + "\");" +
                            "} catch (java.lang.Exception e) {" +
                                "e.printStackTrace();" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.FAIL, \"" + po.getDescription() + "\" + \":  \" + e.getMessage());" +
                                "org.testng.Assert.fail(\"AndroidDriver关闭异常\");" +
                            "} finally {" +
                                "this.extent.endTest(this.test);" +
                                "this.extent.flush();" +
                                "this.extent.close();" +
                            "}";
        return statements;
    }

    public static String destroyIOSWrapper(ParameterObject po) {
        String statements = "try {" +
                                // 调用真实操作方法
                                "com.rider.mobile.driver.IOSDriverOperation.destroyDriver(this.driver);" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.PASS, \"" + po.getDescription() + "\");" +
                            "} catch (java.lang.Exception e) {" +
                                "e.printStackTrace();" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.FAIL, \"" + po.getDescription() + "\" + \":  \" + e.getMessage());" +
                                "org.testng.Assert.fail(\"IOSDriver关闭异常\");" +
                            "} finally {" +
                                "this.extent.endTest(this.test);" +
                                "this.extent.flush();" +
                                "this.extent.close();" +
                            "}";
        return statements;
    }

    public static String standByAppWrapper(ParameterObject po) {
        String time = new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date());
        String statements = "try {" +
                                // 调用真实操作方法
                                "com.rider.mobile.base.BaseDriverOperation.standByApp(this.driver, \"" + po.getLocType() + "\", \"" + po.getLocValue() + "\");" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.PASS, \"" + po.getDescription() + "\");" +
                            "} catch (java.lang.Exception e) {" +
                                "e.printStackTrace();" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.FAIL, \"" + po.getDescription() + " --->> 出现异常\" + \":  \" + e.getMessage());" +
                                "org.testng.Assert.fail(\"APP启动异常\");" +
                            "} finally {" +
                                "if (\"Yes\".equalsIgnoreCase(\"" + po.getScreenCapture() + "\")) {" +
                                    "java.lang.String imgPath = com.rider.tools.Utility.captureScreenShot(this.driver, \"" + po.getSuiteName() + "_" + time + "\", \"" + po.getSuiteName() + "_" + po.getClassName() + "_" + po.getDescription() + "\");" +
                                    "this.test.log(com.relevantcodes.extentreports.LogStatus.INFO, \"截图 -- " + po.getDescription() + ": \" + this.test.addScreenCapture(imgPath));" +
                                "}" +
                            "}";
        System.out.println(statements);
        return statements;
    }

    public static String waitAction(ParameterObject po) {
        String realFun = null;
        String verifyStatement = null;
        String time = new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date());
        if (po.getMethodName().startsWith("wait")) {
            realFun = "boolean flag = false;" +
                      "com.rider.mobile.base.BaseDriverOperation.waitAction(\"" + po.getDataSet() + "\");";
            verifyStatement = FunctionWrapper.appendVerify(po);
            realFun += verifyStatement;
            realFun += "if (\"Yes\".equalsIgnoreCase(\"" + po.getScreenCapture() + "\")) {" +
                            "java.lang.String imgPath = com.rider.tools.Utility.captureScreenShot(this.driver, \"" + po.getSuiteName() + "_" + time + "\", \"" + po.getSuiteName() + "_" + po.getClassName() + "_" + po.getDescription() + "\");" +
                            "this.test.log(com.relevantcodes.extentreports.LogStatus.INFO, \"截图 -- " + po.getDescription() + ": \" + this.test.addScreenCapture(imgPath));" +
                       "}";
        }
        return realFun;
    }

    public static String operationWrapper(ParameterObject po) {
        String realFun = null;
        if (po.getActionType().equalsIgnoreCase("click")) {
            realFun = "com.rider.mobile.base.BaseDriverOperation.click(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("input")) {
            realFun = "com.rider.mobile.base.BaseDriverOperation.input(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("clear")) {
            realFun = "com.rider.mobile.base.BaseDriverOperation.clear(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("swipeToLeft")) {
            realFun = "com.rider.mobile.base.BaseDriverOperation.swipeToLeft(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("swipeToRight")) {
            realFun = "com.rider.mobile.base.BaseDriverOperation.swipeToRight(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("swipeToUp")) {
            realFun = "com.rider.mobile.base.BaseDriverOperation.swipeToUp(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("swipeToDown")) {
            realFun = "com.rider.mobile.base.BaseDriverOperation.swipeToDown(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("longPress")) {
            realFun = "com.rider.mobile.base.BaseDriverOperation.longPress(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("pinchByElement")) {
            realFun = "com.rider.mobile.base.BaseDriverOperation.pinchByElement(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("pinchByPosition")) {
            realFun = "com.rider.mobile.base.BaseDriverOperation.pinchByPosition(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("zoomByElement")) {
            realFun = "com.rider.mobile.base.BaseDriverOperation.zoomByElement(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else if (po.getActionType().equalsIgnoreCase("zoomByPosition")) {
            realFun = "com.rider.mobile.base.BaseDriverOperation.zoomByPosition(this.driver, \"" + po.getLocType()+ "\", \"" + po.getLocValue() + "\", \"" + po.getDataSet() + "\");";
        } else {
            System.out.println("不正确的操作类型");
        }

        String time = new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date());
        String statements = "try {" +
                                realFun +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.PASS, \"" + po.getDescription() + " --->> " + po.getLocValue() + "\");" +
                                "boolean flag = false;" +
                                FunctionWrapper.appendVerify(po) +
                            "} catch (java.lang.Exception e) {" +
                                "e.printStackTrace();" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.FAIL, \"" + po.getDescription() + " --->> " + po.getLocValue() + "\" + \":  \" + e.getMessage());" +
                                "org.testng.Assert.fail(\"元素查找超时导致流程中断\");" +
                            "} finally {" +
                                "try {" +
                                    "java.lang.Thread.sleep(250L);" +
                                "} catch (java.lang.InterruptedException e){" +
                                    "e.printStackTrace();" +
                                "}" +
                                "if (\"Yes\".equalsIgnoreCase(\"" + po.getScreenCapture() + "\")) {" +
                                    "java.lang.String imgPath = com.rider.tools.Utility.captureScreenShot(this.driver, \"" + po.getSuiteName() + "_" + time + "\", \"" + po.getSuiteName() + "_" + po.getClassName() + "_" + po.getDescription() + "\");" +
                                    "this.test.log(com.relevantcodes.extentreports.LogStatus.INFO, \"截图 -- " + po.getDescription() + ": \" + this.test.addScreenCapture(imgPath));" +
                                "}" +
                            "}";
//        System.out.println(statements);
        return statements;
    }

    private static String appendVerify(ParameterObject po) {
        List<?> verifyTypeList = po.getVerifyType();
        List<?> verifyTargetList = po.getVerifyTarget();
        List<?> verifyValueList = po.getVerifyValue();

        String verifyFun = "";
        if (!verifyTypeList.isEmpty() && !verifyTargetList.isEmpty() && !verifyValueList.isEmpty()) {
            if (!verifyTypeList.get(0).equals("") && !verifyTargetList.get(0).equals("") && !verifyValueList.get(0).equals("")) {
                for (int i = 0; i < verifyTypeList.size(); ++i) {
                    String vTypeStr = verifyTypeList.get(i).toString();
                    String vTargetStr = verifyTargetList.get(i).toString();
                    String vValueStr = verifyValueList.get(i).toString();
                    verifyFun += "flag = com.rider.check.VerifyModule.appendVerifyContentStatement(this.driver, \"" + vTypeStr + "\", \"" + vTargetStr + "\", \"" + vValueStr + "\");" +
                                 "if (flag) {" +
                                    "this.test.log(com.relevantcodes.extentreports.LogStatus.PASS, \"" + vValueStr + "\" + \" ---- 文本验证:存在\");" +
                                 "} else {" +
                                    "this.test.log(com.relevantcodes.extentreports.LogStatus.FAIL, \"" + vValueStr + "\" + \" ---- 文本验证:不存在\");" +
                                 "}";
                }
            }
        }
        return verifyFun;
    }
}
