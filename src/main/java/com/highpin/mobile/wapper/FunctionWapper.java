package com.highpin.mobile.wapper;

import com.highpin.mobile.param.ParameterObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/3/19.
 */
public class FunctionWapper {
    public static String initAndroidWapper(ParameterObject po) {
        String time = new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date());
        String statements = "this.extent = new com.relevantcodes.extentreports.ExtentReports(\"reports/" + po.getSuiteName() + "_" + time + "/Automation Test Report-HighPin-MIK.html\", java.lang.Boolean.FALSE);" +
                            "this.extent.addSystemInfo(\"Appium Version\", \"1.4.16.1\");" +
                            "this.extent.addSystemInfo(\"Appium Java Client Version\", \"3.4\");" +
                            "this.extent.addSystemInfo(\"Selenium Server Version\", \"2.52.0\");" +
                            "this.extent.addSystemInfo(\"Environment\", \"QA\");" +
                            "this.test = this.extent.startTest(\"" + po.getClassName() + "\", \"HighPin Automation Test\");" +
                            "try {" +
                                // 调用真实操作方法
                                "this.driver = com.highpin.mobile.app.AndroidDriverOperation.initAndroidDriver();" +
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

    public static String destroyAndroidWapper(ParameterObject po) {
        String statements = "try {" +
                                "com.highpin.mobile.app.AndroidDriverOperation.destroyAndroidDriver(this.driver);" +
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


}
