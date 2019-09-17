package com.lianliantao.yuetuan.base_activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.activity.SearchResultActivity;
import com.lianliantao.yuetuan.myutil.ClipContentUtil;
import com.lianliantao.yuetuan.util.HistorySearchUtil;

import java.util.List;

/*这个类   是搜索标题弹窗类   登录注册模块不要继承这个类  其他全部继承该类*/
public class OriginalActivity extends BigBaseOriginalActivity {

    Dialog dialog;
    ClipboardManager cm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
