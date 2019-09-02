package com.lianliantao.yuetuan.util;

import java.math.BigDecimal;

public class NumUtil {
    public static String getNum(String value) {
        try {
            double v = Double.parseDouble(value);
            if (v > 10000) {
                String str = String.valueOf(v / 10000);
                String s = removeLast0(str);
                BigDecimal bg3 = new BigDecimal(Double.valueOf(s));
                double money = bg3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                return money + "万";
            } else {
                return value;
            }
        } catch (Exception e) {
            return value;
        }
    }

    public static String removeLast0(String string) {
        if (string == null || string.length() == 0) {
            return "0";
        }
        try {
            while (string.charAt(string.length() - 1) == '0') {
                string = string.substring(0, string.length() - 1);
            }
            if (string.charAt(string.length() - 1) == '.') {
                string = string.substring(0, string.length() - 1);
            }
        } catch (Exception e) {
            return "";
        }
        return string;
    }

}
