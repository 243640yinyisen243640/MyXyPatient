package com.vice.bloodpressure.ui.activity.insulin;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.injection.ViewPagerAdapter;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.injection.InjectionBaseData;
import com.vice.bloodpressure.bean.insulin.PlanInfo;
import com.vice.bloodpressure.ui.fragment.insulin.InsulinBaseRateFragment;
import com.vice.bloodpressure.ui.fragment.insulin.InsulinLargeDoseFragment;
import com.vice.bloodpressure.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:方案详情
 */
public class InsulinPlanDetailsActivity extends XYSoftUIBaseActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_FORPROGRAM = 10;
    private LinearLayout LLLargeDose;
    private LinearLayout LLBasalRate;
    private TextView tvRateNum;
    private TextView tvBigNum;
    private CustomViewPager viewPager;
    private List<Fragment> fragments;
    private InjectionBaseData injectionBaseData;
    /**
     * 1大剂量 2基础率
     */
    private String type;
    private String plan_id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String time = getIntent().getStringExtra("time");
        type = getIntent().getStringExtra("type");
        plan_id = getIntent().getStringExtra("plan_id");
        topViewManager().titleTextView().setText(time);
        containerView().addView(initView());
        initListener();
        initValues();
        getUnReadNum();
    }

    private void initListener() {
        LLLargeDose.setOnClickListener(this);
        LLBasalRate.setOnClickListener(this);
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_insulin_plan_details, null);
        LLLargeDose = view.findViewById(R.id.ll_details_large_dose);
        LLBasalRate = view.findViewById(R.id.ll_details_basal_rate);
        tvRateNum = view.findViewById(R.id.tv_details_base_rate_num);
        tvBigNum = view.findViewById(R.id.tv_details_big_dose_num);
        viewPager = getViewByID(view, R.id.vp_insulin_details);
        return view;
    }

    private void initValues() {
        fragments = new ArrayList<>();
        InsulinLargeDoseFragment largeDoseFragment = InsulinLargeDoseFragment.newInstance(plan_id);
        fragments.add(largeDoseFragment);

        InsulinBaseRateFragment baseRateFragment = InsulinBaseRateFragment.newInstance(plan_id);
        fragments.add(baseRateFragment);

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setCurrentItem(Integer.parseInt(type) - 1);//默认选中项
        viewPager.setOffscreenPageLimit(fragments.size());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_details_large_dose:
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_details_basal_rate:
                viewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }


    /**
     * 获取方案未读数
     */
    private void getUnReadNum() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        DataManager.getusereqplanunread(loginBean.getToken(), (call, response) -> {
            if (response.code == 200) {
                PlanInfo planInfo = (PlanInfo) response.object;

                int big = Integer.parseInt(planInfo.getBig());
                int base_rate = Integer.parseInt(planInfo.getBase_rate());
                if (big > 0) {
                    tvBigNum.setVisibility(View.VISIBLE);
                    tvBigNum.setText(String.valueOf(big));
                }
                if (base_rate > 0) {
                    tvRateNum.setVisibility(View.VISIBLE);
                    tvRateNum.setText(String.valueOf(base_rate));
                }
            }
        }, (call, t) -> {
            //            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }


}
