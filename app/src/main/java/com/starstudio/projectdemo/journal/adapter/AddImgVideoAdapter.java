package com.starstudio.projectdemo.journal.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.text.method.CharacterPickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.entity.LocalMedia;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.utils.DisplayMetricsUtil;
import com.starstudio.projectdemo.utils.OtherUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        this.data.add(null);
        this.clickListener = clickListener;
    }

    public void append(List<String> append) {
        data.addAll(append);
        this.notifyDataSetChanged();
    }

    public void reset(List<String> data) {
        this.data = new ArrayList();
        this.data.add(null);
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    public String[] getDataArray() {
        if (data.size() == 1)
            return null;
        String[] res = new String[data.size() - 1];
        for (int i = 0; i < res.length; i ++)
            res[i] = data.get(i + 1);  // 去掉data中第一个占位图
        return res;
    }

    public ArrayList<String> getData(){
        return data;
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull AddImgVideoAdapter.AddHolder holder, int position) {
        holder.loadData(this.data.get(data.size() - 1 - position));
        holder.setClickListener(clickListener, position);
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
            this.imgView.getLayoutParams().height = DisplayMetricsUtil.getDisplayWidthPxiels((Activity) itemView.getContext()) / 3;
        }

        protected void loadData(String data) {
            if (data == null) {
                imgView.setImageResource(R.drawable.add_big);
                imgView.setTag(ItemType.FIRST);
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(data);
                // 预览时将bitmap切成正方形来美化界面
                bitmap = OtherUtil.scaleSquare(bitmap);
                imgView.setImageBitmap(bitmap);
                imgView.setTag(ItemType.OTHER);
            }


        }

        protected void setClickListener(OnItemClickListener clickListener, int position) {
            this.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, position);
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
        void onItemClick(View view, int position);
    }
}




