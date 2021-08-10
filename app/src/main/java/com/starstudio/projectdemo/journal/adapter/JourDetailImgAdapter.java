package com.starstudio.projectdemo.journal.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.utils.OtherUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JourDetailImgAdapter extends RecyclerView.Adapter<JourDetailImgAdapter.DetailImgHolder> {

    private List<String> pictures;
    private OnDetailItemClickListener listener;

    public JourDetailImgAdapter(List<String> data) {
        this.pictures = data;
        Log.e("JourDetailImgAdapter", "JourDetailImgAdapter: 详情页面获取到的图片为"+new Gson().toJson(data));
    }

    public void setItemClickListener(OnDetailItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public DetailImgHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_detail_image_item, parent, false);
        return new DetailImgHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull JourDetailImgAdapter.DetailImgHolder holder, int position) {
        holder.loadData(pictures.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }


    static class DetailImgHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        DetailImgHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.journal_detail_img);
        }

        protected void loadData(String path, OnDetailItemClickListener listener) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(OtherUtil.scaleSquare(bitmap));
            imageView.setAdjustViewBounds(true);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(view, path);
                }
            });
        }
    }

    public static interface OnDetailItemClickListener{
        public void onItemClick(View v, String picture);
    }
}

