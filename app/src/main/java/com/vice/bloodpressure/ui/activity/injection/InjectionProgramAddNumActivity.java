package com.vice.bloodpressure.ui.activity.injection;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.allen.library.utils.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.injection.DrugAdapter1;
import com.vice.bloodpressure.adapter.injection.DrugAdapter2;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.DrugInfo;
import com.vice.bloodpressure.bean.injection.DrugListInfo;
import com.vice.bloodpressure.utils.PickerUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:方案针数
 */
public class InjectionProgramAddNumActivity extends XYSoftUIBaseActivity {
    private List<DrugListInfo> listInfos;
    private String value;
    private final List<DrugInfo> list1 = new ArrayList<>();
    private DrugAdapter1 adapter1;
    private final List<DrugInfo> list2 = new ArrayList<>();
    private DrugAdapter2 adapter2;
    private TextView tvValue;
    private TextView tvName;
    private View vLine;
    private LinearLayout llList;
    private ListView listView1;
    private ListView listView2;
    private TextView tvConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int chooseDetailPos = getIntent().getIntExtra("chooseDetailPos", 0);
        topViewManager().titleTextView().setText("第" + chooseDetailPos + "针");
        containerView().addView(initView());
        initData();
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout._activity_program_drug_detail, null);
        tvValue = view.findViewById(R.id.tv_program_drug_value);
        tvValue.setOnClickListener(v -> {
            //选择结果
            PickerUtils.showChooseSinglePicker(getPageContext(), "", getList(), object -> {
                tvValue.setText(getList().get(Integer.parseInt(String.valueOf(object))));
                value = Integer.parseInt(String.valueOf(object)) + 1 + "";
            });
        });
        tvName = view.findViewById(R.id.tv_program_drug_name);
        vLine = view.findViewById(R.id.v_program_line);
        llList = view.findViewById(R.id.ll_list);
        tvName.setOnClickListener(v -> {
            vLine.setVisibility(View.GONE);
            llList.setVisibility(View.VISIBLE);
        });
        listView1 = view.findViewById(R.id.lv_program_drug_1);
        listView1.setOnItemClickListener((parent, view1, position, id) -> {
            for (int i = 0; i < list1.size(); i++) {
                list1.get(i).setCheck(false);
            }
            list1.get(position).setCheck(true);
            adapter1.notifyDataSetChanged();

            list2.clear();
            for (int i = 0; i < listInfos.get(position).getDrug_data().size(); i++) {
                list2.add(new DrugInfo(listInfos.get(position).getDrug_data().get(i)));
            }
            adapter2.notifyDataSetChanged();
        });
        listView2 = view.findViewById(R.id.lv_program_drug_2);
        listView2.setOnItemClickListener((parent, view12, position, id) -> {
            for (int i = 0; i < list2.size(); i++) {
                list2.get(i).setCheck(false);
            }
            list2.get(position).setCheck(true);
            tvName.setText(list2.get(position).getName());
            adapter2.notifyDataSetChanged();
        });
        tvConfirm = view.findViewById(R.id.tv_program_name_complete);
        tvConfirm.setOnClickListener(v -> {

            if (TextUtils.isEmpty(value)) {
                ToastUtils.showToast("请选择注射单位");
                return;
            }
            String name = tvName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                ToastUtils.showToast("请选择胰岛素类型");
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("value", value);
            intent.putExtra("name", name);
            setResult(RESULT_OK, intent);
            finish();
        });
        return view;
    }

    /**
     * 选择注射单位
     */
    private List<String> getList() {
        List<String> allList = new ArrayList<>();
        for (int i = 1; i < 51; i++) {
            allList.add(i + "单位");
        }
        return allList;
    }

    private void initData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        String token = loginBean.getToken();
        Call<String> requestCall = DataManager.getDrugs(token, (call, response) -> {
            if (200 == response.code) {
                listInfos = (List<DrugListInfo>) response.object;
                setData();
            } else {
                ToastUtils.showToast("网络连接不可用，请稍后重试！");
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }

    private void setData() {
        if (listInfos == null || listInfos.size() == 0) {
            return;
        }
        list1.clear();
        for (int i = 0; i < listInfos.size(); i++) {
            DrugInfo drugInfo = new DrugInfo(listInfos.get(i).getDrug_name());
            if (i == 0) {
                drugInfo.setCheck(true);
            }
            list1.add(drugInfo);
        }
        adapter1 = new DrugAdapter1(getPageContext(), list1);
        listView1.setAdapter(adapter1);
        list2.clear();
        for (int i = 0; i < listInfos.get(0).getDrug_data().size(); i++) {
            list2.add(new DrugInfo(listInfos.get(0).getDrug_data().get(i)));
        }
        adapter2 = new DrugAdapter2(getPageContext(), list2);
        listView2.setAdapter(adapter2);
    }
}
