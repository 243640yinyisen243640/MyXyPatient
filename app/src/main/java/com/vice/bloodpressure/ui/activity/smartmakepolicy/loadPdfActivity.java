package com.vice.bloodpressure.ui.activity.smartmakepolicy;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseActivity;
import com.wdeo3601.pdfview.PDFView;

/**
 * Author: LYD
 * Date: 2022/3/3 9:14
 * Description: 测试PDF的
 */
public class loadPdfActivity extends BaseActivity {
    private PDFView pdfView;

    protected RelativeLayout contentLayout;


    private String url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        hideTvSave();
        setTitle(title);

        // 设置当前显示页的前后缓存个数，效果类似 ViewPager 的这个属性
        pdfView.setOffscreenPageLimit(2);

        // 是否支持缩放
        pdfView.isCanZoom(true);

        // 设置最大缩放倍数,最大支持20倍
        pdfView.setMaxScale(10f);

        // 从本地文件打开 pdf
        //        pdfView.showPdfFromPath(filePath);
        // 从网络打开 pdf
        pdfView.showPdfFromUrl(url);
    }

    @Override
    protected View addContentLayout() {
        View view = getLayoutInflater().inflate(R.layout.activity_text_pdf, contentLayout, false);
        pdfView = view.findViewById(R.id.load_pdf);
        return view;
    }

}
