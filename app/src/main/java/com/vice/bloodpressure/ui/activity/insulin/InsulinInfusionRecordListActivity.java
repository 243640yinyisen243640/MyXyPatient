package com.vice.bloodpressure.ui.activity.insulin;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
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
import com.vice.bloodpressure.adapter.insulin.InsulinInfusionRecordAdapter;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.insulin.InsulinDeviceInfo;
import com.vice.bloodpressure.bean.insulin.RecordBigInfo;
import com.vice.bloodpressure.bean.insulin.RecordDataInfo;
import com.vice.bloodpressure.bean.insulin.RecordErrorInfo;
import com.vice.bloodpressure.bean.insulin.RecordInfo;
import com.vice.bloodpressure.ui.activity.injection.InjectionAddDeviceNoActivity;
import com.vice.bloodpressure.utils.BleUtils;
import com.vice.bloodpressure.utils.MySPUtils;
import com.vice.bloodpressure.view.LoadingImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:输注记录
 */
public class InsulinInfusionRecordListActivity extends XYSoftUIBaseActivity implements View.OnClickListener {

    private static final int BLUETOOTH_PERMISSIONS_REQUEST_CODE = 20;
    private TextView tvDeviceManage;
    private TextView tvBaseMode;
    private FrameLayout flPlan;
    private TextView tvPlanNum;
    private ImageView ivRefresh;
    private TextView bleTips;
    private LoadingImageView ivLoadRefresh;
    private TextView tvDayAll;
    private TextView tvInfoBig;
    private TextView tvBaseRate;
    private TextView tvWarning;
    private ListView lvDataInfo;
    private LinearLayout llLast;
    private TextView tvNoData;
    /**
     * 1日总量 2大剂量 3基础率 4报警记录
     */
    private String type = "1";

