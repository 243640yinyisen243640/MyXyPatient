package com.vice.bloodpressure.ui.activity.insulin;

import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.allen.library.utils.ToastUtils;
import com.google.gson.Gson;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.insulin.InsulinBaseModeAdapter;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.BaseRateBean;
import com.vice.bloodpressure.bean.insulin.InsulinDeviceAllInfo;
import com.vice.bloodpressure.bean.insulin.InsulinDeviceInfo;
import com.vice.bloodpressure.utils.BleUtils;
import com.vice.bloodpressure.utils.MySPUtils;
import com.vice.bloodpressure.utils.StatusBarUtils;
import com.vice.bloodpressure.view.LoadingImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:基础模式
 */
public class InsulinBaseModeListActivity extends XYSoftUIBaseActivity implements View.OnClickListener {

    private static final int BLUETOOTH_PERMISSIONS_REQUEST_CODE = 20;
    /**
     * 头部
     */
    private ImageView IvBack;
    private TextView tvModeFirst;
    private TextView tvModeSecond;


    /**
     * 布局
     */

    private TextView tvDayAll;
    private ImageView ivRefresh;
    private LoadingImageView ivLoadRefresh;
    private TextView bleTips;
    private ListView lvDataInfo;
    private LinearLayout llLast;
    private TextView tvNoData;

