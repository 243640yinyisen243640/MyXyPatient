package com.vice.bloodpressure.ui.activity.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;
import com.vice.bloodpressure.bean.PhysicalExaminationDoctorInfoAllInfo;
import com.vice.bloodpressure.bean.PhysicalExaminationDoctorInfoListBean;
import com.vice.bloodpressure.bean.ScheduleInfoPostBean;
import com.wei.android.lib.colorview.helper.ColorTextViewHelper;
import com.wei.android.lib.colorview.view.ColorTextView;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;

/**
 * 描述: 医生排班详情页面
 * 作者: LYD
 * 创建日期: 2019/10/26 15:59
 */
public class PhysicalExaminationDoctorInfo1Activity extends XYSoftUIBaseActivity implements View.OnClickListener {

    private ImageView imgHead;
    private TextView tvDoctorName;
    private TextView tvDoctorProfession;
    private TextView tvDoctorDesc;
    /**
     * 这个是显示时间的控件，但是点击时间是刷新
     */
    private TextView tvRefresh;
    private TextView tvMonth;
    private TextView tvWeekOne;
    private TextView tvWeekTwo;

    private TextView tvWeekThree;

    private TextView tvWeekFour;

    private TextView tvWeekFive;

    private TextView tvWeekSix;

    private TextView tvWeekSeven;

    private ColorTextView tvAmStateOne;
    private ColorTextView tvAmStateTwo;
    private ColorTextView tvAmStateThree;
    private ColorTextView tvAmStateFour;
    private ColorTextView tvAmStateFive;
    private ColorTextView tvAmStateSix;
    private ColorTextView tvAmStateSeven;
    private ColorTextView tvPmStateOne;
    private ColorTextView tvPmStateTwo;
    private ColorTextView tvPmStateThree;
    private ColorTextView tvPmStateFour;
    private ColorTextView tvPmStateFive;
    private ColorTextView tvPmStateSix;
    private ColorTextView tvPmStateSeven;


    private List<PhysicalExaminationDoctorInfoListBean> list;
    private PhysicalExaminationDoctorInfoAllInfo allInfo;

    private String docname;
    private String docuserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        docname = getIntent().getStringExtra("docname");
        topViewManager().titleTextView().setText(docname);
        topViewManager().titleTextView().setTextColor(ContextCompat.getColor(getPageContext(), R.color.black_text));
        docuserid = getIntent().getStringExtra("docuserid");
        containerView().addView(initView());
        initLister();
        //获取当前月份
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        tvMonth.setText(month + "月");

