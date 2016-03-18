package com.highpin.tools;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/3/16.
 */
public class Utility {
    /**
     * @param obj -- 传入的Java数据结构
     * @return json -- 返回的JSON
     * @Description: 将Java数据结构转为JSON...方便调试...
     */
    public static String dataStructConvertJSON(Object obj) {
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        return json;
    }
}
