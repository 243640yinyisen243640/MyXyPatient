package com.vice.bloodpressure.ui.fragment.registration;


import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.widget.view.decoration.CommonItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.AppointmentOfTimeAdapter;
import com.vice.bloodpressure.base.fragment.BaseFragment;
import com.vice.bloodpressure.bean.AppointmentDoctorAllInfo;
import com.vice.bloodpressure.bean.AppointmentDoctorListBean;
import com.vice.bloodpressure.constant.DataFormatManager;
import com.vice.bloodpressure.net.OkHttpCallBack;
import com.vice.bloodpressure.net.XyUrl;
import com.vice.bloodpressure.utils.DataUtils;
import com.wei.android.lib.colorview.helper.ColorViewHelper;
import com.wei.android.lib.colorview.view.ColorRelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:  预约之按时间预约
 * 作者: LYD
 * 创建日期: 2019/10/21 10:18
 */
public class AppointmentOfTime1Fragment extends BaseFragment {
    private static final int GET_DATA = 10010;
    private static final int GET_NO_DATA = 30002;
    private static final int GET_MORE_DATA = 10086;
    private static final String TAG = "AppointmentOfTimeFragment";
    //七天开始
    @BindView(R.id.tv_appointment_list_date_time)
    TextView timeTextView;

    @BindView(R.id.tv_week_one)
    TextView tvWeekOne;
    @BindView(R.id.tv_day_one)
    TextView tvDayOne;
    @BindView(R.id.rl_one)
    ColorRelativeLayout rlOne;

    @BindView(R.id.tv_week_two)
    TextView tvWeekTwo;
    @BindView(R.id.tv_day_two)
    TextView tvDayTwo;
    @BindView(R.id.rl_two)
    ColorRelativeLayout rlTwo;

    @BindView(R.id.tv_week_three)
    TextView tvWeekThree;
    @BindView(R.id.tv_day_three)
    TextView tvDayThree;
    @BindView(R.id.rl_three)
    ColorRelativeLayout rlThree;

    @BindView(R.id.tv_week_four)
    TextView tvWeekFour;
    @BindView(R.id.tv_day_four)
    TextView tvDayFour;
    @BindView(R.id.rl_four)
    ColorRelativeLayout rlFour;

    @BindView(R.id.tv_week_five)
    TextView tvWeekFive;
    @BindView(R.id.tv_day_five)
    TextView tvDayFive;
    @BindView(R.id.rl_five)
    ColorRelativeLayout rlFive;

    @BindView(R.id.tv_week_six)
    TextView tvWeekSix;
    @BindView(R.id.tv_day_six)
    TextView tvDaySix;
    @BindView(R.id.rl_six)
    ColorRelativeLayout rlSix;

    @BindView(R.id.tv_week_seven)
    TextView tvWeekSeven;
    @BindView(R.id.tv_day_seven)
    TextView tvDaySeven;
    @BindView(R.id.rl_seven)
    ColorRelativeLayout rlSeven;

    //七天结束
    @BindView(R.id.srl_list)
    SmartRefreshLayout srlList;
    @BindView(R.id.rv_list_of_doctor)
    RecyclerView rvListOfDoctor;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;

    //分页
    private AppointmentOfTimeAdapter adapter;
    //总数据
    private List<AppointmentDoctorListBean> list;
    //上拉加载数据
    private List<AppointmentDoctorListBean> tempList;
    //上拉加载页数
    private int pageIndex = 2;
    //分页
    //七天数组

