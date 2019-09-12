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
import com.lianliantao.yuetuan.custom_view.ScrollingDigitalAnimation;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.NumUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChuDanAdapter extends RecyclerView.Adapter<ChuDanAdapter.ChuDanHolder> {
    private Context context;
    private List<HomeListBean.GoodsInfoBean> list;
    private OnItemClick onItemClick;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public ChuDanAdapter(Context context, List<HomeListBean.GoodsInfoBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChuDanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chudan_item, parent, false);
        return new ChuDanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChuDanHolder holder, int position) {
        HomeListBean.GoodsInfoBean bean = list.get(position);
        Glide.with(context).load(bean.getPictUrl()).into(holder.iv);
        IconAndTextGroupUtil.setTextView(context, holder.title, bean.getTitle(), bean.getUserType());
        holder.estimateMoney.setText("预估赚 ¥ " + MoneyFormatUtil.StringFormatWithYuan(bean.getEstimatedEarn()));
        String upgradeEarn = MoneyFormatUtil.StringFormatWithYuan(bean.getUpgradeEarn());
        if (upgradeEarn.equals("0")) {
            holder.upgradeMoney.setVisibility(View.GONE);
        } else {
            holder.upgradeMoney.setVisibility(View.VISIBLE);
            holder.upgradeMoney.setText("升级赚 ¥ " + upgradeEarn);
        }
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
        holder.dnView.setNumberString(bean.getSales());
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

    public class ChuDanHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        RoundedImageView iv;
        @BindView(R.id.title)
        TextView title;
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
        @BindView(R.id.dnView)
        ScrollingDigitalAnimation dnView;

        public ChuDanHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
