package com.lianliantao.yuetuan.itemdecoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.lianliantao.yuetuan.bean.ShareSelectBean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class PinLeiItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private List<ShareSelectBean> list;

    public PinLeiItemDecoration(int space, List<ShareSelectBean> list) {
        this.space = space;
        this.list = list;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) == (list.size()) - 1) {/*代表最后一个item*/
            outRect.right = space;
        }
        outRect.top = space;
        outRect.bottom = space;
        outRect.left = space;
    }

}
