package com.vice.bloodpressure.ui.activity.insulin;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;

import retrofit2.Call;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:设备列表
 */
public class InsulinDeviceListActivity extends XYSoftUIBaseActivity {


    private TextView tvBreak;
    private TextView tvDeviceNum;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().topView().removeAllViews();
        containerView().addView(initView());
    }


    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_insulin_device_list, null);
        tvBreak = view.findViewById(R.id.tv_insulin_break_device);
        tvDeviceNum = view.findViewById(R.id.tv_insulin_device_list_num);
        tvBreak.setOnClickListener(v -> {
            breakDevice();
        });
        return view;
    }

    private void breakDevice() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        String eqcode="";
        //        String mac = BlueUtils.getBlueMac();
        Call<String> requestCall = DataManager.unbindeqinsulin(eqcode, token, (call, response) -> {
            ToastUtils.showShort("解绑成功");
            finish();
        }, (call, t) -> {
            ToastUtils.showShort("解绑成功");
            finish();
        });
    }

}
