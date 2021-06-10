package com.vice.bloodpressure.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.azhon.appupdate.utils.DensityUtil;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lyd.baselib.utils.eventbus.EventBusUtils;
import com.lyd.baselib.utils.eventbus.EventMessage;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.constant.ConstantParam;
import com.vice.bloodpressure.constant.DataFormatManager;
import com.vice.bloodpressure.imp.CallBack;
import com.vice.bloodpressure.net.OkHttpCallBack;
import com.vice.bloodpressure.net.XyUrl;
import com.vice.bloodpressure.utils.DataUtils;
import com.vice.bloodpressure.utils.edittext.TextWatcherForBloodSugarAdd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class BloodSugarDialogHome extends Dialog {
    private EditText etBlood;
    private TextView tvTime;
    private String Time, sugarBlood;
    private Context context;
    private int position;
    private String day;

    private List<String> listStr;
    private CallBack callBack;
    private Button btnSave;
    private LinearLayout all;

    public BloodSugarDialogHome(CallBack callBack, List<String> listStr, Context context, int themeResId, int position, String day) {
        super(context, themeResId);
        this.context = context;
        this.position = position;
        this.listStr = listStr;
        this.callBack = callBack;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        this.day = "" + year + "-" + day;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bloodsuger, null);
        initViews(view);
        setContentView(view);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.TOP;
        lp.height = DensityUtil.dip2px(context, 350);
        lp.width = DensityUtil.dip2px(context, 300);
        win.setAttributes(lp);
    }


    private void initViews(View view) {
        tvTime = view.findViewById(R.id.timeTextView);
        btnSave = view.findViewById(R.id.dialog_saveButton);
        etBlood = view.findViewById(R.id.bloodEdit);
        LinearLayout llTime = view.findViewById(R.id.timeLin);
        all = view.findViewById(R.id.ll_blood_show);
        //        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(context, 350));
        //        all.setLayoutParams(rl);
        etBlood.addTextChangedListener(new TextWatcherForBloodSugarAdd(etBlood).setDigits(40));
        llTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                PickerUtils.showTimeHourAndMin(context, new PickerUtils.TimePickerCallBack() {
                //                    @Override
                //                    public void execEvent(String content) {
                //                        tvTime.setText(content);
                //                        if (!TextUtils.isEmpty(etBlood.getText().toString().trim())) {
                //                            btnSave.setBackgroundColor(ColorUtils.getColor(R.color.main_home));
                //                        }
                //                    }
                //                });
                showTimeWindow();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sugarBlood = etBlood.getText().toString();
                Time = tvTime.getText().toString();
                if (TextUtils.isEmpty(sugarBlood)) {
                    ToastUtils.showShort("请选输入血糖值");
                    return;
                }
                if (TextUtils.isEmpty(Time)) {
                    ToastUtils.showShort("请选择时间");
                    return;
                }
                saveData();
            }
        });
    }

    private void showTimeWindow() {
        Calendar currentDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        startDate.set(currentYear - 120, 0, 1, 0, 0);
        TimePickerView timePickerView = new TimePickerBuilder(context, (date, v) -> {
            String content = DataUtils.convertDateToString(date, DataFormatManager.TIME_FORMAT_H_M);
            tvTime.setText(content);
            if (!TextUtils.isEmpty(etBlood.getText().toString().trim())) {
                btnSave.setBackgroundColor(ColorUtils.getColor(R.color.main_home));
            }
        }).setDate(currentDate).setRangDate(startDate, endDate)
                .setType(new boolean[]{false, false, false, true, true, false})
                .setSubmitColor(ContextCompat.getColor(context, R.color.blue))
                .setCancelColor(ContextCompat.getColor(context, R.color.black_text))
//                .isDialog(true)
//                .setDecorView(all)
                .build();
//        //设置dialog弹出位置
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
//        params.leftMargin = 0;
//        params.rightMargin = 0;
//        ViewGroup contentContainer = timePickerView.getDialogContainerLayout();
//        contentContainer.setLayoutParams(params);
//        timePickerView.getDialog().getWindow().setGravity(Gravity.BOTTOM);//可以改成Bottom
//        timePickerView.getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        timePickerView.show();

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
        HashMap map = new HashMap<>();
        map.put("glucosevalue", sugarBlood);
        map.put("category", position);
        map.put("datetime", day + " " + Time);
        XyUrl.okPostSave(XyUrl.ADD_SUGAR, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                if (listStr == null) {
                    listStr = new ArrayList<>();
                }
                listStr.clear();
                listStr.add(etBlood.getText().toString().trim());
                BigDecimal discountBigDecimal = new BigDecimal(etBlood.getText().toString().trim()).multiply(new BigDecimal("10"));
                int content = discountBigDecimal.toBigInteger().intValue();
                if (content > 0 && content < 38 || content == 38) {
                    listStr.add("2");
                } else if (content > 38 && content < 61) {
                    listStr.add("1");
                } else {
                    listStr.add("3");
                }

                if (callBack != null) {
                    callBack.callBack();
                }
                ToastUtils.showShort(R.string.save_ok);
                EventBusUtils.post(new EventMessage(ConstantParam.BLOOD_SUGAR_ADD));
                dismiss();
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });
    }
}
