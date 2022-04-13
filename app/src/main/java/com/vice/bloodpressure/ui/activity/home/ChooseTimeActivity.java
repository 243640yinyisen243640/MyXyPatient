package com.vice.bloodpressure.ui.activity.home;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ColorUtils;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.constant.DataFormatManager;
import com.vice.bloodpressure.utils.PickerUtils;
import com.vice.bloodpressure.utils.ScreenUtils;

/**
 * 类描述：
 * 类传参：
 *
 * @author android.lyl
 * @date 2021/01/20
 */
public class ChooseTimeActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etBlood;
    private TextView tvTime;
    private LinearLayout llTime;

    private Button btnSave;
    private int position;

    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(getPageContext(), 500));
        getWindow().setGravity(Gravity.CENTER);
        this.setFinishOnTouchOutside(false);

        initView();
        initListener();
    }

    private Context getPageContext() {
        return context;
    }

    private void initListener() {

        btnSave.setOnClickListener(this);
        llTime.setOnClickListener(this);
    }


    private void initView() {
        View view = View.inflate(getPageContext(), R.layout.dialog_bloodsuger, null);
        setContentView(view);
        tvTime = view.findViewById(R.id.timeTextView);
        btnSave = view.findViewById(R.id.dialog_saveButton);
        etBlood = view.findViewById(R.id.bloodEdit);
        llTime = view.findViewById(R.id.timeLin);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.timeLin:
                PickerUtils.showTimeWindow(ChooseTimeActivity.this, new boolean[]{false, false, false, true, true, false}, DataFormatManager.TIME_FORMAT_H_M, new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        tvTime.setText(content);
                        if (!TextUtils.isEmpty(etBlood.getText().toString().trim())) {
                            btnSave.setBackgroundColor(ColorUtils.getColor(R.color.main_home));
                        }
                    }
                });


                break;
            case R.id.dialog_saveButton:

                break;


            default:
                break;
        }
    }



}
