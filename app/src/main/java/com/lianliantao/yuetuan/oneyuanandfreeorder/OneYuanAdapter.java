package com.lianliantao.yuetuan.oneyuanandfreeorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.IconAndTextGroupUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.NumUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OneYuanAdapter extends RecyclerView.Adapter<OneYuanAdapter.MyHolder> {

    private Context context;
    private List<OneYuanBean.FreeGoodsInfoBean> list;
    private OnItemClick onItemClick;
    private int width;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public OneYuanAdapter(Context context, List<OneYuanBean.FreeGoodsInfoBean> list) {
        this.list = list;
        this.context = context;
        width = (context.getResources().getDisplayMetrics().widthPixels - DensityUtils.dip2px(context, 42)) / 2;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.oneyuan_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ViewGroup.LayoutParams ivLayoutParams = holder.iv.getLayoutParams();
        ivLayoutParams.width = width;
        ivLayoutParams.height = width;
        holder.iv.setLayoutParams(ivLayoutParams);
        Glide.with(context).load(list.get(position).getPictUrl()).into(holder.iv);
        IconAndTextGroupUtil.setTextView(context, holder.title, list.get(position).getTitle(), list.get(position).getUserType());
        holder.price.setText("¥ " + MoneyFormatUtil.StringFormatWithYuan(list.get(position).getPayPrice()));
        holder.num.setText("剩余 " + NumUtil.getNum(list.get(position).getFreeRemainCount()));
        holder.money.setText("先付款" + list.get(position).getDiscountPrice() + "   后补贴" + list.get(position).getSubsidyPrice());
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
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.num)
        TextView num;
        @BindView(R.id.money)
        TextView money;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
