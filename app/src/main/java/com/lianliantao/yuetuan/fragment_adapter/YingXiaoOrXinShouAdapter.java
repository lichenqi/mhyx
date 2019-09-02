package com.lianliantao.yuetuan.fragment_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.activity.ImageUrlBigLookActivity;
import com.lianliantao.yuetuan.bean.CircleRecommendBean;
import com.lianliantao.yuetuan.custom_view.CircleImageView;
import com.lianliantao.yuetuan.port_inner.OnItemClick;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YingXiaoOrXinShouAdapter extends RecyclerView.Adapter<YingXiaoOrXinShouAdapter.NyHolder> {

    private Context context;
    private List<CircleRecommendBean.InfoBean> list;
    private OnItemClick onItemClick;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public YingXiaoOrXinShouAdapter(Context context, List<CircleRecommendBean.InfoBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.yingxiaoorxinshouadapter, parent, false);
        return new NyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NyHolder holder, int position) {
        List<CircleRecommendBean.InfoBean.ImgInfoBean> imgInfo = list.get(position).getImgInfo();
        holder.recyclerview.setHasFixedSize(true);
        holder.recyclerview.setLayoutManager(new GridLayoutManager(context, 3));
        NineImageUrlAdapter adapter = new NineImageUrlAdapter(context, imgInfo);
        holder.recyclerview.setAdapter(adapter);
        Glide.with(context).load(list.get(position).getAuthorLogo()).into(holder.iv);
        holder.time.setText(list.get(position).getCreateTime());
        holder.content.setText(list.get(position).getDescription());
        holder.nickName.setText(list.get(position).getAuthor());
        holder.shareButton.setText(list.get(position).getShareNum());
        adapter.setOnClickListener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                Intent intent = new Intent(context, ImageUrlBigLookActivity.class);
                intent.putExtra("imageList", (Serializable) imgInfo);
                intent.putExtra("position", position);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
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
        return list == null ? 0 : list.size();
    }

    public class NyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        CircleImageView iv;
        @BindView(R.id.nickName)
        TextView nickName;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.recyclerview)
        RecyclerView recyclerview;
        @BindView(R.id.shareButton)
        TextView shareButton;

        public NyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
