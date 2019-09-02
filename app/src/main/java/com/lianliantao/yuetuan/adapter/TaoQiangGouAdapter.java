package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.graphics.Paint;
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
import com.lianliantao.yuetuan.bean.HomeListBean;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.NumUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaoQiangGouAdapter extends RecyclerView.Adapter<TaoQiangGouAdapter.HomeOtherListHolder> {
    private OnItemClick onItemClick;
    private Context context;
    private List<HomeListBean.GoodsInfoBean> list;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public TaoQiangGouAdapter(Context context, List<HomeListBean.GoodsInfoBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HomeOtherListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_other_list_item, parent, false);
        return new HomeOtherListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeOtherListHolder holder, int position) {
        Glide.with(context).load(list.get(position).getPictUrl()).into(holder.iv);
        IconAndTextGroupUtil.setTextView(context, holder.title, list.get(position).getTitle(), list.get(position).getUserType());
        holder.estimateMoney.setText("预估赚 ¥ " + list.get(position).getEstimatedEarn());
        holder.upgradeMoney.setText("升级赚 ¥ " + list.get(position).getUpgradeEarn());
        holder.tv_price.setText(MoneyFormatUtil.StringFormatWithYuan(list.get(position).getPayPrice()));
        holder.old_price.setText("¥ " + MoneyFormatUtil.StringFormatWithYuan(list.get(position).getZkFinalPrice()));
        holder.old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        String couponAmount = list.get(position).getCouponAmount();
        if (!TextUtils.isEmpty(couponAmount)) {
            if (Integer.valueOf(couponAmount) > 0) {
                holder.tvCoupon.setVisibility(View.VISIBLE);
                holder.tvCoupon.setText(couponAmount + "元券");
            } else {
                holder.tvCoupon.setVisibility(View.GONE);
            }
        } else {
            holder.tvCoupon.setVisibility(View.GONE);
        }
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

    public class HomeOtherListHolder extends RecyclerView.ViewHolder {

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
        @BindView(R.id.tvCoupon)
        TextView tvCoupon;
        @BindView(R.id.sale_num)
        TextView sale_num;

        public HomeOtherListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
