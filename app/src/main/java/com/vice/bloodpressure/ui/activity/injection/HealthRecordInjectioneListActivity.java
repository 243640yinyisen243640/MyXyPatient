package com.vice.bloodpressure.ui.activity.injection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.allen.library.utils.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.injection.ViewPagerAdapter;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.injection.InjectionBaseData;
import com.vice.bloodpressure.ui.fragment.injection.PatientInfoInjectionFragment;
import com.vice.bloodpressure.ui.fragment.injection.PatientInfoProgrammeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class HealthRecordInjectioneListActivity extends XYSoftUIBaseActivity implements View.OnClickListener {
    private TextView tvNum;
    private TextView tvState;
    private TextView tvRank;
    private TextView tvCompany;
    private LinearLayout llPlan;
    private LinearLayout llProgramme;
    private TextView tvPlanNum;
    private TextView tvTimeYear;
    private TextView tvTimeMonth;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    //    private String userId;
    private InjectionBaseData injectionBaseData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("注射数据");
        containerView().addView(initView());
        topViewManager().moreTextView().setText("新增数据");
        topViewManager().moreTextView().setOnClickListener(v -> {
            Intent intent = new Intent(getPageContext(), InjectionDataAddActivity.class);
            intent.putExtra("isAdd", true);
            startActivity(intent);
        });
        //        userId = getIntent().getStringExtra("userid");
        //        userId = "129199";
        initListener();
        getData();
    }

    private void initListener() {
        llPlan.setOnClickListener(this);
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout._activity_zhusheshujv, null);
        tvNum = view.findViewById(R.id.tv_injection_num);
        tvState = view.findViewById(R.id.tv_injection_state);
        tvRank = view.findViewById(R.id.tv_injection_rank);
        tvCompany = view.findViewById(R.id.tv_injection_company);
        llPlan = view.findViewById(R.id.ll_injection_plan);
        llProgramme = view.findViewById(R.id.ll_injection_programme);
        tvPlanNum = view.findViewById(R.id.tv_injection_plan_num);
        tvTimeYear = view.findViewById(R.id.tv_injection_time_year);
        tvTimeMonth = view.findViewById(R.id.tv_injection_time_month);

        viewPager = getViewByID(view, R.id.vp_injection);
        return view;
    }

    private void initValues() {
        fragments = new ArrayList<>();
        PatientInfoInjectionFragment injectionFragment = PatientInfoInjectionFragment.newInstance();
        fragments.add(injectionFragment);

        PatientInfoProgrammeFragment programmeFragment = PatientInfoProgrammeFragment.newInstance();
        fragments.add(programmeFragment);

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setCurrentItem(0);//默认选中项
        viewPager.setOffscreenPageLimit(fragments.size());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_injection_plan:
                llPlan.setBackground(getResources().getDrawable(R.drawable._2));
                llProgramme.setBackground(getResources().getDrawable(R.color.transparent));
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_injection_programme:
                if ("暂无".equals(injectionBaseData.getAction_time())) {
                    Intent intent = new Intent(getPageContext(), InjectionProgramAddActivity.class);
                    startActivity(intent);
                } else {
                    llPlan.setBackground(getResources().getDrawable(R.color.transparent));
                    llProgramme.setBackground(getResources().getDrawable(R.drawable._2));
                    viewPager.setCurrentItem(1);
                }

                break;
            default:
                break;
        }
    }

    public String getTime() {
        return injectionBaseData.getAction_year() + "/" + injectionBaseData.getAction_time();
    }

    private void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        DataManager.getInjectionBaseInfo(token, (call, response) -> {
            if (response.code == 200) {
                injectionBaseData = (InjectionBaseData) response.object;
                setData();
                initValues();
                llProgramme.setOnClickListener(this);
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }

    private void setData() {
        tvNum.setText(injectionBaseData.getValue() + "");
        if (injectionBaseData.getIsshot() == 0) {
            tvState.setText("待注射");
        } else {
            tvState.setText("已注射");
        }
        tvRank.setText("第" + injectionBaseData.getTimes() + "针");
        tvCompany.setText(injectionBaseData.getDrug_name());
        tvPlanNum.setText(injectionBaseData.getIsshot_num() + "/" + injectionBaseData.getAll_times());
        tvTimeYear.setText(injectionBaseData.getAction_year());
        tvTimeMonth.setText(injectionBaseData.getAction_time());
    }
}
