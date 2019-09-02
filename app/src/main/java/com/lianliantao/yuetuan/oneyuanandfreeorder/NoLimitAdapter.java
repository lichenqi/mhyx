package com.lianliantao.yuetuan.oneyuanandfreeorder;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.NumUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoLimitAdapter extends RecyclerView.Adapter<NoLimitAdapter.MyHolder> {

    private Context context;
    private List<TodayFreeBean.FreeGoodsInfoBean> noLimitList;
    private OnItemClick onItemClick;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public NoLimitAdapter(Context context, List<TodayFreeBean.FreeGoodsInfoBean> noLimitList) {
        this.context = context;
        this.noLimitList = noLimitList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.nolimitadapter, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        TodayFreeBean.FreeGoodsInfoBean bean = noLimitList.get(position);
        Glide.with(context).load(bean.getPictUrl()).into(holder.iv);
        IconAndTextGroupUtil.setTextView(context, holder.title, bean.getTitle(), bean.getUserType());
        holder.tvSalePrice.setText(MoneyFormatUtil.StringFormatWithYuan(bean.getPayPrice()));
        holder.tvOldPrice.setText("¥" + MoneyFormatUtil.StringFormatWithYuan(bean.getZkFinalPrice()));
        holder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.money.setText("付款" + MoneyFormatUtil.StringFormatWithYuan(bean.getPayPrice()) + "元，补贴" + bean.getSubsidyPrice() + "元");
        String freeRemainCount = bean.getFreeRemainCount();
        String freeTotalCount = bean.getFreeTotalCount();
        holder.totalNum.setText("共" + NumUtil.getNum(freeTotalCount) + "单");
        holder.residueNum.setText("剩余" + NumUtil.getNum(freeRemainCount) + "单");
        Double progress = Double.valueOf(freeRemainCount) / Double.valueOf(freeTotalCount) * 100;
        holder.progressBar.setProgress(progress.intValue());
        holder.robUser.setText(bean.getUserRank());
        holder.titleType.setText(bean.getRule());
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
        return noLimitList == null ? 0 : noLimitList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.titleType)
        TextView titleType;
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
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.totalNum)
        TextView totalNum;
        @BindView(R.id.residueNum)
        TextView residueNum;
        @BindView(R.id.robUser)
        TextView robUser;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
