/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.lianliantao.yuetuan.share.themes.classic.port;


import com.lianliantao.yuetuan.share.OnekeyShareThemeImpl;
import com.lianliantao.yuetuan.share.themes.classic.PlatformPage;
import com.lianliantao.yuetuan.share.themes.classic.PlatformPageAdapter;

import java.util.ArrayList;


/** 竖屏的九宫格页面 */
public class PlatformPagePort extends PlatformPage {

	public PlatformPagePort(OnekeyShareThemeImpl impl) {
		super(impl);
	}

	public void onCreate() {
		requestSensorPortraitOrientation();
		super.onCreate();
	}

	protected PlatformPageAdapter newAdapter(ArrayList<Object> cells) {
		return new PlatformPageAdapterPort(this, cells);
	}

}
