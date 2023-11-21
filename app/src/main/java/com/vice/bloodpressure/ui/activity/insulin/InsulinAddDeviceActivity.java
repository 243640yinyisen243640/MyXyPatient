package com.vice.bloodpressure.ui.activity.insulin;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.insulin.InsulinAddDeviceListAdapter;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;

import java.util.ArrayList;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:添加设备
 */
public class InsulinAddDeviceActivity extends XYSoftUIBaseActivity {
    private LinearLayout llNoData;
    private TextView tvSearch;
    private ListView lvDevice;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("添加设备");
        containerView().addView(initView());
        initValues();
    }

    private void initValues() {
        InsulinAddDeviceListAdapter deviceListAdapter = new InsulinAddDeviceListAdapter(getPageContext(), new ArrayList<>());
        lvDevice.setAdapter(deviceListAdapter);
    }


    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_insulin_add_device, null);
        llNoData = view.findViewById(R.id.ll_insulin_add_device_no_data);
        lvDevice = view.findViewById(R.id.lv_insulin_add_device);
        tvSearch = view.findViewById(R.id.tv_insulin_add_device_search);
        tvSearch.setOnClickListener(v -> {

        });
        return view;
    }


}
