package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.port_inner.OnItemClick;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineModuleAdapter extends RecyclerView.Adapter<MineModuleAdapter.MineModuleHolder> {
    private Context context;
    private String[] titles;
    private int[] images;
    private OnItemClick onItemClick;

    public void setonclicklistener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public MineModuleAdapter(Context context, String[] titles, int[] images) {
        this.context = context;
        this.titles = titles;
        this.images = images;
    }

    @NonNull
    @Override
    public MineModuleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mine_moudle_item, parent, false);
        return new MineModuleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MineModuleHolder holder, int position) {
        holder.tv.setText(titles[position]);
        holder.iv.setImageResource(images[position]);
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
        return titles == null ? 0 : titles.length;
    }

    public class MineModuleHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.tv)
        TextView tv;

        public MineModuleHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
