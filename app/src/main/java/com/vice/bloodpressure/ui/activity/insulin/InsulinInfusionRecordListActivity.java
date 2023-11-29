package com.vice.bloodpressure.ui.activity.insulin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

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

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:输注记录
 */
public class InsulinInfusionRecordListActivity extends XYSoftUIBaseActivity implements View.OnClickListener {

    private TextView tvDeviceManage;
    private TextView tvBaseMode;
    private ImageView ivRefresh;
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
                InsulinInfusionRecordAdapter deviceListAdapter = new InsulinInfusionRecordAdapter(getPageContext(), list,type);
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
        ivRefresh = view.findViewById(R.id.iv_infusion_info_refresh);
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
            case R.id.iv_infusion_info_refresh:
                refreshDeviceInfo();
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

    private boolean isClick = true;

    private void refreshDeviceInfo() {
        isClick = false;
        switch (type) {
            case "1":
                //回传日总量记录
                BleUtils.getInstance().connect(getPageContext(), mac, new BleUtils.OnDataCallBackImpl() {
                    @Override
                    public void connect() {
                        super.connect();
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
                            List<RecordDataInfo> list = new ArrayList<>();
                            for (int i = 0; i < recordInfoList.size(); i++) {
                                String dataTime = recordInfoList.get(i).getMonth() + "-" + recordInfoList.get(i).getDate();
                                String value = BleUtils.byte2double(recordInfoList.get(i).getDataHeight(), recordInfoList.get(i).getDataLow()) + "";
                                list.add(new RecordDataInfo(dataTime, value));
                            }
                            setLvDataInfo(list);
                        });
                    }
                });
                break;
            case "2":
                //回传大剂量记录
                BleUtils.getInstance().connect(getPageContext(), mac, new BleUtils.OnDataCallBackImpl() {
                    @Override
                    public void connect() {
                        super.connect();
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
                            List<RecordDataInfo> list = new ArrayList<>();
                            for (int i = 0; i < recordBigInfoList.size(); i++) {
                                String dataTime = recordBigInfoList.get(i).getMonth() + "-" + recordBigInfoList.get(i).getDate() + " " +
                                        recordBigInfoList.get(i).getHour() + ":" + recordBigInfoList.get(i).getMinute();
                                String value = BleUtils.byte2double(recordBigInfoList.get(i).getDataHeight(), recordBigInfoList.get(i).getDataLow()) + "";
                                list.add(new RecordDataInfo(dataTime, value));
                            }
                            setLvDataInfo(list);
                        });
                    }
                });
                break;
            case "3":
                //基础量记录
                BleUtils.getInstance().connect(getPageContext(), mac, new BleUtils.OnDataCallBackImpl() {
                    @Override
                    public void connect() {
                        super.connect();
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
                            List<RecordDataInfo> list = new ArrayList<>();
                            for (int i = 0; i < recordInfoList.size(); i++) {
                                String dataTime = recordInfoList.get(i).getMonth() + "-" + recordInfoList.get(i).getDate();
                                String value = BleUtils.byte2double(recordInfoList.get(i).getDataHeight(), recordInfoList.get(i).getDataLow()) + "";
                                list.add(new RecordDataInfo(dataTime, value));
                            }
                            setLvDataInfo(list);
                        });
                    }
                });
                break;
            case "4":
                //回传报警记录
                BleUtils.getInstance().connect(getPageContext(), mac, new BleUtils.OnDataCallBackImpl() {
                    @Override
                    public void connect() {
                        super.connect();
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
                            List<RecordDataInfo> list = new ArrayList<>();
                            for (int i = 0; i < recordErrorInfos.size(); i++) {
                                String dataTime = recordErrorInfos.get(i).getMonth() + "-" + recordErrorInfos.get(i).getDate();
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
