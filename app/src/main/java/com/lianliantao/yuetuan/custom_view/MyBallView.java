package com.lianliantao.yuetuan.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.lianliantao.yuetuan.R;

/*自定义小球轨迹*/
public class MyBallView extends View {
    /*定义画笔和初始位置*/
    Paint paint = new Paint();
    public float x = 500;
    public float y = 500;
    public int color;

    public MyBallView(Context context) {
        super(context);
    }

    public MyBallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyBallView);
        array.getColor(R.styleable.MyBallView_textballColor, Color.RED);
        array.recycle();
    }

    public MyBallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.BLUE);
        canvas.drawRect(x, y, 100, 100, paint);
        paint.setColor(color);
        canvas.drawText("magic", x, y, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        invalidate();
        return true;
    }
}
