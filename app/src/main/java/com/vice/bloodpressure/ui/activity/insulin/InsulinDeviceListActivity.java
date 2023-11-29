package com.vice.bloodpressure.ui.activity.insulin;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.utils.MySPUtils;
import com.vice.bloodpressure.view.popu.InsulinBreakDeviceWindow;

import retrofit2.Call;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:设备列表
 */
public class InsulinDeviceListActivity extends XYSoftUIBaseActivity {

    private InsulinBreakDeviceWindow breakDeviceWindow;
    private TextView tvBreak;
    private TextView tvDeviceNum;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("设备管理");
        containerView().addView(initView());
        initValues();
    }

    private void initValues() {
        tvDeviceNum.setText(MySPUtils.getString(getPageContext(), MySPUtils.DEVICE_NAME));
    }


    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_insulin_device_list, null);
        tvBreak = view.findViewById(R.id.tv_insulin_break_device);
        tvDeviceNum = view.findViewById(R.id.tv_insulin_device_list_num);
        tvBreak.setOnClickListener(v -> {
            if (breakDeviceWindow == null) {
                breakDeviceWindow = new InsulinBreakDeviceWindow(getPageContext(),

                        sure -> {
                            breakDevice();
                        });
            }
            breakDeviceWindow.showAsDropDown(containerView(), 0, 0, Gravity.CENTER);

        });
        return view;
    }

    private void breakDevice() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        String eqcode = MySPUtils.getString(getPageContext(), MySPUtils.DEVICE_NAME);
        //        String mac = BlueUtils.getBlueMac();
        Call<String> requestCall = DataManager.unbindeqinsulin(eqcode, token, (call, response) -> {
            if (response.code == 200) {
                breakDeviceWindow.dismiss();
                MySPUtils.putString(getPageContext(), MySPUtils.BLUE_MAC, "");
                MySPUtils.putString(getPageContext(), MySPUtils.BLUE_TYPE, "");
                MySPUtils.putString(getPageContext(), MySPUtils.SERIAL_NUMBER, "");
                MySPUtils.putString(getPageContext(), MySPUtils.DEVICE_NAME, "");
                finish();
            }
        }, (call, t) -> {
            ToastUtils.showShort("网络连接异常");
        });
    }

}
