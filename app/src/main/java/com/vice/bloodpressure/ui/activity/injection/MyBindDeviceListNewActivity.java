package com.vice.bloodpressure.ui.activity.injection;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.injection.MyBindDeviceNewAdapter;
import com.vice.bloodpressure.base.activity.BaseActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class MyBindDeviceListNewActivity extends BaseActivity {
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("我的设备");
        setRv();
    }

    private void setRv() {
        String[] stringArray = getResources().getStringArray(R.array.my_device_bind_list_name_new);
        List<String> list = Arrays.asList(stringArray);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getPageContext());
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setAdapter(new MyBindDeviceNewAdapter(list));
    }

    @Override
    protected View addContentLayout() {
        View view = getLayoutInflater().inflate(R.layout.activity_my_bind_device_list, contentLayout, false);
        return view;
    }
}