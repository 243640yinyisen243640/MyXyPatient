package com.vice.bloodpressure.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyd.baselib.bean.LoginBean;
import com.lyd.baselib.utils.SharedPreferencesUtils;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.umeng.analytics.MobclickAgent;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.activity.BaseWebViewActivity;
import com.vice.bloodpressure.bean.GoodsRecommendBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeTwoGoodsAdapter extends BaseQuickAdapter<GoodsRecommendBean, BaseViewHolder> {
    public HomeTwoGoodsAdapter(@Nullable List<GoodsRecommendBean> data) {
        super(R.layout.item_home_eight_module, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, GoodsRecommendBean goodsRecommendBean) {
        QMUIRadiusImageView img = holder.getView(R.id.img_module);
        img.setCornerRadius(10);
        int screenWidth = ScreenUtils.getScreenWidth();
        int paddingWidth = SizeUtils.dp2px(1 * 11 + 2 * 9);
        int totalWidth = screenWidth - paddingWidth;
        int width = totalWidth / 2;
        int height = totalWidth / 4;
        ViewGroup.LayoutParams params = img.getLayoutParams();
        params.width = width;
        params.height = height;
        img.setLayoutParams(params);
        String imgurl = goodsRecommendBean.getImgurl();
        Glide.with(Utils.getApp())
                .load(imgurl)
                .into(img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(goodsRecommendBean.getWeburl())) {

                    LoginBean user = (LoginBean) SharedPreferencesUtils.getBean(Utils.getApp(), SharedPreferencesUtils.USER_INFO);
                    StringBuilder stringBuilderGoods = new StringBuilder();
                    stringBuilderGoods.append(user.getNickname()).append("+").append(user.getUsername());


                    StringBuilder eventIDBuilder = new StringBuilder();
                    eventIDBuilder.append("activity").append("_").append(goodsRecommendBean.getActivityID());

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("nametel", stringBuilderGoods.toString());
                    //上下文   事件ID   map
                    MobclickAgent.onEventObject(Utils.getApp(), eventIDBuilder.toString(), map);

                    Intent intent = new Intent(Utils.getApp(), BaseWebViewActivity.class);
                    intent.putExtra("title", goodsRecommendBean.getName());
                    intent.putExtra("url", goodsRecommendBean.getWeburl());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Utils.getApp().startActivity(intent);
                }
            }
        });
    }
}
