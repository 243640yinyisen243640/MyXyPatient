package com.vice.bloodpressure.ui.activity.insulin;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.allen.library.utils.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.insulin.InsulinBaseModeAdapter;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.insulin.InsulinDeviceAllInfo;
import com.vice.bloodpressure.utils.StatusBarUtils;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:基础模式
 */
public class InsulinBaseModeListActivity extends XYSoftUIBaseActivity implements View.OnClickListener {

    /**
     * 头部
     */
    private ImageView IvBack;
    private TextView tvModeFirst;
    private TextView tvModeSecond;


    /**
     * 布局
     */

    private TextView tvDayAll;
    private ImageView ivRefresh;
    private ListView lvDataInfo;
    private TextView tvLast;
    private TextView tvNoData;
    //1：模式1  2：模式2
    private String type = "1";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().topView().removeAllViews();
        StatusBarUtils.statusBarColor(InsulinBaseModeListActivity.this, getResources().getColor(R.color.main_green));
        containerView().addView(initView());
        initListener();
        getData();
    }

    private void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        DataManager.getuserbase(loginBean.getToken(), (call, response) -> {
            if (response.code == 200) {
                InsulinDeviceAllInfo allInfo = (InsulinDeviceAllInfo) response.object;
                tvDayAll.setText("当前累计注射日基础总量：" + allInfo.getNow_value());
                if ("1".equals(type)) {
                    if (allInfo.getType1() != null && allInfo.getType1().size() > 0) {
                        lvDataInfo.setVisibility(View.VISIBLE);
                        tvLast.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                        InsulinBaseModeAdapter deviceListAdapter = new InsulinBaseModeAdapter(getPageContext(), allInfo.getType1());
                        lvDataInfo.setAdapter(deviceListAdapter);
                    } else {
                        lvDataInfo.setVisibility(View.GONE);
                        tvLast.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (allInfo.getType2() != null && allInfo.getType2().size() > 0) {
                        lvDataInfo.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                        InsulinBaseModeAdapter deviceListAdapter = new InsulinBaseModeAdapter(getPageContext(), allInfo.getType2());
                        lvDataInfo.setAdapter(deviceListAdapter);
                    } else {
                        lvDataInfo.setVisibility(View.GONE);
                        tvLast.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    }

                }

            } else if (30002 == response.code) {
                lvDataInfo.setVisibility(View.GONE);
                tvLast.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }

    private void initListener() {
        IvBack.setOnClickListener(this);
        tvModeFirst.setOnClickListener(this);
        tvModeSecond.setOnClickListener(this);
        ivRefresh.setOnClickListener(this);
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_insulin_base_mode, null);
        IvBack = view.findViewById(R.id.iv_base_mode_back);
        tvModeFirst = view.findViewById(R.id.tv_base_mode_first);
        tvModeSecond = view.findViewById(R.id.tv_base_mode_second);

        tvDayAll = view.findViewById(R.id.tv_base_mode_day_all);
        ivRefresh = view.findViewById(R.id.iv_base_mode_refresh);
        lvDataInfo = view.findViewById(R.id.lv_base_mode_list);
        tvLast = view.findViewById(R.id.tv_insulin_base_mode_last);
        tvNoData = view.findViewById(R.id.tv_insulin_base_mode_no_data);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_base_mode_back:
                finish();
                break;
            case R.id.tv_base_mode_first:
                setBg(tvModeFirst, tvModeSecond);
                break;
            case R.id.tv_base_mode_second:
                setBg(tvModeSecond, tvModeFirst);
                break;
            case R.id.iv_base_mode_refresh:
                getData();
                break;
            default:
                break;
        }
    }

    private void setBg(TextView tvCheck, TextView tvUncheck) {
        tvCheck.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.shape_white_30_2_1);
        tvCheck.setTextSize(18);
        tvCheck.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvUncheck.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tvUncheck.setTextSize(16);
        tvUncheck.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));


    }
}
