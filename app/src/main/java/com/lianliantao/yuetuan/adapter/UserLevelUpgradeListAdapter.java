package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.LevelCentyBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserLevelUpgradeListAdapter extends RecyclerView.Adapter<UserLevelUpgradeListAdapter.MyHolder> {

    private Context context;
    private List<LevelCentyBean.UpgradeConditionBean> upgradeCondition;

    public UserLevelUpgradeListAdapter(Context context, List<LevelCentyBean.UpgradeConditionBean> upgradeCondition) {
        this.context = context;
        this.upgradeCondition = upgradeCondition;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userlevelupgradelistadapter, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        LevelCentyBean.UpgradeConditionBean bean = upgradeCondition.get(position);
        String rank = bean.getRank();
        String total = bean.getTotal();
        String remain = bean.getRemain();
        String unit = bean.getUnit();
        holder.content.setText(rank + "： " + total + unit);
        holder.num.setText(remain);
        holder.totalNum.setText("/" + total + unit);
        Double progress = Double.valueOf(remain) / Double.valueOf(total);
        holder.progressBar.setProgress(progress.intValue());
    }

    @Override
    public int getItemCount() {
        return upgradeCondition == null ? 0 : upgradeCondition.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.num)
        TextView num;
        @BindView(R.id.totalNum)
        TextView totalNum;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
