package com.vice.bloodpressure.ui.fragment.insulin;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.allen.library.utils.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.fragment.BaseEventBusFragment;
import com.vice.bloodpressure.bean.insulin.InsulinDeviceInfo;
import com.vice.bloodpressure.event.MSTBlueEventBus;
import com.vice.bloodpressure.net.OkHttpCallBack;
import com.vice.bloodpressure.net.XyUrl;
import com.vice.bloodpressure.ui.activity.insulin.InsulinInfusionPlanListActivity;
import com.vice.bloodpressure.ui.activity.insulin.InsulinInfusionRecordListActivity;
import com.vice.bloodpressure.ui.activity.insulin.ScanBlueActivity;
import com.vice.bloodpressure.utils.BleMSTUtils;
import com.vice.bloodpressure.utils.BleUtils;
import com.vice.bloodpressure.utils.MySPUtils;
import com.vice.bloodpressure.view.LoadingImageView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static me.devilsen.czxing.thread.ExecutorUtil.runOnUiThread;


public class InsulinFragment extends BaseEventBusFragment {
    private static final int GET_DATA = 0x002998;
    private static final int NO_DATA = 0x001998;
    private static final int BLUETOOTH_PERMISSIONS_REQUEST_CODE = 20;
    @BindView(R.id.iv_main_insulin_refresh)
    ImageView ivRefresh;
    @BindView(R.id.iv_main_insulin_refresh_trends)
    LoadingImageView ivLoadRefresh;
    @BindView(R.id.tv_insulin_main_message_tips)
    TextView tvMessage;
    @BindView(R.id.tv_insulin_main_ble_tips)
    TextView mainBleTips;
    @BindView(R.id.tv_main_insulin_num)
    TextView tvDeviceNum;
    @BindView(R.id.tv_main_insulin_time)
    TextView tvTime;


    @BindView(R.id.ll_main_insulin_kl)
    LinearLayout llKl;
    @BindView(R.id.ll_main_insulin_mst)
    LinearLayout llMst;
    @BindView(R.id.tv_main_insulin_electricity)
    TextView tvElectricity;
    @BindView(R.id.tv_main_insulin_electricity_mst)
    TextView tvMstElectricity;

    @BindView(R.id.tv_main_insulin_medicine)
    TextView tvMedicine;
    @BindView(R.id.tv_main_insulin_switch)
    TextView tvSwitch;
    @BindView(R.id.tv_main_insulin_warning)
    TextView tvWarning;

    @BindView(R.id.tv_main_insulin_mode)
    TextView tvMode;
    @BindView(R.id.tv_main_insulin_rate)
    TextView tvRate;
    @BindView(R.id.tv_main_insulin_injction)
    TextView tvInjction;
    @BindView(R.id.tv_main_insulin_injction_mst)
    TextView tvMstInjction;
    @BindView(R.id.tv_main_insulin_record)
    TextView tvRecord;
    @BindView(R.id.tv_main_insulin_record_mst)
    TextView tvMstRecord;

    private InsulinDeviceInfo data;

