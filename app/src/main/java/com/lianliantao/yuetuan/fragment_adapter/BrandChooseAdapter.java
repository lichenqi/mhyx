package com.lianliantao.yuetuan.fragment_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.BrandChooseBean;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrandChooseAdapter extends RecyclerView.Adapter<BrandChooseAdapter.BrandChooseHolder> {

    private Context context;
    private List<BrandChooseBean.BannerInfoBean> list;
    private OnItemClick onItemClick;

    public void setonclicklistener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public BrandChooseAdapter(Context context, List<BrandChooseBean.BannerInfoBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BrandChooseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.brandchooseadapter, parent, false);
        return new BrandChooseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandChooseHolder holder, int position) {
        List<BrandChooseBean.BannerInfoBean.GoodsInfoBean> goodsInfoList = list.get(position).getGoodsInfo();
        holder.recyclerview.setHasFixedSize(true);
        holder.recyclerview.setLayoutManager(new GridLayoutManager(context, 3));
        ChooseItemAdapter adapter = new ChooseItemAdapter(context, goodsInfoList);
        holder.recyclerview.setAdapter(adapter);
        Glide.with(context).load(list.get(position).getLogo()).into(holder.iv);
        holder.title.setText(list.get(position).getBrandName());
        if (onItemClick != null) {
            holder.reMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.OnItemClickListener(holder.reMore, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class BrandChooseHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        RoundedImageView iv;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.recyclerview)
        RecyclerView recyclerview;
        @BindView(R.id.reMore)
        RelativeLayout reMore;

        public BrandChooseHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
