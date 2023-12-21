package com.vice.bloodpressure.ui.activity.insulin;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.utils.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.insulin.BaseRateDetailsAdapter;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.insulin.PlanAllBaseInfo;
import com.vice.bloodpressure.bean.insulin.PlanInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 类描述：基础率
 * 类传参：
 *
 * @author android.yys
 * @date 2021/1/15
 */
public class InsulinDetailsBaseRateActivity extends XYSoftUIBaseActivity {
    private RecyclerView LvList;
    private TextView tvSure;
    private BaseRateDetailsAdapter adapter;
    private List<PlanInfo> listInfos = new ArrayList<>();

    private String plan_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String time = getIntent().getStringExtra("time");
        topViewManager().titleTextView().setText(time);
        plan_id = getIntent().getStringExtra("plan_id");

        initView();
        adapter = new BaseRateDetailsAdapter(getPageContext(), listInfos);
        LvList.setLayoutManager(new LinearLayoutManager(getPageContext()));
        LvList.setAdapter(adapter);
        getData();
    }

    private void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        Call<String> requestCall = DataManager.getusereqplandetailBase(token, plan_id, (call, response) -> {
            if (200 == response.code) {
                PlanAllBaseInfo allBaseInfo = (PlanAllBaseInfo) response.object;
                if ("1".equals(allBaseInfo.getConfirm())) {
                    tvSure.setClickable(false);
                    tvSure.setBackground(ContextCompat.getDrawable(getPageContext(), R.drawable.shape_grey_d0_90));
                    tvSure.setText("已接受此方案");
                } else {
                    tvSure.setClickable(true);
                    tvSure.setBackground(ContextCompat.getDrawable(getPageContext(), R.drawable.shape_bg_main_green_90));
                    tvSure.setText("接受此方案");
                }
                listInfos.clear();
                listInfos.addAll(allBaseInfo.getData());
                adapter.notifyDataSetChanged();
            } else {
                ToastUtils.showToast(response.msg);
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }


    private void initView() {
        View view = View.inflate(getPageContext(), R.layout.fragment_insulin_infusion_base_rate, null);
        LvList = view.findViewById(R.id.rv_base_rate_detail);
        tvSure = view.findViewById(R.id.tv_insulin_base_rate_sure);
        tvSure.setOnClickListener(v -> {
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
