package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.GoodRecommendBean;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodRecommendAdapter extends RecyclerView.Adapter<GoodRecommendAdapter.GoodRecommendHolder> {
    private Context context;
    private List<GoodRecommendBean.RecommendInfoBean> list;
    private int width;
    private OnItemClick onItemClick;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public GoodRecommendAdapter(Context context, List<GoodRecommendBean.RecommendInfoBean> list) {
        this.context = context;
        this.list = list;
        int i = DensityUtils.dip2px(context, 40);
        width = (context.getResources().getDisplayMetrics().widthPixels - i) / 3;
    }

    @NonNull
    @Override
    public GoodRecommendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.goodrecommendadapter, parent, false);
        return new GoodRecommendHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodRecommendHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.ll_parent.getLayoutParams();
        layoutParams.width = width;
        holder.ll_parent.setLayoutParams(layoutParams);
        ViewGroup.LayoutParams ivLayoutParams = holder.iv.getLayoutParams();
        ivLayoutParams.height = width;
        holder.iv.setLayoutParams(ivLayoutParams);
        Glide.with(context).load(list.get(position).getPictUrl()).into(holder.iv);
        IconAndTextGroupUtil.setTextView(context, holder.title, list.get(position).getTitle(), list.get(position).getUserType());
        holder.price.setText("¥ " + MoneyFormatUtil.StringFormatWithYuan(list.get(position).getPayPrice()));
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

    public class GoodRecommendHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_parent)
        LinearLayout ll_parent;
        @BindView(R.id.iv)
        RoundedImageView iv;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.price)
        TextView price;

        public GoodRecommendHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