    public static InsulinFragment getInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        InsulinFragment insulinFragment = new InsulinFragment();
        insulinFragment.setArguments(bundle);
        return insulinFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_insulin;
    }

    @Override
    public void processHandlerMsg(Message msg) {
        switch (msg.what) {
            case GET_DATA:
                setData();
                break;
            case 10:
                time--;
                if (time > 0) {
                    getHandler().sendEmptyMessageDelayed(10, 1000);
                    Log.i("yys", "time==" + time);
                } else if (time == 0) {
                    time = 60;
                    //倒计时结束
                    isRefult = false;
                    ivRefresh.setVisibility(View.VISIBLE);
                    ivLoadRefresh.setVisibility(View.GONE);
                    ivLoadRefresh.stopLoaddingAnim();
                    mainBleTips.setVisibility(View.VISIBLE);
                    mainBleTips.setText("同步失败,请稍后重试");
                }
                break;
            default:
                break;

        }
    }

    private void setData() {
        if (Integer.parseInt(data.getEq_plan()) > 0) {
            tvMessage.setVisibility(View.VISIBLE);
        } else {
            tvMessage.setVisibility(View.GONE);
        }


        tvDeviceNum.setText(data.getEq_code());
        tvElectricity.setText(data.getPower());
        tvMstElectricity.setText(data.getPower());
        tvMedicine.setText(data.getDosage());
        tvSwitch.setText(data.getStatus());
        tvWarning.setText(data.getWorning());
        tvMode.setText("基础模式" + data.getModel());
        tvRate.setText("当前基础率" + data.getBase_rate());
        tvInjction.setText("已输注" + data.getValue());
        tvMstInjction.setText("已输注" + data.getValue());
        tvTime.setText("最近同步时间：" + data.getUpdatetime());


        if (!"--".equals(data.getPower())) {
            tvElectricity.setText(data.getPower() + "%");
            int power = Integer.parseInt(data.getPower());
            if (power >= 60) {
                tvElectricity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.electricity_full, 0, 0, 0);
            } else if (power > 30 && power < 61) {
                tvElectricity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.electricity_normal, 0, 0, 0);
            } else {
                tvElectricity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.electricity_warning, 0, 0, 0);
            }
        }
        if (!"--".equals(data.getDosage())) {
            double dosage = Double.valueOf(data.getDosage());
            if (dosage >= 300.00) {
                tvMedicine.setCompoundDrawablesWithIntrinsicBounds(R.drawable.medicine_full, 0, 0, 0);
            } else if (dosage > 101.00 && dosage < 200.00) {
                tvMedicine.setCompoundDrawablesWithIntrinsicBounds(R.drawable.medicine_normal, 0, 0, 0);
            } else {
                tvMedicine.setCompoundDrawablesWithIntrinsicBounds(R.drawable.medicine_warning, 0, 0, 0);
            }
        }

        if (!"--".equals(data.getStatus())) {
            //开关状态 1开 2关
            if ("1".equals(data.getStatus())) {
                tvSwitch.setText("开");
                tvSwitch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.insulin_up, 0, 0, 0);
            } else {
                tvSwitch.setText("关");
                tvSwitch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.insulin_off, 0, 0, 0);
            }
        }
        if (!"--".equals(data.getWorning())) {
            //异常
            if ("1".equals(data.getWorning())) {
                tvWarning.setText("正常");
                tvWarning.setCompoundDrawablesWithIntrinsicBounds(R.drawable.insulin_warning_normal, 0, 0, 0);
            } else {
                tvWarning.setText("异常");
                tvWarning.setCompoundDrawablesWithIntrinsicBounds(R.drawable.insulin_warning_un, 0, 0, 0);
            }
        }

        if (!"--".equals(data.getModel())) {
            tvMode.setText("1".equals(data.getModel()) ? "基础模式1" : "基础模式2");
        } else {
            tvMode.setText("暂无");

        }

        if (!"--".equals(data.getBase_rate())) {
            tvRate.setText("当前基础率" + data.getBase_rate());
        } else {
            tvRate.setText("暂无");

        }
        if (!"--".equals(data.getValue())) {
            tvInjction.setText("已输注" + data.getValue());
        } else {
            tvInjction.setText("暂无");
        }


    }


    @Override
    protected void init(View view) {
        getData();
        //1凯联  2迈士通
        String deviceType = MySPUtils.getString(getPageContext(), MySPUtils.BLUE_TYPE);
        if ("1".equals(deviceType)) {
            llKl.setVisibility(View.VISIBLE);
            llMst.setVisibility(View.GONE);
        } else {
            llKl.setVisibility(View.GONE);
            llMst.setVisibility(View.VISIBLE);
        }
    }

    private void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getActivity(), SharedPreferencesUtils.USER_INFO);
        HashMap map = new HashMap<>();
        map.put("access_token", loginBean.getToken());
        XyUrl.okPost(XyUrl.GET_EQ_INFO, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                data = JSONObject.parseObject(value, InsulinDeviceInfo.class);
                Message msg = Message.obtain();
                msg.obj = data;
                msg.what = GET_DATA;
                sendHandlerMessage(msg);
            }

            @Override
            public void onError(int error, String errorMsg) {
                Message msg = Message.obtain();
                msg.what = NO_DATA;
                sendHandlerMessage(msg);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBaseBlueData(MSTBlueEventBus event) {
        int type = getArguments().getInt("type");
        if (type != 1) {
            return;
        }
        if (event != null && event.getType() == 1) {
            Log.i("xjh", "onBaseBlueData: ");
            List<String> list = event.getStringList();
            if (list.size() == 80) {
                //48 49
                int valueInt = BleMSTUtils.hexToInt(list.get(55) + list.get(54));
                double valueDouble = valueInt / 10.0;
                value = valueDouble + "";
                //70 71
                int powerInt = BleMSTUtils.hexToInt(list.get(77) + list.get(76));
                if (powerInt == 4) {
                    power = "100";
                } else if (powerInt == 3) {
                    power = "75";
                } else if (powerInt == 2) {
                    power = "50";
                } else if (powerInt == 1) {
                    power = "25";
                } else {
                    power = "0";
                }
                time = -1;
                updateDevice();
            }
        }
    }

    //电量
    String power = "";
    //药量
    String dosage = "";
    //开关
    String status = "";
    //worning
    String worning = "";
    //基础模式类型 1 2
    String model = "";
    //基础率
    String base_rate = "";
    //已注射总量
    String value = "";

    private boolean isRefult = false;

    @OnClick({R.id.tv_main_insulin_record, R.id.tv_main_insulin_record_mst, R.id.iv_main_insulin_refresh, R.id.tv_main_insulin_num, R.id.tv_insulin_main_message_tips})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.tv_main_insulin_record:
            case R.id.tv_main_insulin_record_mst:
                //                String type = "1";
                //                if ("1".equals(type)) {
                //                    intent = new Intent(getActivity(), InsulinInfusionRecordListActivity.class);
                //                    intent.putExtra("eq_plan", data.getEq_plan());
                //                    startActivity(intent);
                //                } else {
                //                    intent = new Intent(getActivity(), InsulinDeviceListActivity.class);
                //                    startActivity(intent);
                //                }
                intent = new Intent(getActivity(), InsulinInfusionRecordListActivity.class);
                intent.putExtra("eq_plan", data.getEq_plan());
                startActivity(intent);
                break;

            case R.id.iv_main_insulin_refresh:
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
                        mainBleTips.setVisibility(View.VISIBLE);
                        mainBleTips.setText("请开启蓝牙和扫描设备权限");
                        ActivityCompat.requestPermissions(getActivity(), new String[]{
                                "android.permission.BLUETOOTH_SCAN",
                                "android.permission.BLUETOOTH_ADVERTISE",
                                "android.permission.BLUETOOTH_CONNECT"}, BLUETOOTH_PERMISSIONS_REQUEST_CODE);
                    } else {
                        if (!BleUtils.getInstance().initBlueBooth(getActivity())) {
                            mainBleTips.setVisibility(View.VISIBLE);
                            mainBleTips.setText("请开启蓝牙和扫描设备权限");
                            return;
                        }
                        if (isRefult) {
                            return;
                        }

                        isRefult = true;

                        String mac = MySPUtils.getString(getPageContext(), MySPUtils.BLUE_MAC);
                        //                String eqcode = "E4:89:98:3C:D9:8D";

                        Log.i("yys", "mac===" + mac);
                        if (TextUtils.isEmpty(mac)) {
                            isRefult = false;
                            mainBleTips.setVisibility(View.VISIBLE);
                            mainBleTips.setText("请先绑定设备");
                            return;
                        }
                        ivLoadRefresh.setBackgroundResource(R.drawable.loading_progress_bar);
                        ivRefresh.setVisibility(View.GONE);
                        ivLoadRefresh.setVisibility(View.VISIBLE);
                        ivLoadRefresh.startLoadingAnim();
                        mainBleTips.setVisibility(View.VISIBLE);
                        mainBleTips.setText("同步数据,请稍后");
                        //凯联
                        getBaseData1(mac);
                        //迈士通
                        getBaseDataMST(mac);
                        time = 60;
                        getHandler().sendEmptyMessage(10);
                    }

                } else {
                    Log.i("yys", "time=="+System.currentTimeMillis());
                    if (!BleUtils.getInstance().initBlueBooth(getActivity())) {
                        mainBleTips.setVisibility(View.VISIBLE);
                        mainBleTips.setText("请开启蓝牙和扫描设备权限");
                        return;
                    }
                    if (isRefult) {
                        return;
                    }

                    isRefult = true;

                    String mac = MySPUtils.getString(getPageContext(), MySPUtils.BLUE_MAC);
                    //                String eqcode = "E4:89:98:3C:D9:8D";

                    Log.i("yys", "mac===" + mac);
                    if (TextUtils.isEmpty(mac)) {
                        isRefult = false;
                        mainBleTips.setVisibility(View.VISIBLE);
                        mainBleTips.setText("请先绑定设备");
                        return;
                    }
                    ivLoadRefresh.setBackgroundResource(R.drawable.loading_progress_bar);
                    ivRefresh.setVisibility(View.GONE);
                    ivLoadRefresh.setVisibility(View.VISIBLE);
                    ivLoadRefresh.startLoadingAnim();
                    mainBleTips.setVisibility(View.VISIBLE);
                    mainBleTips.setText("同步数据,请稍后");
                    time = 60;
                    Log.i("yys", "time=="+System.currentTimeMillis());
                    getHandler().sendEmptyMessage(10);
                    getBaseData1(mac);
                    //迈士通
                    getBaseDataMST(mac);
                }
                break;
            case R.id.tv_main_insulin_num:
                startActivity(new Intent(getPageContext(), ScanBlueActivity.class));
                break;
            case R.id.tv_insulin_main_message_tips:
                Intent intent1 = new Intent(getPageContext(), InsulinInfusionPlanListActivity.class);
                intent1.putExtra("type", "1");
                startActivity(intent1);
                break;
            default:
                break;

        }
    }

    //获取蓝牙运行基础信息
    private void getBaseDataMST(String mac) {
        if (time <= 0) {
            return;
        }
        if (!BleMSTUtils.getInstance().isConnect()) {
            BleMSTUtils.getInstance().connect(getPageContext().getApplicationContext(), mac);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                BleMSTUtils.getInstance().setRead();
                try {
                    Thread.sleep(2_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                BleMSTUtils.getInstance().sendData(BleMSTUtils.getInstance().comlpeteInstruct("55 14 00 A3 00 AA "));
            }
        }).start();
    }

    private int time = 60;


    private void getBaseData1(String eqcode) {
        if (time <= 0) {
            return;
        }
        BleUtils.getInstance().connect(false, getPageContext(), eqcode, new BleUtils.OnDataCallBackImpl() {

            @Override
            public void onDisConnect(boolean isSuccess) {
                super.onDisConnect(isSuccess);
                runOnUiThread(() -> {
                    if (!isSuccess) {
                        try {
                            Thread.sleep(2_000);
                            getBaseData1(eqcode);
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
                    new Handler().postDelayed(() -> {
                        BleUtils.getInstance().sendData("0577A11369");
                        isRefult = true;
                    }, 2_000);
                });
            }

            @Override
            public void onWorkState(String ele, byte drugHeight, byte drugLow, String isBlock, String infuSwitch) {
                //电量  药高位  药低位  阻塞标志阻塞 0A 0B  输注开关 开（A5) 关（B5)
                runOnUiThread(() -> {
                    power = BleUtils.hexToInt(ele) + "";
                    Log.i("yys", "drugHeight==" + drugHeight);
                    Log.i("yys", "drugLow==" + drugLow);
                    dosage = BleUtils.byte2double(drugHeight, drugLow) + "";
                    status = TextUtils.equals(infuSwitch, "A5") ? "1" : "2";
                    worning = TextUtils.equals(isBlock, "0B") ? "1" : "2";
                    BleUtils.getInstance().disConnect();
                    getBaseData(eqcode);
                });
            }
        });
    }

    private void getBaseData(String eqcode) {
        if (time <= 0) {
            return;
        }
        Log.i("yys", "getBaseData: ");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //回传基础模式、当前时段基础率、本时段已输注基础量
        BleUtils.getInstance().connect(false, getPageContext(), eqcode, new BleUtils.OnDataCallBackImpl() {
            @Override
            public void onDisConnect(boolean isSuccess) {
                super.onDisConnect(isSuccess);
                Log.i("yys", "onDisConnect: ");
                runOnUiThread(() -> {
                    if (!isSuccess) {
                        getBaseData(eqcode);
                    }
                });
            }

            @Override
            public void onConnect() {
                super.onConnect();
                runOnUiThread(() -> {
                    new Handler().postDelayed(() -> {
                        BleUtils.getInstance().sendData("0577A2230A");
                        isRefult = true;
                    }, 1_000);
                });
            }

            @Override
            public void onBaseData(String baseState, String baseValue, String baseValueAll) {
                //基础模式  当前时段基础率  本时段已输注基础量
                runOnUiThread(() -> {
                    Log.i("yys", "onBaseData: ");
                    model = BleUtils.hexToInt(baseState) + "";
                    base_rate = ((double) BleUtils.hexToInt(baseValue) / 10) + "";
                    value = ((double) BleUtils.hexToInt(baseValueAll) / 100) + "";
                    BleUtils.getInstance().disConnect();
                    time = -1;
                    updateDevice();
                });
            }
        });
    }


    private void updateDevice() {
        isRefult = false;
        ivRefresh.setVisibility(View.VISIBLE);
        ivLoadRefresh.setVisibility(View.GONE);
        ivLoadRefresh.stopLoaddingAnim();
        mainBleTips.setVisibility(View.GONE);
        Log.i("yys", "updateDevice: ");
        //        String mac = MySPUtils.getString(getPageContext(), MySPUtils.SERIAL_NUMBER);
        String deviceName = MySPUtils.getString(getPageContext(), MySPUtils.DEVICE_NAME);
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getActivity(), SharedPreferencesUtils.USER_INFO);
        DataManager.updateeqinfo(loginBean.getToken(), deviceName, power, dosage, status, worning, model, base_rate, value, (call, response) -> {
            if (response.code == 200) {
                getData();
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}
