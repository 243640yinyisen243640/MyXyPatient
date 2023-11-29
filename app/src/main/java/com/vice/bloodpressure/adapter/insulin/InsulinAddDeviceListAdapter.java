package com.vice.bloodpressure.adapter.insulin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.bean.BlueInfo;

import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InsulinAddDeviceListAdapter extends BaseAdapter {
    private Context context;
    private List<BlueInfo> list;

    public InsulinAddDeviceListAdapter(Context context, List<BlueInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return  list.size();
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
        //        if (convertView == null) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_insulin_add_device, parent, false);
        TextView textView = convertView.findViewById(R.id.tv_insulin_add_device_num);
        textView.setText(list.get(position).getDevice().getName());
        ImageView imageView = convertView.findViewById(R.id.cb_insulin_add_device_check);
        if (list.get(position).isCheck()){
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.insulin_device_aad_check));
        }else {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.insulin_device_aad_uncheck));
        }
        return convertView;
    }


}