        getData();
    }

    private void initLister() {
        tvRefresh.setOnClickListener(this);
        tvAmStateOne.setOnClickListener(this);
        tvAmStateTwo.setOnClickListener(this);
        tvAmStateThree.setOnClickListener(this);
        tvAmStateFour.setOnClickListener(this);
        tvAmStateFive.setOnClickListener(this);
        tvAmStateSix.setOnClickListener(this);
        tvAmStateSeven.setOnClickListener(this);

        tvPmStateOne.setOnClickListener(this);
        tvPmStateTwo.setOnClickListener(this);
        tvPmStateThree.setOnClickListener(this);
        tvPmStateFour.setOnClickListener(this);
        tvPmStateFive.setOnClickListener(this);
        tvPmStateSix.setOnClickListener(this);
        tvPmStateSeven.setOnClickListener(this);
    }


    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.activity_appointment_date_doctor_info, null);
        imgHead = view.findViewById(R.id.iv_appointment_date_head);
        tvDoctorName = view.findViewById(R.id.tv_appointment_date_name);
        tvDoctorProfession = view.findViewById(R.id.tv_appointment_date_profession);
        tvDoctorDesc = view.findViewById(R.id.tv_appointment_date_desc);
        tvRefresh = view.findViewById(R.id.tv_appointment_date_time);
        tvMonth = view.findViewById(R.id.tv_appointment_date_month);
        tvWeekOne = view.findViewById(R.id.tv_appdate_week_one);
        tvWeekTwo = view.findViewById(R.id.tv_appdate_week_two);
        tvWeekThree = view.findViewById(R.id.tv_appdate_week_three);
        tvWeekFour = view.findViewById(R.id.tv_appdate_week_four);
        tvWeekFive = view.findViewById(R.id.tv_appdate_week_five);
        tvWeekSix = view.findViewById(R.id.tv_appdate_week_six);
        tvWeekSeven = view.findViewById(R.id.tv_appdate_week_seven);


        tvAmStateOne = view.findViewById(R.id.tv_appdate_am_state_one);
        tvAmStateTwo = view.findViewById(R.id.tv_appdate_am_state_two);
        tvAmStateThree = view.findViewById(R.id.tv_appdate_am_state_three);
        tvAmStateFour = view.findViewById(R.id.tv_appdate_am_state_four);
        tvAmStateFive = view.findViewById(R.id.tv_appdate_am_state_five);
        tvAmStateSix = view.findViewById(R.id.tv_appdate_am_state_six);
        tvAmStateSeven = view.findViewById(R.id.tv_appdate_am_state_seven);
        tvPmStateOne = view.findViewById(R.id.tv_appdate_pm_state_one);
        tvPmStateTwo = view.findViewById(R.id.tv_appdate_pm_state_two);
        tvPmStateThree = view.findViewById(R.id.tv_appdate_pm_state_three);
        tvPmStateFour = view.findViewById(R.id.tv_appdate_pm_state_four);
        tvPmStateFive = view.findViewById(R.id.tv_appdate_pm_state_five);
        tvPmStateSix = view.findViewById(R.id.tv_appdate_pm_state_six);
        tvPmStateSeven = view.findViewById(R.id.tv_appdate_pm_state_seven);

        return view;
    }

    private void getData() {
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.USER_INFO);

        Call<String> requestCall = DataManager.getAppDataInfo(docuserid, loginBean.getToken(), (call, response) -> {
            if (response.code == 200) {
                allInfo = (PhysicalExaminationDoctorInfoAllInfo) response.object;
                setDoctorInfo();
            } else if (30002 == response.code) {

            } else {
                ToastUtils.showShort(R.string.network_error);
            }

        }, (call, t) -> {
            ToastUtils.showShort(R.string.network_error);
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_appointment_date_time:
                getData();
                break;
            //上午
            case R.id.tv_appdate_am_state_one:
                checkIsCanGoAppointment(1, 0);
                break;
            case R.id.tv_appdate_am_state_two:
                checkIsCanGoAppointment(1, 1);
                break;
            case R.id.tv_appdate_am_state_three:
                checkIsCanGoAppointment(1, 2);
                break;
            case R.id.tv_appdate_am_state_four:
                checkIsCanGoAppointment(1, 3);
                break;
            case R.id.tv_appdate_am_state_five:
                checkIsCanGoAppointment(1, 4);
                break;
            case R.id.tv_appdate_am_state_six:
                checkIsCanGoAppointment(1, 5);
                break;
            case R.id.tv_appdate_am_state_seven:
                checkIsCanGoAppointment(1, 6);
                break;
            //下午
            case R.id.tv_appdate_pm_state_one:
                checkIsCanGoAppointment(2, 0);
                break;
            case R.id.tv_appdate_pm_state_two:
                checkIsCanGoAppointment(2, 1);
                break;
            case R.id.tv_appdate_pm_state_three:
                checkIsCanGoAppointment(2, 2);
                break;
            case R.id.tv_appdate_pm_state_four:
                checkIsCanGoAppointment(2, 3);
                break;
            case R.id.tv_appdate_pm_state_five:
                checkIsCanGoAppointment(2, 4);
                break;
            case R.id.tv_appdate_pm_state_six:
                checkIsCanGoAppointment(2, 5);
                break;
            case R.id.tv_appdate_pm_state_seven:
                checkIsCanGoAppointment(2, 6);
                break;
            default:
                break;
        }
    }

    /**
     * 校验是否可以跳转
     *
     * @param type  1:上午  2:下午
     * @param index
     */
    private void checkIsCanGoAppointment(int type, int index) {
        PhysicalExaminationDoctorInfoListBean data = allInfo.getList().get(index);
        switch (type) {
            case 1:
                int am = data.getAm();
                String clickam = data.getClick();
                if ("1".equals(clickam)&&1==am) {
                    goToAppointmentActivity(type, index);
                } else {
                    ToastUtils.showShort("不可预约");
                }
                break;
            case 2:
                int pm = data.getPm();
                String clickpm = data.getClick();
                if ("1".equals(clickpm)&&1==pm) {
                    goToAppointmentActivity(type, index);
                } else {
                    ToastUtils.showShort("不可预约");
                }
                break;
        }
    }


    /**
     * 跳转页面
     *
     * @param type
     * @param index
     */
    private void goToAppointmentActivity(int type, int index) {
        PhysicalExaminationDoctorInfoListBean data = list.get(index);

        //获取sid
        String sid = data.getSid() + "";
        //设置
        ScheduleInfoPostBean postBean = new ScheduleInfoPostBean();
        postBean.setSchday(data.getTime());
        postBean.setSid(sid);
        postBean.setType(type + "");
        Intent intent = new Intent(Utils.getApp(), AppointmentCheckActivity.class);
        intent.putExtra("data", postBean);
        startActivity(intent);
    }


    /**
     * 设置状态
     *
     * @param tv
     * @param state 1可预约  2不可预约
     */
    private void setTvState(ColorTextView tv, int state) {
        ColorTextViewHelper helper = tv.getColorHelper();
        // 1：不可预约  2：休息 3：约满
        if (1 == state) {
            tv.setText("预约");
            tv.setTag(1);
            helper.setTextColorNormal(ColorUtils.getColor(R.color.white));
            helper.setBackgroundColorNormal(ColorUtils.getColor(R.color.color_week_selected));
        } else if (3 == state) {
            tv.setText("约满");
            tv.setTag(2);
            helper.setTextColorNormal(ColorUtils.getColor(R.color.color_666));
            helper.setBackgroundColorNormal(ColorUtils.getColor(R.color.color_e5));
        } else {
            tv.setText("-");
            tv.setTag(3);
            helper.setTextColorNormal(ColorUtils.getColor(R.color.color_666));
            //            helper.setBackgroundColorNormal(ColorUtils.getColor(R.color.color_e5));
        }
    }

    /**
     * 设置医生排班信息
     */
    private void setDoctorInfo() {
        tvRefresh.setText("(" + allInfo.getTime().get(0) + "至" + allInfo.getTime().get(1) + ")");
        list = allInfo.getList();
        if (list != null && 7 == list.size()) {
            //设置除时间
            PhysicalExaminationDoctorInfoListBean data = list.get(0);
            String imgurl = data.getImgurl();
            String docname = data.getDocname();
            String doczhc = data.getDoczhc();
            String contents = data.getContents();
            Glide.with(Utils.getApp())
                    .load(imgurl)
                    .error(R.drawable.default_doctor_head)
                    .placeholder(R.drawable.default_doctor_head)
                    .into(imgHead);
            tvDoctorName.setText(docname);
            tvDoctorProfession.setText(doczhc);
            tvDoctorDesc.setText(contents);
            //设置预约状态
            for (int i = 0; i < 7; i++) {
                data = list.get(i);
                int am = data.getAm();
                int pm = data.getPm();
                switch (i) {
                    case 0:
                        setTvState(tvAmStateOne, am);
                        setTvState(tvPmStateOne, pm);
                        break;
                    case 1:
                        setTvState(tvAmStateTwo, am);
                        setTvState(tvPmStateTwo, pm);
                        break;
                    case 2:
                        setTvState(tvAmStateThree, am);
                        setTvState(tvPmStateThree, pm);
                        break;
                    case 3:
                        setTvState(tvAmStateFour, am);
                        setTvState(tvPmStateFour, pm);
                        break;
                    case 4:
                        setTvState(tvAmStateFive, am);
                        setTvState(tvPmStateFive, pm);
                        break;
                    case 5:
                        setTvState(tvAmStateSix, am);
                        setTvState(tvPmStateSix, pm);
                        break;
                    case 6:
                        setTvState(tvAmStateSeven, am);
                        setTvState(tvPmStateSeven, pm);
                        break;
                    default:
                        break;

                }
            }
        }
    }


}
