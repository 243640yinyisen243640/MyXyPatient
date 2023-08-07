package com.vice.bloodpressure.ui.activity.healthrecordadd;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseHandlerActivity;

import butterknife.BindView;
import retrofit2.Call;

/**
 * 作者: beauty
 * 类名:
 * 传参:type 1:偏高   2：偏低
 * contentDouble  上传的值
 * 描述:
 */
public class BloodSugarAddUnNormalActivity extends BaseHandlerActivity {
    @BindView(R.id.tv_title_warning)
    TextView tvTitle;
    @BindView(R.id.tv_desc_warning)
    TextView descTv;
    @BindView(R.id.tv_know_warning)
    TextView knowTv;
    /**
     * 1:偏高   2：偏低
     */
    private String type;
    /**
     * 上传的值
     */
    private String result;
    private String selectPosition;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("预警提示");
        type = getIntent().getStringExtra("type");
        result = getIntent().getStringExtra("result");
        time = getIntent().getStringExtra("time");
        selectPosition = getIntent().getStringExtra("selectPosition");
        setValues();
        setListener();
    }

    private void setListener() {
        knowTv.setOnClickListener(v -> {
            save();
        });
    }

    private void setValues() {
        Log.i("yys","type=="+type+"result=="+result+"time=="+time+"selectPosition=="+selectPosition);
        if ("1".equals(type)) {
            tvTitle.setText("血糖高了");
            descTv.setText(String.format("您上传%s高于正常范围", result));
        } else {
            tvTitle.setText("血糖低了");
            descTv.setText(String.format("您上传%s低于正常范围", result));
        }
    }

    private void save() {
        LoginBean userLogin = (LoginBean) SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.USER_INFO);
        Call<String> requestCall = DataManager.saveXuetang(result, selectPosition, time, userLogin.getToken(), (call, response) -> {
            if (response.code==200){
                finish();
            }else {
                Toast.makeText(getPageContext(),"添加失败",Toast.LENGTH_SHORT).show();
            }

        }, (call, t) -> {
            Toast.makeText(getPageContext(),"网络连接异常，请稍后重试",Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected View addContentLayout() {
        return getLayoutInflater().inflate(R.layout.popup_center, contentLayout, false);
    }

    @Override
    public void processHandlerMsg(Message msg) {

    }
}
