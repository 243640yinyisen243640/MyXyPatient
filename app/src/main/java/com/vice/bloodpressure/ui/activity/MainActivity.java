package com.vice.bloodpressure.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.azhon.appupdate.listener.OnDownloadListener;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.imuxuan.floatingview.FloatingView;
import com.lyd.baselib.BuildConfig;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.constant.BaseConstantParam;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.eventbus.EventBusUtils;
import com.lyd.baselib.utils.eventbus.EventMessage;
import com.lyd.modulemall.ui.activity.ProductDetailActivity;
import com.lyd.modulemall.ui.activity.user.MyCouponListActivity;
import com.lyd.modulemall.ui.fragment.MallHomeFragment;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseHandlerEventBusActivity;
import com.vice.bloodpressure.base.activity.BaseWebViewActivity;
import com.vice.bloodpressure.bean.AdverInfo;
import com.vice.bloodpressure.bean.MyDoctorBean;
import com.vice.bloodpressure.bean.RongUserBean;
import com.vice.bloodpressure.bean.RongYunBean;
import com.vice.bloodpressure.bean.ShopTitleBean;
import com.vice.bloodpressure.bean.UpdateBean;
import com.vice.bloodpressure.bean.insulin.MSTRecordDataInfo;
import com.vice.bloodpressure.constant.ConstantParam;
import com.vice.bloodpressure.event.MSTBlueEventBus;
import com.vice.bloodpressure.net.OkHttpCallBack;
import com.vice.bloodpressure.net.XyUrl;
import com.vice.bloodpressure.ui.activity.mydevice.InputImeiActivity;
import com.vice.bloodpressure.ui.fragment.main.HomeFragment;
import com.vice.bloodpressure.ui.fragment.main.MallWebFragment;
import com.vice.bloodpressure.ui.fragment.main.OutOfHospitalBindFragment;
import com.vice.bloodpressure.ui.fragment.main.OutOfHospitalFragment;
import com.vice.bloodpressure.ui.fragment.main.RegistrationFragment;
import com.vice.bloodpressure.ui.fragment.main.UserFragment;
import com.vice.bloodpressure.utils.BleMSTUtils;
import com.vice.bloodpressure.utils.BlueUtils;
import com.vice.bloodpressure.utils.DialogUtils;
import com.vice.bloodpressure.utils.MySPUtils;
import com.vice.bloodpressure.utils.NotificationUtils;
import com.vice.bloodpressure.utils.ScreenUtils;
import com.vice.bloodpressure.utils.UpdateUtils;
import com.vice.bloodpressure.view.NumberProgressBar;
import com.vice.bloodpressure.view.popu.AdvertisementPop;
import com.vice.bloodpressure.view.popu.CouponPop;
import com.vice.bloodpressure.view.popu.GoodsDetailsPop;
import com.vice.bloodpressure.view.popu.HomeGuidePopup;
import com.vice.bloodpressure.view.popu.NoNeedPop;
import com.vice.bloodpressure.view.popu.UpdatePopup;
import com.wei.android.lib.colorview.view.ColorTextView;
import com.xiaomi.mipush.sdk.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import razerdp.basepopup.BasePopupWindow;
import retrofit2.Call;

/**
 * 描述:
 * 作者: LYD
 * 创建日期: 2020/4/15 13:45
 */
