package com.lianliantao.yuetuan.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.lianliantao.yuetuan.R;


public class BotBgView extends View {

    private Paint mPaint;
    private int centerX, centerY;
    private PointF start, end, control;
    private int color = R.color.white;

    public BotBgView(Context context) {
        super(context);
    }

    public BotBgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setColor(context.getResources().getColor(color));
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        start = new PointF(0, 0);
        end = new PointF(0, 0);
        control = new PointF(0, 0);
    }

    public BotBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w;
        centerY = h;
        start.x = 0;
        start.y = h * (4f / 5f);
        end.x = w;
        end.y = h * (4f / 5f); //
        control.x = w / 2;
        control.y = h * (2f / 5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBG(canvas);
    }

    // 绘制头部的背景图片 贝塞尔曲线的下边
    private void drawBG(Canvas canvas) {
        Path path = new Path();
        path.moveTo(start.x, start.y);
        path.quadTo(control.x, control.y, end.x, end.y);
        path.lineTo(centerX, centerY);
        path.lineTo(start.x, centerY);
        canvas.drawPath(path, mPaint);
    }
}
