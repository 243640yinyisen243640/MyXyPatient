package com.vice.bloodpressure.ui.activity.injection;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.eventbus.EventBusUtils;
import com.quinovaresdk.bletransfer.BleTransfer;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.event.BlueBindEvent;
import com.vice.bloodpressure.utils.BlueUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import retrofit2.Call;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:绑定
 */
public class InjectionProgramBindDeviceActivity extends XYSoftUIBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("添加设备");
        containerView().addView(initView());
        EventBusUtils.register(this);
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout._activity_device_bind, null);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BlueBindEvent event) {
        Log.i("yys", "onMessageEvent: ");
        boolean bind = event.isBind();
        if (bind) {
            LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
            String token = loginBean.getToken();

            String mac = BlueUtils.getBlueMac();

            Call<String> requestCall = DataManager.bindInsulin(mac, token, (call, response) -> {
                ToastUtils.showShort(response.msg);
                if (200 == response.code) {
                    finish();
                } else {
                    BleTransfer.getInstance().unBindDevice();
                    //清楚绑定的缓存
                    BlueUtils.putBoolean(Utils.getApp(),"blueBindState",false);
                    finish();
                }
            }, (call, t) -> {
                ToastUtils.showShort("网络连接失败，请稍后重试");
                BleTransfer.getInstance().unBindDevice();
                //清楚绑定的缓存
                BlueUtils.putBoolean(Utils.getApp(),"blueBindState",false);
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
