package com.vice.bloodpressure.ui.fragment.injection;

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
import com.vice.bloodpressure.adapter.injection.InjectionCurrentAdapter;
import com.vice.bloodpressure.base.TabFragmentAdapter;
import com.vice.bloodpressure.base.fragment.XYBaseFragment;
import com.vice.bloodpressure.bean.injection.InjectionDataDetail;
import com.vice.bloodpressure.ui.activity.injection.HealthRecordInjectioneListActivity;

import retrofit2.Call;

/**
 * 类描述：治疗方案
 * 类传参：
 *
 * @author android.yys
 * @date 2021/1/15
 */
public class PatientInfoCurrentFragment extends XYBaseFragment implements TabFragmentAdapter.RefeshFragment {
    private TextView tvName;
    private TextView tvTime;
    private RecyclerView recyclerView;
    private InjectionCurrentAdapter adapter;
    private InjectionDataDetail dataDetail;
    private String action_time;

    public static PatientInfoCurrentFragment newInstance() {
        Bundle bundle = new Bundle();
        //        bundle.putString("userId", userId);
        PatientInfoCurrentFragment fragment = new PatientInfoCurrentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onCreate() {
        topViewManager().topView().removeAllViews();
        action_time = ((HealthRecordInjectioneListActivity) getActivity()).getTime();
        containerView().addView(initView());
        getData();
    }

    public void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        Call<String> requestCall = DataManager.getInjectionDetail(action_time, "1", token, (call, response) -> {
            if (200 == response.code) {
                dataDetail = (InjectionDataDetail) response.object;
                setData();
            } else {
                ToastUtils.showToast("网络连接不可用，请稍后重试！");
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }

    private void setData() {
        if (!action_time.equals("/暂无")){
            tvName.setText(dataDetail.getPlan_name());
            tvTime.setText(dataDetail.getAction_time() + "执行");
            recyclerView.setLayoutManager(new LinearLayoutManager(getPageContext()));
            adapter = new InjectionCurrentAdapter(getPageContext(), dataDetail.getDetail());
            recyclerView.setAdapter(adapter);
        }

    }


    private View initView() {
        View view = View.inflate(getPageContext(), R.layout._fragment_injection_currcnt, null);
        tvName = view.findViewById(R.id.tv_current_name);
        tvTime = view.findViewById(R.id.tv_current_time);
        recyclerView = view.findViewById(R.id.rv_current);
        return view;
    }


}
