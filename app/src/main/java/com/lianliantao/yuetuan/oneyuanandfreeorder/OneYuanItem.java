package com.lianliantao.yuetuan.oneyuanandfreeorder;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class OneYuanItem extends RecyclerView.ItemDecoration {

    private int space;

    public OneYuanItem(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            outRect.right = space;
        }
        outRect.bottom = space;
    }
}
