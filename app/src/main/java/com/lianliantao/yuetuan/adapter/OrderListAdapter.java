package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.OrderListBean;
import com.lianliantao.yuetuan.custom_view.CircleImageView;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListHolder> {

    private Context context;
    private List<OrderListBean.OrderInfoBean> list;
    private OnItemClick onItemClick;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public OrderListAdapter(Context context, List<OrderListBean.OrderInfoBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orderlistadapter, parent, false);
        return new OrderListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListHolder holder, int position) {
        Glide.with(context).load(list.get(position).getAvatar()).into(holder.iv);
        holder.name.setText(list.get(position).getNickName());
        holder.time.setText(list.get(position).getCreateTime());
        String tkStatus = list.get(position).getTkStatus();
        Glide.with(context).load(list.get(position).getPictUrl()).into(holder.image);
        holder.title.setText(list.get(position).getItemTitle());
        holder.price.setText(list.get(position).getAlipayTotalPrice());
        holder.yuguzhuan.setText("预估赚 ¥ " + list.get(position).getSeller());
        holder.orderNum.setText("订单编号： " + list.get(position).getTradeId());
        switch (tkStatus) {
            case "3":
                holder.orderStatus.setText("已结算");
                break;
            case "12":
                holder.orderStatus.setText("已付款");
                break;
            case "13":
                holder.orderStatus.setText("已失效");
                break;
            case "14":
                holder.orderStatus.setText("已收货");
                break;
        }
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

    public class OrderListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        CircleImageView iv;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.orderStatus)
        TextView orderStatus;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.yuguzhuan)
        TextView yuguzhuan;
        @BindView(R.id.orderNum)
        TextView orderNum;
        @BindView(R.id.image)
        RoundedImageView image;

        public OrderListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
