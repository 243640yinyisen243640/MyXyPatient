package com.vice.bloodpressure.ui.activity.injection;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.allen.library.utils.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.eventbus.EventBusUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.injection.PlanNumInfo;
import com.vice.bloodpressure.constant.DataFormatManager;
import com.vice.bloodpressure.event.DataAddEvent;
import com.vice.bloodpressure.utils.DataUtils;
import com.vice.bloodpressure.utils.PickerUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 作者: beauty
 * 类名:
 * 传参:isAdd   false 需要传 true  data time value injection_id  true 不需要
 * 描述:数据新增和编辑
 */
public class InjectionDataAddActivity extends XYSoftUIBaseActivity {
    private boolean isAdd;
    private TextView tvAddType1;
    private TextView tvAddType2;
    private TextView tvAddType3;
    private TextView tvAddType4;
    private TextView tvAddDate;
    private TextView tvAddTime;
    private TextView tvAddValue;
    private LinearLayout llAddType;

    private TextView tvConfirm;

    private int check = 0;
    private int allType = 0;
    //    1~50单位
    private String unitValue = "15";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("数据新增");
        containerView().addView(initView());
        initValues();
    }

    private void initValues() {
        isAdd = getIntent().getBooleanExtra("isAdd", false);
        if (isAdd) {
            //是添加
            //            initData();
            String dataTime = DataUtils.currentDateString(DataFormatManager.TIME_FORMAT_Y_M_D_H_M);
            String data = dataTime.split(" ")[0];
            String time = dataTime.split(" ")[1];
            tvAddDate.setText(data);
            tvAddTime.setText(time);
            getTimeData(data, time);
            tvAddDate.setOnClickListener(v -> {
                //选择日期
                PickerUtils.showTimeWindow(getPageContext(), new boolean[]{true, true, true, false, false, false}, DataFormatManager.TIME_FORMAT_Y_M_D, new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        getTimeData(content, tvAddTime.getText().toString().trim());
                    }
                });

            });
            tvAddTime.setOnClickListener(v -> {
                //选择时间
                PickerUtils.showTimeWindow(getPageContext(), new boolean[]{false, false, false, true, true, false}, DataFormatManager.TIME_FORMAT_H_M, new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //选择时间
                        getTimeData(tvAddDate.getText().toString().trim(), content);
                    }
                });
            });
        } else {
            //不是添加
            tvAddDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvAddTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            llAddType.setVisibility(View.GONE);

            String date = getIntent().getStringExtra("date");
            tvAddDate.setText(date);
            String time = getIntent().getStringExtra("time");
            tvAddTime.setText(time);
            String value = getIntent().getStringExtra("value");
            //你没写这个
            unitValue = value;
            tvAddValue.setText(value+"单位");
        }
        tvAddValue.setOnClickListener(v -> {
            //选择结果
            PickerUtils.showChooseSinglePicker(getPageContext(), "", getList(), object -> {
                tvAddValue.setText(getList().get(Integer.parseInt(String.valueOf(object))));
                unitValue = Integer.parseInt(String.valueOf(object)) + 1 + "";
            });
        });
    }

    private void getTimeData(String date, String time) {
        //掉接口
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        // 2023-10-01
        Call<String> requestCall = DataManager.getPlanNum(date, time, token, (call, response) -> {
            if (200 == response.code) {
                PlanNumInfo info = (PlanNumInfo) response.object;
                allType = Integer.parseInt(info.getPlan_num());
                check = Integer.parseInt(info.getNow_num());
                initData();
                tvAddDate.setText(date);
                tvAddTime.setText(time);
            } else {
                ToastUtils.showToast(response.msg);
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }

    private void initData() {
        initType();
        setType();
    }

    /**
     * 选择注射单位
     */
    private List<String> getList() {
        List<String> allList = new ArrayList<>();
        for (int i = 1; i < 51; i++) {
            allList.add(i + "单位");
        }
        return allList;
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout._activity_add_data, null);
        tvAddType1 = view.findViewById(R.id.tv_add_type_1);
        tvAddType2 = view.findViewById(R.id.tv_add_type_2);
        tvAddType3 = view.findViewById(R.id.tv_add_type_3);
        tvAddType4 = view.findViewById(R.id.tv_add_type_4);
        tvAddDate = view.findViewById(R.id.tv_add_data_date);
        tvAddTime = view.findViewById(R.id.tv_add_data_time);
        tvAddValue = view.findViewById(R.id.tv_add_data_value);
        llAddType = view.findViewById(R.id.ll_add_data_type);
        tvConfirm = view.findViewById(R.id.tv_add_confirm);
        tvConfirm.setOnClickListener(v -> confirm());
        return view;
    }

    private void setType() {
        if (allType > 0) {
            setTextView(tvAddType1, 1);
            tvAddType1.setVisibility(View.VISIBLE);
        }
        if (allType > 1) {
            setTextView(tvAddType2, 2);
            tvAddType2.setVisibility(View.VISIBLE);
        }
        if (allType > 2) {
            setTextView(tvAddType3, 3);
            tvAddType3.setVisibility(View.VISIBLE);
        }
        if (allType > 3) {
            setTextView(tvAddType4, 4);
            tvAddType4.setVisibility(View.VISIBLE);
        }
    }

    private void initType() {
        initTextView(tvAddType1);
        initTextView(tvAddType2);
        initTextView(tvAddType3);
        initTextView(tvAddType4);
    }

    private void initTextView(TextView textView) {
        textView.setBackground(getDrawable(R.drawable._shape_no_can_check));
        textView.setTextColor(Color.parseColor("#9D9D9D"));
        textView.setVisibility(View.INVISIBLE);
    }

    private void setTextView(TextView textView, int currectNum) {
        if (currectNum == check) {
            textView.setBackgroundColor(Color.parseColor("#4D0CA25B"));
            textView.setTextColor(Color.parseColor("#0CA25B"));
        } else {
            textView.setBackground(getDrawable(R.drawable._shape_can_check));
            textView.setTextColor(Color.parseColor("#545454"));
        }
    }

    private void confirm() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        //提交
        if (isAdd) {
            //添加数据
            String date = tvAddDate.getText().toString().trim();
            if (TextUtils.isEmpty(date)) {
                ToastUtils.showToast("请选择日期");
                return;
            }

            String time = tvAddTime.getText().toString().trim();
            if (TextUtils.isEmpty(time)) {
                ToastUtils.showToast("请选择时间");
                return;
            }

            if (TextUtils.isEmpty(unitValue)) {
                ToastUtils.showToast("请选择注射值");
                return;
            }

            if (check == 0) {
                ToastUtils.showToast("请选择注射类型");
                return;
            }
            String dataTime = date + " " + time;

            // 2023-10-01
            Call<String> requestCall = DataManager.addInsulin("2", check + "", unitValue, dataTime, token, (call, response) -> {
                if (200 == response.code) {
                    EventBusUtils.post(new DataAddEvent());
                    ToastUtils.showToast(response.msg);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    ToastUtils.showToast(response.msg);
                }
            }, (call, t) -> {
                ToastUtils.showToast("网络连接不可用，请稍后重试！");
            });
        } else {
            //修改数据
            //掉接口
            if (TextUtils.isEmpty(unitValue)) {
                ToastUtils.showToast("请选择注射值");
                return;
            }
            String jection_id = getIntent().getStringExtra("injection_id");

            // 2023-10-01
            Call<String> requestCall = DataManager.editInsulin(unitValue, jection_id, token, (call, response) -> {
                if (200 == response.code) {
                    ToastUtils.showToast(response.msg);
                    EventBusUtils.post(new DataAddEvent());
                    setResult(RESULT_OK);
                    finish();
                }
            }, (call, t) -> {
                ToastUtils.showToast("网络连接不可用，请稍后重试！");
            });
        }
    }
}
