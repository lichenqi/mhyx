package com.lianliantao.yuetuan.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.view.CenterAlignImageSpan;


public class IconAndTextGroupUtil {

    public static void setTextView(Context context, TextView textView, CharSequence charSequence, String site) {
        String text = "[icon] " + charSequence;
        SpannableString spannable = new SpannableString(text);
        Drawable drawable;
        if (site.equals("1")) {
            drawable = context.getResources().getDrawable(R.mipmap.tianmao_small_icon);
        } else {
            drawable = context.getResources().getDrawable(R.mipmap.taobao_small_icon);
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //图片居中
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        spannable.setSpan(imageSpan, 0, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);
    }
}
