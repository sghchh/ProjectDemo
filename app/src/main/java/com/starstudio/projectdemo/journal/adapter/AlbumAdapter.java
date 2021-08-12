package com.starstudio.projectdemo.journal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.journal.data.AlbumData;
import com.starstudio.projectdemo.utils.FileUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

/**
 * created by sgh
 * 2021-7-31
 * “心情日记”页面的“相册”板块的适配器
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumHolder> {
    private List<AlbumData> data;
    private AlbumItemClickListener listener;

    public AlbumAdapter() {
        this.data = FileUtil.searchAlbumData();
    }

    public void setListener(AlbumItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public AlbumHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        AlbumHolder holder = new AlbumHolder(view);
        holder.setListener(this.listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AlbumAdapter.AlbumHolder holder, int position) {
        holder.loadData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class AlbumHolder extends RecyclerView.ViewHolder {
        private AlbumData mdata;
        private ImageView cover;
        private TextView name;
        private View layout;

        public AlbumHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            layout = itemView;
            cover = (ImageView)itemView.findViewById(R.id.img);
            name = (TextView)itemView.findViewById(R.id.album_name);
        }

        public void loadData(AlbumData data) {
            mdata = data;
            File pic = new File(data.getCover());
            Glide.with(cover).load(pic).override(200,200).centerCrop().into(cover);
            name.setText(data.getName());
        }

        public void setListener(AlbumItemClickListener listener) {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnAlbumItemClick(v, mdata);
                }
            });
        }
    }

    public static interface AlbumItemClickListener {
        void OnAlbumItemClick(View view, AlbumData data);
    }
}
