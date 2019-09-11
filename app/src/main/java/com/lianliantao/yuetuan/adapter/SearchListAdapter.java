package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.SearchListBean;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchListHolder> {
    private Context context;
    private OnItemClick onItemClick;
    private List<SearchListBean.GoodsInfoBean> list;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public SearchListAdapter(Context context, List<SearchListBean.GoodsInfoBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SearchListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_list_item, parent, false);
        return new SearchListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListHolder holder, int position) {
        Glide.with(context).load(list.get(position).getPictUrl()).into(holder.iv);
        IconAndTextGroupUtil.setTextView(context, holder.tvTitle, list.get(position).getTitle(), list.get(position).getUserType());
        holder.estimateMoney.setText("预估赚 ¥ " + MoneyFormatUtil.StringFormatWithYuan(list.get(position).getEstimatedEarn()));
        String upgradeEarn = MoneyFormatUtil.StringFormatWithYuan(list.get(position).getUpgradeEarn());
        if (upgradeEarn.equals("0")) {
            holder.upgradeMoney.setVisibility(View.GONE);
        } else {
            holder.upgradeMoney.setVisibility(View.VISIBLE);
            holder.upgradeMoney.setText("升级赚 ¥ " + upgradeEarn);
        }
        holder.tvSalePrice.setText(MoneyFormatUtil.StringFormatWithYuan(list.get(position).getPayPrice()));
        holder.tvOldPrice.setText("¥ " + MoneyFormatUtil.StringFormatWithYuan(list.get(position).getZkFinalPrice()));
        holder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.saleNum.setText(list.get(position).getVolume() + "人购买");
        holder.dianpuName.setText(list.get(position).getNick());
        String couponAmount = list.get(position).getCouponAmount();
        if (!TextUtils.isEmpty(couponAmount)) {
            if (Integer.valueOf(couponAmount) > 0) {
                holder.llCoupon.setVisibility(View.VISIBLE);
                holder.coupon_money.setText("¥ " + couponAmount);
            } else {
                holder.llCoupon.setVisibility(View.GONE);
            }
        } else {
            holder.llCoupon.setVisibility(View.GONE);
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

    public class SearchListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        RoundedImageView iv;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.llCoupon)
        LinearLayout llCoupon;
        @BindView(R.id.coupon_money)
        TextView coupon_money;
        @BindView(R.id.saleNum)
        TextView saleNum;
        @BindView(R.id.tvSalePrice)
        TextView tvSalePrice;
        @BindView(R.id.tvOldPrice)
        TextView tvOldPrice;
        @BindView(R.id.dianpuName)
        TextView dianpuName;
        @BindView(R.id.estimateMoney)
        TextView estimateMoney;
        @BindView(R.id.upgradeMoney)
        TextView upgradeMoney;

        public SearchListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
