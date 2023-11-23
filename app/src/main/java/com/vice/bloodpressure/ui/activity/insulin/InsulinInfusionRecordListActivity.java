package com.vice.bloodpressure.ui.activity.insulin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.allen.library.utils.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.insulin.InsulinInfusionRecordAdapter;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.insulin.InsulinDeviceInfo;
import com.vice.bloodpressure.ui.activity.injection.InjectionAddDeviceNoActivity;

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
    private TextView tvLast;
    private TextView tvNoData;
    /**
     * 1日总量 2大剂量 3基础率 4报警记录
     */
    private String type = "1";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("输注记录");
        containerView().addView(initView());
        initListner();
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
                tvLast.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
                List<InsulinDeviceInfo> list = (List<InsulinDeviceInfo>) response.object;
                InsulinInfusionRecordAdapter deviceListAdapter = new InsulinInfusionRecordAdapter(getPageContext(), list);
                lvDataInfo.setAdapter(deviceListAdapter);
            } else if (response.code == 30002) {
                lvDataInfo.setVisibility(View.GONE);
                tvLast.setVisibility(View.GONE);
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
        tvLast = view.findViewById(R.id.tv_insulin_infusion_last);
        tvNoData = view.findViewById(R.id.tv_insulin_base_info_no_data);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_infusion_device_manage:
                String type1 = "1";
                Intent intent;
                if ("1".equals(type1)) {
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
                getData();
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
