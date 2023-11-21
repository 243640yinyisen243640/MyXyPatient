package com.vice.bloodpressure.ui.activity.insulin;

import android.os.Bundle;
import android.view.View;
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
 * 描述:输注记录
 */
public class InsulinInfusionRecordListActivity extends XYSoftUIBaseActivity {

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
        topViewManager().titleTextView().setText("输注记录");
        containerView().addView(initView());
        initValues();
    }

    private void initValues() {
        InsulinInfusionRecordAdapter deviceListAdapter = new InsulinInfusionRecordAdapter(getPageContext(), new ArrayList<>());
        lvDataInfo.setAdapter(deviceListAdapter);
    }


    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_insulin_infusion_record, null);
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
