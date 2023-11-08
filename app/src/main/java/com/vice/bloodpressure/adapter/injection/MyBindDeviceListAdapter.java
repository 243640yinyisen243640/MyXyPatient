package com.vice.bloodpressure.adapter.injection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.bean.injection.InjectionBaseInfo;
import com.vice.bloodpressure.imp.AdapterClickImp;

import java.util.List;

/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class MyBindDeviceListAdapter extends RecyclerView.Adapter<MyBindDeviceListAdapter.ViewHolder> {
    private Context context;
    private List<InjectionBaseInfo> listInfos;
    private AdapterClickImp listener;

    public MyBindDeviceListAdapter(Context context, List<InjectionBaseInfo> listInfos, AdapterClickImp listener) {
        this.context = context;
        this.listInfos = listInfos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_bind_device_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.logoImg.setImageResource(listInfos.get(position).getImg());
        holder.nameTv.setText(listInfos.get(position).getText());
        holder.clickLin.setOnClickListener(v -> {
            listener.onAdapterClick(v, position);
        });
    }

    @Override
    public int getItemCount() {
        return listInfos.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout clickLin;
        ImageView logoImg;
        TextView nameTv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clickLin = itemView.findViewById(R.id.ll_my_device_list_click);
            logoImg = itemView.findViewById(R.id.img_device);
            nameTv = itemView.findViewById(R.id.tv_name);
        }
    }
}
