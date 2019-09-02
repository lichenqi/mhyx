package com.lianliantao.yuetuan.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class HotItem extends RecyclerView.ItemDecoration {

    private int space;

    public HotItem(int space) {
        this.space = space;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) % 4 != 3) {
            outRect.right = space;
        }
        if (parent.getChildLayoutPosition(view) / 4 == 0) {
            outRect.top = space;
        }
        outRect.bottom = space;
    }
}
