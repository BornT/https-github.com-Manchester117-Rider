package com.highpin.operatordata;

import com.highpin.tools.Utility;
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
    private Map<String, List<InitObject>> allSheetTitleMap = new HashMap<>();

    public TestDataExtract() {
        this.caseFolder = new File("cases");
    }

    public SortedMap<String, SortedMap<String, SortedMap<String, Map<String, Object>>>> getAllExcelData() {
        File [] caseList = this.caseFolder.listFiles();
        InputStream is = null;
        XSSFWorkbook excelWBook = null;

        SortedMap<String, SortedMap<String, SortedMap<String, Map<String, Object>>>>  multiExcelMap = new TreeMap<>();
        SortedMap<String, SortedMap<String, Map<String, Object>>> singleExcelMap = null;

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
                        singleExcelMap = this.getSingleExcelData(excelWBook);
                        multiExcelMap.put(excelName, singleExcelMap);
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
        logger.info("所有配置参数: " + Utility.dataStructConvertJSON(this.allSheetTitleMap));
//        logger.info("所有测试数据: " + Utility.dataStructConvertJSON(multiExcelMap));
        return multiExcelMap;
    }

    public SortedMap<String, SortedMap<String, Map<String, Object>>> getSingleExcelData(XSSFWorkbook excelWBook) {
        XSSFSheet titleSheet = null;
        XSSFSheet testCaseSheet = null;

        SortedMap<String, SortedMap<String, Map<String, Object>>> singleExcelMap = null;
        SortedMap<String, Map<String, Object>> sheetMap = null;

        List<InitObject> initList = new ArrayList<>();
        // 为每个Excel定义存放sheet名称的List
        List<String> sheetList = new ArrayList<>();
        // 获取Test Suite
        titleSheet = excelWBook.getSheet("Test Suite");
        // 获取Test Suite的行数
        int rowNum = titleSheet.getPhysicalNumberOfRows();
        // 遍历Test Suite每一行,获取要运行测试的Sheet名
        for (int rowIdx = 1; rowIdx < rowNum; ++rowIdx) {
            String runSheet = titleSheet.getRow(rowIdx).getCell(0).getStringCellValue();
            // 获取每个Driver的初始化参数
            InitObject initObject = new InitObject();
            initObject.setPlatformName(titleSheet.getRow(rowIdx).getCell(2).getStringCellValue());
            initObject.setPlatformVersion(titleSheet.getRow(rowIdx).getCell(3).getStringCellValue());
            initObject.setDeviceName(titleSheet.getRow(rowIdx).getCell(4).getStringCellValue());
            initObject.setAppName(titleSheet.getRow(rowIdx).getCell(5).getStringCellValue());
            // 将每个Driver的初始化参数放到列表中
            initList.add(initObject);

            String runMode = titleSheet.getRow(rowIdx).getCell(6).getStringCellValue();
            // 如果Sheet的RunMode是'Yes'则将这个Sheet名放置到列表当中
            if ("Yes".equalsIgnoreCase(runMode)) {
                sheetList.add(runSheet);
            }
        }
        // 创建单个Excel的数据结构
        singleExcelMap = new TreeMap<>();
        // 遍历每个要运行测试的sheet名
        for (String sheetName : sheetList) {
            // 通过sheet名获取sheet
            testCaseSheet = excelWBook.getSheet(sheetName);
            sheetMap = this.getSheetData(testCaseSheet);
            // 需要在此处插入验证点
            singleExcelMap.put(sheetName, sheetMap);
        }
//        logger.info("单个Excel数据: " + Utility.dataStructConvertJSON(singleExcelMap));
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
                // 获取Sheet的字段Title
                String title = testCaseSheet.getRow(0).getCell(colIdx).getStringCellValue().trim();
                // 获取Sheet的字段Value
                String value = testCaseSheet.getRow(rowIdx).getCell(colIdx).getStringCellValue().trim();
                // 获取步骤ID
                // String testStepID = testCaseSheet.getRow(rowIdx).getCell(0).getStringCellValue().trim();
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
            }
        }
//        logger.info("单个Sheet页数据: " + Utility.dataStructConvertJSON(sheetMap));
        return sheetMap;
    }

    public static void main(String[] args) {
        TestDataExtract eo = new TestDataExtract();
        eo.getAllExcelData();
    }
}