    private String mac;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("输注记录");
        containerView().addView(initView());
        initListner();
        mac = MySPUtils.getString(getPageContext(), MySPUtils.BLUE_MAC);
        getData();
    }

    private void initListner() {
        tvDeviceManage.setOnClickListener(this);
        flPlan.setOnClickListener(this);
        tvBaseMode.setOnClickListener(this);
        ivRefresh.setOnClickListener(this);
        tvDayAll.setOnClickListener(this);
        tvInfoBig.setOnClickListener(this);
        tvBaseRate.setOnClickListener(this);
        tvWarning.setOnClickListener(this);
    }

    private void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        DataManager.geteqinsulins(loginBean.getToken(), type, (call, response) -> {
            if (response.code == 200) {
                lvDataInfo.setVisibility(View.VISIBLE);
                llLast.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
                List<InsulinDeviceInfo> list = (List<InsulinDeviceInfo>) response.object;
                InsulinInfusionRecordAdapter deviceListAdapter = new InsulinInfusionRecordAdapter(getPageContext(), list, type);
                lvDataInfo.setAdapter(deviceListAdapter);
            } else if (response.code == 30002) {
                lvDataInfo.setVisibility(View.GONE);
                llLast.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }


    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_insulin_infusion_record, null);
        tvDeviceManage = view.findViewById(R.id.tv_infusion_device_manage);
        tvBaseMode = view.findViewById(R.id.tv_infusion_base_mode);
        flPlan = view.findViewById(R.id.fl_infusion_plan_num);
        tvPlanNum = view.findViewById(R.id.tv_infusion_plan_num);
        ivRefresh = view.findViewById(R.id.iv_infusion_info_refresh);
        bleTips = view.findViewById(R.id.tv_infusion_base_ble_tips);
        ivLoadRefresh = view.findViewById(R.id.iv_infusion_info_refresh_trends);
        tvDayAll = view.findViewById(R.id.tv_infusion_info_day_all);
        tvInfoBig = view.findViewById(R.id.tv_infusion_info_big);
        tvBaseRate = view.findViewById(R.id.tv_infusion_info_base_rate);
        tvWarning = view.findViewById(R.id.tv_infusion_info_warning);
        lvDataInfo = view.findViewById(R.id.lv_insulin_base_info_list);
        llLast = view.findViewById(R.id.ll_insulin_infusion_last);
        tvNoData = view.findViewById(R.id.tv_insulin_base_info_no_data);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_infusion_device_manage:
                String deviceName = MySPUtils.getString(getPageContext(), MySPUtils.DEVICE_NAME);
                Intent intent;
                if (TextUtils.isEmpty(deviceName)) {
                    intent = new Intent(getPageContext(), InjectionAddDeviceNoActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getPageContext(), InsulinDeviceListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_infusion_base_mode:
                intent = new Intent(getPageContext(), InsulinBaseModeListActivity.class);
                startActivity(intent);
                break;
            case R.id.fl_infusion_plan_num:
                intent = new Intent(getPageContext(), InsulinInfusionPlanListActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_infusion_info_refresh:
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
                        if (!BleUtils.getInstance().initBlueBooth(this)) {
                            bleTips.setVisibility(View.VISIBLE);
                            bleTips.setText("请开启蓝牙和扫描设备权限");
                            return;
                        }
                        if (!isClick) {
                            return;
                        }
                        bleTips.setVisibility(View.VISIBLE);
                        bleTips.setText("正在同步数据,请您稍等片刻");
                        ivLoadRefresh.setBackgroundResource(R.drawable.loading_progress_bar);
                        ivRefresh.setVisibility(View.GONE);
                        ivLoadRefresh.setVisibility(View.VISIBLE);
                        ivLoadRefresh.startLoadingAnim();
                        time = 60;
                        mHandler.sendEmptyMessage(1);
                        refreshDeviceInfo();
                    }
                } else {
                    if (!BleUtils.getInstance().initBlueBooth(this)) {
                        bleTips.setVisibility(View.VISIBLE);
                        bleTips.setText("请开启蓝牙和扫描设备权限");
                        return;
                    }
                    if (!isClick) {
                        return;
                    }
                    bleTips.setVisibility(View.VISIBLE);
                    bleTips.setText("正在同步数据,请您稍等片刻");
                    ivLoadRefresh.setBackgroundResource(R.drawable.loading_progress_bar);
                    ivRefresh.setVisibility(View.GONE);
                    ivLoadRefresh.setVisibility(View.VISIBLE);
                    ivLoadRefresh.startLoadingAnim();
                    time = 60;
                    mHandler.sendEmptyMessage(1);
                    refreshDeviceInfo();
                }
                break;
            case R.id.tv_infusion_info_day_all:
                setBg(tvDayAll, tvInfoBig, tvBaseRate, tvWarning);
                type = "1";
                getData();
                break;
            case R.id.tv_infusion_info_big:
                setBg(tvInfoBig, tvDayAll, tvBaseRate, tvWarning);
                type = "2";
                getData();
                break;
            case R.id.tv_infusion_info_base_rate:
                setBg(tvBaseRate, tvInfoBig, tvDayAll, tvWarning);
                type = "3";
                getData();
                break;
            case R.id.tv_infusion_info_warning:
                setBg(tvWarning, tvInfoBig, tvBaseRate, tvDayAll);
                type = "4";
                getData();
                break;
            default:
                break;
        }
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
            isClick = true;
            ivRefresh.setVisibility(View.VISIBLE);
            ivLoadRefresh.setVisibility(View.GONE);
            ivLoadRefresh.stopLoaddingAnim();
            bleTips.setVisibility(View.VISIBLE);
            bleTips.setText("同步失败,请稍后重试");
        }
    }

    private boolean isClick = true;

    private void refreshDeviceInfo() {
        if (time <= 0) {
            return;
        }
        isClick = false;
        switch (type) {
            case "1":
                //回传日总量记录
                BleUtils.getInstance().connect(false, getPageContext(), mac, new BleUtils.OnDataCallBackImpl() {

                    @Override
                    public void onDisConnect(boolean isSuccess) {
                        super.onDisConnect(isSuccess);
                        runOnUiThread(() -> {
                            if (!isSuccess) {
                                try {
                                    Thread.sleep(2_000);
                                    refreshDeviceInfo();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onConnect() {
                        super.onConnect();
                        runOnUiThread(() -> {
                            new Handler().postDelayed(() -> BleUtils.getInstance().sendData("0577A88240"), 1_000);
                        });
                    }

                    @Override
                    public void onRecordInfoList(List<RecordInfo> recordInfoList) {
                        super.onRecordInfoList(recordInfoList);
                        runOnUiThread(() -> {
                            BleUtils.getInstance().disConnect();
                            isClick = true;
                            time = -1;
                            List<RecordDataInfo> list = new ArrayList<>();
                            for (int i = 0; i < recordInfoList.size(); i++) {
                                String dataTime = recordInfoList.get(i).getMonth() + "-" + recordInfoList.get(i).getDate();
                                String value = BleUtils.byte2double(recordInfoList.get(i).getDataHeight(), recordInfoList.get(i).getDataLow(), 100) + "";
                                list.add(new RecordDataInfo(dataTime, value));
                            }
                            setLvDataInfo(list);
                        });
                    }
                });
                break;
            case "2":
                //回传大剂量记录
                BleUtils.getInstance().connect(false, getPageContext(), mac, new BleUtils.OnDataCallBackImpl() {

                    @Override
                    public void onDisConnect(boolean isSuccess) {
                        super.onDisConnect(isSuccess);
                        runOnUiThread(() -> {
                            if (!isSuccess) {
                                try {
                                    Thread.sleep(2_000);
                                    refreshDeviceInfo();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onConnect() {
                        super.onConnect();
                        runOnUiThread(() -> {
                            new Handler().postDelayed(() -> BleUtils.getInstance().sendData("0577A99261"), 1_000);
                        });
                    }

                    @Override
                    public void onRecordBigInfoList(List<RecordBigInfo> recordBigInfoList) {
                        super.onRecordBigInfoList(recordBigInfoList);
                        runOnUiThread(() -> {
                            BleUtils.getInstance().disConnect();
                            isClick = true;
                            time = -1;
                            List<RecordDataInfo> list = new ArrayList<>();
                            for (int i = 0; i < recordBigInfoList.size(); i++) {
                                String dataTime = recordBigInfoList.get(i).getMonth() + "-" + recordBigInfoList.get(i).getDate() + " " +
                                        recordBigInfoList.get(i).getHour() + ":" + recordBigInfoList.get(i).getMinute();
                                String value = BleUtils.byte2double(recordBigInfoList.get(i).getDataHeight(), recordBigInfoList.get(i).getDataLow(), 100) + "";
                                list.add(new RecordDataInfo(dataTime, value));
                            }
                            setLvDataInfo(list);
                        });
                    }
                });
                break;
            case "3":
                //基础量记录
                BleUtils.getInstance().connect(false, getPageContext(), mac, new BleUtils.OnDataCallBackImpl() {
                    @Override
                    public void onDisConnect(boolean isSuccess) {
                        super.onDisConnect(isSuccess);
                        runOnUiThread(() -> {
                            if (!isSuccess) {
                                try {
                                    Thread.sleep(2_000);
                                    refreshDeviceInfo();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onConnect() {
                        super.onConnect();
                        runOnUiThread(() -> {
                            new Handler().postDelayed(() -> BleUtils.getInstance().sendData("0577AAA202"), 1_000);
                        });
                    }

                    @Override
                    public void onRecordInfoList(List<RecordInfo> recordInfoList) {
                        super.onRecordInfoList(recordInfoList);
                        runOnUiThread(() -> {
                            BleUtils.getInstance().disConnect();
                            isClick = true;
                            time = -1;
                            List<RecordDataInfo> list = new ArrayList<>();
                            for (int i = 0; i < recordInfoList.size(); i++) {
                                String dataTime = recordInfoList.get(i).getMonth() + "-" + recordInfoList.get(i).getDate();
                                String value = BleUtils.byte2double(recordInfoList.get(i).getDataHeight(), recordInfoList.get(i).getDataLow(), 100) + "";
                                list.add(new RecordDataInfo(dataTime, value));
                            }
                            setLvDataInfo(list);
                        });
                    }
                });
                break;
            case "4":
                //回传报警记录
                BleUtils.getInstance().connect(false, getPageContext(), mac, new BleUtils.OnDataCallBackImpl() {
                    @Override
                    public void onDisConnect(boolean isSuccess) {
                        super.onDisConnect(isSuccess);
                        runOnUiThread(() -> {
                            if (!isSuccess) {
                                try {
                                    Thread.sleep(2_000);
                                    refreshDeviceInfo();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onConnect() {
                        super.onConnect();
                        runOnUiThread(() -> {
                            new Handler().postDelayed(() -> BleUtils.getInstance().sendData("0577ABB223"), 1_000);
                        });
                    }

                    @Override
                    public void onRecordErrorList(List<RecordErrorInfo> recordErrorInfos) {
                        super.onRecordErrorList(recordErrorInfos);
                        runOnUiThread(() -> {
                            BleUtils.getInstance().disConnect();
                            isClick = true;
                            time = -1;
                            List<RecordDataInfo> list = new ArrayList<>();
                            for (int i = 0; i < recordErrorInfos.size(); i++) {
                                String dataTime = recordErrorInfos.get(i).getMonth() + "-" + recordErrorInfos.get(i).getDate()
                                        + " " + recordErrorInfos.get(i).getHour() + ":" + recordErrorInfos.get(i).getMinute();
                                String value = BleUtils.hexToInt(recordErrorInfos.get(i).getType()) + "";
                                list.add(new RecordDataInfo(dataTime, value));
                            }
                            setLvDataInfo(list);
                        });
                    }
                });
                break;
            default:
                break;
        }
    }

    private void setLvDataInfo(List<RecordDataInfo> list) {
        String data = new Gson().toJson(list);
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        bleTips.setVisibility(View.INVISIBLE);
        ivRefresh.setVisibility(View.VISIBLE);
        ivLoadRefresh.setVisibility(View.GONE);
        ivLoadRefresh.stopLoaddingAnim();
        DataManager.addeqinsulins(loginBean.getToken(), type, data, (call, response) -> {
            if (response.code == 200) {
                getData();
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }

    private void setBg(TextView tvCheck, TextView tvUncheck1, TextView tvUncheck2, TextView tvUncheck3) {
        tvCheck.setBackground(getResources().getDrawable(R.drawable.shape_bg_main_green_90));
        tvCheck.setTextColor(getResources().getColor(R.color.white));
        tvUncheck1.setBackground(null);
        tvUncheck1.setTextColor(getResources().getColor(R.color.color_666));
        tvUncheck2.setBackground(null);
        tvUncheck2.setTextColor(getResources().getColor(R.color.color_666));
        tvUncheck3.setBackground(null);
        tvUncheck3.setTextColor(getResources().getColor(R.color.color_666));

    }

}
