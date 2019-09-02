package com.lianliantao.yuetuan.myutil;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.ShopDeailBean;
import com.lianliantao.yuetuan.bean.TKLBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.util.DialogUtil;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.ParamUtil;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.ToastUtils;

import org.json.JSONObject;

import java.util.LinkedHashMap;

public class TKLCopyUtil {
    private Context context;
    private String itemId;
    private AppCompatActivity activity;
    private Dialog loadingDialog;
    private ShopDeailBean.GoodsDetailBean goodsDetail;
    private String tklContent;

    public TKLCopyUtil(Context context, String itemId, AppCompatActivity activity) {
        this.context = context;
        this.activity = activity;
        this.itemId = itemId;
    }

    public void copy() {
        loadingDialog = DialogUtil.createLoadingDialog(activity, "加载中...");
        getGoodDetail();
    }

    private void getGoodDetail() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", itemId);
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.SHOPDETAILAPI + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        ShopDeailBean bean = GsonUtil.GsonToBean(response.toString(), ShopDeailBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            goodsDetail = bean.getGoodsDetail();
                            getTKLData();/*获取淘口令接口*/
                        } else {
                            ToastUtils.showToast(context, CommonApi.ERROR_NET_MSG);
                            DialogUtil.closeDialog(loadingDialog, activity);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        DialogUtil.closeDialog(loadingDialog, activity);
                        ToastUtils.showToast(context, CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    private void getTKLData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", itemId);
        map.put("text", goodsDetail.getTitle());
        map.put("url", goodsDetail.getCouponClickUrl());
        map.put("logo", goodsDetail.getPictUrl());
        map.put("payPrice", MoneyFormatUtil.StringFormatWithYuan(goodsDetail.getPayPrice()));
        map.put("zkFinalPrice", MoneyFormatUtil.StringFormatWithYuan(goodsDetail.getZkFinalPrice()));
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.TKLDATA + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        DialogUtil.closeDialog(loadingDialog, activity);
                        TKLBean tklBean = GsonUtil.GsonToBean(response.toString(), TKLBean.class);
                        if (tklBean.getErrno() == CommonApi.RESULTCODEOK) {
                            tklContent = tklBean.getText();
                            showTKLDialog();
                        } else {
                            ToastUtils.showToast(context, tklBean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        DialogUtil.closeDialog(loadingDialog, activity);
                    }
                });
    }

    private void showTKLDialog() {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", tklContent);
        cm.setPrimaryClip(mClipData);
        ClipContentUtil.getInstance(context).putNewSearch(tklContent);//保存记录到数据库
        Dialog dialog = new Dialog(activity, R.style.transparentFrameWindowStyle);
        dialog.setContentView(R.layout.taokoulingcopydialog);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER | Gravity.CENTER);
        window.setWindowAnimations(R.style.my_dialog_style_animation);
        TextView sure = dialog.findViewById(R.id.sure);
        TextView content = dialog.findViewById(R.id.content);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        content.setText(tklContent);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }
}
