package com.lianliantao.yuetuan.home_classic;

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
import com.lianliantao.yuetuan.bean.LikeBean;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.NumUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaoBaoLikeAdapter extends RecyclerView.Adapter<TaoBaoLikeAdapter.MyHolder> {

    private Context context;
    private List<LikeBean.LikeInfoBean> list;
    private OnItemClick onItemClick;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public TaoBaoLikeAdapter(Context context, List<LikeBean.LikeInfoBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_other_list_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Glide.with(context).load(list.get(position).getPictUrl()).into(holder.iv);
        IconAndTextGroupUtil.setTextView(context, holder.title, list.get(position).getTitle(), list.get(position).getUserType());
        holder.estimateMoney.setText("预估赚 ¥ " + MoneyFormatUtil.StringFormatWithYuan(list.get(position).getEstimatedEarn()));
        String upgradeEarn = MoneyFormatUtil.StringFormatWithYuan(list.get(position).getUpgradeEarn());
        if (upgradeEarn.equals("0")) {
            holder.upgradeMoney.setVisibility(View.GONE);
        } else {
            holder.upgradeMoney.setVisibility(View.VISIBLE);
            holder.upgradeMoney.setText("升级赚 ¥ " + upgradeEarn);
        }
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

    public class MyHolder extends RecyclerView.ViewHolder {
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

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
