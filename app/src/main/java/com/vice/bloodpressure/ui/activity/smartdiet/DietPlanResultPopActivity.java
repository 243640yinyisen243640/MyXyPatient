package com.vice.bloodpressure.ui.activity.smartdiet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.eventbus.EventBusUtils;
import com.lyd.baselib.utils.eventbus.EventMessage;
import com.vice.bloodpressure.DataManager;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseHandlerActivity;
import com.vice.bloodpressure.bean.DietPlanAddSuccessBean;
import com.vice.bloodpressure.bean.DietPlanQuestionBean;
import com.vice.bloodpressure.constant.ConstantParam;
import com.vice.bloodpressure.net.OkHttpCallBack;
import com.vice.bloodpressure.net.XyUrl;
import com.vice.bloodpressure.utils.RxTimerUtils;
import com.vice.bloodpressure.utils.SPUtils;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:bean 答题数据
 */
public class DietPlanResultPopActivity extends BaseHandlerActivity implements View.OnClickListener {
    @BindView(R.id.tv_smart)
    TextView smartTv;
    @BindView(R.id.tv_my_select)
    TextView selfTv;
    @BindView(R.id.ll_user_smart_diet)
    LinearLayout allLi;
    @BindView(R.id.ll_user_smart_gif)
    LinearLayout gufLi;

    private DietPlanQuestionBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bean = (DietPlanQuestionBean) getIntent().getSerializableExtra("bean");
        setTitle("选择方式");
        smartTv.setOnClickListener(this);
        selfTv.setOnClickListener(this);
    }

    @Override
    protected View addContentLayout() {
        return getLayoutInflater().inflate(R.layout.popup_smart_diet_select, contentLayout, false);
    }

    /***
     * 清除保存id
     */
    private void toMySelect() {
        HashMap<String, Object> map = new HashMap<>();
        XyUrl.okPostSave(XyUrl.CLEAR_IDS, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                SPUtils.putBean("Diet_Question", bean);
                Intent intent = new Intent(getPageContext(), DietPlanTwoStepsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });
    }

    private void loadGif() {
        try {
            // 如果加载的是gif动图，第一步需要先将gif动图资源转化为GifDrawable
            // 将gif图资源转化为GifDrawable
            GifImageView imgGif = findViewById(R.id.img_gif);
            GifDrawable gifDrawable = new GifDrawable(Utils.getApp().getResources(), R.drawable.smart_diet_loading);
            // gif1加载一个动态图gif
            imgGif.setImageDrawable(gifDrawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toDoSubmit() {
        allLi.setVisibility(View.GONE);
        gufLi.setVisibility(View.VISIBLE);
        loadGif();
        String sex = bean.getSex();
        String height = bean.getHeight();
        String weight = bean.getWeight();
        String profession = bean.getProfession();
        String dn = bean.getDn();
        String dn_type = bean.getDn_type();
        Log.i("yys",sex+height+weight+profession+dn+dn_type);
        LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(this, SharedPreferencesUtils.USER_INFO);
        Call<String> requestCall = DataManager.dietPlanAdd(sex, height, weight, profession, dn, dn_type,loginBean.getToken(), (call, response) -> {
            RxTimerUtils rxTimer = new RxTimerUtils();
            Log.i("yys", "response.code==" + response.code);
            if (response.code == 200) {
                DietPlanAddSuccessBean data = (DietPlanAddSuccessBean) response.object;
                rxTimer.timer(3 * 1000, number -> {
                    int id = data.getId();
                    //清空答题页面
                    finish();
                    //跳转详情
                    Intent intent = new Intent(getPageContext(), DietPlanResultActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                });
                EventBusUtils.post(new EventMessage<>(ConstantParam.HOME_DIET_QUESTION_SUBMIT));
                Log.i("yys", "event==发送");
            } else {
                finish();
                rxTimer.timer(3 * 1000, number -> {
                    Intent intent = new Intent(getPageContext(), DietPlanBaseInfoActivity.class);
                    intent.putExtra("type", "notMatch");
                    startActivity(intent);
                });
                EventBusUtils.post(new EventMessage<>(ConstantParam.HOME_DIET_QUESTION_SUBMIT));
                Log.i("yys", "event==发送");
            }


        }, (call, t) -> {

        });

    }

    @Override
    public void processHandlerMsg(Message msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_smart:
                toDoSubmit();
                break;
            case R.id.tv_my_select:
                toMySelect();
                break;
            default:
                break;
        }
    }
}
