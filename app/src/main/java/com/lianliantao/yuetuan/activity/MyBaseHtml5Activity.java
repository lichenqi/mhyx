package com.lianliantao.yuetuan.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.myutil.ClipContentUtil;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.myutil.StatusBarUtils;
import com.lianliantao.yuetuan.util.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class MyBaseHtml5Activity extends OriginalActivity {
    @BindView(R.id.viewHeight)
    View viewHeight;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvClose)
    TextView tvClose;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivRight)
    ImageView ivRight;
    @BindView(R.id.rlParent)
    RelativeLayout rlParent;
    @BindView(R.id.webview)
    WebView webview;
    private String url;
    private String shareUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(getApplicationContext());
        setContentView(R.layout.mybasehtml5activity);
        ButterKnife.bind(this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHeight.getLayoutParams();
        layoutParams.height = statusBarHeight;
        viewHeight.setLayoutParams(layoutParams);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        setWebView();
    }

    private void setWebView() {
        WebSettings settings = webview.getSettings();
        webview.setVerticalScrollBarEnabled(false);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(true);
        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tvTitle.setText(title);
            }

        });
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                shareUrl = url;
            }
        });
        webview.loadUrl(url);
        webview.addJavascriptInterface(new DemoJavascriptInterface(), "jsandroid");
    }

    public class DemoJavascriptInterface {

        /*地推物料复制链接交互*/
        @JavascriptInterface
        public void copyUrl(String link) {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", link);
            cm.setPrimaryClip(mClipData);
            ClipContentUtil.getInstance(getApplicationContext()).putNewSearch(link);//保存记录到数据库
            ToastUtils.showBackgroudCenterToast(getApplicationContext(), "复制成功");
        }

        /*微信好友分享交互*/
        @JavascriptInterface
        public void StudyShareToFriend(String title, String shortUrl, String picurl) {
            String content = "如何在麻花优选联盟搜索商品\n" + shareUrl;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Wechat.ShareParams sp = new Wechat.ShareParams();
                    sp.setText(content);
                    sp.setShareType(Platform.SHARE_TEXT);
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    wechat.setPlatformActionListener(new PlatformActionListener() {
                        @Override
                        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        }

                        @Override
                        public void onError(Platform platform, int i, Throwable throwable) {
                        }

                        @Override
                        public void onCancel(Platform platform, int i) {
                        }
                    });
                    wechat.share(sp);
                }
            });
        }

        /*微信朋友圈分享交互*/
        @JavascriptInterface
        public void StudyShareToCircle(String title, String shortUrl, String picurl) {
            String content = "如何在麻花优选联盟搜索商品\n" + shortUrl;
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", content);
            cm.setPrimaryClip(mClipData);
            ToastUtils.showToast(getApplicationContext(), "文案已复制到剪切板");
            ClipContentUtil.getInstance(getApplicationContext()).putNewSearch(content);//保存记录到数据库
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
                    sp.setImageUrl(picurl);
                    sp.setShareType(Platform.SHARE_IMAGE);
                    Platform weChat = ShareSDK.getPlatform(WechatMoments.NAME);
                    weChat.setPlatformActionListener(new PlatformActionListener() {
                        @Override
                        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        }

                        @Override
                        public void onError(Platform platform, int i, Throwable throwable) {
                        }

                        @Override
                        public void onCancel(Platform platform, int i) {
                        }
                    });
                    weChat.share(sp);
                }
            });

        }
    }

    @OnClick({R.id.ivBack, R.id.tvClose, R.id.ivRight})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.tvClose:
                finish();
                break;
            case R.id.ivRight:
                if (webview != null) {
                    webview.reload();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (webview != null) {
            webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview.clearHistory();
            ((ViewGroup) webview.getParent()).removeView(webview);
            webview.destroy();
            webview = null;
        }
        super.onDestroy();
    }
}
