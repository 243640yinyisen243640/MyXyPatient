package com.maning.imagebrowserlibrary.utils.immersionbar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.maning.imagebrowserlibrary.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.maning.imagebrowserlibrary.utils.immersionbar.Constants.FLAG_FITS_DEFAULT;
import static com.maning.imagebrowserlibrary.utils.immersionbar.Constants.FLAG_FITS_STATUS;
import static com.maning.imagebrowserlibrary.utils.immersionbar.Constants.FLAG_FITS_SYSTEM_WINDOWS;
import static com.maning.imagebrowserlibrary.utils.immersionbar.Constants.FLAG_FITS_TITLE;
import static com.maning.imagebrowserlibrary.utils.immersionbar.Constants.FLAG_FITS_TITLE_MARGIN_TOP;
import static com.maning.imagebrowserlibrary.utils.immersionbar.Constants.IMMERSION_BOUNDARY_COLOR;
import static com.maning.imagebrowserlibrary.utils.immersionbar.Constants.IMMERSION_ID_NAVIGATION_BAR_VIEW;
import static com.maning.imagebrowserlibrary.utils.immersionbar.Constants.IMMERSION_ID_STATUS_BAR_VIEW;
import static com.maning.imagebrowserlibrary.utils.immersionbar.Constants.IMMERSION_MIUI_NAVIGATION_BAR_DARK;
import static com.maning.imagebrowserlibrary.utils.immersionbar.Constants.IMMERSION_MIUI_STATUS_BAR_DARK;

