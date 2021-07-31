package com.starstudio.projectdemo.journal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.journal.data.JourData;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

/**
 * created by sgh
 * 2021-7-31
 * “心情日记”页面“日记”板块的适配器
 */
public class JourAdapter extends RecyclerView.Adapter<JourAdapter.JourHolder>{

    private JourData[] data;
    private Context context;

    public JourAdapter(JourData[] data) {
        this.data = data;
    }

    private void setContext(Context context) {
        if(this.context == null)
            this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public JourAdapter.JourHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_item, parent, false);
        setContext(context);
        return new JourHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull JourAdapter.JourHolder holder, int position) {
        holder.loadData(data[position], context);
    }



    @Override
    public int getItemCount() {
        return data.length;
    }

    public static class JourHolder extends RecyclerView.ViewHolder {

        // 该RecyclerView是展示图片的控件
        private RecyclerView imgGrid;
        private TextView week, date, location, content;
        private ImageView weather;
        public JourHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgGrid = (RecyclerView) itemView.findViewById(R.id.imgs_recycler);
            week = (TextView)itemView.findViewById(R.id.week);
            date = (TextView)itemView.findViewById(R.id.date);
            weather = (ImageView) itemView.findViewById(R.id.weather);
            content = (TextView)itemView.findViewById(R.id.content);
            location = (TextView)itemView.findViewById(R.id.location);
        }

        // 通过data数据来加载控件内容
        private void loadData(JourData data, Context context) {
            this.date.setText(data.getDate());
            this.location.setText(data.getLoaction());
            this.content.setText(data.getContent());
            this.week.setText(data.getWeek());

            int coloum = Math.min(data.getImgs().length, 3);
            this.imgGrid.setAdapter(new ImgsAdapter(data.getImgs()));
            this.imgGrid.setLayoutManager(new GridLayoutManager(context, coloum, LinearLayoutManager.VERTICAL, false));
        }
    }
}
