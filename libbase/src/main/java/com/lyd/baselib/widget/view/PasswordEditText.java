package com.lyd.baselib.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.lyd.baselib.R;


/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2019/08/25
 * desc   : 密码隐藏显示 EditText
 */
public final class PasswordEditText extends RegexEditText
        implements View.OnTouchListener,
        View.OnFocusChangeListener, TextWatcher {

    private final Drawable mVisibleDrawable;
    private final Drawable mInvisibleDrawable;
    private Drawable mCurrentDrawable;
    private View.OnTouchListener mOnTouchListener;
    private View.OnFocusChangeListener mOnFocusChangeListener;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    @SuppressLint("ClickableViewAccessibility")
    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mVisibleDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.password_off_ic));
        mVisibleDrawable.setBounds(0, 0, mVisibleDrawable.getIntrinsicWidth(), mVisibleDrawable.getIntrinsicHeight());

        mInvisibleDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.password_on_ic));
        mInvisibleDrawable.setBounds(0, 0, mInvisibleDrawable.getIntrinsicWidth(), mInvisibleDrawable.getIntrinsicHeight());

        mCurrentDrawable = mVisibleDrawable;

        // 密码不可见
        addInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        if (getInputRegex() == null) {
            // 密码输入规则
            setInputRegex(REGEX_NONNULL);
        }

        setDrawableVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        super.addTextChangedListener(this);
    }

    private void setDrawableVisible(boolean visible) {
        if (mCurrentDrawable.isVisible() == visible) {
            return;
        }

        mCurrentDrawable.setVisible(visible, false);
        Drawable[] drawables = getCompoundDrawablesRelative();
        setCompoundDrawablesRelative(
                drawables[0],
                drawables[1],
                visible ? mCurrentDrawable : null,
                drawables[3]);
    }

    private void refreshDrawableStatus() {
        Drawable[] drawables = getCompoundDrawablesRelative();
        setCompoundDrawablesRelative(
                drawables[0],
                drawables[1],
                mCurrentDrawable,
                drawables[3]);
    }

    @Override
    public void setOnFocusChangeListener(View.OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    /**
     * {@link View.OnFocusChangeListener}
     */

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus && getText() != null) {
            setDrawableVisible(getText().length() > 0);
        } else {
            setDrawableVisible(false);
        }
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(view, hasFocus);
        }
    }

    /**
     * {@link View.OnTouchListener}
     */

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        if (mCurrentDrawable.isVisible() && x > getWidth() - getPaddingRight() - mCurrentDrawable.getIntrinsicWidth()) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (mCurrentDrawable == mVisibleDrawable) {
                    mCurrentDrawable = mInvisibleDrawable;
                    // 密码可见
                    setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    refreshDrawableStatus();
                } else if (mCurrentDrawable == mInvisibleDrawable) {
                    mCurrentDrawable = mVisibleDrawable;
                    // 密码不可见
                    setTransformationMethod(PasswordTransformationMethod.getInstance());
                    refreshDrawableStatus();
                }
                Editable editable = getText();
                if (editable != null) {
                    setSelection(editable.toString().length());
                }
            }
            return true;
        }
        return mOnTouchListener != null && mOnTouchListener.onTouch(view, motionEvent);
    }

    /**
     * {@link TextWatcher}
     */

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isFocused()) {
            setDrawableVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}