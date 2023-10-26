package com.vice.bloodpressure.adapter.injection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vice.bloodpressure.R;
import com.vice.bloodpressure.bean.injection.InjectDetailInfo;
import com.vice.bloodpressure.bean.injection.InjectionDataListInfo;
import com.vice.bloodpressure.ui.activity.injection.HealthRecordInjectioneInfoActivity;

import java.util.List;


/**
 * 作者: beauty
 * 类名:
 * 传参:
 * 描述:
 */
public class InjectionAdapter extends RecyclerView.Adapter<InjectionAdapter.ViewHolder> {
    private Context context;
    private List<InjectionDataListInfo> listInfos;

    public InjectionAdapter(Context context, List<InjectionDataListInfo> listInfos) {
        this.context = context;
        this.listInfos = listInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout._item_shujv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvDay.setText(listInfos.get(position).getDate() + "");
        if (position % 2 == 0) {
            holder.llBg.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        } else {
            holder.llBg.setBackgroundColor(Color.parseColor("#F7F7F7"));
        }
        int plan_num = listInfos.get(position).getPlan_num();
        setDefStyle(holder);

        if (plan_num > 0) {
            int value = listInfos.get(position).getJections().get(0).getValue();
            int more = listInfos.get(position).getJections().get(0).getMore();
            int ishight = listInfos.get(position).getJections().get(0).getIshight();

            if (value == 0) {
                //缺
                holder.tvOne.setText("缺");
                holder.tvOne.setTextColor(Color.parseColor("#BD3620"));
                holder.tvOne.setBackgroundColor(Color.parseColor("#F9EBEA"));
            } else {
                holder.tvOne.setText(value + "");
                holder.tvOne.setOnClickListener(v -> {
                    jumpDetail(position,0,"第一针");
                });
                if (more > 0) {
                    //多个
                    holder.ivOneMultiple.setVisibility(View.VISIBLE);
                } else {
                    holder.ivOneMultiple.setVisibility(View.GONE);
                }
                if (ishight == 1) {
                    //偏高
                    holder.tvOne.setTextColor(Color.parseColor("#BD3620"));
                    holder.tvOne.setBackgroundColor(Color.parseColor("#F9EBEA"));
                    holder.tvOneState.setText("偏高");
                    holder.tvOneState.setVisibility(View.VISIBLE);
                    holder.tvOneState.setBackground(context.getDrawable(R.drawable._6));
                } else if (ishight == 2) {
                    //偏低
                    holder.tvOne.setTextColor(Color.parseColor("#D68A2C"));
                    holder.tvOne.setBackgroundColor(Color.parseColor("#F4EAE1"));
                    holder.tvOneState.setText("偏低");
                    holder.tvOneState.setVisibility(View.VISIBLE);
                    holder.tvOneState.setBackground(context.getDrawable(R.drawable._7));
                } else {
                    holder.tvOneState.setVisibility(View.GONE);
                }
            }
        }

        if (plan_num > 1) {
            int value = listInfos.get(position).getJections().get(1).getValue();
            int more = listInfos.get(position).getJections().get(1).getMore();
            int ishight = listInfos.get(position).getJections().get(1).getIshight();

            if (value == 0) {
                //缺
                holder.tvTwo.setText("缺");
                holder.tvTwo.setTextColor(Color.parseColor("#BD3620"));
                holder.tvTwo.setBackgroundColor(Color.parseColor("#F9EBEA"));
            } else {
                holder.tvTwo.setText(value + "");
                holder.tvTwo.setOnClickListener(v -> {
                    jumpDetail(position,1,"第二针");
                });
                if (more > 0) {
                    //多个
                    holder.ivTwoMultiple.setVisibility(View.VISIBLE);
                } else {
                    holder.ivTwoMultiple.setVisibility(View.GONE);
                }
                if (ishight == 1) {
                    //偏高
                    holder.tvTwo.setTextColor(Color.parseColor("#BD3620"));
                    holder.tvTwo.setBackgroundColor(Color.parseColor("#F9EBEA"));
                    holder.tvTwoState.setText("偏高");
                    holder.tvTwoState.setVisibility(View.VISIBLE);
                    holder.tvTwoState.setBackground(context.getDrawable(R.drawable._6));
                } else if (ishight == 2) {
                    //偏低
                    holder.tvTwo.setTextColor(Color.parseColor("#D68A2C"));
                    holder.tvTwo.setBackgroundColor(Color.parseColor("#F4EAE1"));
                    holder.tvTwoState.setText("偏低");
                    holder.tvTwoState.setVisibility(View.VISIBLE);
                    holder.tvTwoState.setBackground(context.getDrawable(R.drawable._7));
                } else {
                    holder.tvTwoState.setVisibility(View.GONE);
                }
            }
        }

        if (plan_num > 2) {
            int value = listInfos.get(position).getJections().get(2).getValue();
            int more = listInfos.get(position).getJections().get(2).getMore();
            int ishight = listInfos.get(position).getJections().get(2).getIshight();

            if (value == 0) {
                //缺
                holder.tvThree.setText("缺");
                holder.tvThree.setTextColor(Color.parseColor("#BD3620"));
                holder.tvThree.setBackgroundColor(Color.parseColor("#F9EBEA"));
            } else {
                holder.tvThree.setText(value + "");
                holder.tvThree.setOnClickListener(v -> {
                    jumpDetail(position,2,"第三针");
                });
                if (more > 0) {
                    //多个
                    holder.ivThreeMultiple.setVisibility(View.VISIBLE);
                } else {
                    holder.ivThreeMultiple.setVisibility(View.GONE);
                }
                if (ishight == 1) {
                    //偏高
                    holder.tvThree.setTextColor(Color.parseColor("#BD3620"));
                    holder.tvThree.setBackgroundColor(Color.parseColor("#F9EBEA"));
                    holder.tvThreeState.setText("偏高");
                    holder.tvThreeState.setVisibility(View.VISIBLE);
                    holder.tvThreeState.setBackground(context.getDrawable(R.drawable._6));
                } else if (ishight == 2) {
                    //偏低
                    holder.tvThree.setTextColor(Color.parseColor("#D68A2C"));
                    holder.tvThree.setBackgroundColor(Color.parseColor("#F4EAE1"));
                    holder.tvThreeState.setText("偏低");
                    holder.tvThreeState.setVisibility(View.VISIBLE);
                    holder.tvThreeState.setBackground(context.getDrawable(R.drawable._7));
                } else {
                    holder.tvThreeState.setVisibility(View.GONE);
                }
            }
        }

        if (plan_num > 3) {
            int value = listInfos.get(position).getJections().get(3).getValue();
            int more = listInfos.get(position).getJections().get(3).getMore();
            int ishight = listInfos.get(position).getJections().get(3).getIshight();

            if (value == 0) {
                //缺
                holder.tvFour.setText("缺");
                holder.tvFour.setTextColor(Color.parseColor("#BD3620"));
                holder.tvFour.setBackgroundColor(Color.parseColor("#F9EBEA"));
            } else {
                holder.tvFour.setText(value + "");
                holder.tvThree.setOnClickListener(v -> {
                    jumpDetail(position,3,"第四针");
                });
                if (more > 0) {
                    //多个
                    holder.ivFourMultiple.setVisibility(View.VISIBLE);
                } else {
                    holder.ivFourMultiple.setVisibility(View.GONE);
                }
                if (ishight == 1) {
                    //偏高
                    holder.tvFour.setTextColor(Color.parseColor("#BD3620"));
                    holder.tvFour.setBackgroundColor(Color.parseColor("#F9EBEA"));
                    holder.tvFourState.setText("偏高");
                    holder.tvFourState.setVisibility(View.VISIBLE);
                    holder.tvFourState.setBackground(context.getDrawable(R.drawable._6));
                } else if (ishight == 2) {
                    //偏低
                    holder.tvFour.setTextColor(Color.parseColor("#D68A2C"));
                    holder.tvFour.setBackgroundColor(Color.parseColor("#F4EAE1"));
                    holder.tvFourState.setText("偏低");
                    holder.tvFourState.setVisibility(View.VISIBLE);
                    holder.tvFourState.setBackground(context.getDrawable(R.drawable._7));
                } else {
                    holder.tvFourState.setVisibility(View.GONE);
                }
            }
        }
    }

