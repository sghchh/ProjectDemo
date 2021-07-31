package com.starstudio.projectdemo.journal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starstudio.projectdemo.R;

import org.jetbrains.annotations.NotNull;

/**
 * created by sgh
 * 2021-7-31
 * “心情日记”页面的“相册”板块的适配器
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumHolder> {

    @NonNull
    @NotNull
    @Override
    public AlbumHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
        return new AlbumHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AlbumAdapter.AlbumHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class AlbumHolder extends RecyclerView.ViewHolder {

        public AlbumHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }
}
