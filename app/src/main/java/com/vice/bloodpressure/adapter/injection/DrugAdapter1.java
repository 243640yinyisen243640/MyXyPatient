package com.vice.bloodpressure.adapter.injection;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
public class DrugAdapter1 extends BaseAdapter {
    private Context context;
    private List<DrugInfo> list;

    public DrugAdapter1(Context context, List<DrugInfo> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_drug1, parent, false);
            View view = convertView.findViewById(R.id.v_drug_check);
            TextView textView = convertView.findViewById(R.id.v_drug_name);
            textView.setText(list.get(position).getName());
            if (list.get(position).isCheck()) {
                view.setVisibility(View.VISIBLE);
                textView.setTextColor(Color.parseColor("#0CA25B"));
            } else {
                view.setVisibility(View.INVISIBLE);
                textView.setTextColor(Color.parseColor("#838383"));
            }
//        }
        return convertView;
    }


}
