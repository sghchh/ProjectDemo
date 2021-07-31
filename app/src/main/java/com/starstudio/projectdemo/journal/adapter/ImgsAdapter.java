package com.starstudio.projectdemo.journal.adapter;

import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starstudio.projectdemo.R;

import org.jetbrains.annotations.NotNull;

/**
 * created by sgh
 * 2021-7-31
 * “心情日记”页面中“日记”板块展示图片的RecyclerView对应的适配器
 */
public class ImgsAdapter extends RecyclerView.Adapter<ImgsAdapter.ImgHolder> {

    String[] data;

    public ImgsAdapter (String[] data) {
        this.data = data;
    }
    @NonNull
    @NotNull
    @Override
    public ImgHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
        return new ImgHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImgsAdapter.ImgHolder holder, int position) {
        if (data.length > 9 && position == 8)
            holder.loadData(data[position], true);
        else
            holder.loadData(data[position], false);
    }

    @Override
    public int getItemCount() {
        return Math.min(data.length, 9);
    }

    public static class ImgHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        public ImgHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
        }

        protected void loadData(String data, boolean mask) {
            if (mask)
                img.setImageResource(R.drawable.add);
            else
                img.setImageResource(R.drawable.weather_overcast);
        }
    }
}
