package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.ShareSelectBean;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HorizontalImagesAdapter extends RecyclerView.Adapter<HorizontalImagesAdapter.MyHolder> {

    private Context context;
    private List<ShareSelectBean> list;
    private OnItemClick onSelectItem, onIvItem;

    public void setOnselectClickListener(OnItemClick onSelectItem) {
        this.onSelectItem = onSelectItem;
    }

    public void setOnIvClickListener(OnItemClick onIvItem) {
        this.onIvItem = onIvItem;
    }

    public HorizontalImagesAdapter(Context context, List<ShareSelectBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.horizontalimagesadapter, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Glide.with(context).load(list.get(position).getUrl()).into(holder.iv);
        if (list.get(position).isSelected()) {
            holder.iv_check.setImageResource(R.mipmap.icon_xuanzhong);
        } else {
            holder.iv_check.setImageResource(R.mipmap.share_unselect);
        }
        if (onSelectItem != null) {
            holder.iv_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectItem.OnItemClickListener(holder.iv_check, holder.getAdapterPosition());
                }
            });
        }
        if (onIvItem != null) {
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onIvItem.OnItemClickListener(holder.iv, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv)
        RoundedImageView iv;
        @BindView(R.id.iv_check)
        ImageView iv_check;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
