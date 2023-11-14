package com.vice.bloodpressure.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.lyd.baselib.utils.eventbus.EventBusUtils;
import com.quinovaresdk.bletransfer.BleTransfer;
import com.quinovaresdk.bletransfer.Injection;
import com.vice.bloodpressure.event.BlueBindEvent;
import com.vice.bloodpressure.event.BlueConnectEvent;
import com.vice.bloodpressure.event.BlueHistoryDataEvent;
import com.vice.bloodpressure.event.BlueUnbindEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class BlueUtils {
    //判断是否连接
    public static boolean isConnect = false;

    public static void init(Activity activity, Handler handler) {
        String ally_key = "f793807d88871d1684617915991b245c";
        BleTransfer.getInstance().init(activity, ally_key);
        BleTransfer.getInstance().realInit();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!isConnect) {
                    if (!TextUtils.isEmpty(getBlueMac())) {
                        BleTransfer.getInstance().realConnect(getBlueMac());
                    }
                }
            }
        }, 2_000);
    }

    public static String getBlueMac() {
        String mac = "";
        Object blueDeviceMac = SPUtils.getBean("BlueDeviceMac");
        if (blueDeviceMac != null) {
            mac = (String) blueDeviceMac;
        }
        return mac;
    }

    //判断是否绑定
    public static boolean isBind() {
        boolean blueBindState = BlueUtils.getBoolean(Utils.getApp(),"blueBindState",false);
        return blueBindState;
    }

    public static void callBackListener(Activity activity, Handler handler) {
        BleTransfer.getInstance().setOnDataCallBackListetner(new BleTransfer.OnDataCallListener() {
            @Override
            public void onSdkAuthcation(final int code, final String message) {

            }

            @Override
            public void onDeviceAuthcation(final int code, final String message) {

            }

            @Override
            public void onDeviceConnected() {
                Log.i("yys", "已连接");
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        //连接成功
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isConnect = true;
                                Log.i("yys", "bind");
                                EventBusUtils.post(new BlueConnectEvent(true));
                            }
                        }, 1_000);
                    }
                });
            }

            @Override
            public void onDeviceDisConnected() {
                Log.i("yys", "连接断开");
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        isConnect = false;
                        BleTransfer.getInstance().close();
                        EventBusUtils.post(new BlueConnectEvent(false));
                    }
                });
            }

            @Override
            public void onServicesDisCovered() {

            }

            @Override
            public void onDeviceNotSupport() {
                BleTransfer.getInstance().disconnect();
            }

            @Override
            public void onBindDeviceStatus(final String code, final String message) {
                Log.i("yys", "绑定状态==" + code + "," + message);
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if ("00".equals(code)) {
                            //绑定成功
                            BlueUtils.putBoolean(Utils.getApp(),"blueBindState",true);
                            EventBusUtils.post(new BlueBindEvent(true));
                        } else {

                        }

                    }
                });
            }

            @Override
            public void onUnBindDeviceStatus(final String code, final String message) {

                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Log.i("yys", "解绑状态==" + code + "," + message);
                        if (TextUtils.equals("00", code)) {
                            BlueUtils.putBoolean(Utils.getApp(),"blueBindState",false);
                            EventBusUtils.post(new BlueUnbindEvent(true));
                        }
                    }
                });

            }

            @Override
            public void onSetBuzzerParamStatus(final String code, final String message) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        //                        tv_sendButton003.setText(code + "," + message);
                    }
                });

            }

            @Override
            public void onSetMedicinalType(final String code, final String message) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        //                        tv_sendButton004.setText(code + "," + message);
                    }
                });

            }

            @Override
            public void onGetPressedStatus(final String code, final String message) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        //                        tv_sendButton01.setText(code + "," + message);
                    }
                });
            }

            @Override
            public void onBatteryVolStatus(final byte vol) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        //获取设备电量
                    }
                });
            }

            @Override
            public void onGetBuzzerParamStatus(final byte kaiguan, final byte jiange) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        //                        tv_sendButton03.setText(kaiguan + "," + jiange);
                    }
                });
            }

            @Override
            public void onGetDeviceInfo(final String vender, final String hardware, final float ver) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        //获取设备信息

                    }
                });
            }

            @Override
            public void onProductInfo(final byte vol, final short productType, final String productName) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        //获取产品信息
                        //                        tv_sendButton02_1.setText(vol + "," + productType + "," + productName);
                    }
                });

            }

            @Override
            public void onGetDeviceTime(final short year, final byte month, final byte day, final byte hour, final byte minute, final byte second) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        //获取设备本地时间
                        //                        tv_sendButton05.setText(year + "年" + month + "月" + day + "日"
                        //                        + hour + "时" + minute + "分" + second + "秒");
                    }
                });
            }

            @Override
            public void onSetDeviceTime() {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        //设置设备本地时间
                        //                        tv_sendButton06.setText("成功");
                    }
                });
            }

            @Override
            public void onHistoryInjection(final ArrayList<Injection> datas) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Log.i("yys", "onHistoryInjection");
                        //获取历史注射记录
                        //                        StringBuffer stringBuffer = new StringBuffer();
                        List<BlueHistoryDataEvent.insulis> list = new ArrayList<>();
                        for (int i = 0; i < datas.size(); i++) {
                            Injection injection = datas.get(i);
                            list.add(new BlueHistoryDataEvent.insulis(
                                    injection.getYear() + "-" +
                                            injection.getMonth() + "-" +
                                            injection.getDay() + " " +
                                            injection.getHour() + ":" +
                                            injection.getSecond()
                                    , injection.getDosage() + ""));
                            //                            String text = injection.getYear() + ","
                            ////                                    + injection.getMonth() + ","
                            ////                                    + injection.getDay() + ","
                            ////                                    + injection.getHour() + ","
                            ////                                    + injection.getMinute() + ","
                            ////                                    + injection.getSecond() + ","
                            ////                                    + injection.getDosage() + ","
                            ////                                    + injection.getType();
                            ////
                            ////                            stringBuffer.append(text + "\n");
                        }
                        //                        Log.i("yys", "获取历史注射记录=="+stringBuffer.toString());
                        EventBusUtils.post(new BlueHistoryDataEvent(list, 1));
                        //                        tv_sendButton08.setText("" + stringBuffer.toString());
                    }
                });
            }


            @Override
            public void onInjection(final Injection injection) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        //注射记录通知
                        //                        String text = injection.getYear() + ","
                        //                                + injection.getMonth() + ","
                        //                                + injection.getDay() + ","
                        //                                + injection.getHour() + ","
                        //                                + injection.getMinute() + ","
                        //                                + injection.getSecond() + ","
                        //                                + injection.getDosage() + ","
                        //                                + injection.getType();
                        BlueHistoryDataEvent.insulis insulis = new BlueHistoryDataEvent.insulis(
                                injection.getYear() + "-" +
                                        injection.getMonth() + "-" +
                                        injection.getDay() + " " +
                                        injection.getHour() + ":" +
                                        injection.getSecond()
                                , injection.getDosage() + "");
                        EventBusUtils.post(new BlueHistoryDataEvent(2, insulis));
                    }
                });

            }
        });

    }


    private static final String SP_NAME = "sp_name_blue";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
        SharedPreferences.Editor edit = getSharedPreferences(context).edit();
        return edit;
    }

    public static boolean getBoolean(Context context, String key, boolean value) {
        return getSharedPreferences(context).getBoolean(key, value);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        getSharedPreferencesEditor(context).putBoolean(key, value).commit();
    }
}
