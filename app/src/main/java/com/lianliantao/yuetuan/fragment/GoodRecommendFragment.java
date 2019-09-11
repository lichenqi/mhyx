package com.lianliantao.yuetuan.fragment;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.activity.TaoBaoAuthActivity;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.CircleRecommendBean;
import com.lianliantao.yuetuan.bean.PromotionlinkBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.dialogfragment.BaseNiceDialog;
import com.lianliantao.yuetuan.dialogfragment.NiceDialog;
import com.lianliantao.yuetuan.dialogfragment.ViewConvertListener;
import com.lianliantao.yuetuan.dialogfragment.ViewHolder;
import com.lianliantao.yuetuan.fragment_adapter.CircleGoodRecommendAdapter;
import com.lianliantao.yuetuan.lazy_base_fragment.LazyBaseFragment;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.ClipContentUtil;
import com.lianliantao.yuetuan.myutil.JumpUtil;
import com.lianliantao.yuetuan.myutil.MultiSavePosterPhotosUtil;
import com.lianliantao.yuetuan.myutil.SaveFirstPosterAndImageUrlUtil;
import com.lianliantao.yuetuan.myutil.TKLCopyUtil;
import com.lianliantao.yuetuan.myutil.WeiXinShareOnlyPosterUtil;
import com.lianliantao.yuetuan.myutil.WeiXinSharePosterAndImgaeUrlUtil;
import com.lianliantao.yuetuan.photo_dispose_util.ShareNumData;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.lianliantao.yuetuan.util.XRecyclerViewUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodRecommendFragment extends LazyBaseFragment {
    Context context;
    @BindView(R.id.xrecyclerview)
    XRecyclerView xrecyclerview;
    @BindView(R.id.ivToTop)
    ImageView ivToTop;
    private int pageNum = 1;
    String pid;
    ClipboardManager cm;
    List<CircleRecommendBean.InfoBean> list = new ArrayList<>();
    CircleGoodRecommendAdapter adapter;
    int widthPixels, heightPixels;
    NiceDialog taobaoAuthDialog, shareDialog;
    int positionWhich;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            pid = arguments.getString("pid");
        }
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this, getView());
        context = MyApplication.getInstance();
        cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        widthPixels = displayMetrics.widthPixels;
        heightPixels = displayMetrics.heightPixels;
        initRecyclerview();
        getData();
    }

    @OnClick(R.id.ivToTop)
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ivToTop:
                xrecyclerview.scrollToPosition(0);
                break;
        }
    }

    private void getData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("pid", pid);
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", "10");
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.CIRCLE_GOOD_COMMMEND + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("商品推荐", response.toString());
                        CircleRecommendBean bean = GsonUtil.GsonToBean(response.toString(), CircleRecommendBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<CircleRecommendBean.InfoBean> info = bean.getInfo();
                            if (pageNum == 1) {
                                list.clear();
                                list.addAll(info);
                                adapter.notifyDataSetChanged();
                                xrecyclerview.refreshComplete();
                            } else {
                                list.addAll(info);
                                adapter.notifyDataSetChanged();
                                xrecyclerview.loadMoreComplete();
                            }
                        } else {
                            xrecyclerview.refreshComplete();
                            xrecyclerview.loadMoreComplete();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(context, CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    private void initRecyclerview() {
        XRecyclerViewUtil.setView(xrecyclerview);
        xrecyclerview.setHasFixedSize(true);
        xrecyclerview.setLayoutManager(new LinearLayoutManager(context));
        adapter = new CircleGoodRecommendAdapter(context, list);
        xrecyclerview.setAdapter(adapter);
        xrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                getData();
            }

            @Override
            public void onLoadMore() {
                pageNum++;
                getData();
            }
        });
        adapter.setOnLongContentClickListener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                String description = list.get(position - 1).getDescription();
                ClipData mClipData = ClipData.newPlainText("Label", description);
                cm.setPrimaryClip(mClipData);
                ToastUtils.showBackgroudCenterToast(context, "内容复制成功");
                ClipContentUtil.getInstance(context).putNewSearch(description);//保存记录到数据库
            }
        });
        adapter.setOnShopDetailClickListener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                JumpUtil.jump2ShopDetail(context, list.get(position - 1).getGoodsInfo().getItemId());
            }
        });
        /*淘口令复制按钮*/
        adapter.setOnTaobaoLinkClickListener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                String itemId = list.get(position - 1).getGoodsInfo().getItemId();
                TKLCopyUtil tklCopyUtil = new TKLCopyUtil(context, itemId, activity);
                tklCopyUtil.copy();
            }
        });
        /*点击分享按钮*/
        adapter.setOnShareClickListener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                positionWhich = position - 1;
                List<CircleRecommendBean.InfoBean.ImgInfoBean> imgInfo = list.get(positionWhich).getImgInfo();
                if (imgInfo.size() > 0) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //没有存储权限
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        commonShareFunction();
                    }
                } else {
                    ToastUtils.showBackgroudCenterToast(context, "暂无图片可供分享");
                }
            }
        });
        /*滑动监听*/
        xrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int i = recyclerView.computeVerticalScrollOffset();
                if (i > 1300) {
                    ivToTop.setVisibility(View.GONE);
                } else {
                    ivToTop.setVisibility(View.GONE);
                }
            }
        });
    }

    private void commonShareFunction() {
        String itemId = list.get(positionWhich).getGoodsInfo().getItemId();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("itemId", itemId);
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp().post().tag(this)
                .url(CommonApi.BASEURL + CommonApi.GETGOOD_PROMOTIONLINK + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        PromotionlinkBean bean = GsonUtil.GsonToBean(response.toString(), PromotionlinkBean.class);
                        int errno = bean.getErrno();
                        if (errno == CommonApi.RESULTCODEOK) {
                            /*淘宝渠道已认证*/
                            shareDialog();
                        } else if (errno == 434) {/*未备案*/
                            taobaoQuDaoAuthDialog();
                        } else {
                            ToastUtils.showToast(context, bean.getUsermsg());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(context, CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    /*分享弹框*/
    private void shareDialog() {
        shareDialog = NiceDialog.init();
        shareDialog.setLayoutId(R.layout.circle_good_share_dialog);
        shareDialog.setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                RelativeLayout reSave = holder.getView(R.id.reSave);
                RelativeLayout reWChat = holder.getView(R.id.reWChat);
                RelativeLayout reWChatCircle = holder.getView(R.id.reWChatCircle);
                RelativeLayout reQQ = holder.getView(R.id.reQQ);
                reSave.setOnClickListener(new View.OnClickListener() {/*批量存图*/
                    @Override
                    public void onClick(View v) {
                        shareDialog.dismiss();
                        saveCommonImage();
                    }
                });
                reWChat.setOnClickListener(new View.OnClickListener() {/*微信好友分享*/
                    @Override
                    public void onClick(View v) {
                        shareDialog.dismiss();
                        WChatAndQQFriendShare("WChatFriend");
                    }
                });
                reQQ.setOnClickListener(new View.OnClickListener() {/*QQ好友分享*/
                    @Override
                    public void onClick(View v) {
                        shareDialog.dismiss();
                        WChatAndQQFriendShare("QQFriend");
                    }
                });
                reWChatCircle.setOnClickListener(new View.OnClickListener() {/*微信朋友圈*/
                    @Override
                    public void onClick(View v) {
                        shareDialog.dismiss();
                        saveCommonImage();
                    }
                });
            }
        });
        shareDialog.show(getChildFragmentManager());
        shareDialog.setShowBottom(true);
    }

    /*公共的保存图片方法*/
    private void saveCommonImage() {
        String description = list.get(positionWhich).getDescription();
        ClipData mClipData = ClipData.newPlainText("Label", description);
        cm.setPrimaryClip(mClipData);
        ClipContentUtil.getInstance(context).putNewSearch(description);//保存记录到数据库
        CircleRecommendBean.InfoBean.GoodsInfoBean goodsInfo = list.get(positionWhich).getGoodsInfo();
        List<CircleRecommendBean.InfoBean.ImgInfoBean> imgInfo = list.get(positionWhich).getImgInfo();
        if (goodsInfo == null) {/*全是商品   全都要二维码合成图 保存*/
            MultiSavePosterPhotosUtil util = new MultiSavePosterPhotosUtil(context, imgInfo, (AppCompatActivity) getActivity());
            util.setSave();
        } else {/*第一个 合成二维码组图  其余保存商品纯图片*/
            String itemId = goodsInfo.getItemId();
            SaveFirstPosterAndImageUrlUtil utilSecond = new SaveFirstPosterAndImageUrlUtil(context, imgInfo, itemId, (AppCompatActivity) getActivity());
            utilSecond.setSave();
        }
    }

    /*微信好友和QQ好友分享公共方法*/
    private void WChatAndQQFriendShare(String type) {
        List<CircleRecommendBean.InfoBean.ImgInfoBean> imgInfo = list.get(positionWhich).getImgInfo();
        CircleRecommendBean.InfoBean.GoodsInfoBean goodsInfo = list.get(positionWhich).getGoodsInfo();
        if (goodsInfo == null) {/*全部是商品 全部要合成二维码组图分享*/
            WeiXinShareOnlyPosterUtil weiXinShareOnlyPosterUtil = new WeiXinShareOnlyPosterUtil(context, imgInfo, (AppCompatActivity) getActivity(), type);
            weiXinShareOnlyPosterUtil.setWeiXinFriendShare();
        } else {/*第一张图是商品  要合成  其余全是纯图片*/
            String itemId = goodsInfo.getItemId();
            WeiXinSharePosterAndImgaeUrlUtil util = new WeiXinSharePosterAndImgaeUrlUtil(context, imgInfo, itemId, (AppCompatActivity) getActivity(), type);
            util.setShare();
        }
        ShareNumData shareNumData = new ShareNumData(context, list.get(positionWhich).getId());
        shareNumData.share();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.goodrecommendfragment;
    }

    @Override
    protected boolean setFragmentTarget() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shareDialog != null) {
            shareDialog.dismiss();
        }
        if (taobaoAuthDialog != null) {
            taobaoAuthDialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ToastUtils.showToast(context, "分享需要打开存储权限，请前往设置-应用-麻花优选-权限进行设置");
                return;
            } else if (grantResults.length <= 1 || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                ToastUtils.showToast(context, "分享需要打开存储权限，请前往设置-应用-麻花优选-权限进行设置");
                return;
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                commonShareFunction();
            }
            return;
        }
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
                            public void onSuccess(int i, String s, String s1) {
                                Session session = alibcLogin.getSession();
                                String nick = session.nick;/*淘宝昵称*/
                                String avatarUrl = session.avatarUrl;/*淘宝头像*/
                                Intent intent = new Intent(context, TaoBaoAuthActivity.class);
                                intent.putExtra("nick", nick);
                                intent.putExtra("avatarUrl", avatarUrl);
                                startActivity(intent);
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
        taobaoAuthDialog.show(getChildFragmentManager());
        taobaoAuthDialog.setOutCancel(false);
        taobaoAuthDialog.setCancelable(false);
    }

    FragmentActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }
}
