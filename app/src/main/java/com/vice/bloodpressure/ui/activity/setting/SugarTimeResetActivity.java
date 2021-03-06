package com.vice.bloodpressure.ui.activity.setting;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseHandlerActivity;
import com.vice.bloodpressure.bean.ScopeBean;
import com.vice.bloodpressure.imp.CallBackTime;
import com.vice.bloodpressure.net.OkHttpCallBack;
import com.vice.bloodpressure.net.XyUrl;
import com.vice.bloodpressure.utils.PickerUtils;
import com.vice.bloodpressure.utils.time.BeginEndTimePickerUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SugarTimeResetActivity extends BaseHandlerActivity implements CallBackTime {
    private static final int GET_SCOPE = 10010;
    @BindView(R.id.tv_one_left)
    TextView tvOneLeft;
    @BindView(R.id.tv_one_right)
    TextView tvOneRight;
    @BindView(R.id.tv_two_left)
    TextView tvTwoLeft;
    @BindView(R.id.tv_two_right)
    TextView tvTwoRight;
    @BindView(R.id.tv_three_left)
    TextView tvThreeLeft;
    @BindView(R.id.tv_three_right)
    TextView tvThreeRight;
    @BindView(R.id.tv_four_left)
    TextView tvFourLeft;
    @BindView(R.id.tv_four_right)
    TextView tvFourRight;
    @BindView(R.id.tv_five_left)
    TextView tvFiveLeft;
    @BindView(R.id.tv_five_right)
    TextView tvFiveRight;
    @BindView(R.id.tv_six_left)
    TextView tvSixLeft;
    @BindView(R.id.tv_six_right)
    TextView tvSixRight;
    @BindView(R.id.tv_seven_left)
    TextView tvSevenLeft;
    @BindView(R.id.tv_seven_right)
    TextView tvSevenRight;
    @BindView(R.id.tv_eight_left)
    TextView tvEightLeft;
    @BindView(R.id.tv_eight_right)
    TextView tvEightRight;
    private List<TextView> listSugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("????????????????????????");
        setListSugarTextView();
        getControlTarget();
        getTvSave().setText("??????");
        getTvSave().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSugarPoint();
            }
        });
    }

    private void addSugarPoint() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("empstomach", listSugar.get(0).getText().toString() + "@" + listSugar.get(1).getText().toString());
        map.put("aftbreakfast", listSugar.get(2).getText().toString() + "@" + listSugar.get(3).getText().toString());
        map.put("beflunch", listSugar.get(4).getText().toString() + "@" + listSugar.get(5).getText().toString());
        map.put("aftlunch", listSugar.get(6).getText().toString() + "@" + listSugar.get(7).getText().toString());
        map.put("befdinner", listSugar.get(8).getText().toString() + "@" + listSugar.get(9).getText().toString());
        map.put("aftdinner", listSugar.get(10).getText().toString() + "@" + listSugar.get(11).getText().toString());
        map.put("befsleep", listSugar.get(12).getText().toString() + "@" + listSugar.get(13).getText().toString());
        map.put("inmorning", listSugar.get(14).getText().toString() + "@" + listSugar.get(15).getText().toString());
        XyUrl.okPostSave(XyUrl.ADD_SUGAR_POINT, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                ToastUtils.showShort(R.string.save_ok);
                finish();
            }

            @Override
            public void onError(int error, String errorMsg) {
                ToastUtils.showShort(errorMsg);
            }
        });
    }

    private void getControlTarget() {
        HashMap map = new HashMap<>();
        XyUrl.okPost(XyUrl.GET_USERDATE, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                ScopeBean scope = JSONObject.parseObject(value, ScopeBean.class);
                Message message = Message.obtain();
                message.what = GET_SCOPE;
                message.obj = scope;
                sendHandlerMessage(message);
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });
    }

    private void setListSugarTextView() {
        listSugar = new ArrayList<>();
        listSugar.add(tvOneLeft);
        listSugar.add(tvOneRight);
        listSugar.add(tvTwoLeft);
        listSugar.add(tvTwoRight);
        listSugar.add(tvThreeLeft);
        listSugar.add(tvThreeRight);
        listSugar.add(tvFourLeft);
        listSugar.add(tvFourRight);
        listSugar.add(tvFiveLeft);
        listSugar.add(tvFiveRight);
        listSugar.add(tvSixLeft);
        listSugar.add(tvSixRight);
        listSugar.add(tvSevenLeft);
        listSugar.add(tvSevenRight);
        listSugar.add(tvEightLeft);
        listSugar.add(tvEightRight);
        //??????
        for (int i = 0; i < listSugar.size(); i++) {
            listSugar.get(i).setTag(i);
        }
    }

    @Override
    protected View addContentLayout() {
        View view = getLayoutInflater().inflate(R.layout.activity_sugar_time_reset, contentLayout, false);
        return view;
    }


    @OnClick({R.id.tv_one_left, R.id.tv_one_right,
            R.id.tv_two_left, R.id.tv_two_right,
            R.id.tv_three_left, R.id.tv_three_right,
            R.id.tv_four_left, R.id.tv_four_right,
            R.id.tv_five_left, R.id.tv_five_right,
            R.id.tv_six_left, R.id.tv_six_right,
            R.id.tv_seven_left, R.id.tv_seven_right,
            R.id.tv_eight_left, R.id.tv_eight_right,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //??????
            case R.id.tv_one_left:
                selectTime(0, BeginEndTimePickerUtils.HOUR_THREE, 0, BeginEndTimePickerUtils.HOUR_SEVEN, 0);
                break;
            case R.id.tv_one_right:
                selectTime(1, BeginEndTimePickerUtils.HOUR_SIX, 0, BeginEndTimePickerUtils.HOUR_TEN, 0);
                break;
            //?????????
            case R.id.tv_two_left:
                selectTime(2, BeginEndTimePickerUtils.HOUR_SIX, 0, BeginEndTimePickerUtils.HOUR_TEN, 0);
                break;
            case R.id.tv_two_right:
                selectTime(3, BeginEndTimePickerUtils.HOUR_EIGHT, 0, BeginEndTimePickerUtils.HOUR_TWELVE, 0);
                break;
            //?????????
            case R.id.tv_three_left:
                selectTime(4, BeginEndTimePickerUtils.HOUR_EIGHT, 0, BeginEndTimePickerUtils.HOUR_TWELVE, 0);
                break;
            case R.id.tv_three_right:
                selectTime(5, BeginEndTimePickerUtils.HOUR_TEN, 0, BeginEndTimePickerUtils.HOUR_FOURTEEN, 0);
                break;
            //?????????
            case R.id.tv_four_left:
                selectTime(6, BeginEndTimePickerUtils.HOUR_TEN, 0, BeginEndTimePickerUtils.HOUR_FOURTEEN, 0);
                break;
            case R.id.tv_four_right:
                selectTime(7, BeginEndTimePickerUtils.HOUR_THIRTEEN, 0, BeginEndTimePickerUtils.HOUR_SEVENTEEN, 0);
                break;
            //?????????
            case R.id.tv_five_left:
                selectTime(8, BeginEndTimePickerUtils.HOUR_THIRTEEN, 0, BeginEndTimePickerUtils.HOUR_SEVENTEEN, 0);
                break;
            case R.id.tv_five_right:
                selectTime(9, BeginEndTimePickerUtils.HOUR_SIXTEEN, 0, BeginEndTimePickerUtils.HOUR_TWENTY, 0);
                break;
            //?????????
            case R.id.tv_six_left:
                selectTime(10, BeginEndTimePickerUtils.HOUR_SIXTEEN, 0, BeginEndTimePickerUtils.HOUR_TWENTY, 0);
                break;
            case R.id.tv_six_right:
                selectTime(11, BeginEndTimePickerUtils.HOUR_NINETEEN, 0, BeginEndTimePickerUtils.HOUR_TWENTY_THREE, 0);
                break;
            //??????
            case R.id.tv_seven_left:
                selectTime(12, BeginEndTimePickerUtils.HOUR_NINETEEN, 0, BeginEndTimePickerUtils.HOUR_TWENTY_THREE, 0);
                break;
            case R.id.tv_seven_right:
                selectTime(13, BeginEndTimePickerUtils.HOUR_ZERO, 0, BeginEndTimePickerUtils.HOUR_TWENTY_THREE, 59);
                break;
            //??????
            case R.id.tv_eight_left:
                selectTime(14, BeginEndTimePickerUtils.HOUR_ZERO, 0, BeginEndTimePickerUtils.HOUR_TWENTY_THREE, 59);
                break;
            case R.id.tv_eight_right:
                selectTime(15, BeginEndTimePickerUtils.HOUR_THREE, 0, BeginEndTimePickerUtils.HOUR_SEVEN, 0);
                break;
            default:
                break;

        }
    }



    /**
     * ??????????????????
     */
    private void selectTime(int tag, int beginHour, int beginMinute, int endHour, int endMinute) {
        PickerUtils.onTimePicker(this, beginHour, endHour, (hour, minute) -> {

            //???????????????????????????
            String selectTime = hour + ":" + minute;
            Date selectTimeParse = getDateFromString(selectTime);
            switch (tag) {
                //??????
                case 0:
                    //???????????????????????????
                    listSugar.get(tag).setText(selectTime);
                    //??????????????????
                    listSugar.get(15).setText(minusOneMinute(selectTime));
                    break;
                case 1:
                    setTime(tag, selectTime, "??????", "");
                    break;
                //?????????
                case 2:
                    setTime(tag, selectTime, "?????????", "??????");
                    break;
                case 3:
                    setTime(tag, selectTime, "?????????", "");
                    break;
                //?????????
                case 4:
                    setTime(tag, selectTime, "?????????", "?????????");
                    break;
                case 5:
                    setTime(tag, selectTime, "?????????", "");
                    break;
                //?????????
                case 6:
                    setTime(tag, selectTime, "?????????", "?????????");
                    break;
                case 7:
                    setTime(tag, selectTime, "?????????", "");
                    break;
                //?????????
                case 8:
                    setTime(tag, selectTime, "?????????", "?????????");
                    break;
                case 9:
                    setTime(tag, selectTime, "?????????", "");
                    break;
                //?????????
                case 10:
                    setTime(tag, selectTime, "?????????", "?????????");
                    break;
                case 11:
                    setTime(tag, selectTime, "?????????", "");
                    break;
                //??????
                case 12:
                    setTime(tag, selectTime, "??????", "?????????");
                    break;
                //?????? ????????????
                case 13:
                    if (("22".equals(hour)) || ("23".equals(hour))) {
                        setTime(tag, selectTime, "??????", "");
                    } else if (("00".equals(hour)) || ("01".equals(hour)) || ("02".equals(hour))) {
                        listSugar.get(tag).setText(selectTime);
                        listSugar.get(tag + 1).setText(addOneMinute(selectTime));
                    } else {
                        ToastUtils.showShort("?????????22????????????2??????????????????");
                    }
                    break;
                //?????? ????????????
                case 14:
                    if (("22".equals(hour)) || ("23".equals(hour))) {
                        setTime(tag, selectTime, "??????", "??????");
                    } else if (("00".equals(hour)) || ("01".equals(hour)) || ("02".equals(hour))) {
                        listSugar.get(tag).setText(selectTime);
                        listSugar.get(tag - 1).setText(minusOneMinute(selectTime));
                    } else {
                        ToastUtils.showShort("?????????22????????????2??????????????????");
                    }
                    break;
                case 15:
                    //???????????????????????????
                    listSugar.get(tag).setText(selectTime);
                    //??????????????????
                    listSugar.get(0).setText(addOneMinute(selectTime));
                    break;
                default:
                    break;
            }
        });




//        BeginEndTimePickerUtils.onTimePicker(this, beginHour, beginMinute, endHour, endMinute, new BeginEndTimePickerUtils.TimePickerCallBack() {
//            @Override
//            public void execEvent(String hour, String minute) {
//                //???????????????????????????
//                String selectTime = hour + ":" + minute;
//                Date selectTimeParse = getDateFromString(selectTime);
//                switch (tag) {
//                    //??????
//                    case 0:
//                        //???????????????????????????
//                        listSugar.get(tag).setText(selectTime);
//                        //??????????????????
//                        listSugar.get(15).setText(minusOneMinute(selectTime));
//                        break;
//                    case 1:
//                        setTime(tag, selectTime, "??????", "");
//                        break;
//                    //?????????
//                    case 2:
//                        setTime(tag, selectTime, "?????????", "??????");
//                        break;
//                    case 3:
//                        setTime(tag, selectTime, "?????????", "");
//                        break;
//                    //?????????
//                    case 4:
//                        setTime(tag, selectTime, "?????????", "?????????");
//                        break;
//                    case 5:
//                        setTime(tag, selectTime, "?????????", "");
//                        break;
//                    //?????????
//                    case 6:
//                        setTime(tag, selectTime, "?????????", "?????????");
//                        break;
//                    case 7:
//                        setTime(tag, selectTime, "?????????", "");
//                        break;
//                    //?????????
//                    case 8:
//                        setTime(tag, selectTime, "?????????", "?????????");
//                        break;
//                    case 9:
//                        setTime(tag, selectTime, "?????????", "");
//                        break;
//                    //?????????
//                    case 10:
//                        setTime(tag, selectTime, "?????????", "?????????");
//                        break;
//                    case 11:
//                        setTime(tag, selectTime, "?????????", "");
//                        break;
//                    //??????
//                    case 12:
//                        setTime(tag, selectTime, "??????", "?????????");
//                        break;
//                    //?????? ????????????
//                    case 13:
//                        if (("22".equals(hour)) || ("23".equals(hour))) {
//                            setTime(tag, selectTime, "??????", "");
//                        } else if (("00".equals(hour)) || ("01".equals(hour)) || ("02".equals(hour))) {
//                            listSugar.get(tag).setText(selectTime);
//                            listSugar.get(tag + 1).setText(addOneMinute(selectTime));
//                        } else {
//                            ToastUtils.showShort("?????????22????????????2??????????????????");
//                        }
//                        break;
//                    //?????? ????????????
//                    case 14:
//                        if (("22".equals(hour)) || ("23".equals(hour))) {
//                            setTime(tag, selectTime, "??????", "??????");
//                        } else if (("00".equals(hour)) || ("01".equals(hour)) || ("02".equals(hour))) {
//                            listSugar.get(tag).setText(selectTime);
//                            listSugar.get(tag - 1).setText(minusOneMinute(selectTime));
//                        } else {
//                            ToastUtils.showShort("?????????22????????????2??????????????????");
//                        }
//                        break;
//                    case 15:
//                        //???????????????????????????
//                        listSugar.get(tag).setText(selectTime);
//                        //??????????????????
//                        listSugar.get(0).setText(addOneMinute(selectTime));
//                        break;
//                }
//            }
//        });
    }

    public void setTime(int tag, String selectTime, String typeEnd, String typeStart) {
        Boolean isOdd = isOddNumber(tag);
        int getInt = 0;
        int unitInt = 0;
        String unitTime = "";
        if (isOdd) {
            getInt = tag - 1;
            unitInt = tag + 1;
            unitTime = addOneMinute(selectTime);
        } else {
            getInt = tag - 2;
            unitInt = tag - 1;
            unitTime = minusOneMinute(selectTime);
        }
        //???????????????????????????
        Date selectTimeParse = getDateFromString(selectTime);
        String beginTime = listSugar.get(getInt).getText().toString();
        Date beginTimeParse = getDateFromString(beginTime);
        if (selectTimeParse.compareTo(beginTimeParse) < 0) {
            ToastUtils.showShort(typeEnd + "????????????" + "??????" + typeStart + "????????????");
            return;
        } else {
            //???????????????????????????
            listSugar.get(tag).setText(selectTime);
            //??????????????????
            listSugar.get(unitInt).setText(unitTime);
        }
    }

    /**
     * ??????Date??????
     *
     * @param timeStr
     * @return
     */
    public Date getDateFromString(String timeStr) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = dateFormat.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * ?????????????????????
     *
     * @param number ??????
     * @return
     */
    public Boolean isOddNumber(int number) {
        if ((number & 1) == 1) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * ????????????
     *
     * @param currentTime
     * @return
     */
    public String addOneMinute(String currentTime) {
        String returnTime = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            Date date = df.parse(currentTime);
            date.setTime(date.getTime() + 1 * 60 * 1000);
            returnTime = df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnTime;
    }

    /**
     * ????????????
     *
     * @param currentTime
     * @return
     */
    public String minusOneMinute(String currentTime) {
        String returnTime = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            Date date = df.parse(currentTime);
            date.setTime(date.getTime() - 1 * 60 * 1000);
            returnTime = df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnTime;
    }

    @Override
    public void processHandlerMsg(Message msg) {
        switch (msg.what) {
            case GET_SCOPE:
                //?????????05:01@07:00
                ScopeBean data = (ScopeBean) msg.obj;
                ScopeBean.SugarpointBean sugarpoint = data.getSugarpoint();
                //empstomach	???	string	??????
                List<String> empstomach = sugarpoint.getEmpstomach();
                listSugar.get(0).setText(empstomach.get(0));
                listSugar.get(1).setText(empstomach.get(1));
                //aftbreakfast	???	string	?????????
                List<String> aftbreakfast = sugarpoint.getAftbreakfast();
                listSugar.get(2).setText(aftbreakfast.get(0));
                listSugar.get(3).setText(aftbreakfast.get(1));
                //beflunch	???	string	?????????
                List<String> beflunch = sugarpoint.getBeflunch();
                listSugar.get(4).setText(beflunch.get(0));
                listSugar.get(5).setText(beflunch.get(1));
                //aftlunch	???	string	?????????
                List<String> aftlunch = sugarpoint.getAftlunch();
                listSugar.get(6).setText(aftlunch.get(0));
                listSugar.get(7).setText(aftlunch.get(1));
                //befdinner	???	string	?????????
                List<String> befdinner = sugarpoint.getBefdinner();
                listSugar.get(8).setText(befdinner.get(0));
                listSugar.get(9).setText(befdinner.get(1));
                //aftdinner	???	string	?????????
                List<String> aftdinner = sugarpoint.getAftdinner();
                listSugar.get(10).setText(aftdinner.get(0));
                listSugar.get(11).setText(aftdinner.get(1));
                //befsleep	???	string	?????????
                List<String> befsleep = sugarpoint.getBefsleep();
                listSugar.get(12).setText(befsleep.get(0));
                listSugar.get(13).setText(befsleep.get(1));
                //inmorning	???	string	??????
                List<String> inmorning = sugarpoint.getInmorning();
                listSugar.get(14).setText(inmorning.get(0));
                listSugar.get(15).setText(inmorning.get(1));
                break;
        }
    }

    @Override
    public void callBack(Object object) {

    }
}