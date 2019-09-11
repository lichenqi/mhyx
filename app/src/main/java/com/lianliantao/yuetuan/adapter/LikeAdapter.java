package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.LikeBean;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.NumUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.LikeHolder> {
    private Context context;
    private List<LikeBean.LikeInfoBean> list;
    private OnItemClick onItemClick;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public LikeAdapter(Context context, List<LikeBean.LikeInfoBean> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public LikeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.likeadapter, parent, false);
        return new LikeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikeHolder holder, int position) {
        Glide.with(context).load(list.get(position).getPictUrl()).into(holder.iv);
        IconAndTextGroupUtil.setTextView(context, holder.title, list.get(position).getTitle(), list.get(position).getUserType());
        holder.estimateMoney.setText("预估赚 ¥ " + MoneyFormatUtil.StringFormatWithYuan(list.get(position).getEstimatedEarn()));
        holder.tv_price.setText(MoneyFormatUtil.StringFormatWithYuan(list.get(position).getPayPrice()));
        String upgradeEarn = MoneyFormatUtil.StringFormatWithYuan(list.get(position).getUpgradeEarn());
        if (upgradeEarn.equals("0")) {
            holder.upgradeMoney.setVisibility(View.GONE);
        } else {
            holder.upgradeMoney.setVisibility(View.VISIBLE);
            holder.upgradeMoney.setText("升级赚 ¥ " + upgradeEarn);
        }
        holder.old_price.setText("¥ " + MoneyFormatUtil.StringFormatWithYuan(list.get(position).getZkFinalPrice()));
        holder.old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.coupon_money.setText("¥ " + list.get(position).getCouponAmount());
        holder.sale_num.setText(NumUtil.getNum(list.get(position).getVolume()) + "人购买");
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

    public class LikeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.estimateMoney)
        TextView estimateMoney;
        @BindView(R.id.upgradeMoney)
        TextView upgradeMoney;
        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.old_price)
        TextView old_price;
        @BindView(R.id.coupon_money)
        TextView coupon_money;
        @BindView(R.id.sale_num)
        TextView sale_num;

        public LikeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
