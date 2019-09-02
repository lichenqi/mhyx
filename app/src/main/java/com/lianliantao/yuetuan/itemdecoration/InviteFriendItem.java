package com.lianliantao.yuetuan.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class InviteFriendItem extends RecyclerView.ItemDecoration {

    private int space;
    private int list;

    public InviteFriendItem(int space, int list) {
        this.space = space;
        this.list = list;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) == list - 1) {
            outRect.right = space;
        }
    }
}
