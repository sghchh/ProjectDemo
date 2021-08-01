package com.starstudio.projectdemo.journal.adapter;

import android.content.ClipData;
import android.media.Image;
import android.text.method.CharacterPickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starstudio.projectdemo.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * created by sgh
 * 2021-8-1
 * 写日记板块中添加图片对应的适配器
 */
public class AddImgVideoAdapter extends RecyclerView.Adapter<AddImgVideoAdapter.AddHolder> {

    private ArrayList<String> data;
    private OnItemClickListener clickListener;

    public AddImgVideoAdapter(OnItemClickListener clickListener) {
        this.data = new ArrayList<>();
        this.data.add("first");
        this.clickListener = clickListener;
    }

    public void append(String... append) {
        data.addAll(Arrays.asList(append));
        this.notifyDataSetChanged();
    }

    public void reset(String... data) {
        this.data = new ArrayList();
        this.data.add("first");
        this.data.addAll(Arrays.asList(data));
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AddImgVideoAdapter.AddHolder holder, int position) {
        holder.loadData(this.data.get(data.size() - 1 - position));
        holder.setClickListener(clickListener);
    }

    @NonNull
    @NotNull
    @Override
    public AddHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_img_video_item, parent, false);
        return new AddHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected static class AddHolder extends RecyclerView.ViewHolder {

        private final ImageView imgView;
        public AddHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.imgView = (ImageView) itemView.findViewById(R.id.add_img_video);
        }

        protected void loadData(String data) {
            if (data.equals("first")) {
                imgView.setImageResource(R.drawable.add_big);
                imgView.setTag(ItemType.FIRST);
            } else {
                imgView.setImageResource(R.drawable.weather_overcast);
                imgView.setTag(ItemType.OTHER);
            }


        }

        protected void setClickListener(OnItemClickListener clickListener) {
            this.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view);
                }
            });
        }
    }

    /**
     * 枚举类型
     * 表示item类型
     */
    public static enum ItemType {
        FIRST(), OTHER();
    }

    /**
     * RecyclerView中item的点击事件
     */
    public static interface OnItemClickListener {
        void onItemClick(View view);
    }
}




