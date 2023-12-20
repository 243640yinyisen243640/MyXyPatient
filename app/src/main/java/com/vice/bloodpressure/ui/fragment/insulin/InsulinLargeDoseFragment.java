package com.vice.bloodpressure.ui.fragment.insulin;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.allen.library.utils.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.TabFragmentAdapter;
import com.vice.bloodpressure.base.fragment.XYBaseFragment;

import retrofit2.Call;

/**
 * 类描述：大剂量
 * 类传参：
 *
 * @author android.yys
 * @date 2021/1/15
 */
public class InsulinLargeDoseFragment extends XYBaseFragment implements TabFragmentAdapter.RefeshFragment {
    private TextView tvBreakFast;
    private TextView tvLunnch;
    private TextView tvDinner;
    private TextView tvMore;
    private TextView tvSure;

    private String plan_id;

    public static InsulinLargeDoseFragment newInstance(String plan_id) {
        Bundle bundle = new Bundle();
        bundle.putString("plan_id", plan_id);
        InsulinLargeDoseFragment fragment = new InsulinLargeDoseFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void onCreate() {
        topViewManager().topView().removeAllViews();

        initView();
        getData();
    }

    private void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        plan_id = getArguments().getString("plan_id");
        Call<String> requestCall = DataManager.getusereqplandetail(token, plan_id, (call, response) -> {
            if (200 == response.code) {

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
        containerView().addView(view);

    }


}
