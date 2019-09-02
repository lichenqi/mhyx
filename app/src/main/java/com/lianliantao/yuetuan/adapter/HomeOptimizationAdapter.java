package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.SearchListBean;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.NumUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeOptimizationAdapter extends RecyclerView.Adapter<HomeOptimizationAdapter.MyHolder> {
    private Context context;
    private List<SearchListBean.GoodsInfoBean> list;
    private OnItemClick onItemClick;

    public void setonclicklistener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public HomeOptimizationAdapter(List<SearchListBean.GoodsInfoBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.homeoptimizationadapter, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Glide.with(context).load(list.get(position).getPictUrl()).into(holder.iv);
        IconAndTextGroupUtil.setTextView(context, holder.title, list.get(position).getTitle(), list.get(position).getUserType());
        holder.assistantTitle.setText(list.get(position).getItemDescription());
        holder.estimateMoney.setText("预估赚 ¥ " + list.get(position).getEstimatedEarn());
        holder.upgradeMoney.setText("升级赚 ¥ " + list.get(position).getUpgradeEarn());
        holder.tvSalePrice.setText(MoneyFormatUtil.StringFormatWithYuan(list.get(position).getPayPrice()));
        holder.tvOldPrice.setText("¥" + MoneyFormatUtil.StringFormatWithYuan(list.get(position).getZkFinalPrice()));
        holder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
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
        String volume = list.get(position).getVolume();
        String couponRemainCount = list.get(position).getCouponRemainCount();
        String couponTotalCount = list.get(position).getCouponTotalCount();
        Double progress = Double.valueOf(Integer.valueOf(couponTotalCount) - Integer.valueOf(couponRemainCount)) / Double.valueOf(couponTotalCount) * 100;
        holder.progressBar.setProgress(progress.intValue());
        holder.saleNum.setText("已售" + NumUtil.getNum(volume) + "件");
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
        @BindView(R.id.estimateMoney)
        TextView estimateMoney;
        @BindView(R.id.upgradeMoney)
        TextView upgradeMoney;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.saleNum)
        TextView saleNum;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
