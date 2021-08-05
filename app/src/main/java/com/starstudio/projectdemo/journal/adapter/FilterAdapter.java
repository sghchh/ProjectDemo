package com.starstudio.projectdemo.journal.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.journal.api.HmsImageService;

import org.jetbrains.annotations.NotNull;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder> {

    private final String[] data;
    public FilterAdapter(String[] data) {
        this.data = data;
    }
    @NonNull
    @NotNull
    @Override
    public FilterHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item, parent, false);
        return new FilterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FilterAdapter.FilterHolder holder, int position) {
        holder.loadData(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    static class FilterHolder extends RecyclerView.ViewHolder {

        private TextView txt;
        public FilterHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.filter_item_txt);
        }

        void loadData(String data) {
            txt.setText(data);
            txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        txt.setTextColor(Color.WHITE);
                        txt.setTextSize(19);
                    } else {
                        txt.setTextColor(Color.GRAY);
                        txt.setTextSize(18);
                    }
                }
            });
        }
    }
}
