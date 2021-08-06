package com.starstudio.projectdemo.account.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starstudio.projectdemo.R;

import org.jetbrains.annotations.NotNull;

public class AccoDailyAdapter extends RecyclerView.Adapter<AccoDailyAdapter.AccoDailyHolder> {

    @NonNull
    @NotNull
    @Override
    public AccoDailyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acco_daily_item,parent,false);
        return new AccoDailyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AccoDailyAdapter.AccoDailyHolder holder, int position) {
        holder.loadData();
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    static class AccoDailyHolder extends RecyclerView.ViewHolder{

        public AccoDailyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        private void loadData(){

        }
    }
}
