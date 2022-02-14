package com.vice.bloodpressure.ui.activity.hospital;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.engine.GlideImageEngine;
import com.maning.imagebrowserlibrary.MNImageBrowser;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseHandlerActivity;
import com.vice.bloodpressure.bean.MakeDetailsBean;
import com.vice.bloodpressure.net.OkHttpCallBack;
import com.vice.bloodpressure.net.XyUrl;
import com.vice.bloodpressure.utils.ImgViewUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yicheng on 2018/7/10.
 * 查看预约详情
 */

public class MakeDetailsActivity extends BaseHandlerActivity implements View.OnClickListener {

    private static final int GET_DATA = 0x003;
    private String id;
    private LoginBean user;

    private LinearLayout statusLinearLayout;
    private TextView statusTextView;

    private TextView tvMakeTypeShow;
    private TextView tvMakeNicknameShow;
    private TextView tvMakeAgeShow;
    private TextView tvMakeSexShow;
    private TextView tvMakePhone;
    private TextView tvMakeDiagnosisShow;
    private TextView tvMakeStayShow;
    private TextView tvMakeTimeShow;
    private TextView tvResult;
    private ImageView ivMakeOneShow;
    private ImageView ivMakeTwoShow;
    private ImageView ivMakeThreeShow;
    private MakeDetailsBean makeDetails;
    private LinearLayout backLinearLayout;

