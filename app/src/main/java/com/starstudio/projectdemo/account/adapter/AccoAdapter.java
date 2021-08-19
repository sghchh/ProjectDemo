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

    /**
     * 该方法用于加载 RecyclerView 子项的布局，然后返回一个 ViewHolder 对象**
     * @param parent  可以简单理解为`item`的根`ViewGroup`，`item`的子控件加载在其中
     * @param viewType  `item`的类型，可以根据`viewType`来创建不同的`ViewHolder`，来加载不同的类型的`item`
     * @return
     */
    @NonNull
    @NotNull
    @Override
    public AccoHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acco_item,parent,false);
        return new AccoHolder(view);
    }

    /**
     *  为子项绑定数据
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull AccoAdapter.AccoHolder holder, int position) {
        Log.e(getClass().getSimpleName(), "mData.get(position) = " + mData.get(position));
        holder.loadData(mData.get(position));
    }

    /**
     *
     * @return  设置 RecyclerView 一共有多少子项展示
     */
    @Override
    public int getItemCount() {
        Log.e(getClass().getSimpleName(),"mData.size() = " + mData.size());
        return mData.size();
    }

    /**
     *  设置 RecyclerView 中每一个子项的布局以及数据展示
     *  其构造方法中会传入一个View类型参数，传入的为每一次创建时对应的ItemView（子项）
     */
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
