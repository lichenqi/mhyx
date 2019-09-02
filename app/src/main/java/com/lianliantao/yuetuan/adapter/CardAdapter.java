package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.PosterBean;
import com.lianliantao.yuetuan.port_inner.OnItemClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Context context;
    private List<PosterBean.PosterInfoBean> list;
    private Bitmap qrCodeBitmap;
    private OnItemClick onItemClick, onBigClick;

    public void setOnBigIvClickListener(OnItemClick onBigClick) {
        this.onBigClick = onBigClick;
    }

    public void onclicklistener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public CardAdapter(Context context, List<PosterBean.PosterInfoBean> list, Bitmap qrCodeBitmap) {
        this.context = context;
        this.list = list;
        this.qrCodeBitmap = qrCodeBitmap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getLogo()).into(holder.iv);
        holder.ivqrcode.setImageBitmap(qrCodeBitmap);
        if (list.get(position).isChoose()) {
            holder.iv_choose.setImageResource(R.mipmap.icon_xuanzhong);
        } else {
            holder.iv_choose.setImageResource(R.mipmap.icon_weixuan_no);
        }
        if (onItemClick != null) {
            holder.iv_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.OnItemClickListener(holder.iv_choose, holder.getAdapterPosition());
                }
            });
        }
        if (onBigClick != null) {
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBigClick.OnItemClickListener(holder.iv, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.iv_choose)
        ImageView iv_choose;
        @BindView(R.id.ivqrcode)
        ImageView ivqrcode;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
