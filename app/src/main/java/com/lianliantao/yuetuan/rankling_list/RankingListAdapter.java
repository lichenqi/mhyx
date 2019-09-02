package com.lianliantao.yuetuan.rankling_list;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankingListAdapter extends RecyclerView.Adapter<RankingListAdapter.MyHolder> {
    private Context context;
    private List<HomeListBean.GoodsInfoBean> list;
    private OnItemClick onItemClick;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public RankingListAdapter(Context context, List<HomeListBean.GoodsInfoBean> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ranking_list_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        HomeListBean.GoodsInfoBean bean = list.get(position);
        Glide.with(context).load(bean.getPictUrl()).into(holder.iv);
        IconAndTextGroupUtil.setTextView(context, holder.title, bean.getTitle(), bean.getUserType());
        holder.assistantTitle.setText(bean.getItemDescription());
        holder.estimateMoney.setText("预估赚 ¥ " + bean.getEstimatedEarn());
        holder.upgradeMoney.setText("升级赚 ¥ " + bean.getUpgradeEarn());
        holder.tvSalePrice.setText(MoneyFormatUtil.StringFormatWithYuan(bean.getPayPrice()));
        holder.tvOldPrice.setText("¥" + MoneyFormatUtil.StringFormatWithYuan(bean.getZkFinalPrice()));
        holder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        String couponAmount = bean.getCouponAmount();
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
        String volume = bean.getVolume();
        holder.saleNum.setText(NumUtil.getNum(volume) + "人购买");
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

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        RoundedImageView iv;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.assistantTitle)
        TextView assistantTitle;
        @BindView(R.id.tvSalePrice)
        TextView tvSalePrice;
        @BindView(R.id.tvOldPrice)
        TextView tvOldPrice;
        @BindView(R.id.tvCoupon)
        TextView tvCoupon;
        @BindView(R.id.saleNum)
        TextView saleNum;
        @BindView(R.id.estimateMoney)
        TextView estimateMoney;
        @BindView(R.id.upgradeMoney)
        TextView upgradeMoney;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
