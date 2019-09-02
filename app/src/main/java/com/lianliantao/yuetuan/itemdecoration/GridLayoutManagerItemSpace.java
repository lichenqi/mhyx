package com.lianliantao.yuetuan.itemdecoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class GridLayoutManagerItemSpace extends RecyclerView.ItemDecoration {

    private int space;

    public GridLayoutManagerItemSpace(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            outRect.right = space;
        }
        if (parent.getChildLayoutPosition(view) != 0) {
            outRect.bottom = space;
        }
    }
}
