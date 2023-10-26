package com.vice.bloodpressure.ui.fragment.injection;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.injection.InjectionHistoryAdapter;
import com.vice.bloodpressure.base.TabFragmentAdapter;
import com.vice.bloodpressure.base.fragment.XYBaseFragment;
import com.vice.bloodpressure.bean.injection.InjectionHistoryInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 类描述：查看历史
 * 类传参：
 *
 * @author android.yys
 * @date 2021/1/15
 */
public class PatientInfoHistoryFragment extends XYBaseFragment implements TabFragmentAdapter.RefeshFragment {
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private InjectionHistoryAdapter adapter;
    private List<InjectionHistoryInfo> infoList = new ArrayList<>();
    private List<InjectionHistoryInfo> infoListTemp = new ArrayList<>();
    private String userId;
    private int page = 1;

    public static PatientInfoHistoryFragment newInstance(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        PatientInfoHistoryFragment fragment = new PatientInfoHistoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void onCreate() {
        topViewManager().topView().removeAllViews();
        userId = getArguments().getString("userId");
        initView();
        initReFresh();
        getData();
    }

    private void getData() {
        Call<String> requestCall = DataManager.getInjectionHistoryList(userId, page, (call, response) -> {
            if (200 == response.code) {
                if (page == 1) {
                    infoList.clear();
                }
                infoListTemp = (List<InjectionHistoryInfo>) response.object;
                if (infoListTemp.size() < 10) {
                    smartRefreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    smartRefreshLayout.finishLoadMore();
                }
                infoList.addAll(infoListTemp);
                adapter.notifyDataSetChanged();
            } else {
                ToastUtils.showToast("网络连接不可用，请稍后重试！");
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }


    private void initView() {
        View view = View.inflate(getPageContext(), R.layout._fragment_injection_history, null);
        recyclerView = view.findViewById(R.id.rv_injection_history);
        smartRefreshLayout = view.findViewById(R.id.srl_history);
        containerView().addView(view);
        adapter = new InjectionHistoryAdapter(getPageContext(), userId, infoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getPageContext()));
        recyclerView.setAdapter(adapter);
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
}
