package com.vice.bloodpressure.ui.activity.insulin;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.allen.library.utils.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.injection.ViewPagerAdapter;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.injection.InjectionBaseData;
import com.vice.bloodpressure.ui.fragment.insulin.InsulinBaseRateFragment;
import com.vice.bloodpressure.ui.fragment.insulin.InsulinLargeDoseFragment;
import com.vice.bloodpressure.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InsulinPlanDetailsActivity extends XYSoftUIBaseActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_FORPROGRAM = 10;
    private LinearLayout LLLargeDose;
    private LinearLayout LLBasalRate;
    private TextView tvRateNum;
    private CustomViewPager viewPager;
    private List<Fragment> fragments;
    private InjectionBaseData injectionBaseData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String time = getIntent().getStringExtra("time");
        topViewManager().titleTextView().setText(time);
        containerView().addView(initView());
        initListener();
        getData();
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
        viewPager = getViewByID(view, R.id.vp_insulin_details);

        return view;
    }

    private void initValues() {
        fragments = new ArrayList<>();
        InsulinLargeDoseFragment largeDoseFragment = InsulinLargeDoseFragment.newInstance();
        fragments.add(largeDoseFragment);

        InsulinBaseRateFragment baseRateFragment = InsulinBaseRateFragment.newInstance();
        fragments.add(baseRateFragment);

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setCurrentItem(0);//默认选中项
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


    private void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        DataManager.getInjectionBaseInfo(token, (call, response) -> {
            if (response.code == 200) {
                injectionBaseData = (InjectionBaseData) response.object;
                setData();
                initValues();
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }

    private void setData() {

    }


}
