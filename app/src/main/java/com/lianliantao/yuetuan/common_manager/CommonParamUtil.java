package com.lianliantao.yuetuan.common_manager;

import android.content.Context;
import android.text.TextUtils;

import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.VersionUtil;

import java.util.LinkedHashMap;

public class CommonParamUtil {

    //混淆后缀
    private static String CRYPT_SUFIX_APP = "!oop5clnr57";

    public static String getCommonParamSign(Context context) {
        String uId = PreferUtils.getString(context, "uId");
        if (TextUtils.isEmpty(uId) || uId == null) {
            uId = "";
        }
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("netType", NetUtil.getNetClassic(context));
        map.put("deviceOs", "Android");
        map.put("uId", uId);
        map.put("deviceOp", VersionUtil.getAndroidNumVersion(context));
        map.put("deviceId", AppUtils.getPesudoUniqueID());
        String qianMingMapParam = SortUtils.formatUrlParam(map) + CRYPT_SUFIX_APP;
        String strSig = "1" + EncryptUtil.encrypt(qianMingMapParam);
        map.put("sig", strSig);
        String yuNum = SortUtils.getYuNum(map);
        return "?" + yuNum;
    }

    public static String getOtherParamSign(Context context, LinkedHashMap<String, String> map) {
        String uId = PreferUtils.getString(context, "uId");
        if (TextUtils.isEmpty(uId) || uId == null) {
            uId = "";
        }
        map.put("netType", NetUtil.getNetClassic(context));
        map.put("deviceOs", "Android");
        map.put("uId", uId);
        map.put("deviceOp", VersionUtil.getAndroidNumVersion(context));
        map.put("deviceId", AppUtils.getPesudoUniqueID());
        String qianMingMapParam = SortUtils.formatUrlParam(map) + CRYPT_SUFIX_APP;
        String strSig = "1" + EncryptUtil.encrypt(qianMingMapParam);
        map.put("sig", strSig);
        String yuNum = SortUtils.getYuNum(map);
        return "?" + yuNum;
    }

}
