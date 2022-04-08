package com.vice.bloodpressure.adapter;

import android.content.Intent;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseWebViewActivity;
import com.vice.bloodpressure.bean.DataBean;
import com.vice.bloodpressure.constant.DataFormatManager;
import com.vice.bloodpressure.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

public class LittleToolsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public LittleToolsAdapter(@Nullable List<String> data) {
        super(R.layout.item_little_tools, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, String s) {
        int layoutPosition = viewHolder.getLayoutPosition();
        String[] stringArray = Utils.getApp().getResources().getStringArray(R.array.little_tools_text);
        viewHolder.setText(R.id.tv_little_tools, stringArray[layoutPosition]);

        TypedArray imgArray = Utils.getApp().getResources().obtainTypedArray(R.array.little_tools_img);
        viewHolder.setImageResource(R.id.img_little_tools, imgArray.getResourceId(layoutPosition, 0));

        //点击
        String[] assessmentUrls = Utils.getApp().getResources().getStringArray(R.array.assessment_url);
        List<DataBean> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            DataBean dataBean = new DataBean(0, assessmentUrls[i], "");
            list.add(dataBean);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutPosition != 6) {
                    Intent intent = new Intent(Utils.getApp(), BaseWebViewActivity.class);
                    intent.putExtra("title", "自我检测");
                    intent.putExtra("url", list.get(layoutPosition).getDescribe());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Utils.getApp().startActivity(intent);
                } else {
                    //http://h5.xiyuns.cn/home?token=4b89f6e2c5e721b7f9e7ff515684fe0c&timestamp=2022-03-30 16:08:56
                    String url = "http://h5.xiyuns.cn/home?token=4b89f6e2c5e721b7f9e7ff515684fe0c";
                    String currntTime = DataUtils.currentDateString(DataFormatManager.TIME_FORMAT_Y_M_D_H_M_S);
                    Log.i("yys","currentTime"+currntTime);
                    LoginBean loginBean = (LoginBean) SharedPreferencesUtils.getBean(Utils.getApp(), SharedPreferencesUtils.USER_INFO);

                    String[] strAll = url.split("=");

                    Intent intent = new Intent(Utils.getApp(), BaseWebViewActivity.class);
                    intent.putExtra("title", "自我检测");
                    intent.putExtra("url", strAll[0] +"="+ loginBean.getToken()+"&timestamp="+currntTime);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Utils.getApp().startActivity(intent);
                }

            }
        });
    }
}
