package com.vice.bloodpressure.ui.fragment.injection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.utils.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.eventbus.EventBusUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.injection.InjectionCurrentAdapter;
import com.vice.bloodpressure.base.TabFragmentAdapter;
import com.vice.bloodpressure.base.fragment.XYBaseFragment;
import com.vice.bloodpressure.bean.AddProgramInfo;
import com.vice.bloodpressure.bean.injection.InjectionDataDetail;
import com.vice.bloodpressure.event.AddProgramEventBus;
import com.vice.bloodpressure.ui.activity.injection.HealthRecordInjectioneListActivity;
import com.vice.bloodpressure.ui.activity.injection.InjectionProgramAddActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static android.app.Activity.RESULT_OK;

/**
 * 类描述：治疗方案
 * 类传参：
 *
 * @author android.yys
 * @date 2021/1/15
 */
public class PatientInfoCurrentFragment extends XYBaseFragment implements TabFragmentAdapter.RefeshFragment {
    private static final int REQUEST_CODE_FORPROGRAM = 10;
    private TextView tvName;
    private TextView tvTime;
    private TextView tvEdit;
    private RecyclerView recyclerView;
    private InjectionCurrentAdapter adapter;
    private InjectionDataDetail dataDetail;
    private String action_time;

    public static PatientInfoCurrentFragment newInstance() {
        Bundle bundle = new Bundle();
        PatientInfoCurrentFragment fragment = new PatientInfoCurrentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onCreate() {
        topViewManager().topView().removeAllViews();
        action_time = ((HealthRecordInjectioneListActivity) getActivity()).getTime();
        containerView().addView(initView());
        EventBusUtils.register(this);
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
                ToastUtils.showToast(response.msg);
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }

    private void setData() {
        if (!action_time.equals("/暂无")) {
            tvName.setText(dataDetail.getPlan_name());
            tvTime.setText(dataDetail.getAction_time() + "执行");
            tvEdit.setVisibility(View.VISIBLE);
            tvEdit.setOnClickListener(v -> {
                Intent intent = new Intent(getPageContext(), InjectionProgramAddActivity.class);
                intent.putExtra("isAdd", false);
                List<AddProgramInfo.plan> plans = new ArrayList<>();
                for (int i = 0; i < dataDetail.getDetail().size(); i++) {
                    plans.add(new AddProgramInfo.plan(
                            dataDetail.getDetail().get(i).getPlan_time(),
                            dataDetail.getDetail().get(i).getDrug_name(),
                            dataDetail.getDetail().get(i).getValue() + ""));
                }
                AddProgramInfo info = new AddProgramInfo(dataDetail.getPlan_name(), plans);
                info.setPlanList(plans);
                intent.putExtra("info", info);
                startActivityForResult(intent, REQUEST_CODE_FORPROGRAM);
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(getPageContext()));
            adapter = new InjectionCurrentAdapter(getPageContext(), dataDetail.getDetail());
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FORPROGRAM:
                    getData();
                    break;
                default:
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AddProgramEventBus event) {
        getData();
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout._fragment_injection_currcnt, null);
        tvName = view.findViewById(R.id.tv_current_name);
        tvTime = view.findViewById(R.id.tv_current_time);
        tvEdit = view.findViewById(R.id.tv_program_edit);
        recyclerView = view.findViewById(R.id.rv_current);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }
}
