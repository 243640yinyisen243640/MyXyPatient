package com.vice.bloodpressure.ui.activity.injection;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.utils.ToastUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.injection.InjectionCurrentAdapter;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.injection.InjectionDataDetail;

import retrofit2.Call;

/**
 * 作者: beauty
 * 类名:方案详情
 * 传参:
 * 描述:
 */
public class HealthRecordInjectioneDetailActivity extends XYSoftUIBaseActivity {
    private TextView tvNum;
    private TextView tvName;
    private TextView tvTime;
    private RecyclerView recyclerView;
    private InjectionCurrentAdapter adapter;
    private InjectionDataDetail dataDetail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("方案详情");
        containerView().addView(initView());
        getData();
    }

    public void getData() {
        String userId = getIntent().getStringExtra("userId");
        String action_time = getIntent().getStringExtra("action_time");
        String isuse = getIntent().getStringExtra("isuse");
        Call<String> requestCall = DataManager.getInjectionDetail(userId, action_time, isuse, (call, response) -> {
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
        tvNum.setText(dataDetail.getAction_day() + "");
        tvName.setText(dataDetail.getPlan_name());
        tvTime.setText(dataDetail.getAction_time() + "执行");
        recyclerView.setLayoutManager(new LinearLayoutManager(getPageContext()));
        adapter = new InjectionCurrentAdapter(getPageContext(), dataDetail.getDetail());
        recyclerView.setAdapter(adapter);
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout._fanganxiangqing, null);
        tvNum = view.findViewById(R.id.tv_injection_detail_num);
        tvName = view.findViewById(R.id.tv_injection_detail_name);
        tvTime = view.findViewById(R.id.tv_injection_detail_time);
        recyclerView = view.findViewById(R.id.rv_injection_detail);
        return view;
    }
}
