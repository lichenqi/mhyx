package com.lianliantao.yuetuan.port_inner;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;

import com.lianliantao.yuetuan.activity.MyBaseHtml5Activity;
import com.lianliantao.yuetuan.bean.HomeHeadBean;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.dianpu.CheckUserBeian2ShopManager;
import com.lianliantao.yuetuan.login_and_register.MyWXLoginActivity;
import com.lianliantao.yuetuan.myutil.JumpUtil;
import com.lianliantao.yuetuan.rankling_list.ChuDanListActivity;
import com.lianliantao.yuetuan.rankling_list.RankingListActivity;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

public class BannerOnClickListener implements OnBannerListener {

    private Context context;
    private List<HomeHeadBean.BannerInfoBean> bannerInfo;
    private Intent intent;
    private FragmentActivity activity;

    public BannerOnClickListener(Context context, List<HomeHeadBean.BannerInfoBean> bannerInfo, FragmentActivity activity) {
        this.context = context;
        this.bannerInfo = bannerInfo;
        this.activity = activity;
    }

    @Override
    public void OnBannerClick(int position) {
        HomeHeadBean.BannerInfoBean bean = bannerInfo.get(position);
        String title = bean.getTitle();
        String url = bean.getUrl();
        String type = bean.getType();
        String redirectType = bean.getRedirectType();
        if (type.equals("native")) {/*原生跳转*/
            if (url.contains("gaoyongjin")) {/*高佣金排行榜商品*/
                intent = new Intent(context, RankingListActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("type", "9");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else if (url.contains("chudan")) {/*出单排行榜商品*/
                intent = new Intent(context, ChuDanListActivity.class);
                intent.putExtra("title", title);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else if (url.contains("goodsdetail")) {
                String[] split = url.split("=");
                JumpUtil.jump2ShopDetail(context, split[1]);
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
            } else if (redirectType.equals("ali")) {/*店铺跳转*/
                if (PreferUtils.getBoolean(context, CommonApi.ISLOGIN)) {
                    String[] split = url.split("=");
                    CheckUserBeian2ShopManager manager = new CheckUserBeian2ShopManager(context, split[1], activity);
                    manager.check();
                } else {
                    intent = new Intent(context, MyWXLoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        }
    }

}
