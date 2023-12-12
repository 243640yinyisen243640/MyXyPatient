package com.vice.bloodpressure.ui.activity.insulin;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.utils.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.insulin.InsulinInfusionPlanAdapter;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.insulin.InsulinDeviceInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:输注方案
 */
public class InsulinInfusionPlanListActivity extends XYSoftUIBaseActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private InsulinInfusionPlanAdapter adapter;
    private List<InsulinDeviceInfo> infoList = new ArrayList<>();
    private List<InsulinDeviceInfo> infoListTemp = new ArrayList<>();
    private int page = 1;

    private LinearLayout llLargeDose;
    private TextView tvLargeDose;
    private LinearLayout llBasalRate;
    private TextView tvBasalRate;
    private TextView tvLargeDoseNum;
    private TextView tvBasalRateNum;
    private LinearLayout llLast;
    private TextView tvNoData;
    /**
     * 1大剂量 2基础率
     */
    private String type = "1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("输注方案");
        containerView().addView(initView());
        initListner();
        initReFresh();
        getData();
    }

    private void initReFresh() {
        //下拉刷新
        smartRefreshLayout.setEnableRefresh(false);
        //上拉加载
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                getData();
            }
        });
    }

    private void initListner() {
        llLargeDose.setOnClickListener(this);
        llBasalRate.setOnClickListener(this);
    }


    private void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        Call<String> requestCall = DataManager.getInjectionHistoryList(page, token, (call, response) -> {
            if (200 == response.code) {
                if (page == 1) {
                    infoList.clear();
                }
                infoListTemp = (List<InsulinDeviceInfo>) response.object;
                if (infoListTemp != null && infoListTemp.size() > 0) {
                    if (infoListTemp.size() < 10) {
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    } else {
                        smartRefreshLayout.finishLoadMore();
                    }
                    infoList.addAll(infoListTemp);
                    adapter.notifyDataSetChanged();
                }

            } else {
                ToastUtils.showToast("网络连接不可用，请稍后重试！");
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }


    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_insulin_infusion_plan, null);
        llLargeDose = view.findViewById(R.id.ll_infusion_large_dose);
        tvLargeDoseNum = view.findViewById(R.id.tv_infusion_large_dose_num);
        llBasalRate = view.findViewById(R.id.ll_infusion_basal_rate);
        tvBasalRateNum = view.findViewById(R.id.tv_infusion_base_rate_num);

        tvLargeDose = view.findViewById(R.id.tv_infusion_large_dose);
        tvBasalRate = view.findViewById(R.id.tv_infusion_basal_rate);

        smartRefreshLayout = view.findViewById(R.id.srl_infusion_plan);
        recyclerView = view.findViewById(R.id.rv_infusion_plan);
        llLast = view.findViewById(R.id.ll_insulin_infusion_plan_last);
        tvNoData = view.findViewById(R.id.tv_insulin_plan_no_data);
        adapter = new InsulinInfusionPlanAdapter(getPageContext(), infoList, type);
        recyclerView.setLayoutManager(new LinearLayoutManager(getPageContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_infusion_large_dose:
                setBg(tvLargeDose, tvBasalRate);
                break;
            case R.id.ll_infusion_basal_rate:
                setBg(tvBasalRate, tvLargeDose);
                break;
            default:
                break;
        }
    }


    private void setBg(TextView tvCheck, TextView tvUncheck1) {
        tvCheck.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.shape_green_30_3_2);
        tvCheck.setTextSize(18);
        tvCheck.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvUncheck1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tvUncheck1.setTextSize(16);
        tvUncheck1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

}
