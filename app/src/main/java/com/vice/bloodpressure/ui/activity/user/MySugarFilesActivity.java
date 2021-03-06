package com.vice.bloodpressure.ui.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.allen.library.RxHttpUtils;
import com.allen.library.bean.BaseData;
import com.allen.library.interceptor.Transformer;
import com.allen.library.observer.CommonObserver;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.baselib.utils.eventbus.EventMessage;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.HealthArchiveGroupLevelAdapter;
import com.vice.bloodpressure.adapter.MySugarFilesMedicineHistoryListAdapter;
import com.vice.bloodpressure.base.activity.BaseHandlerEventBusActivity;
import com.vice.bloodpressure.bean.HealthArchiveGroupLevelOneBean;
import com.vice.bloodpressure.bean.HealthArchiveGroupLevelZeroBean;
import com.vice.bloodpressure.bean.PersonalRecordBean;
import com.vice.bloodpressure.bean.PersonalRecordMedicineHistoryBean;
import com.vice.bloodpressure.constant.ConstantParam;
import com.vice.bloodpressure.constant.DataFormatManager;
import com.vice.bloodpressure.imp.AdapterViewClickListener;
import com.vice.bloodpressure.net.OkHttpCallBack;
import com.vice.bloodpressure.net.Service;
import com.vice.bloodpressure.net.XyUrl;
import com.vice.bloodpressure.ui.activity.healthrecordadd.PharmacyAddActivity;
import com.vice.bloodpressure.utils.DataUtils;
import com.vice.bloodpressure.utils.DialogUtils;
import com.vice.bloodpressure.utils.PickerUtils;
import com.vice.bloodpressure.utils.TimeFormatUtils;
import com.vice.bloodpressure.utils.TurnsUtils;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ??????: ???????????????
 * ??????: LYD
 * ????????????: 2019/5/13 16:59
 */
public class MySugarFilesActivity extends BaseHandlerEventBusActivity implements AdapterViewClickListener {
    private static final String TAG = "MySugarFilesActivity";
    private static final int GET_DATA = 10010;
    private static final int GET_DATA_MEDICINE_HISTORY = 10011;
    private static final int GET_DATA_MEDICINE_HISTORY_ERROR = 10012;
    @BindView(R.id.rv_health_archive)
    RecyclerView rvHealthArchive;
    //?????????
    @BindView(R.id.img_medicine_history)
    ImageView imgMedicineHistory;
    @BindView(R.id.ll_medicine_history)
    LinearLayout llMedicineHistory;
    @BindView(R.id.rv_medicine_history)
    RecyclerView rvMedicineHistory;
    @BindView(R.id.el_medicine_history)
    ExpandableLayout elMedicineHistory;


