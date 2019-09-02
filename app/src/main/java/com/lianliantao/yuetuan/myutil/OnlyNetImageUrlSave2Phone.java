package com.lianliantao.yuetuan.myutil;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.bean.CircleRecommendBean;
import com.lianliantao.yuetuan.dialogfragment.BaseNiceDialog;
import com.lianliantao.yuetuan.dialogfragment.NiceDialog;
import com.lianliantao.yuetuan.dialogfragment.ViewConvertListener;
import com.lianliantao.yuetuan.dialogfragment.ViewHolder;
import com.lianliantao.yuetuan.util.PosterPhotoSaveUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OnlyNetImageUrlSave2Phone {
    private Context context;
    private List<CircleRecommendBean.InfoBean.ImgInfoBean> imgInfo;
    private AppCompatActivity activity;
    private List<Bitmap> firstIsPhotoBitmap;
    private NiceDialog notice_dialog;
    private TextView tv_content;

    public OnlyNetImageUrlSave2Phone(Context context, List<CircleRecommendBean.InfoBean.ImgInfoBean> imgInfo, AppCompatActivity activity) {
        this.context = context;
        this.imgInfo = imgInfo;
        this.activity = activity;
    }

    public void save() {
        firstIsPhotoBitmap = new ArrayList<>();
        /*提示图片保存进度*/
        notice_dialog = NiceDialog.init();
        notice_dialog.setLayoutId(R.layout.photo_save_process_dialog);
        notice_dialog.setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                tv_content = holder.getView(R.id.tv_content);
            }
        });
        notice_dialog.setMargin(100);
        notice_dialog.show(activity.getSupportFragmentManager());
        saveMorePhotoToLocal();
    }

    /*批量下载图片*/     /*网络路劲存储*/
    private void saveMorePhotoToLocal() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl;
                try {
                    for (int i = 0; i < imgInfo.size(); i++) {
                        imageurl = new URL(imgInfo.get(i).getUrl());
                        HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        is.close();
                        Message msg = new Message();
                        // 把bm存入消息中,发送到主线程
                        msg.obj = bitmap;
                        handler.sendMessage(msg);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            PosterPhotoSaveUtil.saveBitmap2file(bitmap, context);
            firstIsPhotoBitmap.add(bitmap);
            tv_content.setText("正在保存第" + firstIsPhotoBitmap.size() + "张图");
            if (firstIsPhotoBitmap.size() == imgInfo.size()) {
                notice_dialog.dismiss();
                gotoWeiXinAppDialog();
            }
        }
    };

    /*去微信提示框*/
    private void gotoWeiXinAppDialog() {
        NiceDialog openWeiXinDialog = NiceDialog.init();
        openWeiXinDialog.setLayoutId(R.layout.goto_weixin_dialog);
        openWeiXinDialog.setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                TextView cancel = holder.getView(R.id.cancel);
                TextView open = holder.getView(R.id.open);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openWeiXinDialog.dismiss();
                    }
                });
                open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openWeiXinDialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setComponent(cmp);
                        context.startActivity(intent);
                    }
                });
            }
        });
        openWeiXinDialog.setMargin(100);
        openWeiXinDialog.show(activity.getSupportFragmentManager());
        openWeiXinDialog.setOutCancel(false);
        openWeiXinDialog.setCancelable(false);
    }
}
