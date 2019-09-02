package com.lianliantao.yuetuan.util;

import java.text.NumberFormat;

public class MoneyFormatUtil {

    /*去掉小数点后面末尾的0*/
    public static String StringFormatWithYuan(String content) {
        NumberFormat nf = NumberFormat.getInstance();
        String format = nf.format(Double.valueOf(content)).replace(",", "").trim();
        return format;
    }

}
