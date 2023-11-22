package com.vice.bloodpressure.ui.activity.insulin;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.insulin.InsulinBaseModeAdapter;
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

    private TextView tvDayAll;
    private ImageView ivRefresh;
    private ListView lvDataInfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().topView().removeAllViews();
        containerView().addView(initView());
        initValues();
    }



    private void initValues() {
        InsulinBaseModeAdapter deviceListAdapter = new InsulinBaseModeAdapter(getPageContext(), new ArrayList<>());
        lvDataInfo.setAdapter(deviceListAdapter);
    }


    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_insulin_base_mode, null);
        IvBack = view.findViewById(R.id.iv_base_mode_back);
        tvModeFirst = view.findViewById(R.id.tv_base_mode_first);
        tvModeSecond = view.findViewById(R.id.tv_base_mode_second);

        tvDayAll = view.findViewById(R.id.tv_base_mode_day_all);
        ivRefresh = view.findViewById(R.id.iv_base_mode_refresh);
        lvDataInfo = view.findViewById(R.id.lv_base_mode_list);
        return view;
    }


}
