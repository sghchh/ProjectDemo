package com.starstudio.projectdemo.account.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        private final TextView tvKind;

        public BudgetHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvKind = itemView.findViewById(R.id.tv_kind);
        }

        private void loadData(int position){
            switch (position){
                case 0:
                    tvKind.setText("饮食");
                    break;
                case 1:
                    tvKind.setText("学习进修");
                    break;
                case 2:
                    tvKind.setText("衣服饰品");
                    break;
                case 3:
                    tvKind.setText("通讯交通");
                    break;
                case 4:
                    tvKind.setText("休闲娱乐");
                    break;
                case 5:
                    tvKind.setText("其他项目");
                    break;

            }
        }
    }

}
