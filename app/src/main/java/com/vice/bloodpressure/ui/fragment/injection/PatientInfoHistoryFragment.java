package com.vice.bloodpressure.ui.fragment.injection;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.utils.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.eventbus.EventBusUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.injection.InjectionHistoryAdapter;
import com.vice.bloodpressure.base.TabFragmentAdapter;
import com.vice.bloodpressure.base.fragment.XYBaseFragment;
import com.vice.bloodpressure.bean.injection.InjectionHistoryInfo;
import com.vice.bloodpressure.event.AddProgramEventBus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private int page = 1;

    public static PatientInfoHistoryFragment newInstance() {
        Bundle bundle = new Bundle();
        PatientInfoHistoryFragment fragment = new PatientInfoHistoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void onCreate() {
        topViewManager().topView().removeAllViews();
        initView();
        initReFresh();
        getData();
        EventBusUtils.register(this);
    }

    private void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        Call<String> requestCall = DataManager.getInjectionHistoryList(page,token, (call, response) -> {
            if (200 == response.code) {
                if (page == 1) {
                    infoList.clear();
                }
                infoListTemp = (List<InjectionHistoryInfo>) response.object;
                if (infoListTemp!=null&&infoListTemp.size()>0){
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AddProgramEventBus event) {
        getData();
    }

    private void initView() {
        View view = View.inflate(getPageContext(), R.layout._fragment_injection_history, null);
        recyclerView = view.findViewById(R.id.rv_injection_history);
        smartRefreshLayout = view.findViewById(R.id.srl_history);
        containerView().addView(view);
        adapter = new InjectionHistoryAdapter(getPageContext(),  infoList);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }
}
