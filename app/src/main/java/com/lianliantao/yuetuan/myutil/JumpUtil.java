package com.lianliantao.yuetuan.myutil;

import android.content.Context;
import android.content.Intent;

import com.lianliantao.yuetuan.activity.ShopDetailActivity;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.login_and_register.MyWXLoginActivity;
import com.lianliantao.yuetuan.util.PreferUtils;

public class JumpUtil {

    public static void jump2ShopDetail(Context context, String itemId) {
        Intent intent;
        if (PreferUtils.getBoolean(context, CommonApi.ISLOGIN)) {
            intent = new Intent(context, ShopDetailActivity.class);
            intent.putExtra("itemId", itemId);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            intent = new Intent(context, MyWXLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }
}
