package com.vice.bloodpressure.ui.activity.user;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
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

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.huawei.HuaWeiRegister;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.alibaba.sdk.android.push.register.OppoRegister;
import com.alibaba.sdk.android.push.register.VivoRegister;
import com.allen.library.RxHttpUtils;
import com.allen.library.bean.BaseData;
import com.allen.library.interceptor.Transformer;
import com.allen.library.observer.CommonObserver;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.clj.fastble.BleManager;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;
import com.gyf.immersionbar.ImmersionBar;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.libsteps.StepService;
import com.lzx.starrysky.StarrySky;
import com.umeng.commonsdk.UMConfigure;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseActivity;
import com.vice.bloodpressure.base.activity.BaseWebViewActivity;
import com.vice.bloodpressure.bean.CheckAdviceBean;
import com.vice.bloodpressure.bean.ImeiGetBean;
import com.vice.bloodpressure.bean.im.ImWarningMessage;
import com.vice.bloodpressure.bean.im.ImWarningMessageContentBean;
import com.vice.bloodpressure.bean.im.ImWarningMessageProvider;
import com.vice.bloodpressure.constant.ConstantParam;
import com.vice.bloodpressure.imp.ImWarningClickListener;
import com.vice.bloodpressure.net.OkHttpInstance;
import com.vice.bloodpressure.net.Service;
import com.vice.bloodpressure.net.XyUrl;
import com.vice.bloodpressure.ui.activity.MainActivity;
import com.vice.bloodpressure.ui.activity.im.DoctorAdviceActivity;
import com.vice.bloodpressure.utils.ScreenUtils;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import okhttp3.OkHttpClient;

/**
 * ??????: ?????????
 * ??????: LYD
 * ????????????: 2019/3/25 15:06
 */
public class SplashActivity extends BaseActivity implements RongIMClient.ConnectionStatusListener, RongIMClient.OnReceiveMessageListener, RongIM.ConversationListBehaviorListener{
    private long countDownTime = 3000;//????????????
    /**
     * ?????????
     */
    private CountDownTimer timer;

    /**
     * ?????????????????????
     */
    private Dialog protectDialog;
    private String spanColor = "#FFC600";//????????????span?????????

