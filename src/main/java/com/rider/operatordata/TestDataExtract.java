package com.rider.operatordata;

import com.rider.tools.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Peng.Zhao on 2016/3/15.
 */
public class TestDataExtract {
    public static Logger logger = LogManager.getLogger(TestDataExtract.class.getName());
    private File caseFolder = null;
    private SortedMap<String, SortedMap<String, SortedMap<String, Map<String, Object>>>> allExcelTestMap = new TreeMap<>();
    private SortedMap<String, SortedMap<String, Map<String, Object>>> allExcelInitMap = new TreeMap<>();

    public TestDataExtract() {
        this.caseFolder = new File("cases");
    }

    public void createAllExcelTestData() {
        File[] caseList = this.caseFolder.listFiles();
        InputStream is = null;
        XSSFWorkbook excelWBook = null;

        SortedMap<String, SortedMap<String, Map<String, Object>>> singleExcelMap = null;
        SortedMap<String, Map<String, Object>> initSheetMap = null;

        try {
            // 如果cases文件夹不为空并且cases文件夹中的文件数量大于0才会执行获取Excel handler的操作
            if (caseList != null && caseList.length > 0) {
                // 遍历cases文件夹,获取每一个文件
                for (File excelCaseFile : caseList) {
                    // 获取测试用例文件名
                    String excelName = excelCaseFile.getName();
                    // 判断文件类型,只有文件前缀是test_且后缀是.xlsx,才会执行获取文件句柄的操作
                    if (excelName.startsWith("test_") && excelName.endsWith(".xlsx")) {
                        // 对Excel文件名进行处理(截取后缀)
                        excelName = excelName.substring(0, excelName.indexOf("."));
                        // 将Excel文件转变为XSSFWorkbook
                        is = new FileInputStream(excelCaseFile.getAbsoluteFile());
                        excelWBook = new XSSFWorkbook(is);
                        // 获取可运行的sheet以及sheet中的数据
                        singleExcelMap = this.getSingleExcelTestData(excelWBook);
                        this.allExcelTestMap.put(excelName, singleExcelMap);
                        // 获取每个sheet的初始化参数
                        initSheetMap = this.getSingleExcelTitleData(excelWBook);
                        this.allExcelInitMap.put(excelName, initSheetMap);
                    }
                }
            }
            // 关闭文件输入流
            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(this.allExcelInitMap);
//        System.out.println(this.allExcelTestMap);
        logger.info("所有配置参数: " + Utility.dataStructConvertJSON(this.allExcelInitMap));
        logger.info("所有测试数据: " + Utility.dataStructConvertJSON(this.allExcelTestMap));
    }

    public SortedMap<String, SortedMap<String, Map<String, Object>>> getSingleExcelTestData(XSSFWorkbook excelWBook) {
        XSSFSheet titleSheet = null;
        XSSFSheet testCaseSheet = null;

        SortedMap<String, SortedMap<String, Map<String, Object>>> singleExcelMap = null;
        SortedMap<String, Map<String, Object>> sheetMap = null;

        List<String> initSheetList = new ArrayList<>();
        // 获取Test Suite
        titleSheet = excelWBook.getSheet("Test Suite");
        // 获取Test Suite的行数
        int rowNum = titleSheet.getPhysicalNumberOfRows();
        // 遍历Test Suite每一行,获取要运行测试的Sheet名
        for (int rowIdx = 1; rowIdx < rowNum; ++rowIdx) {
            String sheetName = titleSheet.getRow(rowIdx).getCell(0).getStringCellValue();
            String runMode = titleSheet.getRow(rowIdx).getCell(6).getStringCellValue();
            // 如果Sheet的RunMode是'Yes'则将这个Sheet放置到列表当中
            if ("Yes".equalsIgnoreCase(runMode)) {
                initSheetList.add(sheetName);
            }
        }
        // 创建单个Excel的数据结构
        singleExcelMap = new TreeMap<>();
        for (String runSheet : initSheetList) {
            // 通过sheet名获取sheet
            testCaseSheet = excelWBook.getSheet(runSheet);
            sheetMap = this.getSheetData(testCaseSheet);
            // 需要在此处插入验证点
            singleExcelMap.put(runSheet, sheetMap);
        }
        logger.info("单个Excel数据: " + Utility.dataStructConvertJSON(singleExcelMap));
        return singleExcelMap;
    }

    public SortedMap<String, Map<String, Object>> getSheetData(XSSFSheet testCaseSheet) {
        int rowNum = testCaseSheet.getPhysicalNumberOfRows();
        int colNum = testCaseSheet.getRow(0).getPhysicalNumberOfCells();

        SortedMap<String, Map<String, Object>> sheetMap = new TreeMap<>();
        Map<String, Object> stepMap = null;

        List<String> verifyTypeList = null;
        List<String> verifyTargetList = null;
        List<String> verifyValueList = null;

        // 遍历Excel中的所有数据
        for (int rowIdx = 1; rowIdx < rowNum; ++rowIdx) {
            stepMap = new HashMap<>();
            for (int colIdx = 0; colIdx < colNum; ++colIdx) {
                // 获取测试用例编号
                String caseID = testCaseSheet.getRow(rowIdx).getCell(0).getStringCellValue().trim();
                // 获取Sheet的字段Title
                String title = testCaseSheet.getRow(0).getCell(colIdx).getStringCellValue().trim();
                // 获取Sheet的字段Value
                String value = null;
                try {
                    value = testCaseSheet.getRow(rowIdx).getCell(colIdx).getStringCellValue().trim();
                    // 根据字段获取数据
                    if (title.equals("Test_Step_ID") && !value.isEmpty()) {
                        sheetMap.put(value, stepMap);
                        // 首先给每个测试步骤增加验证点的数据结构
                        verifyTypeList = new ArrayList<>();
                        verifyTargetList = new ArrayList<>();
                        verifyValueList = new ArrayList<>();
                    } else if (title.equals("Description")) {
                        stepMap.put(title, value);
                    } else if (title.equals("Action_Keyword")) {
                        stepMap.put(title, value);
                    } else if (title.equals("Action_Type")) {
                        stepMap.put(title, value);
                    } else if (title.equals("Locator_Type")) {
                        stepMap.put(title, value);
                    } else if (title.equals("Locator_Value")) {
                        stepMap.put(title, value);
                    } else if (title.equals("Data_Set")) {
                        stepMap.put(title, value);
                    } else if (title.equals("Verify_Type")) {
                        stepMap.put(title, verifyTypeList);
                        if (verifyTypeList != null) {
                            verifyTypeList.add(value);
                        }
                    } else if (title.equals("Verify_Target")) {
                        stepMap.put(title, verifyTargetList);
                        if (verifyTargetList != null) {
                            verifyTargetList.add(value);
                        }
                    } else if (title.equals("Verify_Value")) {
                        stepMap.put(title, verifyValueList);
                        if (verifyValueList != null) {
                            verifyValueList.add(value);
                        }
                    } else if (title.equals("Screen_Capture")) {
                        stepMap.put(title, value);
                    }
                } catch (NullPointerException e) {
                    logger.error("出错单元格: " + caseID + "|" + title);
                    e.printStackTrace();
                }
            }
        }
//        logger.info("单个Sheet页数据: " + Utility.dataStructConvertJSON(sheetMap));
        return sheetMap;
    }

    public SortedMap<String, Map<String, Object>> getSingleExcelTitleData(XSSFWorkbook excelWBook) {
        XSSFSheet titleSheet = null;
        // 为每个Excel定义存放sheet属性的Map
        SortedMap<String, Map<String, Object>> initSheetMap = new TreeMap<>();
        // 获取Test Suite
        titleSheet = excelWBook.getSheet("Test Suite");
        // 获取Test Suite的行数
        int rowNum = titleSheet.getPhysicalNumberOfRows();
        try {
            // 遍历Test Suite每一行,获取要运行测试的Sheet名
            for (int rowIdx = 1; rowIdx < rowNum; ++rowIdx) {
                String sheetName = titleSheet.getRow(rowIdx).getCell(0).getStringCellValue();
                // 获取每个Driver的初始化参数
                Map<String, Object> initItem = new HashMap<>();
                initItem.put("platformName", titleSheet.getRow(rowIdx).getCell(2).getStringCellValue());
                initItem.put("platformVersion", titleSheet.getRow(rowIdx).getCell(3).getStringCellValue());
                initItem.put("deviceName", titleSheet.getRow(rowIdx).getCell(4).getStringCellValue());
                initItem.put("appName", titleSheet.getRow(rowIdx).getCell(5).getStringCellValue());
                initItem.put("runMode", titleSheet.getRow(rowIdx).getCell(6).getStringCellValue());
                // 如果Sheet的RunMode是'Yes'则将这个Sheet放置到列表当中
                if ("Yes".equalsIgnoreCase(initItem.get("runMode").toString())) {
                    initSheetMap.put(sheetName, initItem);
                }
            }
        } catch (Exception e) {
            // 规避读取Test Suite每个单元格必须是字符串的问题
            logger.error("Test Suite中存在数字类型的单元格");
            e.printStackTrace();
        }
        logger.info("单个Excel运行Title: " + Utility.dataStructConvertJSON(initSheetMap));
        return initSheetMap;
    }

    public SortedMap<String, SortedMap<String, SortedMap<String, Map<String, Object>>>> getAllExcelTestData() {
        // 返回所有Excel测试类的测试数据
        return this.allExcelTestMap;
    }

    public SortedMap<String, SortedMap<String, Map<String, Object>>> getAllExcelInitData() {
        // 返回所有Excel测试类的初始化参数
        return this.allExcelInitMap;
    }

    public static void main(String[] args) {
        TestDataExtract eo = new TestDataExtract();
        eo.createAllExcelTestData();
        eo.getAllExcelTestData();
    }
}
