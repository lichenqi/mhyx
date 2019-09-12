package com.lianliantao.yuetuan.itemdecoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2018/5/4.
 */

public class PinLeiItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private String[] split;

    public PinLeiItemDecoration(int space, String[] split) {
        this.space = space;
        this.split = split;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) == (split.length) - 1) {/*代表最后一个item*/
            outRect.right = space;
        }
        outRect.top = space;
        outRect.bottom = space;
        outRect.left = space;
    }

}
