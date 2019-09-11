package com.lianliantao.yuetuan.fragment_adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.HomeListBean;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.NumUtil;
import com.lianliantao.yuetuan.util.TextViewUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeOtherHeadAdapter extends RecyclerView.Adapter<HomeOtherHeadAdapter.HomeOtherHeadHolder> {
    private OnItemClick onItemClick;
    private Context context;
    private List<HomeListBean.GoodsInfoBean> list;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public HomeOtherHeadAdapter(Context context, List<HomeListBean.GoodsInfoBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HomeOtherHeadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.homeotherheadadapter, parent, false);
        return new HomeOtherHeadHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeOtherHeadHolder holder, int position) {
        Glide.with(context).load(list.get(position).getPictUrl()).into(holder.iv);
        String sales_hours = "今日销量： " + NumUtil.getNum(list.get(position).getSales()) + " 单";
        TextViewUtil.setTextViewColorAndSize(sales_hours, holder.tvSaleNum);
        switch (position) {
            case 0:
                holder.ivSaleIcon.setImageResource(R.mipmap.sale_one);
                break;
            case 1:
                holder.ivSaleIcon.setImageResource(R.mipmap.sale_two);
                break;
            case 2:
                holder.ivSaleIcon.setImageResource(R.mipmap.sale_three);
                break;
        }
        holder.tvTitle.setText(list.get(position).getTitle());
        String couponAmount = list.get(position).getCouponAmount();
        if (!TextUtils.isEmpty(couponAmount)) {
            if (Integer.valueOf(couponAmount) > 0) {
                holder.llCoupon.setVisibility(View.VISIBLE);
                holder.couponMoney.setText("¥ " + couponAmount);
            } else {
                holder.llCoupon.setVisibility(View.GONE);
            }
        } else {
            holder.llCoupon.setVisibility(View.GONE);
        }
        holder.tvSalePrice.setText(MoneyFormatUtil.StringFormatWithYuan(list.get(position).getPayPrice()));
        holder.tvOldPrice.setText("¥ " + MoneyFormatUtil.StringFormatWithYuan(list.get(position).getZkFinalPrice()));
        holder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.estimateMoney.setText("预估赚 ¥ " + MoneyFormatUtil.StringFormatWithYuan(list.get(position).getEstimatedEarn()));
        String upgradeEarn = MoneyFormatUtil.StringFormatWithYuan(list.get(position).getUpgradeEarn());
        if (upgradeEarn.equals("0")) {
            holder.upgradeMoney.setVisibility(View.GONE);
        } else {
            holder.upgradeMoney.setVisibility(View.VISIBLE);
            holder.upgradeMoney.setText("升级赚 ¥ " + upgradeEarn);
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
        return list == null ? 0 : 3;
    }

    public class HomeOtherHeadHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv)
        RoundedImageView iv;
        @BindView(R.id.ivSaleIcon)
        ImageView ivSaleIcon;
        @BindView(R.id.tvSaleNum)
        TextView tvSaleNum;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tv_coupon_type)
        TextView tvCouponType;
        @BindView(R.id.coupon_money)
        TextView couponMoney;
        @BindView(R.id.llCoupon)
        LinearLayout llCoupon;
        @BindView(R.id.tvSalePrice)
        TextView tvSalePrice;
        @BindView(R.id.tvOldPrice)
        TextView tvOldPrice;
        @BindView(R.id.ll_sale_price)
        LinearLayout llSalePrice;
        @BindView(R.id.estimateMoney)
        TextView estimateMoney;
        @BindView(R.id.upgradeMoney)
        TextView upgradeMoney;
        @BindView(R.id.re_bottom)
        RelativeLayout reBottom;

        public HomeOtherHeadHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
