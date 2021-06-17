package com.vice.bloodpressure.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vice.bloodpressure.R;
import com.vice.bloodpressure.bean.TemperatureListDataBean;

import java.util.List;

public class TemperatureListAdapter extends BaseQuickAdapter<TemperatureListDataBean, BaseViewHolder> {

    public TemperatureListAdapter(@Nullable List<TemperatureListDataBean> data) {
        super(R.layout.item_temperature_record_list, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, TemperatureListDataBean bean) {
        String datetime = bean.getDatetime();
        holder.setText(R.id.tv_time_temperature, datetime);
        holder.setText(R.id.tv_value_temperature,bean.getTemperature());
        if ("1".equals(bean.getType())){
            holder.setText(R.id.tv_type_temperature, R.string.up_temperature_automatic);
        }else {
            holder.setText(R.id.tv_type_temperature, R.string.up_temperature_manualc);
        }

    }
}
