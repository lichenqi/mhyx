package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.TixianRecordBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TiXianRecordAdapter extends RecyclerView.Adapter<TiXianRecordAdapter.MyHolder> {

    private Context context;
    private List<TixianRecordBean.WithdrawInfoBean> list;

    public TiXianRecordAdapter(List<TixianRecordBean.WithdrawInfoBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tixianrecordadapter, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        TixianRecordBean.WithdrawInfoBean bean = list.get(position);
        holder.name.setText(bean.getNickName());
        holder.phone.setText(bean.getMobile());
        String status = bean.getStatus();
        if (status.equals("1")) {
            holder.ivChange.setImageResource(R.mipmap.txjl_you_iconyitixian);
            holder.viewline.setVisibility(View.GONE);
            holder.reason.setVisibility(View.GONE);
        } else {
            holder.ivChange.setImageResource(R.mipmap.txjl_you_iconyijujue);
            holder.viewline.setVisibility(View.VISIBLE);
            holder.reason.setVisibility(View.VISIBLE);
        }
        holder.time.setText(bean.getCreateTime());
        holder.money.setText("¥ " + bean.getMoney());
        holder.reason.setText("拒绝理由： " + bean.getReason());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.money)
        TextView money;
        @BindView(R.id.phone)
        TextView phone;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.ivChange)
        ImageView ivChange;
        @BindView(R.id.reason)
        TextView reason;
        @BindView(R.id.viewline)
        View viewline;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
