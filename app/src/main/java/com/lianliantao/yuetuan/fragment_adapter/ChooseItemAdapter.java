package com.lianliantao.yuetuan.fragment_adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.BrandChooseBean;
import com.lianliantao.yuetuan.myutil.JumpUtil;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseItemAdapter extends RecyclerView.Adapter<ChooseItemAdapter.ChooseViewHolder> {

    private Context context;
    private List<BrandChooseBean.BannerInfoBean.GoodsInfoBean> goodsInfoList;
    int width;

    public ChooseItemAdapter(Context context, List<BrandChooseBean.BannerInfoBean.GoodsInfoBean> goodsInfoList) {
        this.context = context;
        this.goodsInfoList = goodsInfoList;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        width = (displayMetrics.widthPixels - DensityUtils.dip2px(context, 52)) / 3;
    }

    @NonNull
    @Override
    public ChooseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chooseitemadapter, parent, false);
        return new ChooseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseViewHolder holder, int position) {
        Glide.with(context).load(goodsInfoList.get(position).getPictUrl()).into(holder.iv);
        holder.price.setText("¥ " + MoneyFormatUtil.StringFormatWithYuan(goodsInfoList.get(position).getPayPrice()));
        String estimatedEarn = "预估赚 ¥ " + MoneyFormatUtil.StringFormatWithYuan(goodsInfoList.get(position).getEstimatedEarn());
        holder.yuguzhuan.setText(estimatedEarn);
        String couponAmount = goodsInfoList.get(position).getCouponAmount();
        if (TextUtils.isEmpty(couponAmount)) {
            holder.tvCoupon.setVisibility(View.GONE);
        } else {
            holder.tvCoupon.setVisibility(View.VISIBLE);
            holder.tvCoupon.setText(couponAmount + "元券");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.jump2ShopDetail(context, goodsInfoList.get(position).getItemId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return goodsInfoList == null ? 0 : goodsInfoList.size();
    }

    public class ChooseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_parent)
        LinearLayout ll_parent;
        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.yuguzhuan)
        TextView yuguzhuan;
        @BindView(R.id.tvCoupon)
        TextView tvCoupon;

        public ChooseViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
