package com.starstudio.projectdemo.account.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.account.data.AccoData;
import com.starstudio.projectdemo.account.fragments.AccoAddFragment;

import org.jetbrains.annotations.NotNull;

import java.nio.channels.AcceptPendingException;

public class AccoDailyAdapter extends RecyclerView.Adapter<AccoDailyAdapter.AccoDailyHolder> {

    private AccoData mAccoData;

    public AccoDailyAdapter(AccoData data){
        Log.e(getClass().getSimpleName(), "mAccoData = " + mAccoData);
        mAccoData = data;
    }

    @NonNull
    @NotNull
    @Override
    public AccoDailyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acco_daily_item, parent,false);
        return new AccoDailyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AccoDailyAdapter.AccoDailyHolder holder, int position) {
        holder.loadData(mAccoData.getmDailyData().get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new AccoAddFragment(mAccoData.getmDailyData().get(position), mAccoData.getmYear(), mAccoData.getmMonth(), mAccoData.getmDay());
                dialogFragment.show(((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager(),"修改");
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e(getClass().getSimpleName(), "mAccoData.getmDailyData().size() = " + mAccoData.getmDailyData().size());
        return mAccoData.getmDailyData().size();
    }

    static class AccoDailyHolder extends RecyclerView.ViewHolder{
        private final TextView tvKind, tvComment, tvMoney;

        public AccoDailyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvKind = itemView.findViewById(R.id.tv_kind);
            tvComment = itemView.findViewById(R.id.tv_comment);
            tvMoney = itemView.findViewById(R.id.tv_money);
        }

        private void loadData(AccoData.AccoDailyData data){
            tvKind.setText(data.getmKindDetail());
            tvComment.setText(data.getmComment());
            if(data.getmMoney().charAt(0) != '-'){
                if(!data.getmMoney().equals("0")){
                    tvMoney.setTextColor(Color.parseColor("#ABFF3E3E"));
                    tvMoney.setText("+" + data.getmMoney());
                }else{
                    tvMoney.setText(data.getmMoney());
                }
            }else{
                tvMoney.setText(data.getmMoney());
            }
        }
    }
}
