package com.vice.bloodpressure.ui.activity.followupvisit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.lyd.baselib.utils.eventbus.EventBusUtils;
import com.lyd.baselib.utils.eventbus.EventMessage;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseHandlerActivity;
import com.vice.bloodpressure.constant.ConstantParam;
import com.vice.bloodpressure.net.OkHttpCallBack;
import com.vice.bloodpressure.net.XyUrl;

import butterknife.BindView;

/**
 * 作者: beauty
 * 类名:
 * 传参:jsonResult
 * 描述:添加随访以后的操作
 */
public class FollowUpVisitPressureTipsActivity extends BaseHandlerActivity implements View.OnClickListener {

    @BindView(R.id.tv_follow_save)
    TextView saveTextView;
    @BindView(R.id.tv_follow_give_up)
    TextView giveUpTextView;


    private String jsonResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonResult = getIntent().getStringExtra("jsonResult");
        initListener();
    }

    private void initListener() {
        saveTextView.setOnClickListener(this);
        giveUpTextView.setOnClickListener(this);

    }

    @Override
    protected View addContentLayout() {
        return getLayoutInflater().inflate(R.layout.activity_follow_up_visit_save, contentLayout, false);
    }

    @Override
    public void processHandlerMsg(Message msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_follow_give_up:
                Intent intent = new Intent(this, FollowUpVisitBloodPressureSubmitActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_follow_save:
                XyUrl.okPostJson(XyUrl.FOLLOW_ADD, jsonResult, new OkHttpCallBack<String>() {
                    @Override
                    public void onSuccess(String value) {
                        ToastUtils.showShort(value);
                        finish();
                        EventBusUtils.post(new EventMessage<>(ConstantParam.FOLLOW_UP_VISIT_SUBMIT));
                    }

                    @Override
                    public void onError(int errorCode, final String errorMsg) {

                    }
                });
                break;
            default:

                break;
        }
    }


}
