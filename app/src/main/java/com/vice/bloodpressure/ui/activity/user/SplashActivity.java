package com.vice.bloodpressure.ui.activity.user;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;
import com.gyf.immersionbar.ImmersionBar;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.libsteps.StepService;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseActivity;
import com.vice.bloodpressure.base.activity.BaseWebViewActivity;
import com.vice.bloodpressure.bean.ImeiGetBean;
import com.vice.bloodpressure.constant.ConstantParam;
import com.vice.bloodpressure.ui.activity.MainActivity;
import com.vice.bloodpressure.utils.ScreenUtils;
import com.vice.bloodpressure.utils.SharedPreferencesUtilsApp;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述: 启动页
 * 作者: LYD
 * 创建日期: 2019/3/25 15:06
 */
public class SplashActivity extends BaseActivity {
    private long countDownTime = 3000;//单位毫秒
    /**
     * 倒计时
     */
    private CountDownTimer timer;

    /**
     * 隐私政策弹出框
     */
    private Dialog protectDialog;
    private String spanColor = "#FFC600";//隐私政策span颜色值
    /**
     * 是否同意隐私政策，1是，0或空为否
     */
    private String isAgreePricacyProtect;

    private static final String TAG = "LYD";
    AppWakeUpAdapter wakeUpAdapter = new AppWakeUpAdapter() {
        @Override
        public void onWakeUp(AppData appData) {
            //获取绑定数据
            String bindData = appData.getData();
            ImeiGetBean imeiGetBean = GsonUtils.fromJson(bindData, ImeiGetBean.class);
            String imei = imeiGetBean.getIMEI();
            //判断
            LoginBean user = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
            if (user != null) {
                if (!TextUtils.isEmpty(imei)) {
                    SPStaticUtils.put("scanImei", imei);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将window的背景图设置为空
        getWindow().setBackgroundDrawable(null);
        initValues();
        hideTitleBar();
        initStatusBar();
        setSplash();
        //获取唤醒参数
        OpenInstall.getWakeUp(getIntent(), wakeUpAdapter);
        initStepService();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
//            setSplash();
        }
    }

    private void initValues() {
        Map<String, String> map = new HashMap<>();
        map.put(ConstantParam.IS_AGREE_PRIVACY_PROTECT, "0");
        SharedPreferencesUtilsApp.getInfo(getPageContext(), map);
        isAgreePricacyProtect = map.get(ConstantParam.IS_AGREE_PRIVACY_PROTECT);
    }

    /**
     * 初始化计步服务
     */
    private void initStepService() {
        Intent intent = new Intent(this, StepService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //此处要调用，否则App在后台运行时，会无法截获
        OpenInstall.getWakeUp(intent, wakeUpAdapter);
    }

    /**
     * 初始化状态栏
     */
    public void initStatusBar() {
        ImmersionBar.with(this)
                .keyboardEnable(false)
                .statusBarDarkFont(false)
                .fitsSystemWindows(false)
                .statusBarColor(R.color.transparent)
                .init();
    }

    @Override
    protected View addContentLayout() {
        View layout = getLayoutInflater().inflate(R.layout.activity_splash, contentLayout, false);
        return layout;
    }

    /**
     * 设置启动页
     */
    private void setSplash() {
        //        new Handler().postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                if (!"1".equals(isAgreePricacyProtect)) {
        //                    showPrivacyProtectDialog();
        //                } else {
        //                    LoginBean user = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        //                    if (user != null) {
        //                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        //                        finish();
        //                    } else {
        //                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        //                        finish();
        //                    }
        //
        //                }
        //            }
        //        }, 1000);



        if ("1".equals(isAgreePricacyProtect)) {
            Log.i("yys", "111isAgreePricacyProtect===" + isAgreePricacyProtect);
            timer = new CountDownTimer(countDownTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    LoginBean user = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
                    if (user != null) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            };
            timer.start();
        } else {
            Log.i("yys", "000isAgreePricacyProtect===" + isAgreePricacyProtect);
            showPrivacyProtectDialog();
        }
    }

    /**
     * 隐私权限提示
     */
    private void showPrivacyProtectDialog() {
        if (protectDialog == null) {
            protectDialog = new Dialog(getPageContext(), R.style.Dialog_Base);
            View view = View.inflate(getPageContext(), R.layout.dialog_privacy_protect, null);
            protectDialog.setContentView(view);
            WindowManager.LayoutParams attributes = protectDialog.getWindow().getAttributes();
            attributes.width = 4 * ScreenUtils.screenWidth(getPageContext()) / 5;

            attributes.height = ScreenUtils.dip2px(getPageContext(), 430);
            protectDialog.getWindow().setAttributes(attributes);
            protectDialog.setCancelable(false);

            TextView serviceAgreementTextView = view.findViewById(R.id.tv_dpp_service_agreement);
            TextView disagreeTextView = view.findViewById(R.id.tv_dpp_disagree);
            TextView agressTextView = view.findViewById(R.id.tv_dpp_agree);

            String privacyProtectHint = getString(R.string.privacy_protect_hint);
            SpannableString ss = new SpannableString(privacyProtectHint);

            ss.setSpan(new UnderLineClickSpan() {
                @Override
                public void onClick(View widget) {
                    jumpToUserPrivacy();
                }
            }, privacyProtectHint.indexOf("《"), privacyProtectHint.indexOf("》") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.parseColor(spanColor)), privacyProtectHint.indexOf("《"), privacyProtectHint.indexOf("》") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            serviceAgreementTextView.setText(ss);
            serviceAgreementTextView.setHighlightColor(Color.TRANSPARENT);
            serviceAgreementTextView.setMovementMethod(LinkMovementMethod.getInstance());

            disagreeTextView.setOnClickListener(v -> {
                protectDialog.dismiss();
                finish();
            });
            agressTextView.setOnClickListener(v -> {
                protectDialog.dismiss();
                SharedPreferencesUtilsApp.saveInfo(getPageContext(), ConstantParam.IS_AGREE_PRIVACY_PROTECT, "1");
                isAgreePricacyProtect = "1";
                startActivity(new Intent(getPageContext(), LoginActivity.class));
                finish();
            });
            protectDialog.show();
        }
    }

    /**
     * 页面跳转-用户政策
     */
    private void jumpToUserPrivacy() {
        Intent intent = new Intent(getPageContext(), BaseWebViewActivity.class);
        intent.putExtra("title", "用户服务协议");
        intent.putExtra("url", "file:///android_asset/user_protocol.html");
        startActivity(intent);
    }


    private abstract class UnderLineClickSpan extends ClickableSpan {
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
            ds.clearShadowLayer();
        }
    }

    @Override
    protected void onDestroy() {
        wakeUpAdapter = null;
        super.onDestroy();
    }
}
