package com.lianliantao.yuetuan.oneyuanandfreeorder;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
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
import com.lianliantao.yuetuan.util.DensityUtils;
import com.lianliantao.yuetuan.util.DialogUtil;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.ToastUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OneYuanBuyActivity extends OriginalActivity {
    @BindView(R.id.viewHeight)
    View viewHeight;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.oneClick)
    ImageView oneClick;
    @BindView(R.id.zeroClick)
    TextView zeroClick;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private Intent intent;
    private int positionWhich;
    private List<OneYuanBean.FreeGoodsInfoBean> list;
    private NiceDialog taobaoAuthDialog, noticeDialog;
    private String itemId;
    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        int statusBarHeight = PhoneTopStyleUtil.getStatusBarHeight(getApplicationContext());
        setContentView(R.layout.oneyuanbuyactivity);
        ButterKnife.bind(this);
        alibcShowParams = new AlibcShowParams(OpenType.Native, true);
        tvTitle.setText("麻花一元购");
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHeight.getLayoutParams();
        layoutParams.height = statusBarHeight;
        viewHeight.setLayoutParams(layoutParams);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerview.addItemDecoration(new OneYuanItem(DensityUtils.dip2px(getApplicationContext(), 10)));
        getData();
    }

    @OnClick({R.id.ivBack, R.id.oneClick, R.id.zeroClick})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.oneClick:
                intent = new Intent(getApplicationContext(), OneYuanRegulationActivity.class);
                startActivity(intent);
                break;
            case R.id.zeroClick:
                intent = new Intent(getApplicationContext(), ZeroBuyActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getData() {
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.ONEYUAN_BUY + CommonParamUtil.getCommonParamSign(getApplicationContext()))
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("一元购", response.toString());
                        OneYuanBean bean = GsonUtil.GsonToBean(response.toString(), OneYuanBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            list = bean.getFreeGoodsInfo();
                            if (list.size() > 0) {
                                OneYuanAdapter adapter = new OneYuanAdapter(getApplicationContext(), list);
                                recyclerview.setAdapter(adapter);
                                adapter.setOnClickListener(new OnItemClick() {
                                    @Override
                                    public void OnItemClickListener(View view, int position) {
                                        positionWhich = position;
                                        String errorInfo = list.get(position).getErrorInfo();
                                        if (TextUtils.isEmpty(errorInfo)) {
                                            function();
                                        } else {
                                            ToastUtils.showBackgroudCenterToast(getApplicationContext(), errorInfo);
                                        }
                                    }
                                });
                            }
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

    private void function() {
        String hasBindTbk = PreferUtils.getString(getApplicationContext(), "hasBindTbk");
        if (hasBindTbk.equals("true")) {
            noticeDialog();
        } else {/*未备案*/
            taobaoQuDaoAuthDialog();
        }
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
                price.setText("商品付款 ¥ " + list.get(positionWhich).getPayPrice());
                subsidy.setText("麻花优选补贴 ¥ " + list.get(positionWhich).getSubsidyPrice());
                sure.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        noticeDialog.dismiss();
                        itemId = list.get(positionWhich).getItemId();
                        /*获取商品高佣链接接口*/
                        getGoodDetail();
                    }
                });
            }
        });
        noticeDialog.setMargin(100);
        noticeDialog.show(getSupportFragmentManager());
    }

    private Dialog loadingDialog;

    private void getGoodDetail() {
        loadingDialog = DialogUtil.createLoadingDialog(OneYuanBuyActivity.this, "加载中...");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", itemId);
        map.put("type", "2");
        String mapParam = CommonParamUtil.getOtherParamSign(getApplicationContext(), map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.GETGOOD_PROMOTIONLINK + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        DialogUtil.closeDialog(loadingDialog, OneYuanBuyActivity.this);
                        PromotionlinkBean bean = GsonUtil.GsonToBean(response.toString(), PromotionlinkBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            String couponClickUrl = bean.getLink();
                            AlibcBasePage page = new AlibcPage(couponClickUrl);
                            HashMap<String, String> exParams = new HashMap<>();
                            exParams.put("isv_code", "appisvcode");
                            exParams.put("alibaba", "阿里巴巴");
                            AlibcTrade.show(OneYuanBuyActivity.this, page, alibcShowParams, null, exParams, new AlibcTradeCallback() {
                                @Override
                                public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                                    /*阿里百川进淘宝成功*/
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    /*阿里百川进淘宝失败*/
                                }
                            });
                        } else {
                            ToastUtils.showBackgroudCenterToast(getApplicationContext(), bean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        DialogUtil.closeDialog(loadingDialog, OneYuanBuyActivity.this);
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
                        alibcLogin.showLogin(new AlibcLoginCallback() {
                            @Override
                            public void onSuccess(int i) {
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
}
