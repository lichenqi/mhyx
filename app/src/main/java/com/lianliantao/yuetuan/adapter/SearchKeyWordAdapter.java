package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.port_inner.OnItemClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchKeyWordAdapter extends RecyclerView.Adapter<SearchKeyWordAdapter.SearchKeyWordHolder> {
    private Context context;
    private List<String> list;
    private OnItemClick onItemClick;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public SearchKeyWordAdapter(List<String> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SearchKeyWordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tv, parent, false);
        return new SearchKeyWordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchKeyWordHolder holder, int position) {
        holder.tv.setText(list.get(position));
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

    public class SearchKeyWordHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv)
        TextView tv;

        public SearchKeyWordHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
