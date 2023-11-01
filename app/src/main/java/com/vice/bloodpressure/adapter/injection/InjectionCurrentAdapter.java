package com.vice.bloodpressure.adapter.injection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.bean.injection.InjectionDataDetail;

import java.util.List;


/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InjectionCurrentAdapter extends RecyclerView.Adapter<InjectionCurrentAdapter.ViewHolder> {
    private Context context;
    private List<InjectionDataDetail.Detail> listInfos;

    public InjectionCurrentAdapter(Context context, List<InjectionDataDetail.Detail> listInfos) {
        this.context = context;
        this.listInfos = listInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout._item_fanganxiangqing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvStartTime.setText(listInfos.get(position).getBegin());
        holder.tvEndTime.setText(listInfos.get(position).getEnd());
        holder.tvName.setText(listInfos.get(position).getDrug_name());
        holder.tvValue.setText(listInfos.get(position).getValue() + "");
        holder.tvTimes.setText("第"+listInfos.get(position).getTimes() + "针(单位)");
        if (listInfos.size() == 1) {
            holder.ivIcon.setVisibility(View.VISIBLE);
            holder.vLineTop.setVisibility(View.INVISIBLE);
            holder.ivIcon.setImageResource(R.drawable.program_sun);
            holder.vLineBottom.setVisibility(View.INVISIBLE);
        } else {
            if (position == 0) {
                holder.ivIcon.setVisibility(View.VISIBLE);
                holder.vLineTop.setVisibility(View.INVISIBLE);
                holder.ivIcon.setImageResource(R.drawable.program_sun);
                holder.vLineBottom.setVisibility(View.VISIBLE);
            } else if (position == listInfos.size() - 1) {
                holder.vLineTop.setVisibility(View.VISIBLE);
                holder.ivIcon.setVisibility(View.VISIBLE);
                holder.ivIcon.setImageResource(R.drawable.program_moon);
                holder.vLineBottom.setVisibility(View.INVISIBLE);
            } else {
                holder.vLineTop.setVisibility(View.VISIBLE);
                holder.ivIcon.setVisibility(View.GONE);
                holder.vLineBottom.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listInfos.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStartTime;
        TextView tvEndTime;

        View vLineTop;
        ImageView ivIcon;
        View vLineBottom;

        TextView tvName;
        TextView tvValue;
        TextView tvTimes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStartTime = itemView.findViewById(R.id.tv_injection_begin_time);
            tvEndTime = itemView.findViewById(R.id.tv_injection_end_time);
            vLineTop = itemView.findViewById(R.id.v_injection_line_top);
            ivIcon = itemView.findViewById(R.id.iv_injection_icon);
            vLineBottom = itemView.findViewById(R.id.v_injection_line_bottom);
            tvName = itemView.findViewById(R.id.tv_injection_name);
            tvValue = itemView.findViewById(R.id.tv_injection_value);
            tvTimes = itemView.findViewById(R.id.tv_injection_times);
        }
    }
}
