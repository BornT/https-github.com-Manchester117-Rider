package com.highpin.generator.core;

import com.highpin.operatordata.ReadStruct;
import com.highpin.operatordata.TestDataExtract;
import javassist.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/18.
 */
public class ClassGenerator {
    private ClassPool cPool = null;

    private List<List<CtClass>> ctList = null;
    private List<String> suiteList = null;
    private List<List<String>> classNameList = null;

    private List<List<List<Object>>> methodNameList = null;
    private List<List<List<Object>>> methodDescriptionList = null;
    private List<List<List<Object>>> methodElementTypeList = null;
    private List<List<List<Object>>> methodLocatorTypeList = null;
    private List<List<List<Object>>> methodLocatorValueList = null;
    private List<List<List<Object>>> methodDataSetList = null;

    private List<List<List<Object>>> methodVerifyTypeList = null;
    private List<List<List<Object>>> methodVerifyTargetList = null;
    private List<List<List<Object>>> methodVerifyValueList = null;

    private List<List<List<Object>>> methodScreenCaptureList = null;

    public static Logger logger = LogManager.getLogger(ClassGenerator.class.getName());

    /**
     * @Description: 构造方法--通过对Excel的操作获取测试用例的数据结构.
     */
    public ClassGenerator() {
        this.cPool = ClassPool.getDefault();
        TestDataExtract tdExtract = new TestDataExtract();
        ReadStruct rs = new ReadStruct(tdExtract.getAllExcelData());

        this.suiteList = rs.getTestSuiteName();
        this.classNameList = rs.getAllClassName();
        this.methodNameList = rs.getSheetField("Action_Keyword");
        this.methodDescriptionList = rs.getSheetField("Description");
        this.methodElementTypeList = rs.getSheetField("Element_Type");
        this.methodLocatorTypeList = rs.getSheetField("Locator_Type");
        this.methodLocatorValueList = rs.getSheetField("Locator_Value");
        this.methodDataSetList = rs.getSheetField("Data_Set");
        this.methodVerifyTypeList = rs.getSheetField("Verify_Type");
        this.methodVerifyTargetList = rs.getSheetField("Verify_Target");
        this.methodVerifyValueList = rs.getSheetField("Verify_Value");
        this.methodScreenCaptureList = rs.getSheetField("Screen_Capture");

        logger.info("获取测试数据结构,从数据结构中取出所有测试数据,以列表形式存储");
    }

    /**
     * @Description: 按照类名称列表生成测试类
     */
    public void createTestClass() {
        String suiteName = null;
        List<CtClass> subCtClass = null;
        this.ctList = new ArrayList<>();
        for (int i = 0; i < this.suiteList.size(); ++i) {
            suiteName = this.suiteList.get(i);
            subCtClass = new ArrayList<>();
            for (String className : this.classNameList.get(i)) {
                subCtClass.add(this.cPool.makeClass("com.highpin.test." + suiteName + "." + className));
            }
            this.ctList.add(subCtClass);
        }
        logger.info("创建所有可运行的测试类");
    }

    /**
     * @Description: 向类中加入属性
     */
    public void insertField() {
        CtField ctFieldDriver = null;
        CtField ctFieldExtentReports = null;
        CtField ctFieldExtentTest = null;
        for (List<CtClass> suiteCtList : this.ctList) {
            for (CtClass ct : suiteCtList) {
                try {
                    // 加入AppiumDriver成员
                    ctFieldDriver = new CtField(this.cPool.getCtClass("io.appium.java_client.android.AndroidDriver"), "driver", ct);
                    ctFieldDriver.setModifiers(Modifier.PRIVATE);
                    // 加入ExtentReports成员
                    ctFieldExtentReports = new CtField(this.cPool.getCtClass("com.relevantcodes.extentreports.ExtentReports"), "extent", ct);
                    ctFieldExtentReports.setModifiers(Modifier.PRIVATE);
                    // 加入ExtentTest成员
                    ctFieldExtentTest = new CtField(this.cPool.getCtClass("com.relevantcodes.extentreports.ExtentTest"), "test", ct);
                    ctFieldExtentTest.setModifiers(Modifier.PRIVATE);

                    ct.addField(ctFieldDriver);
                    ct.addField(ctFieldExtentReports);
                    ct.addField(ctFieldExtentTest);
                    logger.info("向类当中添加属性");
                } catch (CannotCompileException | NotFoundException e) {
                    logger.error("添加属性失败");
                    e.printStackTrace();
                }
            }
        }
//        System.out.println(this.ctList);
    }

