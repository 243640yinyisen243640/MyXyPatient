package com.vice.bloodpressure.ui.activity.injection;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.allen.library.utils.ToastUtils;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.AddProgramInfo;
import com.vice.bloodpressure.utils.BlueUtils;
import com.vice.bloodpressure.view.popu.InjectionNoBindPopupWindow;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 作者: beauty
 * 类名:
 * 传参: 是否新增  isAdd
 * isAdd==false   需传info
 * 描述:方案添加
 */
public class InjectionProgramAddActivity extends XYSoftUIBaseActivity {
    private AddProgramInfo info = new AddProgramInfo("", new ArrayList<>());
    private static final int REQUEST_CODE_PROGRAM_NAME = 10;
    private static final int REQUEST_CODE_CHOOSE_DETAIL = 11;
    private int chooseNum;
    private int chooseDetailPos;
    private TextView chooseDetailTextView;
    private TextView tvIsConnect;
    private TextView tvName;
    private TextView tvNum1;
    private TextView tvNum2;
    private TextView tvNum3;
    private TextView tvNum4;
    private LinearLayout llNum1;
    private LinearLayout llNum2;
    private LinearLayout llNum3;
    private LinearLayout llNum4;
    private TextView tvChooseTime1;
    private TextView tvChooseTime2;
    private TextView tvChooseTime3;
    private TextView tvChooseTime4;
    private TextView tvChooseDetail1;
    private TextView tvChooseDetail2;
    private TextView tvChooseDetail3;
    private TextView tvChooseDetail4;
    private TextView tvConfirm;
    private boolean isAdd;
    private InjectionNoBindPopupWindow forPopupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("方案编辑");
        containerView().addView(initView());
        isAdd = getIntent().getBooleanExtra("isAdd", true);
        if (isAdd) {
            initData();
        } else {
            info = (AddProgramInfo) getIntent().getSerializableExtra("info");
            tvName.setText(info.getPlan_name());
            initDataEdit();
        }
    }

    private void initDataEdit() {
        chooseNum = info.getPlanList().size();
        initChooseNum();
        LinearLayout linearLayout = findViewById(R.id.ll_num);
        TextView textView = (TextView) linearLayout.getChildAt(chooseNum * 2 - 2);
        textView.setTextColor(Color.parseColor("#0CA25B"));
        textView.setBackgroundColor(Color.parseColor("#4D0CA25B"));
        if (chooseNum > 0) {
            llNum1.setVisibility(View.VISIBLE);
        }
        if (chooseNum > 1) {
            llNum2.setVisibility(View.VISIBLE);
        }
        if (chooseNum > 2) {
            llNum3.setVisibility(View.VISIBLE);
        }
        if (chooseNum > 3) {
            llNum4.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < chooseNum; i++) {
            if (i == 0) {
                tvChooseTime1.setText(info.getPlanList().get(0).getPlan_time());
                tvChooseDetail1.setText(info.getPlanList().get(0).getDrug_name() + "," + info.getPlanList().get(0).getValue() + "单位");
            }
            if (i == 1) {
                tvChooseTime2.setText(info.getPlanList().get(1).getPlan_time());
                tvChooseDetail2.setText(info.getPlanList().get(1).getDrug_name() + "," + info.getPlanList().get(1).getValue() + "单位");
            }
            if (i == 2) {
                tvChooseTime3.setText(info.getPlanList().get(2).getPlan_time());
                tvChooseDetail3.setText(info.getPlanList().get(2).getDrug_name() + "," + info.getPlanList().get(2).getValue() + "单位");
            }
            if (i == 3) {
                tvChooseTime4.setText(info.getPlanList().get(3).getPlan_time());
                tvChooseDetail4.setText(info.getPlanList().get(3).getDrug_name() + "," + info.getPlanList().get(3).getValue() + "单位");
            }
        }
    }

    private void initData() {
        List<AddProgramInfo.plan> planList = new ArrayList<>();
        for (int i = 0; i < chooseNum; i++) {
            if (i == 0) {
                planList.add(new AddProgramInfo.plan("08:00", "", ""));
                tvChooseTime1.setText(planList.get(0).getPlan_time());
                tvChooseDetail1.setText("");
            }
            if (i == 1) {
                planList.add(new AddProgramInfo.plan("12:00", "", ""));
                tvChooseTime2.setText(planList.get(1).getPlan_time());
                tvChooseDetail2.setText("");
            }
            if (i == 2) {
                planList.add(new AddProgramInfo.plan("18:00", "", ""));
                tvChooseTime3.setText(planList.get(2).getPlan_time());
                tvChooseDetail3.setText("");
            }
            if (i == 3) {
                planList.add(new AddProgramInfo.plan("22:00", "", ""));
                tvChooseTime4.setText(planList.get(3).getPlan_time());
                tvChooseDetail4.setText("");
            }
        }
        info.getPlanList().clear();
        info.getPlanList().addAll(planList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("yys", "BlueUtils.isBind()====" + BlueUtils.isBind());
        if (BlueUtils.isBind()) {
            tvIsConnect.setVisibility(View.GONE);
            tvConfirm.setBackground(getResources().getDrawable(R.drawable._shape_confirm));
            tvConfirm.setTextColor(getResources().getColor(R.color.white));
            tvConfirm.setEnabled(true);
        } else {
            tvIsConnect.setVisibility(View.VISIBLE);
            tvConfirm.setBackground(getResources().getDrawable(R.drawable.shape_grey_5));
            tvConfirm.setTextColor(getResources().getColor(R.color.black_text));
            tvConfirm.setEnabled(false);
        }
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout._activity_program_add, null);
        tvIsConnect = view.findViewById(R.id.tv_program_is_connect);
        tvConfirm = view.findViewById(R.id.tv_program_confirm);
        tvConfirm.setOnClickListener(v -> {
            String plan_name = info.getPlan_name();
            if (TextUtils.isEmpty(plan_name)) {
                ToastUtils.showToast("请填写方案名称");
                return;
            }
            if (info.getPlanList().size()==0){
                ToastUtils.showToast("请选择针数");
                return;
            }
            boolean isPass = true;
            for (int i = 0; i < info.getPlanList().size(); i++) {
                if (TextUtils.isEmpty(info.getPlanList().get(i).getDrug_name()) ||
                        TextUtils.isEmpty(info.getPlanList().get(i).getPlan_time()) ||
                        TextUtils.isEmpty(info.getPlanList().get(i).getValue())) {
                    isPass = false;
                    break;
                }
            }
            if (!isPass) {
                ToastUtils.showToast("请完善方案信息");
                return;
            }
            String plan = new Gson().toJson(info.getPlanList());
            Log.i("yys", "plan==" + plan);
            //提交
            LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
            String token = loginBean.getToken();
            Call<String> requestCall = DataManager.addPlan(token, plan_name, plan, (call, response) -> {
                ToastUtils.showToast(response.msg);
                if (200 == response.code) {
                    ToastUtils.showToast(response.msg);
                    finish();
                }
            }, (call, t) -> {
                ToastUtils.showToast("网络连接不可用，请稍后重试！");
            });

        });

        tvIsConnect.setOnClickListener(v -> {
            //去链接蓝牙设备  eventBus接收连接结果
            startActivity(new Intent(getPageContext(), InjectionProgramAddDeviceActivity.class));
        });
        tvName = view.findViewById(R.id.tv_program_name);
        tvName.setOnClickListener(v -> {
            //编辑方案名称
            Intent intent = new Intent(getPageContext(), InjectionProgramAddNameActivity.class);
            intent.putExtra("name", info.getPlan_name() == null ? "" : info.getPlan_name());
            startActivityForResult(intent, REQUEST_CODE_PROGRAM_NAME);
        });
        tvNum1 = view.findViewById(R.id.tv_program_num_1);
        tvNum2 = view.findViewById(R.id.tv_program_num_2);
        tvNum3 = view.findViewById(R.id.tv_program_num_3);
        tvNum4 = view.findViewById(R.id.tv_program_num_4);
        llNum1 = view.findViewById(R.id.ll_program_num_1);
        llNum2 = view.findViewById(R.id.ll_program_num_2);
        llNum3 = view.findViewById(R.id.ll_program_num_3);
        llNum4 = view.findViewById(R.id.ll_program_num_4);
        tvChooseTime1 = view.findViewById(R.id.tv_program_choose_time_1);
        tvChooseTime2 = view.findViewById(R.id.tv_program_choose_time_2);
        tvChooseTime3 = view.findViewById(R.id.tv_program_choose_time_3);
        tvChooseTime4 = view.findViewById(R.id.tv_program_choose_time_4);
        tvChooseDetail1 = view.findViewById(R.id.tv_program_choose_detail_1);
        tvChooseDetail2 = view.findViewById(R.id.tv_program_choose_detail_2);
        tvChooseDetail3 = view.findViewById(R.id.tv_program_choose_detail_3);
        tvChooseDetail4 = view.findViewById(R.id.tv_program_choose_detail_4);


        tvNum1.setOnClickListener(v -> chooseNum(1, tvNum1));
        tvNum2.setOnClickListener(v -> chooseNum(2, tvNum2));
        tvNum3.setOnClickListener(v -> chooseNum(3, tvNum3));
        tvNum4.setOnClickListener(v -> chooseNum(4, tvNum4));

        tvChooseTime1.setOnClickListener(v -> chooseTime(1, tvChooseTime1));
        tvChooseTime2.setOnClickListener(v -> chooseTime(2, tvChooseTime2));
        tvChooseTime3.setOnClickListener(v -> chooseTime(3, tvChooseTime3));
        tvChooseTime4.setOnClickListener(v -> chooseTime(4, tvChooseTime4));
        tvChooseDetail1.setOnClickListener(v -> chooseDetail(1, tvChooseDetail1));
        tvChooseDetail2.setOnClickListener(v -> chooseDetail(2, tvChooseDetail2));
        tvChooseDetail3.setOnClickListener(v -> chooseDetail(3, tvChooseDetail3));
        tvChooseDetail4.setOnClickListener(v -> chooseDetail(4, tvChooseDetail4));
        return view;
    }

    private void chooseDetail(int chooseDetailPos, TextView textView) {
        this.chooseDetailPos = chooseDetailPos;
        chooseDetailTextView = textView;
        Intent intent = new Intent(getPageContext(), InjectionProgramAddNumActivity.class);
        intent.putExtra("chooseDetailPos", chooseDetailPos);
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_DETAIL);
    }

    private void chooseTime(int chooseTimePos, TextView textView) {
        initTime(chooseTimePos, textView);
    }

    private void chooseNum(int num, TextView textView) {
        chooseNum = num;
        initChooseNum();
        textView.setTextColor(Color.parseColor("#0CA25B"));
        textView.setBackgroundColor(Color.parseColor("#4D0CA25B"));
        if (num > 0) {
            llNum1.setVisibility(View.VISIBLE);
        }
        if (num > 1) {
            llNum2.setVisibility(View.VISIBLE);
        }
        if (num > 2) {
            llNum3.setVisibility(View.VISIBLE);
        }
        if (num > 3) {
            llNum4.setVisibility(View.VISIBLE);
        }
        initData();
    }

    private void initChooseNum() {
        tvNum1.setTextColor(Color.parseColor("#545454"));
        tvNum1.setBackground(getDrawable(R.drawable._shape_can_check));
        tvNum2.setTextColor(Color.parseColor("#545454"));
        tvNum2.setBackground(getDrawable(R.drawable._shape_can_check));
        tvNum3.setTextColor(Color.parseColor("#545454"));
        tvNum3.setBackground(getDrawable(R.drawable._shape_can_check));
        tvNum4.setTextColor(Color.parseColor("#545454"));
        tvNum4.setBackground(getDrawable(R.drawable._shape_can_check));
        llNum1.setVisibility(View.GONE);
        llNum2.setVisibility(View.GONE);
        llNum3.setVisibility(View.GONE);
        llNum4.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PROGRAM_NAME:
                    //方案名称
                    String name = data.getStringExtra("name");
                    info.setPlan_name(name);
                    tvName.setText(name);
                    break;
                case REQUEST_CODE_CHOOSE_DETAIL:
                    //选择详情
                    String drugName = data.getStringExtra("name");
                    String value = data.getStringExtra("value");
                    chooseDetailTextView.setText(drugName + "," + value + "单位");
                    info.getPlanList().get(chooseDetailPos - 1).setValue(value);
                    info.getPlanList().get(chooseDetailPos - 1).setDrug_name(drugName);
                    break;
                default:
                    break;
            }
        }
    }

    private List<String> options1Items = new ArrayList<>();
    private List<List<String>> options2Items = new ArrayList<>();


    private void initTime(int chooseNum, TextView textView) {
        options1Items.clear();
        options2Items.clear();
        List<String> times = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                times.add("0" + i + "分");
            } else {
                times.add(i + "分");
            }
        }
        if (info.getPlanList().size() == 0) {
            return;
        }
        int start = 0;
        int end = 23;

        if (chooseNum == 1) {
            if (info.getPlanList().size() > chooseNum) {
                String plan_time = info.getPlanList().get(chooseNum).getPlan_time();
                String[] split = plan_time.split(":");
                end = Integer.parseInt(split[0]) - 1;
            }
        } else if (chooseNum == info.getPlanList().size()) {
            end = 23;
            String plan_time = info.getPlanList().get(chooseNum - 2).getPlan_time();
            String[] split = plan_time.split(":");
            start = Integer.parseInt(split[0]) + 1;
        } else {
            start = Integer.parseInt(info.getPlanList().get(chooseNum - 2).getPlan_time().split(":")[0]) + 1;
            end = Integer.parseInt(info.getPlanList().get(chooseNum).getPlan_time().split(":")[0]) - 1;
        }


        for (int i = start; i <= end; i++) {
            if (i < 10) {
                options1Items.add("0" + i + "时");
            } else {
                options1Items.add(i + "时");
            }
            options2Items.add(times);
        }

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            String time = options1Items.get(options1).replaceAll("时", "") + ":" + (options2Items.get(options1).get(options2).replaceAll("分", ""));
            textView.setText(time);
            info.getPlanList().get(chooseNum - 1).setPlan_time(time);
        })
                .setTitleText("")
                .setContentTextSize(16)
                .build();

        pvOptions.setPicker(options1Items, options2Items);
        pvOptions.show();
    }

    private void pop() {
        if (forPopupWindow == null) {
            forPopupWindow = new InjectionNoBindPopupWindow(getPageContext(),

                    back -> {
                        forPopupWindow.dismiss();
                        finish();
                    },
                    next -> {
                        startActivity(new Intent(getPageContext(), InjectionProgramAddDeviceActivity.class));
                    });
        }
        forPopupWindow.showAsDropDown(containerView(), 0, 0, Gravity.CENTER);
    }
}
