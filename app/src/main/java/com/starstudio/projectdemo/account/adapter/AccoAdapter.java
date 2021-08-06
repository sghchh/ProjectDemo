package com.starstudio.projectdemo.account.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.account.data.AccoData;

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
        holder.loadData(null);
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    static class AccoHolder extends RecyclerView.ViewHolder{

        public AccoHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        private void loadData(AccoData data){

        }
    }

}
