package com.starstudio.projectdemo.account.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.hms.image.vision.B;
import com.starstudio.projectdemo.R;

import org.jetbrains.annotations.NotNull;

/**
 *  “记账”页面“预算”板块的适配器
 */
public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetHolder> {

    @NonNull
    @NotNull
    @Override
    public BudgetHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_item,parent,false);
        return new BudgetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BudgetAdapter.BudgetHolder holder, int position) {
        holder.loadData(position);
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    static class BudgetHolder extends RecyclerView.ViewHolder{

        public BudgetHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        private void loadData(int position){

        }
    }

}
