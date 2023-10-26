package com.vice.bloodpressure.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vice.bloodpressure.base.manager.XYSoftDefaultTopViewManager;


public abstract class XYBaseFragment extends HHSoftBaseFragment {
    private LinearLayout contentView;
    private FrameLayout containerView;
    private XYSoftDefaultTopViewManager topViewManager;

    private boolean isScreen=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = new LinearLayout(getPageContext());
        contentView.setOrientation(LinearLayout.VERTICAL);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        topViewManager = new XYSoftDefaultTopViewManager(getActivity(), false);
        contentView.addView(topViewManager.topView(), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        containerView = new FrameLayout(getPageContext());
        contentView.addView(containerView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        onCreate();
        return contentView;
    }
    protected boolean isFullScreen() {
        return isScreen;
    }
    protected abstract void onCreate();

    /**
     * 头部管理器
     *
     * @return
     */
    protected XYSoftDefaultTopViewManager topViewManager() {
        return topViewManager;
    }


    /**
     * 父布局，包含头部
     *
     * @return
     */
    protected LinearLayout contentView() {
        return contentView;
    }

    /**
     * 内容布局，不包含头部
     *
     * @return
     */
    protected FrameLayout containerView() {
        return containerView;
    }
}
