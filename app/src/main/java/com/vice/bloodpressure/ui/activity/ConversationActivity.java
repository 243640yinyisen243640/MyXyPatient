package com.vice.bloodpressure.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseActivity;

import retrofit2.Call;

/**
 * 描述: 聊天页面
 * 作者: LYD
 * 创建日期: 2019/3/20 16:33
 */
public class ConversationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KeyboardUtils.fixAndroidBug5497(this);
        String title = getIntent().getData().getQueryParameter("title");
        setTitle(title);
        getTvSave().setVisibility(View.VISIBLE);
        getTvSave().setText("举报");
        getTvSave().setOnClickListener(v -> {
            reportDoctor();
        });
    }


    private void reportDoctor() {
        LoginBean user = (LoginBean) SharedPreferencesUtils.getBean(Utils.getApp(), SharedPreferencesUtils.USER_INFO);
        Call<String> requestCall = DataManager.reportDoctor(user.getToken(), (call, response) -> {
            ToastUtils.showShort(response.msg);
        }, (call, t) -> {
            ToastUtils.showShort(R.string.network_error);
        });
    }

    @Override
    protected View addContentLayout() {
        View layout = getLayoutInflater().inflate(R.layout.activity_conversation, contentLayout, false);
        return layout;
    }
}
