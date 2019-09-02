package com.lianliantao.yuetuan.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class SingleScrollNumberView extends View {

    private String mText = "";
    private int mTextColor = Color.BLACK;
    private int mTextSize = 30;
    private int textPadding = 0;

    private Paint mPaint;
    private Context mContext;
    private static String BASE_NUMBER = "0123456789";

    /**
     * 由于只想用一个view完事，但是文字绘制的时候，不带边距，只好在measure的时候，用大字体占个大位置。在onDraw的时候，用小字体。这样文字就自带了边距
     */
    private int textOutHeight;
    private int textOutWidth;
    private int textInnerWidth;
    private int scrollY = 0;
    private int scale = 2;

    public SingleScrollNumberView(Context context) {
        this(context, null);
    }

    public SingleScrollNumberView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleScrollNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //设置字体和颜色
        mPaint.setTextSize(mTextSize * scale);
        mPaint.setColor(mTextColor);
        setPadding(textPadding, textPadding, textPadding, textPadding);
    }

    public SingleScrollNumberView setText(String text) {
        this.mText = text;
        return this;
    }

    public SingleScrollNumberView setTextColor(int textColor) {
        this.mTextColor = textColor;
        mPaint.setColor(mTextColor);
        return this;
    }

    public SingleScrollNumberView setTextSize(int textSize) {
        this.mTextSize = textSize;
        mPaint.setTextSize(mTextSize * scale);
        return this;
    }

    public SingleScrollNumberView setTextPadding(int padding) {
        this.textPadding = padding;
        setPadding(textPadding, textPadding, textPadding, textPadding);
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            Rect bounds = new Rect();
            mPaint.getTextBounds(mText, 0, mText.length(), bounds);
            textOutWidth = bounds.width();
            width = bounds.width() + getPaddingLeft() + getPaddingRight();

        }
        if (heightMode == MeasureSpec.AT_MOST) {
            Rect bounds = new Rect();
            mPaint.getTextBounds(mText, 0, mText.length(), bounds);
            textOutHeight = bounds.height();
            height = textOutHeight + getPaddingTop() + getPaddingBottom();
        }
        //设置宽高
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (TextUtils.isEmpty(mText)) return;
        if (mText.length() > 1) return;
        if (!BASE_NUMBER.contains(mText)) return;

        //保持垂直方向居中
        //getPaddingLeft() + (textOutWidth - textInnerWidth) / 2 保持水平方向居中
        mPaint.setTextSize(mTextSize);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = getHeight() / 2 + dy;

        for (int i = 0; i <= Integer.valueOf(mText).intValue(); i++) {
            mPaint.setTextSize(mTextSize);
            Rect innerBounds = new Rect();
            mPaint.getTextBounds(mText, 0, mText.length(), innerBounds);
            textInnerWidth = innerBounds.width();
            canvas.drawText(String.valueOf(i), getPaddingLeft() + (textOutWidth - textInnerWidth) / 2, baseLine + i * textOutHeight - scrollY, mPaint);
        }
    }


    private void animateView() {
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                //最大数字出现后，就要停下来。还要停稳，不然多几个数字，就会出现高低不平了。
                if (scrollY >= textOutHeight * Integer.valueOf(mText).intValue()) {
                    scrollY = textOutHeight * Integer.valueOf(mText).intValue();
                    SingleScrollNumberView.this.postInvalidate();
                    return;
                }
                scrollY += textOutHeight / 4;
                SingleScrollNumberView.this.postInvalidate();
                animateView();

            }
        }, 8);
    }

    public void start() {
        scrollY = 0;
        animateView();
    }
}
