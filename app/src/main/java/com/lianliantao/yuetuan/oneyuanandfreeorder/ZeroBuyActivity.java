package com.lianliantao.yuetuan.oneyuanandfreeorder;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.activity.TaoBaoAuthActivity;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.bean.PromotionlinkBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.dialogfragment.BaseNiceDialog;
import com.lianliantao.yuetuan.dialogfragment.NiceDialog;
import com.lianliantao.yuetuan.dialogfragment.ViewConvertListener;
import com.lianliantao.yuetuan.dialogfragment.ViewHolder;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.PhoneTopStyleUtil;
import com.lianliantao.yuetuan.myutil.StatusBarUtils;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.DialogUtil;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.ToastUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZeroBuyActivity extends OriginalActivity {
    @BindView(R.id.viewHeight)
    View viewHeight;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.oneClick)
    TextView oneClick;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.previousReview)
    RecyclerView previousReview;
    @BindView(R.id.regulation)
    TextView regulation;
    private List<TodayFreeBean.FreeGoodsInfoBean> noLimitList;
    private NiceDialog taobaoAuthDialog, noticeDialog;
    private int positionWhich;
    private String itemId;
    private Dialog loadingDialog;
    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(getApplicationContext());
        setContentView(R.layout.zerobuyactivity);
        ButterKnife.bind(this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHeight.getLayoutParams();
        layoutParams.height = statusBarHeight;
        viewHeight.setLayoutParams(layoutParams);
        tvTitle.setText("0元抢购免单");
        alibcShowParams = new AlibcShowParams();
        alibcShowParams.setOpenType(OpenType.Native);
        alibcShowParams.setBackUrl("alisdk://");
        getData();
        initWuXianZhi();
    }

    private void initWuXianZhi() {
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        previousReview.setHasFixedSize(true);
        previousReview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void getData() {
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.FREETODAY + CommonParamUtil.getCommonParamSign(getApplicationContext()))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("今日免单", response.toString());
                        TodayFreeBean bean = GsonUtil.GsonToBean(response.toString(), TodayFreeBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            noLimitList = bean.getFreeGoodsInfo();
                            List<TodayFreeBean.FreeGoodsInfoOldBean> list = bean.getFreeGoodsInfoOld();
                            NoLimitAdapter noLimitAdapter = new NoLimitAdapter(getApplicationContext(), noLimitList);
                            recyclerview.setAdapter(noLimitAdapter);
                            PreviousReviewAdapter previousReviewAdapter = new PreviousReviewAdapter(getApplicationContext(), list);
                            previousReview.setAdapter(previousReviewAdapter);
                            regulation.setText(bean.getRuleInfo());
                            noLimitAdapter.setOnClickListener(new OnItemClick() {
                                @Override
                                public void OnItemClickListener(View view, int position) {
                                    positionWhich = position;
                                    String errorInfo = noLimitList.get(position).getErrorInfo();
                                    if (TextUtils.isEmpty(errorInfo)) {
                                        jump2Taobao();
                                    } else {
                                        ToastUtils.showBackgroudCenterToast(getApplicationContext(), errorInfo);
                                    }
                                }
                            });
                            previousReviewAdapter.setOnClickListener(new OnItemClick() {
                                @Override
                                public void OnItemClickListener(View view, int position) {
                                    ToastUtils.showBackgroudCenterToast(getApplicationContext(), "活动已结束");
                                }
                            });
                        } else {
                            ToastUtils.showToast(getApplicationContext(), bean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    private void jump2Taobao() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", noLimitList.get(positionWhich).getItemId());
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.GETGOOD_PROMOTIONLINK + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("淘宝链接", response.toString());
                        PromotionlinkBean bean = GsonUtil.GsonToBean(response.toString(), PromotionlinkBean.class);
                        int errno = bean.getErrno();
                        if (errno == CommonApi.RESULTCODEOK) {
                            noticeDialog();
                        } else if (errno == 434) {
                            /*未备案*/
                            taobaoQuDaoAuthDialog();
                        } else {
                            ToastUtils.showToast(getApplicationContext(), bean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    /*提示语弹框*/
    private void noticeDialog() {
        noticeDialog = NiceDialog.init();
        noticeDialog.setLayoutId(R.layout.zero_notice_dialog);
        noticeDialog.setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                TextView price = holder.getView(R.id.price);
                TextView subsidy = holder.getView(R.id.subsidy);
                TextView sure = holder.getView(R.id.sure);
                price.setText("商品付款 ¥ " + MoneyFormatUtil.StringFormatWithYuan(noLimitList.get(positionWhich).getPayPrice()));
                subsidy.setText("麻花优选补贴 ¥ " + MoneyFormatUtil.StringFormatWithYuan(noLimitList.get(positionWhich).getSubsidyPrice()));
                sure.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        noticeDialog.dismiss();
                        itemId = noLimitList.get(positionWhich).getItemId();
                        /*获取商品高佣链接接口*/
                        getGoodDetail();
                    }
                });
            }
        });
        noticeDialog.setMargin(100);
        noticeDialog.show(getSupportFragmentManager());
    }

    private void getGoodDetail() {
        loadingDialog = DialogUtil.createLoadingDialog(ZeroBuyActivity.this, "加载中...");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", itemId);
        map.put("type", "1");
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.GETGOOD_PROMOTIONLINK + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        DialogUtil.closeDialog(loadingDialog, ZeroBuyActivity.this);
                        PromotionlinkBean bean = GsonUtil.GsonToBean(response.toString(), PromotionlinkBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            String couponClickUrl = bean.getLink();
                            HashMap<String, String> exParams = new HashMap<>();
                            exParams.put("isv_code", "appisvcode");
                            exParams.put("alibaba", "阿里巴巴");
                            // 以显示传入url的方式打开页面（第二个参数是套件名称 暂时传“”）
                            AlibcTrade.openByUrl(ZeroBuyActivity.this, "", couponClickUrl, null,
                                    new WebViewClient(), new WebChromeClient(), alibcShowParams,
                                    null, exParams, new AlibcTradeCallback() {
                                        @Override
                                        public void onTradeSuccess(AlibcTradeResult tradeResult) {
                                        }

                                        @Override
                                        public void onFailure(int code, String msg) {
                                        }
                                    });
                        } else {
                            ToastUtils.showBackgroudCenterToast(getApplicationContext(), bean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        DialogUtil.closeDialog(loadingDialog, ZeroBuyActivity.this);
                        ToastUtils.showToast(getApplicationContext(), CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    /*淘宝渠道认证弹框*/
    private void taobaoQuDaoAuthDialog() {
        taobaoAuthDialog = NiceDialog.init();
        taobaoAuthDialog.setLayoutId(R.layout.taobaoauth_dialog);
        taobaoAuthDialog.setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                TextView cancel = holder.getView(R.id.cancel);
                TextView auth = holder.getView(R.id.auth);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taobaoAuthDialog.dismiss();
                    }
                });
                auth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taobaoAuthDialog.dismiss();
                        AlibcLogin alibcLogin = AlibcLogin.getInstance();
                        Session session = alibcLogin.getSession();
                        String openId = session.openId;
                        if (TextUtils.isEmpty(openId)) {/*阿里百川未授权*/
                            alibcLogin.showLogin(new AlibcLoginCallback() {
                                @Override
                                public void onSuccess(int i, String s, String s1) {
                                    Session session = alibcLogin.getSession();
                                    String nick = session.nick;/*淘宝昵称*/
                                    String avatarUrl = session.avatarUrl;/*淘宝头像*/
                                    Intent intent = new Intent(getApplicationContext(), TaoBaoAuthActivity.class);
                                    intent.putExtra("nick", nick);
                                    intent.putExtra("avatarUrl", avatarUrl);
                                    startActivityForResult(intent, 100);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                }
                            });
                        } else {
                            String nick = session.nick;/*淘宝昵称*/
                            String avatarUrl = session.avatarUrl;/*淘宝头像*/
                            Intent intent = new Intent(getApplicationContext(), TaoBaoAuthActivity.class);
                            intent.putExtra("nick", nick);
                            intent.putExtra("avatarUrl", avatarUrl);
                            startActivityForResult(intent, 100);
                        }
                    }
                });
            }
        });
        taobaoAuthDialog.setMargin(100);
        taobaoAuthDialog.show(getSupportFragmentManager());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            noticeDialog();
        }
    }

    @OnClick({R.id.ivBack, R.id.oneClick})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.oneClick:
                Intent intent = new Intent(getApplicationContext(), OneYuanBuyActivity.class);
                startActivity(intent);
                break;
        }
    }
}