public class MainActivity extends BaseHandlerEventBusActivity implements View.OnClickListener, IUnReadMessageObserver, OnDownloadListener {
    private static final String TAG = "MainActivity";
    private static final int GET_SHOP_TITLE = 10011;
    private static final int GET_UPDATE_DATA = 10012;
    private static final int GET_IM_TOKEN = 10013;
    private static final int BIND = 10014;
    private static final int NO_BIND = 10015;

    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.img_main_home)
    ImageView imgMainHome;
    @BindView(R.id.tv_main_home)
    TextView tvMainHome;
    @BindView(R.id.rl_main_home)
    RelativeLayout rlMainHome;
    @BindView(R.id.img_main_register)
    ImageView imgMainRegister;
    @BindView(R.id.tv_main_register)
    TextView tvMainRegister;
    @BindView(R.id.rl_main_register)
    RelativeLayout rlMainRegister;
    @BindView(R.id.img_main_mall)
    ImageView imgMainMall;
    @BindView(R.id.tv_main_mall)
    TextView tvMainMall;
    @BindView(R.id.rl_main_mall)
    RelativeLayout rlMainMall;
    @BindView(R.id.img_main_me)
    ImageView imgMainMe;
    @BindView(R.id.tv_main_me)
    TextView tvMainMe;
    @BindView(R.id.rl_main_me)
    RelativeLayout rlMainMe;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.tv_main_outside)
    TextView tvMainOutside;
    @BindView(R.id.rl_main_outside)
    RelativeLayout rlMainOutside;
    @BindView(R.id.tv_red_point)
    ColorTextView tvRedPoint;
    @BindView(R.id.tv_red_point_im)
    ColorTextView tvRedImPoint;

    private boolean isHaveUpdate;
    private HomeGuidePopup homeGuideEducationPopup;
    private HomeGuidePopup homeGuideDietPopup;
    private HomeGuidePopup homeGuideSportPopup;

    //Update Start
    //更新弹窗
    private UpdatePopup updatePopup;
    //更新的apk新版本号
    private AppCompatTextView tvUpdateName;
    //更新的新apk大小
    private AppCompatTextView tvUpdateSize;
    //更新内容
    private AppCompatTextView tvUpdateContent;
    //进度条
    private NumberProgressBar pbUpdateProgress;
    //升级按钮
    private AppCompatTextView tvUpdateUpdate;
    //关闭按钮(点击取消)
    private AppCompatImageView ivUpdateClose;
    private LinearLayout closeLinearLayout;
    //更新网址
    private String updateUrl;
    //下载的apk文件
    private File updateApk;
    //Update end

    private String mallUrl;
    private String registrationUrl;
    private String registrationStr;

    private OutOfHospitalFragment outOfHospitalFragment;

    // APP_ID 替换为你的应用从官方网站申请到的合法appID
    private static final String APP_ID = "wxbedc85b967f57dc8";
    // IWXAPI 是第三方app和微信通信的openApi接口
    private IWXAPI api;

    private AdvertisementPop advertisementPop;
    private GoodsDetailsPop goodsDetailsPop;
    private NoNeedPop noNeedPop;
    private CouponPop couponPop;

    private ImageView backgroudImageView;
    private ImageView closeImageView;

    private ImageView backgroudGoodsImageView;
    private ImageView closeGoodsImageView;

    private ImageView backgroudNoNeedImageView;
    private TextView sureTextView;
    private ImageView noNeedcloseImageView;

    private ImageView couponImageView;
    private ImageView couponcloseImageView;


    private AdverInfo outadverInfo;
    private AdverInfo goodsAdverInfo;
    private AdverInfo noadverInfo;
    private AdverInfo couponInfo;


    @Override
    protected View addContentLayout() {
        View layout = getLayoutInflater().inflate(R.layout.activity_main, contentLayout, false);
        return layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BlueUtils.init(this, getHandler());
        //迈士通蓝牙回调
        initMSTBlue();
        BlueUtils.callBackListener(this, getHandler());
        regToWx();
        //添加第一个Fragment
        addFirstFragment();
        //初始化血糖弹窗
        initPopup();
        //设置极光推送别名
        setUPushAlias();
        //设置标题
        getShopTitle();
        //获取融云token
        getImToken();
        //更新APP
        getUpdate();
        //获取融云消息未读数量
        getImUnReadMsgCount();
        //获取唤醒参数
        toSubmitScan();
        //设置用户信息代理
        setRongImUserInfo();
        getImMessageType();
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).statusBarColor(R.color.main_home).init();
    }

    private void initMSTBlue() {
        String mac = MySPUtils.getString(getPageContext(), MySPUtils.BLUE_MAC);
        if (!TextUtils.isEmpty(mac)) {
            BleMSTUtils.getInstance().connect(getPageContext().getApplicationContext(), mac);
        }

        BleMSTUtils.getInstance().setOnDataCallBack(new BleMSTUtils.onDataCallBack() {

            @Override
            public void onDisConnect() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2_000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        BleMSTUtils.getInstance().connect(getPageContext().getApplicationContext(), mac);
                    }
                });
            }

            @Override
            public void onStateData(List<String> list) {
                //读取系统运行状态数据
                Log.i("xjh", "onStateData: ");
                if (list.size() == 80) {
                    //获取需要的数据    +6
                    MSTBlueEventBus mstBlueEventBus = new MSTBlueEventBus();
                    mstBlueEventBus.setType(1);
                    mstBlueEventBus.setStringList(list);
                    EventBusUtils.post(mstBlueEventBus);
                }
            }

            @Override
            public void onBigRecord(List<MSTRecordDataInfo> bigRecordInfo) {
                //大剂量
                for (int i = 0; i < bigRecordInfo.size(); i++) {
                    Log.i("xjh", "bigRecordInfo==" + bigRecordInfo.get(i).toString());
                }
                MSTBlueEventBus mstBlueEventBus = new MSTBlueEventBus();
                mstBlueEventBus.setType(2);
                mstBlueEventBus.setRecordDataInfoList(bigRecordInfo);
                EventBusUtils.post(mstBlueEventBus);
            }

            @Override
            public void onSunRecord(List<MSTRecordDataInfo> sunRecordInfos) {
                //日总量
                for (int i = 0; i < sunRecordInfos.size(); i++) {
                    Log.i("xjh", "bigRecordInfo==" + sunRecordInfos.get(i).toString());
                }
                MSTBlueEventBus mstBlueEventBus = new MSTBlueEventBus();
                mstBlueEventBus.setType(3);
                mstBlueEventBus.setRecordDataInfoList(sunRecordInfos);
                EventBusUtils.post(mstBlueEventBus);
            }

            @Override
            public void onErrorRecord(List<MSTRecordDataInfo> errorRecordInfos) {
                //警示数据
                for (int i = 0; i < errorRecordInfos.size(); i++) {
                    Log.i("xjh", "bigRecordInfo==" + errorRecordInfos.get(i).toString());
                }
                MSTBlueEventBus mstBlueEventBus = new MSTBlueEventBus();
                mstBlueEventBus.setType(4);
                mstBlueEventBus.setRecordDataInfoList(errorRecordInfos);
                EventBusUtils.post(mstBlueEventBus);
            }

            @Override
            public void onBaseRecord(List<MSTRecordDataInfo> baseRecordInfo) {
                //基础率数据
                for (int i = 0; i < baseRecordInfo.size(); i++) {
                    Log.i("xjh", "bigRecordInfo==" + baseRecordInfo.get(i).toString());
                }
                MSTBlueEventBus mstBlueEventBus = new MSTBlueEventBus();
                mstBlueEventBus.setType(5);
                mstBlueEventBus.setRecordDataInfoList(baseRecordInfo);
                EventBusUtils.post(mstBlueEventBus);
            }
        });
    }


    private void regToWx() {
        //通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        //将应用的appId注册到微信(将该app注册到微信)
        api.registerApp(APP_ID);
        //建议动态监听微信启动广播进行注册到微信
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //将该app注册到微信
                api.registerApp(Constants.APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FloatingView.get().attach(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNotifySettings();
        getImMessageType();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FloatingView.get().detach(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        toSubmitScan();
    }

    /**
     * 注销已注册的未读消息数变化监听器。
     * 接收未读消息消息的监听器。
     */
    @Override
    protected void onDestroy() {
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(this);
        super.onDestroy();
    }


    /**
     * 展示更新弹窗
     *
     * @param data
     */
    private void showUpdatePopup(UpdateBean data) {
        if ("1".equals(data.getIs_force())) {
            closeLinearLayout.setVisibility(View.GONE);
        } else {
            closeLinearLayout.setVisibility(View.VISIBLE);
        }
        //更新判断
        if (isHaveUpdate) {
            toShowUpdateDialog(data);
            updatePopup.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    showGuidePopup();
                }
            });
        } else {
            showGuidePopup();
            getGoods();
        }
    }

    /**
     * 展示引导弹窗
     */
    private void showGuidePopup() {
        //引导判断
        int count = SPStaticUtils.getInt("isFirstInstall", 0);
        if (0 == count) {
            homeGuideSportPopup.showPopupWindow();
            homeGuideDietPopup.showPopupWindow();
            homeGuideEducationPopup.showPopupWindow();
            SPStaticUtils.put("isFirstInstall", 1);
        }
    }


    private void toShowUpdateDialog(UpdateBean data) {
        updateUrl = data.getUpdateurl();
        String versionName = data.getVersionname();
        String apkSize = data.getAppsize();
        String updateContent = data.getUpcontent();
        tvUpdateName.setText(versionName);
        tvUpdateSize.setText(apkSize);
        tvUpdateContent.setText(updateContent);
        tvUpdateContent.setVisibility(updateContent == null ? View.GONE : View.VISIBLE);
        updatePopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        updatePopup.setBackPressEnable(false);
        updatePopup.setAllowDismissWhenTouchOutside(false);
        updatePopup.showPopupWindow();
    }

    private void toSubmitScan() {
        String scanImei = SPStaticUtils.getString("scanImei");
        if (!TextUtils.isEmpty(scanImei)) {
            Intent intent = new Intent(getPageContext(), InputImeiActivity.class);
            intent.putExtra("imei", scanImei);
            startActivity(intent);
        }
        SPStaticUtils.put("scanImei", "");
    }


    /**
     * 设置用户信息代理
     */
    private void setRongImUserInfo() {
        List<RongUserBean> rongList = new ArrayList<>();
        //设置用户
        LoginBean user = (LoginBean) SharedPreferencesUtils.getBean(Utils.getApp(), SharedPreferencesUtils.USER_INFO);
        String userid = user.getUserid();
        String username = user.getNickname();
        String picture = user.getPicture();
        RongUserBean userBean = new RongUserBean(userid, username, picture);
        rongList.add(userBean);
        //设置医生
        String mainDocBind = SPStaticUtils.getString("mainDocBind");
        if (!TextUtils.isEmpty(mainDocBind)) {
            String mainDocId = SPStaticUtils.getString("mainDocId");
            String mainDocName = SPStaticUtils.getString("mainDocName");
            String mainDocImgUrl = SPStaticUtils.getString("mainDocImgUrl");
            RongUserBean doctorBean = new RongUserBean(mainDocId, mainDocName, mainDocImgUrl);
            rongList.add(doctorBean);
        }

        boolean isAdd = true;
        for (int i = 0; i < rongList.size(); i++) {
            if (TextUtils.equals(rongList.get(i).getId(), "644395")) {
                isAdd = false;
            }
        }
        if (isAdd) {
            RongUserBean userBean1 = new RongUserBean("644395", "客服小慧", "http://doctor.xiyuns.cn//public//images//kefu.jpg");
            rongList.add(userBean1);
        }
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String imUserId) {
                for (RongUserBean userBean : rongList) {
                    String serverId = userBean.getId();
                    if (serverId.equals(imUserId)) {
                        UserInfo userInfo = new UserInfo(serverId, userBean.getName(), Uri.parse(userBean.getHeadImgUrl()));
                        //调用刷新方法, 刷新用户信息缓存
                        RongIM.getInstance().refreshUserInfoCache(userInfo);
                        return userInfo;
                    }
                }
                return null;
            }
        }, false);
    }

    /**
     * 获取融云消息未读数量
     */
    private void getImUnReadMsgCount() {
        final Conversation.ConversationType[] conversationTypes = {Conversation.ConversationType.PRIVATE};
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
    }

    /**
     * 设置别名
     */
    private void setUPushAlias() {
        LoginBean bean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String userid = bean.getUserid();
        String aliasBefore = BuildConfig.ENVIRONMENT ? "p_" : "t_";
        //JPushUtils.setAlias(aliasBefore + userid);
        String bindAccount = aliasBefore + userid;
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.bindAccount(bindAccount, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onFailed(String s, String s1) {
            }
        });
    }


    /**
     * 弹窗初始化
     */
    private void initPopup() {
        //引导弹窗教育
        homeGuideEducationPopup = new HomeGuidePopup(getPageContext());
        //引导教育弹窗
        homeGuideDietPopup = new HomeGuidePopup(getPageContext());
        ImageView imgHomeGuideDiet = homeGuideDietPopup.findViewById(R.id.img_home_guide);
        imgHomeGuideDiet.setImageResource(R.drawable.home_guide_diet);
        //引导运动弹窗
        homeGuideSportPopup = new HomeGuidePopup(getPageContext());
        ImageView imgHomeGuideSport = homeGuideSportPopup.findViewById(R.id.img_home_guide);
        imgHomeGuideSport.setImageResource(R.drawable.home_guide_sport);
        //更新弹窗
        updatePopup = new UpdatePopup(getPageContext());
        tvUpdateName = updatePopup.findViewById(R.id.tv_update_name);
        tvUpdateSize = updatePopup.findViewById(R.id.tv_update_size);
        tvUpdateContent = updatePopup.findViewById(R.id.tv_update_content);
        pbUpdateProgress = updatePopup.findViewById(R.id.pb_update_progress);
        tvUpdateUpdate = updatePopup.findViewById(R.id.tv_update_update);
        ivUpdateClose = updatePopup.findViewById(R.id.iv_update_close);
        closeLinearLayout = updatePopup.findViewById(R.id.ll_update_cancel);
        tvUpdateUpdate.setOnClickListener(this);
        ivUpdateClose.setOnClickListener(this);

        advertisementPop = new AdvertisementPop(this);
        backgroudImageView = advertisementPop.findViewById(R.id.iv_main_adver);
        closeImageView = advertisementPop.findViewById(R.id.iv_adver_close);
        backgroudImageView.setOnClickListener(this);
        closeImageView.setOnClickListener(this);
        int width = ScreenUtils.screenWidth(getPageContext()) - ScreenUtils.dip2px(getPageContext(), 80);
        int hight = width / 4 * 5;
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(width, hight);
        ll.gravity = Gravity.CENTER;
        backgroudImageView.setLayoutParams(ll);

        goodsDetailsPop = new GoodsDetailsPop(this);
        backgroudGoodsImageView = goodsDetailsPop.findViewById(R.id.iv_main_goods);
        closeGoodsImageView = goodsDetailsPop.findViewById(R.id.iv_goods_close);
        backgroudGoodsImageView.setLayoutParams(ll);
        backgroudGoodsImageView.setOnClickListener(this);
        closeGoodsImageView.setOnClickListener(this);

        noNeedPop = new NoNeedPop(this);
        backgroudNoNeedImageView = noNeedPop.findViewById(R.id.iv_main_no_need);
        noNeedcloseImageView = noNeedPop.findViewById(R.id.iv_main_no_need_close);
        backgroudNoNeedImageView.setLayoutParams(ll);
        backgroudNoNeedImageView.setOnClickListener(this);
        noNeedcloseImageView.setOnClickListener(this);

        couponPop = new CouponPop(this);
        couponImageView = couponPop.findViewById(R.id.iv_main_coupon);
        couponcloseImageView = couponPop.findViewById(R.id.iv_main_coupon_close);
        couponImageView.setLayoutParams(ll);
        couponImageView.setOnClickListener(this);
        couponcloseImageView.setOnClickListener(this);
    }


    /**
     * 检查通知权限
     */
    private void checkNotifySettings() {
        boolean notificationEnabled = NotificationUtils.isNotificationEnabled(getPageContext());
        if (notificationEnabled) {

        } else {
            DialogUtils.getInstance().showDialog1(getPageContext(), "", "请点击确定跳转至设置页面,并允许通知.", true, new DialogUtils.DialogCallBack() {
                @Override
                public void execEvent() {
                    NotificationUtils.goToSettings(getPageContext());
                }
            });
        }
    }

    /**
     * 获取更新
     */
    private void getUpdate() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.USER_INFO);
        HashMap map = new HashMap<>();
        map.put("version_code", AppUtils.getAppVersionCode());
        map.put("access_token", loginBean.getToken());
        map.put("version", ConstantParam.SERVER_VERSION);
        XyUrl.okPost(XyUrl.GET_UPDATE, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                UpdateBean data = JSONObject.parseObject(value, UpdateBean.class);
                Message msg = Message.obtain();
                msg.obj = data;
                msg.what = GET_UPDATE_DATA;
                sendHandlerMessage(msg);
            }

            @Override
            public void onError(int error, String errorMsg) {
                getGoods();
            }
        });
    }

    /**
     * 获取广告
     * type:1：外部链接
     * 2：商品链接
     * 3：不跳转
     * 3-1-2    显示 2—1—3
     */
    private void getAdver() {
        LoginBean userLogin = (LoginBean) SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.USER_INFO);
        Call<String> requestCall = DataManager.getAdver(userLogin.getToken(), "3", (call, response) -> {
            if (response.code == 200) {
                noadverInfo = (AdverInfo) response.object;
                Glide.with(getPageContext()).asBitmap().load(noadverInfo.getImg_url()).centerInside().into(backgroudNoNeedImageView);
                noNeedPop.showPopupWindow();
            } else {
                getCoupon();
            }
        }, (call, t) -> {
            getCoupon();
        });
    }

    /**
     * 获取外部链接
     */
    private void getOut() {
        LoginBean userLogin = (LoginBean) SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.USER_INFO);
        Call<String> requestCall = DataManager.getAdver(userLogin.getToken(), "1", (call, response) -> {
            if (response.code == 200) {
                outadverInfo = (AdverInfo) response.object;
                Glide.with(getPageContext()).asBitmap().load(outadverInfo.getImg_url()).centerInside().into(backgroudImageView);
                advertisementPop.showPopupWindow();
            } else {
                getAdver();
            }
        }, (call, t) -> {
            getAdver();
        });
    }

    /**
     * 获取优惠券
     */
    private void getCoupon() {
        LoginBean userLogin = (LoginBean) SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.USER_INFO);
        Call<String> requestCall = DataManager.getAdver(userLogin.getToken(), "4", (call, response) -> {
            if (response.code == 200) {
                couponInfo = (AdverInfo) response.object;
                Glide.with(getPageContext()).asBitmap().load(couponInfo.getImg_url()).centerInside().into(couponImageView);

                couponPop.showPopupWindow();

            }
        }, (call, t) -> {
        });
    }

    /**
     * 获取商品的弹窗
     */
    private void getGoods() {
        LoginBean userLogin = (LoginBean) SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.USER_INFO);
        Call<String> requestCall = DataManager.getAdver(userLogin.getToken(), "2", (call, response) -> {
            if (response.code == 200) {
                goodsAdverInfo = (AdverInfo) response.object;

                Glide.with(getPageContext()).asBitmap().load(goodsAdverInfo.getImg_url()).centerInside().into(backgroudGoodsImageView);
                goodsDetailsPop.showPopupWindow();

            } else {
                getOut();
            }
        }, (call, t) -> {
            getOut();
        });
    }

    private void receiveCoupon(String coupon_id) {
        LoginBean user = (LoginBean) SharedPreferencesUtils.getBean(Utils.getApp(), SharedPreferencesUtils.USER_INFO);

        Call<String> requestCall = DataManager.receiveCoupin(user.getToken(), coupon_id, (call, response) -> {
            Log.i("yys", "toast==" + response.msg);
            //            Toast.makeText(MainActivity.this, response.msg, ).show();
            //            EToast2.makeText(MainActivity.this, response.msg, Toast.LENGTH_SHORT).show();
            ToastUtils.showShort(response.msg);
            if (response.code == 200) {
                Intent intent = new Intent(getPageContext(), MyCouponListActivity.class);
                intent.putExtra("activity_id", "-1");
                startActivity(intent);
                couponPop.dismiss();
            }
        }, (call, t) -> {

        });

    }

    /**
     * 获取融云消息类型
     */
    private void getImMessageType() {
        LoginBean userLogin = (LoginBean) SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.USER_INFO);
        Call<String> requestCall = DataManager.getImMessageType(userLogin.getToken(), (call, response) -> {
            if (response.code == 200) {
                AdverInfo adverInfo = (AdverInfo) response.object;
                Log.i("yys", "adverInfo.getDocMsg()=" + adverInfo.getDocMsg() + "adverInfo.getKfMsg()=" + adverInfo.getKfMsg());
                if ("1".equals(adverInfo.getDocMsg())) {
                    tvRedPoint.setVisibility(View.VISIBLE);
                } else {
                    tvRedPoint.setVisibility(View.GONE);
                }
                if ("1".equals(adverInfo.getKfMsg())) {
                    tvRedImPoint.setVisibility(View.VISIBLE);
                } else {
                    tvRedImPoint.setVisibility(View.GONE);
                }
            }
        }, (call, t) -> {

        });
    }

    /**
     * 获取融云Token
     */
    private void getImToken() {
        HashMap map = new HashMap<>();
        XyUrl.okPost(XyUrl.GET_IM_TOKEN, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                RongYunBean rongYunBean = JSONObject.parseObject(value, RongYunBean.class);
                Message message = Message.obtain();
                message.what = GET_IM_TOKEN;
                message.obj = rongYunBean;
                sendHandlerMessage(message);
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });
    }

    /**
     * 获取商城标题
     */
    private void getShopTitle() {
        LoginBean user = (LoginBean) SharedPreferencesUtils.getBean(Utils.getApp(), SharedPreferencesUtils.USER_INFO);
        HashMap map = new HashMap<>();
        map.put("access_token", user.getToken());
        XyUrl.okPost(XyUrl.GET_SHOP_TITLE, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                ShopTitleBean data = JSONObject.parseObject(value, ShopTitleBean.class);
                Message message = Message.obtain();
                message.what = GET_SHOP_TITLE;
                message.obj = data;
                sendHandlerMessage(message);
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });

    }


    @Override
    public void onClick(View view) {
        Intent intent;
        LoginBean user = (LoginBean) SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.USER_INFO);
        StringBuilder stringBuilderGoods = new StringBuilder();
        stringBuilderGoods.append(user.getNickname()).append("+").append(user.getUsername());
        switch (view.getId()) {
            //更新升级按钮
            case R.id.tv_update_update:
                //判断字体获取状态
                String text = (String) tvUpdateUpdate.getText();
                switch (text) {
                    case "升级":
                    case "下载失败":
                        //下载并安装
                        toDownLoadAndInstallApk();
                        break;
                    case "下载中":
                        //没有动作
                        break;
                    case "立即安装":
                        //安装apk
                        AppUtils.installApp(updateApk);
                        break;
                }
                break;
            //关闭按钮
            case R.id.iv_update_close:
                updatePopup.dismiss();
                getGoods();
                break;
            //外部链接 点击图片
            case R.id.iv_main_adver:
                Map<String, Object> alerExternal = new HashMap<String, Object>();
                alerExternal.put("nametel", stringBuilderGoods.toString());
                //上下文   事件ID   map
                MobclickAgent.onEventObject(this, "alert_external", alerExternal);
                advertisementPop.dismiss();

                intent = new Intent(getPageContext(), BaseWebViewActivity.class);
                intent.putExtra("title", outadverInfo.getTitle());
                intent.putExtra("url", outadverInfo.getUrl());
                startActivity(intent);
                break;
            //外部链接 点击关闭
            case R.id.iv_adver_close:
                Map<String, Object> alertExternalX = new HashMap<String, Object>();
                alertExternalX.put("nametel", stringBuilderGoods.toString());
                //上下文   事件ID   map
                MobclickAgent.onEventObject(this, "alert_external_x", alertExternalX);
                advertisementPop.dismiss();
                getAdver();
                break;
            //商品 点击图片
            case R.id.iv_main_goods:
                Map<String, Object> alertPoduct = new HashMap<String, Object>();
                alertPoduct.put("nametel", stringBuilderGoods.toString());
                //上下文   事件ID   map
                MobclickAgent.onEventObject(this, "alert_product", alertPoduct);
                goodsDetailsPop.dismiss();
                intent = new Intent(getPageContext(), ProductDetailActivity.class);

                intent.putExtra("goods_id", goodsAdverInfo.getGoods_id());
                startActivity(intent);
                break;
            //商品 点击关闭
            case R.id.iv_goods_close:
                Map<String, Object> alertProductX = new HashMap<String, Object>();
                alertProductX.put("nametel", stringBuilderGoods.toString());
                //上下文   事件ID   map
                MobclickAgent.onEventObject(this, "alert_product_x", alertProductX);
                goodsDetailsPop.dismiss();
                getOut();

                break;
            //无链接 取消
            case R.id.iv_main_no_need_close:
                Map<String, Object> alertNoX = new HashMap<String, Object>();
                alertNoX.put("nametel", stringBuilderGoods.toString());
                //上下文   事件ID   map
                MobclickAgent.onEventObject(this, "alert_no_x", alertNoX);
                noNeedPop.dismiss();
                break;
            //无链接 确定
            case R.id.iv_main_no_need:
                Map<String, Object> alerNo = new HashMap<String, Object>();
                alerNo.put("nametel", stringBuilderGoods.toString());
                //上下文   事件ID   map
                MobclickAgent.onEventObject(this, "alert_no", alerNo);
                noNeedPop.dismiss();
                getCoupon();
                break;
            case R.id.iv_main_coupon:
                receiveCoupon(couponInfo.getCoupon_id());
                break;
            case R.id.iv_main_coupon_close:
                couponPop.dismiss();
                break;
            default:
                break;
        }
    }

    private void toDownLoadAndInstallApk() {
        UpdateUtils.downloadAndInstall(updateUrl, this);
    }

    /**
     * 新增HomeFragment
     */
    private void addFirstFragment() {
        hideTitleBar();
        tvMainHome.setTextColor(getResources().getColor(R.color.main_home));
        tvMainRegister.setTextColor(getResources().getColor(R.color.black_text));
        tvMainMall.setTextColor(getResources().getColor(R.color.black_text));
        tvMainMe.setTextColor(getResources().getColor(R.color.black_text));
        tvMainOutside.setTextColor(getResources().getColor(R.color.black_text));

        rlMainHome.setEnabled(false);
        rlMainRegister.setEnabled(true);
        rlMainMall.setEnabled(true);
        rlMainMe.setEnabled(true);
        rlMainOutside.setEnabled(true);

        imgMainHome.setImageResource(R.drawable.home_selected);
        if (TextUtils.isEmpty(registrationUrl)) {
            imgMainRegister.setImageResource(R.drawable.registration_default);
        } else {
            imgMainRegister.setImageResource(R.drawable.main_bottom_position_02_check);
        }
        if (TextUtils.isEmpty(mallUrl)) {
            imgMainMall.setImageResource(R.drawable.home_mall_default);
        } else {
            imgMainMall.setImageResource(R.drawable.main_bottom_position_04_check);
        }
        imgMainMe.setImageResource(R.drawable.home_me_default);
        HomeFragment homeFragment = new HomeFragment();
        FragmentUtils.replace(getSupportFragmentManager(), homeFragment, R.id.fl_main, false);
    }

    /**
     * 判断预约模块
     */
    private void setShowRegistration() {
        showTitleBar();
        hideBack();
        tvMainHome.setTextColor(getResources().getColor(R.color.black_text));
        tvMainRegister.setTextColor(getResources().getColor(R.color.main_home));
        tvMainMall.setTextColor(getResources().getColor(R.color.black_text));
        tvMainMe.setTextColor(getResources().getColor(R.color.black_text));
        tvMainOutside.setTextColor(getResources().getColor(R.color.black_text));
        rlMainHome.setEnabled(true);
        rlMainRegister.setEnabled(false);
        rlMainMall.setEnabled(true);
        rlMainMe.setEnabled(true);
        rlMainOutside.setEnabled(true);
        imgMainHome.setImageResource(R.drawable.home_default);
        if (TextUtils.isEmpty(registrationUrl)) {
            imgMainRegister.setImageResource(R.drawable.registration_default);
        } else {
            imgMainRegister.setImageResource(R.drawable.main_bottom_position_02_check);
        }
        if (TextUtils.isEmpty(mallUrl)) {
            imgMainMall.setImageResource(R.drawable.home_mall_default);
        } else {
            imgMainMall.setImageResource(R.drawable.main_bottom_position_04_check);
        }
        imgMainMe.setImageResource(R.drawable.home_me_default);

        if (!TextUtils.isEmpty(registrationUrl)) {
            setTitle(registrationStr);
            imgMainRegister.setImageResource(R.drawable.main_bottom_position_02_checked);
            MallWebFragment mallWebFragment = new MallWebFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", registrationUrl);
            mallWebFragment.setArguments(bundle);
            FragmentUtils.replace(getSupportFragmentManager(), mallWebFragment, R.id.fl_main, false);
        } else {
            setTitle("挂号");
            imgMainRegister.setImageResource(R.drawable.registration_selected);
            RegistrationFragment registrationFragment = new RegistrationFragment();
            FragmentUtils.replace(getSupportFragmentManager(), registrationFragment, R.id.fl_main, false);
        }
    }


    private void setOutSide() {
        hideTitleBar();
        hideBack();
        tvMainHome.setTextColor(getResources().getColor(R.color.black_text));
        tvMainRegister.setTextColor(getResources().getColor(R.color.black_text));
        tvMainMall.setTextColor(getResources().getColor(R.color.black_text));
        tvMainMe.setTextColor(getResources().getColor(R.color.black_text));
        tvMainOutside.setTextColor(getResources().getColor(R.color.main_home));
        rlMainHome.setEnabled(true);
        rlMainRegister.setEnabled(true);
        rlMainMall.setEnabled(true);
        rlMainMe.setEnabled(true);
        rlMainOutside.setEnabled(false);
        imgMainHome.setImageResource(R.drawable.home_default);
        if (TextUtils.isEmpty(registrationUrl)) {
            imgMainRegister.setImageResource(R.drawable.registration_default);
        } else {
            imgMainRegister.setImageResource(R.drawable.main_bottom_position_02_check);
        }
        if (TextUtils.isEmpty(mallUrl)) {
            imgMainMall.setImageResource(R.drawable.home_mall_default);
        } else {
            imgMainMall.setImageResource(R.drawable.main_bottom_position_04_check);
        }
        imgMainMe.setImageResource(R.drawable.home_me_default);
        if (NetworkUtils.isConnected()) {
            getDoctorInfo();
        } else {
            outOfHospitalFragment = new OutOfHospitalFragment();
            FragmentUtils.replace(getSupportFragmentManager(), outOfHospitalFragment, R.id.fl_main, false);
        }
        setTitle("院外管理");
    }

    /**
     * 判断商城模块
     */
    private void setShowMall() {
        hideTitleBar();
        hideBack();
        tvMainHome.setTextColor(getResources().getColor(R.color.black_text));
        tvMainRegister.setTextColor(getResources().getColor(R.color.black_text));
        tvMainMall.setTextColor(getResources().getColor(R.color.main_home));
        tvMainMe.setTextColor(getResources().getColor(R.color.black_text));
        tvMainOutside.setTextColor(getResources().getColor(R.color.black_text));
        rlMainHome.setEnabled(true);
        rlMainRegister.setEnabled(true);
        rlMainMall.setEnabled(false);
        rlMainMe.setEnabled(true);
        rlMainOutside.setEnabled(true);
        imgMainHome.setImageResource(R.drawable.home_default);
        if (TextUtils.isEmpty(registrationUrl)) {
            imgMainRegister.setImageResource(R.drawable.registration_default);
        } else {
            imgMainRegister.setImageResource(R.drawable.main_bottom_position_02_check);
        }
        if (TextUtils.isEmpty(mallUrl)) {
            imgMainMall.setImageResource(R.drawable.home_mall_default);
        } else {
            imgMainMall.setImageResource(R.drawable.main_bottom_position_04_check);
        }
        imgMainMe.setImageResource(R.drawable.home_me_default);
        if (!TextUtils.isEmpty(mallUrl)) {
            imgMainMall.setImageResource(R.drawable.main_bottom_position_04_checked);
            MallWebFragment mallWebFragment = new MallWebFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", mallUrl);
            mallWebFragment.setArguments(bundle);
            FragmentUtils.replace(getSupportFragmentManager(), mallWebFragment, R.id.fl_main, false);
        } else {
            imgMainMall.setImageResource(R.drawable.home_mall_selected);
            //MallNativeFragment mallNativeFragment = new MallNativeFragment();
            MallHomeFragment mallNativeFragment = new MallHomeFragment();
            FragmentUtils.replace(getSupportFragmentManager(), mallNativeFragment, R.id.fl_main, false);
        }
    }

    private void setShowMe() {
        hideTitleBar();
        tvMainHome.setTextColor(getResources().getColor(R.color.black_text));
        tvMainRegister.setTextColor(getResources().getColor(R.color.black_text));
        tvMainMall.setTextColor(getResources().getColor(R.color.black_text));
        tvMainMe.setTextColor(getResources().getColor(R.color.main_home));
        tvMainOutside.setTextColor(getResources().getColor(R.color.black_text));
        rlMainHome.setEnabled(true);
        rlMainRegister.setEnabled(true);
        rlMainMall.setEnabled(true);
        rlMainMe.setEnabled(false);
        rlMainOutside.setEnabled(true);
        imgMainHome.setImageResource(R.drawable.home_default);
        if (TextUtils.isEmpty(registrationUrl)) {
            imgMainRegister.setImageResource(R.drawable.registration_default);
        } else {
            imgMainRegister.setImageResource(R.drawable.main_bottom_position_02_check);
        }
        if (TextUtils.isEmpty(mallUrl)) {
            imgMainMall.setImageResource(R.drawable.home_mall_default);
        } else {
            imgMainMall.setImageResource(R.drawable.main_bottom_position_04_check);
        }
        imgMainMe.setImageResource(R.drawable.home_me_selected);
        UserFragment userFragment = new UserFragment();
        FragmentUtils.replace(getSupportFragmentManager(), userFragment, R.id.fl_main, false);
    }


    /**
     * 获取医生信息
     */
    private void getDoctorInfo() {
        HashMap map = new HashMap<>();
        XyUrl.okPost(XyUrl.MY_DOCTOR, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                MyDoctorBean doctor = JSONObject.parseObject(value, MyDoctorBean.class);
                Message msg = Message.obtain();
                msg.obj = doctor;
                msg.what = BIND;
                sendHandlerMessage(msg);
            }

            @Override
            public void onError(int error, String errorMsg) {
                Message msg = Message.obtain();
                msg.what = NO_BIND;
                sendHandlerMessage(msg);
            }
        });
    }

    /**
     * 连接融云服务器
     *
     * @param rongYun
     */
    private void connectRongServer(RongYunBean rongYun) {
        RongIM.connect(rongYun.getToken(), new RongIMClient.ConnectCallback() {
            @Override
            public void onSuccess(String userId) {
            }

            @Override
            public void onError(RongIMClient.ConnectionErrorCode connectionErrorCode) {

            }

            @Override
            public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus databaseOpenStatus) {

            }
        });
    }


    @Override
    public void start() {
    }

    @Override
    public void downloading(int max, int progress) {
        //百分比
        int curr = (int) (progress / (double) max * 100.0);
        //下载中
        tvUpdateUpdate.setText("下载中");
        pbUpdateProgress.setVisibility(View.VISIBLE);
        pbUpdateProgress.setProgress(curr);
    }

    @Override
    public void done(File apk) {
        updateApk = apk;
        //立即安装
        tvUpdateUpdate.setText("立即安装");
        pbUpdateProgress.setVisibility(View.GONE);
    }

    @Override
    public void cancel() {
    }

    @Override
    public void error(Exception e) {
        e.printStackTrace();
        tvUpdateUpdate.setText("下载失败");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        ClickUtils.back2HomeFriendly("再按一次退出程序");
    }

    /**
     * 未读消息数量监听
     *
     * @param count
     */
    @Override
    public void onCountChanged(int count) {
        //        if (count > 0) {
        //            tvRedPoint.setVisibility(View.VISIBLE);
        //        } else {
        //            tvRedPoint.setVisibility(View.GONE);
        //        }
    }


    @OnClick({R.id.rl_main_home, R.id.rl_main_register, R.id.rl_main_mall, R.id.rl_main_me, R.id.rl_main_outside})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_main_home://首页
                addFirstFragment();
                break;
            case R.id.rl_main_register://圈子
                setShowRegistration();
                break;
            case R.id.rl_main_mall://商城
                setShowMall();
                break;
            case R.id.rl_main_me://我的
                setShowMe();
                break;
            case R.id.rl_main_outside:
                setOutSide();
                break;
        }
    }


    @Override
    public void processHandlerMsg(Message msg) {
        switch (msg.what) {
            case GET_IM_TOKEN:
                RongYunBean rongYunBean = (RongYunBean) msg.obj;
                connectRongServer(rongYunBean);
                break;
            //绑定医生
            case BIND:
                MyDoctorBean doctorBean = (MyDoctorBean) msg.obj;
                int docid = doctorBean.getDocid();
                String docname = doctorBean.getDocname();
                String imgurl = doctorBean.getImgurl();
                SPStaticUtils.put("mainDocBind", "1");
                SPStaticUtils.put("mainDocId", docid + "");
                SPStaticUtils.put("mainDocName", docname);
                SPStaticUtils.put("mainDocImgUrl", imgurl);
                EventBusUtils.post(new EventMessage(ConstantParam.RONG_HEAD_REFRESH));
                OutOfHospitalBindFragment outOfHospitalBindFragment = new OutOfHospitalBindFragment();
                FragmentUtils.replace(getSupportFragmentManager(), outOfHospitalBindFragment, R.id.fl_main, false);
                break;
            //未绑定医生
            case NO_BIND:
                outOfHospitalFragment = new OutOfHospitalFragment();
                FragmentUtils.replace(getSupportFragmentManager(), outOfHospitalFragment, R.id.fl_main, false);
                break;
            case GET_UPDATE_DATA:
                int localVersionCode = AppUtils.getAppVersionCode();
                UpdateBean data = (UpdateBean) msg.obj;
                int netVersionCode = data.getVersion();
                if (localVersionCode < netVersionCode) {
                    isHaveUpdate = true;
                } else {
                    isHaveUpdate = false;
                }
                //展示弹窗
                showUpdatePopup(data);
                break;
            case GET_SHOP_TITLE:
                ShopTitleBean shopTitleBean = (ShopTitleBean) msg.obj;
                registrationStr = shopTitleBean.getScheduling();
                tvMainRegister.setText(registrationStr);
                tvMainMall.setText(shopTitleBean.getTitle());
                //registrationUrl = "https://www.zhihu.com/";
                registrationUrl = shopTitleBean.getSchurl();
                //mallUrl = "https://www.baidu.com/";
                mallUrl = shopTitleBean.getUrl();
                if (TextUtils.isEmpty(registrationUrl)) {
                    imgMainRegister.setImageResource(R.drawable.registration_default);
                } else {
                    imgMainRegister.setImageResource(R.drawable.main_bottom_position_02_check);
                }
                if (TextUtils.isEmpty(mallUrl)) {
                    imgMainMall.setImageResource(R.drawable.home_mall_default);
                } else {
                    imgMainMall.setImageResource(R.drawable.main_bottom_position_04_check);
                }
                break;
        }
    }

    @Override
    protected void receiveEvent(EventMessage event) {
        super.receiveEvent(event);
        switch (event.getCode()) {
            case ConstantParam.RONG_HEAD_REFRESH:
                setRongImUserInfo();
                break;
            case ConstantParam.RONGIM_RED_POINT_REFRESH:
                tvRedPoint.setVisibility(View.GONE);
                break;
            case ConstantParam.HOME_TO_OUTSIDE:
                setOutSide();
                break;
            case BaseConstantParam.EventCode.MALL_FINISH_RO_MAIN_ACTIVITY:
                //收到通知
                Log.e(TAG, "支付成功以后回到首页");
                startActivity(new Intent(getPageContext(), MainActivity.class));
                break;
        }
    }
}
