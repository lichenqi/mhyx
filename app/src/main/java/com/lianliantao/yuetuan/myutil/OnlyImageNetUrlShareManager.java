package com.lianliantao.yuetuan.myutil;

import android.content.Context;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.CircleRecommendBean;
import com.lianliantao.yuetuan.dialogfragment.BaseNiceDialog;
import com.lianliantao.yuetuan.dialogfragment.NiceDialog;
import com.lianliantao.yuetuan.dialogfragment.ViewConvertListener;
import com.lianliantao.yuetuan.dialogfragment.ViewHolder;
import com.lianliantao.yuetuan.photo_dispose_util.OnlyNet2FileShare;

import java.util.List;

public class OnlyImageNetUrlShareManager {

    private Context context;
    private List<CircleRecommendBean.InfoBean.ImgInfoBean> imgInfo;
    private AppCompatActivity activity;
    private String shareAppType;
    private NiceDialog notice_dialog;

    public OnlyImageNetUrlShareManager(Context context, List<CircleRecommendBean.InfoBean.ImgInfoBean> imgInfo, AppCompatActivity activity, String shareAppType) {
        this.activity = activity;
        this.context = context;
        this.imgInfo = imgInfo;
        this.shareAppType = shareAppType;
    }

    public void setShare() {
        notice_dialog = NiceDialog.init();
        notice_dialog.setLayoutId(R.layout.photo_save_process_dialog);
        notice_dialog.setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                TextView tv_content = holder.getView(R.id.tv_content);
                tv_content.setText("加载中...");
            }
        });
        notice_dialog.setMargin(100);
        notice_dialog.show(activity.getSupportFragmentManager());
        OnlyNet2FileShare shareManager = new OnlyNet2FileShare(context);
        shareManager.setShareImage(imgInfo, shareAppType);
        notice_dialog.dismiss();
    }
}
