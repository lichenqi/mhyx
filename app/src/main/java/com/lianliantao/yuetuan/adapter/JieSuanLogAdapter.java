package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.JieSuanBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JieSuanLogAdapter extends RecyclerView.Adapter<JieSuanLogAdapter.MyHolder> {

    private Context context;
    private List<JieSuanBean.SettlementLogBean> list;

    public JieSuanLogAdapter(Context context, List<JieSuanBean.SettlementLogBean> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.jiesuanlogadapter, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        JieSuanBean.SettlementLogBean bean = list.get(position);
        holder.name.setText(bean.getNickName());
        holder.phone.setText(bean.getCreateTime());
        holder.money_one.setText(bean.getSettlementMoney());
        holder.money_two.setText(bean.getActualMoney());
        holder.fuwu.setText("服务费： ¥ " + bean.getServiceFee() + "    额外补贴： ¥ " + bean.getSubsidyAdd());
        holder.beizhu.setText("备注： " + bean.getRemarks());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.beizhu)
        TextView beizhu;
        @BindView(R.id.fuwu)
        TextView fuwu;
        @BindView(R.id.money_two)
        TextView money_two;
        @BindView(R.id.money_one)
        TextView money_one;
        @BindView(R.id.phone)
        TextView phone;
        @BindView(R.id.name)
        TextView name;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
