package com.starstudio.projectdemo.journal.adapter;

import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.utils.ContextHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * created by sgh
 * 2021-7-31
 * “心情日记”页面中“日记”板块展示图片的RecyclerView对应的适配器
 */
public class ImgsAdapter extends RecyclerView.Adapter<ImgsAdapter.ImgHolder> {

    private List<String> data;

    public ImgsAdapter (List<String> data) {
        this.data = data;
    }
    @NonNull
    @NotNull
    @Override
    public ImgHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_item, parent, false);
        return new ImgHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull @NotNull ImgsAdapter.ImgHolder holder, int position) {
        if (data.size() > 9 && position == 8)
            holder.loadData(data.get(position), data.size() - 9);
        else
            holder.loadData(data.get(position), 0);
    }

    @Override
    public int getItemCount() {
        return Math.min(data.size(), 9);
    }

    public void clear() {
        this.data = null;
    }

    public void addData(List<String> data) {
        this.data = data;
    }

    public static class ImgHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView txt;
        public ImgHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            txt = (TextView) itemView.findViewById(R.id.last_count_txt);
        }


        protected void loadData(String data, int last) {
            if (last > 0) {
                img.setImageResource(R.drawable.weather_overcast);
                img.setForeground(ContextHolder.context().getDrawable(R.drawable.img_mask));
                txt.setVisibility(View.VISIBLE);
                txt.setText("+" + last);
            }
            else
                img.setImageResource(R.drawable.weather_overcast);
        }
    }
}
