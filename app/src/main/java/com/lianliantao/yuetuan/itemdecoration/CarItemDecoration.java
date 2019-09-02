package com.lianliantao.yuetuan.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2018/6/26.
 */

public class CarItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public CarItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) / 4 == 0) {
            outRect.top = space;
        }
        outRect.bottom = space;
    }

}
