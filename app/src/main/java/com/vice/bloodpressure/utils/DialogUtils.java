package com.vice.bloodpressure.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.utils.edittext.IdNumberKeyListener;


/**
 * 描述:QMUIDialog封装的工具类
 * 作者: LYD
 * 所需参数: 无
 * 创建日期: 2018/10/22 16:18
 */
public class DialogUtils {

    private static volatile DialogUtils instance = null;

    private DialogUtils() {
    }

    /**
     * 懒汉单例(双重检查)
     *
     * @return
     */
    public static DialogUtils getInstance() {
        if (instance == null) {
            synchronized (DialogUtils.class) {
                if (instance == null) {
                    instance = new DialogUtils();
                }
            }
        }
        return instance;
    }


    /**
     * 显示不可取消的Dialog
     *
     * @param context
     * @param titleInfo
     * @param messageInfo
     * @param isHaveCancel
     * @param callBack
     */
    public void showNotCancelDialog(Context context, String titleInfo, String messageInfo, boolean isHaveCancel, final DialogUtils.DialogCallBack callBack) {
        QMUIDialog.MessageDialogBuilder messageDialog = new QMUIDialog.MessageDialogBuilder(context);
        messageDialog.setActionDivider(1, R.color.qmui_config_color_separator, 0, 0);//添加分割线
        messageDialog.setTitle(titleInfo);//设置标题
        messageDialog.setMessage(messageInfo);//设置提示信息
        messageDialog.setCanceledOnTouchOutside(false);
        messageDialog.setCancelable(false);
        if (isHaveCancel) {
            messageDialog.addAction("取消", (dialog, index) -> dialog.dismiss());
        }
        messageDialog.addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, (dialog, index) -> {
            callBack.execEvent();//设置确定的回调
            dialog.dismiss();
        });
        messageDialog.create(R.style.DialogThemeCommon).show();
    }

    /**
     * 显示提示信息:居中
     *
     * @param context     上下文
     * @param titleInfo   标题
     * @param messageInfo 提示信息
     * @param callBack    确定回调
     */
    public void showDialog(Context context, String titleInfo, String messageInfo, boolean isHaveCancel, final DialogUtils.DialogCallBack callBack) {
        showDialog(context, titleInfo, messageInfo, isHaveCancel, callBack, R.style.DialogThemeCommon);
    }

    public void showDialog1(Context context, String titleInfo, String messageInfo, boolean isHaveCancel, final DialogUtils.DialogCallBack callBack) {
        showDialog1(context, titleInfo, messageInfo, isHaveCancel, callBack, R.style.DialogThemeCommon);
    }

    /**
     * 显示提示信息  这个Dialog在oppo手机上弹不出，换成Dialog1（）这个方法，需要把弹框的高度写死，但是没有抛出设置高度的方法。故被弃用
     *
     * @param context
     * @param titleInfo
     * @param messageInfo
     * @param callBack
     */
    public void showDialog(Context context, String titleInfo, String messageInfo, boolean isHaveCancel, final DialogUtils.DialogCallBack callBack, int style) {
        QMUIDialog.MessageDialogBuilder messageDialog = new QMUIDialog.MessageDialogBuilder(context);
        messageDialog.setActionDivider(1, R.color.qmui_config_color_separator, 0, 0);//添加分割线
        messageDialog.setTitle(titleInfo);//设置标题
        messageDialog.setMessage(messageInfo);//设置提示信息
        messageDialog.setCanceledOnTouchOutside(false);
        messageDialog.setCancelable(false);
        if (isHaveCancel) {
            messageDialog.addAction("取消", (dialog, index) -> dialog.dismiss());
        }
        messageDialog.addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, (dialog, index) -> {
            callBack.execEvent();//设置确定的回调
            dialog.dismiss();
        });
        messageDialog.create(style).show();
    }

    /**
     * 这个方法是显示提示信息 的替换方法
     *
     * @param context
     * @param titleInfo
     * @param messageInfo
     * @param isHaveCancel
     * @param callBack
     * @param style
     */
    public void showDialog1(Context context, String titleInfo, String messageInfo, boolean isHaveCancel, final DialogUtils.DialogCallBack callBack, int style) {
        Dialog commonDialog = new Dialog(context, R.style.Dialog_Base);
        View view = View.inflate(context, R.layout.dialog_common, null);
        commonDialog.setContentView(view);
        WindowManager.LayoutParams attributes = commonDialog.getWindow().getAttributes();
        attributes.width = 4 * ScreenUtils.screenWidth(context) / 5;

        attributes.height = ScreenUtils.dip2px(context, 150);
        commonDialog.getWindow().setAttributes(attributes);
        commonDialog.setCancelable(false);

        TextView titleTextView = view.findViewById(R.id.tv_common_dialog_title);
        TextView contentTextView = view.findViewById(R.id.tv_common_dialog_content);
        TextView sureTextView = view.findViewById(R.id.tv_common_dialog_sure);
        TextView unsureTextView = view.findViewById(R.id.tv_common_dialog_unsure);
        View lineView = view.findViewById(R.id.view_common_dialog);
        if (isHaveCancel) {
            lineView.setVisibility(View.VISIBLE);
            unsureTextView.setVisibility(View.VISIBLE);
        } else {
            lineView.setVisibility(View.GONE);
            unsureTextView.setVisibility(View.GONE);
        }
        if ("".equals(titleInfo)) {
            titleTextView.setText("提示");
        } else {
            titleTextView.setText(titleInfo);
        }
        contentTextView.setText(messageInfo);


        unsureTextView.setOnClickListener(v -> {
            commonDialog.dismiss();
        });
        sureTextView.setOnClickListener(v -> {
            callBack.execEvent();//设置确定的回调
            commonDialog.dismiss();
        });
        commonDialog.show();

    }


    /**
     * 被弃用
     *
     * @param context
     * @param titleInfo
     * @param messageInfo
     * @param leftBtnInfo
     * @param rightBtnInfo
     * @param leftCallBack
     * @param rightCallBack
     */
    public void showCommonDialog(Context context,
                                 String titleInfo,
                                 String messageInfo,
                                 String leftBtnInfo,
                                 String rightBtnInfo,
                                 final DialogUtils.DialogCallBack leftCallBack,
                                 final DialogUtils.DialogCallBack rightCallBack) {
        QMUIDialog.MessageDialogBuilder messageDialog = new QMUIDialog.MessageDialogBuilder(context);
        messageDialog.setActionDivider(1, R.color.qmui_config_color_separator, 0, 0);//添加分割线
        messageDialog.setTitle(titleInfo);//设置标题
        messageDialog.setMessage(messageInfo);//设置提示信息
        messageDialog.addAction(leftBtnInfo, (dialog, index) -> {
            leftCallBack.execEvent();//设置确定的回调
            dialog.dismiss();
        });
        messageDialog.addAction(0, rightBtnInfo, QMUIDialogAction.ACTION_PROP_NEGATIVE, (dialog, index) -> {
            rightCallBack.execEvent();//设置确定的回调
            dialog.dismiss();
        });
        messageDialog.create(R.style.DialogThemeCommon).show();
    }

    /**
     * @param context
     * @param titleInfo
     * @param messageInfo
     * @param leftBtnInfo
     * @param rightBtnInfo
     * @param leftCallBack
     * @param rightCallBack
     */
    public void showCommonDialog1(Context context,
                                  String titleInfo,
                                  String messageInfo,
                                  String leftBtnInfo,
                                  String rightBtnInfo,
                                  final DialogUtils.DialogCallBack leftCallBack,
                                  final DialogUtils.DialogCallBack rightCallBack) {
        Dialog commonDialog = new Dialog(context, R.style.Dialog_Base);
        View view = View.inflate(context, R.layout.dialog_common, null);
        commonDialog.setContentView(view);
        WindowManager.LayoutParams attributes = commonDialog.getWindow().getAttributes();
        attributes.width = 4 * ScreenUtils.screenWidth(context) / 5;

        attributes.height = ScreenUtils.dip2px(context, 150);
        commonDialog.getWindow().setAttributes(attributes);
        commonDialog.setCancelable(false);

        TextView titleTextView = view.findViewById(R.id.tv_common_dialog_title);
        TextView contentTextView = view.findViewById(R.id.tv_common_dialog_content);
        TextView leftTextView = view.findViewById(R.id.tv_common_dialog_sure);
        TextView rightTextView = view.findViewById(R.id.tv_common_dialog_unsure);
        View lineView = view.findViewById(R.id.view_common_dialog);

        leftTextView.setTextColor(ContextCompat.getColor(context, R.color.webview_progress));
        rightTextView.setTextColor(ContextCompat.getColor(context, R.color.main_red));
        leftTextView.setText(leftBtnInfo);
        rightTextView.setText(rightBtnInfo);
        if ("".equals(titleInfo)) {
            titleTextView.setText("提示");
        } else {
            titleTextView.setText(titleInfo);
        }
        contentTextView.setText(messageInfo);


        rightTextView.setOnClickListener(v -> {
            rightCallBack.execEvent();//设置取消的回调
            commonDialog.dismiss();
        });
        leftTextView.setOnClickListener(v -> {
            leftCallBack.execEvent();//设置确定的回调
            commonDialog.dismiss();
        });
        commonDialog.show();
    }


    /**
     * 显示输入Dialog
     *
     * @param context
     * @param title
     * @param hint
     * @param callBack
     */
    public void showEditTextDialog(Context context, String title, String hint, final DialogUtils.DialogInputCallBack callBack) {
        showEditTextDialog(context, title, hint, InputType.TYPE_CLASS_TEXT, callBack);
    }


    /**
     * 显示输入Dialog
     *
     * @param context
     * @param title
     * @param hint
     * @param type
     * @param callBack
     */
    public void showEditTextDialog(Context context, String title, String hint, int type, final DialogUtils.DialogInputCallBack callBack) {
        QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(context);
        builder.setTitle(title)
                .setPlaceholder(hint)
                .setInputType(type)
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        String text = builder.getEditText().getText().toString().trim();
                        if (TextUtils.isEmpty(text)) {
                            ToastUtils.showShort(hint);
                        } else {
                            callBack.execEvent(text);
                            dialog.dismiss();
                        }
                    }
                })
                .create(R.style.DialogThemeCommon).show();

    }

    /**
     * @param context
     * @param title
     * @param hint
     * @param callBack
     */
    public void showDecimalNumberInputDialog(Context context, String title, String hint, final DialogUtils.DialogInputCallBack callBack) {
        QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(context);
        builder.setTitle(title)
                .setPlaceholder(hint)
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        String text = builder.getEditText().getText().toString().trim();
                        if (TextUtils.isEmpty(text)) {
                            ToastUtils.showShort(hint);
                        } else {
                            callBack.execEvent(text);
                            dialog.dismiss();
                        }
                    }
                })
                .create(R.style.DialogThemeCommon).show();
        EditText editText = builder.getEditText();
        editText.setInputType(8194);
    }


    public void showEditTextIdNumberDialog(Context context, String title, String hint, final DialogUtils.DialogInputCallBack callBack) {
        QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(context);
        builder.setTitle(title)
                .setPlaceholder(hint)
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        String text = builder.getEditText().getText().toString().trim();
                        if (TextUtils.isEmpty(text)) {
                            ToastUtils.showShort(hint);
                        } else {
                            callBack.execEvent(text);
                            dialog.dismiss();
                        }
                    }
                })
                .create(R.style.DialogThemeCommon).show();
        EditText editText = builder.getEditText();
        editText.setKeyListener(new IdNumberKeyListener());
    }

    public interface DialogCallBack {
        void execEvent();
    }

    public interface DialogInputCallBack {
        void execEvent(String text);
    }


    /**
     * 本项目使用
     *
     * @param context
     * @param tip      不传，不现实title
     * @param msg
     * @param negative 左边文字
     * @param positive 右边文字
     * @param callback
     */
    public static void showOperDialog(Context context, String tip, String msg, String negative, String positive, CommonDialog.SingleButtonCallback callback) {
        CommonDialog commonDialog = new CommonDialog(context);
        commonDialog.setTitle(R.string.tip);
        commonDialog.setMessage(msg);
        commonDialog.setNegative(negative);
        commonDialog.setPositive(positive);
        commonDialog.onAny(callback);
        commonDialog.show();
    }


    public static Dialog editDialog(Context context, String title, String hintMsg,
                                    String content, int inputType, int inputSize,
                                    DialogInputCallBack dialogInputCallBack) {
        Dialog dialog = new Dialog(context, R.style.Dialog_Base);
        View view = View.inflate(context, R.layout.input_user_info_dialog, null);
        TextView titleTextView = view.findViewById(R.id.tv_dialog_title);
        EditText msgEditText = view.findViewById(R.id.tv_dialog_msg);
        TextView cancelTextView = view.findViewById(R.id.tv_dialog_cancel);
        TextView sureTextView = view.findViewById(R.id.tv_dialog_sure);
        msgEditText.setFocusable(true);//设置输入框可聚集
        msgEditText.setFocusableInTouchMode(true);//设置触摸聚焦
        msgEditText.requestFocus();//请求焦点
        titleTextView.setText(title);
        msgEditText.setHint(hintMsg);
        Log.i("yys","content====="+content);

        // 如果传过来的hint是个空的，那就展示hint，如果不空，传过来的content有值，那就用content
        if (TextUtils.isEmpty(content)) {
            msgEditText.setHint(hintMsg);
        } else {
            msgEditText.setText(content);
            msgEditText.setSingleLine();
            msgEditText.setSelection(content.length());
        }

        //设置输入的类型
        msgEditText.setInputType(inputType);
        //设置最多可以输入多少
        if (0 == inputSize) {
            msgEditText.setMaxWidth(Integer.MAX_VALUE);
        } else {
            msgEditText.setMaxWidth(inputSize);
        }
        dialog.setContentView(view);
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.width = ScreenUtils.screenWidth(context) - ScreenUtils.dip2px(context, 60);
        attributes.height = ScreenUtils.dip2px(context, 190);
        dialog.getWindow().setAttributes(attributes);
        dialog.show();

        cancelTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        sureTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String content = msgEditText.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showShort(hintMsg);
                } else {
                    dialogInputCallBack.execEvent(content);
                    dialog.dismiss();
                }


            }
        });
        new Handler().postDelayed(() -> {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            //强制隐藏Android输入法窗口
            // inputManager.hideSoftInputFromWindow(edit.getWindowToken(),0);
        }, 100);
        return dialog;
    }


}
