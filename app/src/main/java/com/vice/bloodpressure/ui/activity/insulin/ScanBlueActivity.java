package com.vice.bloodpressure.ui.activity.insulin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.bean.insulin.RecordBigInfo;
import com.vice.bloodpressure.bean.insulin.RecordErrorInfo;
import com.vice.bloodpressure.bean.insulin.RecordInfo;
import com.vice.bloodpressure.utils.BleUtils;

import java.util.List;

public class ScanBlueActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_blue);
        findViewById(R.id.tv_close).setOnClickListener(view -> {
            BleUtils.getInstance().disConnect();
        });

        findViewById(R.id.tv_send_0).setOnClickListener(view -> {
            BleUtils.getInstance().connect(getPageContext(), "D0:FB:6F:A0:D9:8D", new BleUtils.OnDataCallBackImpl() {
                @Override
                public void connect() {
                    runOnUiThread(() -> {
                        new Handler().postDelayed(() -> BleUtils.getInstance().sendData("0577A00348"), 1_000);
                    });
                }

                @Override
                public void onSerialNum(String serialNum) {
                    //获取设备号
                    runOnUiThread(() -> {
                        TextView textView = findViewById(R.id.tv_send_0);
                        textView.setText(serialNum);
                        BleUtils.getInstance().disConnect();
                    });
                }
            });
        });
        findViewById(R.id.tv_send_1).setOnClickListener(view -> {
            BleUtils.getInstance().connect(getPageContext(), "D0:FB:6F:A0:D9:8D", new BleUtils.OnDataCallBackImpl() {
                @Override
                public void connect() {
                    super.connect();
                    runOnUiThread(() -> {
                        new Handler().postDelayed(() -> BleUtils.getInstance().sendData("0577A11369"), 1_000);
                    });
                }

                @Override
                public void onWorkState(String ele, byte drugHeight, byte drugLow, String isBlock, String infuSwitch) {
                    //电量  药高位  药低位  阻塞标志阻塞 0A 0B  输注开关 开（A5) 关（B5)
                    runOnUiThread(() -> {
                        TextView textView = findViewById(R.id.tv_send_1);
                        textView.setText(ele + drugHeight + drugLow + isBlock + infuSwitch);
                        BleUtils.getInstance().disConnect();
                    });
                }
            });
        });
        findViewById(R.id.tv_send_2).setOnClickListener(view -> {
            //回传基础模式、当前时段基础率、本时段已输注基础量
            BleUtils.getInstance().connect(getPageContext(), "D0:FB:6F:A0:D9:8D", new BleUtils.OnDataCallBackImpl() {
                @Override
                public void connect() {
                    super.connect();
                    runOnUiThread(() -> {
                        new Handler().postDelayed(() -> BleUtils.getInstance().sendData("0577A2230A"), 1_000);
                    });
                }

                @Override
                public void onBaseData(String baseState, String baseValue, String baseValueAll) {
                    //基础模式  当前时段基础率  本时段已输注基础量
                    runOnUiThread(() -> {
                        TextView textView = findViewById(R.id.tv_send_2);
                        textView.setText(baseState + baseValue + baseValueAll);
                        BleUtils.getInstance().disConnect();
                    });
                }
            });
        });
        findViewById(R.id.tv_send_3).setOnClickListener(view -> {
            //回传基础率1
            BleUtils.getInstance().connect(getPageContext(), "D0:FB:6F:A0:D9:8D", new BleUtils.OnDataCallBackImpl() {
                @Override
                public void connect() {
                    super.connect();
                    runOnUiThread(() -> {
                        new Handler().postDelayed(() -> BleUtils.getInstance().sendData("0577A3332B"), 1_000);
                    });
                }

                @Override
                public void onBaseRate(List<String> baseRateList) {
                    super.onBaseRate(baseRateList);
                    //基础率1
                    runOnUiThread(() -> {
                        TextView textView = findViewById(R.id.tv_send_3);
                        textView.setText(baseRateList.size() + "");
                        BleUtils.getInstance().disConnect();
                    });
                }
            });
        });
        findViewById(R.id.tv_send_4).setOnClickListener(view -> {
            //回传基础率2
            BleUtils.getInstance().connect(getPageContext(), "D0:FB:6F:A0:D9:8D", new BleUtils.OnDataCallBackImpl() {
                @Override
                public void connect() {
                    super.connect();
                    runOnUiThread(() -> {
                        new Handler().postDelayed(() -> BleUtils.getInstance().sendData("0577A443CC"), 1_000);
                    });
                }

                @Override
                public void onBaseRate(List<String> baseRateList) {
                    super.onBaseRate(baseRateList);
                    //基础率1
                    runOnUiThread(() -> {
                        TextView textView = findViewById(R.id.tv_send_4);
                        textView.setText(baseRateList.size() + "");
                        BleUtils.getInstance().disConnect();
                    });
                }
            });
        });
        findViewById(R.id.tv_send_5).setOnClickListener(view -> {
            //回传日总量记录
            BleUtils.getInstance().connect(getPageContext(), "D0:FB:6F:A0:D9:8D", new BleUtils.OnDataCallBackImpl() {
                @Override
                public void connect() {
                    super.connect();
                    runOnUiThread(() -> {
                        new Handler().postDelayed(() -> BleUtils.getInstance().sendData("0577A88240"), 1_000);
                    });
                }

                @Override
                public void onRecordInfoList(List<RecordInfo> recordInfoList) {
                    super.onRecordInfoList(recordInfoList);
                    runOnUiThread(() -> {
                        TextView textView = findViewById(R.id.tv_send_5);
                        textView.setText(recordInfoList.size() + "");
                        BleUtils.getInstance().disConnect();
                    });
                }
            });

        });
        findViewById(R.id.tv_send_6).setOnClickListener(view -> {
            //回传大剂量记录
            BleUtils.getInstance().connect(getPageContext(), "D0:FB:6F:A0:D9:8D", new BleUtils.OnDataCallBackImpl() {
                @Override
                public void connect() {
                    super.connect();
                    runOnUiThread(() -> {
                        new Handler().postDelayed(() -> BleUtils.getInstance().sendData("0577A99261"), 1_000);
                    });
                }

                @Override
                public void onRecordBigInfoList(List<RecordBigInfo> recordBigInfoList) {
                    super.onRecordBigInfoList(recordBigInfoList);
                    runOnUiThread(() -> {
                        TextView textView = findViewById(R.id.tv_send_6);
                        textView.setText(recordBigInfoList.size() + "");
                        BleUtils.getInstance().disConnect();
                    });
                }
            });
        });
        findViewById(R.id.tv_send_7).setOnClickListener(view -> {
            //基础量记录
            BleUtils.getInstance().connect(getPageContext(), "D0:FB:6F:A0:D9:8D", new BleUtils.OnDataCallBackImpl() {
                @Override
                public void connect() {
                    super.connect();
                    runOnUiThread(() -> {
                        new Handler().postDelayed(() -> BleUtils.getInstance().sendData("0577AAA202"), 1_000);
                    });
                }

                @Override
                public void onRecordInfoList(List<RecordInfo> recordInfoList) {
                    super.onRecordInfoList(recordInfoList);
                    runOnUiThread(() -> {
                        TextView textView = findViewById(R.id.tv_send_7);
                        textView.setText(recordInfoList.size() + "");
                        BleUtils.getInstance().disConnect();
                    });
                }
            });
        });
        findViewById(R.id.tv_send_8).setOnClickListener(view -> {
            //回传报警记录
            BleUtils.getInstance().connect(getPageContext(), "D0:FB:6F:A0:D9:8D", new BleUtils.OnDataCallBackImpl() {
                @Override
                public void connect() {
                    super.connect();
                    runOnUiThread(() -> {
                        new Handler().postDelayed(() -> BleUtils.getInstance().sendData("0577ABB223"), 1_000);
                    });
                }

                @Override
                public void onRecordErrorList(List<RecordErrorInfo> recordErrorInfos) {
                    super.onRecordErrorList(recordErrorInfos);
                    runOnUiThread(() -> {
                        TextView textView = findViewById(R.id.tv_send_8);
                        textView.setText(recordErrorInfos.size() + "");
                        BleUtils.getInstance().disConnect();
                    });
                }
            });
        });
    }

    private Context getPageContext() {
        return this;
    }

}

