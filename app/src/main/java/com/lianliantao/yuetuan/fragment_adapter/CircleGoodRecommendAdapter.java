package com.lianliantao.yuetuan.fragment_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lianliantao.yuetuan.R;
import com.lianliantao.yuetuan.activity.Circle2PhotoLookActivity;
import com.lianliantao.yuetuan.activity.MultiGoodsCompoundPhotoActivity;
import com.lianliantao.yuetuan.app_manage.MyApplication;
import com.lianliantao.yuetuan.bean.CircleRecommendBean;
import com.lianliantao.yuetuan.bean.PromotionlinkBean;
import com.lianliantao.yuetuan.common_manager.CommonParamUtil;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.custom_view.CircleImageView;
import com.lianliantao.yuetuan.myokhttputils.response.JsonResponseHandler;
import com.lianliantao.yuetuan.port_inner.OnItemClick;
import com.lianliantao.yuetuan.util.GsonUtil;
import com.lianliantao.yuetuan.util.MoneyFormatUtil;
import com.lianliantao.yuetuan.util.NumUtil;
import com.lianliantao.yuetuan.util.ToastUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CircleGoodRecommendAdapter extends RecyclerView.Adapter<CircleGoodRecommendAdapter.MyHolder> {

    private Context context;
    private List<CircleRecommendBean.InfoBean> list;
    private OnItemClick shopDetailClick, copyContentClick, copyTaobaoLinkClick, shareClick;
    private String itemId;

    /*长按复制商品内容*/
    public void setOnLongContentClickListener(OnItemClick copyContentClick) {
        this.copyContentClick = copyContentClick;
    }

    /*进入商品详情页面*/
    public void setOnShopDetailClickListener(OnItemClick shopDetailClick) {
        this.shopDetailClick = shopDetailClick;
    }

    /*复制淘宝令评论*/
    public void setOnTaobaoLinkClickListener(OnItemClick copyTaobaoLinkClick) {
        this.copyTaobaoLinkClick = copyTaobaoLinkClick;
    }

    /*分享按钮*/
    public void setOnShareClickListener(OnItemClick shareClick) {
        this.shareClick = shareClick;
    }

    public CircleGoodRecommendAdapter(Context context, List<CircleRecommendBean.InfoBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.circlegoodrecommendadapter, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        CircleRecommendBean.InfoBean infoBean = list.get(position);
        Glide.with(context).load(infoBean.getAuthorLogo()).into(holder.iv);
        holder.nickName.setText(infoBean.getAuthor());
        holder.time.setText(infoBean.getCreateTime());
        holder.content.setText(infoBean.getDescription());
        List<CircleRecommendBean.InfoBean.ImgInfoBean> imgInfoList = infoBean.getImgInfo();
        holder.recyclerview.setHasFixedSize(true);
        holder.recyclerview.setLayoutManager(new GridLayoutManager(context, 3));
        NinePhotoAdapter adapter = new NinePhotoAdapter(context, imgInfoList);
        holder.recyclerview.setAdapter(adapter);
        holder.shareMoney.setText("分享赚 ¥ " + MoneyFormatUtil.StringFormatWithYuan(infoBean.getEstimatedEarn()));
        holder.shareButton.setText(infoBean.getShareNum());
        CircleRecommendBean.InfoBean.GoodsInfoBean goodsInfo = infoBean.getGoodsInfo();
        if (goodsInfo == null) {
            holder.viewOne.setVisibility(View.GONE);
            holder.goShopdetail.setVisibility(View.GONE);
            holder.viewTwo.setVisibility(View.GONE);
            holder.reTaoParent.setVisibility(View.GONE);
        } else {
            itemId = goodsInfo.getItemId();
            holder.viewOne.setVisibility(View.VISIBLE);
            holder.goShopdetail.setVisibility(View.VISIBLE);
            holder.viewTwo.setVisibility(View.VISIBLE);
            holder.reTaoParent.setVisibility(View.VISIBLE);
            Glide.with(context).load(goodsInfo.getPictUrl()).into(holder.riv);
            holder.title.setText(goodsInfo.getTitle());
            holder.price.setText("券后价 ¥ " + MoneyFormatUtil.StringFormatWithYuan(goodsInfo.getPayPrice()));
            holder.saleNum.setText(NumUtil.getNum(goodsInfo.getVolume()) + "人购买");
            holder.k_price.setText(goodsInfo.getRecommendInfo());
        }
        /*长按复制商品内容*/
        if (copyContentClick != null) {
            holder.content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    copyContentClick.OnItemClickListener(holder.content, holder.getAdapterPosition());
                    return false;
                }
            });
        }
        /*点击进入商品详情页面*/
        if (shopDetailClick != null) {
            holder.goShopdetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopDetailClick.OnItemClickListener(holder.goShopdetail, holder.getAdapterPosition());
                }
            });
        }
        /*淘宝令复制*/
        if (copyTaobaoLinkClick != null) {
            holder.copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    copyTaobaoLinkClick.OnItemClickListener(holder.copy, holder.getAdapterPosition());
                }
            });
        }
        /*分享按钮*/
        if (shareClick != null) {
            holder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareClick.OnItemClickListener(holder.shareButton, holder.getAdapterPosition());
                }
            });
        }
        adapter.setOnClickListener(new OnItemClick() {
            @Override
            public void OnItemClickListener(View view, int position) {
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
                                    if (goodsInfo == null) {
                                        /*多个商品*/
                                        Intent intent = new Intent(context, MultiGoodsCompoundPhotoActivity.class);
                                        intent.putExtra("imageList", (Serializable) imgInfoList);
                                        intent.putExtra("position", position);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    } else {
                                        /*只有单个商品*/
                                        String itemId = goodsInfo.getItemId();
                                        Intent intent = new Intent(context, Circle2PhotoLookActivity.class);
                                        intent.putExtra("itemId", itemId);
                                        intent.putExtra("imageList", (Serializable) imgInfoList);
                                        intent.putExtra("position", position);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }
                                } else if (errno == 434) {/*未备案*/
                                    ToastUtils.showBackgroudCenterToast(context, "请先淘宝渠道认证");
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
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv)
        CircleImageView iv;
        @BindView(R.id.nickName)
        TextView nickName;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.riv)
        RoundedImageView riv;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.saleNum)
        TextView saleNum;
        @BindView(R.id.k_price)
        TextView k_price;
        @BindView(R.id.copy)
        TextView copy;
        @BindView(R.id.shareMoney)
        TextView shareMoney;
        @BindView(R.id.shareButton)
        TextView shareButton;
        @BindView(R.id.recyclerview)
        RecyclerView recyclerview;
        @BindView(R.id.goShopdetail)
        RelativeLayout goShopdetail;
        @BindView(R.id.reTaoParent)
        RelativeLayout reTaoParent;
        @BindView(R.id.viewTwo)
        View viewTwo;
        @BindView(R.id.viewOne)
        View viewOne;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
