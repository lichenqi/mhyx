package com.lianliantao.yuetuan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.lianliantao.yuetuan.activity.StartActivity;
import com.lianliantao.yuetuan.app_status.AppStatus;
import com.lianliantao.yuetuan.app_status.AppStatusManager;
import com.lianliantao.yuetuan.base_activity.OriginalActivity;
import com.lianliantao.yuetuan.constant.CommonApi;
import com.lianliantao.yuetuan.fragment.BrandFragment;
import com.lianliantao.yuetuan.fragment.HairRingFragment;
import com.lianliantao.yuetuan.fragment.HomeFragment;
import com.lianliantao.yuetuan.fragment.MineFragment;
import com.lianliantao.yuetuan.fragment.UserLevelFragment;
import com.lianliantao.yuetuan.login_and_register.MyWXLoginActivity;
import com.lianliantao.yuetuan.util.PreferUtils;
import com.lianliantao.yuetuan.util.StatusBarUtils;
import com.lianliantao.yuetuan.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends OriginalActivity {
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.iv_circle)
    ImageView ivCircle;
    @BindView(R.id.tv_circle)
    TextView tvCircle;
    @BindView(R.id.ll_circle)
    LinearLayout llCircle;
    @BindView(R.id.iv_money)
    ImageView ivMoney;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.ll_money)
    LinearLayout llMoney;
    @BindView(R.id.iv_ticket)
    ImageView ivTicket;
    @BindView(R.id.tv_ticket)
    TextView tvTicket;
    @BindView(R.id.ll_ticket)
    LinearLayout llTicket;
    @BindView(R.id.iv_mine)
    ImageView ivMine;
    @BindView(R.id.tv_mine)
    TextView tvMine;
    @BindView(R.id.ll_mine)
    LinearLayout llMine;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    private List<ImageView> imageViewList = new ArrayList<>();
    private List<TextView> textViewList = new ArrayList<>();
    private int curIndex = 0;
    private int[] noseIds = {R.mipmap.home_selector, R.mipmap.tab_pinpai_yes, R.drawable.vip_centy, R.mipmap.tab_faquan_yes, R.mipmap.tab_wode_yes};
    private int[] seIds = {R.mipmap.home_unselector, R.mipmap.tab_pinpai_no, R.drawable.vip_centy, R.mipmap.tab_faquan_no, R.mipmap.tab_wode_no};
    private Fragment currentFragment;
    long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppStatusManager.getInstance().getAppStatus() == AppStatus.STATUS_RECYVLE) {
            /*跳转到闪屏页*/
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        StatusBarUtils.transparencyBar(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        imageViewList.add(ivHome);
        imageViewList.add(ivCircle);
        imageViewList.add(ivMoney);
        imageViewList.add(ivTicket);
        imageViewList.add(ivMine);
        textViewList.add(tvHome);
        textViewList.add(tvCircle);
        textViewList.add(tv_money);
        textViewList.add(tvTicket);
        textViewList.add(tvMine);
        replaceFragment(curIndex);
    }

    @OnClick({R.id.ll_home, R.id.ll_circle, R.id.ll_money, R.id.ll_ticket, R.id.ll_mine})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                curIndex = 0;
                replaceFragment(curIndex);
                break;
            case R.id.ll_circle:
                curIndex = 1;
                replaceFragment(curIndex);
                break;
            case R.id.ll_money:
                if (PreferUtils.getBoolean(getApplicationContext(), CommonApi.ISLOGIN)) {
                    curIndex = 2;
                    replaceFragment(curIndex);
                } else {
                    startActivity(new Intent(getApplicationContext(), MyWXLoginActivity.class));
                }
                break;
            case R.id.ll_ticket:
                if (PreferUtils.getBoolean(getApplicationContext(), CommonApi.ISLOGIN)) {
                    curIndex = 3;
                    replaceFragment(curIndex);
                } else {
                    startActivity(new Intent(getApplicationContext(), MyWXLoginActivity.class));
                }
                break;
            case R.id.ll_mine:
                if (PreferUtils.getBoolean(getApplicationContext(), CommonApi.ISLOGIN)) {
                    curIndex = 4;
                    replaceFragment(curIndex);
                } else {
                    startActivity(new Intent(getApplicationContext(), MyWXLoginActivity.class));
                }
                break;
        }
    }

    private void replaceFragment(int tag) {

        for (int i = 0; i < imageViewList.size(); i++) {
            ImageView imageView = imageViewList.get(i);
            TextView textView = textViewList.get(i);
            if (i == curIndex) {
                imageView.setBackgroundResource(noseIds[i]);
                textView.setTextColor(0xff000000);
//                initAnimation(imageView, textView);
                continue;
            }
            imageView.setBackgroundResource(seIds[i]);
            textView.setTextColor(0xff666666);
        }

        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(currentFragment).commitAllowingStateLoss();
        }
        currentFragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(tag));
        if (currentFragment == null) {
            switch (tag) {
                case 0:
                    currentFragment = new HomeFragment();
                    break;
                case 1:
                    currentFragment = new BrandFragment();
                    break;
                case 2:
                    currentFragment = new UserLevelFragment();
                    break;
                case 3:
                    currentFragment = new HairRingFragment();
                    break;
                case 4:
                    currentFragment = new MineFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().add(R.id.fl_container, currentFragment, String.valueOf(tag)).commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction().show(currentFragment).commitAllowingStateLoss();
        }
    }

    private void initAnimation(ImageView iv, TextView tv) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_home_tab);
        iv.startAnimation(animation);
        tv.startAnimation(animation);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String loginout = intent.getStringExtra("loginout");
        if (!TextUtils.isEmpty(loginout)) {
            if (loginout.equals("loginout")) {/*首页*/
                curIndex = 0;
                replaceFragment(curIndex);
            } else if (loginout.equals("vipCenty")) {
                curIndex = 2;
                replaceFragment(curIndex);
            }
        } else {
            curIndex = 0;
            replaceFragment(curIndex);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showToast(getApplicationContext(), "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
