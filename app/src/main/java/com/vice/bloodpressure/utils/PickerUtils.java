package com.vice.bloodpressure.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.imp.CallBackTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 描述: 时间选择的工具类
 * 作者: LYD
 * 所需参数: 无
 * 创建日期: 2018/10/31 15:14
 */
public class PickerUtils {
    private PickerUtils() {
    }

    /**
     * 显示 年月日 时间
     *
     * @param context
     * @param callBack
     */
    public static void showTime(Context context, final PickerUtils.TimePickerCallBack callBack) {
        boolean[] booleans = {true, true, true, false, false, false};
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        showTime(context, callBack, booleans, format);
    }

    /**
     * 显示年月日 时分日期
     *
     * @param context
     * @param callBack
     */
    public static void showTimeHm(Context context, final PickerUtils.TimePickerCallBack callBack) {
        boolean[] booleans = {true, true, true, true, true, false};
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        showTime(context, callBack, booleans, format);
    }

    /**
     * 显示 时分日期
     */
    public static void showTimeHourAndMin(Context context, final PickerUtils.TimePickerCallBack callBack) {
        boolean[] booleans = {false, false, false, true, true, false};
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        showTime(context, callBack, booleans, format);
    }

    /**
     * 显示年月日 时分秒日期
     *
     * @param context
     * @param callBack
     * @param booleans
     * @param format
     */
    public static void showTime(Context context, final PickerUtils.TimePickerCallBack callBack, boolean[] booleans, SimpleDateFormat format) {
        Calendar currentDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(startDate.get(Calendar.YEAR) - 120, 0, 1, 0, 0);
        TimePickerView timePickerView = new TimePickerBuilder(context, (date, v) -> {
            String time = format.format(date);
            callBack.execEvent(time);
        })
                .setType(booleans)
                .setRangDate(startDate, endDate)
                .setContentTextSize(21)
                //设置字体大小
                .isDialog(true)
                .setDate(currentDate)
                .build();
        timePickerView.show();
        Dialog mDialog = timePickerView.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);
            params.leftMargin = 0;
            params.rightMargin = 0;
            timePickerView.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
    }

    /**
     * 显示选项
     *
     * @param context
     * @param callBack
     * @param listOption
     */
    public static void showOption(Context context, final PickerUtils.TimePickerCallBack callBack, List listOption) {
        OptionsPickerView pv = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                callBack.execEvent(listOption.get(options1).toString());
            }
        })
                .setContentTextSize(21)
                .build();//构造
        pv.show();//显示
        pv.setPicker(listOption);
        pv.setSelectOptions(0);
    }

    /**
     * 显示选项
     *
     * @param context
     * @param callBack
     * @param listOption
     */
    public static void showOptionPosition(Context context, final PickerUtils.PositionCallBack callBack, List listOption) {
        OptionsPickerView pv = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //callBack.execEvent(listOption.get(options1).toString());
                callBack.execEvent(listOption.get(options1).toString(), options1);
            }
        })
                .setContentTextSize(21)
                .build();//构造
        pv.show();//显示
        pv.setPicker(listOption);
        pv.setSelectOptions(0);
    }


    public static void showTimeWindow(Context context, boolean[] booleans, String dataManager,
                                      final PickerUtils.TimePickerCallBack callBack) {
        Calendar currentDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        startDate.set(currentYear - 120, 0, 1, 0, 0);

        TimePickerView timePickerView = new TimePickerBuilder(context, (date, v) -> {
            String content = DataUtils.convertDateToString(date, dataManager);
            callBack.execEvent(content);
        })
                .setDate(currentDate)
                .setRangDate(startDate, endDate)
                .setType(booleans)
                .setItemVisibleCount(9)
                .setContentTextSize(15)
                .setLineSpacingMultiplier(2.0f)
                .setSubmitColor(ContextCompat.getColor(context, R.color.main_green))
                .setCancelColor(ContextCompat.getColor(context, R.color.black_text))
                .build();
        timePickerView.show();
    }



    public static void showTimeWindow(Context context, boolean[] booleans, String dataManager,String currentTime,
                                      final PickerUtils.TimePickerCallBack callBack) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat(dataManager).parse(currentTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar currentDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        startDate.set(currentYear - 120, 0, 1, 0, 0);

        TimePickerView timePickerView = new TimePickerBuilder(context, (date, v) -> {
            String content = DataUtils.convertDateToString(date, dataManager);
            callBack.execEvent(content);
        })
                .setDate(calendar)
                .setRangDate(startDate, endDate)
                .setType(booleans)
                .setSubmitColor(ContextCompat.getColor(context, R.color.main_green))
                .setCancelColor(ContextCompat.getColor(context, R.color.black_text))
                .build();
        timePickerView.show();
    }

    public static void showTimeWindow(Context context, boolean[] booleans, String dataManager, FrameLayout parentFra, final PickerUtils.TimePickerCallBack callBack) {
        Calendar currentDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentHour = currentDate.get(Calendar.HOUR);
        startDate.set(currentYear - 120, 0, 1, 0, 0);
        TimePickerView timePickerView = new TimePickerBuilder(context, (date, v) -> {
            String content = DataUtils.convertDateToString(date, dataManager);
            callBack.execEvent(content);
        }).setDate(currentDate).setRangDate(startDate, endDate)
                .setType(booleans)
                .setSubmitColor(ContextCompat.getColor(context, R.color.blue))
                .setCancelColor(ContextCompat.getColor(context, R.color.black_text))
                //                .isDialog(true)
                .setDecorView(parentFra)
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
     * 控制显示的时间
     * 显示时分
     *
     * @param context
     * @param startHour
     * @param endHour
     * @param callBack
     */
    public static void onTimePicker(Context context, int startHour, int endHour, PickerUtils.TimePickerCallBack1 callBack) {
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

    public interface TimePickerCallBack1 {
        void execEvent(String hour, String minute);
    }

    public interface TimePickerCallBack {
        void execEvent(String content);
    }


    /**
     * 回调位置
     */
    public interface PositionCallBack {
        void execEvent(String content, int position);
    }

    public static <T> void showChooseSinglePicker(Context context, String title, List<T> list, CallBackTime callBack) {
        OptionsPickerView optionsPickerView = new OptionsPickerBuilder(context, (options1, options2, options3, v) -> {

            callBack.callBack(options1);
        }).setLineSpacingMultiplier(1.5f)
                .setCancelColor(ContextCompat.getColor(context, R.color.gray_text))
                .setSubmitColor(ContextCompat.getColor(context, R.color.main_green))
                .setTitleText(title)
                .setTitleBgColor(ContextCompat.getColor(context, R.color.white_text))
                .setDividerColor(ContextCompat.getColor(context, R.color.gray_light))
                .build();
        optionsPickerView.setPicker(list);
        optionsPickerView.show();
    }

}
