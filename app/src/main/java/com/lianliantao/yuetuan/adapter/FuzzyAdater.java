package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.port_inner.OnItemClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FuzzyAdater extends RecyclerView.Adapter<FuzzyAdater.FuzzyHolder> {
    private Context context;
    private List<List<String>> lists;
    private OnItemClick onItemClick;

    public void setonclicklistener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public FuzzyAdater(Context context, List<List<String>> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public FuzzyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fuzzyadater, parent, false);
        return new FuzzyHolder(view);
    }

    @Override
    public void onBindViewHolder(final FuzzyHolder holder, int position) {
        holder.name.setText(lists.get(position).get(0));
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
        return lists == null ? 0 : lists.size();
    }

    public class FuzzyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;

        public FuzzyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
