package com.vice.bloodpressure.ui.activity.injection;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.utils.ToastUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.injection.MyBindDeviceListAdapter;
import com.vice.bloodpressure.base.activity.BaseActivity;
import com.vice.bloodpressure.bean.DeviceChangeBean;
import com.vice.bloodpressure.bean.injection.InjectionBaseInfo;
import com.vice.bloodpressure.ui.activity.insulin.InsulinDeviceListActivity;
import com.vice.bloodpressure.ui.activity.mydevice.MyBindDeviceActivity;
import com.vice.bloodpressure.ui.activity.mydevice.ScanActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;

public class MyBindDeviceListNewActivity extends BaseActivity {
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    private DeviceChangeBean deviceBean;
    private List<InjectionBaseInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("我的设备");

        //        getIsBindDevice();
        setRv();
    }

    private void getIsBindDevice() {
        LoginBean userLogin = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        Call<String> requestCall = DataManager.getDeviceIsBind(userLogin.getToken(), (call, response) -> {
            if (200 == response.code) {
                deviceBean = (DeviceChangeBean) response.object;
                //保存血糖设备码
                String imei = deviceBean.getImei();
                SPStaticUtils.put("imei", imei);
                //保存血压设备码
                String snnum = deviceBean.getSnnum();
                SPStaticUtils.put("snnum", snnum);
                //保存快舒尔设备码
                String insulinnum = deviceBean.getInsulinnum();
                SPStaticUtils.put("insulinnum", insulinnum);
                rvList.setAdapter(new MyBindDeviceListAdapter(getPageContext(), list, (view, position) -> {
                    if (deviceBean != null) {
                        Intent intent;
                        switch (position) {
                            case 0:
                                if (TextUtils.isEmpty(deviceBean.getImei())) {
                                    intent = new Intent(getPageContext(), ScanActivity.class);
                                    startActivity(intent);
                                } else {
                                    intent = new Intent(getPageContext(), MyBindDeviceActivity.class);
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                }
                                break;
                            case 1:
                                if (TextUtils.isEmpty(deviceBean.getSnnum())) {
                                    intent = new Intent(getPageContext(), ScanActivity.class);
                                    startActivity(intent);
                                } else {
                                    intent = new Intent(getPageContext(), MyBindDeviceActivity.class);
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                }
                                break;
                            case 2:
                                if (TextUtils.isEmpty(deviceBean.getInsulinnum())) {
                                    intent = new Intent(getPageContext(), InjectionAddDeviceNoActivity.class);
                                    intent.putExtra("type", "1");
                                    startActivity(intent);
                                } else {
                                    intent = new Intent(getPageContext(), InjectionProgramUnbindDeviceActivity.class);
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                }
                                break;
                            default:
                                break;
                        }
                    }

                }));
            } else {
                ToastUtils.showToast(response.msg);
            }
        }, (call, t) -> {
            ToastUtils.showToast("网络连接不可用，请稍后重试！");
        });
    }


    private void setRv() {
        //        String[] stringArray = getResources().getStringArray(R.array.my_device_bind_list_name_new);
        //        List<String> list = Arrays.asList(stringArray);

        list = new ArrayList<>();
        list.add(new InjectionBaseInfo("血糖设备", R.drawable.my_bind_device_sugar));
        list.add(new InjectionBaseInfo("血压设备", R.drawable.my_bind_device_bp));
        list.add(new InjectionBaseInfo("注射设备", R.drawable.injection_add));
        list.add(new InjectionBaseInfo("胰岛素泵", R.drawable.insulin_add));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getPageContext());
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setAdapter(new MyBindDeviceListAdapter(getPageContext(), list, (view, position) -> {
            LoginBean userLogin = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
            Call<String> requestCall = DataManager.getDeviceIsBind(userLogin.getToken(), (call, response) -> {
                ToastUtils.showToast(response.msg);
                if (200 == response.code) {
                    deviceBean = (DeviceChangeBean) response.object;
                    //保存血糖设备码
                    String imei = deviceBean.getImei();
                    SPStaticUtils.put("imei", imei);
                    //保存血压设备码
                    String snnum = deviceBean.getSnnum();
                    SPStaticUtils.put("snnum", snnum);
                    //保存快舒尔设备码
                    String insulinnum = deviceBean.getInsulinnum();
                    SPStaticUtils.put("insulinnum", insulinnum);
                    //保存快舒尔设备码
                    String eqinsulinnum = deviceBean.getEqinsulinnum();
                    SPStaticUtils.put("eqinsulinnum", eqinsulinnum);
                    if (deviceBean != null) {
                        Intent intent;
                        switch (position) {
                            case 0:
                                if (TextUtils.isEmpty(deviceBean.getImei())) {
                                    intent = new Intent(getPageContext(), ScanActivity.class);
                                    startActivity(intent);
                                } else {
                                    intent = new Intent(getPageContext(), MyBindDeviceActivity.class);
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                }
                                break;
                            case 1:
                                if (TextUtils.isEmpty(deviceBean.getSnnum())) {
                                    intent = new Intent(getPageContext(), ScanActivity.class);
                                    startActivity(intent);
                                } else {
                                    intent = new Intent(getPageContext(), MyBindDeviceActivity.class);
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                }
                                break;
                            case 2:
                                if (TextUtils.isEmpty(deviceBean.getInsulinnum())) {
                                    intent = new Intent(getPageContext(), InjectionAddDeviceNoActivity.class);
                                    intent.putExtra("type", "1");
                                    startActivity(intent);
                                } else {
                                    intent = new Intent(getPageContext(), InjectionProgramUnbindDeviceActivity.class);
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                }
                                break;
                            case 3:
                                if (TextUtils.isEmpty(deviceBean.getEqinsulinnum())) {
                                    intent = new Intent(getPageContext(), InjectionAddDeviceNoActivity.class);
                                    intent.putExtra("type", "2");
                                    startActivity(intent);
                                } else {
                                    intent = new Intent(getPageContext(), InsulinDeviceListActivity.class);
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }, (call, t) -> {
                ToastUtils.showToast("网络连接不可用，请稍后重试！");
            });

        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRv();
    }

    @Override
    protected View addContentLayout() {
        View view = getLayoutInflater().inflate(R.layout.activity_my_bind_device_list, contentLayout, false);
        return view;
    }
}