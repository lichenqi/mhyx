package com.lianliantao.yuetuan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.activity.MyBaseHtml5Activity;
import com.lianliantao.yuetuan.activity.TaoQiangGouActivity;
import com.lianliantao.yuetuan.bean.HomeHeadBean;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.dianpu.TianMaoActivityManager;
import com.lianliantao.yuetuan.home_classic.TaoBaoLikeActivity;
import com.lianliantao.yuetuan.login_and_register.MyWXLoginActivity;
import com.lianliantao.yuetuan.util.PreferUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeHeadClassicAdapter extends RecyclerView.Adapter<HomeHeadClassicAdapter.MyHolder> {

    private Context context;
    private List<HomeHeadBean.MenuInfoBean> menuInfo;
    private FragmentActivity activity;
    private Intent intent;

    public HomeHeadClassicAdapter(Context context, List<HomeHeadBean.MenuInfoBean> menuInfo, FragmentActivity activity) {
        this.menuInfo = menuInfo;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.homeheadclassicadapter, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Glide.with(context).load(menuInfo.get(position).getLogo()).into(holder.iv);
        holder.title.setText(menuInfo.get(position).getTitle());
        holder.tvFanYong.setText(menuInfo.get(position).getDescription());
        String type = menuInfo.get(position).getType();
        String url = menuInfo.get(position).getUrl();
        String title = menuInfo.get(position).getTitle();
        String redirectType = menuInfo.get(position).getRedirectType();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("native")) {/*原生跳转*/
                    switch (url) {
                        case "taoqianggou":/*淘抢购*/
                            intent = new Intent(context, TaoQiangGouActivity.class);
                            intent.putExtra("title", title);
                            intent.putExtra("type", "7");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            break;
                        case "9.9baoyou":/*9.9包邮*/
                            intent = new Intent(context, TaoQiangGouActivity.class);
                            intent.putExtra("title", title);
                            intent.putExtra("type", "6");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            break;
                        case "taobao":/*淘宝隐藏优惠券跳转*/
                            intent = new Intent(context, TaoBaoLikeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            break;
                    }
                } else if (type.equals("h5")) {/*h5跳转*/
                    if (redirectType.equals("mahua")) {/*内部跳转*/
                        if (PreferUtils.getBoolean(context, CommonApi.ISLOGIN)) {
                            intent = new Intent(context, MyBaseHtml5Activity.class);
                            intent.putExtra("url", url);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            intent = new Intent(context, MyWXLoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    } else if (redirectType.equals("tianmao")) {/*天猫跳转*/
                        if (PreferUtils.getBoolean(context, CommonApi.ISLOGIN)) {
                            String[] split = url.split("=");
                            TianMaoActivityManager tianMaoActivityManager = new TianMaoActivityManager(context, split[1], activity);
                            tianMaoActivityManager.check();
                        } else {
                            intent = new Intent(context, MyWXLoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuInfo == null ? 0 : menuInfo.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.tvFanYong)
        TextView tvFanYong;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
