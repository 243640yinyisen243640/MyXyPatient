package com.vice.bloodpressure.ui.fragment.insulin;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.allen.library.utils.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.eventbus.EventMessage;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.fragment.BaseEventBusFragment;
import com.vice.bloodpressure.bean.insulin.InsulinDeviceInfo;
import com.vice.bloodpressure.net.OkHttpCallBack;
import com.vice.bloodpressure.net.XyUrl;
import com.vice.bloodpressure.ui.activity.insulin.InsulinDeviceListActivity;
import com.vice.bloodpressure.ui.activity.insulin.InsulinInfusionRecordListActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;


public class InsulinFragment extends BaseEventBusFragment {
    private static final int GET_DATA = 0x002998;
    private static final int NO_DATA = 0x001998;
    @BindView(R.id.iv_main_insulin_refresh)
    ImageView ivRefresh;
    @BindView(R.id.tv_main_insulin_num)
    TextView tvDeviceNum;
    @BindView(R.id.tv_main_insulin_time)
    TextView tvTime;


    @BindView(R.id.tv_main_insulin_electricity)
    TextView tvElectricity;
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
    @BindView(R.id.tv_main_insulin_record)
    TextView tvRecord;

    private InsulinDeviceInfo data;


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
            default:
                break;

        }
    }

    private void setData() {
        tvDeviceNum.setText(data.getEq_code());
        if (!"暂无".equals(data.getEq_code())){
            tvDeviceNum.setText(data.getEq_code());
            //电量

            tvElectricity.setText(data.getPower());
            //61~100 蓝色  31~60黄色 0~30黄色
            int power = Integer.getInteger(data.getPower());
            if (power >= 60) {
                tvElectricity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.electricity_full, 0, 0, 0);
            } else if (power > 30 && power < 61){
                tvElectricity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.electricity_normal, 0, 0, 0);
            }else {
                tvElectricity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.electricity_warning, 0, 0, 0);
            }

            tvMedicine.setText(data.getDosage());
            int dosage = Integer.getInteger(data.getDosage());
            if (dosage >= 300) {
                tvMedicine.setCompoundDrawablesWithIntrinsicBounds(R.drawable.medicine_full, 0, 0, 0);
            } else if (dosage > 101 && dosage < 200) {
                tvMedicine.setCompoundDrawablesWithIntrinsicBounds(R.drawable.medicine_normal, 0, 0, 0);
            } else {
                tvMedicine.setCompoundDrawablesWithIntrinsicBounds(R.drawable.medicine_warning, 0, 0, 0);
            }

            //开关状态 1开 2关
            if ("1".equals(data.getStatus())) {
                tvSwitch.setText("开");
                tvSwitch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.insulin_up, 0, 0, 0);
            } else {
                tvSwitch.setText("关");
                tvSwitch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.insulin_off, 0, 0, 0);
            }
            //异常
            if ("1".equals(data.getWorning())) {
                tvWarning.setText("正常");
                tvWarning.setCompoundDrawablesWithIntrinsicBounds(R.drawable.insulin_warning_normal, 0, 0, 0);
            } else {
                tvWarning.setText("异常");
                tvWarning.setCompoundDrawablesWithIntrinsicBounds(R.drawable.insulin_warning_un, 0, 0, 0);
            }

            tvMode.setText("1".equals(data.getModel()) ? "基础模式1" : "基础模式2");
            tvRate.setText("当前基础率" + data.getBase_rate());
            tvInjction.setText("已输注" + data.getValue());
            tvTime.setText(data.getUpdatetime());
        }else {
            tvDeviceNum.setText(data.getEq_code());
            //电量

            tvElectricity.setText(data.getPower());
            tvMedicine.setText(data.getDosage());
            tvSwitch.setText(data.getStatus());
            tvWarning.setText(data.getWorning());
            tvMode.setText("暂无");
            tvRate.setText("暂无");
            tvInjction.setText("暂无");
            tvTime.setText("最近同步时间："+data.getUpdatetime());
        }
    }


    @Override
    protected void init(View view) {
        getData();

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

    @Override
    protected void receiveEvent(EventMessage event) {
        super.receiveEvent(event);
        switch (event.getCode()) {

        }
    }

    @OnClick({R.id.tv_main_insulin_record, R.id.iv_main_insulin_refresh})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.tv_main_insulin_record:
                String type = "1";
                if ("1".equals(type)) {
                    intent = new Intent(getActivity(), InsulinInfusionRecordListActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), InsulinDeviceListActivity.class);
                    startActivity(intent);
                }

                break;

            case R.id.iv_main_insulin_refresh:

                //点击刷新按钮以后，先去获取设备的信息，然后掉接口，把设备信息传给接口，刷新数据
                //设备号
                String eqcode;
                //设备号
                String power;
                //药量
                String dosage;
                //开关
                String status;
                //worning
                String worning;
                //基础模式类型 1 2
                String model;
                //基础率
                String base_rate;
                //已注射总量
                String value;

                break;
            default:
                break;

        }
    }

    private void updateDevice(String eqcode, String power, String dosage, String status, String worning, String model, String base_rate, String value) {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getActivity(), SharedPreferencesUtils.USER_INFO);
        DataManager.updateeqinfo(loginBean.getToken(), eqcode, power, dosage, status, worning, model, base_rate, value, (call, response) -> {
            if (response.code == 200) {

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
