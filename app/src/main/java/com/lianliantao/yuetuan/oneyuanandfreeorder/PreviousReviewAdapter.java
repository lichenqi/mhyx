package com.lianliantao.yuetuan.oneyuanandfreeorder;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviousReviewAdapter extends RecyclerView.Adapter<PreviousReviewAdapter.MyHolder> {

    private Context context;
    private List<TodayFreeBean.FreeGoodsInfoOldBean> list;
    private OnItemClick onItemClick;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public PreviousReviewAdapter(Context context, List<TodayFreeBean.FreeGoodsInfoOldBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.previousreviewadapter, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        TodayFreeBean.FreeGoodsInfoOldBean bean = list.get(position);
        if (position == 0) {
            holder.reType.setVisibility(View.VISIBLE);
        } else {
            holder.reType.setVisibility(View.GONE);
        }
        Glide.with(context).load(bean.getPictUrl()).into(holder.iv);
        IconAndTextGroupUtil.setTextView(context, holder.title, bean.getTitle(), bean.getUserType());
        holder.tvSalePrice.setText(MoneyFormatUtil.StringFormatWithYuan(bean.getPayPrice()));
        holder.tvOldPrice.setText("¥" + MoneyFormatUtil.StringFormatWithYuan(bean.getZkFinalPrice()));
        holder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.money.setText("付款" + MoneyFormatUtil.StringFormatWithYuan(bean.getPayPrice()) + "元，平台补贴" + bean.getSubsidyPrice() + "元");
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
        @BindView(R.id.reType)
        RelativeLayout reType;
        @BindView(R.id.iv)
        RoundedImageView iv;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.tvSalePrice)
        TextView tvSalePrice;
        @BindView(R.id.tvOldPrice)
        TextView tvOldPrice;
        @BindView(R.id.money)
        TextView money;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
