package com.vice.bloodpressure.adapter.injection;

import android.content.Intent;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.ui.activity.injection.InjectionProgramAddDeviceActivity;
import com.vice.bloodpressure.ui.activity.injection.InjectionProgramUnbindDeviceActivity;
import com.vice.bloodpressure.ui.activity.mydevice.MyBindDeviceActivity;
import com.vice.bloodpressure.ui.activity.mydevice.ScanActivity;
import com.vice.bloodpressure.utils.BlueUtils;

import java.util.List;

public class MyBindDeviceNewAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MyBindDeviceNewAdapter(@Nullable List<String> data) {
        super(R.layout.item_my_bind_device_list, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        int layoutPosition = helper.getLayoutPosition();
        TypedArray imgArray = Utils.getApp().getResources().obtainTypedArray(R.array.my_device_bind_list_pic_new);
        helper.setImageResource(R.id.img_device, imgArray.getResourceId(layoutPosition, 0));
        helper.setText(R.id.tv_name, item);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                switch (layoutPosition) {
                    //绑定血糖仪
                    case 0:
                        String imei = SPStaticUtils.getString("imei");
                        if (TextUtils.isEmpty(imei)) {
                            intent = new Intent(Utils.getApp(), ScanActivity.class);
                        } else {
                            intent = new Intent(Utils.getApp(), MyBindDeviceActivity.class);
                            intent.putExtra("position", layoutPosition);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Utils.getApp().startActivity(intent);
                        break;
                    //绑定血压计
                    case 1:
                        String imeiBp = SPStaticUtils.getString("snnum");
                        if (TextUtils.isEmpty(imeiBp)) {
                            intent = new Intent(Utils.getApp(), ScanActivity.class);
                        } else {
                            intent = new Intent(Utils.getApp(), MyBindDeviceActivity.class);
                            intent.putExtra("position", layoutPosition);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Utils.getApp().startActivity(intent);
                        break;
                    case 2:

                        if (BlueUtils.isBind()) {
                            //已绑定
                            intent = new Intent(Utils.getApp(), InjectionProgramUnbindDeviceActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Utils.getApp().startActivity(intent);
                        } else {
                            //未绑定
                            intent = new Intent(Utils.getApp(), InjectionProgramAddDeviceActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Utils.getApp().startActivity(intent);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