    public void insertMethod() {
        CtClass ctClass = null;
        for (int suiteCtIndex = 0; suiteCtIndex < this.ctList.size(); ++suiteCtIndex) {
            List<CtClass> suiteClassList = this.ctList.get(suiteCtIndex);
            for (int caseCtIndex = 0; caseCtIndex < suiteClassList.size(); ++caseCtIndex) {
                ctClass = suiteClassList.get(caseCtIndex);
                this.createMethodBody(ctClass, suiteCtIndex, caseCtIndex);
                try {
                    ctClass.writeFile("target/classes");
                } catch (IOException | CannotCompileException e) {
                    e.printStackTrace();
                }
                ctClass.defrost();
                this.insertRealTestMethod(ctClass, suiteCtIndex, caseCtIndex);
                try {
                    ctClass.writeFile("target/classes");
                } catch (IOException | CannotCompileException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void createMethodBody(CtClass caseCtClass, int suiteCtIndex, int caseCtIndex) {
        for (int methodIndex = 0; methodIndex < this.methodNameList.get(suiteCtIndex).get(caseCtIndex).size(); ++methodIndex) {
            String methodName = this.methodNameList.get(suiteCtIndex).get(caseCtIndex).get(methodIndex).toString();
            String methodBody = "public void " + methodName + "(io.appium.java_client.android.AndroidDriver driver) {}";
            CtMethod ctMethod = null;
            try {
                ctMethod = CtNewMethod.make(methodBody, caseCtClass);
                caseCtClass.addMethod(ctMethod);
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertRealTestMethod(CtClass caseCtClass, int suiteCtIndex, int caseCtIndex) {
        for (int methodIndex = 0; methodIndex < this.methodNameList.get(suiteCtIndex).get(caseCtIndex).size(); ++methodIndex) {
            String methodName = this.methodNameList.get(suiteCtIndex).get(caseCtIndex).get(methodIndex).toString();
            CtMethod ctMethod = null;
            try {
                ctMethod = caseCtClass.getDeclaredMethod(methodName);
                ctMethod.insertBefore("com.highpin.mobile.AndroidHandler.initAndroidDriver($1);");
            } catch (CannotCompileException | NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取所有要运行的类 -- ClassRunner类测试使用
     * @return -- this.ctList 成员变量this.ctList
     */
    public List<List<CtClass>> getAllClassList() {
        return this.ctList;
    }

    /**
     * @Description: 获取所有类的方法 -- ClassRunner类测试使用
     * @return -- methodAllList 返回所有的方法(用List结构返回)
     */
    public List<List<String>> getAllClassMethodList() {
        List<List<String>> methodAllList = new ArrayList<>();
        for (List<CtClass> suiteClassList : this.ctList) {
            for (CtClass ctClass : suiteClassList) {
                logger.info("打印类名称: " + ctClass.getName());
                methodAllList.add(this.getMethod(ctClass.getName()));
            }
        }
        return methodAllList;
    }

    /**
     * @Description: 获取一个类的所有方法 -- ClassRunner类测试使用
     * @param className -- 类名称
     * @return methodNameList -- 类当中所有方法(以List结构返回)
     */
    public List<String> getMethod(String className) {
        CtClass ct = null;
        List<String> methodNameList = new ArrayList<>();
        try {
            ct = this.cPool.get(className);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        CtMethod [] ctMethodList = null;
        if (ct != null) {
            ctMethodList = ct.getDeclaredMethods();
            for (CtMethod ctMethod : ctMethodList) {
                logger.info(ctMethod.getName());
                methodNameList.add(ctMethod.getName());
            }
        }
        return methodNameList;
    }

    public static void main(String[] args) {
        ClassGenerator cg = new ClassGenerator();
        cg.createTestClass();
        cg.insertField();
        cg.insertMethod();
    }
}

