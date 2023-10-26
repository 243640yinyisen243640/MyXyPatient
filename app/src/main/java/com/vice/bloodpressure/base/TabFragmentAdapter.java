package com.vice.bloodpressure.base;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

/**
 * 为了实现Fragement在ViewPager中的替换，替换到fragment需要实现RefeshFragment接口
 */
public class TabFragmentAdapter extends PagerAdapter {
    private static final String TAG = "FragmentPagerAdapter";
    private static final boolean DEBUG = false;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;

    private boolean isRefesh = false;
    ArrayList<Fragment> list = new ArrayList<>();
    //刷新对象的接口
    public interface RefeshFragment{

    }

    public boolean isRefesh(Object object) {
        if (isRefesh && (object instanceof RefeshFragment)) {
            return true;
        } else {
            return false;
        }
    }

    public void setRefesh(boolean t) {
        isRefesh = t;
    }

    public TabFragmentAdapter(FragmentManager fm) {
        mFragmentManager = fm;
    }

    public void setFragments(ArrayList fragments) {
        if (this.list != null) {
            this.list = fragments;
        }
    }

    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return ((Fragment) object).getView() == view;
    }

    @SuppressWarnings("ReferenceEquality")
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        final long itemId = getItemId(position);

        String name = makeFragmentName(container.getId(), itemId);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null && !(isRefesh(fragment))) {
            if (DEBUG) Log.i(TAG, "Attaching item #" + itemId + ": f=" + fragment);
            mCurTransaction.attach(fragment);
        } else {
            fragment = getItem(position);
            if (DEBUG) Log.i(TAG, "Adding item #" + itemId + ": f=" + fragment);
            mCurTransaction.add(container.getId(), fragment, makeFragmentName(container.getId(), itemId));
        }
        if (fragment != mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        if (isRefesh(fragment)) {
            isRefesh = false;
        }
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null || isRefesh(object)) {//避免缓存指令的干扰 每次使用新的 BackStackRecord 对象
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (DEBUG)
            Log.i(TAG, "Detaching item #" + getItemId(position) + ": f=" + object + " v=" + ((Fragment) object).getView());
        if (isRefesh(object)) {
            mCurTransaction.remove((Fragment) object);
        } else {
            mCurTransaction.detach((Fragment) object);
        }
    }

    @SuppressWarnings("ReferenceEquality")
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
        }
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }


    public long getItemId(int position) {
        return position;
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }


    @Override
    public int getItemPosition(Object object) {
        if (isRefesh(object)) {//是否进行销毁
            return POSITION_NONE;
        } else {
            return POSITION_UNCHANGED;
        }

    }
}
