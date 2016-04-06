package com.highpin.operatordata;

import com.highpin.tools.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by Peng.Zhao on 2016/3/27.
 */
public class ReadInitStruct {
    private SortedMap<String, SortedMap<String, Map<String, Object>>> initMap = null;
    public static Logger logger = LogManager.getLogger(ReadInitStruct.class.getName());

    /**
     * @param initMap -- 测试数据结构
     * @Description: 构造方法 -- 从测试数据结构中获取数据
     */
    public ReadInitStruct(SortedMap<String, SortedMap<String, Map<String, Object>>> initMap) {
        this.initMap = initMap;
    }

    public List<List<Map<String, Object>>> getInitField() {
        List<Map<String, Object>> initParamList = null;
        List<List<Map<String, Object>>> sheetInitParamList = new ArrayList<>();

        for (SortedMap<String, Map<String, Object>> testSuite : this.initMap.values()) {
            initParamList = new ArrayList<>();
            for (Map<String, Object> testClass : testSuite.values()) {
                initParamList.add(testClass);
            }
            sheetInitParamList.add(initParamList);
        }
        logger.info("字段列表: " + Utility.dataStructConvertJSON(sheetInitParamList));
        return sheetInitParamList;
    }

    public static void main(String[] args) throws Exception {
        TestDataExtract tde = new TestDataExtract();
        tde.createAllExcelTestData();
        ReadInitStruct rs = new ReadInitStruct(tde.getAllExcelInitData());
        rs.getInitField();
    }
}
