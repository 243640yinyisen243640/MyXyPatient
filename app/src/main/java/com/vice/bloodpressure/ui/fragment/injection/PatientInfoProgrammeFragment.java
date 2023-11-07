package com.vice.bloodpressure.ui.fragment.injection;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.adapter.injection.ViewPagerAdapter;
import com.vice.bloodpressure.base.TabFragmentAdapter;
import com.vice.bloodpressure.base.fragment.XYBaseFragment;
import com.vice.bloodpressure.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：治疗方案
 * 类传参：
 *
 * @author android.yys
 * @date 2021/1/15
 */
public class PatientInfoProgrammeFragment extends XYBaseFragment implements TabFragmentAdapter.RefeshFragment {
    private TextView tvTitle;
    private NoScrollViewPager viewPager;
    private List<Fragment> fragments;

    public static PatientInfoProgrammeFragment newInstance() {
        Bundle bundle = new Bundle();
        PatientInfoProgrammeFragment fragment = new PatientInfoProgrammeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void onCreate() {
        topViewManager().topView().removeAllViews();
        initView();
    }


    private void initView() {
        View view = View.inflate(getPageContext(), R.layout._fragment_data, null);
        tvTitle = view.findViewById(R.id.tv_injection_title);
        viewPager = view.findViewById(R.id.rv_injection);
        viewPager.setNoScroll(false);
        containerView().addView(view);

        fragments = new ArrayList<>();
        PatientInfoCurrentFragment currentFragment = PatientInfoCurrentFragment.newInstance();
        fragments.add(currentFragment);

        PatientInfoHistoryFragment historyFragment = PatientInfoHistoryFragment.newInstance();
        fragments.add(historyFragment);

        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), fragments));
        viewPager.setCurrentItem(0);//默认选中项
        viewPager.setOffscreenPageLimit(fragments.size());
        tvTitle.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() == 0 ? 1 : 0);
            tvTitle.setText(viewPager.getCurrentItem() == 0 ?  "查看历史": "查看当前");
        });
    }


}
