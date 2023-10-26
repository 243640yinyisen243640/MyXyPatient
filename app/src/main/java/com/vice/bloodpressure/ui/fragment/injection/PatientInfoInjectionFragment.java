package com.vice.bloodpressure.ui.fragment.injection;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.utils.ToastUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.injection.InjectionAdapter;
import com.vice.bloodpressure.base.TabFragmentAdapter;
import com.vice.bloodpressure.base.fragment.XYBaseFragment;
import com.vice.bloodpressure.bean.injection.InjectionDataListInfo;
import com.vice.bloodpressure.constant.DataFormatManager;
import com.vice.bloodpressure.utils.DataUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

/**
 * 类描述：注射数据
 * 类传参：
 *
 * @author android.yys
 * @date 2021/1/15
 */
public class PatientInfoInjectionFragment extends XYBaseFragment implements TabFragmentAdapter.RefeshFragment {
    private TextView tvChange;
    private RecyclerView rvInjection;
    private String userId;
    private String beginTime;
    private InjectionAdapter adapter;
    private List<InjectionDataListInfo> listInfos = new ArrayList<>();

    public static PatientInfoInjectionFragment newInstance(String userid) {
        Bundle bundle = new Bundle();
        bundle.putString("userid", userid);
        PatientInfoInjectionFragment fragment = new PatientInfoInjectionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void onCreate() {
        topViewManager().topView().removeAllViews();
        userId = getArguments().getString("userid");
        initView();
        beginTime = DataUtils.convertDateToString(new Date(System.currentTimeMillis()), "YYYY-MM");
        adapter = new InjectionAdapter(getPageContext(), listInfos);
        rvInjection.setLayoutManager(new LinearLayoutManager(getPageContext()));
        rvInjection.setAdapter(adapter);
        getData();
    }

    private void getData() {
        Call<String> requestCall = DataManager.getInjectionList(userId, beginTime, (call, response) -> {
            if (200 == response.code) {
                listInfos.clear();
                listInfos.addAll((List<InjectionDataListInfo>) response.object);
                adapter.notifyDataSetChanged();
            } else {
                ToastUtils.showToast("网络连接不可用，请稍后重试！");
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }


    private void initView() {
        View view = View.inflate(getPageContext(), R.layout._fragment_injection_list, null);
        tvChange = view.findViewById(R.id.tv_injection_change);
        tvChange.setOnClickListener(v -> {
            //选择时间
            beginTime = DataUtils.currentDateString(DataFormatManager.TIME_FORMAT_Y_M);
            getData();
        });
        rvInjection = view.findViewById(R.id.rv_injection);
        containerView().addView(view);

    }


}
