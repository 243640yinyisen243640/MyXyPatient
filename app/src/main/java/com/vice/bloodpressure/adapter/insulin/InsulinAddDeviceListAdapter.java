package com.vice.bloodpressure.adapter.insulin;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.bean.DrugInfo;

import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InsulinAddDeviceListAdapter extends BaseAdapter {
    private Context context;
    private List<DrugInfo> list;

    public InsulinAddDeviceListAdapter(Context context, List<DrugInfo> list) {
        this.context = context;
        this.list = list;
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
        //        if (convertView == null) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_insulin_add_device, parent, false);
        TextView textView = convertView.findViewById(R.id.v_drug_name);
        ImageView imageView = convertView.findViewById(R.id.iv_drug_check);
        textView.setText(list.get(position).getName());
        if (list.get(position).isCheck()) {
            imageView.setVisibility(View.VISIBLE);
            textView.setTextColor(Color.parseColor("#0CA25B"));
            if (position == 0) {
                textView.setBackground(context.getDrawable(R.drawable.shape_drug2_10));
            } else {
                textView.setBackgroundColor(Color.parseColor("#330CA25B"));
            }
        } else {
            imageView.setVisibility(View.GONE);
            textView.setTextColor(Color.parseColor("#333333"));
            textView.setBackgroundColor(Color.parseColor("#00000000"));
        }
        //        }
        return convertView;
    }


}
