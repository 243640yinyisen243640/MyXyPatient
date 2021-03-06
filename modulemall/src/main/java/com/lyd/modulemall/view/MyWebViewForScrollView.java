package com.lyd.modulemall.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.lyd.baselib.widget.view.MyWebView;

/**
 * 描述: 适用于ScrollView的WebView
 * 作者: LYD
 * 创建日期: 2021/1/4 9:53
 */
public class MyWebViewForScrollView extends MyWebView {
    public float oldY;
    private int t;
    private float oldX;

    public MyWebViewForScrollView(Context context) {
        super(context);
    }

    public MyWebViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float Y = ev.getY();
                float Ys = Y - oldY;
                float X = ev.getX();
                //滑动到顶部让父控件重新获得触摸事件
                if (Ys > 0 && t == 0) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_DOWN:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                oldY = ev.getY();
                oldX = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.t = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
