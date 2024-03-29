package com.vice.bloodpressure.ui.fragment.login.registerandlogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.lyd.modulemall.ui.BaseWebViewActivity;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.fragment.BaseFragment;
import com.vice.bloodpressure.bean.RegisterQuestionAddBean;
import com.vice.bloodpressure.constant.ConstantParam;
import com.vice.bloodpressure.net.OkHttpCallBack;
import com.vice.bloodpressure.net.XyUrl;
import com.vice.bloodpressure.ui.activity.MainActivity;
import com.vice.bloodpressure.ui.activity.user.RegisterQuestionActivity;
import com.vice.bloodpressure.ui.fragment.login.forgetpwd.ResetPwdInputPhoneNumberFragment;
import com.vice.bloodpressure.ui.fragment.login.phoneoridcardlogin.LoginBindPhoneNumberFragment;
import com.vice.bloodpressure.ui.fragment.login.quicklogin.QuickLoginFragment;
import com.vice.bloodpressure.utils.SPUtils;
import com.vice.bloodpressure.utils.edittext.IdNumberKeyListener;
import com.vice.bloodpressure.view.popu.LoginAgreementPop;
import com.wei.android.lib.colorview.view.ColorTextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:注册Fragment
 * 作者: LYD
 * 创建日期: 2019/9/17 17:51
 */
