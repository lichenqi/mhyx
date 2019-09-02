package com.lianliantao.yuetuan.fragment_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.BrandBean;
import com.lianliantao.yuetuan.custom_view.CircleImageView;
import com.lianliantao.yuetuan.port_inner.OnItemClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrandHeadAdapter extends RecyclerView.Adapter<BrandHeadAdapter.BrandHeadHolder> {

    private Context context;
    private List<BrandBean.BrandInfoBean> brandInfoList;
    private OnItemClick onItemClick;

    public void setOnClickListerner(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public BrandHeadAdapter(Context context, List<BrandBean.BrandInfoBean> brandInfoList) {
        this.context = context;
        this.brandInfoList = brandInfoList;
    }

    @NonNull
    @Override
    public BrandHeadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.brandheadadapter_item, parent, false);
        return new BrandHeadHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandHeadHolder holder, int position) {
        Glide.with(context).load(brandInfoList.get(position).getLogo()).into(holder.iv);
        holder.title.setText(brandInfoList.get(position).getBrandName());
        holder.tvFanYong.setText(brandInfoList.get(position).getRebateRate());
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
        return brandInfoList == null ? 0 : brandInfoList.size();
    }

    public class BrandHeadHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv)
        CircleImageView iv;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.tvFanYong)
        TextView tvFanYong;

        public BrandHeadHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
