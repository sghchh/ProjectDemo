package com.starstudio.projectdemo.account.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.account.data.AccoData;
import com.starstudio.projectdemo.account.data.AccoEntity;
import com.starstudio.projectdemo.utils.OtherUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


/**
 *  “记账”页面“账单”板块的适配器
 */
public class AccoAdapter extends RecyclerView.Adapter<AccoAdapter.AccoHolder> {

    private  ArrayList<AccoData> mData;

    public AccoAdapter(ArrayList<AccoData> data){
        mData = data;
    }

    @NonNull
    @NotNull
    @Override
    public AccoHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acco_item,parent,false);
        return new AccoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AccoAdapter.AccoHolder holder, int position) {
        Log.e(getClass().getSimpleName(), "mData.get(position) = " + mData.get(position));
        holder.loadData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        Log.e(getClass().getSimpleName(),"mData.size() = " + mData.size());
        return mData.size();
    }


    static class AccoHolder extends RecyclerView.ViewHolder{

        private final RecyclerView recyclerDaily;
        private final TextView tvDailyDate, tvCount;

        public AccoHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            recyclerDaily = (RecyclerView)itemView.findViewById(R.id.recycler_daily);
            tvDailyDate = itemView.findViewById(R.id.tv_daily_date);
            tvCount = itemView.findViewById(R.id.tv_count);
        }

        private void loadData(AccoData data){
            this.recyclerDaily.setAdapter(new AccoDailyAdapter(data));
            this.recyclerDaily.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            tvDailyDate.setText(data.getmMonth() + "月" + data.getmDay() + "日");
            if(data.getmCount().charAt(0) != '-'){
                if(!data.getmCount().equals("0")){
                    tvCount.setTextColor(Color.parseColor("#ABFF3E3E"));
                    tvCount.setText("+" + data.getmCount());
                }else{
                    tvCount.setText(data.getmCount());
                }
            }else{
                tvCount.setText(data.getmCount());
            }

        }
    }

}
