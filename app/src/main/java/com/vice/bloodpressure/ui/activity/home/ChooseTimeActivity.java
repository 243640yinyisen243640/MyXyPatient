package com.vice.bloodpressure.ui.activity.home;

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
import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.ColorUtils;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.constant.DataFormatManager;
import com.vice.bloodpressure.utils.DataUtils;
import com.vice.bloodpressure.utils.ScreenUtils;

import java.util.Calendar;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int height = ScreenUtils.dip2px(ChooseTimeActivity.this, 100);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height);
        getWindow().setGravity(Gravity.CENTER);
        this.setFinishOnTouchOutside(false);

        initView();
        initListener();
    }

    private void initListener() {

        btnSave.setOnClickListener(this);
        llTime.setOnClickListener(this);
    }


    private void initView() {
        View view = View.inflate(ChooseTimeActivity.this, R.layout.dialog_bloodsuger, null);
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
                showTimeWindow();
                break;
            case R.id.dialog_saveButton:

                break;


            default:
                break;
        }
    }

    private void showTimeWindow() {
        Calendar currentDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        startDate.set(currentYear - 120, 0, 1, 0, 0);
        TimePickerView timePickerView = new TimePickerBuilder(ChooseTimeActivity.this, (date, v) -> {
            String content = DataUtils.convertDateToString(date, DataFormatManager.TIME_FORMAT_H_M);
            tvTime.setText(content);
            if (!TextUtils.isEmpty(etBlood.getText().toString().trim())) {
                btnSave.setBackgroundColor(ColorUtils.getColor(R.color.main_home));
            }
        }).setDate(currentDate).setRangDate(startDate, endDate)
                .setType(new boolean[]{false, false, false, true, true, false})
                .setSubmitColor(ContextCompat.getColor(ChooseTimeActivity.this, R.color.blue))
                .setCancelColor(ContextCompat.getColor(ChooseTimeActivity.this, R.color.black_text))
                .build();
        timePickerView.show();
    }


}
