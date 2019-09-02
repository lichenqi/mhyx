package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.TeamMemberBean;
import com.lianliantao.yuetuan.custom_view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DirectlyMemberAdapter extends RecyclerView.Adapter<DirectlyMemberAdapter.MyHolder> {
    private Context context;
    private List<TeamMemberBean.TeamListBean> list;

    public DirectlyMemberAdapter(Context context, List<TeamMemberBean.TeamListBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.directlymemberadapter, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Glide.with(context).load(list.get(position).getAvatar()).into(holder.iv);
        String level = list.get(position).getLevel();
        holder.name.setText(list.get(position).getNickname());
        String mobile = list.get(position).getMobile();
        if (!TextUtils.isEmpty(mobile)) {
            String start = mobile.substring(0, 3);
            String end = mobile.substring(8, mobile.length());
            holder.phone.setText("(" + start + "****" + end + ")");
        }
        holder.time.setText(list.get(position).getCreateTime());
        holder.peoples.setText(list.get(position).getDirectMember());
        holder.orders.setText(list.get(position).getMyEarn());
        switch (level) {
            case "1":
                holder.ivLevel.setImageResource(R.mipmap.orang_one);
                break;
            case "2":
                holder.ivLevel.setImageResource(R.mipmap.orang_two);
                break;
            case "3":
                holder.ivLevel.setImageResource(R.mipmap.orang_three);
                break;
            case "4":
                holder.ivLevel.setImageResource(R.mipmap.orang_four);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        CircleImageView iv;
        @BindView(R.id.ivLevel)
        ImageView ivLevel;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.phone)
        TextView phone;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.peoples)
        TextView peoples;
        @BindView(R.id.orders)
        TextView orders;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
