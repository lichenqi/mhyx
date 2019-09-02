package com.lianliantao.yuetuan.fragment_adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.CircleRecommendBean;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.DensityUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NineImageUrlAdapter extends RecyclerView.Adapter<NineImageUrlAdapter.MyHolder> {

    private Context context;
    private DisplayMetrics displayMetrics;
    private int width;
    private List<CircleRecommendBean.InfoBean.ImgInfoBean> imgInfo;
    private OnItemClick onItemClick;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public NineImageUrlAdapter(Context context, List<CircleRecommendBean.InfoBean.ImgInfoBean> imgInfo) {
        this.context = context;
        this.imgInfo = imgInfo;
        displayMetrics = context.getResources().getDisplayMetrics();
        int dip2px = DensityUtils.dip2px(context, 97);
        width = (displayMetrics.widthPixels - dip2px) / 3;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.nineimageurladapter, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ViewGroup.LayoutParams layoutParamsIv = holder.iv.getLayoutParams();
        ViewGroup.LayoutParams layoutParamsRe = holder.reparent.getLayoutParams();
        layoutParamsIv.width = width;
        layoutParamsIv.height = width;
        layoutParamsRe.width = width;
        layoutParamsRe.height = width;
        holder.iv.setLayoutParams(layoutParamsIv);
        holder.reparent.setLayoutParams(layoutParamsRe);
        Glide.with(context).load(imgInfo.get(position).getUrl()).into(holder.iv);
        if (onItemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.OnItemClickListener(holder.itemView, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return imgInfo == null ? 0 : imgInfo.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reparent)
        RelativeLayout reparent;
        @BindView(R.id.iv)
        ImageView iv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
