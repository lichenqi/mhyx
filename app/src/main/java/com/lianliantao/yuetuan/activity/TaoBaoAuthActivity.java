package com.lianliantao.yuetuan.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.BaseTitleActivity;
import com.lianliantao.yuetuan.bean.BaseBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.PreferUtils;

import org.json.JSONObject;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaoBaoAuthActivity extends BaseTitleActivity {

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    String nick, avatarUrl, authUrl;

    @Override
    public int getContainerView() {
        return R.layout.base_webview;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setMiddleTitle("淘宝渠道备案");
        authUrl = PreferUtils.getString(getApplicationContext(), "authUrl");
        ImageView iv_back = findViewById(R.id.iv_back);
        Intent intent = getIntent();
        nick = intent.getStringExtra("nick");
        avatarUrl = intent.getStringExtra("avatarUrl");
        WebSettings settings = webview.getSettings();
        webview.setVerticalScrollBarEnabled(false);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(true);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        getUrlData();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
            }
        });
        webview.addJavascriptInterface(new DemoJavascriptInterface(), "newsinfo");
    }

    /*获取地址*/
    private void getUrlData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("avatar", avatarUrl);
        map.put("nickName", nick);
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + "u/Savetaobaoinfo" + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("地址", response.toString());
                        BaseBean bean = GsonUtil.GsonToBean(response.toString(), BaseBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            webview.loadUrl(authUrl);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                    }
                });

    }

    public class DemoJavascriptInterface {

        @JavascriptInterface
        public void fromJs(String msg) {
            /*用户淘宝备案成功 更改存储标识*/
            PreferUtils.putString(getApplicationContext(), "hasBindTbk", "true");
            setResult(100);
            finish();
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