    private void jumpDetail(int position,int index,String numString) {
        InjectDetailInfo info = new InjectDetailInfo();
        info.setDatetime(listInfos.get(position).getDatetime());
        info.setNum(numString);
        info.setValue(listInfos.get(position).getJections().get(index).getValue() + "");
        info.setIshight(listInfos.get(position).getJections().get(index).getIshight() + "");
        info.setDataList(listInfos.get(position).getJections().get(index).getJection_data());
        Intent intent = new Intent(context, HealthRecordInjectioneInfoActivity.class);
        intent.putExtra("data",info);
        context.startActivity(intent);
    }

    private void setDefStyle(ViewHolder holder) {
        holder.tvOne.setText("/");
        holder.tvOne.setOnClickListener(v -> {
        });
        holder.tvOne.setTextColor(Color.parseColor("#333333"));
        holder.tvOne.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        holder.tvOneState.setVisibility(View.GONE);
        holder.ivOneMultiple.setVisibility(View.GONE);

        holder.tvTwo.setText("/");
        holder.tvTwo.setOnClickListener(v -> {
        });
        holder.tvTwo.setTextColor(Color.parseColor("#333333"));
        holder.tvTwo.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        holder.tvTwoState.setVisibility(View.GONE);
        holder.ivTwoMultiple.setVisibility(View.GONE);

        holder.tvThree.setText("/");
        holder.tvThree.setOnClickListener(v -> {
        });
        holder.tvThree.setTextColor(Color.parseColor("#333333"));
        holder.tvThree.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        holder.tvThreeState.setVisibility(View.GONE);
        holder.ivThreeMultiple.setVisibility(View.GONE);

        holder.tvFour.setText("/");
        holder.tvFour.setOnClickListener(v -> {
        });
        holder.tvFour.setTextColor(Color.parseColor("#333333"));
        holder.tvFour.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        holder.tvFourState.setVisibility(View.GONE);
        holder.ivFourMultiple.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return listInfos.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay;
        LinearLayout llBg;

        TextView tvOne;
        TextView tvTwo;
        TextView tvThree;
        TextView tvFour;
        TextView tvOneState;
        TextView tvTwoState;
        TextView tvThreeState;
        TextView tvFourState;
        ImageView ivOneMultiple;
        ImageView ivTwoMultiple;
        ImageView ivThreeMultiple;
        ImageView ivFourMultiple;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_injection_item_day);
            llBg = itemView.findViewById(R.id.ll_injection_item_bg);

            tvOne = itemView.findViewById(R.id.tv_item_injection_one);
            tvTwo = itemView.findViewById(R.id.tv_item_injection_two);
            tvThree = itemView.findViewById(R.id.tv_item_injection_three);
            tvFour = itemView.findViewById(R.id.tv_item_injection_four);

            tvOneState = itemView.findViewById(R.id.tv_item_injection_state_one);
            tvTwoState = itemView.findViewById(R.id.tv_item_injection_state_two);
            tvThreeState = itemView.findViewById(R.id.tv_item_injection_state_three);
            tvFourState = itemView.findViewById(R.id.tv_item_injection_state_four);

            ivOneMultiple = itemView.findViewById(R.id.tv_item_injection_multiple_one);
            ivTwoMultiple = itemView.findViewById(R.id.tv_item_injection_multiple_two);
            ivThreeMultiple = itemView.findViewById(R.id.tv_item_injection_multiple_three);
            ivFourMultiple = itemView.findViewById(R.id.tv_item_injection_multiple_four);
        }
    }
}
