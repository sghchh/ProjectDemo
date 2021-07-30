package com.starstudio.projectdemo.journal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starstudio.projectdemo.R;

import org.jetbrains.annotations.NotNull;

public class JourAdapter extends RecyclerView.Adapter<JourAdapter.JourHolder>{


    public static class JourHolder extends RecyclerView.ViewHolder {

        public JourHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @NotNull
    @Override
    public JourAdapter.JourHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_item, parent, false);
        return new JourHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull JourAdapter.JourHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return 5;
    }
}
