package com.lianliantao.yuetuan.fragment;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.CircleLabelBean;
import com.lianliantao.yuetuan.bean.CircleRecommendBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.dialogfragment.BaseNiceDialog;
import com.lianliantao.yuetuan.dialogfragment.NiceDialog;
import com.lianliantao.yuetuan.dialogfragment.ViewConvertListener;
import com.lianliantao.yuetuan.dialogfragment.ViewHolder;
import com.lianliantao.yuetuan.fragment_adapter.CidInfoAdapter;
import com.lianliantao.yuetuan.fragment_adapter.YingXiaoOrXinShouAdapter;
import com.lianliantao.yuetuan.itemdecoration.CidInfoItemSpace;
import com.lianliantao.yuetuan.lazy_base_fragment.LazyBaseFragment;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.myutil.ClipContentUtil;
import com.lianliantao.yuetuan.myutil.OnlyImageNetUrlShareManager;
import com.lianliantao.yuetuan.myutil.OnlyNetImageUrlSave2Phone;
import com.lianliantao.yuetuan.photo_dispose_util.ShareNumData;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class YingXiaoOrXinShouOrShangXueYuanFragment extends LazyBaseFragment {

    Context context;
    List<CircleLabelBean.CidInfo> cidinfoList;
    String pid;
    @BindView(R.id.horizontalRecyclerView)
    RecyclerView horizontalRecyclerView;
    @BindView(R.id.verticalRecyclerView)
    RecyclerView verticalRecyclerView;
    @BindView(R.id.smartrefresh)
    SmartRefreshLayout smartrefresh;
    private int pageNum = 1;
    private List<CircleRecommendBean.InfoBean> list = new ArrayList<>();
    YingXiaoOrXinShouAdapter adapter;
    List<CircleRecommendBean.InfoBean.ImgInfoBean> imgInfo;
    private int positionWhich;
    ClipboardManager cm;
    private String cid = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            pid = arguments.getString("pid");
            cidinfoList = (List<CircleLabelBean.CidInfo>) arguments.getSerializable("cidinfo");
        }
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this, getView());
        context = MyApplication.getInstance();
        initRefrersh();/*刷新实现*/
        initHorizontalRecyclerview();/*水平滑动列表*/
        getListData();
        initVerticalRecyclerview();/*竖直滑动列表*/
    }

    private void initRefrersh() {
        smartrefresh.setRefreshHeader(new ClassicsHeader(context));
        smartrefresh.setRefreshFooter(new ClassicsFooter(context));
        smartrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getListData();
                refreshlayout.finishRefresh(1000);
            }
        });
        smartrefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                getListData();
                refreshLayout.finishLoadMore(600);
            }
        });
    }

    private void initVerticalRecyclerview() {
        verticalRecyclerView.setHasFixedSize(true);
        verticalRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new YingXiaoOrXinShouAdapter(context, list);
        verticalRecyclerView.setAdapter(adapter);
        /*分享按钮点击*/
        adapter.setOnClickListener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                positionWhich = position;
                imgInfo = list.get(position).getImgInfo();
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
    }

    private void commonShareFunction() {
        NiceDialog shareDialog = NiceDialog.init();
        shareDialog.setLayoutId(R.layout.circle_good_share_dialog);
        shareDialog.setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                RelativeLayout reSave = holder.getView(R.id.reSave);
                RelativeLayout reWChat = holder.getView(R.id.reWChat);
                RelativeLayout reWChatCircle = holder.getView(R.id.reWChatCircle);
                RelativeLayout reQQ = holder.getView(R.id.reQQ);
                /*批量存图*/
                reSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareDialog.dismiss();
                        savePhoto2SDOrCircle();
                    }
                });
                /*微信好友分享*/
                reWChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareDialog.dismiss();
                        if (imgInfo.size() == 1) {
                            /*单张图分享*/
                            onePhotoOfWChatFriend();
                        } else {
                            /*多张图分享*/
                            OnlyImageNetUrlShareManager manager = new OnlyImageNetUrlShareManager(context, imgInfo, (AppCompatActivity) getActivity(), "WChatFriend");
                            manager.setShare();
                            ShareNumData shareNumData = new ShareNumData(context, list.get(positionWhich).getId());
                            shareNumData.share();
                        }
                    }
                });
                /*QQ好友分享*/
                reQQ.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareDialog.dismiss();
                        if (imgInfo.size() == 1) {
                            onePhotoOfQQFriend();
                        } else {
                            OnlyImageNetUrlShareManager manager = new OnlyImageNetUrlShareManager(context, imgInfo, (AppCompatActivity) getActivity(), "QQFriend");
                            manager.setShare();
                            ShareNumData shareNumData = new ShareNumData(context, list.get(positionWhich).getId());
                            shareNumData.share();
                        }
                    }
                });
                /*微信朋友圈*/
                reWChatCircle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareDialog.dismiss();
                        if (imgInfo.size() == 1) {
                            onePhotoOfWChatCircle();
                        } else {
                            savePhoto2SDOrCircle();
                        }
                    }
                });
            }
        });
        shareDialog.show(getChildFragmentManager());
        shareDialog.setShowBottom(true);
    }

    /*微信好友单张图用sharesdk分享*/
    private void onePhotoOfWChatFriend() {
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setImageUrl(imgInfo.get(0).getUrl());
        sp.setShareType(Platform.SHARE_IMAGE);
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                list.get(positionWhich).setShareNum(String.valueOf((Integer.valueOf(list.get(positionWhich).getShareNum()) + 1)));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
            }

            @Override
            public void onCancel(Platform platform, int i) {
            }
        });
        wechat.share(sp);
        ShareNumData shareNumData = new ShareNumData(context, list.get(positionWhich).getId());
        shareNumData.share();
    }

    /*qq单图分享*/
    private void onePhotoOfQQFriend() {
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setImageUrl(imgInfo.get(0).getUrl());
        sp.setShareType(Platform.SHARE_IMAGE);
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                list.get(positionWhich).setShareNum(String.valueOf((Integer.valueOf(list.get(positionWhich).getShareNum()) + 1)));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
            }

            @Override
            public void onCancel(Platform platform, int i) {
            }
        });
        qq.share(sp);
        ShareNumData shareNumData = new ShareNumData(context, list.get(positionWhich).getId());
        shareNumData.share();
    }

    private void onePhotoOfWChatCircle() {
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setImageUrl(imgInfo.get(0).getUrl());
        sp.setShareType(Platform.SHARE_IMAGE);
        Platform weChat = ShareSDK.getPlatform(WechatMoments.NAME);
        weChat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                list.get(positionWhich).setShareNum(String.valueOf((Integer.valueOf(list.get(positionWhich).getShareNum()) + 1)));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
            }

            @Override
            public void onCancel(Platform platform, int i) {
            }
        });
        weChat.share(sp);
        ShareNumData shareNumData = new ShareNumData(context, list.get(positionWhich).getId());
        shareNumData.share();
    }

    private void savePhoto2SDOrCircle() {
        String description = list.get(positionWhich).getDescription();
        ClipData mClipData = ClipData.newPlainText("Label", description);
        cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(mClipData);
        ClipContentUtil.getInstance(context).putNewSearch(description);//保存记录到数据库
        OnlyNetImageUrlSave2Phone onlyNetImageUrlSave2Phone = new OnlyNetImageUrlSave2Phone(context, imgInfo, (AppCompatActivity) getActivity());
        onlyNetImageUrlSave2Phone.save();
    }

    private void getListData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("pid", pid);
        map.put("cid", cid);
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", "10");
        String mapParam = CommonParamUtil.getOtherParamSign(context, map);
        MyApplication.getInstance().getMyOkHttp().post()
                .url(CommonApi.BASEURL + CommonApi.CIRCLE_GOOD_COMMMEND + mapParam)
                .enqueue(new JsonResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        Log.i("营销素材", response.toString());
                        CircleRecommendBean bean = GsonUtil.GsonToBean(response.toString(), CircleRecommendBean.class);
                        if (bean.getErrno() == CommonApi.RESULTCODEOK) {
                            List<CircleRecommendBean.InfoBean> info = bean.getInfo();
                            if (pageNum == 1) {
                                list.clear();
                                list.addAll(info);
                                adapter.notifyDataSetChanged();
                            } else {
                                list.addAll(info);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        ToastUtils.showToast(context, CommonApi.ERROR_NET_MSG);
                    }
                });
    }

    private void initHorizontalRecyclerview() {
        horizontalRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        horizontalRecyclerView.setLayoutManager(manager);
        horizontalRecyclerView.addItemDecoration(new CidInfoItemSpace(context));
        CidInfoAdapter cidInfoAdapter = new CidInfoAdapter(context, cidinfoList);
        horizontalRecyclerView.setAdapter(cidInfoAdapter);
        if (cidinfoList.size() > 0) {
            cidinfoList.get(0).setChecked(true);
        }
        cidInfoAdapter.setOnClickListener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
                for (CircleLabelBean.CidInfo bean : cidinfoList) {
                    bean.setChecked(false);
                }
                cidinfoList.get(position).setChecked(true);
                cidInfoAdapter.notifyDataSetChanged();
                cid = cidinfoList.get(position).getCid();
                pageNum = 1;
                getListData();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.yingxiaoorxinshouorshangxueyuanfragment;
    }

    @Override
    protected boolean setFragmentTarget() {
        return true;
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

}
