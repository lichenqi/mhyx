package com.lianliantao.yuetuan.lazy_base_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/*赖加载基类*/
public abstract class LazyBaseFragment extends Fragment {

    /*标志位  判断数据是否初始化*/
    private boolean isInitData = false;
    /*标志位  判断fragment是否初始化*/
    private boolean isVisibleToUser = false;
    /*标志位  判断view已经加载完成  避免空指针操作*/
    private boolean isPrepareView = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepareView = true;/*此时view已经加载完成 设置为true*/
    }

    //fragment生命周期中onViewCreated之后的方法 在这里调用一次懒加载 避免第一次可见不加载数据
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lazyInitData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;/*将fragmen是否可见值赋给标志位isVisibleToUser*/
        lazyInitData();/*实现赖加载*/
    }

    /*fragment显示隐藏监听*/
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            lazyInitData();
        }
    }

    /*赖加载方法*/
    private void lazyInitData() {
        if (setFragmentTarget()) {
            if (!isInitData && isVisibleToUser && isPrepareView) {/*数据未初始化 fragment已经可见 view已经创建完成*/
                initData();/*加载数据*/
                isInitData = true;//是否已经加载数据标志重新赋值为true
            }
        } else {
            if (!isInitData && isVisibleToUser && isPrepareView) {//数据还没有被加载过，fragment已经可见，view已经加载完成
                initData();
                isInitData = true;
            } else if (!isInitData && getParentFragment() == null && isPrepareView) {
                initData();
                isInitData = true;
            }
        }
    }

    /*加载数据的方法  由子类去实现*/
    protected abstract void initData();

    /*由子类实现 返回子类的布局id*/
    protected abstract int getLayoutId();

    /*设置fragment tag 由子类实现*/
    protected abstract boolean setFragmentTarget();
}
