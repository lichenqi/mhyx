package com.lianliantao.yuetuan.util;

import android.content.Context;

public class DensityUtils {

    public static int dip2px(Context contex, int dp) {
        float density = contex.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    public static int dip2pxT(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }
}
