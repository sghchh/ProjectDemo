package com.starstudio.projectdemo.journal.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.starstudio.projectdemo.journal.data.JournalEntity;
import com.starstudio.projectdemo.utils.ContextHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * created by sgh
 * 2021-7-31
 * “心情日记”页面“日记”板块的适配器
 */
public class JourAdapter extends RecyclerView.Adapter<JourAdapter.JourHolder>{

    private final List<JournalEntity> data = new ArrayList();
    private OnJourItemClickListener listener;

    public JourAdapter() { }

    public void reset(List<JournalEntity> append) {
        this.data.clear();
        this.data.addAll(append);
        this.notifyDataSetChanged();
    }

    public void append(JournalEntity append) {
        this.data.add(append);
        this.notifyDataSetChanged();
    }

    public void setListener(OnJourItemClickListener listener) {
        this.listener = listener;
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
        holder.loadData(data.get(position), listener);
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class JourHolder extends RecyclerView.ViewHolder {

        // 该RecyclerView是展示图片的控件
        private final RecyclerView imgGrid;
        private final TextView week, date, location, content;
        private final ImageView weather;
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
        private void loadData(JournalEntity data, OnJourItemClickListener listener) {
            this.date.setText(data.getMonth());
            this.location.setText(data.getLocation());
            this.content.setText(data.getContent());
            this.week.setText(data.getWeek());

            // 计算所需要的列数，不同情境为：1/2/3列
            int coloum = Math.min(data.getPictureArray().size(), 3);
            if (coloum == 0) {
                this.imgGrid.setVisibility(View.GONE);
                return;
            } else {
                this.imgGrid.setVisibility(View.VISIBLE);
            }


            this.imgGrid.setNestedScrollingEnabled(false);
            this.imgGrid.setAdapter(new ImgsAdapter(data.getPictureArray()));
            this.imgGrid.setLayoutManager(new GridLayoutManager(ContextHolder.context(), coloum, LinearLayoutManager.VERTICAL, false));
            this.imgGrid.addItemDecoration(new RecyclerGridDivider(10));
            this.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onJourItemClick(view, data);
                }
            });

            this.imgGrid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onJourItemClick(view, data);
                }
            });
        }
    }

    public static interface OnJourItemClickListener{
        void onJourItemClick(View view, JournalEntity data);
    }
}