public class LoginFragment extends BaseFragment {
    private static final String TAG = "LoginLoginFragment";
    private static final int LOGIN_SUCCESS = 10086;
    private static final int LOGIN_FAILED = 10010;
    @BindView(R.id.et_input_phone_or_id_card)
    EditText etInputPhoneOrIdCard;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_login)
    ColorTextView tvLogin;
    @BindView(R.id.tv_login_agreement)
    TextView agreeTextView;
    private String phoneOrIdCard;
    private String pwd;
    private String spanColor = "#333333";//隐私政策span颜色值

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login_login;
    }

    @Override
    protected void init(View rootView) {
        setTextChangeListener();
    }

    /**
     * 设置监听
     */
    private void setTextChangeListener() {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(getString(R.string.login_agreement_left));
        int startUser = stringBuilder.length();
        stringBuilder.append(getString(R.string.login_agreement_user_agreement));
        int endUser = stringBuilder.length();
        stringBuilder.append(getString(R.string.login_agreement_user_and));
        int startPrivacy = stringBuilder.length();
        stringBuilder.append(getString(R.string.login_agreement_privacy));
        int endPrivacy = stringBuilder.length();
        stringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getPageContext(), R.color.main_green)), startUser, endUser, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getPageContext(), R.color.main_green)), startPrivacy, endPrivacy, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        stringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(getPageContext(), BaseWebViewActivity.class);
                intent.putExtra("title", "用户服务协议");
                intent.putExtra("url", "file:///android_asset/user_protocol.html");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, startUser, endUser, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        stringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(getPageContext(), BaseWebViewActivity.class);
                intent.putExtra("title", "隐私政策");
                intent.putExtra("url", "http://chronics.xiyuns.cn/index/caseapp");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, startPrivacy, endPrivacy, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        agreeTextView.setMovementMethod(LinkMovementMethod.getInstance());
        agreeTextView.setText(stringBuilder);
        etInputPhoneOrIdCard.setKeyListener(new IdNumberKeyListener());
        //        TextWatcherUtils.addTextChangedListener(new TextWatcherUtils.OnTextChangedListener() {
        //            @Override
        //            public void onTextChanged(String etString) {
        //                if (etString.length() > 0) {
        //                    tvLogin.getColorHelper().setTextColorNormal(ColorUtils.getColor(R.color.white));
        //                    tvLogin.getColorHelper().setBackgroundColorNormal(ColorUtils.getColor(R.color.main_home));
        //                } else {
        //                    tvLogin.getColorHelper().setTextColorNormal(ColorUtils.getColor(R.color.gray_text));
        //                    tvLogin.getColorHelper().setBackgroundColorNormal(ColorUtils.getColor(R.color.color_eb));
        //                }
        //            }
        //        }, etInputPhoneOrIdCard, etPwd);
    }

    @OnClick({R.id.tv_register, R.id.tv_forget_pwd, R.id.tv_register_code_quick_login, R.id.tv_login, R.id.tv_login_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_register://注册
                LoginRegisterInputPhoneFragment loginRegisterStepOneFragment = new LoginRegisterInputPhoneFragment();
                FragmentUtils.replace(getParentFragmentManager(), loginRegisterStepOneFragment, R.id.fl_fragment, true);
                break;
            case R.id.tv_forget_pwd://忘记密码
                ResetPwdInputPhoneNumberFragment resetPwdInputPhoneNumberFragment = new ResetPwdInputPhoneNumberFragment();
                FragmentUtils.replace(getParentFragmentManager(), resetPwdInputPhoneNumberFragment, R.id.fl_fragment, true);
                break;
            case R.id.tv_register_code_quick_login://验证码快速登录
                QuickLoginFragment quickLoginInputPhoneNumberFragment = new QuickLoginFragment();
                FragmentUtils.replace(getParentFragmentManager(), quickLoginInputPhoneNumberFragment, R.id.fl_fragment, true);
                break;
            case R.id.tv_login://登录
                toCheckLogin();
                break;
            case R.id.tv_login_agreement:
                if (agreeTextView.isSelected()) {
                    agreeTextView.setSelected(false);
                    agreeTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.shopping_cart_uncheck, 0, 0, 0);
                } else {
                    agreeTextView.setSelected(true);
                    agreeTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.shopping_cart_checked, 0, 0, 0);
                }
                break;
            default:
                break;
            //            case R.id.rl_service_agreement://用户协议
            //                Intent intent = new Intent(getPageContext(), BaseWebViewActivity.class);
            //                intent.putExtra("title", "用户服务协议");
            //                intent.putExtra("url", "file:///android_asset/user_protocol.html");
            //                startActivity(intent);
            //                break;
        }
    }

    /**
     * 校验登录参数
     */
    private void toCheckLogin() {

        phoneOrIdCard = etInputPhoneOrIdCard.getText().toString().trim();
        if (TextUtils.isEmpty(phoneOrIdCard)) {
            ToastUtils.showShort("请输入手机号或身份证");
            return;
        }
        pwd = etPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShort("请输入密码");
            return;
        }
        //        if (!agreeTextView.isSelected()) {
        //            ToastUtils.showShort("请先勾选协议");
        //            return;
        //        }
        if (!agreeTextView.isSelected()) {
            LoginAgreementPop loginAgreementPop = new LoginAgreementPop(getPageContext());
            TextView contentTextView = loginAgreementPop.findViewById(R.id.tv_login_a_content);
            TextView unAgreeTextView = loginAgreementPop.findViewById(R.id.tv_login_a_un);
            TextView agreeTextView = loginAgreementPop.findViewById(R.id.tv_login_a_agree);
            String privacyProtectHint = getString(R.string.login_agreement);
            SpannableString ss = new SpannableString(privacyProtectHint);

            ss.setSpan(new UnderLineClickSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(getPageContext(), com.vice.bloodpressure.base.activity.BaseWebViewActivity.class);
                    intent.putExtra("title", "用户服务协议");
                    intent.putExtra("url", "file:///android_asset/user_protocol.html");
                    startActivity(intent);
                }
            }, privacyProtectHint.indexOf("《"), privacyProtectHint.indexOf("》") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.parseColor(spanColor)), privacyProtectHint.indexOf("《"), privacyProtectHint.indexOf("》") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            ss.setSpan(new UnderLineClickSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(getPageContext(), com.lyd.modulemall.ui.BaseWebViewActivity.class);
                    intent.putExtra("title", "隐私政策");
                    intent.putExtra("url", "http://chronics.xiyuns.cn/index/caseapp");
                    startActivity(intent);
                }
            }, privacyProtectHint.lastIndexOf("《"), privacyProtectHint.lastIndexOf("》") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.parseColor(spanColor)), privacyProtectHint.lastIndexOf("《"), privacyProtectHint.lastIndexOf("》") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            contentTextView.setText(ss);
            contentTextView.setHighlightColor(Color.TRANSPARENT);
            contentTextView.setMovementMethod(LinkMovementMethod.getInstance());
            unAgreeTextView.setOnClickListener(v -> {
                loginAgreementPop.dismiss();

            });
            agreeTextView.setOnClickListener(v -> {
                toDoLogin();
            });
            loginAgreementPop.showPopupWindow();
            return;
        }
        toDoLogin();
    }

    private abstract class UnderLineClickSpan extends ClickableSpan {
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
            ds.clearShadowLayer();
        }
    }

    /**
     * 去登录
     */
    private void toDoLogin() {
        HashMap map = new HashMap<>();
        map.put("username", phoneOrIdCard);
        map.put("password", pwd);
        XyUrl.okPost(XyUrl.LOGIN_NEW, map, new OkHttpCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                LoginBean loginBean = JSONObject.parseObject(value, LoginBean.class);
                Message message = getHandlerMessage();
                message.what = LOGIN_SUCCESS;
                message.obj = loginBean;
                sendHandlerMessage(message);
            }

            @Override
            public void onError(int error, String errorMsg) {
                sendHandlerMessage(LOGIN_FAILED);
                ToastUtils.showShort(errorMsg);
                //                Toast.makeText(getPageContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 检查是否绑定 手机号  身份证  密码  答题
     *
     * @param loginBean
     */
    private void checkIsBindIdCardOrPwd(LoginBean loginBean) {
        String username = loginBean.getUsername();//手机号
        String idcard = loginBean.getIdcard();//身份证号
        String password = loginBean.getPassword();//1第一次注册登录，未设置密码  2已设置密码,
        String isOne = loginBean.getIs_one();//1:未答题 2:已答题
        if (TextUtils.isEmpty(username)) {
            LoginBindPhoneNumberFragment loginBindPhoneNumberFragment = new LoginBindPhoneNumberFragment();
            FragmentUtils.replace(getParentFragmentManager(), loginBindPhoneNumberFragment, R.id.fl_fragment, true);
        } else if (TextUtils.isEmpty(idcard)) {
            LoginBindIdCardFragment loginBindIdCaraFragment = new LoginBindIdCardFragment();
            FragmentUtils.replace(getParentFragmentManager(), loginBindIdCaraFragment, R.id.fl_fragment, true);
        } else if ("1".equals(password)) {
            LoginBindPwdFragment loginBindPwdFragment = new LoginBindPwdFragment();
            FragmentUtils.replace(getParentFragmentManager(), loginBindPwdFragment, R.id.fl_fragment, true);
        } else if ("1".equals(isOne)) {
            //去答题
            RegisterQuestionAddBean bean = new RegisterQuestionAddBean();
            SPUtils.putBean(ConstantParam.REGISTER_QUESTION_KEY, bean);
            Intent intent = new Intent(getPageContext(), RegisterQuestionActivity.class);
            startActivity(intent);
        } else {
            //进首页
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void processHandlerMsg(Message msg) {
        switch (msg.what) {
            case LOGIN_SUCCESS:
                LoginBean loginBean = (LoginBean) msg.obj;
                //保存登录信息
                SharedPreferencesUtils.putBean(getPageContext(), SharedPreferencesUtils.USER_INFO, loginBean);
                //检查是否绑定身份证和密码和答题
                checkIsBindIdCardOrPwd(loginBean);
                break;
            case LOGIN_FAILED:
                KeyboardUtils.hideSoftInput(getActivity());
                break;
        }
    }
}
