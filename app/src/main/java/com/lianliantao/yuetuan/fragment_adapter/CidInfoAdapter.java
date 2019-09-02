package com.lianliantao.yuetuan.fragment_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.CircleLabelBean;
import com.lianliantao.yuetuan.port_inner.OnItemClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CidInfoAdapter extends RecyclerView.Adapter<CidInfoAdapter.MyHolder> {
    private Context context;
    private List<CircleLabelBean.CidInfo> cidInfoList;
    private OnItemClick onItemClick;

    public void setOnClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public CidInfoAdapter(Context context, List<CircleLabelBean.CidInfo> cidInfoList) {
        this.cidInfoList = cidInfoList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cidinfoadapter, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.name.setText(cidInfoList.get(position).getName());
        if (cidInfoList.get(position).isChecked()) {
            holder.name.setTextColor(0xffFF592A);
        } else {
            holder.name.setTextColor(0xff000000);
        }
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
        return cidInfoList == null ? 0 : cidInfoList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
