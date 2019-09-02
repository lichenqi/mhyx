package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class CollectListAdapter extends RecyclerView.Adapter<CollectListAdapter.CollectListHolder> {

    private Context context;
    private List<HomeListBean.GoodsInfoBean> list;
    private OnItemClick onItemClick, onDeleteClick;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setOnDeleteCollectLisrtener(OnItemClick onDeleteClick) {
        this.onDeleteClick = onDeleteClick;
    }

    public CollectListAdapter(List<HomeListBean.GoodsInfoBean> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CollectListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.collectlistadapter, parent, false);
        return new CollectListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectListHolder holder, int position) {
        Glide.with(context).load(list.get(position).getPictUrl()).into(holder.iv);
        IconAndTextGroupUtil.setTextView(context, holder.tvTitle, list.get(position).getTitle(), list.get(position).getUserType());
        holder.estimateMoney.setText("预估赚 ¥ " + MoneyFormatUtil.StringFormatWithYuan(list.get(position).getEstimatedEarn()));
        holder.upgradeMoney.setText("升级赚 ¥ " + MoneyFormatUtil.StringFormatWithYuan(list.get(position).getUpgradeEarn()));
        holder.tvSalePrice.setText(MoneyFormatUtil.StringFormatWithYuan(list.get(position).getPayPrice()));
        holder.tvOldPrice.setText("¥ " + MoneyFormatUtil.StringFormatWithYuan(list.get(position).getZkFinalPrice()));
        holder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.saleNum.setText(NumUtil.getNum(list.get(position).getVolume()) + "人购买");
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
        if (onItemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.OnItemClickListener(holder.itemView, holder.getAdapterPosition());
                }
            });
        }
        if (onDeleteClick != null) {
            holder.reChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClick.OnItemClickListener(holder.reChoose, holder.getAdapterPosition());
                }
            });
        }
        if (list.get(position).isDeleteShow()) {
            holder.reChoose.setVisibility(View.VISIBLE);
        } else {
            holder.reChoose.setVisibility(View.GONE);
        }
        if (list.get(position).isChecked()) {
            holder.ivChoose.setImageResource(R.mipmap.collect_checked);
        } else {
            holder.ivChoose.setImageResource(R.mipmap.collect_unchecked);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class CollectListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reChoose)
        RelativeLayout reChoose;
        @BindView(R.id.ivChoose)
        ImageView ivChoose;
        @BindView(R.id.iv)
        RoundedImageView iv;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvCoupon)
        TextView tvCoupon;
        @BindView(R.id.saleNum)
        TextView saleNum;
        @BindView(R.id.tvSalePrice)
        TextView tvSalePrice;
        @BindView(R.id.tvOldPrice)
        TextView tvOldPrice;
        @BindView(R.id.estimateMoney)
        TextView estimateMoney;
        @BindView(R.id.upgradeMoney)
        TextView upgradeMoney;

        public CollectListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
