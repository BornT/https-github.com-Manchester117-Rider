package com.highpin.generator.core;

import com.highpin.mobile.param.ParameterObject;
import com.highpin.mobile.wrapper.FunctionWapper;
import com.highpin.operatordata.ReadInitStruct;
import com.highpin.operatordata.ReadTestStruct;
import com.highpin.operatordata.TestDataExtract;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/18.
 */
public class ClassGenerator {
    private ClassPool cPool = null;

    private List<List<CtClass>> ctList = null;
    private List<String> suiteList = null;
    private List<List<String>> classNameList = null;
    private List<List<Map<String, Object>>> initList = null;

    private List<List<List<Object>>> methodNameList = null;
    private List<List<List<Object>>> methodDescriptionList = null;
    private List<List<List<Object>>> methodActionTypeList = null;
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
        TestDataExtract tde = new TestDataExtract();
        tde.createAllExcelTestData();
        ReadTestStruct readTestStruct = new ReadTestStruct(tde.getAllExcelTestData());
        ReadInitStruct readInitStruct = new ReadInitStruct(tde.getAllExcelInitData());

        this.initList = readInitStruct.getInitField();

        this.suiteList = readTestStruct.getTestSuiteName();
        this.classNameList = readTestStruct.getAllClassName();
        this.methodNameList = readTestStruct.getSheetField("Action_Keyword");
        this.methodDescriptionList = readTestStruct.getSheetField("Description");
        this.methodActionTypeList = readTestStruct.getSheetField("Action_Type");
        this.methodLocatorTypeList = readTestStruct.getSheetField("Locator_Type");
        this.methodLocatorValueList = readTestStruct.getSheetField("Locator_Value");
        this.methodDataSetList = readTestStruct.getSheetField("Data_Set");
        this.methodVerifyTypeList = readTestStruct.getSheetField("Verify_Type");
        this.methodVerifyTargetList = readTestStruct.getSheetField("Verify_Target");
        this.methodVerifyValueList = readTestStruct.getSheetField("Verify_Value");
        this.methodScreenCaptureList = readTestStruct.getSheetField("Screen_Capture");

