package com.lianliantao.yuetuan.coverflow;

import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class MyLayoutManager extends RecyclerView.LayoutManager {

    private boolean looperEnable = true;

    /*最大存储item信息存储数量，超过设置数量，则动态计算来获取*/
    private final int MAX_RECT_COUNT = 100;
    /*滑动总偏移量*/
    private int mOffsetAll = 0;
    /*Item宽*/
    private int mDecoratedChildWidth = 0;
    /*Item高*/
    private int mDecoratedChildHeight = 0;
    /*Item间隔与item宽的比例*/
    private float mIntervalRatio = 0.5f;
    /*起始ItemX坐标*/
    private int mStartX = 0;

    /**
     * 起始Item Y坐标
     */
    private int mStartY = 0;

    /**
     * 保存所有的Item的上下左右的偏移量信息
     */
    private SparseArray<Rect> mAllItemFrames = new SparseArray<>();

    /**
     * 记录Item是否出现过屏幕且还没有回收。true表示出现过屏幕上，并且还没被回收
     */
    private SparseBooleanArray mHasAttachedItems = new SparseBooleanArray();

    /**
     * RecyclerView的Item回收器
     */
    private RecyclerView.Recycler mRecycle;

    /**
     * RecyclerView的状态器
     */
    private RecyclerView.State mState;

    /**
     * 滚动动画
     */
    private ValueAnimator mAnimation;

    /**
     * 正显示在中间的Item
     */
    private int mSelectPosition = 0;

    /**
     * 前一个正显示在中间的Item
     */
    private int mLastSelectPosition = 0;

    /**
     * 滑动的方向：左
     */
    private static int SCROLL_LEFT = 1;

    /**
     * 滑动的方向：右
     */
    private static int SCROLL_RIGHT = 2;

    /**
     * 选中监听
     */
    private CoverFlowLayoutManger.OnSelected mSelectedListener;

    /**
     * 是否为平面滚动，Item之间没有叠加，也没有缩放
     */
    private boolean mIsFlatFlow = false;

    /**
     * 是否启动Item灰度值渐变
     */
    private boolean mItemGradualGrey = false;

    /**
     * 是否启动Item半透渐变
     */
    private boolean mItemGradualAlpha = false;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //如果没有item，直接返回
        //跳过preLayout，preLayout主要用于支持动画
        if (getItemCount() <= 0 || state.isPreLayout()) {
            return;
        }
        //标注1.如果当前时准备状态，直接返回
        if (state.isPreLayout()) {
            return;
        }
        //标注2.将视图分离放入scrap缓存中，以准备重新对view进行排版
        detachAndScrapAttachedViews(recycler);

        int autualWidth = 0;
        for (int i = 0; i < getItemCount(); i++) {
            //标注3.初始化，将在屏幕内的view填充
            View itemView = recycler.getViewForPosition(i);
            addView(itemView);
            //标注4.测量itemView的宽高
            measureChildWithMargins(itemView, 0, 0);
            int width = getDecoratedMeasuredWidth(itemView);
            int height = getDecoratedMeasuredHeight(itemView);
            //标注5.根据itemView的宽高进行布局
            layoutDecorated(itemView, autualWidth, 0, autualWidth + width, height);
            autualWidth += width;
            //标注6.如果当前布局过的itemView的宽度总和大于RecyclerView的宽，则不再进行布局
            if (autualWidth > getWidth()) {
                break;
            }
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //标注1.横向滑动的时候，对左右两边按顺序填充itemView
        int travl = fill(dx, recycler, state);
        if (travl == 0) {
            return 0;
        }
        //2.滑动
        offsetChildrenHorizontal(-travl);
        //3.回收已经不可见的itemView
        recyclerHideView(dx, recycler, state);
        return travl;
    }

    /**
     * 左右滑动的时候，填充
     */
    private int fill(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dx > 0) {
            //标注1.向左滚动
            View lastView = getChildAt(getChildCount() - 1);
            if (lastView == null) {
                return 0;
            }
            int lastPos = getPosition(lastView);
            //标注2.可见的最后一个itemView完全滑进来了，需要补充新的
            if (lastView.getRight() < getWidth()) {
                View scrap = null;
                //标注3.判断可见的最后一个itemView的索引，
                // 如果是最后一个，则将下一个itemView设置为第一个，否则设置为当前索引的下一个
                if (lastPos == getItemCount() - 1) {
                    if (looperEnable) {
                        scrap = recycler.getViewForPosition(0);
                    } else {
                        dx = 0;
                    }
                } else {
                    scrap = recycler.getViewForPosition(lastPos + 1);
                }
                if (scrap == null) {
                    return dx;
                }
                //标注4.将新的itemViewadd进来并对其测量和布局
                addView(scrap);
                measureChildWithMargins(scrap, 0, 0);
                int width = getDecoratedMeasuredWidth(scrap);
                int height = getDecoratedMeasuredHeight(scrap);
                layoutDecorated(scrap, lastView.getRight(), 0,
                        lastView.getRight() + width, height);
                return dx;
            }
        } else {
            //向右滚动
            View firstView = getChildAt(0);
            if (firstView == null) {
                return 0;
            }
            int firstPos = getPosition(firstView);

            if (firstView.getLeft() >= 0) {
                View scrap = null;
                if (firstPos == 0) {
                    if (looperEnable) {
                        scrap = recycler.getViewForPosition(getItemCount() - 1);
                    } else {
                        dx = 0;
                    }
                } else {
                    scrap = recycler.getViewForPosition(firstPos - 1);
                }
                if (scrap == null) {
                    return 0;
                }
                addView(scrap, 0);
                measureChildWithMargins(scrap, 0, 0);
                int width = getDecoratedMeasuredWidth(scrap);
                int height = getDecoratedMeasuredHeight(scrap);
                layoutDecorated(scrap, firstView.getLeft() - width, 0,
                        firstView.getLeft(), height);
            }
        }
        return dx;
    }

    /**
     * 回收界面不可见的view
     */
    private void recyclerHideView(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view == null) {
                continue;
            }
            if (dx > 0) {
                //标注1.向左滚动，移除左边不在内容里的view
                if (view.getRight() < 0) {
                    removeAndRecycleView(view, recycler);
//                    Log.d(TAG, "循环: 移除 一个view  childCount=" + getChildCount());
                }
            } else {
                //标注2.向右滚动，移除右边不在内容里的view
                if (view.getLeft() > getWidth()) {
                    removeAndRecycleView(view, recycler);
//                    Log.d(TAG, "循环: 移除 一个view  childCount=" + getChildCount());
                }
            }
        }

    }
}
