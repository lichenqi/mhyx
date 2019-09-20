package com.lianliantao.yuetuan.custom_view;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

@SuppressLint("AppCompatCustomView")
public class ScrollingDigitialAnimationSecond extends TextView {

    private String numStart = "1000";  //起始值
    private String numEnd;          //结束值
    private long duration = 1500; //持续时间
    private String prefixString = "";  //前缀字符串
    private String postfixString = ""; //后缀字符串

    public ScrollingDigitialAnimationSecond(Context context) {
        super(context);
    }

    public ScrollingDigitialAnimationSecond(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollingDigitialAnimationSecond(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setNumberString(String number) {
        setNumberString("0", number);
    }

    public void setNumberString(String numberStart, String numberEnd) {
        numStart = numberStart;         //得到设置的起始数字
        numEnd = numberEnd;             //得到设置的最终数字
        if (checkNumString(numberStart, numberEnd)) {
            // 数字合法　开始数字动画
            start();
        } else {
            // 数字不合法　直接调用　setText　设置最终值
            setText(prefixString + numberEnd + postfixString);
        }
    }


    /**
     * 设置前字符串符号方法
     */
    public void setPrefixString(String mPrefixString) {
        this.prefixString = mPrefixString;
    }

    /**
     * 设置后字符串符号方法
     */
    public void setPostfixString(String mPostfixString) {
        this.postfixString = mPostfixString;
    }

    private boolean isInt; // 是否是整数

    /**
     * 校验数字的合法性
     *
     * @param numberStart 　开始的数字
     * @param numberEnd   　结束的数字
     * @return 合法性
     */
    private boolean checkNumString(String numberStart, String numberEnd) {
        try {
            new BigInteger(numberStart);        //起始数字整数的筛选
            new BigInteger(numberEnd);          //最终数字整数的筛选
            isInt = true;                      //确认是整数标记
        } catch (Exception e) {
            isInt = false;                     //否则标记不是整数
            e.printStackTrace();
        }
        try {
            BigDecimal start = new BigDecimal(numberStart);     //起始数字小数的筛选
            BigDecimal end = new BigDecimal(numberEnd);         //最终数字小数的筛选
            return end.compareTo(start) >= 0;                   //比较小数是否大于等于0
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void start() {
        //创建数字动画，并设置起始值和最终值
        ValueAnimator animator = ValueAnimator.ofObject(new ScrollingDigitalAnimation.BigDecimalEvaluator(),
                new BigDecimal(numStart), new BigDecimal(numEnd));
        animator.setDuration(duration);        //设置动画持续的时间
        //设置动画内插器
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        //动画监听器
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                BigDecimal value = (BigDecimal) valueAnimator.getAnimatedValue();
                //设置显示数值
                setText(prefixString + format(value) + postfixString);
            }
        });
        animator.start();       //启动动画
    }

    /**
     * 格式化 BigDecimal ,小数部分时保留两位小数并四舍五入
     */
    private String format(BigDecimal bd) {
        String pattern;
        if (isInt) {                //如果是整数
            pattern = "#,###";      //整数格式
        } else {
            pattern = "#,##0.00";   //小数格式
        }
        DecimalFormat df = new DecimalFormat(pattern);      //进行格式化
        return df.format(bd);                               //返回格式化后的字符串
    }

    //计算线性内插的结果
    static class BigDecimalEvaluator implements TypeEvaluator {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            BigDecimal start = (BigDecimal) startValue;
            BigDecimal end = (BigDecimal) endValue;
            BigDecimal result = end.subtract(start);
            return result.multiply(new BigDecimal("" + fraction)).add(start);
        }
    }
}
