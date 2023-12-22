package com.vice.bloodpressure.ui.activity.insulin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.allen.library.utils.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.insulin.PlanAllInfo;

import retrofit2.Call;

/**
 * 类描述：大剂量
 * 类传参：
 *
 * @author android.yys
 * @date 2021/1/15
 */
public class InsulinDetailsLargeDoseActivity extends XYSoftUIBaseActivity {
    private TextView tvBreakFast;
    private TextView tvLunnch;
    private TextView tvDinner;
    private TextView tvMore;
    private TextView tvSure;

    private String plan_id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plan_id = getIntent().getStringExtra("plan_id");
        String time = getIntent().getStringExtra("time");
        topViewManager().titleTextView().setText(time);
        initView();
        getData();
    }

    private void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        Call<String> requestCall = DataManager.getusereqplandetail(token, plan_id, (call, response) -> {
            if (200 == response.code) {
                PlanAllInfo allInfo = (PlanAllInfo) response.object;
                if ("1".equals(allInfo.getConfirm())) {
                    tvSure.setClickable(false);
                    tvSure.setBackground(ContextCompat.getDrawable(getPageContext(), R.drawable.shape_grey_d0_90));
                    tvSure.setText("已接受此方案");
                } else {
                    tvSure.setClickable(true);
                    tvSure.setBackground(ContextCompat.getDrawable(getPageContext(), R.drawable.shape_bg_main_green_90));
                    tvSure.setText("接受此方案");
                }
                tvBreakFast.setText(allInfo.getData().getBig1());
                tvLunnch.setText(allInfo.getData().getBig2());
                tvDinner.setText(allInfo.getData().getBig3());
                tvMore.setText(allInfo.getData().getBig4());
            } else {
                ToastUtils.showToast(response.msg);
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }


    private void initView() {
        View view = View.inflate(getPageContext(), R.layout.fragment_injection_large_dose, null);
        tvBreakFast = view.findViewById(R.id.tv_insulin_large_dose_breakfast);
        tvLunnch = view.findViewById(R.id.tv_insulin_large_dose_lunnch);
        tvDinner = view.findViewById(R.id.tv_insulin_large_dose_dinner);
        tvMore = view.findViewById(R.id.tv_insulin_large_dose_more);
        tvSure = view.findViewById(R.id.tv_insulin_large_dose_sure);
        tvSure.setOnClickListener(v -> {
            Log.i("yys", "click==");
            planSure();
        });
        containerView().addView(view);

    }

    private void planSure() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        Call<String> requestCall = DataManager.getusereqplanconfirm(token, plan_id, (call, response) -> {
            if (200 == response.code) {
                tvSure.setClickable(false);
                tvSure.setBackground(ContextCompat.getDrawable(getPageContext(), R.drawable.shape_grey_d0_90));
                tvSure.setText("已接受此方案");
            } else {
                ToastUtils.showToast(response.msg);
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }
}
