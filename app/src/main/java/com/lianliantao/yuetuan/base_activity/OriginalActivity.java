package com.lianliantao.yuetuan.base_activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lianliantao.yuetuan.MainActivity;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.activity.SearchResultActivity;
import com.lianliantao.yuetuan.app_status.AppManager;
import com.lianliantao.yuetuan.app_status.AppStatus;
import com.lianliantao.yuetuan.app_status.AppStatusManager;
import com.lianliantao.yuetuan.myutil.ClipContentUtil;
import com.lianliantao.yuetuan.util.HistorySearchUtil;

import java.util.List;

/*最原始基本类*/
public class OriginalActivity extends AppCompatActivity {
    Dialog dialog;
    ClipboardManager cm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this); //添加到栈中
        if (AppStatusManager.getInstance().getAppStatus() == AppStatus.STATUS_RECYVLE) {
            //跳到MainActivity,让MainActivity也finish掉
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*获取剪切板内容*/
        getClipContent();
    }

    private void getClipContent() {
        cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (cm.hasPrimaryClip()) {
            ClipData data = cm.getPrimaryClip();
            if (data == null) return;
            ClipData.Item item = data.getItemAt(0);
            final String content = item.coerceToText(getApplicationContext()).toString().trim().replace("\r\n\r\n", "\r\n");
            if (TextUtils.isEmpty(content)) return;
            /*数据库数据*/
            List<String> clip_list = ClipContentUtil.getInstance(getApplicationContext()).queryHistorySearchList();
            if (clip_list == null) return;
            for (int i = 0; i < clip_list.size(); i++) {
                if (clip_list.get(i).toString().trim().replace("\r\n\r\n", "\r\n").equals(content)) {
                    return;
                }
            }
            showDialog(content);
        }
    }

    private void showDialog(final String content) {
        guoDuTanKuang(content);
    }

    /*过渡弹框*/
    private void guoDuTanKuang(final String content) {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(OriginalActivity.this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(R.layout.clip_search_dialog);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER | Gravity.CENTER);
        window.setWindowAnimations(R.style.my_dialog_style_animation);
        TextView sure = dialog.findViewById(R.id.sure);
        TextView cancel = dialog.findViewById(R.id.cancel);
        TextView title = dialog.findViewById(R.id.content);
        title.setText(content);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (cm.hasPrimaryClip()) {
                    cm.setPrimaryClip(ClipData.newPlainText(null, ""));
                }
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                HistorySearchUtil.getInstance(OriginalActivity.this).putNewSearch(content);//保存记录到数据库
                Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                intent.putExtra("keyword", content);
                intent.putExtra("search_type", 0);
                startActivity(intent);
                if (cm.hasPrimaryClip()) {
                    cm.setPrimaryClip(ClipData.newPlainText(null, ""));
                }
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().finishActivity(this); //从栈中移除
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            //非默认值
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

}
