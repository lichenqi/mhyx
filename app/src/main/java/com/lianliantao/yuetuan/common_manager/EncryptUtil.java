package com.lianliantao.yuetuan.common_manager;

import java.security.MessageDigest;

public class EncryptUtil {

    /**
     * MD532位数加密
     *
     * @param content 待加密内容
     * @return 密文
     */
    public static String encrypt(String content) {
        StringBuffer sb = null;
        try {
            final MessageDigest md = MessageDigest.getInstance("md5");
            md.update(content.getBytes());
            byte b[] = md.digest();

            int i;
            sb = new StringBuffer();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb == null ? null : sb.toString();
    }
}
