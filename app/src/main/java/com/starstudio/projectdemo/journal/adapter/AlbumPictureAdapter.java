package com.starstudio.projectdemo.journal.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.utils.OtherUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class AlbumPictureAdapter extends RecyclerView.Adapter<AlbumPictureAdapter.AlbumPictureHolder> {
    private String[] paths;
    public AlbumPictureAdapter(String[] paths) {
        this.paths = paths;
    }

    @NonNull
    @NotNull
    @Override
    public AlbumPictureHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_picture_item, parent, false);
        return new AlbumPictureHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AlbumPictureAdapter.AlbumPictureHolder holder, int position) {
        holder.loadData(paths[position]);
    }

    @Override
    public int getItemCount() {
        return paths.length;
    }

    static class AlbumPictureHolder extends RecyclerView.ViewHolder {
        private ImageView pic;
        public AlbumPictureHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.album_picture_img);
        }

        public void loadData(String path) {
            File p = new File(path);
            Glide.with(pic).load(p).override(200,200).centerCrop().into(pic);
            //Bitmap bitmap = BitmapFactory.decodeFile(path);
            //pic.setImageBitmap(OtherUtil.scaleSquare(bitmap));
            pic.setAdjustViewBounds(true);
        }
    }
}
