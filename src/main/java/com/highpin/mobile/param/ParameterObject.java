package com.highpin.mobile.param;

import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 */
public class ParameterObject {
    /**
     * @Description: POJO类 -- 用于封装方法参数
     */
    private String suiteName;
    private String className;
    private String methodName;
    private String actionType;
    private String locType;
    private String locValue;
    private String dataSet;
    private String description;
    private List<?> verifyType;     // 使用无限制的通配符
    private List<?> verifyTarget;   // 使用无限制的通配符
    private List<?> verifyValue;    // 使用无限制的通配符
    private String screenCapture;

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getLocType() {
        return locType;
    }

    public void setLocType(String locType) {
        this.locType = locType;
    }

    public String getLocValue() {
        return locValue;
    }

    public void setLocValue(String locValue) {
        this.locValue = locValue;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<?> getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(Object verifyType) {
        if (verifyType instanceof List<?>) {
            this.verifyType = (List<?>)verifyType;
        }
    }

    public List<?> getVerifyTarget() {
        return verifyTarget;
    }

    public void setVerifyTarget(Object verifyTarget) {
        if (verifyTarget instanceof List<?>) {
            this.verifyTarget = (List<?>)verifyTarget;
        }
    }

    public List<?> getVerifyValue() {
        return verifyValue;
    }

    public void setVerifyValue(Object verifyValue) {
        if (verifyValue instanceof List<?>) {
            this.verifyValue = (List<?>)verifyValue;
        }
    }

    public String getScreenCapture() {
        return screenCapture;
    }

    public void setScreenCapture(String screenCapture) {
        this.screenCapture = screenCapture;
    }
}
