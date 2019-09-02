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
import com.lianliantao.yuetuan.bean.AppMsgBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppMsgAdapter extends RecyclerView.Adapter<AppMsgAdapter.MyHolder> {

    private Context context;
    private List<AppMsgBean.NoticeInfoBean> list;
    private String type;

    public AppMsgAdapter(Context context, List<AppMsgBean.NoticeInfoBean> list, String type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appmsgadapter, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        AppMsgBean.NoticeInfoBean bean = list.get(position);
        String noticeType = bean.getNoticeType();
        holder.title.setText(bean.getTitle());
        holder.content.setText(bean.getContent());
        holder.time.setText(bean.getCreateTime());
        if (type.equals("1")) {/*官方通知*/
            holder.ivType.setImageResource(R.mipmap.icon_tuiguang);
        } else {/*订单通知*/
            switch (noticeType) {
                case "1":
                    holder.ivType.setImageResource(R.mipmap.icon_tuiguang);
                    break;
                case "2":
                    holder.ivType.setImageResource(R.mipmap.xx_icontuanduitz);
                    break;
                case "3":
                    holder.ivType.setImageResource(R.mipmap.icon_dingdanshixiao);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivType)
        ImageView ivType;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.content)
        TextView content;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
