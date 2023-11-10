package com.vice.bloodpressure.ui.fragment.injection;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.eventbus.EventMessage;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.fragment.BaseEventBusFragment;
import com.vice.bloodpressure.bean.injection.HomeInjectionInfo;
import com.vice.bloodpressure.net.OkHttpCallBack;
import com.vice.bloodpressure.net.XyUrl;
import com.vice.bloodpressure.ui.activity.injection.HealthRecordInjectioneListActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;


public class InjectionFragment extends BaseEventBusFragment {
    private static final int GET_DATA = 0x002998;
    private static final int NO_DATA = 0x001998;
    @BindView(R.id.iv_home_fist_needle)
    ImageView ivFirst;
    @BindView(R.id.iv_home_second_needle)
    ImageView ivSecond;
    @BindView(R.id.iv_home_third_needle)
    ImageView ivThird;
    @BindView(R.id.iv_home_fourth_needle)
    ImageView ivFourth;


    @BindView(R.id.tv_home_injection_num)
    TextView tvNum;
    @BindView(R.id.tv_home_injection_needle_num)
    TextView tvNeedleNum;
    @BindView(R.id.tv_home_injection_time)
    TextView tvTime;
    @BindView(R.id.tv_main_injection_add)
    TextView tvAdd;

    private HomeInjectionInfo data;


    @Override
    public void processHandlerMsg(Message msg) {
        switch (msg.what) {
            case GET_DATA:
                setData();
                break;
            default:
                break;

        }
    }

    private void setData() {
        String planNum = data.getPlan_num();
        switch (planNum) {
            case "1":
                ivFirst.setVisibility(View.VISIBLE);
                ivSecond.setVisibility(View.INVISIBLE);
                ivThird.setVisibility(View.INVISIBLE);
                ivFourth.setVisibility(View.INVISIBLE);
                //1高 2低 3正常 0无
                setColorOne(data.getOne());
                break;
            case "2":
                ivFirst.setVisibility(View.VISIBLE);
                ivSecond.setVisibility(View.VISIBLE);
                ivThird.setVisibility(View.INVISIBLE);
                ivFourth.setVisibility(View.INVISIBLE);
                setColorOne(data.getOne());
                setColorTwo(data.getTwo());
                break;
            case "3":
                ivFirst.setVisibility(View.VISIBLE);
                ivSecond.setVisibility(View.VISIBLE);
                ivThird.setVisibility(View.VISIBLE);
                ivFourth.setVisibility(View.INVISIBLE);
                setColorOne(data.getOne());
                setColorTwo(data.getTwo());
                setColorThree(data.getThree());
                break;
            case "4":
                ivFirst.setVisibility(View.VISIBLE);
                ivSecond.setVisibility(View.VISIBLE);
                ivThird.setVisibility(View.VISIBLE);
                ivFourth.setVisibility(View.VISIBLE);
                setColorOne(data.getOne());
                setColorTwo(data.getTwo());
                setColorThree(data.getThree());
                setColorFour(data.getFour());
                break;
            default:
                break;
        }

        tvNum.setText(data.getValue());
        tvNeedleNum.setText("第" + data.getTimes() + "针");
        tvTime.setText(data.getTime());
    }

    private void setColorOne(String one) {
        if ("1".equals(one)) {
            ivFirst.setImageResource(R.drawable.main_injection_high);
        } else if ("2".equals(one)) {
            ivFirst.setImageResource(R.drawable.main_injection_low);
        } else if ("3".equals(one)) {
            ivFirst.setImageResource(R.drawable.main_injection_normal);
        } else {
            ivFirst.setImageResource(R.drawable.main_injection_no);
        }
    }

    private void setColorTwo(String two) {
        if ("1".equals(two)) {
            ivSecond.setImageResource(R.drawable.main_injection_high);
        } else if ("2".equals(two)) {
            ivSecond.setImageResource(R.drawable.main_injection_low);
        } else if ("3".equals(two)) {
            ivSecond.setImageResource(R.drawable.main_injection_normal);
        } else {
            ivSecond.setImageResource(R.drawable.main_injection_no);
        }
    }

    private void setColorThree(String three) {
        if ("1".equals(three)) {
            ivThird.setImageResource(R.drawable.main_injection_high);
        } else if ("2".equals(three)) {
            ivThird.setImageResource(R.drawable.main_injection_low);
        } else if ("3".equals(three)) {
            ivThird.setImageResource(R.drawable.main_injection_normal);
        } else {
            ivThird.setImageResource(R.drawable.main_injection_no);
        }
    }

    private void setColorFour(String four) {
        if ("1".equals(four)) {
            ivFourth.setImageResource(R.drawable.main_injection_high);
        } else if ("2".equals(four)) {
            ivFourth.setImageResource(R.drawable.main_injection_low);
        } else if ("3".equals(four)) {
            ivFourth.setImageResource(R.drawable.main_injection_normal);
        } else {
            ivFourth.setImageResource(R.drawable.main_injection_no);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_injection;
    }

    @Override
    protected void init(View view) {
        getData();

    }

    private void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getActivity(), SharedPreferencesUtils.USER_INFO);
        HashMap map = new HashMap<>();
        map.put("access_token", loginBean.getToken());
        XyUrl.okPost(XyUrl.GET_INDEX_INJECTION, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                data = JSONObject.parseObject(value, HomeInjectionInfo.class);
                Message msg = Message.obtain();
                msg.obj = data;
                msg.what = GET_DATA;
                sendHandlerMessage(msg);
            }

            @Override
            public void onError(int error, String errorMsg) {
                Message msg = Message.obtain();
                msg.what = NO_DATA;
                sendHandlerMessage(msg);
            }
        });
    }

    @Override
    protected void receiveEvent(EventMessage event) {
        super.receiveEvent(event);
        switch (event.getCode()) {

        }
    }

    @OnClick({R.id.tv_main_injection_add})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.tv_main_injection_add:
                intent = new Intent(getActivity(), HealthRecordInjectioneListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}
