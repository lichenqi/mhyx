package com.lianliantao.yuetuan.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lianliantao.yuetuan.R;


/*连续点击弹土司显示问题*/
public class ToastUtils {
	

    private static Toast toast, centerToast;

    /*显示在屏幕下方*/
    public static void showToast(Context context, String content) {
        if (context != null) {
            if (toast == null) {
                toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
            } else {
                toast.setText(content);
            }
            toast.show();
        }
    }

    /*显示下屏幕中间*/
    public static void showCenterToast(Context context, String content) {
        if (context != null) {
            if (centerToast == null) {
                centerToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
            } else {
                centerToast.setText(content);
            }
            centerToast.setGravity(Gravity.CENTER, 0, 0);
            centerToast.show();
        }
    }

    /*自定义背景中间显示*/
    public static void showBackgroudCenterToast(Context context, String content) {
        if (context != null) {
            View view = LayoutInflater.from(context).inflate(R.layout.my_backgroud_toast, null);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_content.setText(content);
            tv_content.getBackground().setAlpha(200);
            Toast toast = new Toast(context);
            toast.setView(view);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
