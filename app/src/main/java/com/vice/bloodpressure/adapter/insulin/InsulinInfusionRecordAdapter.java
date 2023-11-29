package com.vice.bloodpressure.adapter.insulin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.bean.insulin.InsulinDeviceInfo;

import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InsulinInfusionRecordAdapter extends BaseAdapter {
    private Context context;
    private List<InsulinDeviceInfo> list;
    private String type;

    public InsulinInfusionRecordAdapter(Context context, List<InsulinDeviceInfo> list, String type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_insulin_infusion_record, parent, false);
        TextView tvTime = convertView.findViewById(R.id.tv_insulin_infusion_record_time);
        TextView tvValues = convertView.findViewById(R.id.tv_insulin_infusion_record_values);
        tvTime.setText(list.get(position).getDatetime());
        if ("4".equals(type)) {
            tvValues.setText(list.get(position).getValue());
        } else {
            tvValues.setText(list.get(position).getValue() + "U");
        }

        return convertView;
    }


}
