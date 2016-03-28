package com.highpin.check;

import com.highpin.mobile.param.ParameterObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Administrator on 2016/3/28.
 */
public class VerifyModule {
    public static Logger logger = LogManager.getLogger(VerifyModule.class.getName());

    public static String returnVerifyContentStatement(String verifyType, String verifyTarget) {
        String verStatements = "org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait((org.openqa.selenium.WebDriver)this.driver, 15L);";
        if (verifyType.equalsIgnoreCase("xpath")) {
            verStatements += "element = this.driver.findElementByXPath(\"" + verifyTarget + "\");";
        } else if (verifyType.equalsIgnoreCase("id")) {
            verStatements += "element = this.driver.findElementById(\"" + verifyTarget + "\");";
        } else {
            logger.info("不正确的验证类型");
            return verStatements;
        }
        verStatements += "wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(element));";
        verStatements += "targetText = element.getText();";
        return verStatements;
    }

    /**
     * @Description: 向方法中加入验证语句,当前是对文本进行验证
     * @param verifyType    --  验证类型
     * @param verifyTarget  --  验证路径
     * @param verifyValue   --  验证值
     * @return  -- 返回验证语句
     * @throws Exception    --  如果验证类型/验证路径不正确则抛出NotFoundLocatorException
     */
    public static String createVerifyContentStatement(String verifyType, String verifyTarget, String verifyValue) {
        String verStatements = "";
        if (!verifyType.isEmpty() && !verifyTarget.isEmpty() && !verifyValue.isEmpty()) {
            verStatements = "try {" +
                                "io.appium.java_client.MobileElement element = null;" +
                                "java.lang.String targetText = null;" +
                                VerifyModule.returnVerifyContentStatement(verifyType, verifyTarget) +
                                "if (targetText.contains(\"" + verifyValue + "\")) {" +
                                    "this.test.log(com.relevantcodes.extentreports.LogStatus.PASS, \"" + verifyValue + "\" + \" ---- 文本验证:存在\");" +
                                "} else {" +
                                    "this.test.log(com.relevantcodes.extentreports.LogStatus.FAIL, \"" + verifyValue + "\" + \" ---- 文本验证:不存在\");" +
                                "}" +
                            "} catch (java.lang.Exception e) {" +
                                "this.test.log(com.relevantcodes.extentreports.LogStatus.FAIL, \"" + verifyTarget + "\" + \" ---- 未找到: \" + e.getMessage());" +
                            "}";
        }
        return verStatements;
    }

    /**
     * @Description: 将多个验证语句进行拼接
     * @param po       --       测试数据结构
     * @return verifyStatement  --  返回拼接的验证语句
     * @throws Exception 如果验证类型/验证路径不正确则抛出NotFoundLocatorException
     */
    public static String appendVerifyContentStatement(ParameterObject po) {
        String verifyTypeString = null;
        String verifyTargetString = null;
        String verifyValueString = null;
        // 获取验证语句
        String verStatements = "";
        // 验证点遍历
        for (int v = 0; v < po.getVerifyValue().size(); ++v) {
            verifyTypeString = po.getVerifyType().get(v).toString();
            verifyTargetString = po.getVerifyTarget().get(v).toString();
            verifyValueString = po.getVerifyValue().get(v).toString();
//            verStatements += "java.lang.Thread.sleep(100);";
            verStatements += VerifyModule.createVerifyContentStatement(verifyTypeString, verifyTargetString, verifyValueString);
        }
        logger.info("添加当前步骤的全部测试验证");
        return verStatements;
    }
}
