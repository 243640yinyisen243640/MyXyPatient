package com.vice.bloodpressure.ui.activity.insulin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.utils.BleMSTUtils;
import com.vice.bloodpressure.utils.MySPUtils;
import com.wei.android.lib.colorview.view.ColorButton;
import com.wei.android.lib.colorview.view.ColorEditText;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InsulinInputNumActivity extends XYSoftUIBaseActivity {
    private ImageView bgImageView;
    private ColorEditText etInput;
    private ColorButton btSure;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        containerView().addView(initView());
        btSure.setOnClickListener(v -> {
            String num = etInput.getText().toString().trim();
            if (TextUtils.isEmpty(num)){
                ToastUtils.showLongToast(getPageContext(),"请输入编号");
                return;
            }
            String mac = getIntent().getStringExtra(MySPUtils.BLUE_MAC);
            String eqcode = getIntent().getStringExtra(MySPUtils.DEVICE_NAME);
            MySPUtils.putString(getPageContext(), MySPUtils.BLUE_MAC, mac);
            MySPUtils.putString(getPageContext(), MySPUtils.DEVICE_NAME, eqcode);
            MySPUtils.putString(getPageContext(), MySPUtils.BLUE_TYPE, "2");
            MySPUtils.putString(getPageContext(), MySPUtils.DEVICE_NUM, num);
            BleMSTUtils.getInstance().connect(getPageContext(), mac);
            finish();
        });
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_insulin_input_num, null);
        etInput = view.findViewById(R.id.et_insulin_input_img_bg);
        btSure = view.findViewById(R.id.tv_insulin_input_sure);
        return view;
    }
}