    @Override
    public void processHandlerMsg(Message msg) {
        switch (msg.what) {
            case GET_DATA:
                makeDetails = (MakeDetailsBean) msg.obj;
                //1：预约中 2：预约成功 3：预约失败
                if ("1".equals(makeDetails.getStatus())) {
                    backLinearLayout.setVisibility(View.GONE);
                    statusLinearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.appointment_doing));
                    statusTextView.setText(R.string.appointment_doing);
                } else if ("2".equals(makeDetails.getStatus())) {
                    statusLinearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.appointment_success));
                    statusTextView.setText(R.string.appointment_success_title);
                    backLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    statusLinearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.appointment_failes));
                    statusTextView.setText(R.string.appointment_failed);
                    backLinearLayout.setVisibility(View.VISIBLE);
                }

                if (makeDetails.getType().equals("1")) {
                    tvMakeTypeShow.setText("初次");
                } else if (makeDetails.getType().equals("2")) {
                    tvMakeTypeShow.setText("再次");
                }
                tvMakeAgeShow.setText(makeDetails.getAge());
                String sex = makeDetails.getSex();
                if ("1".equals(sex)) {
                    tvMakeSexShow.setText("男");
                } else {
                    tvMakeSexShow.setText("女");
                }
                tvMakeNicknameShow.setText(makeDetails.getName());
                tvMakePhone.setText(makeDetails.getTelephone());
                tvMakeDiagnosisShow.setText(makeDetails.getDescribe());
                tvMakeTimeShow.setText(makeDetails.getTime());
                tvMakeStayShow.setText(makeDetails.getResult());
                tvResult.setText(makeDetails.getReason());
                if (makeDetails.getBlpic() != null && makeDetails.getBlpic().length > 0) {
                    if (makeDetails.getBlpic().length == 1) {
                        ImgViewUtils.loadRoundImage(this,R.drawable.hospital_appointmen_photo_load,makeDetails.getBlpic()[0],ivMakeOneShow);
//                        ImageLoaderUtil.loadImgUrl(ivMakeOneShow, makeDetails.getBlpic()[0]);

                        ivMakeOneShow.setOnClickListener(this);

                        ivMakeTwoShow.setVisibility(View.INVISIBLE);
                        ivMakeThreeShow.setVisibility(View.INVISIBLE);
                    } else if (makeDetails.getBlpic().length == 2) {

                        ImgViewUtils.loadRoundImage(this,R.drawable.hospital_appointmen_photo_load,makeDetails.getBlpic()[0],ivMakeOneShow);
                        ImgViewUtils.loadRoundImage(this,R.drawable.hospital_appointmen_photo_load,makeDetails.getBlpic()[1],ivMakeTwoShow);


//                        ImageLoaderUtil.loadImgUrl(ivMakeOneShow, makeDetails.getBlpic()[0]);
//                        ImageLoaderUtil.loadImgUrl(ivMakeTwoShow, makeDetails.getBlpic()[1]);

                        ivMakeOneShow.setOnClickListener(this);
                        ivMakeTwoShow.setOnClickListener(this);

                        ivMakeThreeShow.setVisibility(View.INVISIBLE);
                    } else if (makeDetails.getBlpic().length == 3) {

                        ImgViewUtils.loadRoundImage(this,R.drawable.hospital_appointmen_photo_load,makeDetails.getBlpic()[0],ivMakeOneShow);
                        ImgViewUtils.loadRoundImage(this,R.drawable.hospital_appointmen_photo_load,makeDetails.getBlpic()[1],ivMakeTwoShow);
                        ImgViewUtils.loadRoundImage(this,R.drawable.hospital_appointmen_photo_load,makeDetails.getBlpic()[2],ivMakeThreeShow);

//
                        //                        ImageLoaderUtil.loadImgUrl(ivMakeOneShow, makeDetails.getBlpic()[0]);
                        //                        ImageLoaderUtil.loadImgUrl(ivMakeTwoShow, makeDetails.getBlpic()[1]);
                        //                        ImageLoaderUtil.loadImgUrl(ivMakeThreeShow, makeDetails.getBlpic()[2]);

                        ivMakeOneShow.setOnClickListener(this);
                        ivMakeTwoShow.setOnClickListener(this);
                        ivMakeThreeShow.setOnClickListener(this);
                    }
                }

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.make_details));
        user = (LoginBean) SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.USER_INFO);
        id = getIntent().getStringExtra("id");
        initViews();
        getData();
    }

    /**
     * 预约详情
     */
    private void getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("token", user.getToken());
        XyUrl.okPost(XyUrl.GET_INFO, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                makeDetails = JSONObject.parseObject(value, MakeDetailsBean.class);
                Message msg = Message.obtain();
                msg.obj = makeDetails;
                msg.what = GET_DATA;
                sendHandlerMessage(msg);
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });
    }

    private void initViews() {
        statusLinearLayout = findViewById(R.id.ll_appointment_status);
        statusTextView = findViewById(R.id.tv_appointment_status);
        tvMakeTypeShow = findViewById(R.id.tv_make_type_show);
        tvMakeAgeShow = findViewById(R.id.tv_make_age_show);
        tvMakeNicknameShow = findViewById(R.id.tv_make_hos_nickname_show);
        tvMakeSexShow = findViewById(R.id.tv_make_sex_show);
        tvMakePhone = findViewById(R.id.tv_make_phone);
        tvMakeDiagnosisShow = findViewById(R.id.tv_make_diagnosis_show);
        tvMakeStayShow = findViewById(R.id.tv_make_stay_show);
        tvMakeTimeShow = findViewById(R.id.tv_make_time_show);
        tvResult = findViewById(R.id.tv_result);
        ivMakeOneShow = findViewById(R.id.iv_make_one_show);
        ivMakeTwoShow = findViewById(R.id.iv_make_two_show);
        ivMakeThreeShow = findViewById(R.id.iv_make_three_show);
        backLinearLayout = findViewById(R.id.ll_appointment_doctor_back);
    }

    @Override
    protected View addContentLayout() {
        return getLayoutInflater().inflate(R.layout.activity_make_show, contentLayout, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_make_one_show:
                MNImageBrowser.with(MakeDetailsActivity.this)
                        .setImageEngine(new GlideImageEngine())
                        .setImageUrl(makeDetails.getBlpic()[0])
                        .setIndicatorHide(false)
                        .setFullScreenMode(true)
                        .show(v);
                break;
            case R.id.iv_make_two_show:
                MNImageBrowser.with(MakeDetailsActivity.this)
                        .setImageEngine(new GlideImageEngine())
                        .setImageUrl(makeDetails.getBlpic()[1])
                        .setIndicatorHide(false)
                        .setFullScreenMode(true)
                        .show(v);
                break;
            case R.id.iv_make_three_show:
                MNImageBrowser.with(MakeDetailsActivity.this)
                        .setImageEngine(new GlideImageEngine())
                        .setImageUrl(makeDetails.getBlpic()[2])
                        .setIndicatorHide(false)
                        .setFullScreenMode(true)
                        .show(v);
                break;
        }
    }
}
