package com.starstudio.projectdemo.journal.adapter;

import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.SUNNY;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.journal.GlideEngine;
import com.starstudio.projectdemo.journal.data.JournalEntity;
import com.starstudio.projectdemo.utils.ContextHolder;
import com.starstudio.projectdemo.utils.OtherUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journal, parent, false);
        JourHolder holder = new JourHolder(view);
        holder.setListener(listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull JourAdapter.JourHolder holder, int position) {
        holder.loadData(data.get(position));
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class JourHolder extends RecyclerView.ViewHolder {
        private OnJourItemClickListener listener;
        private View layout;
        // 该RecyclerView是展示图片的控件
        private final RecyclerView imgGrid;
        private final TextView week, date, location, content, videoNullTxt;
        private final ImageView weather, videoImg, videoPlayer;
        private final FrameLayout videoRoot, audioRoot;
        public JourHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            layout = itemView;
            imgGrid = (RecyclerView) itemView.findViewById(R.id.imgs_recycler);
            week = (TextView)itemView.findViewById(R.id.week);
            date = (TextView)itemView.findViewById(R.id.date);
            weather = (ImageView) itemView.findViewById(R.id.weather);
            content = (TextView)itemView.findViewById(R.id.content);
            location = (TextView)itemView.findViewById(R.id.location);

            videoImg = (ImageView)itemView.findViewById(R.id.journal_video_pre);
            videoRoot = (FrameLayout) itemView.findViewById(R.id.journal_video_root);
            videoPlayer = (ImageView)itemView.findViewById(R.id.journal_video_play);
            videoNullTxt = (TextView)itemView.findViewById(R.id.journal_video_null_txt);

            audioRoot = (FrameLayout)itemView.findViewById(R.id.journal_audio_root);
//            audioNullTxt = (TextView)itemView.findViewById(R.id.journal_audio_null_txt);
        }

        public void setListener(OnJourItemClickListener listener) {
            this.listener = listener;
        }
        // 通过data数据来加载控件内容
        private void loadData(JournalEntity data) {
            this.date.setText(data.getMonth());
            this.location.setText(data.getLocation());
            this.content.setText(data.getContent());
            this.week.setText(data.getWeek());
            this.weather.setImageDrawable(weather.getContext().getDrawable(OtherUtil.weatherId2Mipmap.getOrDefault(data.getWeather(), SUNNY)));

            this.videoRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onJourItemClick(v, data);
                }
            });

            this.imgGrid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onJourItemClick(v, data);
                }
            });

//            this.audioRoot.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onJourItemClick(v, data);
//                }
//            });

            // 计算所需要的列数，不同情境为：1/2/3列
            int coloum = Math.min(data.getPictureArray().size(), 3);
            if (coloum == 0) {
                this.imgGrid.setVisibility(View.GONE);
            } else {
                this.imgGrid.setVisibility(View.VISIBLE);
                this.imgGrid.setNestedScrollingEnabled(false);
                this.imgGrid.setAdapter(new ImgsAdapter(data.getPictureArray()));
                this.imgGrid.setLayoutManager(new GridLayoutManager(ContextHolder.context(), coloum, LinearLayoutManager.VERTICAL, false));
                this.imgGrid.addItemDecoration(new RecyclerGridDivider(10));
            }

            if (data.getVideo() == null) {
                videoRoot.setVisibility(View.GONE);
            } else {
                videoRoot.setVisibility(View.VISIBLE);
                File file = new File(data.getVideo());
                if (file.exists()) {
                    // 说明本地的视频没有被删除，或者没有被移动位置
                    videoRoot.setEnabled(true);
                    GlideEngine.createGlideEngine().loadImage(videoImg.getContext(), data.getVideo(), videoImg);
                    videoPlayer.setVisibility(View.VISIBLE);
                    videoNullTxt.setVisibility(View.GONE);
                }
                else {
                    videoRoot.setEnabled(false);
                    Glide.with(videoImg.getContext()).load(R.drawable.pathnull).into(videoImg);
                    videoPlayer.setVisibility(View.GONE);
                    videoNullTxt.setVisibility(View.VISIBLE);
                }

            }

//            if(data.getAudio() == null){
//                audioRoot.setVisibility(View.GONE);
//            }else{
//                audioRoot.setVisibility(View.VISIBLE);
//                File fileAudio = new File(data.getAudio());
//                if (fileAudio.exists()) {
//                    // 说明本地的视频没有被删除，或者没有被移动位置
//                    audioRoot.setEnabled(true);
//                    audioRoot.setVisibility(View.GONE);
//                }
//                else {
//                    audioRoot.setEnabled(false);
//                    audioRoot.setVisibility(View.GONE);
//                    audioNullTxt.setVisibility(View.VISIBLE);
//                }
//            }

        }
    }

    public static interface OnJourItemClickListener{
        void onJourItemClick(View view, JournalEntity data);
    }
}
