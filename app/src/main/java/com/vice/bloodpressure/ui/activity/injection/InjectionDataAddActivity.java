package com.vice.bloodpressure.ui.activity.injection;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;

/**
 * 作者: beauty
 * 类名:
 * 传参:type  1:新增  2：编辑
 * 描述:数据新增和编辑
 */
public class InjectionDataAddActivity extends XYSoftUIBaseActivity {
    private TextView tvDate;
    private TextView tvTime;
    private TextView tvUnit;
    private CheckBox cbOne;
    private CheckBox cbTwo;
    private CheckBox cbThree;
    private CheckBox cbFour;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        containerView().addView(initView());
        initValues();
    }

    private void initValues() {

    }


    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_injection_data_add, null);
        tvDate = view.findViewById(R.id.tv_data_add_date);
        tvTime = view.findViewById(R.id.tv_data_add_time);
        tvUnit = view.findViewById(R.id.tv_data_add_unit);
        cbOne = view.findViewById(R.id.cb_injection_one);
        cbTwo = view.findViewById(R.id.cb_injection_two);
        cbThree = view.findViewById(R.id.cb_injection_three);
        cbFour = view.findViewById(R.id.cb_injection_four);
        return view;
    }
}
