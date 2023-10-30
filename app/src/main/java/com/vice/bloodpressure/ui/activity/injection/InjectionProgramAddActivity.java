package com.vice.bloodpressure.ui.activity.injection;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.AddProgramInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:方案添加
 */
public class InjectionProgramAddActivity extends XYSoftUIBaseActivity {
    private AddProgramInfo info;
    private static final int REQUEST_CODE_PROGRAM_NAME = 10;
    private static final int REQUEST_CODE_CHOOSE_DETAIL = 11;
    private int chooseNum;
    private int chooseTimePos;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        containerView().addView(initView());
        initData();
    }

    private void initData() {
        List<AddProgramInfo.plan> planList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            planList.add(new AddProgramInfo.plan("", "", ""));
        }
        info = new AddProgramInfo("", "", planList);
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout._activity_program_add, null);
        tvIsConnect = view.findViewById(R.id.tv_program_is_connect);
        tvIsConnect.setOnClickListener(v -> {
            //去链接蓝牙设备  eventBus接收连接结果
            startActivity(new Intent(getPageContext(), InjectionProgramAddDeviceActivity.class));
        });
        tvName = view.findViewById(R.id.tv_program_name);
        tvName.setOnClickListener(v -> {
            //编辑方案名称
            startActivityForResult(new Intent(getPageContext(), InjectionProgramAddNameActivity.class), REQUEST_CODE_PROGRAM_NAME);
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
        tvConfirm = view.findViewById(R.id.tv_program_confirm);
        tvConfirm.setOnClickListener(v -> {
            //提交
        });

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
        startActivityForResult(new Intent(getPageContext(), InjectionProgramAddNumActivity.class), REQUEST_CODE_CHOOSE_DETAIL);
    }

    private void chooseTime(int chooseTimePos, TextView textView) {

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
                    info.getPlanList().get(chooseDetailPos).setValue(value);
                    info.getPlanList().get(chooseDetailPos).setDrug_name(drugName);
                    break;
                default:
                    break;
            }
        }
    }
}
