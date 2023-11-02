package com.vice.bloodpressure.ui.activity.injection;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.eventbus.EventBusUtils;
import com.quinovaresdk.bletransfer.BleTransfer;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.event.BlueUnbindEvent;
import com.vice.bloodpressure.utils.BlueUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import retrofit2.Call;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:解绑
 */
public class InjectionProgramUnbindDeviceActivity extends XYSoftUIBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        containerView().addView(initView());
        EventBusUtils.register(this);
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout._activity_device_unbind, null);
        findViewById(R.id.tv_device_unbind).setOnClickListener(v -> {
            BleTransfer.getInstance().unBindDevice();
            //清楚绑定的缓存
//            SPUtils.putBean("blueBindState", false);
        });
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BlueUnbindEvent event) {
        Log.i("yys", "onMessageEvent: ");
        boolean bind = event.isUnBind();
        if (bind){
            LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
            String token = loginBean.getToken();

            String mac = BlueUtils.getBlueMac();

            Call<String> requestCall = DataManager.unbindInsulin(mac,token, (call, response) -> {
                ToastUtils.showShort("解绑成功");
                finish();
            }, (call, t) -> {
                ToastUtils.showShort("解绑成功");
                finish();
            });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }
}
