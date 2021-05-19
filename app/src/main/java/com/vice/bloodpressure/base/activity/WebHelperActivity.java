package com.vice.bloodpressure.base.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.view.X5WebView;

public class WebHelperActivity extends XYSoftUIBaseActivity {
    private ProgressBar progressBar;
    /**
     * webView
     */
    private X5WebView webView;
    /**
     * 说明/协议链接
     */
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topViewManager().titleTextView().setText(getIntent().getStringExtra("title"));
        containerView().addView(initView());
        url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            setWebViewData(webView, url);
        }
    }

    private View initView() {
        View view = View.inflate(getPageContext(), R.layout.base_activity_webview_help, null);
        progressBar = getViewByID(view, R.id.progressBar);
        webView = getViewByID(view, R.id.wv_helper);
        return view;
    }

    protected void setWebViewData(final X5WebView webView, final String url) {
        initHardwareAccelerate();

        webView.loadUrl(url);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
                super.onProgressChanged(webView, newProgress);
            }
        });
    }

    /**
     * 启用硬件加速
     */
    private void initHardwareAccelerate() {
        try {
            getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        } catch (Exception e) {
        }
    }
}
