package com.vice.bloodpressure.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.vice.bloodpressure.utils.PickerUtils;
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
    private FrameLayout all;

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
        lp.gravity = Gravity.CENTER;
        //        lp.height = DensityUtil.dip2px(context, 350);
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        //        lp.width = DensityUtil.dip2px(context, 300);
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);//这是什么呀

    }


    private void initViews(View view) {
        tvTime = view.findViewById(R.id.timeTextView);
        btnSave = view.findViewById(R.id.dialog_saveButton);
        etBlood = view.findViewById(R.id.bloodEdit);
        LinearLayout llTime = view.findViewById(R.id.timeLin);
        all = view.findViewById(R.id.ll_blood_show);
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        all.setLayoutParams(rl);
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
                closeKeyboard();
                PickerUtils.showTimeWindow(context, new boolean[]{false, false, false, true, true, false}, DataFormatManager.TIME_FORMAT_H_M, all, new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        tvTime.setText(content);
                        if (!TextUtils.isEmpty(etBlood.getText().toString().trim())) {
                            btnSave.setBackgroundColor(ColorUtils.getColor(R.color.main_home));
                        }
                    }
                });
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
    //只是关闭软键盘  隐藏所有的软键盘
    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
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
