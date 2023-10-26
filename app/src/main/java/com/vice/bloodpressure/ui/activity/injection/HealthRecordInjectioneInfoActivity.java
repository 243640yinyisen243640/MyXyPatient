package com.vice.bloodpressure.ui.activity.injection;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.injection.InjectionDetailAdapter;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.injection.InjectDetailInfo;

/**
 * 描述: 注射详情
 * 作者: LYD
 * 创建日期: 2019/3/6 17:07
 */
public class HealthRecordInjectioneInfoActivity extends XYSoftUIBaseActivity {
    private InjectDetailInfo info;
    private TextView tvDate;
    private TextView tvValue;
    private TextView tvIsHeight;
    private RecyclerView recyclerView;
    private InjectionDetailAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("注射详情");
        containerView().addView(initView());
        info = (InjectDetailInfo) getIntent().getSerializableExtra("data");
        setData();
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout._activity_zhushexiangqing, null);
        tvDate = view.findViewById(R.id.tv_injection_detail_date);
        tvValue = view.findViewById(R.id.tv_injection_detail_value);
        tvIsHeight = view.findViewById(R.id.tv_injection_detail_is_height);
        recyclerView = view.findViewById(R.id.rv_injection_detail);
        return view;
    }

    private void setData() {
        tvDate.setText(info.getDatetime()+" "+info.getNum());
        tvValue.setText(info.getValue());
        String ishight = info.getIshight();
        //1偏高 2偏低 3正常
        String isHeightData;
        if (ishight.equals("1")){
            isHeightData = "剂量过高";
        }else if (ishight.equals("2")){
            isHeightData = "剂量偏低";
        }else {
            isHeightData = "剂量正常";
        }
        tvIsHeight.setText(isHeightData);
        adapter=new InjectionDetailAdapter(getPageContext(),info.getDataList());
        recyclerView.setLayoutManager(new LinearLayoutManager(getPageContext()));
        recyclerView.setAdapter(adapter);
    }
}
