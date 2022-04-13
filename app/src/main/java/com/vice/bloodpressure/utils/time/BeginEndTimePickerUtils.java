package com.vice.bloodpressure.utils.time;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;

import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.vice.bloodpressure.R;

import java.util.ArrayList;
import java.util.List;

import cn.addapp.pickers.picker.TimePicker;

public class BeginEndTimePickerUtils {
    public static final int HOUR_ZERO = 0;
    public static final int HOUR_ONE = 1;
    public static final int HOUR_TWO = 2;
    public static final int HOUR_THREE = 3;
    public static final int HOUR_FOUR = 4;
    public static final int HOUR_FIVE = 5;
    public static final int HOUR_SIX = 6;
    public static final int HOUR_SEVEN = 7;
    public static final int HOUR_EIGHT = 8;
    public static final int HOUR_NINE = 9;
    public static final int HOUR_TEN = 10;
    public static final int HOUR_ELEVEN = 11;
    public static final int HOUR_TWELVE = 12;
    public static final int HOUR_THIRTEEN = 13;
    public static final int HOUR_FOURTEEN = 14;
    public static final int HOUR_FIFTEEN = 15;
    public static final int HOUR_SIXTEEN = 16;
    public static final int HOUR_SEVENTEEN = 17;
    public static final int HOUR_EIGHTEEN = 18;
    public static final int HOUR_NINETEEN = 19;
    public static final int HOUR_TWENTY = 20;
    public static final int HOUR_TWENTY_ONE = 21;
    public static final int HOUR_TWENTY_TWO = 22;
    public static final int HOUR_TWENTY_THREE = 23;


    //    @IntDef({ALIGN_BOTTOM, ALIGN_BASELINE, ALIGN_CENTER, ALIGN_TOP})
    //    @Retention(RetentionPolicy.SOURCE)
    //    public @interface Align {
    //    }

    /**
     * @param activity    类
     * @param startHour   开始小时
     * @param startMinute 开始分钟
     * @param endHour     结束小时
     * @param endMinute   结束分钟
     * @param callBack    回调
     */
    public static void onTimePicker(Activity activity, int startHour, int startMinute, int endHour, int endMinute, final BeginEndTimePickerUtils.TimePickerCallBack callBack) {
        TimePicker picker = new TimePicker(activity, TimePicker.HOUR_24);
        //09:00
        picker.setRangeStart(startHour, startMinute);
        //18:30
        picker.setRangeEnd(endHour, endMinute);
        picker.setTopLineVisible(false);
        picker.setLineVisible(false);
        picker.setGravity(Gravity.BOTTOM);
        picker.setTextSize(21);
        picker.setSelectedTextColor(ContextCompat.getColor(activity, R.color.black_text));
        picker.setCancelTextColor(ContextCompat.getColor(activity, R.color.begin_end_time));
        picker.setCancelTextSize(17);
        picker.setSubmitTextColor(ContextCompat.getColor(activity, R.color.begin_end_time));
        picker.setSubmitTextSize(17);
        picker.setLineVisible(true);
        picker.setOuterLabelEnable(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                callBack.execEvent(hour, minute);
            }
        });
        picker.show();
    }


    /**
     * 显示时分
     *
     * @param context
     * @param startHour
     * @param endHour
     * @param callBack
     */
    public static void onTimePicker(Context context, int startHour, int endHour, TimePickerCallBack callBack) {
        if (0 <= startHour && startHour < 24 && 0 <= endHour && endHour < 24 && startHour <= endHour) {
            List<String> optionsItem1 = new ArrayList<>();
            List<List<String>> optionsItem2 = new ArrayList<>();
            for (int i = startHour; i < endHour + 1; i++) {
                if (i < 10) {
                    optionsItem1.add("0" + i);
                } else {
                    optionsItem1.add("" + i);
                }
                List<String> item2 = new ArrayList<>();
                for (int j = 0; j < 60; j++) {
                    if (j < 10) {
                        item2.add("0" + j);
                    } else {
                        item2.add("" + j);
                    }
                }
                optionsItem2.add(item2);
            }
            OptionsPickerView optionsPickerView = new OptionsPickerBuilder(context, (options1, options2, options3, v) -> {
                String hour = optionsItem1.get(options1);
                //                String result = optionsItem1.get(options1) + "时" + optionsItem2.get(options1).get(options2) + "分";
                String min = optionsItem2.get(options1).get(options2);
                callBack.execEvent(hour, min);
            })
                    .setCancelText("取消")
                    .setSubmitText("确定")
                    //                .setCancelColor(ContextCompat.getColor(context, R.color.begin_end_time))
                    //                .setSubmitColor(ContextCompat.getColor(context, R.color.begin_end_time))
                    .setLabels("时", "分", "")
                    .build();
            optionsPickerView.setPicker(optionsItem1, optionsItem2);
            optionsPickerView.show();
        }
    }

    public interface TimePickerCallBack {
        void execEvent(String hour, String minute);
    }


}
