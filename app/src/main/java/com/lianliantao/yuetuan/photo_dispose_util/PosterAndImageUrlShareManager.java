package com.lianliantao.yuetuan.photo_dispose_util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.lianliantao.yuetuan.bean.CircleRecommendBean;
import com.lianliantao.yuetuan.util.CommonUtil;
import com.lianliantao.yuetuan.util.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PosterAndImageUrlShareManager {

    private Context context;
    private List<File> files = new ArrayList<>();
    private ComponentName comp;

    public PosterAndImageUrlShareManager(Context context) {
        this.context = context;
    }

    public void setShareImage(Bitmap hebingbitmap, List<CircleRecommendBean.InfoBean.ImgInfoBean> imagesList, String shareAppType) {
        if (shareAppType.equals("QQFriend") && !Tools.isAppAvilible(context, "com.tencent.mobileqq")) {
            ToastUtils.showToast(context, "您还没有安装QQ客户端，请先安装QQ客户端");
            return;
        } else if (shareAppType.equals("WChatFriend") && !Tools.isAppAvilible(context, "com.tencent.mm")) {
            ToastUtils.showToast(context, "您还没有安装微信客户端,请先安转客户端");
            return;
        } else if (shareAppType.equals("QQZone") && !Tools.isAppAvilible(context, "com.qzone")) {
            ToastUtils.showToast(context, "您还没有安装QQ空间客户端,请先安装");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file_one = CommonUtil.saveFile(hebingbitmap, context);
                files.add(file_one);
                for (int i = 1; i < imagesList.size(); i++) {
                    File netFile = Tools.saveImageToSdCard(context, imagesList.get(i).getUrl());/*网络路劲转File都ok*/
                    files.add(netFile);
                }
                Intent intent = new Intent();
                if (shareAppType.equals("QQFriend")) {   /*qq好友*/
                    comp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
                } else if (shareAppType.equals("QQZone")) { /*QQ空间*/
                    comp = new ComponentName("com.qzone", "com.qzonex.module.operation.ui.QZonePublishMoodActivity");
                } else if (shareAppType.equals("WChatFriend")) { /*微信好友*/
                    comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
                } else if (shareAppType.equals("WChatCircle")) { /*微信朋友圈*/
                    comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                }
                intent.setComponent(comp);
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("image/*");
                intent.putExtra("Kdescription", "");
                ArrayList<Uri> imageUris = new ArrayList<>();
                for (File f : files) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        //android 7.0以下
                        imageUris.add(Uri.fromFile(f));
                    } else {
                        //android 7.0及以上
                        Uri uri = null;
                        try {
                            uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), f.getAbsolutePath(), f.getName(), null));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        imageUris.add(uri);
                    }
                }
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }).start();
    }
}