    private AppointmentDoctorAllInfo allInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_appointment_of_time1;
    }

    @Override
    protected void init(View rootView) {
        initRv();
        String currntTime = DataUtils.currentDateString(DataFormatManager.TIME_FORMAT_Y_M_D);
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(Calendar.DAY_OF_WEEK);


        getList(currntTime);
        initRefresh(currntTime);
        //设置初次选中

        switch (i) {
            case 1:
                setRlCheckAndUnCheck(6);
                setTvCheckAndUnCheck(4);
                break;
            case 2:
                setRlCheckAndUnCheck(0);
                setTvCheckAndUnCheck(0);
                break;
            case 3:
                setRlCheckAndUnCheck(1);
                setTvCheckAndUnCheck(1);
                break;
            case 4:
                setRlCheckAndUnCheck(2);
                setTvCheckAndUnCheck(2);
                break;
            case 5:
                setRlCheckAndUnCheck(3);
                setTvCheckAndUnCheck(3);
                break;
            case 6:
                setRlCheckAndUnCheck(4);
                setTvCheckAndUnCheck(4);
                break;
            case 7:
                setRlCheckAndUnCheck(5);
                setTvCheckAndUnCheck(5);
                break;
            default:
                break;

        }
    }

    /**
     * 设置间隔
     */
    private void initRv() {
        rvListOfDoctor.setLayoutManager(new GridLayoutManager(getPageContext(), 2));
        int spacing = ConvertUtils.dp2px(10);
        rvListOfDoctor.addItemDecoration(new CommonItemDecoration(spacing, spacing, spacing));
    }

    /**
     * 刷新
     *
     * @param time
     */
    private void initRefresh(String time) {
        srlList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                srlList.finishRefresh(2000);
                pageIndex = 2;
                getList(time);
            }
        });
        srlList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                srlList.finishLoadMore(2000);
                getMoreList(time);
            }
        });
    }


    /**
     * 获取更多
     *
     * @param time
     */
    private void getMoreList(String time) {
        int depid = getArguments().getInt("depid", 0);
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", pageIndex);
        map.put("schtime", time);
        map.put("depid", depid);
        XyUrl.okPost(XyUrl.GET_SCHEDULE_DOC, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                allInfo = JSONObject.parseObject(value, AppointmentDoctorAllInfo.class);
                tempList = allInfo.getList();
//                tempList = JSONObject.parseArray(value, AppointmentDoctorListBean.class);
                list.addAll(tempList);
                Message message = getHandlerMessage();
                message.what = GET_MORE_DATA;
                sendHandlerMessage(message);
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });
    }

    /**
     * 获取
     *
     * @param time
     */
    private void getList(String time) {
        int depid = getArguments().getInt("depid", 0);
        LoginBean user = (LoginBean) SharedPreferencesUtils.getBean(getActivity(), SharedPreferencesUtils.USER_INFO);
        HashMap map = new HashMap<>();
        map.put("schtime", time);
        map.put("depid", depid);
        map.put("page", 1);
        XyUrl.okPost(XyUrl.GET_SCHEDULE_DOC, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                allInfo = JSONObject.parseObject(value, AppointmentDoctorAllInfo.class);
                list = allInfo.getList();
                if (list != null && list.size() > 0) {
                    Message msg = getHandlerMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("time", time);
                    msg.setData(bundle);
                    msg.obj = list;
                    msg.what = GET_DATA;
                    sendHandlerMessage(msg);
                } else {
                    Message msg = getHandlerMessage();
                    msg.what = GET_NO_DATA;
                    sendHandlerMessage(msg);
                }
            }

            @Override
            public void onError(int error, String errorMsg) {
                Message msg = getHandlerMessage();
                msg.what = GET_NO_DATA;
                sendHandlerMessage(msg);
            }
        });
    }


    @Override
    public void processHandlerMsg(Message msg) {
        switch (msg.what) {
            case GET_NO_DATA:
                srlList.setVisibility(View.GONE);
                llEmpty.setVisibility(View.VISIBLE);
                break;
            case GET_DATA:
                srlList.setVisibility(View.VISIBLE);
                llEmpty.setVisibility(View.GONE);
                Bundle bundle = msg.getData();
                String time = bundle.getString("time");
                timeTextView.setText("(" + allInfo.getTime().get(0) + "至" + allInfo.getTime().get(6) + ")");
                list = allInfo.getList();
                adapter = new AppointmentOfTimeAdapter(getPageContext(), R.layout.item_appoint_doctor_of_time, list, time);
                rvListOfDoctor.setAdapter(adapter);
                break;
            case GET_MORE_DATA:
                pageIndex++;
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @OnClick({R.id.rl_one, R.id.rl_two, R.id.rl_three, R.id.rl_four, R.id.rl_five, R.id.rl_six, R.id.rl_seven})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_one:
                setRlCheckAndUnCheck(0);
                setTvCheckAndUnCheck(0);
                getAndRefresh(0);
                break;
            case R.id.rl_two:
                setRlCheckAndUnCheck(1);
                setTvCheckAndUnCheck(1);
                getAndRefresh(1);
                break;
            case R.id.rl_three:
                setRlCheckAndUnCheck(2);
                setTvCheckAndUnCheck(2);
                getAndRefresh(2);
                break;
            case R.id.rl_four:
                setRlCheckAndUnCheck(3);
                setTvCheckAndUnCheck(3);
                getAndRefresh(3);
                break;
            case R.id.rl_five:
                setRlCheckAndUnCheck(4);
                setTvCheckAndUnCheck(4);
                getAndRefresh(4);
                break;
            case R.id.rl_six:
                setRlCheckAndUnCheck(5);
                setTvCheckAndUnCheck(5);
                getAndRefresh(5);
                break;
            case R.id.rl_seven:
                setRlCheckAndUnCheck(6);
                setTvCheckAndUnCheck(6);
                getAndRefresh(6);
                break;
        }
    }

    /**
     * 获取并刷新
     *
     * @param position
     */
    private void getAndRefresh(int position) {

        if (allInfo!=null){
            String str = allInfo.getTime().get(position);
            getList(str);
            initRefresh(str);
        }

    }

    /**
     * 设置问题选中和取消选中
     *
     * @param position
     */
    private void setTvCheckAndUnCheck(int position) {
        List<TextView> listTvWeeks = new ArrayList<>();
        listTvWeeks.add(tvWeekOne);
        listTvWeeks.add(tvWeekTwo);
        listTvWeeks.add(tvWeekThree);
        listTvWeeks.add(tvWeekFour);
        listTvWeeks.add(tvWeekFive);
        listTvWeeks.add(tvWeekSix);
        listTvWeeks.add(tvWeekSeven);

        List<TextView> listTvDays = new ArrayList<>();
        listTvDays.add(tvDayOne);
        listTvDays.add(tvDayTwo);
        listTvDays.add(tvDayThree);
        listTvDays.add(tvDayFour);
        listTvDays.add(tvDayFive);
        listTvDays.add(tvDaySix);
        listTvDays.add(tvDaySeven);

        for (int i = 0; i < listTvWeeks.size(); i++) {
            if (position == i) {
                //选中
                TextView tvWeek = listTvWeeks.get(position);
                tvWeek.setTextColor(ColorUtils.getColor(R.color.white));
                TextView tvDay = listTvDays.get(position);
                tvDay.setTextColor(ColorUtils.getColor(R.color.white));
            } else {
                //取消选中
                TextView tvWeek = listTvWeeks.get(i);
                tvWeek.setTextColor(ColorUtils.getColor(R.color.gray_text));
                TextView tvDay = listTvDays.get(i);
                tvDay.setTextColor(ColorUtils.getColor(R.color.gray_text));
            }
        }

    }


    /**
     * 设置选中和取消选中
     *
     * @param position
     */
    private void setRlCheckAndUnCheck(int position) {
        List<ColorRelativeLayout> listRl = new ArrayList<>();
        listRl.add(rlOne);
        listRl.add(rlTwo);
        listRl.add(rlThree);
        listRl.add(rlFour);
        listRl.add(rlFive);
        listRl.add(rlSix);
        listRl.add(rlSeven);
        for (int i = 0; i < listRl.size(); i++) {
            if (position == i) {
                //选中
                ColorViewHelper checkHelper = listRl.get(position).getColorHelper();
                checkHelper.setBackgroundColorNormal(ColorUtils.getColor(R.color.color_week_selected));
                checkHelper.setCornerRadiusBottomLeftNormal(90);
                checkHelper.setCornerRadiusBottomRightNormal(90);
                checkHelper.setCornerRadiusTopLeftNormal(90);
                checkHelper.setCornerRadiusTopRightNormal(90);
            } else {
                //取消选中
                ColorViewHelper checkHelper = listRl.get(i).getColorHelper();
                checkHelper.setBackgroundColorNormal(ColorUtils.getColor(R.color.white));
                checkHelper.setCornerRadiusBottomLeftNormal(90);
                checkHelper.setCornerRadiusBottomRightNormal(90);
                checkHelper.setCornerRadiusTopLeftNormal(90);
                checkHelper.setCornerRadiusTopRightNormal(90);
            }
        }
    }


}
