package com.vice.bloodpressure.ui.activity.insulin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:选择设备
 */
public class InsulinChooseDeviceActivity extends XYSoftUIBaseActivity implements View.OnClickListener {
    private FrameLayout flAllFirst;
    private CheckBox cbCheckFirst;
    private TextView tvContentFirst;
    private FrameLayout flAllSecond;
    private CheckBox cbCheckSecond;
    private TextView tvContentSecond;

    private TextView tvNext;

    private String isCheck = "1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("选择品牌");
        containerView().addView(initView());
        initListener();
    }

    private void initListener() {
        flAllFirst.setOnClickListener(this);
        flAllSecond.setOnClickListener(this);
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_insulin_choose_device, null);
        flAllFirst = view.findViewById(R.id.fl_insulin_choose_bg_first);
        cbCheckFirst = view.findViewById(R.id.cb_insulin_choose_check_first);
        tvContentFirst = view.findViewById(R.id.tv_insulin_choose_content_first);
        flAllSecond = view.findViewById(R.id.fl_insulin_choose_bg_second);
        cbCheckSecond = view.findViewById(R.id.cb_insulin_choose_check_second);
        tvContentSecond = view.findViewById(R.id.tv_insulin_choose_content_second);
        tvNext = view.findViewById(R.id.tv_insulin_choose_next);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_insulin_choose_bg_first:
                isCheck = "1";
                flAllFirst.setBackground(getResources().getDrawable(R.drawable.shape_green_tran_12_5));
                cbCheckFirst.setVisibility(View.VISIBLE);
                tvContentFirst.setTextColor(getResources().getColor(R.color.main_green));

                flAllSecond.setBackground(getResources().getDrawable(R.drawable.shape_grey_f8_5));
                cbCheckSecond.setVisibility(View.GONE);
                tvContentSecond.setTextColor(getResources().getColor(R.color.black_text));
                break;
            case R.id.fl_insulin_choose_bg_second:
                isCheck = "2";
                flAllFirst.setBackground(getResources().getDrawable(R.drawable.shape_grey_f8_5));
                cbCheckFirst.setVisibility(View.GONE);
                tvContentFirst.setTextColor(getResources().getColor(R.color.black_text));

                flAllSecond.setBackground(getResources().getDrawable(R.drawable.shape_green_tran_12_5));
                cbCheckSecond.setVisibility(View.VISIBLE);
                tvContentSecond.setTextColor(getResources().getColor(R.color.main_green));
                break;
            case R.id.tv_insulin_choose_next:
                startActivity(new Intent(getPageContext(),InsulinAddDeviceActivity.class));
                break;
            default:
                break;
        }
    }
}
