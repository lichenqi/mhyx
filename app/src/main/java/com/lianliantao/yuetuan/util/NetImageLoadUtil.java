package com.lianliantao.yuetuan.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/*网络图片加载工具*/
public class NetImageLoadUtil {
    public static void loadImage(String url, Context context, ImageView view) {
//        if (TextUtils.isEmpty( url )) {
//            view.setImageResource( R.drawable.loading_img );
//        } else {
        if (url.contains("alicdn") || url.contains("tbcdn") || url.contains("taobaocdn")) {
            String small_url = url + "_400x400.jpg";
            Glide.with(context).load(small_url).dontAnimate().into(view);
        } else {
            Glide.with(context).load(url).dontAnimate().into(view);
        }
//        }
    }
}
