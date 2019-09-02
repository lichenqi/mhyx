package com.lianliantao.yuetuan.util;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

public class XRecyclerViewUtil {
    public static void setView(XRecyclerView xrecycler) {
        xrecycler.setLoadingMoreEnabled(true);
        xrecycler.setPullRefreshEnabled(true);
        xrecycler.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xrecycler.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xrecycler.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        xrecycler.getDefaultFootView().setNoMoreHint("已加载完毕");
    }
}
