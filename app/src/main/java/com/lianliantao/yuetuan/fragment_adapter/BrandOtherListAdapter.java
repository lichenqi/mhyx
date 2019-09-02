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
import com.lianliantao.yuetuan.bean.OtherListBean;
import com.lianliantao.yuetuan.custom_view.CircleImageView;
import com.lianliantao.yuetuan.port_inner.OnItemClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrandOtherListAdapter extends RecyclerView.Adapter<BrandOtherListAdapter.BrandOtherListHolder> {

    private Context context;
    private List<OtherListBean.BrandInfoBean> list;
    private OnItemClick onItemClick;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public BrandOtherListAdapter(Context context, List<OtherListBean.BrandInfoBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BrandOtherListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.brandotherlistadapter_item, parent, false);
        return new BrandOtherListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandOtherListHolder holder, int position) {
        Glide.with(context).load(list.get(position).getLogo()).into(holder.iv);
        holder.title.setText(list.get(position).getBrandName());
        holder.tvFanYong.setText(list.get(position).getRebateRate());
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

    public class BrandOtherListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        CircleImageView iv;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.tvFanYong)
        TextView tvFanYong;

        public BrandOtherListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
