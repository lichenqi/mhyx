package com.lianliantao.yuetuan.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParamUtil {

    /*编码拼接字符串*/
    /*try catch的目的是防止hashmap的value值为空*/
    public static String getMapParam(LinkedHashMap<String, String> map) {
        try {
            StringBuilder sb = new StringBuilder();
            for (LinkedHashMap.Entry<String, String> entry : map.entrySet()) {
                try {
                    sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "utf-8"));
                    sb.append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (Exception e) {
            Log.e("tag", "异常处理");
            return "";
        }
    }

    /*直接拼接字符串*/
    public static String getQianMingMapParam(HashMap<String, String> map) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (Exception e) {
            Log.e("tag", "异常处理");
            return "";
        }
    }

    public static String getHeadToken(HashMap<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "utf-8"));
                sb.append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

//    /*组装请求头签名参数*/
//    public static String GroupMap(Context context, String member_id) {
//        LinkedHashMap<String, String> map = new LinkedHashMap<>();
//        map.put("x-appid", Constant.APPID);
//        map.put("x-devid", PreferUtils.getString(context, Constant.PESUDOUNIQUEID));
//        if (TextUtils.isEmpty(member_id)) {
//            map.put("x-userid", "");
//        } else {
//            map.put("x-userid", member_id);
//        }
//        map.put("AppKey", Constant.APPKEY);
//        StringBuilder sb = new StringBuilder();
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            sb.append(entry.getValue()).append("&");
//        }
//        sb.deleteCharAt(sb.length() - 1);
//        String encrypt = EncryptUtil.encrypt(sb.toString());
//        return encrypt;
//    }

}
