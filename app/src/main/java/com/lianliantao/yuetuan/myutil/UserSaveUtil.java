package com.lianliantao.yuetuan.myutil;

import android.content.Context;
import android.text.TextUtils;

import com.lianliantao.yuetuan.bean.LoginBean;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.util.PreferUtils;

import cn.jpush.android.api.JPushInterface;

public class UserSaveUtil {

    /*存储用户信息*/
    public static void saveData(Context context, LoginBean.UserInfoBean userInfo) {
        PreferUtils.putBoolean(context, CommonApi.ISLOGIN, true);
        PreferUtils.putString(context, "avatar", userInfo.getAvatar());/*存储用户微信头像*/
        PreferUtils.putString(context, "uId", userInfo.getUId());/*存储用户Id*/
        PreferUtils.putString(context, "mobile", userInfo.getMobile());/*存储用户手机号*/
        PreferUtils.putString(context, "nickName", userInfo.getNickName());/*存储用户昵称*/
        PreferUtils.putString(context, "invitationCode", userInfo.getInvitationCode());/*存储用户邀请码*/
        PreferUtils.putString(context, "userRank", userInfo.getUserRank());/*存储用户等级别名*/
        PreferUtils.putString(context, "level", userInfo.getLevel());/*存储用户等级*/
        PreferUtils.putString(context, "authUrl", userInfo.getAuthUrl());/*存储用户淘宝备案授权地址*/
        PreferUtils.putString(context, "appLink", userInfo.getAppLink());/*存储用户海报二维码信息*/
        PreferUtils.putString(context, "hasBindTbk", userInfo.getHasBindTbk());/*存储用户是否淘宝备案标识*/
        PreferUtils.putString(context, "wechatNumber", userInfo.getWechatNumber());/*存储用户设置的微信号字段*/
        JPushInterface.setAlias(context, 0, userInfo.getMobile());/*用户名作为推送别名*/
    }

    /*关于uid存储为空处理*/
    public static void aboutuIdSave(Context context) {
        String uId = PreferUtils.getString(context, "uId");
        if (TextUtils.isEmpty(uId) || uId == null) {
            PreferUtils.putString(context, "uId", "");/*/*存储游客id  默认为空字符串/ */
        }
    }
}
