package com.vice.bloodpressure.adapter.insulin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.bean.insulin.PlanInfo;
import com.vice.bloodpressure.imp.AdapterClickImp;

import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InsulinInfusionPlanAdapter extends RecyclerView.Adapter<InsulinInfusionPlanAdapter.ViewHolder> {
    private Context context;
    private List<PlanInfo> list;
    private String type;
    private AdapterClickImp clickImp;

    public InsulinInfusionPlanAdapter(Context context, List<PlanInfo> list, String type, AdapterClickImp clickImp) {
        this.context = context;
        this.list = list;
        this.type = type;
        this.clickImp = clickImp;
    }


    @NonNull
    @Override
    public InsulinInfusionPlanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_insulin_infusion_plan, parent, false);
        return new InsulinInfusionPlanAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull InsulinInfusionPlanAdapter.ViewHolder holder, int position) {
        holder.tvTime.setText(list.get(position).getAddtime());
        holder.tvValues.setText(list.get(position).getValue());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime;
        TextView tvValues;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_insulin_infusion_plan_time);
            tvValues = itemView.findViewById(R.id.tv_insulin_infusion_plan_values);
        }
    }

}
