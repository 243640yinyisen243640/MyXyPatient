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
import com.vice.bloodpressure.utils.SharedPreferencesUtilsApp;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import okhttp3.OkHttpClient;

/**
 * 描述: 启动页
 * 作者: LYD
 * 创建日期: 2019/3/25 15:06
 */
public class SplashActivity extends BaseActivity implements RongIMClient.ConnectionStatusListener, RongIMClient.OnReceiveMessageListener, RongIM.ConversationListBehaviorListener{
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
//        MobclickAgent.setDebugMode( true );
        UMConfigure.setLogEnabled(true);
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
     * 隐私权限提示
     */
    private void showPrivacyProtectDialog() {
        if (protectDialog == null) {
            protectDialog = new Dialog(getPageContext(), R.style.Dialog_Base);
            View view = View.inflate(getPageContext(), R.layout.dialog_privacy_protect, null);
            protectDialog.setContentView(view);
            WindowManager.LayoutParams attributes = protectDialog.getWindow().getAttributes();
            attributes.width = 4 * ScreenUtils.screenWidth(getPageContext()) / 5;

            attributes.height = ScreenUtils.dip2px(getPageContext(), 480);
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

            ss.setSpan(new UnderLineClickSpan() {
                @Override
                public void onClick(View widget) {
                    jumpToUserAgreement();
                }
            }, privacyProtectHint.lastIndexOf("《"), privacyProtectHint.lastIndexOf("》") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.parseColor(spanColor)), privacyProtectHint.lastIndexOf("《"), privacyProtectHint.lastIndexOf("》") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

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
                SharedPreferencesUtilsApp.saveInfo(getPageContext(), ConstantParam.IS_AGREE_PRIVACY_PROTECT, "1");
                isAgreePricacyProtect = "1";
                startActivity(new Intent(getPageContext(), LoginActivity.class));
                finish();
            });
            protectDialog.show();
        }
    }


    private void initUmeng() {
        /**
         * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
         * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
         * UMConfigure.init调用中appkey和channel参数请置为null）。
         */
        UMConfigure.init(this, "5d64a0be4ca3579c70000b20", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        //        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        // 支持在子进程中统计自定义事件
        UMConfigure.setProcessEvent(true);
    }


    private void initAudio() {
        //        StarrySkyConfig config = new StarrySkyConfig().newBuilder()
        //                .build();
        //        StarrySky.init(this, config, null);
        StarrySky.init(getApplication()).apply();
    }
    /**
     * 初始化云推送通道
     */
    private void initAliPush() {
        createNotificationChannel();
        PushServiceFactory.init(getApplication());
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(this, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.e("aliPush", "初始化成功");
                String deviceId = pushService.getDeviceId();
                Log.e("aliPush", "设备deviceId==" + deviceId);
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.e("aliPush", "初始化失败-- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
        //小米通道注册
        MiPushRegister.register(this, "2882303761517853353", "5511785339353");
        //华为通道注册
        HuaWeiRegister.register(getApplication());
        // OPPO通道注册
        OppoRegister.register(this, "63d86bfd3a5441929cc66f5f11e9dabb", "91f24bf3f35b4e0daffeb0625e100f1f");
        // VIVO通道注册
        VivoRegister.register(this);
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //通知渠道的id
            String id = "1";
            //用户可以看到的通知渠道的名字.
            CharSequence name = "notification channel";
            //用户可以看到的通知渠道的描述
            String description = "notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            //配置通知渠道的属性
            mChannel.setDescription(description);
            //设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            //设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    private void initBugly() {
        //测试阶段建议设置成true，发布时设置为false
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
     * 初始化极光推送
     */
    private void initJPush() {
        //JPushInterface.setDebugMode(true);
        //JPushInterface.init(this);
        //JPushUtils.init();
    }


    /**
     * 蓝牙
     */
    private void initBle() {
        BleManager.getInstance().init(getApplication());
    }

    /**
     * 初始化
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
     * 初始化融云
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
        //实现单点登录
        RongIM.setConnectionStatusListener(this);
        //注册自定义消息类型
        RongIM.registerMessageType(ImWarningMessage.class);
        //注册消息监听
        RongIM.setOnReceiveMessageListener(this);
        //注册消息模板
        RongIM.registerMessageTemplate(new ImWarningMessageProvider(new ImWarningClickListener() {
            @Override
            public void onCardClick(View view, ImWarningMessage content) {
                getCheckAdvice(view, content);
            }
        }));
        //会话列表操作监听
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
                        //2已处理
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
     * 页面跳转-用户政策
     */
    private void jumpToUserPrivacy() {
        Intent intent = new Intent(getPageContext(), BaseWebViewActivity.class);
        intent.putExtra("title", "用户服务协议");
        intent.putExtra("url", "file:///android_asset/user_protocol.html");
        startActivity(intent);
    }

    /**
     * 页面跳转-用户政策
     */
    private void jumpToUserAgreement() {
        Intent intent = new Intent(getPageContext(), com.lyd.modulemall.ui.BaseWebViewActivity.class);
        intent.putExtra("title", "隐私政策");
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
