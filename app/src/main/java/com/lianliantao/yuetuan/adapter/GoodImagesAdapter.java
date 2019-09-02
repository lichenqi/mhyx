package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.lianliantao.yuetuan.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodImagesAdapter extends RecyclerView.Adapter<GoodImagesAdapter.GoodImagesHolder> {
    private Context context;
    private List<String> list;

    public GoodImagesAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GoodImagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.goodimagesadapter, parent, false);
        return new GoodImagesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodImagesHolder holder, int position) {
        final int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        String url = list.get(position);
        if (!url.substring(0, 4).equals("http")) {
            url = "http:" + url;
        }
        Glide.with(context).load(url).asBitmap()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        //原始图片宽高
                        int imageWidth = resource.getWidth();
                        int imageHeight = resource.getHeight();
                        if (imageHeight >0) {
                            //按比例收缩图片
                            float ratio = (float) ((imageWidth * 1.0) / (widthPixels * 1.0));
                            int height = (int) (imageHeight * 1.0 / ratio);
                            ViewGroup.LayoutParams params = holder.iv.getLayoutParams();
                            params.width = widthPixels;
                            params.height = height;
                            holder.iv.setImageBitmap(resource);
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class GoodImagesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv)
        ImageView iv;

        public GoodImagesHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
