package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.LevelCentyBean;
import com.lianliantao.yuetuan.custom_view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserImageIvListAdapter extends RecyclerView.Adapter<UserImageIvListAdapter.MyHolder> {

    private Context context;
    private List<LevelCentyBean.UpgradeUserBean> otherUserList;

    public UserImageIvListAdapter(Context context, List<LevelCentyBean.UpgradeUserBean> otherUserList) {
        this.context = context;
        this.otherUserList = otherUserList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userimageivlistadapter, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Glide.with(context).load(otherUserList.get(position).getAvatar()).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return otherUserList == null ? 0 : otherUserList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv)
        CircleImageView iv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
