package com.lianliantao.yuetuan.custom_view;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.bean.HomeHeadBean;
import com.makeramen.roundedimageview.RoundedImageView;
import com.youth.banner.loader.ImageLoader;

public class MyBannerRoundImageView extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(((HomeHeadBean.BannerInfoBean) path).getLogo()).into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        RoundedImageView rv = new RoundedImageView(context);
        rv.setScaleType(ImageView.ScaleType.FIT_XY);
        rv.setCornerRadius(20);
        rv.setPadding(30, 0, 30, 0);
        return rv;
    }
}
