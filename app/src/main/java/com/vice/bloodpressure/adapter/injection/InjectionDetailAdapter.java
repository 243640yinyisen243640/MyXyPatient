package com.vice.bloodpressure.adapter.injection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.bean.injection.InjectionDataListInfo;

import java.util.List;


/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InjectionDetailAdapter extends RecyclerView.Adapter<InjectionDetailAdapter.ViewHolder> {
    private Context context;
    private List<InjectionDataListInfo.InjectionData> listInfos;

    public InjectionDetailAdapter(Context context, List<InjectionDataListInfo.InjectionData> listInfos) {
        this.context = context;
        this.listInfos = listInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout._item_xiangqing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvValue.setText(listInfos.get(position).getValue());
        String isAuto = listInfos.get(position).getType();
//        holder. tvIsAutomatic.setText(isAuto.equals("1")?"自动添加":"手动添加");
        if ("1".equals(isAuto)){
            holder.tvIsAutomatic.setText("自动添加");
            holder.tvIsAutomatic.setCompoundDrawablesWithIntrinsicBounds(R.drawable.injection_add_with_auto, 0, 0, 0);
        }else {
            holder.tvIsAutomatic.setText("手动添加");
            holder.tvIsAutomatic.setCompoundDrawablesWithIntrinsicBounds(R.drawable.injection_add_with_hand, 0, 0, 0);

        }
        holder.tvDate.setText(listInfos.get(position).getDate());
        holder.tvTime.setText(listInfos.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return listInfos.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvValue;
        TextView tvIsAutomatic;
        TextView tvDate;
        TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvValue = itemView.findViewById(R.id.tv_detail_item_value);
            tvIsAutomatic = itemView.findViewById(R.id.tv_detail_item_is_auto);
            tvDate = itemView.findViewById(R.id.tv_detail_item_date);
            tvTime = itemView.findViewById(R.id.tv_detail_item_time);
        }
    }
}
