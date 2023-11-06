package com.vice.bloodpressure.ui.fragment.injection;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.utils.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.eventbus.EventBusUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.injection.InjectionAdapter;
import com.vice.bloodpressure.base.TabFragmentAdapter;
import com.vice.bloodpressure.base.fragment.XYBaseFragment;
import com.vice.bloodpressure.bean.injection.InjectionDataListInfo;
import com.vice.bloodpressure.constant.DataFormatManager;
import com.vice.bloodpressure.event.DataAddEvent;
import com.vice.bloodpressure.utils.DataUtils;
import com.vice.bloodpressure.utils.PickerUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
//    private String userId;
    private String beginTime;
    private InjectionAdapter adapter;
    private List<InjectionDataListInfo> listInfos = new ArrayList<>();

    public static PatientInfoInjectionFragment newInstance() {
        Bundle bundle = new Bundle();
//        bundle.putString("userid", userid);
        PatientInfoInjectionFragment fragment = new PatientInfoInjectionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void onCreate() {
        topViewManager().topView().removeAllViews();
//        userId = getArguments().getString("userid");
        initView();
        beginTime = DataUtils.convertDateToString(new Date(System.currentTimeMillis()), "YYYY-MM");
        adapter = new InjectionAdapter(getPageContext(), listInfos);
        rvInjection.setLayoutManager(new LinearLayoutManager(getPageContext()));
        rvInjection.setAdapter(adapter);
        EventBusUtils.register(this);
        getData();
    }

    private void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        Call<String> requestCall = DataManager.getInjectionList(beginTime,token, (call, response) -> {
            if (200 == response.code) {
                listInfos.clear();
                listInfos.addAll((List<InjectionDataListInfo>) response.object);
                adapter.notifyDataSetChanged();
            } else {
                ToastUtils.showToast(response.msg);
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DataAddEvent event) {
        getData();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    private void initView() {
        View view = View.inflate(getPageContext(), R.layout._fragment_injection_list, null);
        tvChange = view.findViewById(R.id.tv_injection_change);
        tvChange.setOnClickListener(v -> {
            //选择时间
            PickerUtils.showTimeWindow(getPageContext(), new boolean[]{true, true, false, false, false, false}, DataFormatManager.TIME_FORMAT_Y_M, new PickerUtils.TimePickerCallBack() {
                @Override
                public void execEvent(String content) {
                    beginTime = content;
                    getData();
                }
            });

        });
        rvInjection = view.findViewById(R.id.rv_injection);
        containerView().addView(view);

    }


}