/**
 * android 4.4?????????????????????bar?????????
 *
 * @author gyf
 * @date 2017 /05/09
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public final class ImmersionBar implements ImmersionCallback {

    private Activity mActivity;
    private Fragment mSupportFragment;
    private android.app.Fragment mFragment;
    private Dialog mDialog;
    private Window mWindow;
    private ViewGroup mDecorView;
    private ViewGroup mContentView;

    /**
     * ????????????Activity?????????
     */
    private boolean mIsActivity = false;
    /**
     * ????????????Fragment?????????
     */
    private boolean mIsFragment = false;
    /**
     * ????????????Dialog?????????
     */
    private boolean mIsDialog = false;
    /**
     * ???????????????bar??????
     */
    private BarParams mBarParams;
    /**
     * ??????bar????????????
     */
    private BarConfig mBarConfig;
    /**
     * ???????????????????????????Emui3????????????
     */
    private int mNavigationBarHeight = 0;
    /**
     * ???????????????????????????Emui3????????????
     */
    private int mNavigationBarWidth = 0;
    /**
     * ActionBar?????????
     */
    private int mActionBarHeight = 0;
    /**
     * ?????????????????????
     */
    private FitsKeyboard mFitsKeyboard = null;
    /**
     * ????????????tag?????????bar???????????????
     */
    private Map<String, BarParams> mTagMap = new HashMap<>();
    /**
     * ???????????????????????????????????????????????????????????????
     */
    private int mFitsStatusBarType = FLAG_FITS_DEFAULT;
    /**
     * ?????????????????????init()?????????
     */
    private boolean mInitialized = false;
    /**
     * ActionBar????????????LOLLIPOP???????????????
     */
    private boolean mIsActionBarBelowLOLLIPOP = false;

    private boolean mKeyboardTempEnable = false;

    private int mPaddingLeft = 0, mPaddingTop = 0, mPaddingRight = 0, mPaddingBottom = 0;

    /**
     * ???Activity??????
     * With immersion bar.
     *
     * @param activity the activity
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull Activity activity) {
        return getRetriever().get(activity);
    }

    /**
     * ???Fragment??????
     * With immersion bar.
     *
     * @param fragment the fragment
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull Fragment fragment) {
        return getRetriever().get(fragment);
    }

    /**
     * ???Fragment??????
     * With immersion bar.
     *
     * @param fragment the fragment
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull android.app.Fragment fragment) {
        return getRetriever().get(fragment);
    }

    /**
     * ???DialogFragment??????
     * With immersion bar.
     *
     * @param dialogFragment the dialog fragment
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull DialogFragment dialogFragment) {
        return getRetriever().get(dialogFragment);
    }

    /**
     * ???DialogFragment??????
     * With immersion bar.
     *
     * @param dialogFragment the dialog fragment
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull android.app.DialogFragment dialogFragment) {
        return getRetriever().get(dialogFragment);
    }

    /**
     * ???dialog?????????
     * With immersion bar.
     *
     * @param activity the activity
     * @param dialog   the dialog
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull Activity activity, @NonNull Dialog dialog) {
        return getRetriever().get(activity, dialog);
    }

    /**
     * ???Dialog?????????????????????DialogFragment
     *
     * @param activity the activity
     * @param dialog   the dialog
     */
    public static void destroy(@NonNull Activity activity, @NonNull Dialog dialog) {
        getRetriever().destroy(activity, dialog);
    }

    /**
     * ???Activity????????????
     * Instantiates a new Immersion bar.
     *
     * @param activity the activity
     */
    ImmersionBar(Activity activity) {
        mIsActivity = true;
        mActivity = activity;
        initCommonParameter(mActivity.getWindow());
    }

    /**
     * ???Fragment????????????
     * Instantiates a new Immersion bar.
     *
     * @param fragment the fragment
     */
    ImmersionBar(Fragment fragment) {
        mIsFragment = true;
        mActivity = fragment.getActivity();
        mSupportFragment = fragment;
        checkInitWithActivity();
        initCommonParameter(mActivity.getWindow());
    }

    /**
     * ???Fragment????????????
     * Instantiates a new Immersion bar.
     *
     * @param fragment the fragment
     */
    ImmersionBar(android.app.Fragment fragment) {
        mIsFragment = true;
        mActivity = fragment.getActivity();
        mFragment = fragment;
        checkInitWithActivity();
        initCommonParameter(mActivity.getWindow());
    }

    /**
     * ???dialogFragment?????????
     * Instantiates a new Immersion bar.
     *
     * @param dialogFragment the dialog fragment
     */
    ImmersionBar(DialogFragment dialogFragment) {
        mIsDialog = true;
        mActivity = dialogFragment.getActivity();
        mSupportFragment = dialogFragment;
        mDialog = dialogFragment.getDialog();
        checkInitWithActivity();
        initCommonParameter(mDialog.getWindow());
    }

    /**
     * ???dialogFragment?????????
     * Instantiates a new Immersion bar.
     *
     * @param dialogFragment the dialog fragment
     */
    ImmersionBar(android.app.DialogFragment dialogFragment) {
        mIsDialog = true;
        mActivity = dialogFragment.getActivity();
        mFragment = dialogFragment;
        mDialog = dialogFragment.getDialog();
        checkInitWithActivity();
        initCommonParameter(mDialog.getWindow());
    }

    /**
     * ???Dialog????????????
     * Instantiates a new Immersion bar.
     *
     * @param activity the activity
     * @param dialog   the dialog
     */
    ImmersionBar(Activity activity, Dialog dialog) {
        mIsDialog = true;
        mActivity = activity;
        mDialog = dialog;
        checkInitWithActivity();
        initCommonParameter(mDialog.getWindow());
    }

    /**
     * ?????????????????????Activity????????????????????????????????????????????????
     */
    private void checkInitWithActivity() {
        if (!with(mActivity).initialized()) {
            with(mActivity).init();
        }
    }

    /**
     * ?????????????????????
     *
     * @param window window
     */
    private void initCommonParameter(Window window) {
        mWindow = window;
        mBarParams = new BarParams();
        mDecorView = (ViewGroup) mWindow.getDecorView();
        mContentView = mDecorView.findViewById(android.R.id.content);
    }

    /**
     * ??????????????????????????????
     *
     * @return the immersion bar
     */
    public ImmersionBar transparentStatusBar() {
        mBarParams.statusBarColor = Color.TRANSPARENT;
        return this;
    }

    /**
     * ??????????????????????????????
     *
     * @return the immersion bar
     */
    public ImmersionBar transparentNavigationBar() {
        mBarParams.navigationBarColor = Color.TRANSPARENT;
        mBarParams.fullScreen = true;
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @return the immersion bar
     */
    public ImmersionBar transparentBar() {
        mBarParams.statusBarColor = Color.TRANSPARENT;
        mBarParams.navigationBarColor = Color.TRANSPARENT;
        mBarParams.fullScreen = true;
        return this;
    }

    /**
     * ???????????????
     *
     * @param statusBarColor ?????????????????????????????????R.color.xxx???
     * @return the immersion bar
     */
    public ImmersionBar statusBarColor(@ColorRes int statusBarColor) {
        return this.statusBarColorInt(ContextCompat.getColor(mActivity, statusBarColor));
    }

    /**
     * ???????????????
     *
     * @param statusBarColor ?????????????????????????????????R.color.xxx???
     * @param alpha          the alpha  ?????????
     * @return the immersion bar
     */
    public ImmersionBar statusBarColor(@ColorRes int statusBarColor,
                                       @FloatRange(from = 0f, to = 1f) float alpha) {
        return this.statusBarColorInt(ContextCompat.getColor(mActivity, statusBarColor), alpha);
    }

    /**
     * ???????????????
     *
     * @param statusBarColor          ?????????????????????????????????R.color.xxx???
     * @param statusBarColorTransform the status bar color transform ???????????????????????????
     * @param alpha                   the alpha  ?????????
     * @return the immersion bar
     */
    public ImmersionBar statusBarColor(@ColorRes int statusBarColor,
                                       @ColorRes int statusBarColorTransform,
                                       @FloatRange(from = 0f, to = 1f) float alpha) {
        return this.statusBarColorInt(ContextCompat.getColor(mActivity, statusBarColor),
                ContextCompat.getColor(mActivity, statusBarColorTransform),
                alpha);
    }

    /**
     * ???????????????
     * Status bar color int immersion bar.
     *
     * @param statusBarColor the status bar color
     * @return the immersion bar
     */
    public ImmersionBar statusBarColor(String statusBarColor) {
        return this.statusBarColorInt(Color.parseColor(statusBarColor));
    }

    /**
     * ???????????????
     *
     * @param statusBarColor ???????????????
     * @param alpha          the alpha  ?????????
     * @return the immersion bar
     */
    public ImmersionBar statusBarColor(String statusBarColor,
                                       @FloatRange(from = 0f, to = 1f) float alpha) {
        return this.statusBarColorInt(Color.parseColor(statusBarColor), alpha);
    }

    /**
     * ???????????????
     *
     * @param statusBarColor          ???????????????
     * @param statusBarColorTransform the status bar color transform ???????????????????????????
     * @param alpha                   the alpha  ?????????
     * @return the immersion bar
     */
    public ImmersionBar statusBarColor(String statusBarColor,
                                       String statusBarColorTransform,
                                       @FloatRange(from = 0f, to = 1f) float alpha) {
        return this.statusBarColorInt(Color.parseColor(statusBarColor),
                Color.parseColor(statusBarColorTransform),
                alpha);
    }

    /**
     * ???????????????
     *
     * @param statusBarColor ?????????????????????????????????R.color.xxx???
     * @return the immersion bar
     */
    public ImmersionBar statusBarColorInt(@ColorInt int statusBarColor) {
        mBarParams.statusBarColor = statusBarColor;
        return this;
    }

    /**
     * ???????????????
     *
     * @param statusBarColor ?????????????????????????????????R.color.xxx???
     * @param alpha          the alpha  ?????????
     * @return the immersion bar
     */
    public ImmersionBar statusBarColorInt(@ColorInt int statusBarColor,
                                          @FloatRange(from = 0f, to = 1f) float alpha) {
        mBarParams.statusBarColor = statusBarColor;
        mBarParams.statusBarAlpha = alpha;
        return this;
    }

    /**
     * ???????????????
     *
     * @param statusBarColor          ?????????????????????????????????R.color.xxx???
     * @param statusBarColorTransform the status bar color transform ???????????????????????????
     * @param alpha                   the alpha  ?????????
     * @return the immersion bar
     */
    public ImmersionBar statusBarColorInt(@ColorInt int statusBarColor,
                                          @ColorInt int statusBarColorTransform,
                                          @FloatRange(from = 0f, to = 1f) float alpha) {
        mBarParams.statusBarColor = statusBarColor;
        mBarParams.statusBarColorTransform = statusBarColorTransform;
        mBarParams.statusBarAlpha = alpha;
        return this;
    }

    /**
     * ???????????????
     *
     * @param navigationBarColor the navigation bar color ???????????????
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColor(@ColorRes int navigationBarColor) {
        return this.navigationBarColorInt(ContextCompat.getColor(mActivity, navigationBarColor));
    }

    /**
     * ???????????????
     *
     * @param navigationBarColor the navigation bar color ???????????????
     * @param navigationAlpha    the navigation alpha ?????????
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColor(@ColorRes int navigationBarColor,
                                           @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        return this.navigationBarColorInt(ContextCompat.getColor(mActivity, navigationBarColor), navigationAlpha);
    }

    /**
     * ???????????????
     *
     * @param navigationBarColor          the navigation bar color ???????????????
     * @param navigationBarColorTransform the navigation bar color transform  ???????????????????????????
     * @param navigationAlpha             the navigation alpha  ?????????
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColor(@ColorRes int navigationBarColor,
                                           @ColorRes int navigationBarColorTransform,
                                           @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        return this.navigationBarColorInt(ContextCompat.getColor(mActivity, navigationBarColor),
                ContextCompat.getColor(mActivity, navigationBarColorTransform), navigationAlpha);
    }

    /**
     * ???????????????
     *
     * @param navigationBarColor the navigation bar color ???????????????
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColor(String navigationBarColor) {
        return this.navigationBarColorInt(Color.parseColor(navigationBarColor));
    }

    /**
     * ???????????????
     *
     * @param navigationBarColor the navigation bar color ???????????????
     * @param navigationAlpha    the navigation alpha ?????????
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColor(String navigationBarColor,
                                           @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        return this.navigationBarColorInt(Color.parseColor(navigationBarColor), navigationAlpha);
    }

    /**
     * ???????????????
     *
     * @param navigationBarColor          the navigation bar color ???????????????
     * @param navigationBarColorTransform the navigation bar color transform  ???????????????????????????
     * @param navigationAlpha             the navigation alpha  ?????????
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColor(String navigationBarColor,
                                           String navigationBarColorTransform,
                                           @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        return this.navigationBarColorInt(Color.parseColor(navigationBarColor),
                Color.parseColor(navigationBarColorTransform), navigationAlpha);
    }

    /**
     * ???????????????
     *
     * @param navigationBarColor the navigation bar color ???????????????
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColorInt(@ColorInt int navigationBarColor) {
        mBarParams.navigationBarColor = navigationBarColor;
        return this;
    }

    /**
     * ???????????????
     *
     * @param navigationBarColor the navigation bar color ???????????????
     * @param navigationAlpha    the navigation alpha ?????????
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColorInt(@ColorInt int navigationBarColor,
                                              @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        mBarParams.navigationBarColor = navigationBarColor;
        mBarParams.navigationBarAlpha = navigationAlpha;
        return this;
    }

    /**
     * ???????????????
     *
     * @param navigationBarColor          the navigation bar color ???????????????
     * @param navigationBarColorTransform the navigation bar color transform  ???????????????????????????
     * @param navigationAlpha             the navigation alpha  ?????????
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColorInt(@ColorInt int navigationBarColor,
                                              @ColorInt int navigationBarColorTransform,
                                              @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        mBarParams.navigationBarColor = navigationBarColor;
        mBarParams.navigationBarColorTransform = navigationBarColorTransform;
        mBarParams.navigationBarAlpha = navigationAlpha;
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    public ImmersionBar barColor(@ColorRes int barColor) {
        return this.barColorInt(ContextCompat.getColor(mActivity, barColor));
    }

    /**
     * ???????????????????????????
     *
     * @param barColor the bar color
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    public ImmersionBar barColor(@ColorRes int barColor, @FloatRange(from = 0f, to = 1f) float barAlpha) {
        return this.barColorInt(ContextCompat.getColor(mActivity, barColor), barColor);
    }

    /**
     * ???????????????????????????
     *
     * @param barColor          the bar color
     * @param barColorTransform the bar color transform
     * @param barAlpha          the bar alpha
     * @return the immersion bar
     */
    public ImmersionBar barColor(@ColorRes int barColor,
                                 @ColorRes int barColorTransform,
                                 @FloatRange(from = 0f, to = 1f) float barAlpha) {
        return this.barColorInt(ContextCompat.getColor(mActivity, barColor),
                ContextCompat.getColor(mActivity, barColorTransform), barAlpha);
    }

    /**
     * ???????????????????????????
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    public ImmersionBar barColor(String barColor) {
        return this.barColorInt(Color.parseColor(barColor));
    }

    /**
     * ???????????????????????????
     *
     * @param barColor the bar color
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    public ImmersionBar barColor(String barColor, @FloatRange(from = 0f, to = 1f) float barAlpha) {
        return this.barColorInt(Color.parseColor(barColor), barAlpha);
    }

    /**
     * ???????????????????????????
     *
     * @param barColor          the bar color
     * @param barColorTransform the bar color transform
     * @param barAlpha          the bar alpha
     * @return the immersion bar
     */
    public ImmersionBar barColor(String barColor,
                                 String barColorTransform,
                                 @FloatRange(from = 0f, to = 1f) float barAlpha) {
        return this.barColorInt(Color.parseColor(barColor), Color.parseColor(barColorTransform), barAlpha);
    }

    /**
     * ???????????????????????????
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    public ImmersionBar barColorInt(@ColorInt int barColor) {
        mBarParams.statusBarColor = barColor;
        mBarParams.navigationBarColor = barColor;
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @param barColor the bar color
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    public ImmersionBar barColorInt(@ColorInt int barColor, @FloatRange(from = 0f, to = 1f) float barAlpha) {
        mBarParams.statusBarColor = barColor;
        mBarParams.navigationBarColor = barColor;
        mBarParams.statusBarAlpha = barAlpha;
        mBarParams.navigationBarAlpha = barAlpha;
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @param barColor          the bar color
     * @param barColorTransform the bar color transform
     * @param barAlpha          the bar alpha
     * @return the immersion bar
     */
    public ImmersionBar barColorInt(@ColorInt int barColor,
                                    @ColorInt int barColorTransform,
                                    @FloatRange(from = 0f, to = 1f) float barAlpha) {
        mBarParams.statusBarColor = barColor;
        mBarParams.navigationBarColor = barColor;

        mBarParams.statusBarColorTransform = barColorTransform;
        mBarParams.navigationBarColorTransform = barColorTransform;

        mBarParams.statusBarAlpha = barAlpha;
        mBarParams.navigationBarAlpha = barAlpha;
        return this;
    }


    /**
     * ????????????????????????????????????????????????
     *
     * @param statusBarColorTransform the status bar color transform
     * @return the immersion bar
     */
    public ImmersionBar statusBarColorTransform(@ColorRes int statusBarColorTransform) {
        return this.statusBarColorTransformInt(ContextCompat.getColor(mActivity, statusBarColorTransform));
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param statusBarColorTransform the status bar color transform
     * @return the immersion bar
     */
    public ImmersionBar statusBarColorTransform(String statusBarColorTransform) {
        return this.statusBarColorTransformInt(Color.parseColor(statusBarColorTransform));
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param statusBarColorTransform the status bar color transform
     * @return the immersion bar
     */
    public ImmersionBar statusBarColorTransformInt(@ColorInt int statusBarColorTransform) {
        mBarParams.statusBarColorTransform = statusBarColorTransform;
        return this;
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param navigationBarColorTransform the m navigation bar color transform
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColorTransform(@ColorRes int navigationBarColorTransform) {
        return this.navigationBarColorTransformInt(ContextCompat.getColor(mActivity, navigationBarColorTransform));
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param navigationBarColorTransform the m navigation bar color transform
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColorTransform(String navigationBarColorTransform) {
        return this.navigationBarColorTransformInt(Color.parseColor(navigationBarColorTransform));
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param navigationBarColorTransform the m navigation bar color transform
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColorTransformInt(@ColorInt int navigationBarColorTransform) {
        mBarParams.navigationBarColorTransform = navigationBarColorTransform;
        return this;
    }

    /**
     * ????????????????????????????????????????????????????????????
     *
     * @param barColorTransform the bar color transform
     * @return the immersion bar
     */
    public ImmersionBar barColorTransform(@ColorRes int barColorTransform) {
        return this.barColorTransformInt(ContextCompat.getColor(mActivity, barColorTransform));
    }

    /**
     * ????????????????????????????????????????????????????????????
     *
     * @param barColorTransform the bar color transform
     * @return the immersion bar
     */
    public ImmersionBar barColorTransform(String barColorTransform) {
        return this.barColorTransformInt(Color.parseColor(barColorTransform));
    }

    /**
     * ????????????????????????????????????????????????????????????
     *
     * @param barColorTransform the bar color transform
     * @return the immersion bar
     */
    public ImmersionBar barColorTransformInt(@ColorInt int barColorTransform) {
        mBarParams.statusBarColorTransform = barColorTransform;
        mBarParams.navigationBarColorTransform = barColorTransform;
        return this;
    }

    /**
     * Add ??????????????????View
     *
     * @param view the view
     * @return the immersion bar
     */
    public ImmersionBar addViewSupportTransformColor(View view) {
        return this.addViewSupportTransformColorInt(view, mBarParams.statusBarColorTransform);
    }

    /**
     * Add ??????????????????View
     *
     * @param view                    the view
     * @param viewColorAfterTransform the view color after transform
     * @return the immersion bar
     */
    public ImmersionBar addViewSupportTransformColor(View view, @ColorRes int viewColorAfterTransform) {
        return this.addViewSupportTransformColorInt(view, ContextCompat.getColor(mActivity, viewColorAfterTransform));
    }

    /**
     * Add ??????????????????View
     *
     * @param view                     the view
     * @param viewColorBeforeTransform the view color before transform
     * @param viewColorAfterTransform  the view color after transform
     * @return the immersion bar
     */
    public ImmersionBar addViewSupportTransformColor(View view, @ColorRes int viewColorBeforeTransform,
                                                     @ColorRes int viewColorAfterTransform) {
        return this.addViewSupportTransformColorInt(view,
                ContextCompat.getColor(mActivity, viewColorBeforeTransform),
                ContextCompat.getColor(mActivity, viewColorAfterTransform));
    }

    /**
     * Add ??????????????????View
     *
     * @param view                    the view
     * @param viewColorAfterTransform the view color after transform
     * @return the immersion bar
     */
    public ImmersionBar addViewSupportTransformColor(View view, String viewColorAfterTransform) {
        return this.addViewSupportTransformColorInt(view, Color.parseColor(viewColorAfterTransform));
    }

    /**
     * Add ??????????????????View
     *
     * @param view                     the view
     * @param viewColorBeforeTransform the view color before transform
     * @param viewColorAfterTransform  the view color after transform
     * @return the immersion bar
     */
    public ImmersionBar addViewSupportTransformColor(View view, String viewColorBeforeTransform,
                                                     String viewColorAfterTransform) {
        return this.addViewSupportTransformColorInt(view,
                Color.parseColor(viewColorBeforeTransform),
                Color.parseColor(viewColorAfterTransform));
    }

    /**
     * Add ??????????????????View
     *
     * @param view                    the view
     * @param viewColorAfterTransform the view color after transform
     * @return the immersion bar
     */
    public ImmersionBar addViewSupportTransformColorInt(View view, @ColorInt int viewColorAfterTransform) {
        if (view == null) {
            throw new IllegalArgumentException("View??????????????????");
        }
        Map<Integer, Integer> map = new HashMap<>();
        map.put(mBarParams.statusBarColor, viewColorAfterTransform);
        mBarParams.viewMap.put(view, map);
        return this;
    }

    /**
     * Add ??????????????????View
     *
     * @param view                     the view
     * @param viewColorBeforeTransform the view color before transform
     * @param viewColorAfterTransform  the view color after transform
     * @return the immersion bar
     */
    public ImmersionBar addViewSupportTransformColorInt(View view, @ColorInt int viewColorBeforeTransform,
                                                        @ColorInt int viewColorAfterTransform) {
        if (view == null) {
            throw new IllegalArgumentException("View??????????????????");
        }
        Map<Integer, Integer> map = new HashMap<>();
        map.put(viewColorBeforeTransform, viewColorAfterTransform);
        mBarParams.viewMap.put(view, map);
        return this;
    }

    /**
     * view?????????
     * View alpha immersion bar.
     *
     * @param viewAlpha the view alpha
     * @return the immersion bar
     */
    public ImmersionBar viewAlpha(@FloatRange(from = 0f, to = 1f) float viewAlpha) {
        mBarParams.viewAlpha = viewAlpha;
        return this;
    }

    /**
     * Remove support view immersion bar.
     *
     * @param view the view
     * @return the immersion bar
     */
    public ImmersionBar removeSupportView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View??????????????????");
        }
        Map<Integer, Integer> map = mBarParams.viewMap.get(view);
        if (map != null && map.size() != 0) {
            mBarParams.viewMap.remove(view);
        }
        return this;
    }

    /**
     * Remove support all view immersion bar.
     *
     * @return the immersion bar
     */
    public ImmersionBar removeSupportAllView() {
        if (mBarParams.viewMap.size() != 0) {
            mBarParams.viewMap.clear();
        }
        return this;
    }

    /**
     * ???????????????????????????Activity??????????????????
     *
     * @param isFullScreen the is full screen
     * @return the immersion bar
     */
    public ImmersionBar fullScreen(boolean isFullScreen) {
        mBarParams.fullScreen = isFullScreen;
        return this;
    }

    /**
     * ??????????????????
     *
     * @param statusAlpha the status alpha
     * @return the immersion bar
     */
    public ImmersionBar statusBarAlpha(@FloatRange(from = 0f, to = 1f) float statusAlpha) {
        mBarParams.statusBarAlpha = statusAlpha;
        return this;
    }

    /**
     * ??????????????????
     *
     * @param navigationAlpha the navigation alpha
     * @return the immersion bar
     */
    public ImmersionBar navigationBarAlpha(@FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        mBarParams.navigationBarAlpha = navigationAlpha;
        return this;
    }

    /**
     * ??????????????????????????????
     *
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    public ImmersionBar barAlpha(@FloatRange(from = 0f, to = 1f) float barAlpha) {
        mBarParams.statusBarAlpha = barAlpha;
        mBarParams.navigationBarAlpha = barAlpha;
        return this;
    }

    /**
     * ???????????? ????????????StatusBar???NavigationBar???????????????????????????????????????
     *
     * @param isEnable true?????? ??????false
     * @return the immersion bar
     */
    public ImmersionBar autoDarkModeEnable(boolean isEnable) {
        return this.autoDarkModeEnable(isEnable, 0f);
    }

    /**
     * ????????????????????????StatusBar???NavigationBar???????????????????????????????????????
     * Auto dark mode enable immersion bar.
     *
     * @param isEnable          the is enable
     * @param autoDarkModeAlpha the auto dark mode alpha
     * @return the immersion bar
     */
    public ImmersionBar autoDarkModeEnable(boolean isEnable, @FloatRange(from = 0f, to = 1f) float autoDarkModeAlpha) {
        mBarParams.autoStatusBarDarkModeEnable = isEnable;
        mBarParams.autoStatusBarDarkModeAlpha = autoDarkModeAlpha;
        mBarParams.autoNavigationBarDarkModeEnable = isEnable;
        mBarParams.autoNavigationBarDarkModeAlpha = autoDarkModeAlpha;
        return this;
    }

    /**
     * ????????????????????????StatusBar???????????????????????????????????????
     * Auto status bar dark mode enable immersion bar.
     *
     * @param isEnable the is enable
     * @return the immersion bar
     */
    public ImmersionBar autoStatusBarDarkModeEnable(boolean isEnable) {
        return this.autoStatusBarDarkModeEnable(isEnable, 0f);
    }

    /**
     * ????????????????????????StatusBar???????????????????????????????????????
     * Auto status bar dark mode enable immersion bar.
     *
     * @param isEnable          the is enable
     * @param autoDarkModeAlpha the auto dark mode alpha
     * @return the immersion bar
     */
    public ImmersionBar autoStatusBarDarkModeEnable(boolean isEnable, @FloatRange(from = 0f, to = 1f) float autoDarkModeAlpha) {
        mBarParams.autoStatusBarDarkModeEnable = isEnable;
        mBarParams.autoStatusBarDarkModeAlpha = autoDarkModeAlpha;
        return this;
    }

    /**
     * ????????????????????????StatusBar???????????????????????????????????????
     * Auto navigation bar dark mode enable immersion bar.
     *
     * @param isEnable the is enable
     * @return the immersion bar
     */
    public ImmersionBar autoNavigationBarDarkModeEnable(boolean isEnable) {
        return this.autoNavigationBarDarkModeEnable(isEnable, 0f);
    }

    /**
     * ????????????????????????NavigationBar???????????????????????????????????????
     * Auto navigation bar dark mode enable immersion bar.
     *
     * @param isEnable          the is enable
     * @param autoDarkModeAlpha the auto dark mode alpha
     * @return the immersion bar
     */
    public ImmersionBar autoNavigationBarDarkModeEnable(boolean isEnable, @FloatRange(from = 0f, to = 1f) float autoDarkModeAlpha) {
        mBarParams.autoNavigationBarDarkModeEnable = isEnable;
        mBarParams.autoNavigationBarDarkModeAlpha = autoDarkModeAlpha;
        return this;
    }

    /**
     * ??????????????????????????????
     *
     * @param isDarkFont true ??????
     * @return the immersion bar
     */
    public ImmersionBar statusBarDarkFont(boolean isDarkFont) {
        return statusBarDarkFont(isDarkFont, 0f);
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????????????????
     * Status bar dark font immersion bar.
     *
     * @param isDarkFont  the is dark font
     * @param statusAlpha the status alpha ????????????????????????????????????????????????statusAlpha????????????????????????????????????????????????????????????????????????
     * @return the immersion bar
     */
    public ImmersionBar statusBarDarkFont(boolean isDarkFont, @FloatRange(from = 0f, to = 1f) float statusAlpha) {
        mBarParams.statusBarDarkFont = isDarkFont;
        if (isDarkFont && !isSupportStatusBarDarkFont()) {
            mBarParams.statusBarAlpha = statusAlpha;
        } else {
            mBarParams.flymeOSStatusBarFontColor = 0;
            mBarParams.statusBarAlpha = 0f;
        }
        return this;
    }

    /**
     * ??????????????????????????????????????????android o????????????
     * Navigation bar dark icon immersion bar.
     *
     * @param isDarkIcon the is dark icon
     * @return the immersion bar
     */
    public ImmersionBar navigationBarDarkIcon(boolean isDarkIcon) {
        return navigationBarDarkIcon(isDarkIcon, 0f);
    }

    /**
     * ??????????????????????????????????????????android o???????????????????????????????????????????????????????????????????????????????????????
     * Navigation bar dark icon immersion bar.
     *
     * @param isDarkIcon      the is dark icon
     * @param navigationAlpha the navigation alpha ????????????????????????????????????????????????navigationAlpha????????????????????????????????????????????????????????????????????????
     * @return the immersion bar
     */
    public ImmersionBar navigationBarDarkIcon(boolean isDarkIcon, @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        mBarParams.navigationBarDarkIcon = isDarkIcon;
        if (isDarkIcon && !isSupportNavigationIconDark()) {
            mBarParams.navigationBarAlpha = navigationAlpha;
        } else {
            mBarParams.navigationBarAlpha = 0f;
        }
        return this;
    }

    /**
     * ?????? Flyme OS???????????????????????????????????????????????????statusBarDarkFont(boolean isDarkFont)??????
     * Flyme os status bar font color immersion bar.
     *
     * @param flymeOSStatusBarFontColor the flyme os status bar font color
     * @return the immersion bar
     */
    public ImmersionBar flymeOSStatusBarFontColor(@ColorRes int flymeOSStatusBarFontColor) {
        mBarParams.flymeOSStatusBarFontColor = ContextCompat.getColor(mActivity, flymeOSStatusBarFontColor);
        return this;
    }

    /**
     * ?????? Flyme OS???????????????????????????????????????????????????statusBarDarkFont(boolean isDarkFont)??????
     * Flyme os status bar font color immersion bar.
     *
     * @param flymeOSStatusBarFontColor the flyme os status bar font color
     * @return the immersion bar
     */
    public ImmersionBar flymeOSStatusBarFontColor(String flymeOSStatusBarFontColor) {
        mBarParams.flymeOSStatusBarFontColor = Color.parseColor(flymeOSStatusBarFontColor);
        return this;
    }

    /**
     * ?????? Flyme OS???????????????????????????????????????????????????statusBarDarkFont(boolean isDarkFont)??????
     * Flyme os status bar font color immersion bar.
     *
     * @param flymeOSStatusBarFontColor the flyme os status bar font color
     * @return the immersion bar
     */
    public ImmersionBar flymeOSStatusBarFontColorInt(@ColorInt int flymeOSStatusBarFontColor) {
        mBarParams.flymeOSStatusBarFontColor = flymeOSStatusBarFontColor;
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @param barHide the bar hide
     * @return the immersion bar
     */
    public ImmersionBar hideBar(BarHide barHide) {
        mBarParams.barHide = barHide;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || OSUtils.isEMUI3_x()) {
            if ((mBarParams.barHide == BarHide.FLAG_HIDE_NAVIGATION_BAR) ||
                    (mBarParams.barHide == BarHide.FLAG_HIDE_BAR)) {
                mBarParams.hideNavigationBar = true;
            } else {
                mBarParams.hideNavigationBar = false;
            }
        }
        return this;
    }

    /**
     * ????????????????????????????????????
     *
     * @param fits the fits
     * @return the immersion bar
     */
    public ImmersionBar fitsSystemWindows(boolean fits) {
        mBarParams.fits = fits;
        if (mBarParams.fits) {
            if (mFitsStatusBarType == FLAG_FITS_DEFAULT) {
                mFitsStatusBarType = FLAG_FITS_SYSTEM_WINDOWS;
            }
        } else {
            mFitsStatusBarType = FLAG_FITS_DEFAULT;
        }
        return this;
    }

    /**
     * ?????????????????????????????????????????????????????????
     * Fits system windows immersion bar.
     *
     * @param fits         the fits
     * @param contentColor the content color ?????????????????????
     * @return the immersion bar
     */
    public ImmersionBar fitsSystemWindows(boolean fits, @ColorRes int contentColor) {
        return fitsSystemWindowsInt(fits, ContextCompat.getColor(mActivity, contentColor));
    }

    /**
     * ?????????????????????????????????????????????????????????
     * Fits system windows immersion bar.
     *
     * @param fits                  the fits
     * @param contentColor          the content color ?????????????????????
     * @param contentColorTransform the content color transform  ?????????????????????????????????
     * @param contentAlpha          the content alpha ?????????????????????
     * @return the immersion bar
     */
    public ImmersionBar fitsSystemWindows(boolean fits, @ColorRes int contentColor
            , @ColorRes int contentColorTransform, @FloatRange(from = 0f, to = 1f) float contentAlpha) {
        return fitsSystemWindowsInt(fits, ContextCompat.getColor(mActivity, contentColor),
                ContextCompat.getColor(mActivity, contentColorTransform), contentAlpha);
    }

    /**
     * ?????????????????????????????????????????????????????????
     * Fits system windows int immersion bar.
     *
     * @param fits         the fits
     * @param contentColor the content color ?????????????????????
     * @return the immersion bar
     */
    public ImmersionBar fitsSystemWindowsInt(boolean fits, @ColorInt int contentColor) {
        return fitsSystemWindowsInt(fits, contentColor, Color.BLACK, 0);
    }

    /**
     * ?????????????????????????????????????????????????????????
     * Fits system windows int immersion bar.
     *
     * @param fits                  the fits
     * @param contentColor          the content color ?????????????????????
     * @param contentColorTransform the content color transform ?????????????????????????????????
     * @param contentAlpha          the content alpha ?????????????????????
     * @return the immersion bar
     */
    public ImmersionBar fitsSystemWindowsInt(boolean fits, @ColorInt int contentColor
            , @ColorInt int contentColorTransform, @FloatRange(from = 0f, to = 1f) float contentAlpha) {
        mBarParams.fits = fits;
        mBarParams.contentColor = contentColor;
        mBarParams.contentColorTransform = contentColorTransform;
        mBarParams.contentAlpha = contentAlpha;
        if (mBarParams.fits) {
            if (mFitsStatusBarType == FLAG_FITS_DEFAULT) {
                mFitsStatusBarType = FLAG_FITS_SYSTEM_WINDOWS;
            }
        } else {
            mFitsStatusBarType = FLAG_FITS_DEFAULT;
        }
        mContentView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.contentColor,
                mBarParams.contentColorTransform, mBarParams.contentAlpha));
        return this;
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param view the view
     * @return the immersion bar
     */
    public ImmersionBar statusBarView(View view) {
        if (view == null) {
            return this;
        }
        mBarParams.statusBarView = view;
        if (mFitsStatusBarType == FLAG_FITS_DEFAULT) {
            mFitsStatusBarType = FLAG_FITS_STATUS;
        }
        return this;
    }

    /**
     * ????????????????????????????????????????????????,?????????Activity?????????
     *
     * @param viewId the view id
     * @return the immersion bar
     */
    public ImmersionBar statusBarView(@IdRes int viewId) {
        return statusBarView(mActivity.findViewById(viewId));
    }

    /**
     * ????????????????????????????????????????????????
     * Status bar view immersion bar.
     *
     * @param viewId   the view id
     * @param rootView the root view
     * @return the immersion bar
     */
    public ImmersionBar statusBarView(@IdRes int viewId, View rootView) {
        return statusBarView(rootView.findViewById(viewId));
    }

    /**
     * ??????????????????????????????????????????????????????
     * Title bar immersion bar.
     *
     * @param view the view
     * @return the immersion bar
     */
    public ImmersionBar titleBar(View view) {
        if (view == null) {
            return this;
        }
        return titleBar(view, true);
    }

    /**
     * ??????????????????????????????????????????????????????
     * Title bar immersion bar.
     *
     * @param view                          the view
     * @param statusBarColorTransformEnable the status bar flag ?????????true false?????????????????????????????????true???????????????????????????
     * @return the immersion bar
     */
    public ImmersionBar titleBar(View view, boolean statusBarColorTransformEnable) {
        if (view == null) {
            return this;
        }
        if (mFitsStatusBarType == FLAG_FITS_DEFAULT) {
            mFitsStatusBarType = FLAG_FITS_TITLE;
        }
        mBarParams.titleBarView = view;
        mBarParams.statusBarColorEnabled = statusBarColorTransformEnable;
        return this;
    }

    /**
     * ??????????????????????????????????????????????????????????????????Activity
     * Title bar immersion bar.
     *
     * @param viewId the view id
     * @return the immersion bar
     */
    public ImmersionBar titleBar(@IdRes int viewId) {
        return titleBar(viewId, true);
    }

    /**
     * Title bar immersion bar.
     *
     * @param viewId                        the view id
     * @param statusBarColorTransformEnable the status bar flag
     * @return the immersion bar
     */
    public ImmersionBar titleBar(@IdRes int viewId, boolean statusBarColorTransformEnable) {
        if (mSupportFragment != null && mSupportFragment.getView() != null) {
            return titleBar(mSupportFragment.getView().findViewById(viewId), statusBarColorTransformEnable);
        } else if (mFragment != null && mFragment.getView() != null) {
            return titleBar(mFragment.getView().findViewById(viewId), statusBarColorTransformEnable);
        } else {
            return titleBar(mActivity.findViewById(viewId), statusBarColorTransformEnable);
        }
    }

    /**
     * Title bar immersion bar.
     *
     * @param viewId   the view id
     * @param rootView the root view
     * @return the immersion bar
     */
    public ImmersionBar titleBar(@IdRes int viewId, View rootView) {
        return titleBar(rootView.findViewById(viewId), true);
    }

    /**
     * ?????????????????????????????????????????????????????????????????????view
     * Title bar immersion bar.
     *
     * @param viewId                        the view id
     * @param rootView                      the root view
     * @param statusBarColorTransformEnable the status bar flag ?????????true false?????????????????????????????????true???????????????????????????
     * @return the immersion bar
     */
    public ImmersionBar titleBar(@IdRes int viewId, View rootView, boolean statusBarColorTransformEnable) {
        return titleBar(rootView.findViewById(viewId), statusBarColorTransformEnable);
    }

    /**
     * ?????????????????????????????????????????????????????????
     * Title bar margin top immersion bar.
     *
     * @param viewId the view id   ???????????????id
     * @return the immersion bar
     */
    public ImmersionBar titleBarMarginTop(@IdRes int viewId) {
        if (mSupportFragment != null && mSupportFragment.getView() != null) {
            return titleBarMarginTop(mSupportFragment.getView().findViewById(viewId));
        } else if (mFragment != null && mFragment.getView() != null) {
            return titleBarMarginTop(mFragment.getView().findViewById(viewId));
        } else {
            return titleBarMarginTop(mActivity.findViewById(viewId));
        }
    }

    /**
     * ?????????????????????????????????????????????????????????
     * Title bar margin top immersion bar.
     *
     * @param viewId   the view id  ???????????????id
     * @param rootView the root view  ??????view
     * @return the immersion bar
     */
    public ImmersionBar titleBarMarginTop(@IdRes int viewId, View rootView) {
        return titleBarMarginTop(rootView.findViewById(viewId));
    }

    /**
     * ?????????????????????????????????????????????????????????
     * Title bar margin top immersion bar.
     *
     * @param view the view  ?????????????????????view
     * @return the immersion bar
     */
    public ImmersionBar titleBarMarginTop(View view) {
        if (view == null) {
            return this;
        }
        if (mFitsStatusBarType == FLAG_FITS_DEFAULT) {
            mFitsStatusBarType = FLAG_FITS_TITLE_MARGIN_TOP;
        }
        mBarParams.titleBarView = view;
        return this;
    }

    /**
     * ?????????actionBar?????????,??????????????????????????????actionBar??????????????????
     * Support action bar immersion bar.
     *
     * @param isSupportActionBar the is support action bar
     * @return the immersion bar
     */
    public ImmersionBar supportActionBar(boolean isSupportActionBar) {
        mBarParams.isSupportActionBar = isSupportActionBar;
        return this;
    }

    /**
     * Status bar color transform enable immersion bar.
     *
     * @param statusBarColorTransformEnable the status bar flag
     * @return the immersion bar
     */
    public ImmersionBar statusBarColorTransformEnable(boolean statusBarColorTransformEnable) {
        mBarParams.statusBarColorEnabled = statusBarColorTransformEnable;
        return this;
    }

    /**
     * ????????????????????????
     * Reset immersion bar.
     *
     * @return the immersion bar
     */
    public ImmersionBar reset() {
        mBarParams = new BarParams();
        mFitsStatusBarType = FLAG_FITS_DEFAULT;
        return this;
    }

    /**
     * ?????????????????????tag???????????????bar?????????.
     * Add tag bar tag.
     *
     * @param tag the tag
     * @return the bar tag
     */
    public ImmersionBar addTag(String tag) {
        if (isEmpty(tag)) {
            throw new IllegalArgumentException("tag????????????");
        }
        BarParams barParams = mBarParams.clone();
        mTagMap.put(tag, barParams);
        return this;
    }

    /**
     * ??????tag?????????????????????????????????
     * Recover immersion bar.
     *
     * @param tag the tag
     * @return the immersion bar
     */
    public ImmersionBar getTag(String tag) {
        if (isEmpty(tag)) {
            throw new IllegalArgumentException("tag????????????");
        }
        BarParams barParams = mTagMap.get(tag);
        if (barParams != null) {
            mBarParams = barParams.clone();
        }
        return this;
    }

    /**
     * ????????????????????????????????????????????? ????????????false
     * Keyboard enable immersion bar.
     *
     * @param enable the enable
     * @return the immersion bar
     */
    public ImmersionBar keyboardEnable(boolean enable) {
        return keyboardEnable(enable, mBarParams.keyboardMode);
    }

    /**
     * ????????????????????????????????????????????? ????????????false
     *
     * @param enable       the enable
     * @param keyboardMode the keyboard mode
     * @return the immersion bar
     */
    public ImmersionBar keyboardEnable(boolean enable, int keyboardMode) {
        mBarParams.keyboardEnable = enable;
        mBarParams.keyboardMode = keyboardMode;
        mKeyboardTempEnable = enable;
        return this;
    }

    /**
     * ??????????????????
     * Keyboard mode immersion bar.
     *
     * @param keyboardMode the keyboard mode
     * @return the immersion bar
     */
    public ImmersionBar keyboardMode(int keyboardMode) {
        mBarParams.keyboardMode = keyboardMode;
        return this;
    }

    /**
     * ????????????????????????????????????
     * Sets on keyboard listener.
     *
     * @param onKeyboardListener the on keyboard listener
     * @return the on keyboard listener
     */
    public ImmersionBar setOnKeyboardListener(@Nullable OnKeyboardListener onKeyboardListener) {
        if (mBarParams.onKeyboardListener == null) {
            mBarParams.onKeyboardListener = onKeyboardListener;
        }
        return this;
    }

    /**
     * ??????????????????????????????
     * Sets on navigation bar listener.
     *
     * @param onNavigationBarListener the on navigation bar listener
     * @return the on navigation bar listener
     */
    public ImmersionBar setOnNavigationBarListener(OnNavigationBarListener onNavigationBarListener) {
        if (onNavigationBarListener != null) {
            if (mBarParams.onNavigationBarListener == null) {
                mBarParams.onNavigationBarListener = onNavigationBarListener;
                NavigationBarObserver.getInstance().addOnNavigationBarListener(mBarParams.onNavigationBarListener);
            }
        } else {
            if (mBarParams.onNavigationBarListener != null) {
                NavigationBarObserver.getInstance().removeOnNavigationBarListener(mBarParams.onNavigationBarListener);
                mBarParams.onNavigationBarListener = null;
            }
        }
        return this;
    }


    /**
     * Bar??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * Sets on bar listener.
     *
     * @param onBarListener the on bar listener
     * @return the on bar listener
     */
    public ImmersionBar setOnBarListener(OnBarListener onBarListener) {
        if (onBarListener != null) {
            if (mBarParams.onBarListener == null) {
                mBarParams.onBarListener = onBarListener;
            }
        } else {
            if (mBarParams.onBarListener != null) {
                mBarParams.onBarListener = null;
            }
        }
        return this;
    }

    /**
     * ?????????????????????????????????????????????true
     * ????????? navigationBarEnable > navigationBarWithEMUI3Enable > navigationBarWithKitkatEnable
     * Navigation bar enable immersion bar.
     *
     * @param navigationBarEnable the enable
     * @return the immersion bar
     */
    public ImmersionBar navigationBarEnable(boolean navigationBarEnable) {
        mBarParams.navigationBarEnable = navigationBarEnable;
        return this;
    }

    /**
     * ??????????????????4.4?????????????????????????????????true
     * ????????? navigationBarEnable > navigationBarWithEMUI3Enable > navigationBarWithKitkatEnable
     *
     * @param navigationBarWithKitkatEnable the navigation bar with kitkat enable
     * @return the immersion bar
     */
    public ImmersionBar navigationBarWithKitkatEnable(boolean navigationBarWithKitkatEnable) {
        mBarParams.navigationBarWithKitkatEnable = navigationBarWithKitkatEnable;
        if (OSUtils.isEMUI3_x()) {
            if (mBarParams.navigationBarWithEMUI3Enable) {
                mBarParams.navigationBarWithKitkatEnable = true;
            } else {
                if (mBarParams.navigationBarWithKitkatEnable) {
                    mBarParams.navigationBarWithKitkatEnable = false;
                }
            }
        }
        return this;
    }

    /**
     * ?????????????????????emui3.1???????????????????????????true???
     * ????????? navigationBarEnable > navigationBarWithEMUI3Enable > navigationBarWithKitkatEnable
     * Navigation bar with emui 3 enable immersion bar.
     *
     * @param navigationBarWithEMUI3Enable the navigation bar with emui 3 1 enable
     * @return the immersion bar
     */
    public ImmersionBar navigationBarWithEMUI3Enable(boolean navigationBarWithEMUI3Enable) {
        //??????????????????emui3?????????????????????
        if (OSUtils.isEMUI3_x()) {
            mBarParams.navigationBarWithEMUI3Enable = navigationBarWithEMUI3Enable;
            mBarParams.navigationBarWithKitkatEnable = navigationBarWithEMUI3Enable;
        }
        return this;
    }

    /**
     * ?????????????????????????????????????????????true???????????????false????????????????????????????????????????????????????????????????????????????????????
     * Bar enable immersion bar.
     *
     * @param barEnable the bar enable
     * @return the immersion bar
     */
    public ImmersionBar barEnable(boolean barEnable) {
        mBarParams.barEnable = barEnable;
        return this;
    }

    /**
     * ???????????????????????????????????????????????????
     */
    public void init() {
        if (mBarParams.barEnable) {
            //??????Bar?????????
            updateBarParams();
            //???????????????
            setBar();
            //??????????????????
            fitsWindows();
            //?????????????????????????????????????????????
            fitsKeyboard();
            //??????view
            transformView();
            mInitialized = true;
        }
    }

    /**
     * ????????????????????????
     */
    void destroy() {
        //????????????
        cancelListener();
        if (mIsDialog) {
            ImmersionBar immersionBar = with(mActivity);
            if (immersionBar != null) {
                immersionBar.mBarParams.keyboardEnable = immersionBar.mKeyboardTempEnable;
                if (immersionBar.mBarParams.barHide != BarHide.FLAG_SHOW_BAR) {
                    immersionBar.setBar();
                }
            }
        }
        mInitialized = false;
    }

    /**
     * ??????Bar?????????
     * Update bar params.
     */
    private void updateBarParams() {
        adjustDarkModeParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //??????Bar????????????
            updateBarConfig();
            //?????????Fragment???????????????Activity??????Fragment???BarParams??????
            if (mIsFragment) {
                ImmersionBar immersionBar = with(mActivity);
                if (immersionBar != null) {
                    immersionBar.mBarParams = mBarParams;
                }
            }
            //??????dialog????????????keyboardEnable???true??????Activity???????????????keyboardEnable???false
            if (mIsDialog) {
                ImmersionBar immersionBar = with(mActivity);
                if (immersionBar != null) {
                    if (immersionBar.mKeyboardTempEnable) {
                        immersionBar.mBarParams.keyboardEnable = false;
                    }
                }
            }
        }
    }

    /**
     * ??????????????????????????????
     */
    void setBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //??????????????????????????????????????????????????????
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_x()) {
                //???????????????
                fitsNotchScreen();
                //?????????5.0???????????????5.0
                uiFlags = initBarAboveLOLLIPOP(uiFlags);
                //android 6.0????????????????????????????????????
                uiFlags = setStatusBarDarkFont(uiFlags);
                //android 8.0????????????????????????????????????
                uiFlags = setNavigationIconDark(uiFlags);
            } else {
                //?????????5.0?????????4.4???????????????
                initBarBelowLOLLIPOP();
            }
            //??????????????????????????????
            uiFlags = hideBar(uiFlags);
            mDecorView.setSystemUiVisibility(uiFlags);
        }
        if (OSUtils.isMIUI6Later()) {
            //??????miui?????????????????????
            setMIUIBarDark(mWindow, IMMERSION_MIUI_STATUS_BAR_DARK, mBarParams.statusBarDarkFont);
            //??????miui????????????????????????
            if (mBarParams.navigationBarEnable) {
                setMIUIBarDark(mWindow, IMMERSION_MIUI_NAVIGATION_BAR_DARK, mBarParams.navigationBarDarkIcon);
            }
        }
        // ??????Flyme OS?????????????????????
        if (OSUtils.isFlymeOS4Later()) {
            if (mBarParams.flymeOSStatusBarFontColor != 0) {
                FlymeOSStatusBarFontUtils.setStatusBarDarkIcon(mActivity, mBarParams.flymeOSStatusBarFontColor);
            } else {
                FlymeOSStatusBarFontUtils.setStatusBarDarkIcon(mActivity, mBarParams.statusBarDarkFont);
            }
        }
        //????????????????????????????????????????????????????????????????????????????????????
        if (mBarParams.onNavigationBarListener != null) {
            NavigationBarObserver.getInstance().register(mActivity.getApplication());
        }
    }

    /**
     * ???????????????
     * Fits notch screen.
     */
    private void fitsNotchScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !initialized()) {
            WindowManager.LayoutParams lp = mWindow.getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            mWindow.setAttributes(lp);
        }
    }

    /**
     * ?????????android 5.0???????????????????????????
     *
     * @param uiFlags the ui flags
     * @return the int
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private int initBarAboveLOLLIPOP(int uiFlags) {
        //???????????????????????????
        if (!initialized()) {
            mBarParams.defaultNavigationBarColor = mWindow.getNavigationBarColor();
        }
        //Activity???????????????????????????????????????????????????????????????????????????Activity??????????????????????????????????????????
        uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (mBarParams.fullScreen && mBarParams.navigationBarEnable) {
            //Activity???????????????????????????????????????????????????????????????????????????Activity??????????????????????????????????????????
            uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //???????????????????????????
        if (mBarConfig.hasNavigationBar()) {
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        //?????????????????????????????????????????????????????????
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //?????????????????????
        if (mBarParams.statusBarColorEnabled) {
            mWindow.setStatusBarColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    mBarParams.statusBarColorTransform, mBarParams.statusBarAlpha));
        } else {
            mWindow.setStatusBarColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    Color.TRANSPARENT, mBarParams.statusBarAlpha));
        }
        //?????????????????????
        if (mBarParams.navigationBarEnable) {
            mWindow.setNavigationBarColor(ColorUtils.blendARGB(mBarParams.navigationBarColor,
                    mBarParams.navigationBarColorTransform, mBarParams.navigationBarAlpha));
        } else {
            mWindow.setNavigationBarColor(mBarParams.defaultNavigationBarColor);
        }
        return uiFlags;
    }

    /**
     * ?????????android 4.4???emui3.1?????????????????????
     */
    private void initBarBelowLOLLIPOP() {
        //???????????????
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //???????????????????????????
        setupStatusBarView();
        //?????????????????????????????????????????????????????????
        if (mBarConfig.hasNavigationBar() || OSUtils.isEMUI3_x()) {
            if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
                //???????????????????????????????????????????????????????????????????????????????????????
                mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            } else {
                mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            if (mNavigationBarHeight == 0) {
                mNavigationBarHeight = mBarConfig.getNavigationBarHeight();
            }
            if (mNavigationBarWidth == 0) {
                mNavigationBarWidth = mBarConfig.getNavigationBarWidth();
            }
            //???????????????????????????
            setupNavBarView();
        }
    }

    /**
     * ?????????????????????????????????????????????
     */
    private void setupStatusBarView() {
        View statusBarView = mDecorView.findViewById(IMMERSION_ID_STATUS_BAR_VIEW);
        if (statusBarView == null) {
            statusBarView = new View(mActivity);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    mBarConfig.getStatusBarHeight());
            params.gravity = Gravity.TOP;
            statusBarView.setLayoutParams(params);
            statusBarView.setVisibility(View.VISIBLE);
            statusBarView.setId(IMMERSION_ID_STATUS_BAR_VIEW);
            mDecorView.addView(statusBarView);
        }
        if (mBarParams.statusBarColorEnabled) {
            statusBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    mBarParams.statusBarColorTransform, mBarParams.statusBarAlpha));
        } else {
            statusBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    Color.TRANSPARENT, mBarParams.statusBarAlpha));
        }
    }

    /**
     * ?????????????????????????????????????????????
     */
    private void setupNavBarView() {
        View navigationBarView = mDecorView.findViewById(IMMERSION_ID_NAVIGATION_BAR_VIEW);
        if (navigationBarView == null) {
            navigationBarView = new View(mActivity);
            navigationBarView.setId(IMMERSION_ID_NAVIGATION_BAR_VIEW);
            mDecorView.addView(navigationBarView);
        }

        FrameLayout.LayoutParams params;
        if (mBarConfig.isNavigationAtBottom()) {
            params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mBarConfig.getNavigationBarHeight());
            params.gravity = Gravity.BOTTOM;
        } else {
            params = new FrameLayout.LayoutParams(mBarConfig.getNavigationBarWidth(), FrameLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.END;
        }
        navigationBarView.setLayoutParams(params);
        navigationBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.navigationBarColor,
                mBarParams.navigationBarColorTransform, mBarParams.navigationBarAlpha));

        if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable && !mBarParams.hideNavigationBar) {
            navigationBarView.setVisibility(View.VISIBLE);
        } else {
            navigationBarView.setVisibility(View.GONE);
        }
    }

    /**
     * ??????????????????????????????
     */
    private void adjustDarkModeParams() {
        if (mBarParams.autoStatusBarDarkModeEnable && mBarParams.statusBarColor != Color.TRANSPARENT) {
            boolean statusBarDarkFont = mBarParams.statusBarColor > IMMERSION_BOUNDARY_COLOR;
            statusBarDarkFont(statusBarDarkFont, mBarParams.autoStatusBarDarkModeAlpha);
        }
        if (mBarParams.autoNavigationBarDarkModeEnable && mBarParams.navigationBarColor != Color.TRANSPARENT) {
            boolean navigationBarDarkIcon = mBarParams.navigationBarColor > IMMERSION_BOUNDARY_COLOR;
            navigationBarDarkIcon(navigationBarDarkIcon, mBarParams.autoNavigationBarDarkModeAlpha);
        }
    }

    /**
     * Hide bar.
     * ???????????????????????????????????????
     *
     * @param uiFlags the ui flags
     * @return the int
     */
    private int hideBar(int uiFlags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            switch (mBarParams.barHide) {
                case FLAG_HIDE_BAR:
                    uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.INVISIBLE;
                    break;
                case FLAG_HIDE_STATUS_BAR:
                    uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.INVISIBLE;
                    break;
                case FLAG_HIDE_NAVIGATION_BAR:
                    uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                    break;
                case FLAG_SHOW_BAR:
                    uiFlags |= View.SYSTEM_UI_FLAG_VISIBLE;
                    break;
                default:
                    break;
            }
        }
        return uiFlags | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }

    /**
     * ??????????????????
     */
    void fitsWindows() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_x()) {
                //android 5.0??????????????????????????????????????????
                fitsWindowsAboveLOLLIPOP();
            } else {
                //android 5.0??????????????????????????????????????????
                fitsWindowsBelowLOLLIPOP();
            }
            //????????????????????????????????????
            fitsLayoutOverlap();
        }
    }

    /**
     * android 5.0??????????????????????????????????????????
     */
    private void fitsWindowsBelowLOLLIPOP() {
        if (mBarParams.isSupportActionBar) {
            mIsActionBarBelowLOLLIPOP = true;
            mContentView.post(this);
        } else {
            mIsActionBarBelowLOLLIPOP = false;
            postFitsWindowsBelowLOLLIPOP();
        }
    }

    @Override
    public void run() {
        postFitsWindowsBelowLOLLIPOP();
    }

    private void postFitsWindowsBelowLOLLIPOP() {
        updateBarConfig();
        //??????android4.4???????????????????????????activity????????????????????????????????????android 5.0??????????????????????????????????????????
        fitsWindowsKITKAT();
        //????????????emui3.1??????3.0??????????????????????????????
        if (!mIsFragment && OSUtils.isEMUI3_x()) {
            fitsWindowsEMUI();
        }
    }

    /**
     * android 5.0??????????????????????????????????????????
     * Fits windows above lollipop.
     */
    private void fitsWindowsAboveLOLLIPOP() {
        updateBarConfig();
        if (checkFitsSystemWindows(mDecorView.findViewById(android.R.id.content))) {
            if (mBarParams.isSupportActionBar) {
                setPadding(0, mActionBarHeight, 0, 0);
            }
            return;
        }
        int top = 0;
        if (mBarParams.fits && mFitsStatusBarType == FLAG_FITS_SYSTEM_WINDOWS) {
            top = mBarConfig.getStatusBarHeight();
        }
        if (mBarParams.isSupportActionBar) {
            top = mBarConfig.getStatusBarHeight() + mActionBarHeight;
        }
        setPadding(0, top, 0, 0);
    }

    /**
     * ??????android4.4???????????????????????????activity????????????????????????????????????android 5.0??????????????????????????????????????????
     * Fits windows below lollipop.
     */
    private void fitsWindowsKITKAT() {
        if (checkFitsSystemWindows(mDecorView.findViewById(android.R.id.content))) {
            if (mBarParams.isSupportActionBar) {
                setPadding(0, mActionBarHeight, 0, 0);
            }
            return;
        }
        int top = 0, right = 0, bottom = 0;
        if (mBarParams.fits && mFitsStatusBarType == FLAG_FITS_SYSTEM_WINDOWS) {
            top = mBarConfig.getStatusBarHeight();
        }
        if (mBarParams.isSupportActionBar) {
            top = mBarConfig.getStatusBarHeight() + mActionBarHeight;
        }
        if (mBarConfig.hasNavigationBar() && mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
            if (!mBarParams.fullScreen) {
                if (mBarConfig.isNavigationAtBottom()) {
                    bottom = mBarConfig.getNavigationBarHeight();
                } else {
                    right = mBarConfig.getNavigationBarWidth();
                }
            }
            if (mBarParams.hideNavigationBar) {
                if (mBarConfig.isNavigationAtBottom()) {
                    bottom = 0;
                } else {
                    right = 0;
                }
            } else {
                if (!mBarConfig.isNavigationAtBottom()) {
                    right = mBarConfig.getNavigationBarWidth();
                }
            }

        }
        setPadding(0, top, right, bottom);
    }

    /**
     * ??????emui3.x?????????????????????
     * Register emui 3 x.
     */
    private void fitsWindowsEMUI() {
        View navigationBarView = mDecorView.findViewById(IMMERSION_ID_NAVIGATION_BAR_VIEW);
        if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
            if (navigationBarView != null) {
                EMUI3NavigationBarObserver.getInstance().addOnNavigationBarListener(this);
                EMUI3NavigationBarObserver.getInstance().register(mActivity.getApplication());
            }
        } else {
            EMUI3NavigationBarObserver.getInstance().removeOnNavigationBarListener(this);
            navigationBarView.setVisibility(View.GONE);
        }
    }

    /**
     * ??????BarConfig
     */
    private void updateBarConfig() {
        mBarConfig = new BarConfig(mActivity);
        if (!initialized() || mIsActionBarBelowLOLLIPOP) {
            mActionBarHeight = mBarConfig.getActionBarHeight();
        }
        if (mFitsKeyboard != null) {
            mFitsKeyboard.updateBarConfig(mBarConfig);
        }
    }

    @Override
    public void onNavigationBarChange(boolean show) {
        View navigationBarView = mDecorView.findViewById(IMMERSION_ID_NAVIGATION_BAR_VIEW);
        if (navigationBarView != null) {
            mBarConfig = new BarConfig(mActivity);
            int bottom = mContentView.getPaddingBottom(), right = mContentView.getPaddingRight();
            if (!show) {
                //??????????????????
                navigationBarView.setVisibility(View.GONE);
                bottom = 0;
                right = 0;
            } else {
                //??????????????????
                navigationBarView.setVisibility(View.VISIBLE);
                if (checkFitsSystemWindows(mDecorView.findViewById(android.R.id.content))) {
                    bottom = 0;
                    right = 0;
                } else {
                    if (mNavigationBarHeight == 0) {
                        mNavigationBarHeight = mBarConfig.getNavigationBarHeight();
                    }
                    if (mNavigationBarWidth == 0) {
                        mNavigationBarWidth = mBarConfig.getNavigationBarWidth();
                    }
                    if (!mBarParams.hideNavigationBar) {
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) navigationBarView.getLayoutParams();
                        if (mBarConfig.isNavigationAtBottom()) {
                            params.gravity = Gravity.BOTTOM;
                            params.height = mNavigationBarHeight;
                            bottom = !mBarParams.fullScreen ? mNavigationBarHeight : 0;
                            right = 0;
                        } else {
                            params.gravity = Gravity.END;
                            params.width = mNavigationBarWidth;
                            bottom = 0;
                            right = !mBarParams.fullScreen ? mNavigationBarWidth : 0;
                        }
                        navigationBarView.setLayoutParams(params);
                    }
                }
            }
            setPadding(0, mContentView.getPaddingTop(), right, bottom);
        }
    }

    /**
     * Sets status bar dark font.
     * ??????????????????????????????android6.0??????
     */
    private int setStatusBarDarkFont(int uiFlags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mBarParams.statusBarDarkFont) {
            return uiFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            return uiFlags;
        }
    }

    /**
     * ????????????????????????????????????
     * Sets dark navigation icon.
     */
    private int setNavigationIconDark(int uiFlags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mBarParams.navigationBarDarkIcon) {
            return uiFlags | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        } else {
            return uiFlags;
        }
    }

    @SuppressLint("PrivateApi")
    private void setMIUIBarDark(Window window, String key, boolean dark) {
        if (window != null) {
            Class<? extends Window> clazz = window.getClass();
            try {
                int darkModeFlag;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField(key);
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    //??????????????????????????????
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    //??????????????????
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
            } catch (Exception ignored) {

            }
        }
    }

    /**
     * ????????????????????????????????????
     * Fits layout overlap.
     */
    private void fitsLayoutOverlap() {
        switch (mFitsStatusBarType) {
            case FLAG_FITS_TITLE:
                //????????????paddingTop???????????????????????????
                setTitleBar(mActivity, mBarParams.titleBarView);
                break;
            case FLAG_FITS_TITLE_MARGIN_TOP:
                //????????????marginTop???????????????????????????
                setTitleBarMarginTop(mActivity, mBarParams.titleBarView);
                break;
            case FLAG_FITS_STATUS:
                //????????????????????????????????????????????????
                setStatusBarView(mActivity, mBarParams.statusBarView);
                break;
            default:
                break;
        }
    }

    /**
     * ??????view
     * Transform view.
     */
    private void transformView() {
        if (mBarParams.viewMap.size() != 0) {
            Set<Map.Entry<View, Map<Integer, Integer>>> entrySet = mBarParams.viewMap.entrySet();
            for (Map.Entry<View, Map<Integer, Integer>> entry : entrySet) {
                View view = entry.getKey();
                Map<Integer, Integer> map = entry.getValue();
                Integer colorBefore = mBarParams.statusBarColor;
                Integer colorAfter = mBarParams.statusBarColorTransform;
                for (Map.Entry<Integer, Integer> integerEntry : map.entrySet()) {
                    colorBefore = integerEntry.getKey();
                    colorAfter = integerEntry.getValue();
                }
                if (view != null) {
                    if (Math.abs(mBarParams.viewAlpha - 0.0f) == 0) {
                        view.setBackgroundColor(ColorUtils.blendARGB(colorBefore, colorAfter, mBarParams.statusBarAlpha));
                    } else {
                        view.setBackgroundColor(ColorUtils.blendARGB(colorBefore, colorAfter, mBarParams.viewAlpha));
                    }
                }
            }
        }
    }

    /**
     * ????????????emui3.x???????????????????????????????????????
     * Cancel listener.
     */
    private void cancelListener() {
        if (mActivity != null) {
            if (mFitsKeyboard != null) {
                mFitsKeyboard.cancel();
                mFitsKeyboard = null;
            }
            EMUI3NavigationBarObserver.getInstance().removeOnNavigationBarListener(this);
            NavigationBarObserver.getInstance().removeOnNavigationBarListener(mBarParams.onNavigationBarListener);
        }
    }

    /**
     * ???????????????????????????????????????
     * Keyboard enable.
     */
    private void fitsKeyboard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!mIsFragment) {
                if (mBarParams.keyboardEnable) {
                    if (mFitsKeyboard == null) {
                        mFitsKeyboard = new FitsKeyboard(this, mActivity, mWindow);
                    }
                    mFitsKeyboard.enable(mBarParams.keyboardMode);
                } else {
                    if (mFitsKeyboard != null) {
                        mFitsKeyboard.disable();
                    }
                }
            } else {
                ImmersionBar immersionBar = with(mActivity);
                if (immersionBar != null) {
                    if (immersionBar.mBarParams.keyboardEnable) {
                        if (immersionBar.mFitsKeyboard == null) {
                            immersionBar.mFitsKeyboard = new FitsKeyboard(immersionBar, immersionBar.mActivity, immersionBar.mWindow);
                        }
                        immersionBar.mFitsKeyboard.enable(immersionBar.mBarParams.keyboardMode);
                    } else {
                        if (immersionBar.mFitsKeyboard != null) {
                            immersionBar.mFitsKeyboard.disable();
                        }
                    }
                }
            }
        }
    }

    /**
     * ????????????Fragment????????????
     *
     * @return the boolean
     */
    boolean isFragment() {
        return mIsFragment;
    }

    /**
     * ?????????????????????init()?????????
     */
    boolean initialized() {
        return mInitialized;
    }

    /**
     * ActionBar????????????LOLLIPOP???????????????
     *
     * @return boolean
     */
    boolean isActionBarBelowLOLLIPOP() {
        return mIsActionBarBelowLOLLIPOP;
    }

    /**
     * Gets bar params.
     *
     * @return the bar params
     */
    public BarParams getBarParams() {
        return mBarParams;
    }

    private void setPadding(int left, int top, int right, int bottom) {
        if (mContentView != null) {
            mContentView.setPadding(left, top, right, bottom);
        }
        mPaddingLeft = left;
        mPaddingTop = top;
        mPaddingRight = right;
        mPaddingBottom = bottom;
    }

    /**
     * Gets padding left.
     *
     * @return the padding left
     */
    int getPaddingLeft() {
        return mPaddingLeft;
    }

    /**
     * Gets padding top.
     *
     * @return the padding top
     */
    int getPaddingTop() {
        return mPaddingTop;
    }

    /**
     * Gets padding right.
     *
     * @return the padding right
     */
    int getPaddingRight() {
        return mPaddingRight;
    }

    /**
     * Gets padding bottom.
     *
     * @return the padding bottom
     */
    int getPaddingBottom() {
        return mPaddingBottom;
    }

    Activity getActivity() {
        return mActivity;
    }

    /**
     * ?????????????????????????????????????????????
     * Is support status bar dark font boolean.
     *
     * @return the boolean
     */
    public static boolean isSupportStatusBarDarkFont() {
        return OSUtils.isMIUI6Later() || OSUtils.isFlymeOS4Later()
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    /**
     * ???????????????????????????????????????
     * Is support navigation icon dark boolean.
     *
     * @return the boolean
     */
    public static boolean isSupportNavigationIconDark() {
        return OSUtils.isMIUI6Later() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * ??????????????????????????????
     * Sets title bar.
     *
     * @param activity the activity
     * @param view     the view
     */
    public static synchronized void setTitleBar(final Activity activity, final View... view) {
        if (activity == null) {
            return;
        }
        for (final View v : view) {
            if (v == null) {
                continue;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                final int statusBarHeight = getStatusBarHeight(activity);
                Integer fitsHeight = (Integer) v.getTag(R.id.mn_ib_fits_layout_overlap);
                if (fitsHeight == null) {
                    fitsHeight = 0;
                }
                if (fitsHeight != statusBarHeight) {
                    v.setTag(R.id.mn_ib_fits_layout_overlap, statusBarHeight);
                    ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                    if (layoutParams == null) {
                        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                    if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT ||
                            layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                        final ViewGroup.LayoutParams finalLayoutParams = layoutParams;
                        final Integer finalFitsHeight = fitsHeight;
                        v.post(new Runnable() {
                            @Override
                            public void run() {
                                finalLayoutParams.height = v.getHeight() + statusBarHeight - finalFitsHeight;
                                v.setPadding(v.getPaddingLeft(),
                                        v.getPaddingTop() + statusBarHeight - finalFitsHeight,
                                        v.getPaddingRight(),
                                        v.getPaddingBottom());
                                v.setLayoutParams(finalLayoutParams);
                            }
                        });
                    } else {
                        layoutParams.height += statusBarHeight - fitsHeight;
                        v.setPadding(v.getPaddingLeft(), v.getPaddingTop() + statusBarHeight - fitsHeight,
                                v.getPaddingRight(), v.getPaddingBottom());
                        v.setLayoutParams(layoutParams);
                    }
                }
            }
        }
    }

    public static void setTitleBar(Fragment fragment, View... view) {
        if (fragment == null) {
            return;
        }
        setTitleBar(fragment.getActivity(), view);
    }

    public static void setTitleBar(android.app.Fragment fragment, View... view) {
        if (fragment == null) {
            return;
        }
        setTitleBar(fragment.getActivity(), view);
    }

    /**
     * ???????????????MarginTop????????????????????????
     * Sets title bar margin top.
     *
     * @param activity the activity
     * @param view     the view
     */
    public static void setTitleBarMarginTop(Activity activity, View... view) {
        if (activity == null) {
            return;
        }
        for (View v : view) {
            if (v == null) {
                continue;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                int statusBarHeight = getStatusBarHeight(activity);
                Integer fitsHeight = (Integer) v.getTag(R.id.mn_ib_fits_layout_overlap);
                if (fitsHeight == null) {
                    fitsHeight = 0;
                }
                if (fitsHeight != statusBarHeight) {
                    v.setTag(R.id.mn_ib_fits_layout_overlap, statusBarHeight);
                    ViewGroup.LayoutParams lp = v.getLayoutParams();
                    if (lp == null) {
                        lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) lp;
                    layoutParams.setMargins(layoutParams.leftMargin,
                            layoutParams.topMargin + statusBarHeight - fitsHeight,
                            layoutParams.rightMargin,
                            layoutParams.bottomMargin);
                    v.setLayoutParams(layoutParams);
                }
            }
        }
    }

    public static void setTitleBarMarginTop(Fragment fragment, View... view) {
        if (fragment == null) {
            return;
        }
        setTitleBarMarginTop(fragment.getActivity(), view);
    }

    public static void setTitleBarMarginTop(android.app.Fragment fragment, View... view) {
        if (fragment == null) {
            return;
        }
        setTitleBarMarginTop(fragment.getActivity(), view);
    }

    /**
     * ?????????????????????????????????view??????????????????????????????
     * Sets status bar view.
     *
     * @param activity the activity
     * @param view     the view
     */
    public static void setStatusBarView(Activity activity, View view) {
        if (activity == null) {
            return;
        }
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight(activity);
            Integer fitsHeight = (Integer) view.getTag(R.id.mn_ib_fits_layout_overlap);
            if (fitsHeight == null) {
                fitsHeight = 0;
            }
            if (fitsHeight != statusBarHeight) {
                view.setTag(R.id.mn_ib_fits_layout_overlap, statusBarHeight);
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                if (lp == null) {
                    lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                }
                lp.height = statusBarHeight;
                view.setLayoutParams(lp);
            }
        }
    }

    public static void setStatusBarView(Fragment fragment, View view) {
        if (fragment == null) {
            return;
        }
        setStatusBarView(fragment.getActivity(), view);
    }

    public static void setStatusBarView(android.app.Fragment fragment, View view) {
        if (fragment == null) {
            return;
        }
        setStatusBarView(fragment.getActivity(), view);
    }

    /**
     * ?????????????????????????????????
     * Sets fits system windows.
     *
     * @param activity the activity
     */
    public static void setFitsSystemWindows(Activity activity) {
        if (activity == null) {
            return;
        }
        ViewGroup parent = activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                if (childView instanceof DrawerLayout) {
                    continue;
                }
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    public static void setFitsSystemWindows(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        setFitsSystemWindows(fragment.getActivity());
    }

    public static void setFitsSystemWindows(android.app.Fragment fragment) {
        if (fragment == null) {
            return;
        }
        setFitsSystemWindows(fragment.getActivity());
    }

    /**
     * ????????????????????????????????????android:fitsSystemWindows="true"??????
     * Check fits system windows boolean.
     *
     * @param view the view
     * @return the boolean
     */
    public static boolean checkFitsSystemWindows(View view) {
        if (view == null) {
            return false;
        }
        if (view.getFitsSystemWindows()) {
            return true;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0, count = viewGroup.getChildCount(); i < count; i++) {
                View childView = viewGroup.getChildAt(i);
                if (childView instanceof DrawerLayout) {
                    if (checkFitsSystemWindows(childView)) {
                        return true;
                    }
                }
                if (childView.getFitsSystemWindows()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Has navigtion bar boolean.
     * ???????????????????????????
     *
     * @param activity the activity
     * @return the boolean
     */
    @TargetApi(14)
    public static boolean hasNavigationBar(@NonNull Activity activity) {
        BarConfig config = new BarConfig(activity);
        return config.hasNavigationBar();
    }

    @TargetApi(14)
    public static boolean hasNavigationBar(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return false;
        }
        return hasNavigationBar(fragment.getActivity());
    }

    @TargetApi(14)
    public static boolean hasNavigationBar(@NonNull android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            return false;
        }
        return hasNavigationBar(fragment.getActivity());
    }

    /**
     * Gets navigation bar height.
     * ????????????????????????
     *
     * @param activity the activity
     * @return the navigation bar height
     */
    @TargetApi(14)
    public static int getNavigationBarHeight(@NonNull Activity activity) {
        BarConfig config = new BarConfig(activity);
        return config.getNavigationBarHeight();
    }

    @TargetApi(14)
    public static int getNavigationBarHeight(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getNavigationBarHeight(fragment.getActivity());
    }

    @TargetApi(14)
    public static int getNavigationBarHeight(@NonNull android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getNavigationBarHeight(fragment.getActivity());
    }

    /**
     * Gets navigation bar width.
     * ????????????????????????
     *
     * @param activity the activity
     * @return the navigation bar width
     */
    @TargetApi(14)
    public static int getNavigationBarWidth(@NonNull Activity activity) {
        BarConfig config = new BarConfig(activity);
        return config.getNavigationBarWidth();
    }

    @TargetApi(14)
    public static int getNavigationBarWidth(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getNavigationBarWidth(fragment.getActivity());
    }

    @TargetApi(14)
    public static int getNavigationBarWidth(@NonNull android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getNavigationBarWidth(fragment.getActivity());
    }

    /**
     * Is navigation at bottom boolean.
     * ??????????????????????????????
     *
     * @param activity the activity
     * @return the boolean
     */
    @TargetApi(14)
    public static boolean isNavigationAtBottom(@NonNull Activity activity) {
        BarConfig config = new BarConfig(activity);
        return config.isNavigationAtBottom();
    }

    @TargetApi(14)
    public static boolean isNavigationAtBottom(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return false;
        }
        return isNavigationAtBottom(fragment.getActivity());
    }

    @TargetApi(14)
    public static boolean isNavigationAtBottom(@NonNull android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            return false;
        }
        return isNavigationAtBottom(fragment.getActivity());
    }

    /**
     * Gets status bar height.
     * ????????????????????????
     *
     * @param activity the activity
     * @return the status bar height
     */
    @TargetApi(14)
    public static int getStatusBarHeight(@NonNull Activity activity) {
        BarConfig config = new BarConfig(activity);
        return config.getStatusBarHeight();
    }

    @TargetApi(14)
    public static int getStatusBarHeight(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getStatusBarHeight(fragment.getActivity());
    }

    @TargetApi(14)
    public static int getStatusBarHeight(@NonNull android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getStatusBarHeight(fragment.getActivity());
    }

    /**
     * Gets action bar height.
     * ??????ActionBar?????????
     *
     * @param activity the activity
     * @return the action bar height
     */
    @TargetApi(14)
    public static int getActionBarHeight(@NonNull Activity activity) {
        BarConfig config = new BarConfig(activity);
        return config.getActionBarHeight();
    }

    @TargetApi(14)
    public static int getActionBarHeight(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getActionBarHeight(fragment.getActivity());
    }

    @TargetApi(14)
    public static int getActionBarHeight(@NonNull android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getActionBarHeight(fragment.getActivity());
    }

    /**
     * ??????????????????
     * Has notch screen boolean.
     * e.g:getWindow().getDecorView().post(() -> ImmersionBar.hasNotchScreen(this));
     *
     * @param activity the activity
     * @return the boolean
     */
    public static boolean hasNotchScreen(@NonNull Activity activity) {
        return NotchUtils.hasNotchScreen(activity);
    }

    public static boolean hasNotchScreen(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return false;
        }
        return hasNotchScreen(fragment.getActivity());
    }

    public static boolean hasNotchScreen(@NonNull android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            return false;
        }
        return hasNotchScreen(fragment.getActivity());
    }

    /**
     * ??????????????????
     * Has notch screen boolean.
     *
     * @param view the view
     * @return the boolean
     */
    public static boolean hasNotchScreen(@NonNull View view) {
        return NotchUtils.hasNotchScreen(view);
    }

    /**
     * ???????????????
     * Notch height int.
     * e.g: getWindow().getDecorView().post(() -> ImmersionBar.getNotchHeight(this));
     *
     * @param activity the activity
     * @return the int
     */
    public static int getNotchHeight(@NonNull Activity activity) {
        if (hasNotchScreen(activity)) {
            return NotchUtils.getNotchHeight(activity);
        } else {
            return 0;
        }
    }

    public static int getNotchHeight(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getNotchHeight(fragment.getActivity());
    }

    public static int getNotchHeight(@NonNull android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getNotchHeight(fragment.getActivity());
    }

    /**
     * ???????????????
     * Hide status bar.
     *
     * @param window the window
     */
    public static void hideStatusBar(@NonNull Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * ???????????????
     * Show status bar.
     *
     * @param window the window
     */
    public static void showStatusBar(@NonNull Window window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private static RequestManagerRetriever getRetriever() {
        return RequestManagerRetriever.getInstance();
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
