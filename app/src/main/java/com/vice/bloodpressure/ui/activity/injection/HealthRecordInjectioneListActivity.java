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
import com.google.gson.Gson;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.eventbus.EventBusUtils;
import com.quinovaresdk.bletransfer.BleTransfer;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.injection.ViewPagerAdapter;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.injection.InjectionBaseData;
import com.vice.bloodpressure.event.BlueConnectEvent;
import com.vice.bloodpressure.event.BlueHistoryDataEvent;
import com.vice.bloodpressure.ui.fragment.injection.PatientInfoInjectionFragment;
import com.vice.bloodpressure.ui.fragment.injection.PatientInfoProgrammeFragment;
import com.vice.bloodpressure.utils.BlueUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class HealthRecordInjectioneListActivity extends XYSoftUIBaseActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_FORPROGRAM = 10;
    private TextView tvNum;
    private TextView tvState;
    private TextView tvRank;
    private TextView tvCompany;
    private LinearLayout llPlan;
    private LinearLayout llProgramme;
    private TextView tvPlanNum;
    private TextView tvTimeYear;
    private TextView tvTimeMonth;
    private TextView tvIsConnect;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    //    private String userId;
    private InjectionBaseData injectionBaseData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
        //获取蓝牙历史数据
        BleTransfer.getInstance().getHistoryInjection();
        topViewManager().titleTextView().setText("注射数据");
        containerView().addView(initView());
        topViewManager().moreTextView().setText("新增数据");
        topViewManager().moreTextView().setOnClickListener(v -> {
            Intent intent = new Intent(getPageContext(), InjectionDataAddActivity.class);
            intent.putExtra("isAdd", true);
            startActivity(intent);
        });
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
        tvIsConnect = view.findViewById(R.id.tv_blue_is_connect);

        if (BlueUtils.isBind() && BlueUtils.isConnect) {
            //已连接
            setTextIsConnect(true);
        } else {
            //未连接
            setTextIsConnect(false);
        }

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
                    startActivityForResult(intent, REQUEST_CODE_FORPROGRAM);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FORPROGRAM:
                    getData();
                    break;
                default:
                    break;
            }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BlueHistoryDataEvent event) {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        switch (event.getType()) {
            case 1:
                if (event.getInsulis() != null && event.getInsulis().size() != 0) {
                    String insulis = new Gson().toJson(event.getInsulis());
                    DataManager.addInsulins(token, insulis, (call, response) -> {

                    }, (call, t) -> {

                    });
                }
                break;
            case 2:
                BlueHistoryDataEvent.insulis insuli = event.getInsuli();
                if (insuli != null) {
                    //弹出弹窗
                    String value = insuli.getValue();
                    String datetime = insuli.getDatetime();
                    DataManager.addInsulin("", "", value, datetime, token, (call, response) -> {
                        if (200 == response.code) {

                        } else {

                        }
                        ToastUtils.showToast(response.msg);
                    }, (call, t) -> {
                        ToastUtils.showToast("网络连接不可用，请稍后重试！");
                    });
                }

                break;
            default:
                break;
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BlueConnectEvent event) {
        if (BlueUtils.isBind() && event.isConnect()) {
            setTextIsConnect(true);
        } else {
            setTextIsConnect(false);
        }
    }

    private void setTextIsConnect(boolean isConnect) {
        tvIsConnect.setText(isConnect ? "已连接" : "未连接");

        tvIsConnect.setOnClickListener(v -> {
            if (!isConnect) {
                BleTransfer.getInstance().connect(BlueUtils.getBlueMac());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }
}
