package com.lianliantao.yuetuan.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class XiangGuangTuiJianItem extends RecyclerView.ItemDecoration {

    private int space;

    public XiangGuangTuiJianItem(int space) {
        this.space = space;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = space;
        }
        outRect.right = space;
        outRect.bottom = space;
    }
}
