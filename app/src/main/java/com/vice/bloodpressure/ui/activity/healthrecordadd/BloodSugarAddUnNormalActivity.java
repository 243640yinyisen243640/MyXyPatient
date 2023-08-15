package com.vice.bloodpressure.ui.activity.healthrecordadd;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseHandlerActivity;
import com.vice.bloodpressure.ui.activity.MainActivity;
import com.vice.bloodpressure.ui.activity.healthrecordlist.BloodSugarListActivity;
import com.vice.bloodpressure.ui.activity.healthrecordlist.HealthActivity;
import com.vice.bloodpressure.ui.activity.healthrecordlist.HealthRecordBloodSugarMainActivity;
import com.vice.bloodpressure.ui.activity.sysmsg.SystemMsgListActivity;

import butterknife.BindView;
import retrofit2.Call;

/**
 * 作者: beauty
 * 类名:
 * 传参:type 1:偏高   2：偏低
 * from 1：消息列表  2：健康记录  3:7天或30天血糖  4：首页血糖的Fragment中进来的  5：推送进来的  6：也是阿里推送（不理解为啥）
 * contentDouble  上传的值
 * 描述:
 */
public class BloodSugarAddUnNormalActivity extends BaseHandlerActivity {
    @BindView(R.id.ll_desc_warning)
    LinearLayout backLinearLayout;
    @BindView(R.id.tv_title_warning)
    TextView tvTitle;
    @BindView(R.id.tv_desc_warning)
    TextView descTv;
    @BindView(R.id.tv_desc_warning_text)
    TextView warningTitleTv;
    @BindView(R.id.tv_desc_warning_unit)
    TextView unitTitleTv;
    @BindView(R.id.tv_know_warning)
    TextView knowTv;
    @BindView(R.id.tv_know_warning_back)
    TextView backTv;
    /**
     * 1:偏高   2：偏低
     */
    private String type;
    /**
     * 1：消息列表  2：健康记录  3:7天或30天血糖  4：首页血糖的Fragment中进来的  5：推送进来的  6：也是阿里推送（不理解为啥）
     */
    private String from;
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
        from = getIntent().getStringExtra("from");
        result = getIntent().getStringExtra("result");
        time = getIntent().getStringExtra("time");
        selectPosition = getIntent().getStringExtra("selectPosition");
        Log.i("yys","type=="+type+"from==="+from);
        setValues();
        setListener();
    }

    private void setListener() {
        knowTv.setOnClickListener(v -> {
            save();
        });
        backTv.setOnClickListener(v -> {
            finish();
        });
    }

    private void setValues() {
        Log.i("yys", "type==" + type + "result==" + result + "time==" + time + "selectPosition==" + selectPosition);
        descTv.setText(result);
        if ("1".equals(type)) {
            unitTitleTv.setTextColor(Color.parseColor("#FF0000"));
            descTv.setTextColor(Color.parseColor("#FF0000"));
            tvTitle.setText("血糖偏高");
            warningTitleTv.setText("高于正常范围");
            backLinearLayout.setBackground(getDrawable(R.drawable.warning_high));
        } else {
            descTv.setTextColor(Color.parseColor("#2390F6"));
            unitTitleTv.setTextColor(Color.parseColor("#2390F6"));
            tvTitle.setText("血糖偏低");
            warningTitleTv.setText("低于正常范围");
            backLinearLayout.setBackground(getDrawable(R.drawable.warning_low));
        }
    }

    private void save() {
        LoginBean userLogin = (LoginBean) SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.USER_INFO);
        Call<String> requestCall = DataManager.saveXuetang(result, selectPosition, time, userLogin.getToken(), (call, response) -> {
            if (response.code == 200) {
                //                finish();
              //  1：消息列表  2：健康记录  3:7天或30天血糖  4：首页血糖的Fragment中进来的  5：推送进来的  6：也是阿里推送（不理解为啥）
                Toast.makeText(getPageContext(), "添加成功", Toast.LENGTH_SHORT).show();
                Intent intent = null;
                if ("1".equals(from)){
                    intent  = new Intent(this, SystemMsgListActivity.class);
                }else if ("2".equals(type)){
                    intent  = new Intent(this, HealthActivity.class);
                }else if ("3".equals(from)){
                    intent  = new Intent(this, HealthRecordBloodSugarMainActivity.class);
                }else if ("7".equals(from)){
                    intent  = new Intent(this, BloodSugarListActivity.class);
                }else {
                    intent  = new Intent(this, MainActivity.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getPageContext(), "添加失败", Toast.LENGTH_SHORT).show();
            }

        }, (call, t) -> {
            Toast.makeText(getPageContext(), "网络连接异常，请稍后重试", Toast.LENGTH_SHORT).show();
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
