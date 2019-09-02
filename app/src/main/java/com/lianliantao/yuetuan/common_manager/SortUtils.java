package com.lianliantao.yuetuan.common_manager;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mtopsdk.common.util.StringUtils;

public class SortUtils {

    /*请求参数按拼音字母升序排列 不需要编码*/
    public static String formatUrlParam(LinkedHashMap<String, String> param) {
        String params = "";
        LinkedHashMap<String, String> map = param;
        try {
            List<LinkedHashMap.Entry<String, String>> itmes = new ArrayList<LinkedHashMap.Entry<String, String>>(map.entrySet());
            //对所有传入的参数按照字段名从小到大排序
            //Collections.sort(items); 默认正序
            //可通过实现Comparator接口的compare方法来完成自定义排序
            Collections.sort(itmes, new Comparator<Map.Entry<String, String>>() {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey().toString().compareTo(o2.getKey()));
                }
            });
            //构造URL 键值对的形式
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> item : itmes) {
                if (StringUtils.isNotBlank(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    sb.append(key + "=" + val);
                }
            }
            params = sb.toString();
        } catch (Exception e) {
            return "";
        }
        return params;
    }

    /*带&符号拼接参数链接 编码拼接 拼接成接口请求链接*/
    public static String getYuNum(LinkedHashMap<String, String> parama) {
        String params = "";
        LinkedHashMap<String, String> map = parama;
        try {
            List<LinkedHashMap.Entry<String, String>> itmes = new ArrayList<LinkedHashMap.Entry<String, String>>(map.entrySet());
            //对所有传入的参数按照字段名从小到大排序
            //Collections.sort(items); 默认正序
            //可通过实现Comparator接口的compare方法来完成自定义排序
            Collections.sort(itmes, new Comparator<Map.Entry<String, String>>() {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey().toString().compareTo(o2.getKey()));
                }
            });
            //构造URL 键值对的形式
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> item : itmes) {
                if (StringUtils.isNotBlank(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    val = URLEncoder.encode(val, "utf-8");
                    sb.append(key + "=" + val);
                    sb.append("&");
                }
            }
            params = sb.toString();
            if (!params.isEmpty()) {
                params = params.substring(0, params.length() - 1);
            }
        } catch (Exception e) {
            return "";
        }
        return params;
    }
}