    private String type = "1";
    private String mac;
    private List<String> baseList1;
    private List<String> baseList2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().topView().removeAllViews();
        StatusBarUtils.statusBarColor(InsulinBaseModeListActivity.this, getResources().getColor(R.color.main_green));
        containerView().addView(initView());
        initListener();
        getData();

    }


    private void getBaseData1() {
        //回传基础率1
        BleUtils.getInstance().connect(false, getPageContext(), mac, new BleUtils.OnDataCallBackImpl() {

            @Override
            public void onDisConnect(boolean isSuccess) {
                super.onDisConnect(isSuccess);
                runOnUiThread(() -> {
                    try {
                        Thread.sleep(2_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!isSuccess) {
                        getBaseData1();
                    }
                });
            }

            @Override
            public void onConnect() {
                super.onConnect();
                runOnUiThread(() -> {
                    new Handler().postDelayed(() -> BleUtils.getInstance().sendData("0577A3332B"), 2_000);
                });
            }

            @Override
            public void onBaseRate(List<String> baseRateList) {
                super.onBaseRate(baseRateList);
                //基础率1
                runOnUiThread(() -> {
                    baseList1 = baseRateList;
                    BleUtils.getInstance().disConnect();
                    getBaseData2();
                });
            }
        });
    }

    private void getBaseData2() {
        try {
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //回传基础率2
        BleUtils.getInstance().connect(false, getPageContext(), mac, new BleUtils.OnDataCallBackImpl() {
            @Override
            public void onDisConnect(boolean isSuccess) {
                super.onDisConnect(isSuccess);
                runOnUiThread(() -> {
                    if (!isSuccess) {
                        getBaseData2();
                    }
                });
            }

            @Override
            public void onConnect() {
                super.onConnect();
                runOnUiThread(() -> {
                    new Handler().postDelayed(() -> BleUtils.getInstance().sendData("0577A443CC"), 2_000);
                });
            }

            @Override
            public void onBaseRate(List<String> baseRateList) {
                super.onBaseRate(baseRateList);
                //基础率2
                runOnUiThread(() -> {
                    baseList2 = baseRateList;
                    BleUtils.getInstance().disConnect();
                    setBaseData();
                });
            }
        });
    }

    private String list2String(List<String> strings) {
        List<BaseRateBean> list = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            String time;
            if (i < 10) {
                time = "0" + i + ":" + "00";
            } else {
                time = i + ":00";
            }
            double value = BleUtils.hexToInt(strings.get(i)) / 10.0;
            list.add(new BaseRateBean(time, value + ""));
        }
        return new Gson().toJson(list);
    }

    private void setBaseData() {
        time = -1;
        //还有停止动画代码都可以写在这

        String base_rate1 = list2String(baseList1);
        String base_rate2 = list2String(baseList2);
        bleTips.setVisibility(View.GONE);
        ivRefresh.setVisibility(View.VISIBLE);
        ivLoadRefresh.setVisibility(View.GONE);
        ivLoadRefresh.stopLoaddingAnim();
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        DataManager.adduserbase(loginBean.getToken(), base_rate1, base_rate2, (call, response) -> {
            if (response.code == 200) {
                getData();
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }

    private InsulinBaseModeAdapter adapter;

    private final List<InsulinDeviceInfo> list = new ArrayList<>();
    private List<InsulinDeviceInfo> list1;
    private List<InsulinDeviceInfo> list2;

    private void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        bleTips.setVisibility(View.GONE);
        ivRefresh.setVisibility(View.VISIBLE);
        ivLoadRefresh.setVisibility(View.GONE);
        ivLoadRefresh.stopLoaddingAnim();
        DataManager.getuserbase(loginBean.getToken(), (call, response) -> {
            if (response.code == 200) {
                InsulinDeviceAllInfo allInfo = (InsulinDeviceAllInfo) response.object;
                tvDayAll.setText("当前累计注射日基础总量：" + allInfo.getNow_value());
                list1 = allInfo.getType1();
                list2 = allInfo.getType2();
                list.clear();
                if (TextUtils.equals("1", type)) {
                    list.addAll(list1);
                } else {
                    list.addAll(list2);
                }
                adapter.notifyDataSetChanged();

            } else if (30002 == response.code) {
                lvDataInfo.setVisibility(View.GONE);
                llLast.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }
    private int time = 60;

    @Override
    protected void processHandlerMsg(Message msg) {
        super.processHandlerMsg(msg);
        time--;
        if (time > 0) {
            mHandler.sendEmptyMessageDelayed(1, 1000);
        } else if (time == 0) {
            time = 60;
            //倒计时结束
            ivRefresh.setVisibility(View.VISIBLE);
            ivLoadRefresh.setVisibility(View.GONE);
            ivLoadRefresh.stopLoaddingAnim();
            bleTips.setVisibility(View.VISIBLE);
            bleTips.setText("同步失败,请稍后重试");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_base_mode_back:
                finish();
                break;
            case R.id.tv_base_mode_first:
                type = "1";
                setBg(tvModeFirst, tvModeSecond);
                list.clear();
                list.addAll(list1);
                adapter.notifyDataSetChanged();
                if (list1 != null && list1.size() > 0) {
                    lvDataInfo.setVisibility(View.VISIBLE);
                    llLast.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                } else {
                    lvDataInfo.setVisibility(View.GONE);
                    llLast.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_base_mode_second:
                setBg(tvModeSecond, tvModeFirst);
                type = "2";
                list.clear();
                list.addAll(list2);
                adapter.notifyDataSetChanged();
                if (list2 != null && list2.size() > 0) {
                    lvDataInfo.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                } else {
                    lvDataInfo.setVisibility(View.GONE);
                    llLast.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_base_mode_refresh:

                if (Build.VERSION.SDK_INT > 30) {
                    Log.i("yys", "Build.VERSION.SDK_INT==" + Build.VERSION.SDK_INT);
                    if (ContextCompat.checkSelfPermission(getPageContext(),
                            "android.permission.BLUETOOTH_SCAN")
                            != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(getPageContext(),
                            "android.permission.BLUETOOTH_ADVERTISE")
                            != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(getPageContext(),
                            "android.permission.BLUETOOTH_CONNECT")
                            != PackageManager.PERMISSION_GRANTED) {
                        bleTips.setVisibility(View.VISIBLE);
                        bleTips.setText("请开启蓝牙和扫描设备权限");
                        ActivityCompat.requestPermissions(this, new String[]{
                                "android.permission.BLUETOOTH_SCAN",
                                "android.permission.BLUETOOTH_ADVERTISE",
                                "android.permission.BLUETOOTH_CONNECT"}, BLUETOOTH_PERMISSIONS_REQUEST_CODE);
                    } else {
                        if (!BleUtils.getInstance().initBlueBooth(this)){
                            bleTips.setVisibility(View.VISIBLE);
                            bleTips.setText("请开启蓝牙和扫描设备权限");
                            return;
                        }
                        mac = MySPUtils.getString(getPageContext(), MySPUtils.BLUE_MAC);
                        if (mac == null) {
                            return;
                        }
                        bleTips.setVisibility(View.VISIBLE);
                        bleTips.setText("同步数据,请稍后");
                        ivLoadRefresh.setBackgroundResource(R.drawable.loading_progress_bar);
                        ivRefresh.setVisibility(View.GONE);
                        ivLoadRefresh.setVisibility(View.VISIBLE);
                        ivLoadRefresh.startLoadingAnim();
                        time = 60;
                        mHandler.sendEmptyMessage(1);
                        getBaseData1();
                    }
                }else {
                    if (!BleUtils.getInstance().initBlueBooth(this)){
                        bleTips.setVisibility(View.VISIBLE);
                        bleTips.setText("请开启蓝牙和扫描设备权限");
                        return;
                    }
                    mac = MySPUtils.getString(getPageContext(), MySPUtils.BLUE_MAC);
                    if (mac == null) {
                        return;
                    }
                    bleTips.setVisibility(View.VISIBLE);
                    bleTips.setText("同步数据,请稍后");
                    ivLoadRefresh.setBackgroundResource(R.drawable.loading_progress_bar);
                    ivRefresh.setVisibility(View.GONE);
                    ivLoadRefresh.setVisibility(View.VISIBLE);
                    ivLoadRefresh.startLoadingAnim();
                    time = 60;
                    mHandler.sendEmptyMessage(1);
                    getBaseData1();
                }
                break;
            default:
                break;
        }
    }

    private void setBg(TextView tvCheck, TextView tvUncheck) {
        tvCheck.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.shape_white_30_2_1);
        tvCheck.setTextSize(18);
        tvCheck.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvUncheck.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tvUncheck.setTextSize(16);
        tvUncheck.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));


    }
    private void initListener() {
        IvBack.setOnClickListener(this);
        tvModeFirst.setOnClickListener(this);
        tvModeSecond.setOnClickListener(this);
        ivRefresh.setOnClickListener(this);
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_insulin_base_mode, null);
        IvBack = view.findViewById(R.id.iv_base_mode_back);
        tvModeFirst = view.findViewById(R.id.tv_base_mode_first);
        tvModeSecond = view.findViewById(R.id.tv_base_mode_second);

        tvDayAll = view.findViewById(R.id.tv_base_mode_day_all);
        ivRefresh = view.findViewById(R.id.iv_base_mode_refresh);
        ivLoadRefresh = view.findViewById(R.id.iv_base_mode_refresh_trends);
        bleTips = view.findViewById(R.id.tv_base_mode_ble_tips);
        lvDataInfo = view.findViewById(R.id.lv_base_mode_list);
        llLast = view.findViewById(R.id.ll_insulin_base_mode_last);
        tvNoData = view.findViewById(R.id.tv_insulin_base_mode_no_data);
        adapter = new InsulinBaseModeAdapter(getPageContext(), list);
        lvDataInfo.setAdapter(adapter);
        return view;
    }
}
