package com.lianliantao.yuetuan.mine_activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.PosterPhotoSaveUtil;
import com.lianliantao.yuetuan.util.QRCodeUtil;
import com.lianliantao.yuetuan.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContantOurActivity extends BaseTitleActivity {

    @BindView(R.id.code)
    ImageView code;
    private Bitmap codeBitmap;

    @Override
    public int getContainerView() {
        return R.layout.contantouractivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setMiddleTitle("联系我们");
        codeBitmap = QRCodeUtil.createQRCodeBitmap("李晨奇", DensityUtils.dip2px(getApplicationContext(), 120), BitmapFactory.decodeResource(getResources(), R.mipmap.logo_seventy), 0.2f);
        code.setImageBitmap(codeBitmap);
        code.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //没有存储权限
                    ActivityCompat.requestPermissions(ContantOurActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    save();
                }
                return false;
            }
        });
        initPermission();
    }

    private void save() {
        PosterPhotoSaveUtil.saveBitmap2file(codeBitmap, getApplicationContext());
        ToastUtils.showBackgroudCenterToast(getApplicationContext(), "二维码已保存至手机图库");
    }

    /*权限申请*/
    private void initPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //没有存储权限
            ActivityCompat.requestPermissions(ContantOurActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ToastUtils.showToast(getApplicationContext(), "保存图片需要打开存储权限，请前往设置-应用-麻花优选-权限进行设置");
                return;
            } else if (grantResults.length <= 1 || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                ToastUtils.showToast(getApplicationContext(), "保存图片需要打开存储权限，请前往设置-应用-麻花优选-权限进行设置");
                return;
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
            return;
        }
    }

}
