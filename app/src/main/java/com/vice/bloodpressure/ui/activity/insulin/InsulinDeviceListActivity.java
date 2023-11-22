package com.vice.bloodpressure.ui.activity.insulin;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.insulin.InsulinDeviceListAdapter;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;

import java.util.ArrayList;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:设备列表
 */
public class InsulinDeviceListActivity extends XYSoftUIBaseActivity {


    private TextView tvBreak;
    private ListView lvDataInfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().topView().removeAllViews();
        containerView().addView(initView());
        initValues();
    }



    private void initValues() {
        InsulinDeviceListAdapter deviceListAdapter = new InsulinDeviceListAdapter(getPageContext(), new ArrayList<>());
        lvDataInfo.setAdapter(deviceListAdapter);
    }


    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_insulin_device_list, null);
        tvBreak = view.findViewById(R.id.tv_insulin_break_device);
        lvDataInfo = view.findViewById(R.id.lv_insulin_device_list);
        return view;
    }


}
