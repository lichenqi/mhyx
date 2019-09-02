package com.lianliantao.yuetuan.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.view.CenterAlignImageSpan;

public class TextViewUtil {
    /*Android 中同一个TextView设置不同大小字体*/
    public static void setTextViewSize(String content, TextView textView) {
        Spannable sp = new SpannableString(content);
        sp.setSpan(new AbsoluteSizeSpan(12, true), 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(13, true), 3, content.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(sp);
    }

    /*同一个textview设置不同字体大小颜色*/
    public static void setTextViewColorAndSize(String content, TextView tv_content) {
        SpannableString sp = new SpannableString(content);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#F76A7C")), 6, content.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(16, true), 6, content.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tv_content.setText(sp);
    }

    /*同一个textview设置不同字体大小颜色前面带图片的*/
    public static void setTextViewColorWithIcon(Context context, CharSequence charSequence, TextView textView, int position) {
        String text = "[icon] " + charSequence;
        SpannableString spannable = new SpannableString(text);
        Drawable drawable;
        if (position == 0) {
            drawable = context.getResources().getDrawable(R.mipmap.sale_one);
        } else if (position == 1) {
            drawable = context.getResources().getDrawable(R.mipmap.sale_two);
        } else {
            drawable = context.getResources().getDrawable(R.mipmap.sale_three);
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //图片居中
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        spannable.setSpan(imageSpan, 0, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);
    }
}
