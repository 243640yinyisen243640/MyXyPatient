package com.vice.bloodpressure.adapter.injection;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.bean.injection.InjectionHistoryInfo;
import com.vice.bloodpressure.ui.activity.injection.HealthRecordInjectioneDetailActivity;

import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InjectionHistoryAdapter extends RecyclerView.Adapter<InjectionHistoryAdapter.ViewHolder> {
    private Context context;
//    private String userId;
    private List<InjectionHistoryInfo> listInfos;

    public InjectionHistoryAdapter(Context context, List<InjectionHistoryInfo> listInfos) {
        this.context = context;
//        this.userId = userId;
        this.listInfos = listInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout._item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTime.setText(listInfos.get(position).getAction_time() + "执行");
        holder.tvName.setText(listInfos.get(position).getPlan_name());
        holder.tvNum.setText(listInfos.get(position).getAction_day()+"");
        holder.ivIsHistory.setVisibility(listInfos.get(position).getIsuse() == 1 ? View.VISIBLE : View.GONE);
//        holder.tvDetail.setVisibility(listInfos.get(position).getIsuse() == 1 ? View.VISIBLE : View.GONE);
        holder.tvDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, HealthRecordInjectioneDetailActivity.class);
            intent.putExtra("action_time", listInfos.get(position).getAction_time());
            intent.putExtra("isuse", listInfos.get(position).getIsuse() + "");
            context.startActivity(intent);
        });
        if (position == 0) {
            holder.llTopLine.setVisibility(View.GONE);
            holder.llIconTop.setVisibility(View.VISIBLE);
        } else {
            holder.llTopLine.setVisibility(View.VISIBLE);
            holder.llIconTop.setVisibility(View.GONE);
        }
        if (position == listInfos.size() - 1) {
            holder.llBottomLine.setVisibility(View.INVISIBLE);
        } else {
            holder.llBottomLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return listInfos.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llTopLine;
        LinearLayout llIconTop;
        LinearLayout llBottomLine;
        TextView tvTime;
        TextView tvName;
        TextView tvNum;
        TextView tvDetail;
        ImageView ivIsHistory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llTopLine = itemView.findViewById(R.id.ll_history_line_top);
            llIconTop = itemView.findViewById(R.id.ll_history_icon_top);
            llBottomLine = itemView.findViewById(R.id.ll_history_line_bottom);
            tvTime = itemView.findViewById(R.id.tv_history_time);
            tvName = itemView.findViewById(R.id.tv_history_name);
            tvNum = itemView.findViewById(R.id.tv_history_num);
            tvDetail = itemView.findViewById(R.id.tv_history_detail);
            ivIsHistory = itemView.findViewById(R.id.iv_history_isHistory);
        }
    }
}