        logger.info("获取测试数据结构,从数据结构中取出所有测试数据,以列表形式存储.");
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
                    ctFieldDriver = new CtField(this.cPool.getCtClass("io.appium.java_client.AppiumDriver"), "driver", ct);
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
//        logger.info(this.ctList);
    }

    public void insertMethod() {
        CtClass ctClass = null;
        for (int suiteCtIndex = 0; suiteCtIndex < this.ctList.size(); ++suiteCtIndex) {
            List<CtClass> suiteClassList = this.ctList.get(suiteCtIndex);
            for (int caseCtIndex = 0; caseCtIndex < suiteClassList.size(); ++caseCtIndex) {
                // 获取当前遍历到的类
                ctClass = suiteClassList.get(caseCtIndex);
                // 向类中插入框架方法
                this.createMethodBody(ctClass, suiteCtIndex, caseCtIndex);
                try {
                    // 写出字节码到指定目录
                    ctClass.writeFile("target/classes");
                } catch (IOException | CannotCompileException e) {
                    e.printStackTrace();
                }
                // 解除类冻结
                ctClass.defrost();
                // 向框架方法中插入真实执行代码
                this.insertRealTestMethod(ctClass, suiteCtIndex, caseCtIndex);
                try {
                    // 再次写出字节码到指定目录
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
            String methodBody = "public void " + methodName + "() {}";
            CtMethod ctMethod = null;
            try {
                ctMethod = CtNewMethod.make(methodBody, caseCtClass);
                caseCtClass.addMethod(ctMethod);
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }

            // 给测试方法添加注解
            if (methodName.startsWith("init")) {
                this.addAnnotation(caseCtClass, ctMethod, "org.testng.annotations.BeforeClass", "alwaysRun", true);
            } else if (methodName.startsWith("standBy")) {
                this.addAnnotation(caseCtClass, ctMethod, "org.testng.annotations.Test", "enabled", true);
            } else if (methodName.startsWith("destroy")) {
                this.addAnnotation(caseCtClass, ctMethod, "org.testng.annotations.AfterClass", "alwaysRun", true);
            } else {
                String annotValue = this.methodNameList.get(suiteCtIndex).get(caseCtIndex).get(methodIndex - 1).toString();
                this.addAnnotation(caseCtClass, ctMethod, "org.testng.annotations.Test", "dependsOnMethods", annotValue);
            }
        }
    }

    private void addAnnotation(CtClass ctClass, CtMethod ctMethod, String annotTitle, String annotKey, Object annotValue) {
        // 给方法添加Annotation
        ClassFile classFile = ctClass.getClassFile();
        ConstPool cPool = classFile.getConstPool();
        String methodName = ctMethod.getName();
        AnnotationsAttribute attr = new AnnotationsAttribute(cPool, AnnotationsAttribute.visibleTag);
        Annotation annotation = new Annotation(annotTitle, cPool);
        if (methodName.startsWith("init") || methodName.startsWith("destroy") || methodName.startsWith("standBy")) {
            // 如果Annotation的属性值是boolean类型
            annotation.addMemberValue(annotKey, new BooleanMemberValue((boolean) annotValue, cPool));
        } else {
            // dependsOnMethod注解值必须是数组,所以使用ArrayMemberValue
            ArrayMemberValue arrMemberValue = new ArrayMemberValue(cPool);
            StringMemberValue[] strMemberValues = new StringMemberValue[]{new StringMemberValue(annotValue.toString(), cPool)};
            arrMemberValue.setValue(strMemberValues);
            annotation.addMemberValue(annotKey, arrMemberValue);
        }
        attr.addAnnotation(annotation);
        ctMethod.getMethodInfo().addAttribute(attr);
    }

    public void insertRealTestMethod(CtClass caseCtClass, int suiteCtIndex, int caseCtIndex) {
        ParameterObject po = new ParameterObject();
        for (int methodIndex = 0; methodIndex < this.methodNameList.get(suiteCtIndex).get(caseCtIndex).size(); ++methodIndex) {
            po.setSuiteName(this.suiteList.get(suiteCtIndex));
            po.setClassName(this.classNameList.get(suiteCtIndex).get(caseCtIndex));
            po.setMethodName(this.methodNameList.get(suiteCtIndex).get(caseCtIndex).get(methodIndex).toString());
            po.setActionType(this.methodActionTypeList.get(suiteCtIndex).get(caseCtIndex).get(methodIndex).toString());
            po.setLocType(this.methodLocatorTypeList.get(suiteCtIndex).get(caseCtIndex).get(methodIndex).toString());
            po.setLocValue(this.methodLocatorValueList.get(suiteCtIndex).get(caseCtIndex).get(methodIndex).toString());
            po.setDataSet(this.methodDataSetList.get(suiteCtIndex).get(caseCtIndex).get(methodIndex).toString());
            po.setDescription(this.methodDescriptionList.get(suiteCtIndex).get(caseCtIndex).get(methodIndex).toString());
            po.setVerifyType(this.methodVerifyTypeList.get(suiteCtIndex).get(caseCtIndex).get(methodIndex));
            po.setVerifyTarget(this.methodVerifyTargetList.get(suiteCtIndex).get(caseCtIndex).get(methodIndex));
            po.setVerifyValue(this.methodVerifyValueList.get(suiteCtIndex).get(caseCtIndex).get(methodIndex));
            po.setScreenCapture(this.methodScreenCaptureList.get(suiteCtIndex).get(caseCtIndex).get(methodIndex).toString());

            String platformName = this.initList.get(suiteCtIndex).get(caseCtIndex).get("platformName").toString();
            String platformVersion = this.initList.get(suiteCtIndex).get(caseCtIndex).get("platformVersion").toString();
            String deviceName = this.initList.get(suiteCtIndex).get(caseCtIndex).get("deviceName").toString();
            String appName = this.initList.get(suiteCtIndex).get(caseCtIndex).get("appName").toString();

            CtMethod ctMethod = null;
            try {
                ctMethod = caseCtClass.getDeclaredMethod(po.getMethodName());
                if (po.getMethodName().equalsIgnoreCase("initDriver") && deviceName.equalsIgnoreCase("Android")) {
                    ctMethod.insertAfter(FunctionWapper.initAndroidWrapper(po, appName, platformName, platformVersion, deviceName));
                } else if (po.getMethodName().equalsIgnoreCase("destroyDriver") && deviceName.equalsIgnoreCase("Android")) {
                    ctMethod.insertAfter(FunctionWapper.destroyAndroidWrapper(po));
                } else if (po.getMethodName().equalsIgnoreCase("initDriver") && deviceName.equalsIgnoreCase("iPhone")) {
                    ctMethod.insertAfter(FunctionWapper.initIOSWrapper(po, appName, platformName, platformVersion, deviceName));
                } else if (po.getMethodName().equalsIgnoreCase("destroyDriver") && deviceName.equalsIgnoreCase("iPhone")) {
                    ctMethod.insertAfter(FunctionWapper.destroyIOSWrapper(po));
                } else if (po.getMethodName().equalsIgnoreCase("standByApp")) {
                    ctMethod.insertAfter(FunctionWapper.standByAppWrapper(po));
                } else if (po.getMethodName().startsWith("wait")) {
                    ctMethod.insertAfter(FunctionWapper.waitAction(po));
                } else {
                    ctMethod.insertAfter(FunctionWapper.operationWrapper(po));
                }
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

