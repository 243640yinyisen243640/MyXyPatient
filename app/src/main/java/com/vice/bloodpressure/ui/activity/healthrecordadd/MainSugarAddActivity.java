package com.vice.bloodpressure.ui.activity.healthrecordadd;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lyd.baselib.utils.eventbus.EventBusUtils;
import com.lyd.baselib.utils.eventbus.EventMessage;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseHandlerActivity;
import com.vice.bloodpressure.constant.ConstantParam;
import com.vice.bloodpressure.constant.DataFormatManager;
import com.vice.bloodpressure.net.OkHttpCallBack;
import com.vice.bloodpressure.net.XyUrl;
import com.vice.bloodpressure.utils.PickerUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class MainSugarAddActivity extends BaseHandlerActivity {
    @BindView(R.id.bloodEdit)
    EditText bloodEdit;
    @BindView(R.id.timeTextView)
    TextView timeTextView;
    @BindView(R.id.timeLin)
    LinearLayout timeLin;
    @BindView(R.id.dialog_saveButton)
    Button saveButton;

    private String sugarBlood;
    private String time;
    private String typePosition;
    private String timeMd;
    private List<String> listStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("添加血糖");
        typePosition = getIntent().getStringExtra("typePosition");
        timeMd = getIntent().getStringExtra("timeMd");
        listStr = (List<String>) getIntent().getSerializableExtra("listStr");
        setClickListener();
    }

    private void setClickListener() {
        timeLin.setOnClickListener(v -> {
            closeKeyboard();
            PickerUtils.showTimeWindow(getPageContext(), new boolean[]{false, false, false, true, true, false}, DataFormatManager.TIME_FORMAT_H_M, content -> {
                timeTextView.setText(content);
                if (!TextUtils.isEmpty(bloodEdit.getText().toString().trim())) {
                    saveButton.setBackgroundColor(ColorUtils.getColor(R.color.main_home));
                }
            });
        });
        saveButton.setOnClickListener(v -> {
            sugarBlood = bloodEdit.getText().toString();
            time = timeTextView.getText().toString();
            if (TextUtils.isEmpty(sugarBlood)) {
                ToastUtils.showShort("请选输入血糖值");
                return;
            }
            if (TextUtils.isEmpty(time)) {
                ToastUtils.showShort("请选择时间");
                return;
            }
            saveData();
        });
    }

    /**
     * 时间点 ‘凌晨’,’8’,
     * ’睡前’,’7’,
     * ’晚餐后2小时’,’6’,
     * ’晚餐前’,’5’,’
     * 午餐后2小时’,’4’,
     * ’午餐前’,’3’,
     * ’早餐后2小时’,’2’,
     * ’空腹’,’1’
     */
    private void saveData() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        HashMap map = new HashMap<>();
        map.put("glucosevalue", sugarBlood);
        map.put("category", typePosition);
        map.put("datetime", "" + year + "-" + timeMd + " " + time);
        XyUrl.okPostSave(XyUrl.ADD_SUGAR, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                if (listStr == null) {
                    listStr = new ArrayList<>();
                }
                listStr.clear();
                listStr.add(bloodEdit.getText().toString().trim());
                BigDecimal discountBigDecimal = new BigDecimal(bloodEdit.getText().toString().trim()).multiply(new BigDecimal("10"));
                int content = discountBigDecimal.toBigInteger().intValue();
                if (content > 0 && content < 38 || content == 38) {
                    listStr.add("2");
                } else if (content > 38 && content < 61) {
                    listStr.add("1");
                } else {
                    listStr.add("3");
                }
                ToastUtils.showShort(R.string.save_ok);
                EventBusUtils.post(new EventMessage(ConstantParam.BLOOD_SUGAR_ADD));
                finish();
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });
    }

    //只是关闭软键盘  隐藏所有的软键盘
    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    protected View addContentLayout() {
        return getLayoutInflater().inflate(R.layout.dialog_bloodsuger, contentLayout, false);
    }

    @Override
    public void processHandlerMsg(Message msg) {

    }
}
