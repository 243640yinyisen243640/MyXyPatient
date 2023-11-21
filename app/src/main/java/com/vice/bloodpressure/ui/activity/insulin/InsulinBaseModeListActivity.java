package com.vice.bloodpressure.ui.activity.insulin;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.insulin.InsulinInfusionRecordAdapter;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;

import java.util.ArrayList;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:基础模式
 */
public class InsulinBaseModeListActivity extends XYSoftUIBaseActivity {

    /**
     * 头部
     * */
    private ImageView IvBack;
    private TextView tvModeFirst;
    private TextView tvModeSecond;


    /**
     * 布局
     */
    private TextView tvDeviceManage;
    private TextView tvBaseMode;
    private TextView tvDayAll;
    private TextView tvInfoBig;
    private TextView tvBaseRate;
    private TextView tvWarning;
    private ListView lvDataInfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().topView().removeAllViews();
        topViewManager().topView().addView(initTopView());
        containerView().addView(initView());
        initValues();
    }

    private View initTopView() {
        View topView = View.inflate(getPageContext(), R.layout.include_base_mode_top, null);
        IvBack = topView.findViewById(R.id.iv_base_mode_back);
        tvModeFirst = topView.findViewById(R.id.tv_base_mode_first);
        tvModeSecond = topView.findViewById(R.id.tv_base_mode_second);
        return topView;
    }

    private void initValues() {
        InsulinInfusionRecordAdapter deviceListAdapter = new InsulinInfusionRecordAdapter(getPageContext(), new ArrayList<>());
        lvDataInfo.setAdapter(deviceListAdapter);
    }


    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_insulin_base_mode, null);
        tvDeviceManage = view.findViewById(R.id.tv_infusion_device_manage);
        tvBaseMode = view.findViewById(R.id.tv_infusion_base_mode);
        tvDayAll = view.findViewById(R.id.tv_infusion_info_day_all);
        tvInfoBig = view.findViewById(R.id.tv_infusion_info_big);
        tvBaseRate = view.findViewById(R.id.tv_infusion_info_base_rate);
        tvWarning = view.findViewById(R.id.tv_infusion_info_warning);
        lvDataInfo = view.findViewById(R.id.lv_insulin_add_device);
        return view;
    }


}
