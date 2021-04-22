package com.vice.bloodpressure.ui.test;

import android.view.View;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseActivity;

public class TestActivity extends BaseActivity {


    @Override
    protected View addContentLayout() {
        View view = getLayoutInflater().inflate(R.layout.activity_test, contentLayout, false);
        return view;
    }
}