    //???????????????
    CityPickerView mCityPickerView = new CityPickerView();
    private PersonalRecordBean personalRecordBean;
    private int sexInt;
    private HealthArchiveGroupLevelAdapter adapter;
    private MySugarFilesMedicineHistoryListAdapter medicineHistoryListAdapter;
    private ArrayList<MultiItemEntity> multiItemEntityArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("????????????");
        getData();
        //????????????????????????
        mCityPickerView.init(this);
    }

    @Override
    protected View addContentLayout() {
        View layout = getLayoutInflater().inflate(R.layout.activity_my_files, contentLayout, false);
        return layout;
    }

    /**
     * ??????????????????
     */
    private void getData() {
        LoginBean userLogin = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", userLogin.getToken());
        XyUrl.okPost(XyUrl.PERSONAL_RECORD, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                //????????????
                personalRecordBean = JSONObject.parseObject(value, PersonalRecordBean.class);
                Message message = getHandlerMessage();
                message.what = GET_DATA;
                message.obj = personalRecordBean;
                sendHandlerMessage(message);
                //????????????
                getHistory();
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });
    }

    /**
     * ???????????????
     */
    private void getHistory() {
        LoginBean userLogin = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", userLogin.getToken());
        XyUrl.okPost(XyUrl.PERSONAL_RECORD_MEDICINE_HISTORY, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                List<PersonalRecordMedicineHistoryBean> historyBean = JSONObject.parseArray(value, PersonalRecordMedicineHistoryBean.class);
                Message message = getHandlerMessage();
                message.what = GET_DATA_MEDICINE_HISTORY;
                message.obj = historyBean;
                sendHandlerMessage(message);
            }

            @Override
            public void onError(int error, String errorMsg) {
                Message message = getHandlerMessage();
                message.what = GET_DATA_MEDICINE_HISTORY_ERROR;
                sendHandlerMessage(message);
            }
        });
    }

    /***
     * ????????????
     * @param data
     * @param list
     */
    private void addData(PersonalRecordBean data, List<PersonalRecordMedicineHistoryBean> list) {
        List<HealthArchiveGroupLevelZeroBean> lv0 = new ArrayList<>();
        //??????????????????
        String[] stringRes = new String[]{"??????????????????", "????????????", "????????????",
                "???????????????", "?????????/?????????", "????????????"};
        for (int i = 0; i < stringRes.length; i++) {
            lv0.add(new HealthArchiveGroupLevelZeroBean(stringRes[i]));
        }
        //??????????????????
        //??????????????????
        String[] stringResOneLeft = new String[]{
                "????????????", "??????", "??????", "????????????",
                "??????", "???????????????", "?????????????????????", "??????",
                "??????", "????????????", "????????????", "????????????",
                "????????????", "??????", "??????", "??????????????????",
                "????????????", "????????????", "????????????", "???????????????",
                "?????????????????????", "??????", "????????????"};

        String nickname = data.getNickname();

        String petname = data.getPetname();
        String sex = data.getSex() + "";
        String birthTime = getTime(data.getBirthtime());

        String minzu = data.getMinzu();
        String diabeteslei = data.getDiabeteslei() + "";
        long diabetesleitime = data.getDiabetesleitime();
        String diabetesleiTime = getTime(diabetesleitime);

        String smoke = data.getSmoke() + "";

        String drink = data.getDrink() + "";
        String culture = data.getCulture() + "";
        String perofession = data.getProfession() + "";
        String marriage = data.getMarriage() + "";

        String bedrid = data.getBedrid() + "";
        String nativeplace = data.getNativeplace();
        String address = data.getAddress();
        String payWay = data.getZhifufangshi() + "";

        String idCard = data.getIdcard();
        String jzkahao = data.getJzkahao();
        String duju = data.getDuju() + "";
        String gxyzhenduan = data.getGxyzhenduan() + "";

        String gxyTime = getTime(data.getGxytime());
        String gestation = data.getGestation() + "";
        String gestationTime = getTime(data.getGestationtime());


        String[] stringResOneRight = new String[]{
                nickname, petname, sex, birthTime,
                minzu, diabeteslei, diabetesleiTime, smoke,
                drink, culture, perofession, marriage,
                bedrid, nativeplace, address, payWay,
                idCard, jzkahao, duju, gxyzhenduan,
                gxyTime, gestation, gestationTime};

        for (int i = 0; i < stringResOneLeft.length; i++) {
            HealthArchiveGroupLevelOneBean lv1 = null;
            switch (i) {
                //??????
                case 2:
                    String sexGet = stringResOneRight[i];
                    if ("1".equals(sexGet)) {
                        lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "???");
                        lv0.get(0).addSubItem(lv1);
                    } else {
                        lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "???");
                        lv0.get(0).addSubItem(lv1);
                    }
                    break;
                //??????????????? 1???1???  2???2???  3?????????  4??????
                case 5:
                    String tnbType = stringResOneRight[i];
                    switch (tnbType) {
                        case "0":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "???");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        case "1":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "1???");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        case "2":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "2???");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        case "3":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "??????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        case "4":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "??????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        default:
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "??????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                    }
                    break;
                case 7://??????   1??? 2???
                case 8://??????
                case 12://????????????
                case 18://??????
                case 21://??????
                    String yesOrNo = stringResOneRight[i];
                    if ("1".equals(yesOrNo)) {
                        lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "???");
                        lv0.get(0).addSubItem(lv1);
                    } else if ("2".equals(yesOrNo)) {
                        lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "???");
                        lv0.get(0).addSubItem(lv1);
                    } else {
                        lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "?????????");
                        lv0.get(0).addSubItem(lv1);
                    }
                    break;
                case 9://???????????? 1 ??????????????? 2?????? 3?????? 4???????????????
                    String cultureGet = stringResOneRight[i];
                    switch (cultureGet) {
                        case "1":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "???????????????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        case "2":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "??????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        case "3":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "??????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        case "4":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "???????????????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        default:
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "?????????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                    }
                    break;
                case 10://1 ????????? 2 ????????? 3 ??????
                    String profession = stringResOneRight[i];
                    switch (profession) {
                        case "1":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "?????????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        case "2":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "?????????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        case "3":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "?????????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        default:
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "?????????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                    }
                    break;
                case 11://1?????? 2??????
                    String marriageGet = stringResOneRight[i];
                    if ("1".equals(marriageGet)) {
                        lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "??????");
                        lv0.get(0).addSubItem(lv1);
                    } else if ("2".equals(marriageGet)) {
                        lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "??????");
                        lv0.get(0).addSubItem(lv1);
                    } else {
                        lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "?????????");
                        lv0.get(0).addSubItem(lv1);
                    }
                    break;
                case 15://??????????????????
                    String pay = stringResOneRight[i];
                    switch (pay) {
                        case "1":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "????????????????????????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        case "2":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "??????????????????????????????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        case "3":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "????????????????????????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        case "4":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "????????????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        case "5":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "????????????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        case "6":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "????????????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        case "7":
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "??????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                        default:
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "?????????");
                            lv0.get(0).addSubItem(lv1);
                            break;
                    }
                    break;
                case 19: //???????????????
                    String gxyzhenduanGet = stringResOneRight[i];
                    if ("1".equals(gxyzhenduanGet)) {
                        lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "?????????");
                        lv0.get(0).addSubItem(lv1);
                    } else if ("2".equals(gxyzhenduanGet)) {
                        lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "?????????");
                        lv0.get(0).addSubItem(lv1);
                    } else if ("3".equals(gxyzhenduanGet)) {
                        lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "?????????");
                        lv0.get(0).addSubItem(lv1);
                    } else {
                        lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], "?????????");
                        lv0.get(0).addSubItem(lv1);
                    }
                    break;
                default:
                    lv1 = new HealthArchiveGroupLevelOneBean(stringResOneLeft[i], stringResOneRight[i]);
                    lv0.get(0).addSubItem(lv1);
                    break;
            }
        }
        //??????????????????
        String[] stringResTwoLeft = new String[]{
                "??????", "??????", "BMI", "??????",
                "??????", "?????????", "?????????", "?????????",
                "????????????"};
        //????????????bmi
        SPStaticUtils.put("height", data.getHeight());
        SPStaticUtils.put("weight", data.getWeight());
        double height = TurnsUtils.getDouble(data.getHeight(), 1);
        double weight = TurnsUtils.getDouble(data.getWeight(), 1);
        double heightM = height * 0.01;
        double bmi;
        String bmiStr;
        if ("?????????  ".equals(data.getWeight()) || "?????????  ".equals(data.getHeight())) {
            bmiStr = "";
        } else {
            bmi = weight / (heightM * heightM);
            //??????????????????
            String result = String.format("%.1f", bmi);
            bmiStr = result;
        }
        //???????????? ?????????
        SPStaticUtils.put("waistline", data.getWaistline());
        SPStaticUtils.put("hipline", data.getHipline());
        double Waistline = TurnsUtils.getDouble(data.getWaistline(), 1);
        double Hipline = TurnsUtils.getDouble(data.getHipline(), 1);
        double wCompareH;
        String wCompareHStr;
        if ("?????????  ".equals(data.getWaistline()) || "?????????  ".equals(data.getHipline())) {
            wCompareHStr = "";
        } else {
            wCompareH = Waistline / Hipline;
            wCompareHStr = String.format("%.1f", wCompareH);
        }
        String[] stringResTwoRight = new String[]{
                data.getHeight(), data.getWeight(), bmiStr, data.getWaistline(),
                data.getHipline(), wCompareHStr, data.getSystolic(), data.getDiastolic(),
                data.getHeartrate()};
        String[] stringResTwoUnits = Utils.getApp().getResources().getStringArray(R.array.my_file_physique_unit);
        for (int i = 0; i < stringResTwoLeft.length; i++) {
            HealthArchiveGroupLevelOneBean lv1 = new HealthArchiveGroupLevelOneBean(stringResTwoLeft[i], stringResTwoRight[i], stringResTwoUnits[i]);
            lv0.get(1).addSubItem(lv1);
        }
        //??????????????????
        String[] stringResThreeLeft = new String[]{
                "OGTT2h??????", "H b A 1 c", "????????????", "????????????",
                "?????????2h????????????", "????????????", "????????????", "???????????????????????????"};
        String[] stringResThreeRight = new String[]{
                data.getXtogtt2h(), data.getXthbalc(), data.getXtsuiji(), data.getXtkongfu(),
                data.getXtcaihou(), data.getXtyejian(), data.getXtshuiqian(),
                data.getXtdi()};
        String[] stringResThreeUnits = Utils.getApp().getResources().getStringArray(R.array.my_file_blood_sugar);
        for (int i = 0; i < stringResThreeLeft.length; i++) {
            if (i == 7) {
                String s = stringResThreeRight[i];
                if ("1".equals(s)) {
                    HealthArchiveGroupLevelOneBean lv1 = new HealthArchiveGroupLevelOneBean(stringResThreeLeft[i], "???");
                    lv0.get(2).addSubItem(lv1);
                } else {
                    HealthArchiveGroupLevelOneBean lv1 = new HealthArchiveGroupLevelOneBean(stringResThreeLeft[i], "???");
                    lv0.get(2).addSubItem(lv1);
                }
            } else {
                HealthArchiveGroupLevelOneBean lv1 = new HealthArchiveGroupLevelOneBean(stringResThreeLeft[i], stringResThreeRight[i], stringResThreeUnits[i]);
                lv0.get(2).addSubItem(lv1);
            }
        }
        //???????????????   ????????????????????????  0???  1??????  2??????
        String[] stringResFourLeft = new String[]{
                "????????????(TC)", "????????????", "??????????????????(LDL-C)", "???????????????(ALT)",
                "???????????????(AST)", "????????????(T-BIL)", "??????????????????(HDL-C)", "???????????????(ALP)",
                "?????????(?????????)", "24??????????????????(24hUAE)", "????????????/??????(ACR)", "????????????(UAE)", "?????????(BUN)", "?????????????????????(Ccr)"};
        String[] stringResFourRight = new String[]{
                data.getSytc(), data.getSytg(), data.getSyldl(), data.getSyalt(),
                data.getSyast(), data.getSytbil(), data.getSyhdl(), data.getSyalp(),
                data.getSyncg(), data.getSyhuae(), data.getSyacr(), data.getSyuae(),
                data.getSynsd(), data.getSynsjg(), data.getSyxqjg()};
        String[] stringResFourUnits = Utils.getApp().getResources().getStringArray(R.array.my_file_laboratory_unit);
        for (int i = 0; i < stringResFourLeft.length; i++) {
            HealthArchiveGroupLevelOneBean lv1 = new HealthArchiveGroupLevelOneBean(stringResFourLeft[i], stringResFourRight[i], stringResFourUnits[i]);
            lv0.get(3).addSubItem(lv1);
        }
        sexInt = data.getSex();
        if (1 == sexInt) {
            HealthArchiveGroupLevelOneBean lv1 = new HealthArchiveGroupLevelOneBean("????????????(SRC)", data.getSyxqjg(), "mmol/L");
            lv0.get(3).addSubItem(lv1);
        } else {
            HealthArchiveGroupLevelOneBean lv1 = new HealthArchiveGroupLevelOneBean("????????????(SRC)", data.getSyxqjgg(), "mmol/L");
            lv0.get(3).addSubItem(lv1);
        }
        //?????????/?????????
        String[] stringResFiveLeft = new String[]{
                "???????????????", "????????????????????????", "?????????????????????", "???????????????????????????",
                "????????????", "????????????????????????", "????????????????????????"};
        String[] stringResFiveRight = new String[]{data.getNephropathy() + "", data.getRetina() + "", data.getNerve() + "", data.getLegs() + "",
                data.getDiabeticfoot() + "", data.getKetoacidosis() + "", data.getHypertonic() + ""};
        //1 ????????? 2 ????????? 3 ?????????
        for (int i = 0; i < stringResFiveLeft.length; i++) {
            String yesOrNo = stringResFiveRight[i];
            if ("1".equals(yesOrNo)) {
                HealthArchiveGroupLevelOneBean lv1 = new HealthArchiveGroupLevelOneBean(stringResFiveLeft[i], "?????????");
                lv0.get(4).addSubItem(lv1);
            } else if ("2".equals(yesOrNo)) {
                HealthArchiveGroupLevelOneBean lv1 = new HealthArchiveGroupLevelOneBean(stringResFiveLeft[i], "?????????");
                lv0.get(4).addSubItem(lv1);
            } else {
                HealthArchiveGroupLevelOneBean lv1 = new HealthArchiveGroupLevelOneBean(stringResFiveLeft[i], "?????????");
                lv0.get(4).addSubItem(lv1);
            }
        }


        //????????????
        String[] stringResSixLeft = new String[]{
                "?????????", "????????????", "?????????", "????????????",
                "??????????????????", "????????????????????????"};
        String[] stringResSixRight = new String[]{
                data.getHighblood() + "", data.getHlp() + "", data.getCoronary() + "", data.getCerebrovascular() + "",
                data.getDiabetes() + "", data.getAngiocarpy() + ""};
        for (int i = 0; i < stringResSixLeft.length; i++) {
            HealthArchiveGroupLevelOneBean lv1 = null;
            switch (i) {
                case 0:
                    switch (data.getHighblood()) {
                        case 1://1 ????????? 2 ????????? 3 ?????????
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "?????????");
                            lv0.get(5).addSubItem(lv1);
                            break;
                        case 2:
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "?????????");
                            lv0.get(5).addSubItem(lv1);
                            break;
                        case 3:
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "?????????");
                            lv0.get(5).addSubItem(lv1);
                            break;
                        default:
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "?????????");
                            lv0.get(5).addSubItem(lv1);
                            break;
                    }
                    break;
                case 1:
                    switch (data.getHighblood()) {
                        case 1://1 ????????? 2 ????????? 3 ?????????
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "?????????");
                            lv0.get(5).addSubItem(lv1);
                            break;
                        case 2:
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "?????????");
                            lv0.get(5).addSubItem(lv1);
                            break;
                        case 3:
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "?????????");
                            break;
                        default:
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "?????????");
                            lv0.get(5).addSubItem(lv1);
                            break;
                    }
                    break;
                case 2:
                    switch (data.getHighblood()) {
                        case 1://1 ????????? 2 ????????? 3 ?????????
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "?????????");
                            lv0.get(5).addSubItem(lv1);
                            break;
                        case 2:
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "?????????");
                            lv0.get(5).addSubItem(lv1);
                            break;
                        case 3:
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "?????????");
                            lv0.get(5).addSubItem(lv1);
                            break;
                        default:
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "?????????");
                            lv0.get(5).addSubItem(lv1);
                            break;
                    }
                    break;
                case 3:
                    switch (data.getHighblood()) {
                        case 1://1 ????????? 2 ????????? 3 ?????????
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "?????????");
                            lv0.get(5).addSubItem(lv1);
                            break;
                        case 2:
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "?????????");
                            lv0.get(5).addSubItem(lv1);
                            break;
                        case 3:
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "?????????");
                            lv0.get(5).addSubItem(lv1);
                            break;
                        default:
                            lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "?????????");
                            lv0.get(5).addSubItem(lv1);
                            break;
                    }
                    break;
                case 4:
                case 5:
                    String yesOrNo = stringResSixRight[i];
                    if ("1".equals(yesOrNo)) {
                        lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "???");
                        lv0.get(5).addSubItem(lv1);
                    } else {
                        lv1 = new HealthArchiveGroupLevelOneBean(stringResSixLeft[i], "???");
                        lv0.get(5).addSubItem(lv1);
                    }
                    break;
                default:
                    break;
            }
        }

        //??????0?????????
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getPageContext());
        multiItemEntityArrayList = new ArrayList<>(lv0);
        adapter = new HealthArchiveGroupLevelAdapter(this, multiItemEntityArrayList, this);


        rvHealthArchive.setAdapter(adapter);
        //??????????????????
        rvHealthArchive.setItemViewCacheSize(23);
        rvHealthArchive.setLayoutManager(linearLayoutManager);


        //???????????????
        medicineHistoryListAdapter = new MySugarFilesMedicineHistoryListAdapter(list);
        rvMedicineHistory.setLayoutManager(new LinearLayoutManager(getPageContext()));
        rvMedicineHistory.setAdapter(medicineHistoryListAdapter);


        medicineHistoryListAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                toDelMedicineHistory(list, position);
                return true;
            }
        });
        medicineHistoryListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                Intent intent = new Intent(getPageContext(), PharmacyAddActivity.class);
                PersonalRecordMedicineHistoryBean detailBean = list.get(position);
                intent.putExtra("detailBean", detailBean);
                startActivity(intent);
            }
        });
    }

    private void toDelMedicineHistory(List<PersonalRecordMedicineHistoryBean> list, int position) {
        LoginBean userLogin = (LoginBean) SharedPreferencesUtils.getBean(Utils.getApp(), SharedPreferencesUtils.USER_INFO);
        String token = userLogin.getToken();
        DialogUtils.getInstance().showNotCancelDialog(getPageContext(), "",
                "?????????????????????", true, new DialogUtils.DialogCallBack() {
                    @Override
                    public void execEvent() {
                        RxHttpUtils.createApi(Service.class)
                                .delMedicine(token, list.get(position).getId() + "")
                                .compose(Transformer.switchSchedulers())
                                .subscribe(new CommonObserver<BaseData>() {
                                    @Override
                                    protected void onError(String errorMsg) {

                                    }

                                    @Override
                                    protected void onSuccess(BaseData bean) {
                                        list.remove(position);
                                        medicineHistoryListAdapter.notifyDataSetChanged();
                                    }
                                });
                    }
                });
    }


    @Override
    public void adapterViewClick(BaseViewHolder help) {
        TextView tvLeft = help.getView(R.id.tv_left);
        TextView tvRight = help.getView(R.id.tv_right);
        String text = tvLeft.getText().toString();
        switch (text) {
            case "????????????":
                DialogUtils.editDialog(getPageContext(), "????????????", "?????????????????????", "", InputType.TYPE_CLASS_TEXT, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {
                        toDoSave("nickname", text, tvRight, help);
                    }
                });
                //                DialogUtils.getInstance().showEditTextDialog(getPageContext(), "????????????", "?????????????????????", text1 -> {
                //                    //                    tvRight.setText(text1);
                //                    toDoSave("nickname", text1, tvRight, help);
                //                });
                break;
            case "??????":
                DialogUtils.editDialog(getPageContext(), "??????", "???????????????", "", InputType.TYPE_CLASS_TEXT, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {
                        toDoSave("petname", text, tvRight, help);
                    }
                });
                //                showEditDialog("petname", "??????", "???????????????", tvRight, help);
                //                DialogUtils.getInstance().showEditTextDialog(getPageContext(), "??????", "???????????????", text1 -> {
                //                    //                    tvRight.setText(text1);
                //                    toDoSave("petname", text1, tvRight, help);
                //                });
                break;
            case "??????":
                ArrayList<String> sexList = new ArrayList<>();
                sexList.add("???");
                sexList.add("???");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("???".equals(content)) {
                            toDoSave("sex", "1", tvRight, help);
                        } else {
                            toDoSave("sex", "2", tvRight, help);
                        }
                    }
                }, sexList);
                break;
            case "????????????":
                PickerUtils.showTimeWindow(getPageContext(), new boolean[]{true, true, true, false, false, false}, DataFormatManager.TIME_FORMAT_Y_M_D, new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        long timeMs = TimeUtils.string2Millis(content, TimeFormatUtils.getDefaultFormat());
                        long timeS = timeMs / 1000;
                        toDoSave("birthtime", timeS + "", tvRight, help);
                    }
                });

                //                PickerUtils.showTime(getPageContext(), new PickerUtils.TimePickerCallBack() {
                //                    @Override
                //                    public void execEvent(String content) {
                //                        //                        tvRight.setText(content);
                //                        long timeMs = TimeUtils.string2Millis(content, TimeFormatUtils.getDefaultFormat());
                //                        long timeS = timeMs / 1000;
                //                        toDoSave("birthtime", timeS + "", tvRight, help);
                //                    }
                //                });
                break;
            case "??????":
                String[] nations = Utils.getApp().getResources().getStringArray(R.array.nation_list);
                List<String> list = Arrays.asList(nations);
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        toDoSave("minzu", content, tvRight, help);
                    }
                }, list);
                break;
            case "???????????????"://1???1???  2???2???  3?????????  4 ??????
                ArrayList<String> diabetesTypeList = new ArrayList<>();
                diabetesTypeList.add("1???");
                diabetesTypeList.add("2???");
                diabetesTypeList.add("??????");
                diabetesTypeList.add("??????");
                diabetesTypeList.add("???");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        switch (content) {
                            case "1???":
                                toDoSave("diabeteslei", "1", tvRight, help);
                                break;
                            case "2???":
                                toDoSave("diabeteslei", "2", tvRight, help);
                                break;
                            case "??????":
                                toDoSave("diabeteslei", "3", tvRight, help);
                                break;
                            case "??????":
                                toDoSave("diabeteslei", "4", tvRight, help);
                                break;
                            case "???":
                                toDoSave("diabeteslei", "0", tvRight, help);
                                break;
                            default:
                                break;
                        }
                    }
                }, diabetesTypeList);
                break;
            case "?????????????????????":

                PickerUtils.showTimeWindow(getPageContext(), new boolean[]{true, true, true, false, false, false}, DataFormatManager.TIME_FORMAT_Y_M_D, new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        long timeMs = TimeUtils.string2Millis(content, TimeFormatUtils.getDefaultFormat());
                        long timeS = timeMs / 1000;
                        toDoSave("diabetesleitime", timeS + "", tvRight, help);
                    }
                });

                //                PickerUtils.showTime(getPageContext(), new PickerUtils.TimePickerCallBack() {
                //                    @Override
                //                    public void execEvent(String content) {
                //                        //                        tvRight.setText(content);
                //                        long timeMs = TimeUtils.string2Millis(content, TimeFormatUtils.getDefaultFormat());
                //                        long timeS = timeMs / 1000;
                //                        toDoSave("diabetesleitime", timeS + "", tvRight, help);
                //                    }
                //                });
                break;
            case "??????":
                ArrayList<String> smokeList = new ArrayList<>();
                smokeList.add("???");
                smokeList.add("???");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("???".equals(content)) {
                            toDoSave("smoke", "1", tvRight, help);
                        } else {
                            toDoSave("smoke", "2", tvRight, help);
                        }
                    }
                }, smokeList);
                break;
            case "??????":
                ArrayList<String> drinkList = new ArrayList<>();
                drinkList.add("???");
                drinkList.add("???");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("???".equals(content)) {
                            toDoSave("drink", "1", tvRight, help);
                        } else {
                            toDoSave("drink", "2", tvRight, help);
                        }
                    }
                }, drinkList);
                break;
            case "????????????"://1 ??????????????? 2 ?????? 3 ?????? 4 ???????????????
                ArrayList<String> cultureTypeList = new ArrayList<>();
                cultureTypeList.add("???????????????");
                cultureTypeList.add("??????");
                cultureTypeList.add("??????");
                cultureTypeList.add("???????????????");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        switch (content) {
                            case "???????????????":
                                toDoSave("culture", "1", tvRight, help);
                                break;
                            case "??????":
                                toDoSave("culture", "2", tvRight, help);
                                break;
                            case "??????":
                                toDoSave("culture", "3", tvRight, help);
                                break;
                            case "???????????????":
                                toDoSave("culture", "4", tvRight, help);
                                break;
                        }
                    }
                }, cultureTypeList);
                break;
            case "????????????"://1 ????????? 2 ????????? 3 ??????
                ArrayList<String> professionTypeList = new ArrayList<>();
                professionTypeList.add("?????????");
                professionTypeList.add("?????????");
                professionTypeList.add("?????????");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        switch (content) {
                            case "?????????":
                                toDoSave("profession", "1", tvRight, help);
                                break;
                            case "?????????":
                                toDoSave("profession", "2", tvRight, help);
                                break;
                            case "?????????":
                                toDoSave("profession", "3", tvRight, help);
                                break;
                        }
                    }
                }, professionTypeList);
                break;
            case "????????????"://???????????? 1 ?????? 2 ??????
                ArrayList<String> marriageList = new ArrayList<>();
                marriageList.add("??????");
                marriageList.add("??????");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("??????".equals(content)) {
                            toDoSave("marriage", "1", tvRight, help);
                        } else {
                            toDoSave("marriage", "2", tvRight, help);
                        }
                    }
                }, marriageList);
                break;
            case "????????????"://1??? 2???
                ArrayList<String> bedridList = new ArrayList<>();
                bedridList.add("???");
                bedridList.add("???");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("???".equals(content)) {
                            toDoSave("bedrid", "1", tvRight, help);
                        } else {
                            toDoSave("bedrid", "2", tvRight, help);
                        }
                    }
                }, bedridList);
                break;
            case "??????":
                showCityPickerView(tvRight);
                break;
            case "??????":
                DialogUtils.editDialog(getPageContext(), "??????", "???????????????", "", InputType.TYPE_CLASS_TEXT, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {
                        toDoSave("address", text, tvRight, help);
                    }
                });
                //                showEditDialog("address", "??????", "???????????????", tvRight, help);
                //                DialogUtils.getInstance().showEditTextDialog(getPageContext(), "??????", "???????????????", text1 -> {
                //                    //                    tvRight.setText(text1);
                //                    toDoSave("address", text1, tvRight, help);
                //                });
                break;
            case "??????????????????":// 1 ???????????????????????? 2 ?????????????????????????????? 3 ???????????????????????? 4 ???????????? 5 ???????????? 6 ???????????? 7 ??????
                ArrayList<String> payList = new ArrayList<>();
                payList.add("????????????????????????");
                payList.add("??????????????????????????????");
                payList.add("????????????????????????");
                payList.add("????????????");
                payList.add("????????????");
                payList.add("????????????");
                payList.add("??????");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        switch (content) {
                            case "????????????????????????":
                                toDoSave("zhifufangshi", "1", tvRight, help);
                                break;
                            case "??????????????????????????????":
                                toDoSave("zhifufangshi", "2", tvRight, help);
                                break;
                            case "????????????????????????":
                                toDoSave("zhifufangshi", "3", tvRight, help);
                                break;
                            case "????????????":
                                toDoSave("zhifufangshi", "4", tvRight, help);
                                break;
                            case "????????????":
                                toDoSave("zhifufangshi", "5", tvRight, help);
                                break;
                            case "????????????":
                                toDoSave("zhifufangshi", "6", tvRight, help);
                                break;
                            case "??????":
                                toDoSave("zhifufangshi", "7", tvRight, help);
                                break;
                            default:
                                break;
                        }
                    }
                }, payList);
                break;
            case "????????????":
                DialogUtils.editDialog(getPageContext(), "????????????", "?????????????????????", "", InputType.TYPE_CLASS_TEXT, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        if (!RegexUtils.isIDCard18(text)) {
                            ToastUtils.showShort("?????????????????????????????????");
                            return;
                        }
                        toDoSave("idcard", text, tvRight, help);
                    }
                });
                //                showEditDialog("idcard", "????????????", "?????????????????????", tvRight, help);
                //                DialogUtils.getInstance().showEditTextIdNumberDialog(getPageContext(), "????????????", "?????????????????????", text1 -> {
                //                    //                    tvRight.setText(text1);
                //                    if (!RegexUtils.isIDCard18(text1)) {
                //                        ToastUtils.showShort("?????????????????????????????????");
                //                        return;
                //                    }
                //                    toDoSave("idcard", text1, tvRight, help);
                //                });
                break;
            case "????????????":
                DialogUtils.editDialog(getPageContext(), "????????????", "?????????????????????", "", InputType.TYPE_CLASS_TEXT, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSave("jzkahao", text, tvRight, help);
                    }
                });
                //                showEditDialog("jzkahao", "????????????", "?????????????????????", tvRight, help);
                //                DialogUtils.getInstance().showEditTextDialog(getPageContext(), "????????????", "?????????????????????", InputType.TYPE_CLASS_NUMBER, text1 -> {
                //                    //                    tvRight.setText(text1);
                //                    toDoSave("jzkahao", text1, tvRight, help);
                //                });
                break;
            case "????????????"://?????????1??? 2???
                ArrayList<String> dujuList = new ArrayList<>();
                dujuList.add("???");
                dujuList.add("???");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("???".equals(content)) {
                            toDoSave("duju", "1", tvRight, help);
                        } else {
                            toDoSave("duju", "2", tvRight, help);
                        }
                    }
                }, dujuList);
                break;
            case "???????????????"://???????????????   1 ?????????  2 ?????????
                ArrayList<String> gxyzhenduanList = new ArrayList<>();
                gxyzhenduanList.add("?????????");
                gxyzhenduanList.add("?????????");
                gxyzhenduanList.add("?????????");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("?????????".equals(content)) {
                            toDoSave("gxyzhenduan", "1", tvRight, help);
                        } else if ("?????????".equals(content)) {
                            toDoSave("gxyzhenduan", "2", tvRight, help);
                        } else if ("?????????".equals(content)) {
                            toDoSave("gxyzhenduan", "3", tvRight, help);
                        }
                    }
                }, gxyzhenduanList);
                break;
            case "?????????????????????":
                PickerUtils.showTimeWindow(getPageContext(), new boolean[]{true, true, true, false, false, false}, DataFormatManager.TIME_FORMAT_Y_M_D, new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        long timeMs = TimeUtils.string2Millis(content, TimeFormatUtils.getDefaultFormat());
                        long timeS = timeMs / 1000;
                        toDoSave("gxytime", timeS + "", tvRight, help);
                    }
                });


                //                PickerUtils.showTime(getPageContext(), new PickerUtils.TimePickerCallBack() {
                //                    @Override
                //                    public void execEvent(String content) {
                //                        //                        tvRight.setText(content);
                //                        long timeMs = TimeUtils.string2Millis(content, TimeFormatUtils.getDefaultFormat());
                //                        long timeS = timeMs / 1000;
                //                        toDoSave("gxytime", timeS + "", tvRight, help);
                //                    }
                //                });
                break;
            case "??????"://?????? ???1???  2???
                ArrayList<String> gestationList = new ArrayList<>();
                gestationList.add("???");
                gestationList.add("???");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("???".equals(content)) {
                            toDoSave("gestation", "1", tvRight, help);
                        } else {
                            toDoSave("gestation", "2", tvRight, help);
                        }
                    }
                }, gestationList);
                break;
            case "????????????":
                PickerUtils.showTimeWindow(getPageContext(), new boolean[]{true, true, true, false, false, false}, DataFormatManager.TIME_FORMAT_Y_M_D, new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        long timeMs = TimeUtils.string2Millis(content, TimeFormatUtils.getDefaultFormat());
                        long timeS = timeMs / 1000;
                        toDoSave("gestationtime", timeS + "", tvRight, help);
                    }
                });

                //                PickerUtils.showTime(getPageContext(), new PickerUtils.TimePickerCallBack() {
                //                    @Override
                //                    public void execEvent(String content) {
                //                        //                        tvRight.setText(content);
                //                        long timeMs = TimeUtils.string2Millis(content, TimeFormatUtils.getDefaultFormat());
                //                        long timeS = timeMs / 1000;
                //                        toDoSave("gestationtime", timeS + "", tvRight, help);
                //                    }
                //                });
                break;
            //????????????
            ////////////////////////////////////////////////////
            case "??????"://height
                DialogUtils.editDialog(getPageContext(), "??????", "???????????????", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSave("height", text, tvRight, help);
                    }
                });
                //                showEditDialog("height", "??????", "???????????????", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "??????", "???????????????", text1 -> {
                //                    //tvRight.setText(text1);
                //                    SPStaticUtils.put("height", text1);
                //                    toDoSave("height", text1, tvRight, help);
                //                    //                    resetValue(help, tvRight, text1);
                //                    resetBmi(help);
                //                });
                break;
            case "??????"://weight
                DialogUtils.editDialog(getPageContext(), "??????", "???????????????", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSave("weight", text, tvRight, help);
                    }
                });
                //                showEditDialog("weight", "??????", "???????????????", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "??????", "???????????????", text1 -> {
                //                    //tvRight.setText(text1);
                //                    SPStaticUtils.put("weight", text1);
                //                    toDoSave("weight", text1, tvRight, help);
                //                    resetBmi(help);
                //                });
                break;
            case "BMI"://??????,????????????
                String weight = SPStaticUtils.getString("weight");
                String height = SPStaticUtils.getString("height");
                break;
            case "??????"://waistline
                DialogUtils.editDialog(getPageContext(), "??????", "???????????????", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSave("waistline", text, tvRight, help);
                    }
                });
                //                showEditDialog("waistline", "??????", "???????????????", tvRight, help);

                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "??????", "???????????????", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSave("waistline", text1, tvRight, help);
                //                    SPStaticUtils.put("waistline", text1);
                //                    resetThr();
                //                });
                break;
            case "??????"://hipline
                DialogUtils.editDialog(getPageContext(), "??????", "???????????????", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSave("hipline", text, tvRight, help);
                    }
                });
                //                showEditDialog("hipline", "??????", "???????????????", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "??????", "???????????????", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSave("hipline", text1, tvRight, help);
                //                    SPStaticUtils.put("hipline", text1);
                //                    resetThr();
                //                });
                break;
            case "?????????"://??????,????????????
                break;
            case "?????????"://systolic
                DialogUtils.editDialog(getPageContext(), "?????????", "??????????????????", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSave("systolic", text, tvRight, help);
                    }
                });
                //                showEditDialog("systolic", "?????????", "??????????????????", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "?????????", "??????????????????", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSave("systolic", text1, tvRight, help);
                //                });
                break;
            case "?????????"://diastolic
                DialogUtils.editDialog(getPageContext(), "?????????", "??????????????????", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSave("diastolic", text, tvRight, help);
                    }
                });
                //                showEditDialog("diastolic", "?????????", "??????????????????", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "?????????", "??????????????????", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSave("diastolic", text1, tvRight, help);
                //                });
                break;
            case "????????????"://heartrate
                DialogUtils.editDialog(getPageContext(), "????????????", "?????????????????????", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSave("heartrate", text, tvRight, help);
                    }
                });
                //                showEditDialog("heartrate", "????????????", "?????????????????????", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "????????????", "?????????????????????", text1 -> {
                //                    //tvRight.setText(text1);
                //                    resetValue(help, tvRight, text1);
                //                    toDoSave("heartrate", text1, tvRight, help);
                //                });
                break;
            //????????????
            //////////////////////////////////////////////////////////////////
            case "OGTT2h??????"://xtogtt2h
                DialogUtils.editDialog(getPageContext(), "OGTT2h??????", "?????????OGTT2h??????", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("xtogtt2h", text, tvRight, help);
                    }
                });
                //                showEditDialog("xtogtt2h", "OGTT2h??????", "?????????OGTT2h??????", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "OGTT2h??????", "?????????OGTT2h??????", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("xtogtt2h", text1, tvRight, help);
                //                });
                break;
            case "H b A 1 c":
                DialogUtils.editDialog(getPageContext(), "H b A 1 c", "?????????H b A 1 c", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("xthbalc", text, tvRight, help);
                    }
                });
                //                showEditDialog("xthbalc", "H b A 1 c", "?????????H b A 1 c", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "H b A 1 c", "?????????H b A 1 c", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("xthbalc", text1, tvRight, help);
                //                });
                break;
            case "????????????":
                DialogUtils.editDialog(getPageContext(), "????????????", "?????????????????????", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("xtsuiji", text, tvRight, help);
                    }
                });
                //                showEditDialog("xtsuiji", "????????????", "?????????????????????", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "????????????", "?????????????????????", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("xtsuiji", text1, tvRight, help);
                //                });
                break;
            case "????????????":
                DialogUtils.editDialog(getPageContext(), "????????????", "?????????????????????", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("xtkongfu", text, tvRight, help);
                    }
                });
                //                showEditDialog("xtkongfu", "????????????", "?????????????????????", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "????????????", "?????????????????????", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("xtkongfu", text1, tvRight, help);
                //                });
                break;
            case "?????????2h????????????":
                DialogUtils.editDialog(getPageContext(), "?????????2h????????????", "??????????????????2h????????????", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("xtcaihou", text, tvRight, help);
                    }
                });
                //                showEditDialog("xtcaihou", "?????????2h????????????", "??????????????????2h????????????", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "?????????2h????????????", "??????????????????2h????????????", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("xtcaihou", text1, tvRight, help);
                //                });
                break;
            case "????????????":
                DialogUtils.editDialog(getPageContext(), "????????????", "?????????????????????", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("xtyejian", text, tvRight, help);
                    }
                });
                //                showEditDialog("xtyejian", "????????????", "?????????????????????", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "????????????", "?????????????????????", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("xtyejian", text1, tvRight, help);
                //                });
                break;
            case "????????????":
                DialogUtils.editDialog(getPageContext(), "????????????", "?????????????????????", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("xtshuiqian", text, tvRight, help);
                    }
                });
                //                showEditDialog("xtshuiqian", "????????????", "?????????????????????", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "????????????", "?????????????????????", text1 -> {
                //                    //tvRight.setText(text1);
                //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("xtshuiqian", text1, tvRight, help);
                //                });
                break;
            case "???????????????????????????"://??????
                ArrayList<String> xtogtt2hList = new ArrayList<>();
                xtogtt2hList.add("???");
                xtogtt2hList.add("???");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("???".equals(content)) {
                            toDoSaveGlucose("xtdi", "1", tvRight, help);
                        } else {
                            toDoSaveGlucose("xtdi", "2", tvRight, help);
                        }
                    }
                }, xtogtt2hList);
                break;
            //????????????
            /////////////////////////////////////////////////////
            case "????????????(TC)":
                DialogUtils.editDialog(getPageContext(), "????????????(TC)", "?????????????????????(TC)", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("sytc", text, tvRight, help);
                    }
                });
                //                showEditDialog("sytc", "????????????(TC)", "?????????????????????(TC)", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "????????????(TC)", "?????????????????????(TC)", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("sytc", text1, tvRight, help);
                //                });
                break;
            case "????????????":
                DialogUtils.editDialog(getPageContext(), "????????????", "?????????????????????", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("sytg", text, tvRight, help);
                    }
                });
                //                showEditDialog("sytg", "????????????", "?????????????????????", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "????????????", "?????????????????????", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("sytg", text1, tvRight, help);
                //                });
                break;
            case "??????????????????(LDL-C)":
                DialogUtils.editDialog(getPageContext(), "??????????????????(LDL-C)", "???????????????????????????(LDL-C)", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("syldl", text, tvRight, help);
                    }
                });
                //                showEditDialog("syldl", "??????????????????(LDL-C)", "???????????????????????????(LDL-C)", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "??????????????????(LDL-C)", "???????????????????????????(LDL-C)", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("syldl", text1, tvRight, help);
                //                });
                break;
            case "???????????????(ALT)":
                DialogUtils.editDialog(getPageContext(), "???????????????(ALT)", "????????????????????????(ALT)", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("syalt", text, tvRight, help);
                    }
                });
                //                showEditDialog("syalt", "???????????????(ALT)", "????????????????????????(ALT)", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "???????????????(ALT)", "????????????????????????(ALT)", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("syalt", text1, tvRight, help);
                //                });
                break;
            case "???????????????(AST)":
                DialogUtils.editDialog(getPageContext(), "???????????????(AST)", "????????????????????????(AST)", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("syast", text, tvRight, help);
                    }
                });
                //                showEditDialog("syast", "???????????????(AST)", "????????????????????????(AST)", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "???????????????(AST)", "????????????????????????(AST)", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("syast", text1, tvRight, help);
                //                });
                break;
            case "????????????(T-BIL)":
                DialogUtils.editDialog(getPageContext(), "????????????(T-BIL)", "?????????????????????(T-BIL)", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("sytbil", text, tvRight, help);
                    }
                });
                //                showEditDialog("sytbil", "????????????(T-BIL)", "?????????????????????(T-BIL)", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "????????????(T-BIL)", "?????????????????????(T-BIL)", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("sytbil", text1, tvRight, help);
                //                });
                break;
            case "??????????????????(HDL-C)":
                DialogUtils.editDialog(getPageContext(), "??????????????????(HDL-C)", "???????????????????????????(HDL-C)", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("syhdl", text, tvRight, help);
                    }
                });
                //                showEditDialog("syhdl", "??????????????????(HDL-C)", "???????????????????????????(HDL-C)", tvRight, help);

                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "??????????????????(HDL-C)", "???????????????????????????(HDL-C)", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("syhdl", text1, tvRight, help);
                //                });
                break;
            case "???????????????(ALP)":
                DialogUtils.editDialog(getPageContext(), "???????????????(ALP)", "????????????????????????(ALP)", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("syalp", text, tvRight, help);
                    }
                });
                //                showEditDialog("syalp", "???????????????(ALP)", "????????????????????????(ALP)", tvRight, help);

                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "???????????????(ALP)", "????????????????????????(ALP)", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("syalp", text1, tvRight, help);
                //                });
                break;
            case "?????????(?????????)":
                DialogUtils.editDialog(getPageContext(), "?????????(?????????)", "??????????????????(?????????)", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("syncg", text, tvRight, help);
                    }
                });
                //                showEditDialog("syncg", "?????????(?????????)", "??????????????????(?????????)", tvRight, help);

                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "?????????(?????????)", "??????????????????(?????????)", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("syncg", text1, tvRight, help);
                //                });
                break;
            case "24??????????????????(24hUAE)":
                DialogUtils.editDialog(getPageContext(), "24??????????????????(24hUAE)", "?????????24??????????????????(24hUAE)", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("syhuae", text, tvRight, help);
                    }
                });
                //                showEditDialog("syhuae", "24??????????????????(24hUAE)", "?????????24??????????????????(24hUAE)", tvRight, help);
                //
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "24??????????????????(24hUAE)", "?????????24??????????????????(24hUAE)", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("syhuae", text1, tvRight, help);
                //                });
                break;
            case "????????????/??????(ACR)":
                DialogUtils.editDialog(getPageContext(), "????????????/??????(ACR)", "?????????????????????/??????(ACR)", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("syacr", text, tvRight, help);
                    }
                });
                //                showEditDialog("syacr", "????????????/??????(ACR)", "?????????????????????/??????(ACR)", tvRight, help);

                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "????????????/??????(ACR)", "?????????????????????/??????(ACR)", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("syacr", text1, tvRight, help);
                //                });
                break;
            case "????????????(UAE)":
                DialogUtils.editDialog(getPageContext(), "????????????(UAE)", "?????????????????????(UAE)", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("syuae", text, tvRight, help);
                    }
                });
                //                showEditDialog("syuae", "????????????(UAE)", "?????????????????????(UAE)", tvRight, help);
                //
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "????????????(UAE)", "?????????????????????(UAE)", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("syuae", text1, tvRight, help);
                //                });
                break;
            case "?????????(BUN)":
                DialogUtils.editDialog(getPageContext(), "?????????(BUN)", "??????????????????(BUN)", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("synsd", text, tvRight, help);
                    }
                });
                //                showEditDialog("synsd", "?????????(BUN)", "??????????????????(BUN)", tvRight, help);

                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "?????????(BUN)", "??????????????????(BUN)", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("synsd", text1, tvRight, help);
                //                });
                break;
            case "?????????????????????(Ccr)":
                DialogUtils.editDialog(getPageContext(), "?????????????????????(Ccr)", "??????????????????????????????(Ccr)", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        toDoSaveGlucose("synsjg", text, tvRight, help);
                    }
                });
                //                showEditDialog("synsjg", "?????????????????????(Ccr)", "??????????????????????????????(Ccr)", tvRight, help);

                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "?????????????????????(Ccr)", "??????????????????????????????(Ccr)", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    toDoSaveGlucose("synsjg", text1, tvRight, help);
                //                });
                break;
            case "????????????(SRC)":
                DialogUtils.editDialog(getPageContext(), "?????????????????????(Ccr)", "??????????????????????????????(Ccr)", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 0, new DialogUtils.DialogInputCallBack() {
                    @Override
                    public void execEvent(String text) {

                        if (1 == sexInt) {
                            //??????
                            toDoSaveGlucose("syxqjg", text, tvRight, help);
                        } else {
                            //??????
                            toDoSaveGlucose("syxqjgg", text, tvRight, help);
                        }
                    }
                });
                //                showEditDialog("choose", "?????????????????????(Ccr)", "??????????????????????????????(Ccr)", tvRight, help);
                //                DialogUtils.getInstance().showDecimalNumberInputDialog(getPageContext(), "????????????(SRC)", "?????????????????????(SRC)", text1 -> {
                //                    //tvRight.setText(text1);
                //                    //                    resetValue(help, tvRight, text1);
                //                    if (1 == sexInt) {
                //                        //??????
                //                        toDoSaveGlucose("syxqjg", text1, tvRight, help);
                //                    } else {
                //                        //??????
                //                        toDoSaveGlucose("syxqjgg", text1, tvRight, help);
                //                    }
                //                });
                break;
            //????????????
            ///////////////////////////////////////////
            case "???????????????"://nephropathy
                ArrayList<String> nephropathyList = new ArrayList<>();
                nephropathyList.add("?????????");
                nephropathyList.add("?????????");
                nephropathyList.add("?????????");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("?????????".equals(content)) {
                            toDoSaveBINGFA("nephropathy", "1", tvRight, help);
                        } else if ("?????????".equals(content)) {
                            toDoSaveBINGFA("nephropathy", "2", tvRight, help);
                        } else {
                            toDoSaveBINGFA("nephropathy", "3", tvRight, help);
                        }
                    }
                }, nephropathyList);
                break;
            case "????????????????????????"://retina
                ArrayList<String> retinaList = new ArrayList<>();
                retinaList.add("?????????");
                retinaList.add("?????????");
                retinaList.add("?????????");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("?????????".equals(content)) {
                            toDoSaveBINGFA("retina", "1", tvRight, help);
                        } else if ("?????????".equals(content)) {
                            toDoSaveBINGFA("retina", "2", tvRight, help);
                        } else {
                            toDoSaveBINGFA("retina", "3", tvRight, help);
                        }
                    }
                }, retinaList);
                break;
            case "?????????????????????"://nerve
                ArrayList<String> nerveList = new ArrayList<>();
                nerveList.add("?????????");
                nerveList.add("?????????");
                nerveList.add("?????????");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("?????????".equals(content)) {
                            toDoSaveBINGFA("nerve", "1", tvRight, help);
                        } else if ("?????????".equals(content)) {
                            toDoSaveBINGFA("nerve", "2", tvRight, help);
                        } else {
                            toDoSaveBINGFA("nerve", "3", tvRight, help);
                        }
                    }
                }, nerveList);
                break;
            case "???????????????????????????"://legs
                ArrayList<String> legsList = new ArrayList<>();
                legsList.add("?????????");
                legsList.add("?????????");
                legsList.add("?????????");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("?????????".equals(content)) {
                            toDoSaveBINGFA("legs", "1", tvRight, help);
                        } else if ("?????????".equals(content)) {
                            toDoSaveBINGFA("legs", "2", tvRight, help);
                        } else {
                            toDoSaveBINGFA("legs", "3", tvRight, help);
                        }
                    }
                }, legsList);
                break;
            case "????????????"://diabeticfoot
                ArrayList<String> diabeticfootList = new ArrayList<>();
                diabeticfootList.add("?????????");
                diabeticfootList.add("?????????");
                diabeticfootList.add("?????????");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("?????????".equals(content)) {
                            toDoSaveBINGFA("diabeticfoot", "1", tvRight, help);
                        } else if ("?????????".equals(content)) {
                            toDoSaveBINGFA("diabeticfoot", "2", tvRight, help);
                        } else {
                            toDoSaveBINGFA("diabeticfoot", "3", tvRight, help);
                        }
                    }
                }, diabeticfootList);
                break;
            case "????????????????????????"://ketoacidosis
                ArrayList<String> ketoacidosList = new ArrayList<>();
                ketoacidosList.add("?????????");
                ketoacidosList.add("?????????");
                ketoacidosList.add("?????????");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("?????????".equals(content)) {
                            toDoSaveBINGFA("ketoacidosis", "1", tvRight, help);
                        } else if ("?????????".equals(content)) {
                            toDoSaveBINGFA("ketoacidosis", "2", tvRight, help);
                        } else {
                            toDoSaveBINGFA("ketoacidosis", "3", tvRight, help);
                        }
                    }
                }, ketoacidosList);
                break;
            case "????????????????????????"://hypertonic
                ArrayList<String> hypertonicList = new ArrayList<>();
                hypertonicList.add("?????????");
                hypertonicList.add("?????????");
                hypertonicList.add("?????????");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("?????????".equals(content)) {
                            toDoSaveBINGFA("hypertonic", "1", tvRight, help);
                        } else if ("?????????".equals(content)) {
                            toDoSaveBINGFA("hypertonic", "2", tvRight, help);
                        } else {
                            toDoSaveBINGFA("hypertonic", "3", tvRight, help);
                        }
                    }
                }, hypertonicList);
                break;
            //????????????
            /////////////////////////////////////
            case "?????????"://highblood
                ArrayList<String> highbloodList = new ArrayList<>();
                highbloodList.add("?????????");
                highbloodList.add("?????????");
                highbloodList.add("?????????");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("?????????".equals(content)) {
                            toDoSaveBINGFA("highblood", "1", tvRight, help);
                        } else if ("?????????".equals(content)) {
                            toDoSaveBINGFA("highblood", "2", tvRight, help);
                        } else {
                            toDoSaveBINGFA("highblood", "3", tvRight, help);
                        }
                    }
                }, highbloodList);
                break;
            case "????????????"://hlp
                ArrayList<String> hlpList = new ArrayList<>();
                hlpList.add("?????????");
                hlpList.add("?????????");
                hlpList.add("?????????");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("?????????".equals(content)) {
                            toDoSaveBINGFA("hlp", "1", tvRight, help);
                        } else if ("?????????".equals(content)) {
                            toDoSaveBINGFA("hlp", "2", tvRight, help);
                        } else {
                            toDoSaveBINGFA("hlp", "3", tvRight, help);
                        }
                    }
                }, hlpList);
                break;
            case "?????????"://coronary
                ArrayList<String> coronaryList = new ArrayList<>();
                coronaryList.add("?????????");
                coronaryList.add("?????????");
                coronaryList.add("?????????");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("?????????".equals(content)) {
                            toDoSaveBINGFA("coronary", "1", tvRight, help);
                        } else if ("?????????".equals(content)) {
                            toDoSaveBINGFA("coronary", "2", tvRight, help);
                        } else {
                            toDoSaveBINGFA("coronary", "3", tvRight, help);
                        }
                    }
                }, coronaryList);
                break;
            case "????????????"://cerebrovascular
                ArrayList<String> cerebrovascularList = new ArrayList<>();
                cerebrovascularList.add("?????????");
                cerebrovascularList.add("?????????");
                cerebrovascularList.add("?????????");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("?????????".equals(content)) {
                            toDoSaveBINGFA("cerebrovascular", "1", tvRight, help);
                        } else if ("?????????".equals(content)) {
                            toDoSaveBINGFA("cerebrovascular", "2", tvRight, help);
                        } else {
                            toDoSaveBINGFA("cerebrovascular", "3", tvRight, help);
                        }
                    }
                }, cerebrovascularList);
                break;
            case "??????????????????"://diabetes
                ArrayList<String> diabetesList = new ArrayList<>();
                diabetesList.add("???");
                diabetesList.add("???");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("???".equals(content)) {
                            toDoSave("diabetes", "1", tvRight, help);
                        } else {
                            toDoSave("diabetes", "2", tvRight, help);
                        }
                    }
                }, diabetesList);
                break;
            case "????????????????????????"://angiocarpy
                ArrayList<String> angiocarpyList = new ArrayList<>();
                angiocarpyList.add("???");
                angiocarpyList.add("???");
                PickerUtils.showOption(getPageContext(), new PickerUtils.TimePickerCallBack() {
                    @Override
                    public void execEvent(String content) {
                        //                        tvRight.setText(content);
                        if ("???".equals(content)) {
                            toDoSave("angiocarpy", "1", tvRight, help);
                        } else {
                            toDoSave("angiocarpy", "2", tvRight, help);
                        }
                    }
                }, angiocarpyList);
                break;
            default:
                break;
        }
    }


    /**
     * ?????????????????????
     */
    private void showCityPickerView(TextView textView) {
        String textColor = "#057dff";
        CityConfig.WheelType mWheelType = CityConfig.WheelType.PRO_CITY_DIS;
        CityConfig cityConfig = new CityConfig.Builder()
                .title("????????????")
                .cancelTextColor(textColor)
                .confirTextColor(textColor)
                .visibleItemsCount(5)
                .province("??????")
                .city("?????????")
                .district("?????????")
                .provinceCyclic(false)
                .cityCyclic(false)
                .districtCyclic(false)
                .setCityWheelType(mWheelType)
                .setCustomItemLayout(R.layout.item_city)
                .setCustomItemTextViewId(R.id.item_city_name_tv)
                .setShowGAT(false)
                .build();
        mCityPickerView.setConfig(cityConfig);
        mCityPickerView.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                StringBuilder sb = new StringBuilder();
                if (province != null) {
                    String name = province.getName();
                    sb.append(name);
                    String id = province.getId();
                    toDoSave("jbprov", id, textView, null);
                }
                if (city != null) {
                    String name = city.getName();
                    sb.append(name);
                    String id = city.getId();
                    toDoSave("jbcity", id, textView, null);
                }
                if (district != null) {
                    String name = district.getName();
                    sb.append(name);
                    String id = district.getId();
                    toDoSave("jbdist", id, textView, null);
                }
                toDoSave("nativeplace", sb.toString(), textView, null);
            }

            @Override
            public void onCancel() {

            }
        });
        mCityPickerView.showCityPicker();
    }


    /**
     * ?????? ??????????????????  ????????????
     *
     * @param fieldName
     * @param fieldValue
     */
    private void toDoSave(String fieldName, String fieldValue, TextView textView, BaseViewHolder help) {
        Map<String, Object> map = new HashMap<>();
        map.put("fieldname", fieldName);
        map.put("fieldvalue", fieldValue);
        XyUrl.okPostSave(XyUrl.PERSONAL_SAVE, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String msg) {
                MySugarFilesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MultiItemEntity multiItemEntity1 = multiItemEntityArrayList.get(help.getBindingAdapterPosition());
                        HealthArchiveGroupLevelOneBean levelOneBean1 = (HealthArchiveGroupLevelOneBean) multiItemEntity1;
                        levelOneBean1.setContent(fieldValue);
                        switch (fieldName) {
                            case "nickname":
                                int lengthnickname = fieldValue.length();
                                if (lengthnickname > 9) {
                                    textView.setText(fieldValue.substring(0, 9) + "...");
                                } else {
                                    textView.setText(fieldValue);
                                }
                                //???????????????????????????
                                LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(getPageContext(), SharedPreferencesUtils.USER_INFO);
                                loginBean.setNickname(fieldValue);
                                SharedPreferencesUtils.putBean(getPageContext(), SharedPreferencesUtils.USER_INFO, loginBean);
                                break;
                            case "petname":
                                int lengthpetname = fieldValue.length();
                                if (lengthpetname > 9) {
                                    textView.setText(fieldValue.substring(0, 9) + "...");
                                    levelOneBean1.setContent(fieldValue.substring(0, 9) + "...");
                                } else {
                                    textView.setText(fieldValue);
                                    levelOneBean1.setContent(fieldValue);
                                }

                                break;
                            //??????
                            case "sex":
                                if ("1".equals(fieldValue)) {
                                    textView.setText("???");
                                    levelOneBean1.setContent("???");
                                } else {
                                    textView.setText("???");
                                    levelOneBean1.setContent("???");
                                }

                                break;
                            //????????????
                            case "birthtime":
                                //?????????????????????
                            case "diabetesleitime":
                                //?????????????????????
                            case "gxytime":
                                //????????????
                            case "gestationtime":
                                long date = Long.parseLong(fieldValue) * 1000;
                                String string2DateFormat = DataUtils.getString2DateFormat(date, DataFormatManager.TIME_FORMAT_Y_M_D);
                                textView.setText(string2DateFormat);
                                levelOneBean1.setContent(string2DateFormat);
                                break;
                            //???????????????
                            case "diabeteslei":
                                if ("1".equals(fieldValue)) {
                                    textView.setText("1???");
                                    levelOneBean1.setContent("1???");
                                } else if ("2".equals(fieldValue)) {
                                    textView.setText("2???");
                                    levelOneBean1.setContent("2???");
                                } else if ("3".equals(fieldValue)) {
                                    textView.setText("??????");
                                    levelOneBean1.setContent("??????");
                                } else if ("4".equals(fieldValue)) {
                                    textView.setText("??????");
                                    levelOneBean1.setContent("??????");
                                } else {
                                    textView.setText("???");
                                    levelOneBean1.setContent("???");
                                }
                                break;
                            //????????????
                            case "smoke":
                                //????????????
                            case "drink":
                                //????????????
                            case "bedrid":
                                //????????????
                            case "duju":
                                //????????????
                            case "gestation":

                                //??????????????????
                            case "diabetes":
                                //?????????????????????
                            case "angiocarpy":
                                if ("1".equals(fieldValue)) {
                                    textView.setText("???");
                                    levelOneBean1.setContent("???");
                                } else {
                                    textView.setText("???");
                                    levelOneBean1.setContent("???");
                                }
                                break;
                            //????????????
                            case "culture":
                                if ("1".equals(fieldValue)) {
                                    textView.setText("???????????????");
                                    levelOneBean1.setContent("???????????????");
                                } else if ("2".equals(fieldValue)) {
                                    textView.setText("??????");
                                    levelOneBean1.setContent("??????");
                                } else if ("3".equals(fieldValue)) {
                                    textView.setText("??????");
                                    levelOneBean1.setContent("??????");
                                } else {
                                    textView.setText("???????????????");
                                    levelOneBean1.setContent("???????????????");
                                }
                                break;
                            //????????????
                            case "profession":
                                if ("1".equals(fieldValue)) {
                                    textView.setText("?????????");
                                    levelOneBean1.setContent("?????????");
                                } else if ("2".equals(fieldValue)) {
                                    textView.setText("?????????");
                                    levelOneBean1.setContent("?????????");
                                } else {
                                    textView.setText("?????????");
                                    levelOneBean1.setContent("?????????");
                                }
                                break;
                            //????????????
                            case "marriage":
                                if ("1".equals(fieldValue)) {
                                    textView.setText("??????");
                                    levelOneBean1.setContent("??????");
                                } else {
                                    textView.setText("??????");
                                    levelOneBean1.setContent("??????");
                                }
                                break;
                            case "jbprov":
                                break;
                            case "jbcity":
                                break;
                            case "jbdist":
                                break;
                            case "nativeplace":

                                textView.setText(fieldValue);
                                break;
                            //??????????????????
                            case "zhifufangshi":
                                if ("1".equals(fieldValue)) {
                                    textView.setText("????????????????????????");
                                    levelOneBean1.setContent("????????????????????????");
                                } else if ("2".equals(fieldValue)) {
                                    textView.setText("??????????????????????????????");
                                    levelOneBean1.setContent("??????????????????????????????");
                                } else if ("3".equals(fieldValue)) {
                                    textView.setText("????????????????????????");
                                    levelOneBean1.setContent("????????????????????????");
                                } else if ("4".equals(fieldValue)) {
                                    textView.setText("????????????");
                                    levelOneBean1.setContent("????????????");
                                } else if ("5".equals(fieldValue)) {
                                    textView.setText("????????????");
                                    levelOneBean1.setContent("????????????");
                                } else if ("6".equals(fieldValue)) {
                                    textView.setText("????????????");
                                    levelOneBean1.setContent("????????????");
                                } else {
                                    textView.setText("??????");
                                    levelOneBean1.setContent("??????");
                                }
                                break;
                            //???????????????
                            case "gxyzhenduan":
                                if ("1".equals(fieldValue)) {
                                    textView.setText("?????????");
                                    levelOneBean1.setContent("?????????");
                                } else if ("2".equals(fieldValue)) {
                                    textView.setText("?????????");
                                    levelOneBean1.setContent("?????????");
                                } else {
                                    textView.setText("?????????");
                                    levelOneBean1.setContent("?????????");
                                }
                                break;


                            //??????
                            case "height":
                                SPStaticUtils.put("height", fieldValue);
                                resetValue(help, textView, fieldValue);
                                resetBmiHeight(help, fieldValue);
                                break;
                            //??????
                            case "weight":
                                SPStaticUtils.put("weight", fieldValue);
                                resetValue(help, textView, fieldValue);
                                resetBmiWeight(help, "weight");
                                break;
                            //??????
                            case "waistline":
                                SPStaticUtils.put("waistline", fieldValue);
                                resetThr(help, "waistline");
                                resetValue(help, textView, fieldValue);
                                break;
                            //??????
                            case "hipline":
                                SPStaticUtils.put("hipline", fieldValue);
                                resetThr(help, "hipline");
                                resetValue(help, textView, fieldValue);
                                break;
                            //?????????
                            case "systolic":
                                //?????????
                            case "diastolic":
                            case "heartrate":
                                resetValue(help, textView, fieldValue);
                                break;
                            default:
                                textView.setText(fieldValue);

                                break;
                        }

                    }
                });

                ToastUtils.showShort("????????????");

            }

            @Override
            public void onError(int error, String errorMsg) {
            }
        });
    }

    /**
     * ????????????  ???????????????
     *
     * @param fieldName
     * @param fieldValue
     */
    private void toDoSaveGlucose(String fieldName, String fieldValue, TextView textView, BaseViewHolder help) {
        Map<String, Object> map = new HashMap<>();
        map.put("fieldname", fieldName);
        map.put("fieldvalue", fieldValue);
        XyUrl.okPostSave(XyUrl.PERSONAL_SAVE_GLUCOSE, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String msg) {
                MySugarFilesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MultiItemEntity multiItemEntity1 = multiItemEntityArrayList.get(help.getBindingAdapterPosition());
                        HealthArchiveGroupLevelOneBean levelOneBean1 = (HealthArchiveGroupLevelOneBean) multiItemEntity1;
                        levelOneBean1.setContent(fieldValue);
                        switch (fieldName) {
                            //OGTT2h??????
                            case "xtogtt2h":
                                //H b A 1 c
                            case "xthbalc":
                                //????????????
                            case "xtsuiji":
                                //????????????
                            case "xtkongfu":
                                //??????2h??????
                            case "xtcaihou":
                                //????????????
                            case "xtyejian":
                                //????????????
                            case "xtshuiqian":
                                //????????????
                            case "sytc":
                                //????????????
                            case "sytg":
                                //??????????????????
                            case "syldl":
                                //???????????????(ALT)
                            case "syalt":
                                //???????????????(AST)
                            case "syast":
                                //????????????(T-BIL)
                            case "sytbil":
                                //??????????????????(HDL-C)
                            case "syhdl":
                                //???????????????(ALP)
                            case "syalp":
                                //?????????(?????????)
                            case "syncg":
                                //24??????????????????(24hUAE)
                            case "syhuae":
                                //????????????/??????(ACR)
                            case "syacr":
                                //????????????(UAE)
                            case "syuae":
                                //?????????(BUN)
                            case "synsd":
                                //?????????????????????(Ccr)
                            case "synsjg":
                                //????????????(SRC) ??????????????????
                            case "syxqjg":
                            case "syxqjgg":
                                resetValue(help, textView, fieldValue);
                                break;
                            //???????????????????????????
                            case "xtdi":
                                if ("1".equals(fieldValue)) {
                                    textView.setText("???");
                                    levelOneBean1.setContent("???");
                                } else {
                                    textView.setText("???");
                                    levelOneBean1.setContent("???");
                                }
                                break;

                            default:
                                break;
                        }
                    }
                });

                ToastUtils.showShort(msg);
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });
    }

    /**
     * ?????????/?????????  ????????????
     *
     * @param fieldName
     * @param fieldValue
     */
    private void toDoSaveBINGFA(String fieldName, String fieldValue, TextView textView, BaseViewHolder help) {
        Map<String, Object> map = new HashMap<>();
        map.put("fieldname", fieldName);
        map.put("fieldvalue", fieldValue);
        XyUrl.okPostSave(XyUrl.PERSONAL_SAVE_BINGFA, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String msg) {
                MySugarFilesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MultiItemEntity multiItemEntity1 = multiItemEntityArrayList.get(help.getBindingAdapterPosition());
                        HealthArchiveGroupLevelOneBean levelOneBean1 = (HealthArchiveGroupLevelOneBean) multiItemEntity1;
                        levelOneBean1.setContent(fieldValue);
                        switch (fieldName) {
                            //????????????????????????
                            case "nephropathy":
                                //???????????????
                            case "retina":
                                //?????????????????????
                            case "nerve":
                                //???????????????????????????
                            case "legs":
                                //????????????
                            case "diabeticfoot":
                                //????????????????????????
                            case "ketoacidosis":
                                //????????????????????????
                            case "hypertonic":
                                //?????????
                            case "highblood":
                                //?????????
                            case "hlp":
                                //?????????
                            case "coronary":
                                //????????????
                            case "cerebrovascular":
                                if ("1".equals(fieldValue)) {
                                    textView.setText("?????????");
                                    levelOneBean1.setContent("?????????");
                                } else if ("2".equals(fieldValue)) {
                                    textView.setText("?????????");
                                    levelOneBean1.setContent("?????????");
                                } else {
                                    textView.setText("?????????");
                                    levelOneBean1.setContent("?????????");
                                }
                                break;

                            default:
                                break;
                        }
                    }
                });
                ToastUtils.showShort(msg);
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });
    }


    @Override
    protected void receiveEvent(EventMessage event) {
        super.receiveEvent(event);
        switch (event.getCode()) {
            case ConstantParam.ADD_MEDICINE:
                getHistory();
                break;
        }
    }


    private void resetValue(BaseViewHolder help, TextView textView, String value) {
        HealthArchiveGroupLevelOneBean bean = (HealthArchiveGroupLevelOneBean) adapter.getItem(help.getAdapterPosition());
        String unit = bean.getUnit();
        SpannableString spannableString = new SpannableString(value + " " + unit);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.gray_text));
        spannableString.setSpan(colorSpan, 0, value.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }


    private void resetBmiHeight(BaseViewHolder help, String value) {
        String height = SPStaticUtils.getString("height");
        //        String height = value;
        String weight = SPStaticUtils.getString("weight");
        Log.i("yys", "height===" + height + "weight===" + weight);

        NumberFormat nf = NumberFormat.getNumberInstance();
        //??????????????????
        nf.setMaximumFractionDigits(1);
        //??????????????????????????????????????????RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.HALF_UP);
        if (height != "" && weight != "") {
            String str = nf.format(Double.valueOf(weight) / Double.valueOf(height) / Double.valueOf(height) * 10000);
            SpannableString spannableString = new SpannableString(str + " " + "kg/m??");
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.gray_text));
            spannableString.setSpan(colorSpan, 0, str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            adapter.getTvBmi().setText(spannableString);
            MultiItemEntity multiItemEntity2 = multiItemEntityArrayList.get(help.getBindingAdapterPosition() + 2);
            HealthArchiveGroupLevelOneBean levelOneBean2 = (HealthArchiveGroupLevelOneBean) multiItemEntity2;
            levelOneBean2.setContent(str);
        }
    }

    private void resetBmiWeight(BaseViewHolder help, String type) {
        String height = SPStaticUtils.getString("height");
        String weight = SPStaticUtils.getString("weight");
        NumberFormat nf = NumberFormat.getNumberInstance();
        //??????????????????
        nf.setMaximumFractionDigits(1);
        //??????????????????????????????????????????RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.HALF_UP);
        if (height != "" && weight != "") {
            String str = nf.format(Double.valueOf(weight) / Double.valueOf(height) / Double.valueOf(height) * 10000);
            SpannableString spannableString = new SpannableString(str + " " + "kg/m??");
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.gray_text));
            spannableString.setSpan(colorSpan, 0, str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            adapter.getTvBmi().setText(spannableString);
            MultiItemEntity multiItemEntity2 = multiItemEntityArrayList.get(help.getBindingAdapterPosition() + 1);
            HealthArchiveGroupLevelOneBean levelOneBean2 = (HealthArchiveGroupLevelOneBean) multiItemEntity2;
            levelOneBean2.setContent(str);
        }

    }


    private void resetThr(BaseViewHolder help, String type) {
        String waistline = SPStaticUtils.getString("waistline");
        String hipline = SPStaticUtils.getString("hipline");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(1);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        if (waistline != "" && hipline != "") {
            String str = nf.format(Double.valueOf(waistline) / Double.valueOf(hipline));
            SpannableString spannableString = new SpannableString(str + " ");
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.gray_text));
            spannableString.setSpan(colorSpan, 0, str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            if ("waistline".equals(type)) {
                MultiItemEntity multiItemEntity2 = multiItemEntityArrayList.get(help.getBindingAdapterPosition() + 2);
                HealthArchiveGroupLevelOneBean levelOneBean2 = (HealthArchiveGroupLevelOneBean) multiItemEntity2;
                levelOneBean2.setContent(str);
            } else {
                MultiItemEntity multiItemEntity2 = multiItemEntityArrayList.get(help.getBindingAdapterPosition() + 1);
                HealthArchiveGroupLevelOneBean levelOneBean2 = (HealthArchiveGroupLevelOneBean) multiItemEntity2;
                levelOneBean2.setContent(str);
            }
            adapter.getTvThr().setText(spannableString);
        }
    }


    private String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String strTime = "";
        if (0 == time) {
            strTime = "?????????";
        } else {
            strTime = TimeUtils.millis2String(time * 1000L, format);
        }
        return strTime;
    }


    @OnClick({R.id.ll_medicine_history, R.id.ll_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_medicine_history:
                toToggle(elMedicineHistory, imgMedicineHistory);
                break;
            case R.id.ll_add:
                Intent intent = new Intent(getPageContext(), PharmacyAddActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
        }
    }

    /**
     * ??????
     *
     * @param el
     * @param img
     */
    private void toToggle(ExpandableLayout el, ImageView img) {
        el.toggle();
        Bitmap bitmap = ImageUtils.getBitmap(R.drawable.right_arrow);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (el.isExpanded()) {
            Bitmap resizedBitmap = ImageUtils.rotate(bitmap, 90, width, height);
            img.setImageBitmap(resizedBitmap);
        } else {
            Bitmap resizedBitmap = ImageUtils.rotate(bitmap, 0, width, height);
            img.setImageBitmap(resizedBitmap);
        }
    }


    @Override
    public void processHandlerMsg(Message msg) {
        switch (msg.what) {
            case GET_DATA:
                personalRecordBean = (PersonalRecordBean) msg.obj;
                break;
            //??????????????????
            case GET_DATA_MEDICINE_HISTORY:
                List<PersonalRecordMedicineHistoryBean> list = (List<PersonalRecordMedicineHistoryBean>) msg.obj;
                addData(personalRecordBean, list);
                break;
            //?????????????????????
            case GET_DATA_MEDICINE_HISTORY_ERROR:
                List<PersonalRecordMedicineHistoryBean> listEmpty = new ArrayList<>();
                addData(personalRecordBean, listEmpty);
                break;
            default:
                break;
        }
    }
}