    private static final String TAG = "LYD";
    AppWakeUpAdapter wakeUpAdapter = new AppWakeUpAdapter() {
        @Override
        public void onWakeUp(AppData appData) {
            //??????????????????
            String bindData = appData.getData();
            ImeiGetBean imeiGetBean = GsonUtils.fromJson(bindData, ImeiGetBean.class);
            String imei = imeiGetBean.getIMEI();
            //??????
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
        //???window????????????????????????
//        MobclickAgent.setDebugMode( true );
        UMConfigure.setLogEnabled(true);
        getWindow().setBackgroundDrawable(null);
        hideTitleBar();
        initStatusBar();
        setSplash();
        //??????????????????
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


    /**
     * ?????????????????????
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
        //????????????????????????App????????????????????????????????????
        OpenInstall.getWakeUp(intent, wakeUpAdapter);
    }

    /**
     * ??????????????????
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
     * ???????????????
     */
    private void setSplash() {
        if ("1".equals(SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.IS_AGREE))) {
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
            showPrivacyProtectDialog();
        }
    }

    /**
     * ??????????????????
     */
    private void showPrivacyProtectDialog() {
        if (protectDialog == null) {
            protectDialog = new Dialog(getPageContext(), R.style.Dialog_Base);
            View view = View.inflate(getPageContext(), R.layout.dialog_privacy_protect, null);
            protectDialog.setContentView(view);
            WindowManager.LayoutParams attributes = protectDialog.getWindow().getAttributes();
            attributes.width = 4 * ScreenUtils.screenWidth(getPageContext()) / 5;

            attributes.height = ScreenUtils.dip2px(getPageContext(), 400);
            protectDialog.getWindow().setAttributes(attributes);
            protectDialog.setCancelable(false);

            TextView serviceAgreementTextView = view.findViewById(R.id.tv_dpp_service_agreement);
            TextView disagreeTextView = view.findViewById(R.id.tv_dpp_disagree);
            TextView agressTextView = view.findViewById(R.id.tv_dpp_agree);

            String privacyProtectHint = getString(R.string.privacy_protect_hint1);
            SpannableString ss = new SpannableString(privacyProtectHint);

            ss.setSpan(new UnderLineClickSpan() {
                @Override
                public void onClick(View widget) {
                    jumpToUserPrivacy();
                }
            }, privacyProtectHint.indexOf("???"), privacyProtectHint.indexOf("???") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.parseColor(spanColor)), privacyProtectHint.indexOf("???"), privacyProtectHint.indexOf("???") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            ss.setSpan(new UnderLineClickSpan() {
                @Override
                public void onClick(View widget) {
                    jumpToUserAgreement();
                }
            }, privacyProtectHint.lastIndexOf("???"), privacyProtectHint.lastIndexOf("???") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.parseColor(spanColor)), privacyProtectHint.lastIndexOf("???"), privacyProtectHint.lastIndexOf("???") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            serviceAgreementTextView.setText(ss);
            serviceAgreementTextView.setHighlightColor(Color.TRANSPARENT);
            serviceAgreementTextView.setMovementMethod(LinkMovementMethod.getInstance());

            disagreeTextView.setOnClickListener(v -> {
                protectDialog.dismiss();
                finish();
            });
            agressTextView.setOnClickListener(v -> {
                SharedPreferencesUtils.putBean(getPageContext(), SharedPreferencesUtils.IS_AGREE, "1");
                initJPush();
                initIm();
                initRxHttp();
                initBle();
                initOpenInstall();
                initBugly();
                initAliPush();
                initAudio();
                initUmeng();
                protectDialog.dismiss();
                startActivity(new Intent(getPageContext(), LoginActivity.class));
                finish();
            });
            protectDialog.show();
        }
    }


    private void initUmeng() {
        /**
         * ??????: ??????????????????AndroidManifest.xml????????????appkey???channel??????????????????App????????????
         * ????????????????????????????????????AndroidManifest.xml???????????????appkey???channel??????
         * UMConfigure.init?????????appkey???channel???????????????null??????
         */
        UMConfigure.init(this, "5d64a0be4ca3579c70000b20", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        //        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        // ??????????????????????????????????????????
        UMConfigure.setProcessEvent(true);
    }


    private void initAudio() {
        //        StarrySkyConfig config = new StarrySkyConfig().newBuilder()
        //                .build();
        //        StarrySky.init(this, config, null);
        StarrySky.init(getApplication()).apply();
    }
    /**
     * ????????????????????????
     */
    private void initAliPush() {
        createNotificationChannel();
        PushServiceFactory.init(getApplication());
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(this, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.e("aliPush", "???????????????");
                String deviceId = pushService.getDeviceId();
                Log.e("aliPush", "??????deviceId==" + deviceId);
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.e("aliPush", "???????????????-- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
        //??????????????????
        MiPushRegister.register(this, "2882303761517853353", "5511785339353");
        //??????????????????
        HuaWeiRegister.register(getApplication());
        // OPPO????????????
        OppoRegister.register(this, "63d86bfd3a5441929cc66f5f11e9dabb", "91f24bf3f35b4e0daffeb0625e100f1f");
        // VIVO????????????
        VivoRegister.register(this);
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //???????????????id
            String id = "1";
            //??????????????????????????????????????????.
            CharSequence name = "notification channel";
            //??????????????????????????????????????????
            String description = "notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            //???????????????????????????
            mChannel.setDescription(description);
            //??????????????????????????????????????? android ?????????????????????
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            //??????????????????????????????????????? android ?????????????????????
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            //?????????notificationmanager????????????????????????
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    private void initBugly() {
        //???????????????????????????true?????????????????????false
        //CrashReport.initCrashReport(getApplicationContext(), BuildConfig.BUGLY_ID, false);
    }


    private void initOpenInstall() {
        if (isMainProcess()) {
            OpenInstall.setDebug(true);
            OpenInstall.init(this);
        }
    }

    public boolean isMainProcess() {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return getApplicationInfo().packageName.equals(appProcess.processName);
            }
        }
        return false;
    }

    /**
     * ?????????????????????
     */
    private void initJPush() {
        //JPushInterface.setDebugMode(true);
        //JPushInterface.init(this);
        //JPushUtils.init();
    }


    /**
     * ??????
     */
    private void initBle() {
        BleManager.getInstance().init(getApplication());
    }

    /**
     * ?????????
     */
    private void initRxHttp() {
        OkHttpClient okClient = OkHttpInstance.createInstance();
        RxHttpUtils
                .getInstance()
                .init(getApplication())
                .config()
                .setBaseUrl(XyUrl.HOST_URL)
                .setOkClient(okClient);
    }


    /**
     * ???????????????
     */
    private void initIm() {
        //        PushConfig config = new PushConfig.Builder()
        //                .enableMiPush(ConstantParam.MI_PUSH_ID, ConstantParam.MI_PUSH_KEY)
        //                .enableOppoPush(ConstantParam.OPPO_PUSH_KEY, ConstantParam.OPPO_PUSH_APP_SECRET)
        //                .enableVivoPush(true)
        //                .enableHWPush(false)
        //                .build();
        //        RongPushClient.setPushConfig(config);
        RongIM.init(this, ConstantParam.IM_KEY);
        //??????????????????
        RongIM.setConnectionStatusListener(this);
        //???????????????????????????
        RongIM.registerMessageType(ImWarningMessage.class);
        //??????????????????
        RongIM.setOnReceiveMessageListener(this);
        //??????????????????
        RongIM.registerMessageTemplate(new ImWarningMessageProvider(new ImWarningClickListener() {
            @Override
            public void onCardClick(View view, ImWarningMessage content) {
                getCheckAdvice(view, content);
            }
        }));
        //????????????????????????
        RongIM.setConversationListBehaviorListener(this);
    }

    private void getCheckAdvice(View view, ImWarningMessage content) {
        String getContent = content.getContent();
        ImWarningMessageContentBean bean = GsonUtils.fromJson(getContent, ImWarningMessageContentBean.class);
        int wid = bean.getWid();
        int type = bean.getType();
        String typeName = bean.getTypename();
        String val = bean.getVal();
        LoginBean user = (LoginBean) SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.USER_INFO);
        String token = user.getToken();

        RxHttpUtils.createApi(Service.class)
                .checkAdvice(token, wid + "")
                .compose(Transformer.switchSchedulers())
                .subscribe(new CommonObserver<BaseData<CheckAdviceBean>>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(BaseData<CheckAdviceBean> checkAdviceBeanBaseData) {
                        CheckAdviceBean data = checkAdviceBeanBaseData.getData();
                        String advice = data.getContent();
                        //2?????????
                        Intent intent = new Intent(SplashActivity.this, DoctorAdviceActivity.class);
                        intent.putExtra("advice", advice);
                        intent.putExtra("type", type + "");
                        intent.putExtra("typeName", typeName);
                        intent.putExtra("val", val);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
    }








    /**
     * ????????????-????????????
     */
    private void jumpToUserPrivacy() {
        Intent intent = new Intent(getPageContext(), BaseWebViewActivity.class);
        intent.putExtra("title", "??????????????????");
        intent.putExtra("url", "file:///android_asset/user_protocol.html");
        startActivity(intent);
    }

    /**
     * ????????????-????????????
     */
    private void jumpToUserAgreement() {
        Intent intent = new Intent(getPageContext(), com.lyd.modulemall.ui.BaseWebViewActivity.class);
        intent.putExtra("title", "????????????");
        intent.putExtra("url", "http://chronics.xiyuns.cn/index/caseapp");
        startActivity(intent);
    }

    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }

    @Override
    public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {

    }

    @Override
    public boolean onReceived(Message message, int i) {
        return false;
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
