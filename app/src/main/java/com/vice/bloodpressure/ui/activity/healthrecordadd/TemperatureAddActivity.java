package com.vice.bloodpressure.ui.activity.healthrecordadd;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.lsp.RulerView;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.eventbus.EventBusUtils;
import com.lyd.baselib.utils.eventbus.EventMessage;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseHandlerActivity;
import com.vice.bloodpressure.constant.ConstantParam;
import com.vice.bloodpressure.constant.DataFormatManager;
import com.vice.bloodpressure.net.OkHttpCallBack;
import com.vice.bloodpressure.net.XyUrl;
import com.vice.bloodpressure.utils.DataUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 描述: 温度记录
 * 作者: LYD
 * 创建日期: 2020/5/9 13:56
 */
public class TemperatureAddActivity extends BaseHandlerActivity {
    @BindView(R.id.tv_temperature_default)
    TextView tvTemperature;
    @BindView(R.id.ruler_temperature)
    RulerView rulerTemperature;
    @BindView(R.id.tv_temperature_time)
    TextView tvTime;
    @BindView(R.id.ll_temperature_select_time)
    LinearLayout llSelectTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.recording_temperature));
        setRuler();
        setTvSave();
        setCurrentTime();
    }

    private void setCurrentTime() {
        SimpleDateFormat Allformat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String allTimeString = TimeUtils.millis2String(System.currentTimeMillis(), Allformat);
        tvTime.setText(allTimeString);
    }

    private void setTvSave() {
        getTvSave().setText("保存");
        getTvSave().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                toSave();
                saveData();
            }
        });
    }
    private void saveData() {
        LoginBean user = (LoginBean) SharedPreferencesUtils.getBean(Utils.getApp(), SharedPreferencesUtils.USER_INFO);
        String userid = user.getUserid();
        String docId = SPStaticUtils.getString("docId");
        String token = user.getToken();
        String temperature = tvTemperature.getText().toString().trim();
        String time = tvTime.getText().toString().trim();
        Call<String> requestCall = DataManager.saveDataTemperature(token,userid, temperature, time, docId,"2", (call, response) -> {
            if (response.code == 200) {
                ToastUtils.showShort(response.msg);
                setResult(RESULT_OK);
                finish();
            }else {

            }
        }, (call, t) -> {
            ToastUtils.showShort(getString(R.string.network_error));
        });
    }

    /**
     * 保存体温
     */
    private void toSave() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.USER_INFO);
        HashMap<String, Object> map = new HashMap<>();
        map.put("access_token", loginBean.getToken());
        map.put("weight", tvTemperature.getText().toString().trim());
        map.put("type", 2);
        map.put("datetime", tvTime.getText().toString().trim());
        XyUrl.okPostSave(XyUrl.ADD_WEIGHT, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                ToastUtils.showShort(R.string.save_ok);
                EventBusUtils.post(new EventMessage<>(ConstantParam.ADD_WEIGHT));
                finish();
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });


    }

    private void setRuler() {
        rulerTemperature.setOnChooseResulterListener(new RulerView.OnChooseResulterListener() {
            @Override
            public void onEndResult(String result) {

            }

            @Override
            public void onScrollResult(String result) {
//                tvTemperature.setText(floatStringToIntString(result));
                tvTemperature.setText(result);
            }
        });
    }

    private String floatStringToIntString(String floatString) {
        int a = (int) Float.parseFloat(floatString);
        return String.valueOf(a);
    }


    @Override
    protected View addContentLayout() {
        View view = getLayoutInflater().inflate(R.layout.activity_temperature_add, contentLayout, false);
        return view;
    }

    @Override
    public void processHandlerMsg(Message msg) {

    }

    @OnClick(R.id.ll_temperature_select_time)
    public void onViewClicked() {
        //        PickerUtils.showTimeHm(getPageContext(), new PickerUtils.TimePickerCallBack() {
        //            @Override
        //            public void execEvent(String content) {
        //                tvTime.setText(content);
        //            }
        //        });
        showTimeWindow();
    }

    private void showTimeWindow() {
        Calendar currentDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        startDate.set(currentYear - 120, 0, 1, 0, 0);
        TimePickerView timePickerView = new TimePickerBuilder(getPageContext(), (date, v) -> {
            String content = DataUtils.convertDateToString(date, DataFormatManager.TIME_FORMAT_Y_M_D_H_M);
            tvTime.setText(content);
        }).setDate(currentDate).setRangDate(startDate, endDate)
                .setType(new boolean[]{true, true, true, true, true, false})
                .setSubmitColor(ContextCompat.getColor(getPageContext(), R.color.blue))
                .setCancelColor(ContextCompat.getColor(getPageContext(), R.color.black_text))
                .build();
        timePickerView.show();
    }

}
