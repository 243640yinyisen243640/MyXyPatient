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
import com.vice.bloodpressure.imp.AdapterClickImp;

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
    private AdapterClickImp listener;
    public InjectionDetailAdapter(Context context, List<InjectionDataListInfo.InjectionData> listInfos,AdapterClickImp listener) {
        this.context = context;
        this.listInfos = listInfos;
        this.listener = listener;
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
        if ("1".equals(isAuto)){
            holder.tvEdit.setVisibility(View.GONE);
            holder.tvIsAutomatic.setText("自动添加");
            holder.tvIsAutomatic.setCompoundDrawablesWithIntrinsicBounds(R.drawable.injection_add_with_auto, 0, 0, 0);
        }else {
            holder.tvEdit.setVisibility(View.VISIBLE);
            holder.tvIsAutomatic.setText("手动添加");
            holder.tvIsAutomatic.setCompoundDrawablesWithIntrinsicBounds(R.drawable.injection_add_with_hand, 0, 0, 0);

        }
        holder.tvDate.setText(listInfos.get(position).getDate());
        holder.tvTime.setText(listInfos.get(position).getTime());
        holder.tvEdit.setOnClickListener(v -> {

            listener.onAdapterClick(v,position);

        });
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
        TextView tvEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvValue = itemView.findViewById(R.id.tv_detail_item_value);
            tvIsAutomatic = itemView.findViewById(R.id.tv_detail_item_is_auto);
            tvDate = itemView.findViewById(R.id.tv_detail_item_date);
            tvTime = itemView.findViewById(R.id.tv_detail_item_time);
            tvEdit = itemView.findViewById(R.id.tv_detail_item_edit);
        }
    }
}
