package com.lianliantao.yuetuan.custom_view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.lianliantao.yuetuan.R;

import java.util.ArrayList;
import java.util.List;

public class DanceNumberView extends LinearLayout {

    private String text;
    private int textColor;
    private int textSize;
    private float textPadding;  //文字间距

    private List<SingleScrollNumberView> list = new ArrayList<>();


    public DanceNumberView(Context context) {
        this(context, null);
    }

    public DanceNumberView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DanceNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.scrollNumber);
        initAttributesData(typedArray);
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER);
    }

    public void createView() {
        if (TextUtils.isEmpty(text)) return;
        char[] chars = String.valueOf(Integer.valueOf(text).intValue()).toCharArray();
        for (int i = 0; i < chars.length; i++) {
            SingleScrollNumberView scrollNumber = new SingleScrollNumberView(getContext());
            scrollNumber.setTextColor(textColor);
            scrollNumber.setTextSize(textSize);
            scrollNumber.setText(String.valueOf(chars[i]));
            addView(scrollNumber);
            list.add(scrollNumber);
        }
    }

    public void start() {
        if (list.size() == 0) return;
        for (SingleScrollNumberView view : list) {
            view.start();
        }
    }

    private void initAttributesData(TypedArray typedArray) {
        textColor = typedArray.getColor(R.styleable.scrollNumber_textColor, Color.BLACK);
        textSize = (int) typedArray.getDimension(R.styleable.scrollNumber_textSize, 16);
        text = typedArray.getString(R.styleable.scrollNumber_text);
        textPadding = typedArray.getDimension(R.styleable.scrollNumber_textPadding, 5);
    }

}
