package com.vice.bloodpressure.ui.activity.injection;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.XYSoftUIBaseActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:方案名称
 */
public class InjectionProgramAddNameActivity extends XYSoftUIBaseActivity {
    private EditText etName;
    private TextView tvNameNum;
    private TextView tvComplete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText("方案名称");
        containerView().addView(initView());
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout._activity_program_edit_name, null);
        etName = view.findViewById(R.id.et_program_name);
        tvNameNum = view.findViewById(R.id.tv_program_name_num);
        tvComplete = view.findViewById(R.id.tv_program_name_complete);

        //限制只能输入中文，英文，数字
        InputFilter typeFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern p = Pattern.compile("[0-9a-zA-Z|\u4e00-\u9fa5]+");
                Matcher m = p.matcher(source.toString());
                if (!m.matches()) return "";
                return null;
            }
        };
        //如果要限制输入字数，数组中加上new InputFilter.LengthFilter(maxLength)
        etName.setFilters(new InputFilter[]{typeFilter, new InputFilter.LengthFilter(10)});
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvNameNum.setText(s.toString().length()+"/10");
            }
        });
        tvComplete.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (TextUtils.isEmpty(name)){
                ToastUtils.showShort("请输入方案名称");
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("name",name);
            setResult(RESULT_OK,intent);
            finish();
        });
        return view;
    }
}
