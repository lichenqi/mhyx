package com.lianliantao.yuetuan.view;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

public class RoundBannerLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //实用你自己的图片加载框架加载图片
        Glide.with(context).load(String.valueOf(path)).into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        ImageView rv = new ImageView(context);
        rv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return rv;
    }

}
