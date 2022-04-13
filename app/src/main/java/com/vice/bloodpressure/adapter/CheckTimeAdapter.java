package com.vice.bloodpressure.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.base.XyBaseAdapter;
import com.vice.bloodpressure.bean.CheckTimeInfo;

import java.util.List;

/**
 * Author: LYD
 * Date: 2022/4/12 16:37
 * Description:
 */
public class CheckTimeAdapter extends XyBaseAdapter<CheckTimeInfo> {
    public CheckTimeAdapter(Context mContext, List<CheckTimeInfo> mList) {
        super(mContext, mList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(getContext(), R.layout.item_check_time_list, null);
            viewHolder.chooseTextView = convertView.findViewById(R.id.tv_check_time_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CheckTimeInfo info = getList().get(position);
        viewHolder.chooseTextView.setText(info.getHour());

        return convertView;
    }

    private class ViewHolder {
        TextView chooseTextView;
    }
}
