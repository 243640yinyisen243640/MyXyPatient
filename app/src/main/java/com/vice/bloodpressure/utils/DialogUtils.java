package com.vice.bloodpressure.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
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

        leftTextView.setTextColor(ContextCompat.getColor(context,R.color.webview_progress));
        rightTextView.setTextColor(ContextCompat.getColor(context,R.color.main_red));
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
}
