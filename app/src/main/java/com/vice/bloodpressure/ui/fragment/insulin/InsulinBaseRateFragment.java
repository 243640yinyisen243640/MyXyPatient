package com.vice.bloodpressure.ui.fragment.insulin;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.utils.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.insulin.BaseRateDetailsAdapter;
import com.vice.bloodpressure.base.TabFragmentAdapter;
import com.vice.bloodpressure.base.fragment.XYBaseFragment;
import com.vice.bloodpressure.bean.insulin.InsulinDeviceInfo;

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
public class InsulinBaseRateFragment extends XYBaseFragment implements TabFragmentAdapter.RefeshFragment {
    private TextView tvStarTime;
    private TextView tvEndTime;
    private TextView tvRate;
    private RecyclerView LvList;
    private TextView tvSure;
    private BaseRateDetailsAdapter adapter;
    private List<InsulinDeviceInfo> listInfos = new ArrayList<>();

    public static InsulinBaseRateFragment newInstance(String plan_id) {
        Bundle bundle = new Bundle();
        bundle.putString("plan_id", plan_id);
        InsulinBaseRateFragment fragment = new InsulinBaseRateFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void onCreate() {
        topViewManager().topView().removeAllViews();
        initView();
        adapter = new BaseRateDetailsAdapter(getPageContext(), listInfos);
        LvList.setLayoutManager(new LinearLayoutManager(getPageContext()));
        LvList.setAdapter(adapter);
        getData();
    }

    private void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        Call<String> requestCall = DataManager.getusereqplandetailBase("", token, (call, response) -> {
            if (200 == response.code) {
                listInfos.clear();
                listInfos.addAll((List<InsulinDeviceInfo>) response.object);
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
        tvStarTime = view.findViewById(R.id.tv_infusion_base_rate_star_time);
        tvEndTime = view.findViewById(R.id.tv_infusion_base_rate_end_time);
        tvRate = view.findViewById(R.id.tv_infusion_base_rate_rate);
        LvList = view.findViewById(R.id.rv_base_rate_detail);
        tvSure = view.findViewById(R.id.tv_insulin_base_rate_sure);
        containerView().addView(view);

    }


}
