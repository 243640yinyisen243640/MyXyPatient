package com.vice.bloodpressure.base.manager;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.vice.bloodpressure.base.model.HHSoftTopViewInfo;
import com.vice.bloodpressure.base.utils.XYSoftDensityUtils;

public class XYSoftDefaultTopViewManager {
    public static final HHSoftTopViewInfo mTopViewInfo = new HHSoftTopViewInfo();
    /**
     * 上下文对象
     */
    private FragmentActivity mActivity;
    /**
     * 页面顶部布局：状态栏+头部
     */
    private LinearLayout mView;
    /**
     * 状态栏占位布局
     */
    private TextView mStatusBarView;
    /**
     * 头部布局
     */
    private RelativeLayout mTopView;
    /**
     * 返回键
     */
    private TextView mBackTextView;
    /**
     * 标题
     */
    private TextView mTitleTextView;
    /**
     * 更多布局
     */
    private LinearLayout mMoreLayout;
    /**
     * 更多
     */
    private TextView mMoreTextView;
    /**
     * 顶部布局与内容分割线
     */
    private TextView mLineView;
    /**
     * 是否展示状态栏占位布局
     */
    private boolean mIsShowStatusBar;

    public XYSoftDefaultTopViewManager(FragmentActivity activity) {
        this.mActivity = activity;
        this.mIsShowStatusBar = false;
        initView();
    }

    public XYSoftDefaultTopViewManager(FragmentActivity activity, boolean isShowStatusBar) {
        this.mActivity = activity;
        this.mIsShowStatusBar = isShowStatusBar;
        initView();

    }

    /**
     * 初始化布局
     */
    private void initView() {
        mView = new LinearLayout(mActivity);
        mView.setOrientation(LinearLayout.VERTICAL);
        if (mTopViewInfo.topBackgroundDrawableRes != 0) {
            mView.setBackgroundResource(mTopViewInfo.topBackgroundDrawableRes);
        } else {
            mView.setBackgroundColor(Color.parseColor(mTopViewInfo.topBackgroundColor));
        }
        mStatusBarView = new TextView(mActivity);
        mStatusBarView.setBackgroundResource(mTopViewInfo.topBackgroundDrawableRes);
        mStatusBarView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, XYSoftDensityUtils.statusBarHeight(mActivity)));
        mView.addView(mStatusBarView);
        if (mIsShowStatusBar) {
            mStatusBarView.setVisibility(View.VISIBLE);
            //            mStatusBarView.setFitsSystemWindows(true);
        } else {
            mStatusBarView.setVisibility(View.GONE);
        }
        mTopView = new RelativeLayout(mActivity);
        mView.addView(mTopView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, XYSoftDensityUtils.dip2px(mActivity, mTopViewInfo.topViewHeight)));
        mBackTextView = new TextView(mActivity);
        mBackTextView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mBackTextView.setCompoundDrawablesWithIntrinsicBounds(mTopViewInfo.backLeftDrawable, 0, 0, 0);
        mBackTextView.setPadding(XYSoftDensityUtils.dip2px(mActivity,10),0,0,0);

        mBackTextView.setGravity(Gravity.CENTER_VERTICAL);
        mBackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        mTopView.addView(mBackTextView);

        mTitleTextView = new TextView(mActivity);
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTitleTextView.setLayoutParams(titleParams);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setLines(1);
        mTitleTextView.setTextSize(mTopViewInfo.titleSize);
        mTitleTextView.setTextColor(Color.parseColor(mTopViewInfo.titleTextColor));
        mTitleTextView.setCompoundDrawablesWithIntrinsicBounds(mTopViewInfo.titleLeftDrawable, 0, 0, 0);
        mTopView.addView(mTitleTextView);

        mMoreLayout = new LinearLayout(mActivity);
        RelativeLayout.LayoutParams moreParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        moreParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mMoreLayout.setLayoutParams(moreParams);
        mMoreTextView = new TextView(mActivity);
        mMoreTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mMoreTextView.setGravity(Gravity.CENTER);
        mMoreTextView.setTextSize(mTopViewInfo.moreSize);
        mMoreTextView.setTextColor(Color.parseColor(mTopViewInfo.moreTextColor));
        int padding10 = XYSoftDensityUtils.dip2px(mActivity, 10);
        mMoreTextView.setPadding(padding10, 0, padding10, 0);
        mMoreLayout.addView(mMoreTextView);
        mTopView.addView(mMoreLayout);

        mLineView = new TextView(mActivity);
        RelativeLayout.LayoutParams lineParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, XYSoftDensityUtils.dip2px(mActivity, mTopViewInfo.topLineHeight));
        lineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mLineView.setBackgroundColor(Color.parseColor(mTopViewInfo.topLineColor));
        mTopView.addView(mLineView, lineParams);
    }

    /**
     * 获取顶部布局
     *
     * @return
     */
    public LinearLayout topView() {
        return mView;
    }

    /**
     * 获取顶部布局
     *
     * @return
     */
    public RelativeLayout topTitleView() {
        return mTopView;
    }

    /**
     * 获取返回键
     *
     * @return
     */
    public TextView backTextView() {
        return mBackTextView;
    }

    /**
     * 获取标题控件
     *
     * @return
     */
    public TextView titleTextView() {
        return mTitleTextView;
    }

    /**
     * 获取更多布局
     *
     * @return
     */
    public LinearLayout moreLayout() {
        return mMoreLayout;
    }

    /**
     * 获取更多控件
     *
     * @return
     */
    public TextView moreTextView() {
        return mMoreTextView;
    }

    /**
     * 获取分割线
     *
     * @return
     */
    public TextView lineView() {
        return mLineView;
    }

    /**
     * 获取状态栏
     *
     * @return
     */
    public TextView statusBarView() {
        return mStatusBarView;
    }

    /**
     * 设置分割线可见
     *
     * @param visibility
     */
    public void lineViewVisibility(int visibility) {
        if (mLineView != null) {
            mLineView.setVisibility(visibility);
        }
    }

    public boolean getMIsShowStatusBar() {
        return mIsShowStatusBar;
    }
}
