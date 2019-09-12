package com.lianliantao.yuetuan.itemdecoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class BrandBaseItem extends RecyclerView.ItemDecoration {

    private int space;

    public BrandBaseItem(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) % 4 == 2 || parent.getChildLayoutPosition(view) % 4 == 3 || parent.getChildLayoutPosition(view) % 4 == 1) {
            outRect.right = space;
        }
        outRect.bottom = space;
    }
}
