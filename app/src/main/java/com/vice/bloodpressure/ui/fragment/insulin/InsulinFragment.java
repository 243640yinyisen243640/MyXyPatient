package com.vice.bloodpressure.ui.fragment.insulin;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.eventbus.EventMessage;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.fragment.BaseEventBusFragment;
import com.vice.bloodpressure.bean.injection.HomeInjectionInfo;
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

    private HomeInjectionInfo data;


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
        String planNum = data.getPlan_num();

    }



    @Override
    protected void init(View view) {
        getData();

    }

    private void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getActivity(), SharedPreferencesUtils.USER_INFO);
        HashMap map = new HashMap<>();
        map.put("access_token", loginBean.getToken());
        XyUrl.okPost(XyUrl.GET_INDEX_INJECTION, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                data = JSONObject.parseObject(value, HomeInjectionInfo.class);
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

    @OnClick({R.id.tv_main_insulin_record})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.tv_main_insulin_record:
                String type = "1";
                if ("1".equals(type)){
                    intent = new Intent(getActivity(), InsulinInfusionRecordListActivity.class);
                    startActivity(intent);
                }else {
                    intent = new Intent(getActivity(), InsulinDeviceListActivity.class);
                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}
