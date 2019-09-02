package com.lianliantao.yuetuan.itemdecoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lianliantao.yuetuan.util.DensityUtils;

public class CidInfoItemSpace extends RecyclerView.ItemDecoration {

    private int top, left;
    private Context context;

    public CidInfoItemSpace(Context context) {
        this.context = context;
        top = DensityUtils.dip2px(context, 10);
        left = DensityUtils.dip2px(context, 15);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = left;
        }
        outRect.right = left;
        outRect.bottom = top;
        outRect.top = top;
    }
}
